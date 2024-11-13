package nhom12.eauta.cookinginstructions.Adapter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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
            // Tạo và hiển thị AlertDialog xác nhận xóa
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc chắn muốn xóa món ăn này khỏi danh sách yêu thích không?");
            builder.setIcon(R.mipmap.chef);
            builder.setPositiveButton("Có", (dialog, which) -> {
                // Xóa ngay khỏi danh sách và cập nhật giao diện
                favoriteList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Đã xóa món ăn yêu thích", Toast.LENGTH_SHORT).show();

                // Gọi Firebase để xóa
                removeFavoriteFromFirebase(favorite.getRecipeId());
            });
            builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());

            // Hiển thị AlertDialog
            builder.show();
        });


        return convertView;
    }


    private void removeFavoriteFromFirebase(String recipeId) {
        DatabaseReference favoriteRef = FirebaseDatabase.getInstance().getReference("Favorites")
                .child(userId)
                .child(recipeId);

        favoriteRef.removeValue().addOnFailureListener(e -> {
            Log.e("FavoriteAdapter", "Lỗi khi xóa món ăn yêu thích", e);
            Toast.makeText(context, "Không thể xóa món ăn yêu thích", Toast.LENGTH_SHORT).show();
        });
    }



}
