package com.vistaprint.api.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import com.vistaprint.api.connectors.*;
import com.vistaprint.*;
import com.vistaprint.api.model.*;

public class Pingdao {
//@Autowired
	//PingRepositry pingrep;
	PingRes ping = new PingRes();

	public String getByuser(String username){
		
		
		 ObjectMapper obj = new ObjectMapper();
		 try {
			ping = obj.readValue(username, PingRes.class);
			ConnectionDb c = new ConnectionDb();
			  Connection conn = c.getConnection();
			RethinkDB r = c.getR();
			Cursor<Object> cursor=r.db("maintennance").table("ping").filter(row ->row.g("sender").eq(ping.getSender())).run(conn);
			String a = "";
			for (Object t : cursor ) {
				a=a+t.toString();
			}
			return a ;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		}
		

	}

	
	public String getByuseronchange(String username) {
		ConnectionDb c = new ConnectionDb();
		  Connection conn = c.getConnection();
		RethinkDB r = c.getR();
		Cursor<Object> cursor=r.db("maintennance").table("ping").filter(row ->row.g("sender").eq(username)).changes().run(conn);
		String a = "";
		for (Object t : cursor ) {
			a=a+t.toString();
		}
return a ;
	}
	
	
	
public String getAll() {
	ConnectionDb c = new ConnectionDb();
	  Connection conn = c.getConnection();
	RethinkDB r = c.getR();
	List<PingRes> t = new ArrayList<>();
	Cursor<Object> cursor=r.db("maintennance").table("ping").run(conn);
String a ="";
	for (Object ping :cursor) {
		a=a+ping.toString();
	}
	return a;
	
	
}
public String getAllonChange() {
	ConnectionDb c = new ConnectionDb();
	  Connection conn = c.getConnection();
	RethinkDB r = c.getR();
	List<PingRes> t = new ArrayList<>();
	Cursor<Object> cursor=r.db("maintennance").table("ping").changes().run(conn);
String a ="";
	for (Object ping :cursor) {
		a=a+ping.toString();
	}
	try {
		
	
	System.out.println("size of table " +t.size());
	} catch (Exception e ) {
		System.out.println("error is kadha"+e.getMessage());
		//pingrep.findAll();
	}
	return a;	
	
}
}
