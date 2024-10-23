package nhom12.eauta.cookinginstructions;

import static android.app.ProgressDialog.show;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nhom12.eauta.cookinginstructions.Controller.Activity_Home;
import nhom12.eauta.cookinginstructions.Controller.Activity_Register;
import nhom12.eauta.cookinginstructions.R;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private TextView signupText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo Firebase Database
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");

        // Liên kết UI elements
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupText = findViewById(R.id.signupText);

        // Xử lý sự kiện khi nhấn nút đăng nhập
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                loginUser(email, password);
            }
        });

        // Xử lý sự kiện khi nhấn chữ "Đăng ký"
        signupText.setOnClickListener(v -> {
            // Chuyển đến màn hình đăng ký
            Intent intent = new Intent(MainActivity.this, Activity_Register.class);
            startActivity(intent);
        });

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");

        if (email != null && password != null) {
            emailEditText.setText(email);
            passwordEditText.setText(password);
        }
    }

    private void loginUser(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Đang đăng nhập...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        usersRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        progressDialog.dismiss();
                        Log.d("Login", "onDataChange called");
                        if (dataSnapshot.exists()) {
                            Log.d("Login", "User exists");
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String storedPassword = snapshot.child("password").getValue(String.class);
                                if (storedPassword != null && storedPassword.equals(password)) {
                                    Log.d("Login", "Password matches");
                                    String uid = snapshot.getKey();
                                    saveUserId(uid);
                                    Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, Activity_Home.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.d("Login", "Password does not match");
                                    Toast.makeText(MainActivity.this, "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Log.d("Login", "Email does not exist");
                            Toast.makeText(MainActivity.this, "Email không tồn tại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressDialog.dismiss();
                        Log.w("Firebase", "loadUser:onCancelled", databaseError.toException());
                        Toast.makeText(MainActivity.this, "Lỗi kết nối đến Firebase. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void saveUserId(String uid) {
        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userId", uid);
        editor.apply();
    }
}