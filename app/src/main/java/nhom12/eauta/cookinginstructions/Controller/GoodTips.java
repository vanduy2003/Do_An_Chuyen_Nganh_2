package nhom12.eauta.cookinginstructions.Controller;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_tips);

        // Khởi tạo các thành phần
        viewPager = findViewById(R.id.banner_goodtips);
        btnThoat = findViewById(R.id.btnThoat);
        lvTipsList = findViewById(R.id.lvTipsList);

        List<Integer> bannerList = Arrays.asList(
                R.drawable.banner4,
                R.drawable.img_3,
                R.drawable.img_1
        );

        GoodTipsAdapter adapter = new GoodTipsAdapter(bannerList);
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
                handler.postDelayed(this, 2000); // Chuyển ảnh sau 2  giây
            }
        };
        handler.postDelayed(runnable, 2000);

        loadTips();

        btnThoat.setOnClickListener(v -> finish());
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
                    arrTips.add(tips);
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
}
