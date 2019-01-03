package repository.gw.generate.custom;

public class DBA {

	private String dba;
	private String retireDate;

	public DBA() {

	}

	public DBA(String dba) {
		setDBA(dba);
	}

	public DBA(String dba, String retireDate) {
		setDBA(dba);
		setRetireDate(retireDate);
	}

	public String getDBA() {
		return this.dba;
	}

	public void setDBA(String dba) {
		this.dba = dba;
	}

	public String getRetireDate() {
		return retireDate;
	}

	public void setRetireDate(String retireDate) {
		this.retireDate = retireDate;
	}

}
