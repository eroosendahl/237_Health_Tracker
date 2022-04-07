package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.User;

class UserTest {
	User user;
	@BeforeEach
	void setup() {
		user = new User("user",1);
	}
	@Test
	void getUserRow() {
		int actualRow = user.getRow();
		assertEquals(1, actualRow);
	}
	
	@Test
	void setUserRow() {
		user.setRow(4);
		assertEquals(4, user.getRow());
	}
	
	@Test
	void getUserColumn() {
		int actualColumn = user.getCurrentColumn();
		assertEquals(0, actualColumn);
	}
	
	@Test
	void setUserColumn() {
		user.setCurrentColumn(6);
		assertEquals(6, user.getCurrentColumn());
	}
	
	@Test
	void getUserName() {
		String actualName = user.getName();
		assertEquals("user", actualName);
	}
	
	@Test
	void setUserName() {
		user.setName("newName");
		assertEquals("newName", user.getName());
	}

}
