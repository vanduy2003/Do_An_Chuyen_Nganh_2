package nhom12.eauta.cookinginstructions.Controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

import nhom12.eauta.cookinginstructions.R;

public class Activity_UpdateInfo extends AppCompatActivity {

    private EditText emailEditText, phoneEditText, passwordEditText, addressEditText, usernameEditText, avatarUrlEditText, txtBio, txtSex, txtBirthday;
    private DatabaseReference mDatabase;
    private String userId;
    private String avatarUrl; // Thay đổi: Thêm biến để lưu URL ảnh đại diện
    FloatingActionButton btnThoat;
    Button saveButton;


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
        avatarUrlEditText = findViewById(R.id.avatarUrlEditText);
        saveButton = findViewById(R.id.saveButton);
        btnThoat = findViewById(R.id.btnThoat);
        txtBirthday = findViewById(R.id.txtBirthday);
        txtSex = findViewById(R.id.txtSex);

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
                    avatarUrlEditText.setText(avatarUrl);
                    txtSex.setText(sex);
                    txtBirthday.setText(birthday);
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
            }
            finish();
        });

        // Close button listener
        btnThoat.setOnClickListener(v -> finish());
    }

    private void updateUserData() {
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String avatarUrl = avatarUrlEditText.getText().toString().trim();
        String bio = txtBio.getText().toString().trim();
        String sex = txtSex.getText().toString().trim();
        String birthday = txtBirthday.getText().toString().trim();

        // Update user data in Firebase
        mDatabase.child("email").setValue(email);
        mDatabase.child("phone").setValue(phone);
        mDatabase.child("password").setValue(password);
        mDatabase.child("address").setValue(address);
        mDatabase.child("username").setValue(username);
        mDatabase.child("avatar").setValue(avatarUrl);
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
    }

    private boolean isInputValid() {
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

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


}

