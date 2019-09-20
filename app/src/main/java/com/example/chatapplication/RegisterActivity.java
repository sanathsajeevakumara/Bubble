package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;


import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText username, email, password;
    Button registerBtn;
    TextView register_singInText;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        Toolbar toolbar = findViewById(R.id.register_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Register");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        auth = FirebaseAuth.getInstance();

        username = findViewById(R.id.register_username);
        email = findViewById(R.id.register_Email);
        password = findViewById(R.id.register_password);
        registerBtn = findViewById(R.id.register_registerButton);
        register_singInText = findViewById(R.id.register_singInText);

        registerMethod();
        signInMethod();


    }

    private void registerMethod(){
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String txt_userName = username.getText().toString();
                String txt_email = email.getText().toString();
                final String txt_password = password.getText().toString();

                WaitDialog.show(RegisterActivity.this, "Uploading...");

                if (TextUtils.isEmpty(txt_userName) || TextUtils.isEmpty(txt_email) ||
                        TextUtils.isEmpty(txt_password) ){
                    TipDialog.show(RegisterActivity.this,
                            "All fields are required", TipDialog.TYPE.WARNING).setTipTime(1500);
                } else if (txt_password.length() < 6){
                    TipDialog.show(RegisterActivity.this,
                            "Password must be at least 6 characters", TipDialog.TYPE.WARNING).setTipTime(1500);
                }
                else {
                    auth.createUserWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        FirebaseUser user = auth.getCurrentUser();
                                        String userID = user.getUid();

                                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("id", userID);
                                        hashMap.put("username", txt_userName);
                                        hashMap.put("imageURL", "default");
                                        hashMap.put("isOnline", "offline");
                                        hashMap.put("search", txt_userName.toLowerCase());

                                        reference.setValue(hashMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful())
                                                        {
                                                            startActivity(new Intent(getApplicationContext(), MainActivity.class)
                                                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                                            | Intent.FLAG_ACTIVITY_NEW_TASK));

                                                            TipDialog.show(RegisterActivity.this,
                                                                    "Account create successfully", TipDialog.TYPE.SUCCESS).setTipTime(1500);
                                                            finish();
                                                        }
                                                    }
                                                });
                                    } else
                                        TipDialog.show(RegisterActivity.this,
                                                "You can't create account with this Email or Password", TipDialog.TYPE.ERROR).setTipTime(1500);
                                }
                            });
                }

            }
        });
    }

    private void signInMethod(){
        register_singInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}
