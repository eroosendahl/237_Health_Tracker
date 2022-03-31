package commands;

import commands.AbstractCommand;
import main.CommandPrompt;
import main.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class NewUserCommand extends AbstractCommand {
	String csvFileName;
	User user;
	String userName;
	CommandPrompt commandPrompt;
	
	public NewUserCommand(CommandPrompt cp) {
		name = "NewUser";
		commandPrompt = cp;
	}
	
//	public NewUserCommand(String csvFile) {
//		name = "newUser";
//		csvFileName = csvFile;
//	}
	
	@Override
	public int execute() {
		System.out.println("Value required for creating a new user.");
		return 0;
	}
	
	@Override
	public int execute(String executionMod) {
		
		System.out.println("Executing NewUserCommand");
		
		csvFileName = commandPrompt.getFile();
		user = commandPrompt.getCurrentUser();
		userName = user.getName();
		userName = executionMod;
		
		commandPrompt.loadExistantUsers();
		
		List<String> user = new LinkedList<String>();
		user.add(userName + "\n");
		try {
			File outputFile = new File(csvFileName);
			FileWriter data = new FileWriter(outputFile, true);
			String collect = user.stream().collect(Collectors.joining());
			
			if (commandPrompt.isUniqueUser(collect.trim()) && commandPrompt.isAlphaNumericUsername(collect.trim())) {
				data.append(collect);
			} else if (!commandPrompt.isAlphaNumericUsername(collect.trim())) {
				System.out.println("Invalid username: only pure alphanumeric usernames are accepted.");
			} else if (!commandPrompt.isUniqueUser(collect.trim())) {
				System.out.println("Can't add user: duplicate username.");
			}

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
		System.out.println("Format: NewUser [new-username]");
		return 0;
	}
}
