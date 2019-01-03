package persistence.enums;

public class Underwriter {
	public enum UnderwriterLine {
		Commercial("Commercial"), 
		Personal("Personal");
		String value;
		
		private UnderwriterLine(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum UnderwriterTitle {
		Underwriter("Underwriter"), 
		Underwriter_Supervisor("Underwriting Supervisor"),
		Assistant_Underwriter("Assistant Underwriter"),
		Processor("Processor");
		String value;
		
		private UnderwriterTitle(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
}
