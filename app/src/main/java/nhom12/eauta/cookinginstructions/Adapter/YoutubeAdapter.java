package nhom12.eauta.cookinginstructions.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nhom12.eauta.cookinginstructions.Model.VideoCook;
import nhom12.eauta.cookinginstructions.R;

public class YoutubeAdapter extends ArrayAdapter<VideoCook> {
    private final Activity context;
    private final int IdLayout;
    private final ArrayList<VideoCook> arr;

    public YoutubeAdapter(Activity context, int IdLayout, ArrayList<VideoCook> arr) {
        super(context, IdLayout, arr);
        this.context = context;
        this.IdLayout = IdLayout;
        this.arr = arr;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(IdLayout, parent, false);
        }

        VideoCook videoCook = arr.get(position);

        TextView txtTitleVideo = convertView.findViewById(R.id.txtTitleVideo);
        WebView webView = convertView.findViewById(R.id.webView);

        txtTitleVideo.setText(videoCook.getTitle());

        // Cấu hình WebView để phát video
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        // Sử dụng WebViewClient để không mở ứng dụng bên ngoài
        webView.setWebViewClient(new WebViewClient());

        // Sử dụng WebChromeClient để hỗ trợ thêm tính năng (nếu cần)
        webView.setWebChromeClient(new WebChromeClient());

        // Kiểm tra URL và tạo URL nhúng cho YouTube
        if(videoCook.getUrlVideo() != null) {
            String youtubeEmbedUrl = "https://www.youtube.com/embed/" + extractYouTubeVideoId(videoCook.getUrlVideo());
            webView.loadUrl(youtubeEmbedUrl);
        }

        return convertView;
    }

    // Hàm trích xuất ID video từ URL YouTube
    private String extractYouTubeVideoId(String videoUrl) {
        String videoId = "";
        String pattern = "^(https?://)?(www\\.)?(youtube\\.com|youtu\\.?be)/.+$";

        if (videoUrl.matches(pattern)) {
            String[] parts = videoUrl.split("v=");
            if (parts.length > 1) {
                videoId = parts[1].split("&")[0];  // Trích xuất ID video
            } else if (videoUrl.contains("youtu.be/")) {
                // Nếu URL có dạng "youtu.be/VIDEO_ID"
                parts = videoUrl.split("youtu.be/");
                if (parts.length > 1) {
                    videoId = parts[1];
                }
            }
        }
        return videoId;
    }
}
