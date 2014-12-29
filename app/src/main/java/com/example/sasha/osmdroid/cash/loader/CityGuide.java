package com.example.sasha.osmdroid.cash.loader;

/**
 * Created by sasha on 12/21/14.
 */
public class CityGuide {
    private String  name;
    private String description;
    private String imgUrl;
    private byte rating;
    private String cashUri;
    private String mapCashUrl;
    public CityGuide(){

    }

    public CityGuide(String name, String description, String imgUrl, byte rating, String cashUri, String mapCashUrl) {
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.rating = rating;
        this.cashUri = cashUri;
        this.mapCashUrl = mapCashUrl;
    }

    @Override
    public String toString() {
        return "CityGuide{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", rating=" + rating +
                ", cashUri='" + cashUri + '\'' +
                ", mapCashUrl='" + mapCashUrl + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public byte getRating() {
        return rating;
    }

    public void setRating(byte rating) {
        this.rating = rating;
    }

    public String getCashUri() {
        return cashUri;
    }

    public void setCashUri(String cashUri) {
        this.cashUri = cashUri;
    }

    public String getMapCashUrl() {
        return mapCashUrl;
    }

    public void setMapCashUrl(String mapCashUrl) {
        this.mapCashUrl = mapCashUrl;
    }
}
