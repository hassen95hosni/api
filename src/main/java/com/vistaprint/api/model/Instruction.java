package com.vistaprint.api.model;

public class Instruction {

	String id ;
	String sender ;
	String add ;
	String date;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getAdd() {
		return add;
	}
	public void setAdd(String add) {
		this.add = add;
	}
	public Instruction() {
		super();
	}
	@Override
	public String toString() {
		return "Instruction [id=" + id + ", sender=" + sender + ", add=" + add + ", date=" + date + "]";
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
