package nhom12.eauta.cookinginstructions.Controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nhom12.eauta.cookinginstructions.Model.Favorite;
import nhom12.eauta.cookinginstructions.Model.Recipe;
import nhom12.eauta.cookinginstructions.Model.Step;
import nhom12.eauta.cookinginstructions.Model.Tips;
import nhom12.eauta.cookinginstructions.R;

public class Activity_TipsDetail extends AppCompatActivity {
    private TextView txtNameF, txtTitle, txtDesc, txtTitleVideo, tvSteps;
    private LinearLayout layoutSteps;
    private FirebaseDatabase database;
    private DatabaseReference tipsRef,favoriteRef;
    ImageView btnThoat, btnZoomIn, btnZoomOut, btnThreeDots;
    WebView webView;
    private String imageUrl;  // Lưu URL hình ảnh công thức
    private int defaultColor;
    private int colorAcc;
    private int colorFavorite;
    private int colorMeoHay;
    private int colorBiQuyet;
    private int textColor;
    private int colorCook;
    private TextView btnAcc, btnFavorite, btnMeoHay, btnCamNang, btnCook;
    // Khai báo kích thước chữ và giá trị tăng giảm
    private float textSize = 16f; // Kích thước chữ mặc định
    private final float zoomIncrement = 2f; // Giá trị thay đổi khi zoom
    private String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tips_detail);

        txtNameF = findViewById(R.id.txtNameF);
        txtTitle = findViewById(R.id.txtTitle);
        txtDesc = findViewById(R.id.txtDesc);
        tvSteps = findViewById(R.id.tvSteps);
        txtTitleVideo = findViewById(R.id.txtTitleVideo);
        layoutSteps = findViewById(R.id.layoutSteps);
        btnThoat = findViewById(R.id.btnThoat);
        webView = findViewById(R.id.webView);
        btnCook = findViewById(R.id.btnCook);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnMeoHay = findViewById(R.id.btnMeoHay);
        btnCamNang = findViewById(R.id.btnCamNang);
        btnAcc = findViewById(R.id.btnAcount);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnThreeDots = findViewById(R.id.btnThreeDots);

        defaultColor = getResources().getColor(R.color.trang);
        colorAcc = getResources().getColor(R.color.hong);
        colorFavorite = getResources().getColor(R.color.xanhla);
        colorMeoHay = getResources().getColor(R.color.hongdam);
        colorBiQuyet = getResources().getColor(R.color.blue);
        textColor = getResources().getColor(R.color.trang);
        colorCook = getResources().getColor(R.color.tim);
        btnZoomIn = findViewById(R.id.btnZoomIn);
        btnZoomOut = findViewById(R.id.btnZoomOut);

        // Lấy userId từ SharedPreferences
        userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("userId", null);
        String tipsId = getIntent().getStringExtra("TipsId");

        // Thiết lập OnClickListener cho btnThreeDots
        btnThreeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị menu tùy chọn
                showMenu(v, tipsId);
            }
        });


        loadTipsDetail(tipsId);

        btnThoat = findViewById(R.id.btnThoat);
        btnThoat.setOnClickListener(v -> {
            finish();
        });

        // Lấy userId từ SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = preferences.getString("userId", null);
        // check gọi menu để chuyển trang
        // bí quyết
        btnCamNang.setOnClickListener(view -> {
            changeButtonColor(btnCamNang,colorBiQuyet);
            Intent intent = new Intent(Activity_TipsDetail.this, Activity_HandBook.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        // bí quyết
        btnFavorite.setOnClickListener(view -> {
            changeButtonColor(btnFavorite,colorFavorite);
            Intent intent = new Intent(Activity_TipsDetail.this, Activity_Favorite.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        // Khi người dùng nhấn nút mẹo hay
        btnMeoHay.setOnClickListener(view -> {
            changeButtonColor(btnMeoHay, colorMeoHay);
            Intent intent = new Intent(Activity_TipsDetail.this, GoodTips.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        // Khi người dùng nhấn nút tài khoản
        btnAcc.setOnClickListener(view -> {
            changeButtonColor(btnAcc, colorAcc);
            if (userId != null) {
                Intent intent = new Intent(Activity_TipsDetail.this, UpdateInfor.class);
                intent.putExtra("userId", userId); // Truyền userId qua Intent
                startActivity(intent);
            } else {
                Toast.makeText(Activity_TipsDetail.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });
        btnCook.setOnClickListener(view -> {
            changeButtonColor(btnCook, colorCook);
            Intent intent = new Intent(Activity_TipsDetail.this, Activity_Home.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        // Thiết lập OnClickListener cho nút zoom in
        btnZoomIn.setOnClickListener(v -> {
            textSize += zoomIncrement; // Tăng kích thước chữ
            applyTextSize(); // Áp dụng kích thước chữ mới
        });

        // Thiết lập OnClickListener cho nút zoom out
        btnZoomOut.setOnClickListener(v -> {
            if (textSize > 10f) { // Kiểm tra kích thước chữ không nhỏ hơn 10
                textSize -= zoomIncrement; // Giảm kích thước chữ
                applyTextSize(); // Áp dụng kích thước chữ mới
            }
        });
    }


    // Phương thức áp dụng kích thước chữ
    private void applyTextSize() {
        txtNameF.setTextSize(textSize);
        txtTitle.setTextSize(textSize);
        txtDesc.setTextSize(textSize);
        txtTitleVideo.setTextSize(textSize);
        for (int i = 0; i < layoutSteps.getChildCount(); i++) {
            View view = layoutSteps.getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextSize(textSize);
            }
        }
    }

    private void showMenu(View v, String tipsId) {
        // Tạo PopupMenu
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_share_tym, popupMenu.getMenu());

        // Kiểm tra phiên bản API và tự xử lý biểu tượng nếu cần thiết
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            // Nếu API level >= 29, sử dụng setForceShowIcon (cho các phiên bản mới)
            popupMenu.setForceShowIcon(true);
        }

        // Xử lý sự kiện khi chọn mục trong menu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.btnShare) {
                    // Gọi hàm chia sẻ công thức hoặc thông tin ở đây
                    shareRecipe();
                    return true; // Trả về true nếu xử lý thành công
                } else if (item.getItemId() == R.id.btnTym) {
                    // Xử lý thêm vào danh sách yêu thích
                    addTipToFavorites(tipsId);
                    return true;
                } else {
                    return false; // Trả về false nếu không xử lý
                }
            }
        });

        // Hiển thị menu
        popupMenu.show();
    }



    // Hàm chia sẻ công thức
    private void shareRecipe() {
        // Lấy nội dung cần chia sẻ
        String title = txtTitle.getText().toString();
        String description = txtDesc.getText().toString();


        // Kiểm tra xem dữ liệu đã được load đầy đủ chưa
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(Activity_TipsDetail.this, "Vui lòng đợi nội dung được tải đầy đủ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo nội dung chia sẻ
        String shareContent = title + "\n\n" + description + "\n\nNguyên liệu:\n" + txtDesc + "\n\nCác bước làm:\n";

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
            Toast.makeText(Activity_TipsDetail.this, "Không có ứng dụng nào hỗ trợ chia sẻ", Toast.LENGTH_SHORT).show();
        }

    }

    // Hàm thêm công thức vào danh sách yêu thích
    private void addTipToFavorites(String tipsId) {
        tipsRef = database.getReference("Tips").child(tipsId);
        favoriteRef = database.getReference("Favorites").child(userId).child(tipsId);

        tipsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tips tips = dataSnapshot.getValue(Tips.class);
                if (tips != null) {
                    Favorite favoriteRecipe = new Favorite(
                            tipsId,
                            tips.getTitle(),
                            tips.getImageUrl()
                    );

                    favoriteRef.setValue(favoriteRecipe).addOnSuccessListener(aVoid -> {
                        Toast.makeText(Activity_TipsDetail.this, "Đã thêm vào món yêu thích", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(Activity_TipsDetail.this, "Thêm vào món yêu thích thất bại", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "addRecipeToFavorites:onCancelled", databaseError.toException());
            }
        });
    }

    // màu cho menu
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
        if (button != btnCamNang) {
            btnCamNang.setBackgroundColor(defaultColor);
            btnCamNang.setTextColor(getResources().getColor(R.color.black));
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

        btnCamNang.setBackgroundColor(defaultColor);
        btnCamNang.setTextColor(getResources().getColor(R.color.black));
        btnCook.setBackgroundColor(defaultColor);
        btnCook.setTextColor(getResources().getColor(R.color.black));
    }

    private void loadTipsDetail(String tipsId) {
        database = FirebaseDatabase.getInstance();
        tipsRef = database.getReference("Tips").child(tipsId);

        tipsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Tips tips = snapshot.getValue(Tips.class);
                Recipe recipe = snapshot.getValue(Recipe.class);

                if (tips != null) {
                    // Cập nhật các thông tin cơ bản
                    txtNameF.setText(tips.getTitle());
                    txtTitle.setText(tips.getTitle());
                    txtDesc.setText(tips.getDescription());
                    imageUrl = recipe.getImg();  // Lưu URL hình ảnh để chia sẻ


                    // Cấu hình WebView để hiển thị và phát video trong app
                    if (tips.getUrlVideo() != null && !tips.getUrlVideo().isEmpty()) {
                        webView.getSettings().setJavaScriptEnabled(true);  // Kích hoạt JavaScript
                        webView.getSettings().setDomStorageEnabled(true);  // Kích hoạt DOM storage
                        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);  // Cho phép mở cửa sổ mới
                        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);  // Cho phép phát media không cần thao tác người dùng

                        // Sử dụng WebViewClient để không mở ứng dụng bên ngoài
                        webView.setWebViewClient(new WebViewClient());

                        // Hiển thị video YouTube nhúng
                        String youtubeEmbedUrl = "https://www.youtube.com/embed/" + extractYouTubeVideoId(tips.getUrlVideo());
                        webView.loadUrl(youtubeEmbedUrl);
                    }else {
                        txtTitleVideo.setVisibility(View.GONE);
                        webView.setVisibility(View.GONE);
                    }

                    // Hiển thị các bước trong layoutSteps
                    layoutSteps.removeAllViews(); // Xóa các view cũ trước khi thêm mới

                    for (Step step : tips.getSteps()) {
                        // Tạo TextView cho mỗi bước
                        TextView stepTextView = new TextView(Activity_TipsDetail.this);
                        stepTextView.setText("Cách " + step.getStepNumber() + ": " + "\n" + " - " + step.getDescription());
                        stepTextView.setTextSize(22);
                        stepTextView.setPadding(0, 20, 0, 20);
                        Typeface typeface = ResourcesCompat.getFont(Activity_TipsDetail.this, R.font.balsamiq_sans_bold);
                        stepTextView.setTypeface(typeface);
                        layoutSteps.addView(stepTextView);

                        // Tạo ImageView cho hình ảnh mỗi bước (nếu cần)
                        ImageView stepImageView = new ImageView(Activity_TipsDetail.this);
                        // Xử lý khi có ảnh
                        if (step.getImageUrl() != null && !step.getImageUrl().isEmpty()) {
                            // Load ảnh từ URL
                             Glide.with(Activity_TipsDetail.this).load(step.getImageUrl()).into(stepImageView);
                        } else {
                            // Đặt ảnh mặc định hoặc xử lý khi không có ảnh
                            stepImageView.setImageResource(R.drawable.baking); // Thay thế bằng ảnh mặc định của bạn
                        }
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
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi
            }
        });
    }


}

