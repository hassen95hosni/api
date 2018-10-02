package com.vistaprint.api.controllers;



import org.apache.tomcat.util.http.parser.MediaType;
import org.json.simple.parser.JSONParser;
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

import ch.qos.logback.core.net.SyslogOutputStream;

import org.springframework.web.bind.annotation.CrossOrigin;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.concurrent.atomic.AtomicLong;

import javax.validation.constraints.Null;




@Component("demoBeanA")
@RestController

public class Restcontrolleur {
	//@Autowired
	public String id ="";
	Pingdao pingdao;
	Userdao userdao;
	InstructionDao instdao;
	Userdao us;
	UserClass user;
	PingRes ping = new PingRes();
	private static final String SENDING_URL = "/topic/server-broadcaster";
    private static final String RECEIVING_URL = "/server-receiver";
    private static final String SENDING_URL2 = "/topic/server-broadcasters";
    
    private final SimpMessagingTemplate template;
    private AtomicLong counter = new AtomicLong(0);

    private String message = "";

    
    @Autowired
	public Restcontrolleur(SimpMessagingTemplate template) {
		super();
		this.template = template;
	}
    @SubscribeMapping(SENDING_URL2)
    public String onSubscribesocket() {
        return "waiting for response " + message;
	}
    
	@SubscribeMapping(SENDING_URL)
    public String onSubscribe() {
        return "SUBSCRIBED : " + message;
	}
	//@GetMapping("/pinAllss")
	@Scheduled(fixedRate = 10000)
    public void sendMessage() {
    	ConnectionDb c = new ConnectionDb();
    		  Connection conn = c.getConnection();
    		RethinkDB r = c.getR();
    		List<PingRes> t = new ArrayList<>();
    		Cursor<Object> cursor=r.db("maintennance").table("ping").changes().run(conn);
    	String a ="";
    		for (Object ping :cursor) {
    			a=ping.toString();
    	template.convertAndSend(SENDING_URL, buildNextMessage(a));		
    		}
        
	}
	
	@Scheduled(fixedRate = 10000)
    public void sendMessagetsocket() {
    	ConnectionDb c = new ConnectionDb();
    		  Connection conn = c.getConnection();
    		RethinkDB r = c.getR();
    		List<PingRes> t = new ArrayList<>();
    		if(!this.id.isEmpty()) {
    			Cursor<Object> cursor=r.db("maintennance").table("instruction").get(id).changes().run(conn);
    	    	String a ="";
    	    		for (Object ping :cursor) {
    	    			a=ping.toString();
    	    	template.convertAndSend(SENDING_URL2, buildNextMessage(a));		
    	    		}	
    		}
    		
        
	}
	
	
	private String buildNextMessage(String text) {
        message = text;
        System.out.println("Send message " + message);
	
			return message;
	//	}
		
    }






	@CrossOrigin
@RequestMapping("/addinstruction")
public String addInstruction(@RequestBody String body   ) {
	String k ="";
	try{
		k=java.net.URLDecoder.decode(body, "UTF-8");
		//k =instdao.addInstruction(k);	
	//return k;
}
	catch(Exception e) {
		//return "error"+e.getMessage();
				
	}
	try {
		Instruction inst = new Instruction();
		 ObjectMapper obj = new ObjectMapper();
		 inst = obj.readValue(k, Instruction.class);
				 
				ConnectionDb c = new ConnectionDb();
				  Connection conn = c.getConnection();
				RethinkDB r = c.getR();
				
				Date d = new Date();
				
				String date =String.valueOf(d);
				Cursor<Object> object=r.db("maintennance").table("instruction")
				.insert(r.hashMap("sender", inst.getSender())
				.with("add",inst.getAdd())
				.with("date",date)
				.with("type",inst.getType())
				.with("result",inst.getResult()))
				.run(conn);
				inst.setDate(date);
				for(Object emp :object)
				{	int firstletter= emp.toString().indexOf("id=");
					int lastletter=emp.toString().indexOf(",",firstletter);
					 id = emp.toString().substring(firstletter+3,lastletter);
					//sendMessagetsocket();
				};
				// 
				//
				return inst.toString();
	}
	catch (Exception e )
	{System.out.println("errror"+e.getMessage());
		return "false"+e.getMessage();
	}
	
	
	
}
@CrossOrigin
	@RequestMapping(value = "pingAll", method = RequestMethod.GET)
	public List<String> getAllPing() {
		List <String> ab = new ArrayList<String>();
		ConnectionDb c = new ConnectionDb();
	  Connection conn = c.getConnection();
	RethinkDB r = c.getR();
	
	Cursor<Object> cursor=r.db("maintennance").table("ping").limit(20).run(conn);
String a ="";
	for (Object ping :cursor) {
		ab.add(ping.toString());
		a=a+ping.toString();
	}
	return ab;
		//return pingdao.getAll();
	}
	@GetMapping("/greeting")
	public String hello() {
		return "hello to rest Controller";
	}
	@CrossOrigin
	@GetMapping("/userAll")
	public List<String> getAllUser() throws JsonParseException, JsonMappingException, IOException {
		ConnectionDb c = new ConnectionDb();
		  Connection conn = c.getConnection();
		RethinkDB r = c.getR();
		List<String> list = new ArrayList<String>();
		Cursor<Object> cursor=r.db("maintennance").table("user").run(conn);
	String a ="";
	ObjectMapper obj = new ObjectMapper();
		for (Object user :cursor) {
			// Object vr = JSON.parse(user.toString());
				 UserClass uc ;
			//uc = obj.readValue(vr.toString(), UserClass.class);
			//uc.setAverage(pingaveragebysender(uc.toString()));
			list.add(user.toString());
			a=a+user.toString();

		}	
		
		return list;	
		
		//return us.getAll();
		
	}
	@CrossOrigin
	@PostMapping("/getpingbysender")
	public List<String> getpingBySender(@RequestBody String sender) throws UnsupportedEncodingException, JsonParseException, JsonMappingException, IOException {
		//String k ="";
		 System.out.println(sender);
			//ping = obj.readValue(k, PingRes.class);
		 List <String> ab = new ArrayList<String>();
			System.out.println(ping);
			ConnectionDb c = new ConnectionDb();
			  Connection conn = c.getConnection();
			RethinkDB r = c.getR();
			Cursor<Object> cursor=r.db("maintennance").table("ping").filter(row ->row.g("user").eq(sender)).limit(20).run(conn);
			String a = "";
			for (Object t : cursor ) {
						System.out.println(a);
				a=a+t.toString();
				ab.add(t.toString());
			}
			return ab ;
			
	}
	
	
	
	@CrossOrigin
	@PostMapping("/userbyname")
	public String getUserByName(@RequestBody String username) throws UnsupportedEncodingException {
		String k ="";
		
		k=java.net.URLDecoder.decode(username, "UTF-8");
		
		 ObjectMapper obj = new ObjectMapper();
		 try {
			user = obj.readValue(k, UserClass.class);
			ConnectionDb c = new ConnectionDb();
			 Connection conn = c.getConnection();
		   RethinkDB r = c.getR();
		   Cursor<Object> cursor=r.db("maintennance").table("user").filter(row ->row.g("name").eq(user.getName())).run(conn);
		   String a = "";
		   for (Object t : cursor ) {
			   a=a+t.toString();}
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
		
			//return userdao.getUserByName(username);
	
		
		}
	
	
	//@GetMapping("/greet")
	//public String greet() {
		// return "voila";
	//}
	@CrossOrigin
	@RequestMapping(value = "/pingAlls")
	public String getAllPings() {
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
	public String pingaveragebysender(String sender) throws UnsupportedEncodingException {
		String k ="";
		
		k=java.net.URLDecoder.decode(sender, "UTF-8");
		
		 ObjectMapper obj = new ObjectMapper();
		 try {
			 PingRes p ;
			ping = obj.readValue(k, PingRes.class);
			ConnectionDb c = new ConnectionDb();
			  Connection conn = c.getConnection();
			RethinkDB r = c.getR();
			Cursor<Object> cursor=r.db("maintennance").table("ping").filter(row ->row.g("sender").eq(ping.getName())).limit(10).run(conn);
			String a = "";
			float avg = 0;
			for (Object t : cursor ) {
				p=obj.readValue(t.toString(), PingRes.class);
				
				avg=avg+p.getAverage();
				
				
			}
			a=String.valueOf(avg);
			return a;
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
	
	
	
	@CrossOrigin
	@PostMapping("/updateUser")
	public String updateUser(@RequestBody String username) throws UnsupportedEncodingException {
		String k ="";
		
		k=java.net.URLDecoder.decode(username, "UTF-8");
		
		 ObjectMapper obj = new ObjectMapper();
		 try {
			user = obj.readValue(k, UserClass.class);
			ConnectionDb c = new ConnectionDb();
			 Connection conn = c.getConnection();
		   RethinkDB r = c.getR();
		   Cursor<Object> cursor=r.db("maintennance").table("user").get(user.getId()).update(r.hashMap("initialaddresse",user.getInitialaddresse())).run(conn);
		   String a = "";
		   for (Object t : cursor ) {
			   a=a+t.toString();}
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
		
			//return userdao.getUserByName(username);
	
		
		}
	
	
	
	
	
	
}
