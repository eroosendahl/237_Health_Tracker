package main;

public class User{

	private int row;
	private int currentColumn;
	private String name;
	
	public User(String n, int r) {
		name = n;
		row = r;
	}

	public int getRow(){

		return this.row;

	}

	public void setRow(int row){
		
		this.row = row;

	}

	public int getCurrentColumn(){
		
		return this.currentColumn;
	}

	public void setCurrentColumn(int column) {
	
		this.currentColumn = column;
	}

	public String getName(){

		return this.name;
	}

	public void setName(String name){

		this.name = name;
	}
	
}
