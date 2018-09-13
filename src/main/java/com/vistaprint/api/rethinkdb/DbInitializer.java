package com.vistaprint.api.rethinkdb;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.concurrent.TimeoutException;

public class DbInitializer implements InitializingBean {
    @Autowired
    private RethinkDBConnectionFactory connectionFactory;

   
    private static final RethinkDB r = RethinkDB.r;

   

    private void createDb() throws TimeoutException {
        Connection connection = connectionFactory.createConnection();
        List<String> dbList = r.dbList().run(connection);
        if (!dbList.contains("chat")) {
            r.dbCreate("chat").run(connection);
        }
        List<String> tables = r.db("chat").tableList().run(connection);
        if (!tables.contains("messages")) {
            r.db("chat").tableCreate("messages").run(connection);
            r.db("chat").table("messages").indexCreate("time").run(connection);
        }
    }



	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
