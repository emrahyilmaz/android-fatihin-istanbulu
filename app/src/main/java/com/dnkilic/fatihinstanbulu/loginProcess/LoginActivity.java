package com.dnkilic.fatihinstanbulu.loginProcess;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.dnkilic.fatihinstanbulu.MainActivity;
import com.dnkilic.fatihinstanbulu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private DatabaseReference mFireBaseDatabase;
    private Button mLoginButton;
    private EditText mUserEmail, mUserPassword;
    private RelativeLayout mMainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mFireBaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mLoginButton = (Button) findViewById(R.id.loginButton);
        mUserEmail = (EditText) findViewById(R.id.loginEmailField);
        mUserPassword = (EditText) findViewById(R.id.loginPassField);
        mMainLayout = (RelativeLayout) findViewById(R.id.activity_login);


    }




    private void girisDogrulama() {
        String email = this.mUserEmail.getText().toString().trim();
        String sifre = this.mUserPassword.getText().toString().trim();

        if (!email.isEmpty() && !sifre.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isComplete()) {
                        Snackbar.make(mMainLayout, R.string.loginError1, Snackbar.LENGTH_LONG)
                                .setAction(R.string.register, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(getApplicationContext(), Register.class));
                                        finish();
                                    }
                                }).show();
                    }else{
                        checkUser();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar snackbar = Snackbar
                            .make(mMainLayout, R.string.loginError1, Snackbar.LENGTH_LONG)
                            .setAction(R.string.register, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(getApplicationContext(), Register.class));
                                    finish();
                                }
                            });

                    snackbar.show();
                }
            });
        }
    }

    private void checkUser() {
        final String userId = mAuth.getCurrentUser().getUid();
        mFireBaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId)) {
                    Intent internt = new Intent(LoginActivity.this, MainActivity.class);
                    internt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(internt);
                }else{
                    Snackbar snackbar = Snackbar
                            .make(mMainLayout, R.string.loginError1, Snackbar.LENGTH_LONG)
                            .setAction(R.string.register, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(getApplicationContext(), Register.class));
                                    finish();
                                }
                            });

                    snackbar.show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar snackbar = Snackbar
                        .make(mMainLayout, R.string.loginError1, Snackbar.LENGTH_LONG)
                        .setAction(R.string.register, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(getApplicationContext(), Register.class));
                                finish();
                            }
                        });

                snackbar.show();

            }
        });
    }


    public void onClick(View view) {
        girisDogrulama();
    }
}