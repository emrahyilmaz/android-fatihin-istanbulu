package com.dnkilic.fatihinstanbulu.LoginProcess;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dnkilic.fatihinstanbulu.MainActivity;
import com.dnkilic.fatihinstanbulu.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {
    private static final int RC_SIGN_IN = 1000;
    private static final String TAG = "RAPOR";
    private FirebaseAuth mAuth;
    private DatabaseReference mFireBaseDatabase;
    private Button mLoginButton;
    private SignInButton mloginGoogleButton;
    private EditText mUserEmail, mUserPassword;
    private RelativeLayout mMainLayout;
    private GoogleApiClient mGoogleApiClient;


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
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mloginGoogleButton= (SignInButton) findViewById(R.id.loginButtonGoogle);
        mloginGoogleButton.setSize(SignInButton.SIZE_STANDARD);
        mloginGoogleButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }


    private void girisDogrulama() {
        String email = this.mUserEmail.getText().toString().trim();
        String sifre = this.mUserPassword.getText().toString().trim();

        if (!email.isEmpty() && !sifre.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isComplete()) {
                        Snackbar.make(mMainLayout, R.string.loginError1, Snackbar.LENGTH_LONG)
                                .setAction(R.string.register, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(getApplicationContext(), Register.class));
                                        finish();
                                    }
                                }).show();
                    } else {
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
                } else {
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                girisDogrulama();
                break;
            case R.id.loginButtonGoogle:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent=Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("İsim",acct.getDisplayName());
            String isim=acct.getDisplayName();
            String email=acct.getEmail();
            String userId=acct.getId();
            DatabaseReference userDatabaseId=mFireBaseDatabase.child(userId);
            userDatabaseId.child("isim").setValue(isim);
            userDatabaseId.child("email").setValue(email);

            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getId(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Authentication pass.",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });



            startActivity(new Intent(LoginActivity.this,MainActivity.class));



        }
    }

}