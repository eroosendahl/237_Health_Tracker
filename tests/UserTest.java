package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.User;

public class UserTest {
	User user;
	
	@BeforeEach
	public void setup() {
		user = new User("user",1);
	}
	
	@Test
	public void testGetUserRow() {
		int actualRow = user.getRow();
		assertEquals(1, actualRow);
	}
	
	@Test
	public void testSetUserRow() {
		user.setRow(4);
		assertEquals(4, user.getRow());
	}
	
	@Test
	public void testGetUserColumn() {
		int actualColumn = user.getCurrentColumn();
		assertEquals(0, actualColumn);
	}
	
	@Test
	public void testSetUserColumn() {
		user.setCurrentColumn(6);
		assertEquals(6, user.getCurrentColumn());
	}
	
	@Test
	public void testGetUserName() {
		String actualName = user.getName();
		assertEquals("user", actualName);
	}
	
	@Test
	public void testSetUserName() {
		user.setName("newName");
		assertEquals("newName", user.getName());
	}
}
