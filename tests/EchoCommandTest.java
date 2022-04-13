package tests;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import commands.EchoCommand;

public class EchoCommandTest {
	
	@Test
	public void testEcho() {
		ByteArrayOutputStream newOut = new ByteArrayOutputStream();
		PrintStream oldOut = System.out;
		System.setOut(new PrintStream(newOut));
		
		String message = "Hello World";
		String sysOutLnSuffix = "\r\n";
		EchoCommand echoCommand = new EchoCommand();
		echoCommand.execute(message);
		
		String output = newOut.toString();
		
		assertEquals(message+sysOutLnSuffix, output);
	}

}
