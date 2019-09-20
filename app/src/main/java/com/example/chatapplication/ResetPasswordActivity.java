package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.kongzue.dialog.v3.TipDialog;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText email;
    Button reset;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        email = findViewById(R.id.reset_Email);
        reset = findViewById(R.id.reset_resetButton);

        firebaseAuth = FirebaseAuth.getInstance();
        resetMethod();
    }

    private void resetMethod(){
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                if (txt_email.equals("")){
                    TipDialog.show(ResetPasswordActivity.this,
                            "Email is required", TipDialog.TYPE.WARNING).setTipTime(1500);
                }
                else {
                    firebaseAuth.sendPasswordResetEmail(txt_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                TipDialog.show(ResetPasswordActivity.this,
                                        "Please check you email", TipDialog.TYPE.SUCCESS).setTipTime(1500);
                            }
                            else {
                                TipDialog.show(ResetPasswordActivity.this,
                                        "Something went wrong", TipDialog.TYPE.ERROR).setTipTime(1000);
                            }
                        }
                    });
                }
            }
        });
    }
}
