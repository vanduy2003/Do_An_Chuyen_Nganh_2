package nhom12.eauta.cookinginstructions.Controller;

import android.os.Bundle;
import android.widget.ListView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        lvYoutube = findViewById(R.id.lvYoutube);
        videoList = new ArrayList<>();

        youtubeAdapter = new YoutubeAdapter(this, R.layout.layout_item_youtobe, videoList);
        lvYoutube.setAdapter(youtubeAdapter);

        // Khởi tạo tham chiếu đến bảng "Videos" trong Firebase
        videoRef = FirebaseDatabase.getInstance().getReference("Videos");

        // Gọi hàm load video từ Firebase
        loadVideos();
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
}
