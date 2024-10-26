package nhom12.eauta.cookinginstructions.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import nhom12.eauta.cookinginstructions.Model.Favorite;
import nhom12.eauta.cookinginstructions.R;
import nhom12.eauta.cookinginstructions.Adapter.FavoriteAdapter;

public class Activity_Favorite extends AppCompatActivity {
    private ListView lvFavorites;
    private FirebaseDatabase database;
    private DatabaseReference favoritesRef;
    private ArrayList<Favorite> favoriteList;
    private FavoriteAdapter adapter;
    ImageView btnThoat, btnRemove;
    private int defaultColor;
    private int colorAcc;
    private int colorFavorite;
    private int colorMeoHay;
    private int colorBiQuyet;
    private int textColor;
    private int colorCook;
    private TextView btnAcc, btnFavorite, btnMeoHay, btnBiQuyet, btnCook;

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
        btnBiQuyet = findViewById(R.id.btnBiQuyet);
        btnAcc = findViewById(R.id.btnAcount);
        btnRemove = findViewById(R.id.btnRemove);

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
        // bí quyết
        btnBiQuyet.setOnClickListener(view -> {
            changeButtonColor(btnBiQuyet,colorBiQuyet);
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
            String recipeId = favorite.getRecipeId();
            Intent intent = new Intent(Activity_Favorite.this, Activity_RecipeDetail.class);
            intent.putExtra("RecipeId", recipeId);
            startActivity(intent);
        });

    }


    private void loadFavoriteRecipes(String userId) {
        database = FirebaseDatabase.getInstance();
        favoritesRef = database.getReference("Favorites").child(userId);

        ProgressDialog progressDialog = new ProgressDialog(Activity_Favorite.this);
        progressDialog.setMessage("Đang tải...");
        progressDialog.show();

        favoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                favoriteList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Favorite favorite = snapshot.getValue(Favorite.class);
                    favoriteList.add(favorite);
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

