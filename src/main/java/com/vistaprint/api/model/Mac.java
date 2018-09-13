package com.vistaprint.api.model;

import java.util.List;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;

public class Mac {
Long id ;
String mac ;
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getMac() {
	return mac;
}
public void setMac(String mac) {
	this.mac = mac;
}
public Mac() {
	super();
}
public void addMac(Connection conn,RethinkDB db , String mac ) {
	db.db("maintennance").table("mac").insert(db.hashMap("mac",mac)).run(conn);
	db.db("maintennance").table("user").eqJoin("pc_id", db.db("maintennance").table("mac")).run(conn);
	
}
	
	public Object getmac ( Connection cn, RethinkDB db , String mac){
		Object j = new Object();
		j=db.db("maintennance").table("mac").getAll(mac).coerceTo("array").run(cn);
		
	//System.out.println(j);
		return j;
	}
}
