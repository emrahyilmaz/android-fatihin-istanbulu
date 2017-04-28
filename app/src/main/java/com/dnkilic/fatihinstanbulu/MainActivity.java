package com.dnkilic.fatihinstanbulu;

        import android.content.Intent;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.widget.Toast;

        import com.dnkilic.fatihinstanbulu.loginProcess.LoginActivity;
        import com.dnkilic.fatihinstanbulu.network.GetPlacesTask;
        import com.dnkilic.fatihinstanbulu.network.PlaceResultListener;
        import com.google.firebase.auth.FirebaseAuth;

        import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PlaceResultListener, PlaceListItemClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    ArrayList<Place> places;
    RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * For login Process
         */
        mAuth=FirebaseAuth.getInstance();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Toast.makeText(MainActivity.this, "Kullanici oturumu yok giriş yapın", Toast.LENGTH_SHORT).show();
                    Intent gecis=new Intent(getApplicationContext(),LoginActivity.class);//geçiş işlemi
                    gecis.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(gecis);
                    finish();
                }
            }
        };

        /**
         * For login Process
         */


        places = new ArrayList<>();
        places.add(new Place("Doğan", "Yok"));

        RecyclerView rvPlaces = (RecyclerView) findViewById(R.id.rvPlaces);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvPlaces.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(places, this);
        rvPlaces.setAdapter(mAdapter);

        new GetPlacesTask(this).execute();


    }

    @Override
    public void onPlaceResultSuccess(ArrayList<Place> places) {

        for(Place item : places)
        {
            this.places.add(item);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPlaceResultError() {

    }

    @Override
    public void onPlaceSelected(Place place) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("NAME", place.getName());
        intent.putExtra("ID", place.getPlaceId());
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
