package com.example.sasha.osmdroid.types;

import com.example.sasha.osmdroid.database.HelperFactory;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
    private double latitude;

    @DatabaseField()
    private  double longitude;

    @DatabaseField()
    private String description;

    @DatabaseField()
    private String imgUrl;

    @DatabaseField()
    private String fullImgUrl;

    @DatabaseField()
    private byte rating;

    @DatabaseField()
    private String mapCash;

    @DatabaseField()
    private String dataCash;

    @DatabaseField(dataType = DataType.DATE)
    private Date changed;

    @ForeignCollectionField(eager = true)
    public Collection<CustomGeoPoint> points;// = new ArrayList<CustomGeoPoint>();

    public boolean installed;

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

    public CityGuide(String name, double longitude, double latitude, String description, String imgUrl, String fullImgUrl, byte rating, String mapCash, String dataCash, Date changed) {
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.fullImgUrl = fullImgUrl;
        this.rating = rating;
        this.mapCash = mapCash;
        this.dataCash=dataCash;
        this.changed = changed;
        this.latitude=latitude;
        this.longitude=longitude;

        points = new ArrayList<CustomGeoPoint>();
    }
    public CityGuide(){
        super();
        points = new ArrayList<CustomGeoPoint>();
    }
    public String getName() {
        return name;
    }

    public String getDataCash() {
        return dataCash;
    }

    public void setDataCash(String dataCash) {
        this.dataCash = dataCash;
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

    public byte getRating() {
        return rating;
    }

    public void setRating(byte rating) {
        this.rating = rating;
    }

    public String getCasMaphUri() {
        return mapCash;
    }

    public Date getChanged() {
        return changed;
    }

    public void setChanged(Date changed) {
        this.changed = changed;
    }

    public Collection<CustomGeoPoint> getPoints() {
        return points;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMapCash() {
        return mapCash;
    }

    public void setMapCash(String mapCash) {
        this.mapCash = mapCash;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }


    public void addPoint(CustomGeoPoint value) throws SQLException {
        value.setCityGuide(this);
        HelperFactory.getHelper().getCustomGeoPointDAO().create(value);
        points.add(value);
    }

    public void removePoint(CustomGeoPoint value) throws SQLException {
        points.remove(value);
        HelperFactory.getHelper().getCustomGeoPointDAO().delete(value);
    }

    @Override
    public String toString() {
        return "CityGuide{" +
                "id= "+Id+" "+
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", ullImgUrl='"+fullImgUrl+'\''+
                ", rating=" + rating +
                ", mapCash='" + mapCash + '\'' +
                ", changed=" + changed +
                ", points=\n" + Arrays.toString(points.toArray()) +
                "}\n";
    }
}
