package nhom12.eauta.cookinginstructions.Controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nhom12.eauta.cookinginstructions.Model.Step;
import nhom12.eauta.cookinginstructions.Model.Tips;
import nhom12.eauta.cookinginstructions.R;

public class Activity_TipsDetail extends AppCompatActivity {
    private TextView txtNameF, txtTitle, txtDesc, txtTitleVideo;
    private LinearLayout layoutSteps;
    private FirebaseDatabase database;
    private DatabaseReference tipsRef;
    ImageView btnThoat;
    Button btnTym, btnShare;
    WebView webView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tips_detail);

        txtNameF = findViewById(R.id.txtNameF);
        txtTitle = findViewById(R.id.txtTitle);
        txtDesc = findViewById(R.id.txtDesc);
        txtTitleVideo = findViewById(R.id.txtTitleVideo);
        layoutSteps = findViewById(R.id.layoutSteps);
        btnThoat = findViewById(R.id.btnThoat);
        btnTym = findViewById(R.id.btnTym);
        btnShare = findViewById(R.id.btnShare);
        webView = findViewById(R.id.webView);

        String tipsId = getIntent().getStringExtra("TipsId");
        loadTipsDetail(tipsId);
    }

    private void loadTipsDetail(String tipsId) {
        database = FirebaseDatabase.getInstance();
        tipsRef = database.getReference("Tips").child(tipsId);

        tipsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Tips tips = snapshot.getValue(Tips.class);

                if (tips != null) {
                    // Cập nhật các thông tin cơ bản
                    txtNameF.setText(tips.getTitle());
                    txtTitle.setText(tips.getTitle());
                    txtDesc.setText(tips.getDescription());


                    // Cấu hình WebView để hiển thị và phát video trong app
                    if (tips.getUrlVideo() != null && !tips.getUrlVideo().isEmpty()) {
                        webView.getSettings().setJavaScriptEnabled(true);  // Kích hoạt JavaScript
                        webView.getSettings().setDomStorageEnabled(true);  // Kích hoạt DOM storage
                        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);  // Cho phép mở cửa sổ mới
                        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);  // Cho phép phát media không cần thao tác người dùng

                        // Sử dụng WebViewClient để không mở ứng dụng bên ngoài
                        webView.setWebViewClient(new WebViewClient());

                        // Hiển thị video YouTube nhúng
                        String youtubeEmbedUrl = "https://www.youtube.com/embed/" + extractYouTubeVideoId(tips.getUrlVideo());
                        webView.loadUrl(youtubeEmbedUrl);
                    }else {
                        txtTitleVideo.setVisibility(View.GONE);
                        webView.setVisibility(View.GONE);
                    }

                    // Hiển thị các bước trong layoutSteps
                    layoutSteps.removeAllViews(); // Xóa các view cũ trước khi thêm mới

                    for (Step step : tips.getSteps()) {
                        // Tạo TextView cho mỗi bước
                        TextView stepTextView = new TextView(Activity_TipsDetail.this);
                        stepTextView.setText("Cách " + step.getStepNumber() + ": " + "\n" + " - " + step.getDescription());
                        stepTextView.setTextSize(22);
                        stepTextView.setPadding(0, 20, 0, 20);
                        layoutSteps.addView(stepTextView);

                        // Tạo ImageView cho hình ảnh mỗi bước (nếu cần)
                        ImageView stepImageView = new ImageView(Activity_TipsDetail.this);
                        // Xử lý khi có ảnh
                        if (step.getImageUrl() != null && !step.getImageUrl().isEmpty()) {
                            // Load ảnh từ URL
                             Glide.with(Activity_TipsDetail.this).load(step.getImageUrl()).into(stepImageView);
                        } else {
                            // Đặt ảnh mặc định hoặc xử lý khi không có ảnh
                            stepImageView.setImageResource(R.drawable.baking); // Thay thế bằng ảnh mặc định của bạn
                        }
                        layoutSteps.addView(stepImageView);
                    }
                }
            }

            // Hàm trích xuất ID video từ URL YouTube
            private String extractYouTubeVideoId(String videoUrl) {
                String videoId = "";
                String pattern = "^(https?://)?(www\\.)?(youtube\\.com|youtu\\.?be)/.+$";

                if (videoUrl.matches(pattern)) {
                    String[] parts = videoUrl.split("v=");
                    if (parts.length > 1) {
                        videoId = parts[1].split("&")[0];  // Trích xuất ID video
                    }
                }
                return videoId;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi
            }
        });
    }


}

