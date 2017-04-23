package com.dnkilic.fatihinstanbulu;

public class Place {
    String name;
    String placeId;

    public Place(String name, String placeId) {
        this.name = name;
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public String getPlaceId() {
        return placeId;
    }
}
