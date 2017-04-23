package com.dnkilic.fatihinstanbulu.network;

import android.os.AsyncTask;

import com.dnkilic.fatihinstanbulu.Place;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Hafta_Sonu on 9.04.2017.
 */

public class GetPlaceDetailsTask extends AsyncTask<Void, Void,Void> {

    String key = "AIzaSyCUeNT4WZgP0vYTNtan32ovLz2MHQQfQVY";
    String placeId;

    public GetPlaceDetailsTask(String placeId) {
        this.placeId = placeId;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            getPlaceDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void getPlaceDetail() throws Exception
    {
        ArrayList<Place> places = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/details/" +
                        "json?placeid=" + placeId + "&key=" + key)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String result = response.body().string();
        JSONObject base = new JSONObject(result);

        JSONObject results = base.getJSONObject("result");
        String formattedAddress = results.getString("formatted_address");
        String internationalPhoneNumber = results.getString("international_phone_number");
        String rating = results.getString("rating");

        JSONArray photos = results.getJSONArray("photos");
        String reference = photos.getJSONObject(0).getString("photo_reference");

        String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?photoreference="
                + reference + "&sensor=false" + "&maxheight=400&maxwidth=400" + "&key=" + key;
    }
}
