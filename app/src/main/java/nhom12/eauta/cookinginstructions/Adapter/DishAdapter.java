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

import nhom12.eauta.cookinginstructions.Model.DishItem;
import nhom12.eauta.cookinginstructions.R;

public class DishAdapter extends ArrayAdapter<DishItem> {
    Activity context;
    int IdLayout;
    ArrayList<DishItem> arr;

    public DishAdapter(Activity context, int IdLayout, ArrayList<DishItem> arr) {
        super(context, IdLayout, arr);
        this.context = context;
        this.IdLayout = IdLayout;
        this.arr = arr;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(IdLayout, null);

        DishItem dishItem = arr.get(position);

        ImageView img = convertView.findViewById(R.id.imgDish);

        String imageUrl = dishItem.getImg();


        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context).load(imageUrl).into(img);
        } else {
            // Đặt ảnh mặc định hoặc xử lý khi không có ảnh
            img.setImageResource(R.drawable.a1); // Thay thế bằng ảnh mặc định của bạn
        }

        TextView txtTitle = convertView.findViewById(R.id.txtDishTitle);
        txtTitle.setText(dishItem.getTitle());

        return convertView;
    }
}
