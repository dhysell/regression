package persistence.globaldatarepo.entities;

// Generated Jan 26, 2016 11:51:07 AM by Hibernate Tools 4.0.0

import javax.persistence.*;

@Entity
@Table(name = "PC_RoundTrip", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class PcRoundTrip {

	private int id;
	private int accountNumber;
	private String agentUserName;
	private String accountDate;

	public PcRoundTrip() {
	}

	public PcRoundTrip(int id, int accountNumber, String agentUserName) {
		this.id = id;
		this.accountNumber = accountNumber;
		this.agentUserName = agentUserName;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "AccountNumber", nullable = false)
	public int getAccountNumber() {
		return this.accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Column(name = "AgentUserName", nullable = false, length = 50)
	public String getAgentUserName() {
		return this.agentUserName;
	}

	public void setAgentUserName(String agentUserName) {
		this.agentUserName = agentUserName;
	}
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "AccountDate", nullable = true, length = 50)
	public String getAccountDate() {
		return this.accountDate;
	}

	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}

}
