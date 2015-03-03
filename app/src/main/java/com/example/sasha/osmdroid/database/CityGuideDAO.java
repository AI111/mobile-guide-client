package com.example.sasha.osmdroid.database;

import com.example.sasha.osmdroid.types.CityGuide;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by sasha on 1/26/15.
 */
public class CityGuideDAO extends BaseDaoImpl<CityGuide, Integer> {

    protected CityGuideDAO(ConnectionSource connectionSource,
                           Class<CityGuide> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<CityGuide> getAllCities() throws SQLException {
        return this.queryForAll();
    }
}

