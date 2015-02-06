package com.example.sasha.osmdroid.types;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Arrays;
@DatabaseTable(tableName = "points")
public class CustomGeoPoint {
public CustomGeoPoint(String title, PointType type, String description,
                  String text, String[] galery, String audio) {
		super();
		this.title = title;
		this.type = type;
		this.description = description;
		this.text = text;
		this.galery = galery;
		this.audio = audio;
	}
    public CustomGeoPoint(){
        super();
    }

    enum PointType{SHOWPLACE,HOTEL,SHOP,RESTAURAN};

    @DatabaseField()
    public String title;

    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    public PointType type;

    @DatabaseField()
    public String description;

    @DatabaseField()
    public String text;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    public String[] galery;

    @DatabaseField()
    public String audio;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private CityGuide cityGuide;

    public CityGuide getCityGuide() {
        return cityGuide;
    }

    public void setCityGuide(CityGuide cityGuide) {
        this.cityGuide = cityGuide;
    }

    @Override
    public String toString() {
        return "CustomGeoPoint{" +
                "title='" + title + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", text='" + text + '\'' +
                ", galery=" + Arrays.toString(galery) +
                ", audio='" + audio + '\'' +
                "}\n";
    }
}
