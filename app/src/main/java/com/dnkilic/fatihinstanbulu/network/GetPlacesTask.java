package com.dnkilic.fatihinstanbulu.network;

import android.os.AsyncTask;

import com.dnkilic.fatihinstanbulu.Place;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Hafta_Sonu on 9.04.2017.
 */

public class GetPlacesTask extends AsyncTask<Void, Void, ArrayList<Place> > {
    // 1 web requesti yap
    // 2 sonuçları recycle view'a yolla

    String key = "AIzaSyCUeNT4WZgP0vYTNtan32ovLz2MHQQfQVY";
    PlaceResultListener listener;

    public GetPlacesTask(PlaceResultListener listener) {
        this.listener = listener;
    }

    public ArrayList<Place> getPlaces() throws Exception
    {
        ArrayList<Place> places = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                        "json?location=-33.8670,151.1957&radius=500&key=" + key)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String result = response.body().string();
        JSONObject base = new JSONObject(result);

        JSONArray results = base.getJSONArray("results");

        for(int i=0; i<results.length(); i++)
        {
            JSONObject currentPlace = results.getJSONObject(i);
            String name = currentPlace.getString("name");
            String placeId = currentPlace.getString("place_id");
            places.add(new Place(name, placeId));
        }

        return places;
    }

    @Override
    protected ArrayList<Place>  doInBackground(Void... params) {

        ArrayList<Place> places = null;

        try {
            places = getPlaces();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return places;
    }

    @Override
    protected void onPostExecute(ArrayList<Place> places) {
        super.onPostExecute(places);
        if(places == null)
        {
            listener.onPlaceResultError();
        }
        else
        {
            listener.onPlaceResultSuccess(places);
        }
    }
}
