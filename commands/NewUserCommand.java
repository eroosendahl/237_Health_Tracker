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
	
	public NewUserCommand(String user, String csvFile) {
		name = user;
		csvFileName = csvFile;
	}
	
	@Override
	public int execute() {
		List<String> user = new LinkedList<String>();
		user.add("\n" + name);
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
		System.out.println(name);
		return 0;
	}
	
	@Override
	public int execute(String executionMod) {
		System.out.println(executionMod);
		return 0;
	}
	
	@Override
	public int helpMessage() {
		System.out.println("Format: ");
		return 0;
	}
}
