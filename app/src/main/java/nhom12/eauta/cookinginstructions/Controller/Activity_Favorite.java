package nhom12.eauta.cookinginstructions.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import nhom12.eauta.cookinginstructions.Model.Favorite;
import nhom12.eauta.cookinginstructions.R;
import nhom12.eauta.cookinginstructions.Adapter.FavoriteAdapter;

public class Activity_Favorite extends AppCompatActivity {
    private ListView lvFavorites;
    private FirebaseDatabase database;
    private DatabaseReference favoritesRef;
    private ArrayList<Favorite> favoriteList;
    private FavoriteAdapter adapter;
    FloatingActionButton btnThoat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        lvFavorites = findViewById(R.id.lvFavorites);
        favoriteList = new ArrayList<>();
        adapter = new FavoriteAdapter(this, R.layout.favorite_item, favoriteList);
        lvFavorites.setAdapter(adapter);

        // Lấy userId của người dùng hiện tại
        String userId = getIntent().getStringExtra("UserId");

        // Truy vấn các món ăn yêu thích từ Firebase dựa trên userId
        loadFavoriteRecipes(userId);

        lvFavorites.setOnItemClickListener((parent, view, position, id) -> {
            Favorite favorite = favoriteList.get(position);
            String recipeId = favorite.getRecipeId();
            Intent intent = new Intent(Activity_Favorite.this, Activity_RecipeDetail.class);
            intent.putExtra("RecipeId", recipeId);
            startActivity(intent);
        });

        btnThoat = findViewById(R.id.btnThoat);
        btnThoat.setOnClickListener(v -> {
           finish();
        });
    }


    private void loadFavoriteRecipes(String userId) {
        database = FirebaseDatabase.getInstance();
        favoritesRef = database.getReference("Favorites").child(userId);

        ProgressDialog progressDialog = new ProgressDialog(Activity_Favorite.this);
        progressDialog.setMessage("Đang tải...");
        progressDialog.show();

        favoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                favoriteList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Favorite favorite = snapshot.getValue(Favorite.class);
                    favoriteList.add(favorite);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Log.w("Firebase", "loadFavoriteRecipes:onCancelled", databaseError.toException());
            }
        });
    }
}
