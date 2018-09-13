package com.vistaprint.api.rethinkdb;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;


import java.util.concurrent.TimeoutException;

public class RethinkDBConnectionFactory {
    private String host;

    public RethinkDBConnectionFactory(String host) {
        this.host = host;
    }

    public Connection createConnection() throws TimeoutException {
        return RethinkDB.r.connection().hostname(host).connect();
    }
}
