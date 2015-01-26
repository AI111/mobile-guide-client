package com.example.sasha.osmdroid.types;

import java.util.Arrays;

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
public String title;
public PointType type;
public String description;
public String text;
public String[] galery;
public String audio;

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
