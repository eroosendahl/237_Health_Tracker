package main;

public class User{

	private int row;
	private int currentColumn;
	private String name;

	public int getUserRow(){

		return this.row;

	}

	public void setUserRow(int row){
		
		this.row = row;

	}

	public int getUserColumn(){
		
		return this.currentColumn;
	}

	public void setUserColumn(int column) {
	
		this.currentColumn = column;
	}

	public String getUserName(){

		return this.name;
	}

	public void setUserName(String name){

		this.name = name;
	}
	
}
