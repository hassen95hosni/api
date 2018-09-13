package com.vistaprint.api.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tomcat.jni.Time;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import com.vistaprint.api.connectors.ConnectionDb;
import com.vistaprint.api.model.Instruction;
import com.vistaprint.api.model.PingRes;

public class InstructionDao {

	
	public String addInstruction(String body) {
		try {
			Instruction inst = new Instruction();
			 ObjectMapper obj = new ObjectMapper();
			 inst = obj.readValue(body, Instruction.class);
					 
					ConnectionDb c = new ConnectionDb();
					  Connection conn = c.getConnection();
					RethinkDB r = c.getR();
					
					Date d = new Date();
					
					String date =String.valueOf(d);
					r.db("maintennance").table("instruction").insert(r.hashMap("sender", inst.getSender()).with("add",inst.getAdd()).with("date",date)).run(conn);
					inst.setDate(date);
					return inst.toString();
		}
		catch (Exception e )
		{System.out.println("errror"+e.getMessage());
			return "false"+e.getMessage();
		}
	}
}
