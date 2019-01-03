package repository.gw.generate.custom;

import java.util.Date;

public class UICreditReportBankruptcies {
	
	private Date dateFiled;
	private String status;
	private String type;

	public Date getDateFiled() {
		return dateFiled;
	}
	
	public void setDateFiled(Date dateFiled) {
		this.dateFiled = dateFiled;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
