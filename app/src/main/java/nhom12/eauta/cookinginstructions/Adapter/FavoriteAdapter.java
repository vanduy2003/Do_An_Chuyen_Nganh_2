package nhom12.eauta.cookinginstructions.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nhom12.eauta.cookinginstructions.Model.Favorite;
import nhom12.eauta.cookinginstructions.R;

public class FavoriteAdapter extends ArrayAdapter<Favorite> {
    private Context context;
    private int resource;
    private ArrayList<Favorite> favoriteList;

    public FavoriteAdapter(Context context, int resource, ArrayList<Favorite> favoriteList) {
        super(context, resource, favoriteList);
        this.context = context;
        this.resource = resource;
        this.favoriteList = favoriteList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
        }

        ImageView imgFavorite = convertView.findViewById(R.id.imgFavorite);
        TextView tvFavoriteTitle = convertView.findViewById(R.id.imgTipss);

        Favorite favorite = favoriteList.get(position);

        tvFavoriteTitle.setText(favorite.getRecipeTitle());
        Glide.with(context).load(favorite.getRecipeImageUrl()).into(imgFavorite);

        return convertView;
    }

    // Phương thức để xóa item
    public void removeItem(int position) {
        if (position >= 0 && position < favoriteList.size()) {
            favoriteList.remove(position);
            notifyDataSetChanged(); // Cập nhật giao diện
        }
    }
}
