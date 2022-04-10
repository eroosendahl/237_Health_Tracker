package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.User;

class UserTest {
	User user;
	@BeforeEach
	public void setup() {
		user = new User("user",1);
	}
	@Test
	public void getUserRow() {
		int actualRow = user.getRow();
		assertEquals(1, actualRow);
	}
	
	@Test
	public void setUserRow() {
		user.setRow(4);
		assertEquals(4, user.getRow());
	}
	
	@Test
	public void getUserColumn() {
		int actualColumn = user.getCurrentColumn();
		assertEquals(0, actualColumn);
	}
	
	@Test
	public void setUserColumn() {
		user.setCurrentColumn(6);
		assertEquals(6, user.getCurrentColumn());
	}
	
	@Test
	public void getUserName() {
		String actualName = user.getName();
		assertEquals("user", actualName);
	}
	
	@Test
	public void setUserName() {
		user.setName("newName");
		assertEquals("newName", user.getName());
	}

}
