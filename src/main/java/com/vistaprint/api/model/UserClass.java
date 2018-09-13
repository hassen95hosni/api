package com.vistaprint.api.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.mortbay.util.ajax.JSON;

import com.google.gson.Gson;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Json;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;

public class UserClass {
String id ;
String name;
String macs;
String ipAddresse;

public String getIpAddresse() {
	return ipAddresse;
}
public void setIpAddresse(String ipAddresse) {
	this.ipAddresse = ipAddresse;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getMacs() {
	return macs;
}
public void setMacs(String macs) {
	this.macs = macs;
}



public UserClass(String id, String name, String macs, String ipAddresse) {
	super();
	this.id = id;
	this.name = name;
	this.macs = macs;
	this.ipAddresse = ipAddresse;
}
@Override
public String toString() {
	return  ""+macs+"" ;
}
public Object adduser(Connection connection , RethinkDB database , UserClass user) {
	//List<String> list = user.getMacs();
	//String t = new Gson().toJson(list);
	Object j =database.db("maintennance").table("user").insert(database.hashMap("name", user.getName()).with("macs",user.getMacs()).with("ipAddresse",user.getIpAddresse())).run(connection);
   	System.out.println("voila"+database.db("maintennance").table("user").eqJoin("pc_id", database.db("maintennance").table("user")).run(connection).toString());
	UserClass users = new UserClass();
	ObjectMapper mapper = new ObjectMapper();
	
	try {
		System.out.println(j.toString());
		users = mapper.readValue(j.toString(), UserClass.class);
	} catch (JsonParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JsonMappingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   	
   	return j;
}

public UserClass addusert(Connection connection , RethinkDB database , UserClass user) {
	
	//String t = new Gson().toJson(list);
	Object j =database.db("maintennance").table("user").insert(database.hashMap("name", user.getName()).with("macs",user.getMacs()).with("ipAddresse",user.getIpAddresse())).run(connection);
   	//System.out.println("voila"+database.db("maintennance").table("user").eqJoin("pc_id", database.db("maintennance").table("user")).run(connection).toString());
	UserClass users = new UserClass();
	ObjectMapper mapper = new ObjectMapper();
	
	try {
		 int i = j.toString().indexOf("[");
		 int kp = j.toString().indexOf("]");
		 String hh=j.toString().substring(i+1, kp);
		 
			System.out.println("j"+j+"i"+i+"kp"+kp);
		 users = mapper.readValue(users.finduserByID(connection, database,hh).toString(), UserClass.class);
	} catch (JsonParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JsonMappingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   	
   	return users;
}


public UserClass() {
	super();
}
public List<Object> findByMac(Connection connection , RethinkDB database ,String mac){
	
	List<Object> fi = new ArrayList<Object>();
	fi=database.db("maintennance").table("user").filter(row ->row.g("macs").equals(mac)).coerceTo("array").run(connection);
	System.out.println(fi.toString());
	return fi ;
}
public List<UserClass> findByName(Connection connection , RethinkDB database ,String name){
	
	List<UserClass> fi = new ArrayList<UserClass>();
	//fi=database.db("maintennance").table("user").filter(row ->row.g("name").equals(name)).coerceTo("array").run(connection);
	Cursor<UserClass>cursor = database.db("maintennance").table("user").filter(row ->row.g("name").eq(name)).run(connection);
	fi =cursor.toList();
	//System.out.println(fi.toString());
	return fi ;
}

public UserClass finduserByID(Connection connection , RethinkDB database , String id) {
	List<UserClass> fi = new ArrayList<UserClass>();
	//fi=database.db("maintennance").table("user").filter(row ->row.g("id").eq(id)).coerceTo("array").run(connection);
	Cursor<UserClass>cursor = database.db("maintennance").table("user").filter(row ->row.g("id").eq(id.toString())).run(connection);
	fi =cursor.toList();
	//System.out.println(fi.toString());
	for(int nn =0 ; nn<fi.size();nn++) {
		System.out.println(fi.get(nn));
	}
	for (UserClass user :cursor) {
		System.out.println("usr:"+user);
	}
	UserClass users = new UserClass();
	ObjectMapper mapper = new ObjectMapper();
	//System.out.println(fi.size());
	JSONObject json = new JSONObject();
	json.putAll((Map) fi.get(0));
	System.out.println(json);
	try {
		//JSONArray tlala = (JSONArray)fi.get(0).toString().substring(1,fi.get(0).toString().length());
		
		UserClass j = mapper.readValue(json.toString(), UserClass.class);
	} catch (JsonParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JsonMappingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   	
	
   	return users;
}


}
