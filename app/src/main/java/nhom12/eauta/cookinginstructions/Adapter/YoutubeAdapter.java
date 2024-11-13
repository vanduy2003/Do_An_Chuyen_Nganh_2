package nhom12.eauta.cookinginstructions.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import nhom12.eauta.cookinginstructions.Model.VideoCook;
import nhom12.eauta.cookinginstructions.R;

public class YoutubeAdapter extends ArrayAdapter<VideoCook> {
    Activity context;
    int IdLayout;
    ArrayList<VideoCook> arr;

    public YoutubeAdapter(Activity context, int IdLayout, ArrayList<VideoCook> arr) {
        super(context, IdLayout, arr);
        this.context = context;
        this.IdLayout = IdLayout;
        this.arr = arr;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(IdLayout, parent, false);
    }

    VideoCook videoCook = arr.get(position);

    TextView txtTitleVideo = convertView.findViewById(R.id.txtTitleVideo);
    WebView webView = convertView.findViewById(R.id.webView);

    txtTitleVideo.setText(videoCook.getTitle());
    webView.getSettings().setJavaScriptEnabled(true);
    webView.loadData(videoCook.getUrlVideo(), "text/html", "utf-8");

    return convertView;
}
}
