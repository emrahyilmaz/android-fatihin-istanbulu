package com.dnkilic.fatihinstanbulu.LoginProcess;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dnkilic.fatihinstanbulu.MainActivity;
import com.dnkilic.fatihinstanbulu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText isim,email,sifre;
    private Button girisButonu;

    private FirebaseAuth firebaseGiris;
    private DatabaseReference firebaseDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        firebaseDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseGiris=FirebaseAuth.getInstance();
        isim= (EditText) findViewById(R.id.nameField);
        email= (EditText) findViewById(R.id.eMailField);
        sifre = (EditText) findViewById(R.id.passField);
        girisButonu= (Button) findViewById(R.id.kayitButton);
        girisButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girisikontrolet();


            }
        });

    }

    private void girisikontrolet() {
        final String isim=this.isim.getText().toString().trim();
        final String email=this.email.getText().toString().trim();
        final String sifre=this.sifre.getText().toString().trim();

        if (!TextUtils.isEmpty(isim) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(sifre)){
            firebaseGiris.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String kullaniciId=firebaseGiris.getCurrentUser().getUid();//geçerli yani aktif kullanıcının id değerini aldık
                        DatabaseReference kullaniciDataid= firebaseDatabase.child(kullaniciId);
                        kullaniciDataid.child("isim").setValue(isim);
                        kullaniciDataid.child("email").setValue(email);
                        kullaniciDataid.child("sifre").setValue(sifre);



                        Intent yol=new Intent(Register.this, MainActivity.class);
                        yol.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(yol);

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Register.this, "Beklenmedik Hata", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
