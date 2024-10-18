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
import nhom12.eauta.cookinginstructions.Model.DishItem;
import nhom12.eauta.cookinginstructions.R;

public class TopAdapter extends ArrayAdapter<CatagoryItem> {
    Activity context;
    int IdLayout;
    ArrayList<CatagoryItem> arr;

    public TopAdapter(Activity context, int IdLayout, ArrayList<CatagoryItem> arr) {
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

        CatagoryItem catagoryItem = arr.get(position);

        ImageView imgTop = convertView.findViewById(R.id.imgTop);
        TextView tvTopTitle = convertView.findViewById(R.id.tvTopTitle);

        tvTopTitle.setText(catagoryItem.getName());
        Glide.with(context).load(catagoryItem.getImage()).into(imgTop);

        return convertView;
    }
}

