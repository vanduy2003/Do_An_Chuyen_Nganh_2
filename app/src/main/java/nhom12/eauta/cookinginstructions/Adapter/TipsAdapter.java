package nhom12.eauta.cookinginstructions.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nhom12.eauta.cookinginstructions.Model.Tips;
import nhom12.eauta.cookinginstructions.R;

public class TipsAdapter extends ArrayAdapter<Tips> {
    Activity context;
    int IdLayout;
    ArrayList<Tips> arrTip;

    public TipsAdapter(Activity context, int IdLayout, ArrayList<Tips> arr) {
        super(context, IdLayout, arr);
        this.context = context;
        this.IdLayout = IdLayout;
        this.arrTip = arr;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(IdLayout, null);

        Tips tips = arrTip.get(position);

        ImageView img = convertView.findViewById(R.id.imgTipss);

        String imageUrl = tips.getImageUrl();


        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context).load(imageUrl).into(img);
        } else {
            // Đặt ảnh mặc định hoặc xử lý khi không có ảnh
            img.setImageResource(R.drawable.baking); // Thay thế bằng ảnh mặc định của bạn
        }

        TextView txtTitle = convertView.findViewById(R.id.txtTipsTitle);
        txtTitle.setText(tips.getTitle());

        return convertView;
    }
}
