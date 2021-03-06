package com.vistaprint.api.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import com.vistaprint.api.connectors.ConnectionDb;
import com.vistaprint.api.model.PingRes;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Userdao {
	public String getAll() {
		ConnectionDb c = new ConnectionDb();
		  Connection conn = c.getConnection();
		RethinkDB r = c.getR();
		
		Cursor<Object> cursor=r.db("maintennance").table("user").run(conn);
	String a ="";
		for (Object user :cursor) {
			a=a+user.toString();
		}
		
		return a;	
		
	}
	
	
	public String getAllonchanges() {
		ConnectionDb c = new ConnectionDb();
		  Connection conn = c.getConnection();
		RethinkDB r = c.getR();
		List<PingRes> t = new ArrayList<>();
		Cursor<Object> cursor=r.db("maintennance").table("user").changes().run(conn);
	String a ="";
		for (Object user :cursor) {
			a=a+user.toString();
		}
		try {
			
		
		System.out.println("size of table " +t.size());
		} catch (Exception e ) {
			System.out.println("error is kadha"+e.getMessage());
			//pingrep.findAll();
		}
		return a;	
		
	}
	public String getUserByName(String username) throws JsonParseException, JsonMappingException, IOException{
		
		
		ObjectMapper obj = new ObjectMapper();
		ConnectionDb c = new ConnectionDb();
			 Connection conn = c.getConnection();
		   RethinkDB r = c.getR();
		   Cursor<Object> cursor=r.db("maintennance").table("ping").filter(row ->row.g("name").eq(username)).run(conn);
		   String a = "";
		   for (Object t : cursor ) {
			   a=a+t.toString();
		   }
		   return a ;
	   

   }

}
