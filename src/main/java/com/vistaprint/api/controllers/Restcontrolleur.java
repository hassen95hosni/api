package com.vistaprint.api.controllers;



import org.apache.tomcat.util.http.parser.MediaType;

import org.mortbay.util.ajax.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Json;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import com.vistaprint.api.connectors.ConnectionDb;
import com.vistaprint.api.dao.*;
import com.vistaprint.api.model.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component("demoBeanA")
@RestController

public class Restcontrolleur {
	//@Autowired
	Pingdao pingdao;
	Userdao userdao;
	InstructionDao instdao;
	Userdao us;
	PingRes ping = new PingRes();

@RequestMapping("/addinstruction")
public String addInstruction(@RequestBody String body   ) {
	String k ="";
	try{
		k=java.net.URLDecoder.decode(body, "UTF-8");
		k =instdao.addInstruction(k);
	
		
		
	return k;}
	catch(Exception e) {
		return "error"+e.getMessage();
				
	}
}

	@RequestMapping(value = "pingAll", method = RequestMethod.GET)
	public String getAllPing() {
		
		return pingdao.getAll();
	}
	@GetMapping("/greeting")
	public String hello() {
		return "hello to rest Controller";
	}
	@GetMapping("/userAll")
	public String getAllUser() {
		ConnectionDb c = new ConnectionDb();
		  Connection conn = c.getConnection();
		RethinkDB r = c.getR();
		
		Cursor<Object> cursor=r.db("maintennance").table("user").run(conn);
	String a ="";
		for (Object user :cursor) {
			a=a+user.toString();
		}
		
		return a;	
		
		//return us.getAll();
		
	}
	
	@PostMapping("/getpingbysender")
	public String getpingBySender(@RequestBody String sender) throws UnsupportedEncodingException {
		String k ="";
		
			k=java.net.URLDecoder.decode(sender, "UTF-8");
			
			 ObjectMapper obj = new ObjectMapper();
			 try {
				ping = obj.readValue(k, PingRes.class);
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
	
	
	
	
	@GetMapping("/greet")
	public String greet() {
		try {
			
			
		
			return pingdao.getAll();
	
		}
		catch (Exception e ) {
			return e.getMessage();
			}
		}
	
	
	//@GetMapping("/greet")
	//public String greet() {
		// return "voila";
	//}
	@RequestMapping(value = "/pingAlls")
	public String getAllPings() {
		List<PingRes> list = new ArrayList<>();
		list.add(new PingRes());
		return pingdao.getAll();
		//return JSON.parse(list.toString()).toString();
	//return "voila";
	}
	
	
}
