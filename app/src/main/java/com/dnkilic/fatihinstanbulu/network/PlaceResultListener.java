package com.dnkilic.fatihinstanbulu.network;

import com.dnkilic.fatihinstanbulu.Place;

import java.util.ArrayList;

public interface PlaceResultListener {
    void onPlaceResultSuccess(ArrayList<Place> places);
    void onPlaceResultError();
}
