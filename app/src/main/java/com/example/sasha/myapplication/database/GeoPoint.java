package com.example.sasha.myapplication.database;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Arrays;

@DatabaseTable(tableName = "points")
public class GeoPoint {
    @DatabaseField(id = true, canBeNull = false)
    private int point_id;
    @DatabaseField()
    private String title;
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private PointType type;
    @DatabaseField()
    private String description;

    @DatabaseField()
    private String text;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private String[] galery;
    @DatabaseField()
    private String audio;
    @DatabaseField()
    private double latitude;
    @DatabaseField()
    private double longitude;
    @DatabaseField(foreign = true, foreignAutoRefresh = true,canBeNull = true)
    private Guide cityGuide;

    public GeoPoint(String title, PointType type, String description, String text, String[] galery, String audio, double latitude, double longitude, Guide cityGuide) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.text = text;
        this.galery = galery;
        this.audio = audio;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cityGuide = cityGuide;
    }

    public GeoPoint() {
        super();
    }

    public Guide getGuide() {
        return cityGuide;
    }

    public void setGuide(Guide cityGuide) {
        this.cityGuide = cityGuide;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PointType getType() {
        return type;
    }

    public void setType(PointType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getGalery() {
        return galery;
    }

    public void setGalery(String[] galery) {
        this.galery = galery;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getPoint_id() {
        return point_id;
    }

    @Override
    public String toString() {
        return "GeoPoint{" +
                "title='" + title + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", text='" + text + '\'' +
                ", galery=" + Arrays.toString(galery) +
                ", audio='" + audio + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                "\n";
    }

    public enum PointType {SHOWPLACE, HOTEL, SHOP, RESTAURAN, MUSEUM, CAFE}
}
