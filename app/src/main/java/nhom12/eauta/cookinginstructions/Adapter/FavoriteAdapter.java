package nhom12.eauta.cookinginstructions.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import nhom12.eauta.cookinginstructions.Model.Favorite;
import nhom12.eauta.cookinginstructions.R;


public class FavoriteAdapter extends ArrayAdapter<Favorite> {
    private Context context;
    private int resource;
    private ArrayList<Favorite> favoriteList;
    private String userId;  // Thêm trường userId để biết người dùng hiện tại

    public FavoriteAdapter(Context context, int resource, ArrayList<Favorite> favoriteList, String userId) {
        super(context, resource, favoriteList);
        this.context = context;
        this.resource = resource;
        this.favoriteList = favoriteList;
        this.userId = userId;  // Lưu userId để sử dụng khi xóa
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
        }

        ImageView imgFavorite = convertView.findViewById(R.id.imgFavorite);
        TextView tvFavoriteTitle = convertView.findViewById(R.id.imgTipss);
        ImageView btnRemove = convertView.findViewById(R.id.btnRemove);

        Favorite favorite = favoriteList.get(position);

        tvFavoriteTitle.setText(favorite.getRecipeTitle());
        Glide.with(context).load(favorite.getRecipeImageUrl()).into(imgFavorite);

        // Xử lý khi người dùng bấm vào btnRemove
        btnRemove.setOnClickListener(v -> {
            // Xóa món ăn yêu thích khỏi Firebase
            removeFavoriteFromFirebase(favorite.getRecipeId(), position);
        });

        return convertView;
    }

    private void removeFavoriteFromFirebase(String recipeId, int position) {
        // Truy vấn đến Firebase để xóa món ăn yêu thích
        DatabaseReference favoriteRef = FirebaseDatabase.getInstance().getReference("Favorites")
                .child(userId) // Truy cập vào Favorites của người dùng
                .child(recipeId); // Truy cập vào món ăn yêu thích cần xóa

        favoriteRef.removeValue().addOnSuccessListener(aVoid -> {
            // Xóa thành công, cập nhật lại giao diện
            favoriteList.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Đã xóa món ăn yêu thích", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            // Nếu xảy ra lỗi
            Log.e("FavoriteAdapter", "Lỗi khi xóa món ăn yêu thích", e);
            Toast.makeText(context, "Không thể xóa món ăn yêu thích", Toast.LENGTH_SHORT).show();
        });
    }
}
