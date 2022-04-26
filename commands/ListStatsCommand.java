package commands;

	import java.util.ArrayList;

	import main.CommandPrompt;
	import main.User;
	import main.HealthTrackerGeneralVariables.endState;

	public class ListStatsCommand extends AbstractCommand {
		
		public ListStatsCommand(CommandPrompt cp) {
			commandPrompt = cp;
			name = "listStats";
		}

		@Override
		public int execute() {
			System.out.println("Listing stats without help info:");
			commandPrompt.initializeHealthStats();
			commandPrompt.printSupportedHealthStats();
			return endState.SUCCESS.value();
		}

		@Override
		public int execute(String executionMod) {
			return 0;
		}

		@Override
		public String formatMessage() {
			return "listStats";
		}

		@Override
		public String descriptionMessage() {
			return "Lists available stats.";
		}

	}
