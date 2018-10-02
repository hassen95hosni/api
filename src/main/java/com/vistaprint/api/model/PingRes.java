package com.vistaprint.api.model;

import java.util.Date;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;

public class PingRes {
	
	Long id ;
	UserClass user;
	float average;
	int loss;
	String type;
	String name;
	String sender ;
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public UserClass getUser() {
		return user;
	}
	public void setUser(UserClass user) {
		this.user = user;
	}
	public float getAverage() {
		return average;
	}
	public void setAverage(float average) {
		this.average = average;
	}
	public int getLoss() {
		return loss;
	}
	public void setLoss(int loss) {
		this.loss = loss;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public PingRes(Long id, UserClass user, float average, int loss, String type,String name) {
		super();
		this.id = id;
		this.user = user;
		this.average = average;
		this.loss = loss;
		this.type = type;
	}
	public void addping(UserClass user , float average , int loss, String type, Connection connection , RethinkDB database ){
		Date d = new Date(System.currentTimeMillis());
	Object j =database.db("maintennance").table("ping").insert(database.hashMap("user", user).with("average",average).with("loss",loss).with("date",d.toString())).run(connection);
System.out.println(j.toString());


}
	
@Override
	public String toString() {
		return "PingRes [id=" + id + ", user=" + user + ", average=" + average + ", loss=" + loss + ", type=" + type
				+ "]";
	}
public PingRes() {
		super();
	}
public PingRes toping(String pingres,UserClass user){
System.out.println(pingres);
PingRes ping = new PingRes(Long.valueOf(1), user, 2, 3,"","");
int a = pingres.indexOf("(");
int b = pingres.indexOf(")");

if(a!=-1 && b!=-1) {
	try {
loss = Integer.parseInt(pingres.substring(a+1, b).replaceAll("\\D", ""));
a = pingres.lastIndexOf("=");
//String t =pingres.substring(a+1, pingres.length()).replaceAll("\\D", "");
//System.out.println(t);
average = Float.valueOf(pingres.substring(a+1, pingres.length()).replaceAll("\\D", ""));
ping.setAverage(average);
//System.out.println(average);
ping.setUser(user);
ping.setLoss(loss);
//System.out.println(loss);
}
	catch (Exception e) {
		System.out.println(e.getMessage());
	System.out.println("still not a complete ping");
	}
	}
else 
	System.out.println("not a ping message ");
return ping;
}}