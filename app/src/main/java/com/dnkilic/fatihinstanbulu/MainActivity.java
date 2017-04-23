package com.dnkilic.fatihinstanbulu;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;

        import com.dnkilic.fatihinstanbulu.network.GetPlacesTask;
        import com.dnkilic.fatihinstanbulu.network.PlaceResultListener;

        import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PlaceResultListener, PlaceListItemClickListener {

    ArrayList<Place> places;
    RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
