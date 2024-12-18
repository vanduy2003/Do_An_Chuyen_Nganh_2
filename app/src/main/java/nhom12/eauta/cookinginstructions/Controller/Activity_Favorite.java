package nhom12.eauta.cookinginstructions.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import nhom12.eauta.cookinginstructions.Adapter.CatagoryAdapter;
import nhom12.eauta.cookinginstructions.Model.CatagoryItem;
import nhom12.eauta.cookinginstructions.Model.Favorite;
import nhom12.eauta.cookinginstructions.R;
import nhom12.eauta.cookinginstructions.Adapter.FavoriteAdapter;

public class Activity_Favorite extends AppCompatActivity {
    private ListView lvFavorites;
    private FirebaseDatabase database;
    private DatabaseReference favoritesRef;
    private ArrayList<Favorite> favoriteList;
    private FavoriteAdapter adapter;
    ImageView btnThoat, btnRemove,btn_timKiem;
    EditText txtSearch;
    private int defaultColor;
    private int colorAcc;
    private int colorFavorite;
    private int colorMeoHay;
    private int colorBiQuyet;
    private int textColor;
    private int colorCook;
    private TextView btnAcc, btnFavorite, btnMeoHay, btnCamNang, btnCook, txtName;
    private EditText txtTimKiem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        lvFavorites = findViewById(R.id.lvFavorites);
        favoriteList = new ArrayList<>();
        adapter = new FavoriteAdapter(this, R.layout.favorite_item, favoriteList, "");
        lvFavorites.setAdapter(adapter);
        btnCook = findViewById(R.id.btnCook);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnMeoHay = findViewById(R.id.btnMeoHay);
        btnCamNang = findViewById(R.id.btnCamNang);
        btnAcc = findViewById(R.id.btnAcount);
        btnRemove = findViewById(R.id.btnRemove);
        btn_timKiem = findViewById(R.id.btn_timKiem);
        txtTimKiem = findViewById(R.id.txtSearch);
        txtName = findViewById(R.id.txtNameF);
        txtSearch = findViewById(R.id.txtSearch);

        defaultColor = getResources().getColor(R.color.trang);
        colorAcc = getResources().getColor(R.color.hong);
        colorFavorite = getResources().getColor(R.color.xanhla);
        colorMeoHay = getResources().getColor(R.color.hongdam);
        colorBiQuyet = getResources().getColor(R.color.blue);
        textColor = getResources().getColor(R.color.trang);
        colorCook = getResources().getColor(R.color.tim);

        // Lấy userId của người dùng hiện tại
        String userId = getIntent().getStringExtra("UserId");


        // Kiểm tra userId trước khi gọi loadFavoriteRecipes
        if (userId == null || userId.isEmpty()) {
            Log.e("Activity_Favorite", "UserId is null or empty");
            // Có thể hiển thị thông báo lỗi hoặc quay về Activity trước
            finish(); // Hoặc thực hiện hành động nào đó
            return;
        }
        // ẩn hiển tìm kiếm
        btn_timKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtTimKiem.getVisibility() == View.GONE) {
                    // Hiển thị thanh tìm kiếm và ẩn tiêu đề
                    txtTimKiem.setVisibility(View.VISIBLE);
                    txtName.setVisibility(View.GONE);
                } else {
                    // Ẩn thanh tìm kiếm và hiển thị tiêu đề
                    txtTimKiem.setVisibility(View.GONE);
                    txtName.setVisibility(View.VISIBLE);
                }
            }
        });

        // Xử lý sự kiện tìm kiếm
        txtSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchFavourite(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
                // Không cần xử lý
            }
        });

        // Khởi tạo adapter với userId
        adapter = new FavoriteAdapter(this, R.layout.favorite_item, favoriteList, userId);
        lvFavorites.setAdapter(adapter);

        // Truy vấn các món ăn yêu thích từ Firebase dựa trên userId
        loadFavoriteRecipes(userId);

        lvFavorites.setOnItemClickListener((parent, view, position, id) -> {
            Favorite favorite = favoriteList.get(position);
            String recipeId = favorite.getRecipeId();
            Intent intent = new Intent(Activity_Favorite.this, Activity_RecipeDetail.class);
            intent.putExtra("RecipeId", recipeId);
            startActivity(intent);
        });

        btnThoat = findViewById(R.id.btnThoat);
        btnThoat.setOnClickListener(v -> {
            finish();
        });


        // Lấy userId từ SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // check gọi menu để chuyển trang
        // Cẩm nang
        btnCamNang.setOnClickListener(view -> {
            changeButtonColor(btnCamNang,colorBiQuyet);
            Intent intent = new Intent(Activity_Favorite.this, Activity_HandBook.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        // Khi người dùng nhấn nút mẹo hay
        btnMeoHay.setOnClickListener(view -> {
            changeButtonColor(btnMeoHay, colorFavorite);
            Intent intent = new Intent(Activity_Favorite.this, GoodTips.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        // Khi người dùng nhấn nút tài khoản
        btnAcc.setOnClickListener(view -> {
            changeButtonColor(btnAcc, colorAcc);
            if (userId != null) {
                Intent intent = new Intent(Activity_Favorite.this, UpdateInfor.class);
                intent.putExtra("userId", userId); // Truyền userId qua Intent
                startActivity(intent);
            } else {
                Toast.makeText(Activity_Favorite.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });
        btnCook.setOnClickListener(view -> {
            changeButtonColor(btnCook, colorCook);
            Intent intent = new Intent(Activity_Favorite.this, Activity_Home.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });


        lvFavorites.setOnItemClickListener((parent, view, position, id) -> {
            Favorite favorite = favoriteList.get(position);
            String itemId = favorite.getRecipeId();

            Intent intent;
            if (itemId.startsWith("recipe_id")) {
                // Nếu là công thức nấu ăn
                intent = new Intent(Activity_Favorite.this, Activity_RecipeDetail.class);
                intent.putExtra("RecipeId", itemId);
            } else if (itemId.startsWith("tip_id")) {
                // Nếu là mẹo hay
                intent = new Intent(Activity_Favorite.this, Activity_TipsDetail.class);
                intent.putExtra("TipsId", itemId);
            } else {
                // Nếu không phải là công thức nấu ăn hoặc mẹo hay
                Log.e("Activity_Favorite", "Unknown item type: " + itemId);
                return;
            }
            startActivity(intent);
        });

    }

    private void searchFavourite(String query) {
        if (query.isEmpty()) {
            // Hiển thị lại danh sách gốc nếu không có từ khóa tìm kiếm
            adapter = new FavoriteAdapter(this, R.layout.favorite_item, favoriteList, getIntent().getStringExtra("UserId"));
            lvFavorites.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            // Tạo danh sách lọc
            ArrayList<Favorite> filteredList = new ArrayList<>();
            for (Favorite item : favoriteList) {
                if (item.getRecipeTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }

            // Cập nhật adapter với danh sách đã lọc
            adapter = new FavoriteAdapter(this, R.layout.favorite_item, filteredList, getIntent().getStringExtra("UserId"));
            lvFavorites.setAdapter(adapter);

            lvFavorites.setOnItemClickListener((parent, view, position, id) -> {
                Favorite favorite = filteredList.get(position);
                String recipeId = favorite.getRecipeId();
                Intent intent = new Intent(Activity_Favorite.this, Activity_RecipeDetail.class);
                intent.putExtra("RecipeId", recipeId);
                startActivity(intent);
            });

            adapter.notifyDataSetChanged();
        }
    }



    private void loadFavoriteRecipes(String userId) {
        database = FirebaseDatabase.getInstance();
        favoritesRef = database.getReference("Favorites").child(userId);

        ProgressDialog progressDialog = new ProgressDialog(Activity_Favorite.this);
        progressDialog.setMessage("Đang tải...");
        progressDialog.show();

        favoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                favoriteList.clear();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Favorite favorite = snapshot.getValue(Favorite.class);
                    if (favorite != null) {
                        favoriteList.add(favorite);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Log.w("Firebase", "loadFavoriteRecipes:onCancelled", databaseError.toException());
            }
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
}

