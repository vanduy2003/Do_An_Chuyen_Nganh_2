package nhom12.eauta.cookinginstructions.Controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nhom12.eauta.cookinginstructions.Adapter.CatagoryAdapter;
import nhom12.eauta.cookinginstructions.Adapter.SecretAdapter;
import nhom12.eauta.cookinginstructions.Adapter.TopAdapter;
import nhom12.eauta.cookinginstructions.Model.CatagoryItem;
import nhom12.eauta.cookinginstructions.R;

public class Activity_Home extends AppCompatActivity {
    private ViewPager2 viewPager;
    GridView gvCatagory, gvTopSearch;
    CatagoryAdapter catagoryAdapter;
    TopAdapter topAdapter;
    ArrayList<CatagoryItem> arrCategories = new ArrayList<>();
    ArrayList<CatagoryItem> arrTopSearch = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference categoriesRef;
    EditText txtSearch;
    private TextView btnAcc, btnFavorite, btnMeoHay, btnCamNang;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    private ImageButton btnMenu;
    private int defaultColor;
    private int colorAcc;
    private int colorFavorite;
    private int colorMeoHay;
    private int colorBiQuyet;
    private int textColor;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) { 
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Thiết lập ActionBarDrawerToggle
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        // Khóa vuốt mở Navigation Drawer
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        // Kiểm tra ActionBar có null không
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Khởi tạo màu sắc
        defaultColor = getResources().getColor(R.color.trang);
        colorAcc = getResources().getColor(R.color.hong);
        colorFavorite = getResources().getColor(R.color.xanhla);
        colorMeoHay = getResources().getColor(R.color.hongdam);
        colorBiQuyet = getResources().getColor(R.color.blue);
        textColor = getResources().getColor(R.color.trang);

        catagoryAdapter = new CatagoryAdapter(this, R.layout.layout_item_catagory, arrCategories);
        gvCatagory = findViewById(R.id.gvDishList);
        gvCatagory.setAdapter(catagoryAdapter);
        btnFavorite = findViewById(R.id.btnFavorite);
        txtSearch = findViewById(R.id.txtSearch);
        btnMenu = findViewById(R.id.btnMenu);
        btnAcc = findViewById(R.id.btnAcount);
        btnMeoHay = findViewById(R.id.btnMeoHay);
        btnCamNang = findViewById(R.id.btnCamNang);
        gvTopSearch = findViewById(R.id.gvTopSearch);

        btnMenu.setOnClickListener(view -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        loadCategories();
        LoadTopSearch();

        // Lấy userId từ SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = preferences.getString("userId", null);

        // bí quyết
        btnCamNang.setOnClickListener(view -> {
            changeButtonColor(btnCamNang,colorBiQuyet);
            Intent intent = new Intent(Activity_Home.this, Activity_HandBook.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        // Khi người dùng nhấn nút mẹo hay
        btnMeoHay.setOnClickListener(view -> {
            changeButtonColor(btnMeoHay, colorMeoHay);
            Intent intent = new Intent(Activity_Home.this, GoodTips.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        // Khi người dùng nhấn nút tài khoản
        btnAcc.setOnClickListener(view -> {
            changeButtonColor(btnAcc, colorAcc);
            if (userId != null) {
                Intent intent = new Intent(Activity_Home.this, UpdateInfor.class);
                intent.putExtra("userId", userId); // Truyền userId qua Intent
                startActivity(intent);
            } else {
                Toast.makeText(Activity_Home.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });

        gvCatagory.setOnItemClickListener((parent, view, position, id) -> {
            CatagoryItem selectedCategory = arrCategories.get(position);
            // Tạo Intent để chuyển sang Activity mới
            Intent intent = new Intent(Activity_Home.this, Activity_DishList.class);
            // Gửi dữ liệu qua Activity mới
            intent.putExtra("CategoryId", selectedCategory.getId());
            startActivity(intent);
        });

        gvTopSearch.setOnItemClickListener((parent, view, position, id) -> {
            CatagoryItem selectedCategory = arrTopSearch.get(position);
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
            changeButtonColor(btnFavorite, colorFavorite);
            Intent intent = new Intent(Activity_Home.this, Activity_Favorite.class);
            intent.putExtra("UserId", userId); // Truyền userId qua Intent
            startActivity(intent);
        });

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Xử lý các mục menu tại đây
                if (item.getItemId() == R.id.menu_Tym) {
                    Intent intent = new Intent(Activity_Home.this, Activity_Favorite.class);
                    intent.putExtra("UserId", userId); // Truyền userId qua Intent
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true; // Trả về true nếu đã xử lý

                } else if (item.getItemId() == R.id.menu_Share) {
                    Toast.makeText(Activity_Home.this, "Share", Toast.LENGTH_SHORT).show();
                    // Chia sẻ ứng dụng
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    String shareMessage = "Tải ứng dụng của chúng tôi tại: https://www.amazon.com/gp/product/B0DJ1NGCKH";
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Ứng dụng hay dành cho bạn");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Chia sẻ ứng dụng qua"));

                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (item.getItemId() == R.id.menu_Evaluate) {
                    Toast.makeText(Activity_Home.this, "Đánh giá", Toast.LENGTH_SHORT).show();
                    // Đánh giá ứng dụng
                    Intent evaluateIntent = new Intent(Intent.ACTION_VIEW);
                    evaluateIntent.setData(android.net.Uri.parse("https://www.amazon.com/gp/product/B0DJ1NGCKH"));
                    startActivity(evaluateIntent);

                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (item.getItemId() == R.id.menu_Clause) {
                    Toast.makeText(Activity_Home.this, "Đánh giá", Toast.LENGTH_SHORT).show();
                    // Điều khoản sử dụng
                    Intent clauseIntent = new Intent(Intent.ACTION_VIEW);
                    clauseIntent.setData(android.net.Uri.parse("https://www.freeprivacypolicy.com/live/8e8a6b8c-1c82-4e35-a1bc-d7c1477e0d9d"));
                    startActivity(clauseIntent);
                } else if (item.getItemId() == R.id.Youtube) {
                    Intent intent = new Intent(Activity_Home.this, Activity_Youtube.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }


                return false; // Trả về false nếu không xử lý được mục nào
            }
        });

        // chuyển động banner
        viewPager = findViewById(R.id.Banner);
        List<Integer> bannerList = Arrays.asList(
                R.drawable.banner4,
                R.drawable.img_3,
                R.drawable.img_1
        );

        SecretAdapter adapter = new SecretAdapter(bannerList);
        viewPager.setAdapter(adapter);

        // Tự động chuyển ảnh sau mỗi 5 giây
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int currentPage = 0;

            @Override
            public void run() {
                if (currentPage == bannerList.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000); // Chuyển ảnh sau 2 giây
            }
        };
        handler.postDelayed(runnable, 3000);
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
    }



    private void searchCategories(String query) {
        if (query.isEmpty()) {
            loadCategories(); // Load lại toàn bộ danh mục khi từ khóa tìm kiếm trống
            return;
        }

        ArrayList<CatagoryItem> filteredList = new ArrayList<>();
        for (CatagoryItem item : arrCategories) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }

        catagoryAdapter = new CatagoryAdapter(this, R.layout.layout_item_catagory, filteredList);
        gvCatagory.setAdapter(catagoryAdapter);

        gvCatagory.setOnItemClickListener((parent, view, position, id) -> {
            CatagoryItem selectedCategory = filteredList.get(position);
            // Tạo Intent để chuyển sang Activity mới
            Intent intent = new Intent(Activity_Home.this, Activity_DishList.class);
            // Gửi dữ liệu qua Activity mới
            intent.putExtra("CategoryId", selectedCategory.getId());
            startActivity(intent);
        });

        catagoryAdapter.notifyDataSetChanged();
    }

    private void loadCategories() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference categoriesRef = database.getReference("Categories");

        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() { // Sử dụng addListenerForSingleValueEvent để chỉ lắng nghe một lần
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrCategories.clear(); // Xóa dữ liệu cũ trước khi thêm dữ liệu mới
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String categoryId = snapshot.getKey(); // Lấy id của danh mục
                    CatagoryItem category = snapshot.getValue(CatagoryItem.class);
                    if (category != null) {
                        category.setId(categoryId); // Gán id cho đối tượng CatagoryItem
                        arrCategories.add(category);
                    }
                }
                catagoryAdapter = new CatagoryAdapter(Activity_Home.this, R.layout.layout_item_catagory, arrCategories);
                gvCatagory.setAdapter(catagoryAdapter);
                catagoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "loadCategories:onCancelled", databaseError.toException());
            }
        });
    }

    private void LoadTopSearch() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference topSearchRef = database.getReference("Categories"); // Đảm bảo đường dẫn trong Firebase là đúng

        topSearchRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrTopSearch.clear(); // Xóa dữ liệu cũ trước khi thêm dữ liệu mới
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String categoryId = snapshot.getKey(); // Lấy id của danh mục
                    CatagoryItem category = snapshot.getValue(CatagoryItem.class);
                    if (category != null) {
                        category.setId(categoryId); // Gán id cho đối tượng CatagoryItem
                        arrTopSearch.add(category);
                    }
                }
                topAdapter = new TopAdapter(Activity_Home.this, R.layout.layout_item_topsearch, arrTopSearch);
                gvTopSearch.setAdapter(topAdapter);
                topAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "loadTopSearch:onCancelled", databaseError.toException());
            }
        });
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        // Tạo Dialog xác nhận thoát
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn có muốn thoát ứng dụng không?");
        builder.setIcon(R.mipmap.chef);
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

        // Kiểm tra nếu ngăn kéo menu đang mở, thì đóng lại thay vì thoát ứng dụng
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

}
