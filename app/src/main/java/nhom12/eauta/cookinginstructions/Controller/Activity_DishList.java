package nhom12.eauta.cookinginstructions.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_list);

        // Khởi tạo các thành phần
        gvDishList = findViewById(R.id.gvDishList);
        txtSearch = findViewById(R.id.txtSearch);
        dishAdapter = new DishAdapter(this, R.layout.layout_item_dishlist, filteredList);
        gvDishList.setAdapter(dishAdapter);

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

}

