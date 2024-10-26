package nhom12.eauta.cookinginstructions.Controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import nhom12.eauta.cookinginstructions.R;

public class Activity_UpdateInfo extends AppCompatActivity {

    private EditText emailEditText, phoneEditText, passwordEditText, addressEditText, usernameEditText, txtBio, txtSex, txtBirthday;
    private DatabaseReference mDatabase;
    private String userId;
    private ImageView imgAvatar; // Thay đổi: Thêm biến để lưu URL ảnh đại diện
    ImageView btnThoat;
    private Button saveButton;
    private static final int PICK_IMAGE_REQUEST = 1;
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_info);

        // Initialize UI components
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        addressEditText = findViewById(R.id.addressEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        txtBio = findViewById(R.id.txtBio);
        saveButton = findViewById(R.id.saveButton);
        btnThoat = findViewById(R.id.btnThoat);
        txtBirthday = findViewById(R.id.txtBirthday);
        txtSex = findViewById(R.id.txtSex);
        imgAvatar = findViewById(R.id.imgAvatar);
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



        // Thêm sự kiện click cho imgAvatar
        imgAvatar.setOnClickListener(v -> openGallery());
        // Get the userId from the Intent
        userId = getIntent().getStringExtra("userId");

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        // Retrieve user data from Firebase
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Assuming the user data model has these fields
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String password = dataSnapshot.child("password").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String avatarUrl = dataSnapshot.child("avatar").getValue(String.class);
                    String bio = dataSnapshot.child("favoritefood").getValue(String.class);
                    String sex = dataSnapshot.child("sex").getValue(String.class);
                    String birthday = dataSnapshot.child("birthday").getValue(String.class);

                    // Update UI with retrieved data
                    emailEditText.setText(email);
                    phoneEditText.setText(phone);
                    passwordEditText.setText(password);
                    addressEditText.setText(address);
                    usernameEditText.setText(username);
                    txtBio.setText(bio);
                    txtSex.setText(sex);
                    txtBirthday.setText(birthday);

                    // Load ảnh đại diện từ URL
                    if (avatarUrl != null) {
                        // Load ảnh từ URL
                        Glide.with(Activity_UpdateInfo.this)
                                .load(avatarUrl)
                                .into(imgAvatar);
                    }
                } else {
                    Toast.makeText(Activity_UpdateInfo.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Activity_UpdateInfo.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
            }
        });

        // Save button click listener
        saveButton.setOnClickListener(v -> {
            if (isInputValid()) {
                updateUserData();
                System.out.println("Save button clicked");
            }
            finish();
        });

        btnThoat.setOnClickListener(v -> {
            finish();
        });

        btnBiQuyet.setOnClickListener(view -> {
            changeButtonColor(btnBiQuyet,colorBiQuyet);
            Intent intent = new Intent(Activity_UpdateInfo.this, Activity_HandBook.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        btnMeoHay.setOnClickListener(view -> {
            changeButtonColor(btnMeoHay, colorMeoHay);
            Intent intent = new Intent(Activity_UpdateInfo.this, GoodTips.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        btnAcc.setOnClickListener(view -> {
            changeButtonColor(btnAcc, colorAcc);
            if (userId != null) {
                Intent intent = new Intent(Activity_UpdateInfo.this, UpdateInfor.class);
                intent.putExtra("userId", userId); // Truyền userId qua Intent
                startActivity(intent);
            } else {
                Toast.makeText(Activity_UpdateInfo.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });
        btnCook.setOnClickListener(view -> {
            changeButtonColor(btnCook, colorCook);
            Intent intent = new Intent(Activity_UpdateInfo.this, Activity_Home.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
        btnFavorite.setOnClickListener(view -> {
            changeButtonColor(btnFavorite, colorFavorite);
            Intent intent = new Intent(Activity_UpdateInfo.this, Activity_Favorite.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
        });
    }

    private void updateUserData() {
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String bio = txtBio.getText().toString().trim();
        String sex = txtSex.getText().toString().trim();
        String birthday = txtBirthday.getText().toString().trim();

        // Update user data in Firebase Database
        try {
            // Update user data in Firebase
            mDatabase.child("email").setValue(email);
            mDatabase.child("phone").setValue(phone);
            mDatabase.child("password").setValue(password);
            mDatabase.child("address").setValue(address);
            mDatabase.child("username").setValue(username);
            mDatabase.child("sex").setValue(sex);
            mDatabase.child("birthday").setValue(birthday);
            mDatabase.child("favoritefood").setValue(bio)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Activity_UpdateInfo.this, "User data updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Activity_UpdateInfo.this, "Failed to update user data", Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (Exception e){
            Toast.makeText(Activity_UpdateInfo.this, "Failed to update user data", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isInputValid() {
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Password phải có ít nhất 6 ký tự
        if (password.length() < 6) {
            passwordEditText.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email không hợp lệ");
            return false;
        }

        if (!Patterns.PHONE.matcher(phone).matches()) {
            phoneEditText.setError("Số điện thoại không hợp lệ");
            return false;
        }

        // Thêm các kiểm tra khác nếu cần
        return true;
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    // Chuyển URI thành Bitmap để hiển thị trong ImageView
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    imgAvatar.setImageBitmap(bitmap);

                    // Upload ảnh lên Firebase Storage
                    uploadImageToFirebase(imageUri);

                } catch (Exception e) {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Thêm phương thức uploadImageToFirebase
    private void uploadImageToFirebase(Uri imageUri) {
        // Tạo đường dẫn trong Firebase Storage với userId
        String fileName = "avatars/" + userId + ".jpg";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child(fileName);

        // Upload file lên Firebase Storage
        storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            // Lấy URL của ảnh đã upload
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String downloadUrl = uri.toString();

                // Cập nhật URL ảnh vào Firebase Database
                mDatabase.child("avatar").setValue(downloadUrl)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(Activity_UpdateInfo.this, "Avatar updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Activity_UpdateInfo.this, "Failed to update avatar URL", Toast.LENGTH_SHORT).show();
                            }
                        });
            }).addOnFailureListener(e -> {
                Toast.makeText(Activity_UpdateInfo.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(Activity_UpdateInfo.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
        });
    }



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

