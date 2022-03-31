package main;

public class User{

	private int row;
	private int index;
	private String name;
	
	public User(String n, int r) {
		name = n;
		row = r;
	}
	
	public boolean compareName(String otherUsername) {
		if (name.equals(otherUsername))
			return true;
		return false;
	}

	public int getRow(){

		return this.row;

	}

	public void setRow(int row){
		
		this.row = row;

	}

	public int getCurrentColumn(){
		
		return this.index;
	}

	public void setCurrentColumn(int column) {
	
		this.index = column;
	}

	public String getName(){

		return this.name;
	}

	public void setName(String name){

		this.name = name;
	}
	
}
