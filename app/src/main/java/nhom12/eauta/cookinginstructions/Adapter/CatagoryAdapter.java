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

import nhom12.eauta.cookinginstructions.Model.CatagoryItem;
import nhom12.eauta.cookinginstructions.R;

public class CatagoryAdapter extends ArrayAdapter<CatagoryItem> {
    Activity context;
    int IdLayout;
    ArrayList<CatagoryItem> arr;

    public CatagoryAdapter(Activity context, int IdLayout, ArrayList<CatagoryItem> arr) {
        super(context, IdLayout, arr);
        this.context = context;
        this.IdLayout = IdLayout;
        this.arr = arr;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Tạo một đối tượng LayoutInflater để chuyển đổi từ file xml (layout) sang View
        LayoutInflater inflater = context.getLayoutInflater();
        // Chuyển đổi file xml (layout) sang View
        convertView = inflater.inflate(IdLayout, null);
        // Lấy ra 1 phần tử trong mảng
        CatagoryItem catagoryItem = arr.get(position);
        // Khai báo và ánh xạ ID và hiển thị lên giao diện
        ImageView img = convertView.findViewById(R.id.imgCatagory);

        // Sử dụng thư viện như Glide hoặc Picasso để tải ảnh từ URL
        Glide.with(context).load(catagoryItem.getImage()).into(img);

        TextView txtName = convertView.findViewById(R.id.txtName);
        txtName.setText(catagoryItem.getName());

        return convertView;
    }

}
