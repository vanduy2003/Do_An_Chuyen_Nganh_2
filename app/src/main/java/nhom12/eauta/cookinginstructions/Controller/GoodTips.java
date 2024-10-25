package nhom12.eauta.cookinginstructions.Controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nhom12.eauta.cookinginstructions.Adapter.GoodTipsAdapter;
import nhom12.eauta.cookinginstructions.Adapter.TipsAdapter;
import nhom12.eauta.cookinginstructions.Model.Tips;
import nhom12.eauta.cookinginstructions.R;  // Đảm bảo rằng bạn nhập đúng gói chứa file R

public class GoodTips extends AppCompatActivity {
    private ViewPager2 viewPager;
    ImageView btnThoat;
    ListView lvTipsList;
    TipsAdapter tipsAdapter;
    ArrayList<Tips> arrTips = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference recipesRef;
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
        setContentView(R.layout.activity_good_tips);

        // Khởi tạo các thành phần
        viewPager = findViewById(R.id.banner_goodtips);
        btnThoat = findViewById(R.id.btnThoat);
        lvTipsList = findViewById(R.id.lvTipsList);
        btnCook = findViewById(R.id.btnCook);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnMeoHay = findViewById(R.id.btnMeoHay);
        btnBiQuyet = findViewById(R.id.btnBiQuyet);
        btnAcc = findViewById(R.id.btnAcount);

        defaultColor = getResources().getColor(R.color.trang);
        colorAcc = getResources().getColor(R.color.hong);
        colorFavorite = getResources().getColor(R.color.xanhla);
        colorMeoHay = getResources().getColor(R.color.hongdam);
        colorBiQuyet = getResources().getColor(R.color.blue);
        textColor = getResources().getColor(R.color.trang);
        colorCook = getResources().getColor(R.color.tim);

        List<Integer> bannerList = Arrays.asList(
                R.drawable.banner4,
                R.drawable.img_3,
                R.drawable.img_1
        );

        GoodTipsAdapter adapter = new GoodTipsAdapter(bannerList);
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
                handler.postDelayed(this, 5000); // Chuyển ảnh sau 5  giây
            }
        };
        handler.postDelayed(runnable, 5000);
        btnThoat = findViewById(R.id.btnThoat);

        loadTips();

        lvTipsList.setOnItemClickListener((parent, view, position, id) -> {
            Tips tips = arrTips.get(position);
            Intent intent = new Intent(GoodTips.this, Activity_TipsDetail.class);
            intent.putExtra("TipsId", tips.getId());
            startActivity(intent);
        });

        btnThoat.setOnClickListener(v -> {
            finish();
        });

        // Lấy userId từ SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = preferences.getString("userId", null);

        // check gọi menu để chuyển trang
        // bí quyết
        btnBiQuyet.setOnClickListener(view -> {
            changeButtonColor(btnBiQuyet,colorBiQuyet);
            Intent intent = new Intent(GoodTips.this, Secret.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        // Khi người dùng nhấn nút mẹo hay
        btnFavorite.setOnClickListener(view -> {
            changeButtonColor(btnFavorite, colorFavorite);
            Intent intent = new Intent(GoodTips.this, Activity_Favorite.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        // Khi người dùng nhấn nút tài khoản
        btnAcc.setOnClickListener(view -> {
            changeButtonColor(btnAcc, colorAcc);
            if (userId != null) {
                Intent intent = new Intent(GoodTips.this, UpdateInfor.class);
                intent.putExtra("userId", userId); // Truyền userId qua Intent
                startActivity(intent);
            } else {
                Toast.makeText(GoodTips.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });
        btnCook.setOnClickListener(view -> {
            changeButtonColor(btnCook, colorCook);
            Intent intent = new Intent(GoodTips.this, Activity_Home.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
    }

    private void loadTips() {
        database = FirebaseDatabase.getInstance();
        recipesRef = database.getReference("Tips");

        recipesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrTips.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Tips tips = data.getValue(Tips.class);
                    if (tips != null) {
                        tips.setId(data.getKey()); // Set key làm id
                        arrTips.add(tips);
                    }
                }
                tipsAdapter = new TipsAdapter(GoodTips.this, R.layout.layout_item_meohay, arrTips);
                lvTipsList.setAdapter(tipsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi
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
        if (button != btnBiQuyet) {
            btnBiQuyet.setBackgroundColor(defaultColor);
            btnBiQuyet.setTextColor(getResources().getColor(R.color.black));
        }
        if (button != btnAcc) {
            btnAcc.setBackgroundColor(defaultColor);
            btnAcc.setTextColor(getResources().getColor(R.color.black));
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
