package com.example.sasha.myapplication.database;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Arrays;

@DatabaseTable(tableName = "points")
public class GeoPoint {
    @DatabaseField(id = true, canBeNull = false)
    public int point_id;
    @DatabaseField()
    public String title;
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    public PointType type;
    @DatabaseField()
    public String description;
    ;
    @DatabaseField()
    public String text;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    public String[] galery;
    @DatabaseField()
    public String audio;
    @DatabaseField()
    public double latitude;
    @DatabaseField()
    public double longitude;
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
