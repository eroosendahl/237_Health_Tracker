package commands;


import commands.AbstractCommand;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class NewUserCommand extends AbstractCommand {
	String csvFileName;
	String userName;
	
	public NewUserCommand(String csvFile) {
		name = "newUser";
		csvFileName = csvFile;
	}
	
	@Override
	public int execute() {
		return 0;
	}
	
	@Override
	public int execute(String executionMod) {
		
		userName = executionMod;
		
		List<String> user = new LinkedList<String>();
		user.add("\n" + userName);
		try {
			File outputFile = new File(csvFileName);
			FileWriter data = new FileWriter(outputFile, true);
			String collect = user.stream().collect(Collectors.joining());
			data.append(collect);
			data.flush();
			data.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println(name);
		return 0;
	}
	
	@Override
	public int helpMessage() {
		System.out.println("Format: ");
		return 0;
	}
}
