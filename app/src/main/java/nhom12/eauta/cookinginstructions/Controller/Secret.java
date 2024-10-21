package nhom12.eauta.cookinginstructions.Controller;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Arrays;
import java.util.List;

import nhom12.eauta.cookinginstructions.Adapter.SecretAdapter;
import nhom12.eauta.cookinginstructions.R;

public class Secret extends AppCompatActivity {
    private ViewPager2 viewPager;
    ImageView btnThoat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret);
        viewPager = findViewById(R.id.banner_goodtips);
        List<Integer> bannerList = Arrays.asList(
                R.drawable.banner4,
                R.drawable.img_3,
                R.drawable.img_1
        );

        SecretAdapter adapter = new SecretAdapter(bannerList);
        viewPager.setAdapter(adapter);

        // Tự động chuyển ảnh sau mỗi 3 giây
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int currentPage = 0;

            @Override
            public void run() {
                if (currentPage == bannerList.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 2000); // Chuyển ảnh sau 2 giây
            }
        };
        handler.postDelayed(runnable, 2000);
        btnThoat = findViewById(R.id.btnThoat);
        btnThoat.setOnClickListener(v -> finish());
    }
}
