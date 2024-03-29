package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
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
import com.kongzue.dialog.v3.TipDialog;


public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView signUp, forgotPassword;


    FirebaseAuth auth;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.login_Email);
        password = findViewById(R.id.login_password);
        login = findViewById(R.id.login_loginButton);
        signUp = findViewById(R.id.login_singUpText);
        forgotPassword = findViewById(R.id.login_forgotPassword);

        loginMethod();
        signUpMethod();
        forgotPasswordMethod();

    }

    private void forgotPasswordMethod() {
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });
    }

    private void loginMethod(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) ){
                    TipDialog.show(LoginActivity.this,
                            "All fields are required", TipDialog.TYPE.WARNING).setTipTime(1500);
                }
                else {
                    auth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                        | Intent.FLAG_ACTIVITY_NEW_TASK));
                                        finish();
                                    }
                                    else {
                                        TipDialog.show(LoginActivity.this,
                                                "Incorrect Email or Password", TipDialog.TYPE.ERROR).setTipTime(1500);
                                    }
                                }
                            });
                }

            }
        });
    }

    private void signUpMethod(){
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }
}
