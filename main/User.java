package main;

public class User{

	private int userRow;
	private int userColumn;
	private String userName;

	public int getUserRow(){

		return this.userRow;

	}

	public void setUserRow(int row){
		
		this.userRow = row;

	}

	public int getUserColumn(){
		
		return this.userColumn;
	}

	public void setUserColumn(int column) {
	
		this.userColumn = column;
	}

	public String getUserName(){

		return this.userName;
	}

	public void setUserName(String name){

		this.userName = name;
	}
	
}
