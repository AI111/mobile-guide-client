package com.example.sasha.myapplication.database;


import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by sasha on 1/26/15.
 */
public class GuideDAO extends BaseDaoImpl<Guide, Integer> {

    protected GuideDAO(ConnectionSource connectionSource,
                       Class<Guide> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Guide> getAllCities() throws SQLException {
        return this.queryForAll();
    }
}

