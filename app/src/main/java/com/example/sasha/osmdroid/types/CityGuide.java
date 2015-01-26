package com.example.sasha.osmdroid.types;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by sasha on 12/21/14.
 */
@DatabaseTable(tableName = "cities")
public class CityGuide {

    @DatabaseField(id = true,canBeNull = false)
    private int Id;

    @DatabaseField()
    private String  name;

    @DatabaseField()
    private String description;

    @DatabaseField()
    private String imgUrl;

    @DatabaseField()
    private byte rating;

    @DatabaseField()
    private String cashMapUri;

    @DatabaseField(dataType = DataType.DATE)
    private Date changed;

    @DatabaseField()
    private CustomGeoPoint[] points;

    public CityGuide(String name, String description, String imgUrl, byte rating, String cashMapUri, Date changed, CustomGeoPoint[] points) {
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.rating = rating;
        this.cashMapUri = cashMapUri;
        this.changed = changed;
        this.points = points;
    }
    public CityGuide(){
        super();
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

    public String getCasMaphUri() {
        return cashMapUri;
    }

    public void setCasMaphUri(String cashMapUri) {
        this.cashMapUri = cashMapUri;
    }

    public Date getChanged() {
        return changed;
    }

    public void setChanged(Date changed) {
        this.changed = changed;
    }

    public CustomGeoPoint[] getPoints() {
        return points;
    }

    public void setPoints(CustomGeoPoint[] points) {
        this.points = points;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCashMapUri() {
        return cashMapUri;
    }

    public void setCashMapUri(String cashMapUri) {
        this.cashMapUri = cashMapUri;
    }

    @Override
    public String toString() {
        return "CityGuide{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", rating=" + rating +
                ", cashMapUri='" + cashMapUri + '\'' +
                ", changed=" + changed +
                ", points=" + Arrays.toString(points) +
                "}\n";
    }
}
