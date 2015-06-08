package com.example.sasha.osmdroid.types;

import com.example.sasha.osmdroid.database.HelperFactory;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sasha on 12/21/14.
 */
@DatabaseTable(tableName = "cities")
public class Guide implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;
    public boolean installed;
    @ForeignCollectionField(eager = true)
    public Collection<GeoPoint> points = new ArrayList<GeoPoint>();
    @DatabaseField
    Locale locale;
    @DatabaseField(id = true, canBeNull = false)
    private int id;
    @DatabaseField()
    private String name;
    @DatabaseField()
    private double latitude;
    @DatabaseField()
    private double longitude;
    @DatabaseField()
    private String description;
    @DatabaseField()
    private String imgUrl;
    @DatabaseField()
    private String fullImgUrl;
    @DatabaseField()
    private float rating;
    @DatabaseField()
    private String mapCash;
    @DatabaseField()
    private String dataCash;
    @DatabaseField(dataType = DataType.DATE)
    private Date changed;

    public Guide(String name, double longitude, double latitude, String description, String imgUrl, String fullImgUrl, float rating, String mapCash, String dataCash, Date changed) {
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.fullImgUrl = fullImgUrl;
        this.rating = rating;
        this.mapCash = mapCash;
        this.dataCash = dataCash;
        this.changed = changed;
        this.latitude = latitude;
        this.longitude = longitude;

        //points = new ArrayList<GeoPoint>();
    }

    public Guide() {
        super();
        // points = new ArrayList<GeoPoint>();
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getFullImgUrl() {
        return fullImgUrl;
    }

    public void setFullImgUrl(String fullImgUrl) {
        this.fullImgUrl = fullImgUrl;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataCash() {
        return dataCash;
    }

    public void setDataCash(String dataCash) {
        this.dataCash = dataCash;
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

    public float getRating() {
        return rating;
    }

    public void setRating(byte rating) {
        this.rating = rating;
    }

    public Date getChanged() {
        return changed;
    }

    public void setChanged(Date changed) {
        this.changed = changed;
    }

    public Collection<GeoPoint> getPoints() {
        return points;
    }

    public String getMapCash() {
        return mapCash;
    }

    public void setMapCash(String mapCash) {
        this.mapCash = mapCash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }


    public void addPoint(GeoPoint value) throws SQLException {
        value.setGuide(this);
        HelperFactory.getHelper().getGeoPointDAO().create(value);
        points.add(value);
    }

    public void removePoint(GeoPoint value) throws SQLException {
        points.remove(value);
        HelperFactory.getHelper().getGeoPointDAO().delete(value);
    }

    public void removeAllPoint() throws SQLException {
        HelperFactory.getHelper().getGeoPointDAO().delete(points);
        points.clear();
    }

    @Override
    public String toString() {
        return "Guide{" +
                "id= " + id + " " +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", ullImgUrl='" + fullImgUrl + '\'' +
                ", rating=" + rating +
                ", mapCash='" + mapCash + '\'' +
                ", changed=" + changed +
                ", points=\n" + (points == null ? null : Arrays.toString(points.toArray())) +
                "}\n";
    }
}
