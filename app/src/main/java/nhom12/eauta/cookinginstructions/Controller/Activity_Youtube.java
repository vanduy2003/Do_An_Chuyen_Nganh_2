package nhom12.eauta.cookinginstructions.Controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import nhom12.eauta.cookinginstructions.Adapter.YoutubeAdapter;
import nhom12.eauta.cookinginstructions.Model.VideoCook;
import nhom12.eauta.cookinginstructions.R;

public class Activity_Youtube extends AppCompatActivity {
    private DatabaseReference videoRef;
    private ListView lvYoutube;
    private YoutubeAdapter youtubeAdapter;
    private ArrayList<VideoCook> videoList;
    private TextView btnAcc, btnFavorite, btnMeoHay, btnCamNang, btnCook;

    private int defaultColor;
    private int colorAcc;
    private int colorFavorite;
    private int colorMeoHay;
    private int colorBiQuyet;
    private int textColor;
    private int colorCook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        // Lấy userId từ SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = preferences.getString("userId", null);


        // Ánh xạ các view
        btnAcc = findViewById(R.id.btnAcount);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnMeoHay = findViewById(R.id.btnMeoHay);
        btnCamNang = findViewById(R.id.btnCamNang);
        btnCook = findViewById(R.id.btnCook);
        lvYoutube = findViewById(R.id.lvYoutube);
        videoList = new ArrayList<>();

        youtubeAdapter = new YoutubeAdapter(this, R.layout.layout_item_youtobe, videoList);
        lvYoutube.setAdapter(youtubeAdapter);

        // Khởi tạo màu sắc
        defaultColor = getResources().getColor(R.color.trang);
        colorAcc = getResources().getColor(R.color.hong);
        colorFavorite = getResources().getColor(R.color.xanhla);
        colorMeoHay = getResources().getColor(R.color.hongdam);
        colorBiQuyet = getResources().getColor(R.color.blue);
        textColor = getResources().getColor(R.color.trang);
        colorCook = getResources().getColor(R.color.tim);

        // Khởi tạo tham chiếu đến bảng "Videos" trong Firebase
        videoRef = FirebaseDatabase.getInstance().getReference("Videos");

        // Gọi hàm load video từ Firebase
        loadVideos();

        // bí quyết
        btnCamNang.setOnClickListener(view -> {
            changeButtonColor(btnCamNang,colorBiQuyet);
            Intent intent4 = new Intent(Activity_Youtube.this, Activity_HandBook.class);
            intent4.putExtra("UserId", userId);
            startActivity(intent4);
        });
        // Khi người dùng nhấn nút mẹo hay
        btnMeoHay.setOnClickListener(view -> {
            changeButtonColor(btnMeoHay, colorMeoHay);
            Intent intent1 = new Intent(Activity_Youtube.this, GoodTips.class);
            intent1.putExtra("UserId", userId);
            startActivity(intent1);
        });
        // Khi người dùng nhấn nút tài khoản
        btnAcc.setOnClickListener(view -> {
            changeButtonColor(btnAcc, colorAcc);
            if (userId != null) {
                Intent intent2 = new Intent(Activity_Youtube.this, UpdateInfor.class);
                intent2.putExtra("userId", userId); // Truyền userId qua Intent
                startActivity(intent2);
            } else {
                Toast.makeText(Activity_Youtube.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });

        // Khi người dùng nhấn nút yêu thích
        btnFavorite.setOnClickListener(view -> {
            changeButtonColor(btnFavorite, colorFavorite);
            Intent intent3 = new Intent(Activity_Youtube.this, Activity_Favorite.class);
            intent3.putExtra("UserId", userId); // Truyền userId qua Intent
            startActivity(intent3);
        });

        btnCook.setOnClickListener(view -> {
            changeButtonColor(btnCook, colorCook);
            Intent intent = new Intent(Activity_Youtube.this, Activity_Home.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
    }

    private void loadVideos() {
        videoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                videoList.clear();
                for (DataSnapshot videoSnapshot : snapshot.getChildren()) {
                    VideoCook video = videoSnapshot.getValue(VideoCook.class);
                    if (video != null) {
                        videoList.add(video);
                    }
                }
                youtubeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Activity_Youtube.this, "Failed to load videos", Toast.LENGTH_SHORT).show();
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
}
