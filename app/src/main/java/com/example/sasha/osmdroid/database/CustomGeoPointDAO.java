package com.example.sasha.osmdroid.database;

import com.example.sasha.osmdroid.types.CityGuide;
import com.example.sasha.osmdroid.types.CustomGeoPoint;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by sasha on 1/27/15.
 */
public class CustomGeoPointDAO extends BaseDaoImpl<CustomGeoPoint, Integer> {

        protected CustomGeoPointDAO(ConnectionSource connectionSource,
                               Class<CustomGeoPoint> dataClass) throws SQLException {
            super(connectionSource, dataClass);
        }

        public List<CustomGeoPoint> getAllPoints() throws SQLException{
            return this.queryForAll();
        }
    }

