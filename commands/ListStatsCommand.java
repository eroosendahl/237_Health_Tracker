package commands;

	import java.util.ArrayList;

	import main.CommandPrompt;
	import main.User;
	import main.HealthTrackerGeneralVariables.endState;

	public class ListStatsCommand extends AbstractCommand {
		CommandPrompt commandPrompt;
		
		public ListStatsCommand(CommandPrompt cp) {
			commandPrompt = cp;
			name = "listStats";
		}

		@Override
		public int execute() {
			System.out.println("Listing stats without help info:");
			commandPrompt.printSupportedHealthStats();
			return endState.SUCCESS.value();
		}

		@Override
		public int execute(String executionMod) {
			return 0;
		}

		@Override
		public int formatMessage() {
			System.out.println("listStats");
			return 0;
		}

		@Override
		public int descriptionMessage() {
			System.out.println("Lists available stats.");
			return 0;
		}

	}
