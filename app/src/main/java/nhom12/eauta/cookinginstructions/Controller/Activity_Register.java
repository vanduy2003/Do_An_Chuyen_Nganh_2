package nhom12.eauta.cookinginstructions.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import nhom12.eauta.cookinginstructions.MainActivity;
import nhom12.eauta.cookinginstructions.Model.User;
import nhom12.eauta.cookinginstructions.R;

public class Activity_Register extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText;
    private Button registerButton;
    private TextView signinText;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Khởi tạo Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Ánh xạ các view
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.RegisterButton);
        signinText = findViewById(R.id.signinText);

        // Xử lý sự kiện khi nhấn nút đăng ký
        registerButton.setOnClickListener(v -> registerUser());

        // Xử lý sự kiện khi nhấn vào TextView để chuyển đến màn hình đăng nhập
        signinText.setOnClickListener(v -> {
            // Chuyển đến màn hình đăng nhập (cần xác định hoạt động đăng nhập)
            finish(); // Hoặc startActivity với intent nếu cần chuyển đến Activity đăng nhập
        });
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Activity_Register.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(Activity_Register.this);
        progressDialog.setMessage("Đang tải...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        FirebaseUser User = mAuth.getCurrentUser();
                        if (User != null) {
                            String userId = User.getUid();
                            nhom12.eauta.cookinginstructions.Model.User newUser = new User(username, password, email, null, null, null, null, null, null);
                            usersRef.child(userId).setValue(newUser)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(Activity_Register.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Activity_Register.this, MainActivity.class);
                                            intent.putExtra("email", email);
                                            intent.putExtra("password", password);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(Activity_Register.this, "Lỗi khi lưu thông tin người dùng", Toast.LENGTH_SHORT).show();
                                            Log.e("RegisterError", "Lỗi khi lưu thông tin: ", task1.getException());
                                        }
                                    });
                        }
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(Activity_Register.this, "Đăng ký thất bại: " + errorMessage, Toast.LENGTH_LONG).show();
                        Log.e("RegisterError", "Đăng ký thất bại: ", task.getException());
                    }
                });

    }



}
