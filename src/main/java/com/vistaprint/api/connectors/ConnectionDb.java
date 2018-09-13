package com.vistaprint.api.connectors;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.exc.ReqlError;
import com.rethinkdb.gen.exc.ReqlQueryLogicError;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Connection;

public class ConnectionDb {

public static final RethinkDB r = RethinkDB.r;
Connection connection;

public Connection getConnection() {
	return connection;
}

public void setConnection(Connection connection) {
	this.connection = connection;
}

public static RethinkDB getR() {
	return r;
}

public ConnectionDb() {
	connection = r.connection().connect();
	//System.out.println(connection = r.connection().connect());
	//r.db("maintennance").tableCreate("ping").run(connection);

   	//r.db("maintennance").table("ping").insert(r.hashMap("sender","mac").with("loss", "per centage %").with("average", "per milleseconde")).run(connection);
	
	
	// TODO Auto-generated constructor stub
}

}
