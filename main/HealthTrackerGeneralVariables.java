package main;

public class HealthTrackerGeneralVariables {
	
	public enum endState {
		UNIMPLEMENTED(-2),
		GENERAL_FAILURE(-1),
		SUCCESS(0),
		;
		
		private final int value;	
		
		endState(int value) {
			this.value = value;
		}
		
		// easily confusable with default "enum.values()"
		public int value() { return value; }
	}

}
