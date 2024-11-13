package nhom12.eauta.cookinginstructions.Controller;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;

import nhom12.eauta.cookinginstructions.Adapter.YoutubeAdapter;
import nhom12.eauta.cookinginstructions.Model.VideoCook;
import nhom12.eauta.cookinginstructions.R;

public class Activity_Youtube extends AppCompatActivity {
    DatabaseReference videoRef;
    ListView lvYoutube;
    YoutubeAdapter youtubeAdapter;
    ArrayAdapter<VideoCook> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_youtube);

        lvYoutube = findViewById(R.id.lvYoutube);



    }
}