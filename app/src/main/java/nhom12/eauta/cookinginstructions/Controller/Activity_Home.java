package nhom12.eauta.cookinginstructions.Controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.Toast;

import java.util.ArrayList;

import nhom12.eauta.cookinginstructions.Adapter.CatagoryAdapter;
import nhom12.eauta.cookinginstructions.Model.CatagoryItem;
import nhom12.eauta.cookinginstructions.R;
public class Activity_Home extends AppCompatActivity {
    GridView gvCatagory;
    CatagoryAdapter catagoryAdapter;
    ArrayList<CatagoryItem> arr = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference categoriesRef;
    EditText txtSearch;
    private ImageButton btnAcc, btnFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        catagoryAdapter = new CatagoryAdapter(this, R.layout.layout_item_catagory, arr);
        gvCatagory = findViewById(R.id.gvDishList);
        btnAcc = findViewById(R.id.btnAcount);
        gvCatagory.setAdapter(catagoryAdapter);
        btnFavorite = findViewById(R.id.btnFavorite);
        txtSearch = findViewById(R.id.txtSearch);


        loadCategories();

        // Lấy userId từ SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = preferences.getString("userId", null);

        // Khi người dùng nhấn nút tài khoản
        btnAcc.setOnClickListener(view -> {
            if (userId != null) {
                Intent intent = new Intent(Activity_Home.this, UpdateInfor.class);
                intent.putExtra("userId", userId); // Truyền userId qua Intent
                startActivity(intent);
            } else {
                Toast.makeText(Activity_Home.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });


        gvCatagory.setOnItemClickListener((parent, view, position, id) -> {
            CatagoryItem selectedCategory = arr.get(position);
            // Tạo Intent để chuyển sang Activity mới
            Intent intent = new Intent(Activity_Home.this, Activity_DishList.class);
            // Gửi dữ liệu qua Activity mới
            intent.putExtra("CategoryId", selectedCategory.getId());
            startActivity(intent);
        });


        txtSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchCategories(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
                // Không cần xử lý
            }
        });

        // Khi người dùng nhấn nút yêu thích
        btnFavorite.setOnClickListener(view -> {
            Intent intent = new Intent(Activity_Home.this, Activity_Favorite.class);
            intent.putExtra("UserId", userId); // Truyền userId qua Intent
            startActivity(intent);
        });

    }

    private void searchCategories(String query) {
        if (query.isEmpty()) {
            loadCategories(); // Load lại toàn bộ danh mục khi từ khóa tìm kiếm trống
            return;
        }

        ArrayList<CatagoryItem> filteredList = new ArrayList<>();
        for (CatagoryItem item : arr) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }

        catagoryAdapter = new CatagoryAdapter(this, R.layout.layout_item_catagory, filteredList);
        gvCatagory.setAdapter(catagoryAdapter);
        catagoryAdapter.notifyDataSetChanged();

    }

    private void loadCategories() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference categoriesRef = database.getReference("Categories");

        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() { // Sử dụng addListenerForSingleValueEvent để chỉ lắng nghe một lần
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arr.clear(); // Xóa dữ liệu cũ trước khi thêm dữ liệu mới
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String categoryId = snapshot.getKey(); // Lấy id của danh mục
                    CatagoryItem category = snapshot.getValue(CatagoryItem.class);
                    if (category != null) {
                        category.setId(categoryId); // Gán id cho đối tượng CatagoryItem
                        arr.add(category);
                    }
                }
                catagoryAdapter = new CatagoryAdapter(Activity_Home.this, R.layout.layout_item_catagory, arr);
                gvCatagory.setAdapter(catagoryAdapter);
                catagoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "loadCategories:onCancelled", databaseError.toException());
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        // Tao Dialog xac nhan thoat
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn có muốn thoát ứng dụng không?");
        builder.setIcon(R.mipmap.cooking);
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Gọi super.onBackPressed() sau khi người dùng xác nhận thoát
                Activity_Home.super.onBackPressed();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel(); // Đóng dialog nếu chọn "Không"
            }
        });
        builder.create().show();
    }


}
