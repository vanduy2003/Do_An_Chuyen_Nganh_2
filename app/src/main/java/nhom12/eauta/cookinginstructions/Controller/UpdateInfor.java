package nhom12.eauta.cookinginstructions.Controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nhom12.eauta.cookinginstructions.MainActivity;
import nhom12.eauta.cookinginstructions.R;

public class UpdateInfor extends AppCompatActivity {

    ImageView imgAvatar;
    TextView txtName, txtSex, txtEmail, txtPhone, txtAddress, txtFavorite, txtBirthday;
    ImageView btnThoat, btnSetting;
    private int defaultColor;
    private int colorAcc;
    private int colorFavorite;
    private int colorMeoHay;
    private int colorBiQuyet;
    private int textColor;
    private int colorCook;
    private TextView btnAcc, btnFavorite, btnMeoHay, btnBiQuyet, btnCook;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_infor);

        btnThoat = findViewById(R.id.btnThoat);
        txtName = findViewById(R.id.txtNamef);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);
        txtFavorite = findViewById(R.id.txtFavorite);
        txtBirthday = findViewById(R.id.txtBirthday);
        imgAvatar = findViewById(R.id.imgAvatar);
        txtSex = findViewById(R.id.txtSex);
        btnSetting = findViewById(R.id.btnSetting);
        btnCook = findViewById(R.id.btnCook);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnMeoHay = findViewById(R.id.btnMeoHay);
        btnBiQuyet = findViewById(R.id.btnBiQuyet);
        btnAcc = findViewById(R.id.btnAcount);

        defaultColor = getResources().getColor(R.color.trang);
        colorAcc = getResources().getColor(R.color.hong);
        colorFavorite = getResources().getColor(R.color.xanhla);
        colorMeoHay = getResources().getColor(R.color.hongdam);
        colorBiQuyet = getResources().getColor(R.color.blue);
        textColor = getResources().getColor(R.color.trang);
        colorCook = getResources().getColor(R.color.tim);


        String userId = getIntent().getStringExtra("userId");

        // Thiết lập Firebase Database reference
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId); // Thay "userId" bằng id thực tế của người dùng

        ProgressDialog progressDialog = new ProgressDialog(UpdateInfor.this);
        progressDialog.setMessage("Đang tải...");
        progressDialog.show();

        // Thêm xử lý cho btnSetting
        btnSetting.setOnClickListener(v -> {
            // Tạo PopupMenu
            PopupMenu popupMenu = new PopupMenu(UpdateInfor.this, btnSetting);
            popupMenu.getMenuInflater().inflate(R.menu.menu_setting, popupMenu.getMenu());

            // Xử lý sự kiện khi chọn mục trong menu
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.btnUpdate) {
                    // Xử lý cập nhật thông tin ở đây
                    Intent intent = new Intent(UpdateInfor.this, Activity_UpdateInfo.class);
                    intent.putExtra("userId", getIntent().getStringExtra("userId"));
                    startActivity(intent);
                    return true; // Trả về true nếu xử lý thành công
                } else if (item.getItemId() == R.id.btnDangxuat) {
                    // Xử lý đăng xuất
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Xác nhận");
                    builder.setMessage("Bạn có muốn đăng xuất không?");
                    builder.setIcon(R.mipmap.cooking);
                    builder.setPositiveButton("Có", (dialog, which) -> {
                        Intent intent = new Intent(UpdateInfor.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Kết thúc activity hiện tại
                    });
                    builder.setNegativeButton("Không", (dialog, which) -> dialog.cancel());
                    builder.create().show();
                    return true;
                }
                return false; // Trả về false nếu không xử lý
            });

            // Hiển thị menu
            popupMenu.show();
        });


        // Truy vấn dữ liệu từ Firebase
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if (dataSnapshot.exists()) {
                    // Lấy dữ liệu từ DataSnapshot
                    String name = dataSnapshot.child("username").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);
                    String favorite = dataSnapshot.child("favoritefood").getValue(String.class);
                    String birthday = dataSnapshot.child("birthday").getValue(String.class);
                    String avatarUrl = dataSnapshot.child("avatar").getValue(String.class);
                    String sex = dataSnapshot.child("sex").getValue(String.class);

                    // Cập nhật giao diện với dữ liệu
                    txtName.setText(name);
                    txtEmail.setText(email);
                    txtPhone.setText(phone);
                    txtAddress.setText(address);
                    txtFavorite.setText(favorite);
                    txtBirthday.setText(birthday);
                    txtSex.setText(sex);

                    // Sử dụng Glide để load ảnh avatar
                    Glide.with(UpdateInfor.this).load(avatarUrl).into(imgAvatar);
                } else {
                    Toast.makeText(UpdateInfor.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi truy vấn
                progressDialog.dismiss();
                Toast.makeText(UpdateInfor.this, "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnThoat.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateInfor.this, Activity_Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Hoặc gọi finish() nếu bạn muốn kết thúc Activity hiện tại
        });

        // check gọi menu để chuyển trang
        // bí quyết
        btnBiQuyet.setOnClickListener(view -> {
            changeButtonColor(btnBiQuyet,colorBiQuyet);
            Intent intent = new Intent(UpdateInfor.this, Secret.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        // Khi người dùng nhấn nút mẹo hay
        btnMeoHay.setOnClickListener(view -> {
            changeButtonColor(btnMeoHay, colorMeoHay);
            Intent intent = new Intent(UpdateInfor.this, GoodTips.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        // Khi người dùng nhấn nút mẹo hay
        btnFavorite.setOnClickListener(view -> {
            changeButtonColor(btnFavorite, colorFavorite);
            Intent intent = new Intent(UpdateInfor.this, GoodTips.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });

        btnCook.setOnClickListener(view -> {
            changeButtonColor(btnCook, colorCook);
            Intent intent = new Intent(UpdateInfor.this, Activity_Home.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
    }

    // màu cho menu
    private void changeButtonColor(TextView button, int color) {
        // Đặt màu nền
        button.setBackgroundColor(color);
        // Đặt màu chữ thành trắng
        button.setTextColor(textColor);

        // Khôi phục lại màu sắc và chữ cho các nút khác
        if (button != btnAcc) {
            btnAcc.setBackgroundColor(defaultColor);
            btnAcc.setTextColor(getResources().getColor(R.color.black)); // Màu chữ mặc định
        }
        if (button != btnFavorite) {
            btnFavorite.setBackgroundColor(defaultColor);
            btnFavorite.setTextColor(getResources().getColor(R.color.black));
        }
        if (button != btnMeoHay) {
            btnMeoHay.setBackgroundColor(defaultColor);
            btnMeoHay.setTextColor(getResources().getColor(R.color.black));
        }
        if (button != btnBiQuyet) {
            btnBiQuyet.setBackgroundColor(defaultColor);
            btnBiQuyet.setTextColor(getResources().getColor(R.color.black));
        }
        if (button != btnCook) {
            btnCook.setBackgroundColor(defaultColor);
            btnCook.setTextColor(getResources().getColor(R.color.black));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Khôi phục màu sắc và chữ mặc định cho các nút
        btnAcc.setBackgroundColor(defaultColor);
        btnAcc.setTextColor(getResources().getColor(R.color.black)); // Đặt màu chữ mặc định

        btnFavorite.setBackgroundColor(defaultColor);
        btnFavorite.setTextColor(getResources().getColor(R.color.black));

        btnMeoHay.setBackgroundColor(defaultColor);
        btnMeoHay.setTextColor(getResources().getColor(R.color.black));

        btnBiQuyet.setBackgroundColor(defaultColor);
        btnBiQuyet.setTextColor(getResources().getColor(R.color.black));
        btnCook.setBackgroundColor(defaultColor);
        btnCook.setTextColor(getResources().getColor(R.color.black));
    }

}
