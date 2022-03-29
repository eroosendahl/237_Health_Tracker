package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import commands.NewEntryCommand;
import commands.NewUserCommand;
import main.User;


public class BasicTests {

	@Test
	void testAddUser() throws FileNotFoundException {
		
		String testName = "Nicholas";
		File testFile = new File("testFile.csv");
		
		NewUserCommand userCom = new NewUserCommand("testFile.csv");
		
		userCom.execute(testName);
		
		Scanner scan = new Scanner(testFile);
		scan.useDelimiter(",");
		
		boolean matchFound = false;
		
		while(scan.hasNext()) {
			
			String line = scan.next();
			
			if(line.contains(testName)) {
				matchFound = true;
				break;
			}
		}
		
		assertEquals(true, matchFound);
		
	}
	
	
	@Test
	void testAddEntry() throws FileNotFoundException {
		
		String fileName = "testFile.csv";
		String testEntry = "Walk 500 09/16/2000";
		String expectedEntry = "09/16/2000 Walk(500)";
		User testUser = new User();
		testUser.setCurrentColumn(1);
		testUser.setRow(1);
		testUser.setName("Nicholas");
		
		
		NewEntryCommand entryCom = new NewEntryCommand(testUser,fileName);
		entryCom.execute(testEntry);
		
		Scanner scan = new Scanner(new File(fileName));
		scan.useDelimiter(",");
		
		boolean matchFound = false;
		
		while(scan.hasNext()) {
			
			String line = scan.next();
			
			if(line.contains(expectedEntry)) {
				matchFound = true;
				break;
			}
		}
		
		assertEquals(true, matchFound);
		
	}
	
}
