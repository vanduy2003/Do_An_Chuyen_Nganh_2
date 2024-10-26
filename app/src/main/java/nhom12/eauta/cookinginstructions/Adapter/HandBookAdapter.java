package nhom12.eauta.cookinginstructions.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nhom12.eauta.cookinginstructions.Model.HandBook;
import nhom12.eauta.cookinginstructions.Model.Tips;
import nhom12.eauta.cookinginstructions.R;


public class HandBookAdapter extends ArrayAdapter<HandBook> {
    Activity context;
    int IdLayout;
    ArrayList<HandBook> arrHandBook;

    public HandBookAdapter(Activity context, int IdLayout, ArrayList<HandBook> arr) {
        super(context, IdLayout, arr);
        this.context = context;
        this.IdLayout = IdLayout;
        this.arrHandBook = arr;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(IdLayout, null);

        HandBook handBook = arrHandBook.get(position);

        ImageView img = convertView.findViewById(R.id.imgHandbook);

        String imageUrl = handBook.getImg();
        Glide.with(context).load(imageUrl).into(img);

        return convertView;
    }
}
