package nhom12.eauta.cookinginstructions.Controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import nhom12.eauta.cookinginstructions.Adapter.DishAdapter;
import nhom12.eauta.cookinginstructions.Model.DishItem;
import nhom12.eauta.cookinginstructions.R;

public class Activity_DishList extends AppCompatActivity {
    // Khai báo các thành phần
    GridView gvDishList;
    DishAdapter dishAdapter;
    ArrayList<DishItem> arr = new ArrayList<>();
    ArrayList<DishItem> filteredList = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference recipesRef;
    ImageView btnThoat;
    EditText txtSearch;
    private int defaultColor;
    private int colorAcc;
    private int colorFavorite;
    private int colorMeoHay;
    private int colorBiQuyet;
    private int textColor;
    private int colorCook;
    private TextView btnAcc, btnFavorite, btnMeoHay, btnCamNang, btnCook;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_list);

        // Khởi tạo các thành phần
        gvDishList = findViewById(R.id.gvDishList);
        txtSearch = findViewById(R.id.txtSearch);
        dishAdapter = new DishAdapter(this, R.layout.layout_item_dishlist, filteredList);
        gvDishList.setAdapter(dishAdapter);
        btnCook = findViewById(R.id.btnCook);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnMeoHay = findViewById(R.id.btnMeoHay);
        btnCamNang = findViewById(R.id.btnCamNang);
        btnAcc = findViewById(R.id.btnAcount);
        btnFavorite = findViewById(R.id.btnFavorite);

        defaultColor = getResources().getColor(R.color.trang);
        colorAcc = getResources().getColor(R.color.hong);
        colorFavorite = getResources().getColor(R.color.xanhla);
        colorMeoHay = getResources().getColor(R.color.hongdam);
        colorBiQuyet = getResources().getColor(R.color.blue);
        textColor = getResources().getColor(R.color.trang);
        colorCook = getResources().getColor(R.color.tim);

        // Nhận CategoryId từ Intent
        String categoryId = getIntent().getStringExtra("CategoryId");

        // Load dữ liệu món ăn theo danh mục
        loadDishesByCategory(categoryId);

        // Thêm sự kiện tìm kiếm
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDishes(s.toString()); // Gọi hàm lọc món ăn
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý
            }
        });

        // Xử lý sự kiện khi nhấn vào một món ăn
        gvDishList.setOnItemClickListener((parent, view, position, id) -> {
            DishItem selectedDish = arr.get(position);
            Intent intent = new Intent(Activity_DishList.this, Activity_RecipeDetail.class);
            intent.putExtra("RecipeId", selectedDish.getId());
            intent.putExtra("RecipeName", selectedDish.getTitle());
            intent.putExtra("RecipeDescription", selectedDish.getDescription());
            startActivity(intent);
        });

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
            Intent intent = new Intent(Activity_DishList.this, Activity_HandBook.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        // bí quyết
        btnFavorite.setOnClickListener(view -> {
            changeButtonColor(btnFavorite,colorFavorite);
            Intent intent = new Intent(Activity_DishList.this, Activity_Favorite.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        // Khi người dùng nhấn nút mẹo hay
        btnMeoHay.setOnClickListener(view -> {
            changeButtonColor(btnMeoHay, colorMeoHay);
            Intent intent = new Intent(Activity_DishList.this, GoodTips.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        // Khi người dùng nhấn nút tài khoản
        btnAcc.setOnClickListener(view -> {
            changeButtonColor(btnAcc, colorAcc);
            if (userId != null) {
                Intent intent = new Intent(Activity_DishList.this, UpdateInfor.class);
                intent.putExtra("userId", userId); // Truyền userId qua Intent
                startActivity(intent);
            } else {
                Toast.makeText(Activity_DishList.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });
        btnCook.setOnClickListener(view -> {
            changeButtonColor(btnCook, colorCook);
            Intent intent = new Intent(Activity_DishList.this, Activity_Home.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
    }

    private void loadDishesByCategory(String categoryId) {
        database = FirebaseDatabase.getInstance();
        recipesRef = database.getReference("Recipes");

        recipesRef.orderByChild("category_id").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        arr.clear(); // Xóa dữ liệu cũ trước khi thêm dữ liệu mới
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String RecipeId = snapshot.getKey(); // Lấy id của món ăn
                            DishItem dish = snapshot.getValue(DishItem.class);
                            if (dish != null) {
                                dish.setId(RecipeId);
                                arr.add(dish);
                            }
                        }
                        filteredList.clear();
                        filteredList.addAll(arr); // Hiển thị tất cả các món ăn khi load xong
                        dishAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Firebase", "loadDishesByCategory:onCancelled", databaseError.toException());
                    }
                });
    }

    private void filterDishes(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(arr); // Nếu từ khóa tìm kiếm trống, hiển thị tất cả các món ăn
        } else {
            for (DishItem dish : arr) {
                if (dish.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(dish); // Thêm món ăn vào danh sách lọc nếu khớp với từ khóa tìm kiếm
                }
            }
        }
        dishAdapter.notifyDataSetChanged(); // Cập nhật lại giao diện GridView
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
}

