package nhom12.eauta.cookinginstructions.Controller;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nhom12.eauta.cookinginstructions.Model.Favorite;
import nhom12.eauta.cookinginstructions.Model.Recipe;
import nhom12.eauta.cookinginstructions.Model.Step;
import nhom12.eauta.cookinginstructions.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Activity_RecipeDetail extends AppCompatActivity {
    private TextView tvIngredient, txtNameF, txtTitle, txtDesc, tvSteps,  txtTitleVideo;
    private LinearLayout layoutSteps;
    private FirebaseDatabase database;
    private DatabaseReference recipeRef, favoriteRef;
    ImageView btnThoat, btnThreeDots ;
    WebView webView;
    private String userId;
    private boolean isFavorite = false;  // Biến để theo dõi tình trạng yêu thích
    private String imageUrl;  // Lưu URL hình ảnh công thức
    private int defaultColor;
    private int colorAcc;
    private int colorFavorite;
    private int colorMeoHay;
    private int colorBiQuyet;
    private int textColor;
    private int colorCook;
    private TextView btnAcc, btnFavorite, btnMeoHay, btnBiQuyet, btnCook;
    private String recipeId;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        tvIngredient = findViewById(R.id.tvIngredient);
        tvSteps = findViewById(R.id.tvSteps);
        layoutSteps = findViewById(R.id.layoutSteps);
        txtNameF = findViewById(R.id.txtNameF);
        btnThoat = findViewById(R.id.btnThoat);
        txtDesc = findViewById(R.id.txtDesc);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitleVideo = findViewById(R.id.txtTitleVideo);
        webView = findViewById(R.id.webView);
        btnCook = findViewById(R.id.btnCook);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnMeoHay = findViewById(R.id.btnMeoHay);
        btnBiQuyet = findViewById(R.id.btnBiQuyet);
        btnAcc = findViewById(R.id.btnAcount);
        btnThreeDots = findViewById(R.id.btn_threedots);

        defaultColor = getResources().getColor(R.color.trang);
        colorAcc = getResources().getColor(R.color.hong);
        colorFavorite = getResources().getColor(R.color.xanhla);
        colorMeoHay = getResources().getColor(R.color.hongdam);
        colorBiQuyet = getResources().getColor(R.color.blue);
        textColor = getResources().getColor(R.color.trang);
        colorCook = getResources().getColor(R.color.tim);

        // Lấy userId từ SharedPreferences
        userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("userId", null);

        String recipeId = getIntent().getStringExtra("RecipeId");
        loadRecipeDetails(recipeId);
        checkFavoriteStatus(recipeId);

        // Thiết lập OnClickListener cho btnThreeDots
        btnThreeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị menu tùy chọn
                showMenu(v, recipeId);
            }
        });



        btnThoat.setOnClickListener(v -> finish());
        // check gọi menu để chuyển trang
        // bí quyết
        btnBiQuyet.setOnClickListener(view -> {
            changeButtonColor(btnBiQuyet,colorBiQuyet);
            Intent intent = new Intent(Activity_RecipeDetail.this, Secret.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        // Khi người dùng nhấn nút mẹo hay
        btnMeoHay.setOnClickListener(view -> {
            changeButtonColor(btnMeoHay, colorMeoHay);
            Intent intent = new Intent(Activity_RecipeDetail.this, GoodTips.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        // Khi người dùng nhấn nút tài khoản
        btnAcc.setOnClickListener(view -> {
            changeButtonColor(btnAcc, colorAcc);
            if (userId != null) {
                Intent intent = new Intent(Activity_RecipeDetail.this, UpdateInfor.class);
                intent.putExtra("userId", userId); // Truyền userId qua Intent
                startActivity(intent);
            } else {
                Toast.makeText(Activity_RecipeDetail.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });
        btnCook.setOnClickListener(view -> {
            changeButtonColor(btnCook, colorCook);
            Intent intent = new Intent(Activity_RecipeDetail.this, Activity_Home.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        btnFavorite.setOnClickListener(view -> {
            changeButtonColor(btnFavorite, colorFavorite);
            Intent intent = new Intent(Activity_RecipeDetail.this, Activity_Favorite.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
    }
    // Hàm hiển thị menu tùy chọn
    private void showMenu(View v, String recipeId) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_share_tym, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.btnShare) {
                    shareRecipe();
                    return true;
                } else if (item.getItemId() == R.id.btnTym) {
                    addRecipeToFavorites(recipeId);
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }

    // Hàm chia sẻ công thức
    private void shareRecipe() {
        // Lấy nội dung cần chia sẻ
        String title = txtTitle.getText().toString();
        String description = txtDesc.getText().toString();
        String ingredients = tvIngredient.getText().toString();


        // Kiểm tra xem dữ liệu đã được load đầy đủ chưa
        if (title.isEmpty() || description.isEmpty() || ingredients.isEmpty()) {
            Toast.makeText(Activity_RecipeDetail.this, "Vui lòng đợi nội dung được tải đầy đủ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo nội dung chia sẻ
        String shareContent = title + "\n\n" + description + "\n\nNguyên liệu:\n" + ingredients + "\n\nCác bước làm:\n";

        // Nếu có hình ảnh, thêm vào nội dung chia sẻ
        if (imageUrl != null && !imageUrl.isEmpty()) {
            shareContent += "\n\nHình ảnh món ăn: " + imageUrl;
        }

        // Tạo Intent chia sẻ
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Chia sẻ công thức món ăn");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);

        // Kiểm tra xem có ứng dụng nào hỗ trợ chia sẻ không
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(shareIntent, "Chia sẻ công thức qua"));
        } else {
            Toast.makeText(Activity_RecipeDetail.this, "Không có ứng dụng nào hỗ trợ chia sẻ", Toast.LENGTH_SHORT).show();
        }

    }

    // Hàm kiểm tra xem công thức đã có trong danh sách yêu thích chưa
    private void checkFavoriteStatus(String recipeId) {
        database = FirebaseDatabase.getInstance();
        favoriteRef = database.getReference("Favorites").child(userId).child(recipeId);

        favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Nếu đã có trong yêu thích
                    isFavorite = true;  // Đánh dấu là đã yêu thích
                } else {
                    // Nếu chưa có
                    isFavorite = false; // Đánh dấu là chưa yêu thích
                }
                // Không gán sự kiện click ở đây nữa
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "checkFavoriteStatus:onCancelled", databaseError.toException());
            }
        });
    }

    // Phương thức xử lý lựa chọn menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btnShare) {
            shareRecipe();
            return true;
        } else if (item.getItemId() == R.id.btnTym) {
            if (isFavorite) {
                removeRecipeFromFavorites(recipeId);  // Xóa khỏi danh sách yêu thích
            } else {
                addRecipeToFavorites(recipeId);  // Thêm vào danh sách yêu thích
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }



    private void loadRecipeDetails(String recipeId) {
        database = FirebaseDatabase.getInstance();
        recipeRef = database.getReference("Recipes").child(recipeId);

        ProgressDialog progressDialog = new ProgressDialog(Activity_RecipeDetail.this);
        progressDialog.setMessage("Đang tải...");
        progressDialog.show();

        recipeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                Recipe recipe = dataSnapshot.getValue(Recipe.class);

                if (recipe != null) {
                    txtNameF.setText(recipe.getTitle());
                    txtDesc.setText(recipe.getDescription());
                    txtTitle.setText(recipe.getTitle());
                    imageUrl = recipe.getImg();  // Lưu URL hình ảnh để chia sẻ

                    // Cấu hình WebView để hiển thị và phát video trong app
                    if (recipe.getUrlVideo() != null && !recipe.getUrlVideo().isEmpty()) {
                        webView.getSettings().setJavaScriptEnabled(true);  // Kích hoạt JavaScript
                        webView.getSettings().setDomStorageEnabled(true);  // Kích hoạt DOM storage
                        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);  // Cho phép mở cửa sổ mới
                        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);  // Cho phép phát media không cần thao tác người dùng

                        // Sử dụng WebViewClient để không mở ứng dụng bên ngoài
                        webView.setWebViewClient(new WebViewClient());

                        // Hiển thị video YouTube nhúng
                        String youtubeEmbedUrl = "https://www.youtube.com/embed/" + extractYouTubeVideoId(recipe.getUrlVideo());
                        webView.loadUrl(youtubeEmbedUrl);
                    }else {
                        txtTitleVideo.setVisibility(View.GONE);
                        webView.setVisibility(View.GONE);
                    }

                    // Hiển thị nguyên liệu
                    StringBuilder ingredients = new StringBuilder();
                    for (String item : recipe.getIngredients()) {
                        ingredients.append("  - ").append(item).append("\n");
                    }
                    tvIngredient.setText(ingredients.toString());

                    // Hiển thị các bước
                    layoutSteps.removeAllViews();
                    for (Step step : recipe.getSteps()) {
                        TextView stepTextView = new TextView(Activity_RecipeDetail.this);
                        stepTextView.setText("Bước " + step.getStepNumber() + ": " + "\n" + " - " + step.getDescription());
                        stepTextView.setTextSize(22);
                        stepTextView.setPadding(0, 20, 0, 20);

                        ImageView stepImageView = new ImageView(Activity_RecipeDetail.this);
                        stepImageView.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                500
                        ));
                        stepImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                        Glide.with(Activity_RecipeDetail.this)
                                .load(step.getImageUrl())
                                .into(stepImageView);

                        layoutSteps.addView(stepTextView);
                        layoutSteps.addView(stepImageView);
                    }
                }
            }

            // Hàm trích xuất ID video từ URL YouTube
            private String extractYouTubeVideoId(String videoUrl) {
                String videoId = "";
                String pattern = "^(https?://)?(www\\.)?(youtube\\.com|youtu\\.?be)/.+$";

                if (videoUrl.matches(pattern)) {
                    String[] parts = videoUrl.split("v=");
                    if (parts.length > 1) {
                        videoId = parts[1].split("&")[0];  // Trích xuất ID video
                    }
                }
                return videoId;
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Log.w("Firebase", "loadRecipeDetails:onCancelled", databaseError.toException());
            }
        });
    }

    // Hàm thêm công thức vào danh sách yêu thích
    private void addRecipeToFavorites(String recipeId) {
        recipeRef = database.getReference("Recipes").child(recipeId);
        favoriteRef = database.getReference("Favorites").child(userId).child(recipeId);

        recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                if (recipe != null) {
                    Favorite favoriteRecipe = new Favorite(
                            recipeId,
                            recipe.getTitle(),
                            recipe.getImg()
                    );

                    favoriteRef.setValue(favoriteRecipe).addOnSuccessListener(aVoid -> {
                        Toast.makeText(Activity_RecipeDetail.this, "Đã thêm vào món yêu thích", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(Activity_RecipeDetail.this, "Thêm vào món yêu thích thất bại", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "addRecipeToFavorites:onCancelled", databaseError.toException());
            }
        });
    }

    private void removeRecipeFromFavorites(String recipeId) {
        favoriteRef = database.getReference("Favorites").child(userId).child(recipeId);

        favoriteRef.removeValue().addOnSuccessListener(aVoid -> {
            Toast.makeText(Activity_RecipeDetail.this, "Đã bỏ thích", Toast.LENGTH_SHORT).show();
//            btnTym.setText("Yêu thích");
            isFavorite = false;  // Cập nhật trạng thái sau khi xóa
        }).addOnFailureListener(e -> {
            Toast.makeText(Activity_RecipeDetail.this, "Bỏ thích thất bại", Toast.LENGTH_SHORT).show();
        });
    }

    private void changeButtonColor(TextView button, int color) {
        // Đặt màu nền
        button.setBackgroundColor(color);
        // Đặt màu chữ thành trắng
        button.setTextColor(textColor);

        // Khôi phục lại màu sắc và chữ cho các nút khác
        if (button != btnAcc) {
            btnAcc.setBackgroundColor(defaultColor);
            btnAcc.setTextColor(getResources().getColor(R.color.black)); // Màu chữ mặc định
        }
        if (button != btnFavorite) {
            btnFavorite.setBackgroundColor(defaultColor);
            btnFavorite.setTextColor(getResources().getColor(R.color.black));
        }
        if (button != btnMeoHay) {
            btnMeoHay.setBackgroundColor(defaultColor);
            btnMeoHay.setTextColor(getResources().getColor(R.color.black));
        }
        if (button != btnBiQuyet) {
            btnBiQuyet.setBackgroundColor(defaultColor);
            btnBiQuyet.setTextColor(getResources().getColor(R.color.black));
        }
        if (button != btnCook) {
            btnCook.setBackgroundColor(defaultColor);
            btnCook.setTextColor(getResources().getColor(R.color.black));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Khôi phục màu sắc và chữ mặc định cho các nút
        btnAcc.setBackgroundColor(defaultColor);
        btnAcc.setTextColor(getResources().getColor(R.color.black)); // Đặt màu chữ mặc định

        btnFavorite.setBackgroundColor(defaultColor);
        btnFavorite.setTextColor(getResources().getColor(R.color.black));

        btnMeoHay.setBackgroundColor(defaultColor);
        btnMeoHay.setTextColor(getResources().getColor(R.color.black));

        btnBiQuyet.setBackgroundColor(defaultColor);
        btnBiQuyet.setTextColor(getResources().getColor(R.color.black));

        btnCook.setBackgroundColor(defaultColor);
        btnCook.setTextColor(getResources().getColor(R.color.black));
    }

}
