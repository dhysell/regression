package repository.gw.enums;

public class ReportPayloadRetrieval {
	
	public enum ReportType {
		None("<none>"),
		All("All"),
		Auto_Loss_History("Auto Loss History"),
		Insurance_Score("Insurance Score"),
		Policy_Information("Policy Information"),
		Property_Loss_History("Property Loss History");
		
		String value;
		
		private ReportType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	
	}
	
	public enum Direction {
		None("<none>"),
		Request("Request"),
		Response("Response");
		
		String value;
		
		private Direction(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
}
