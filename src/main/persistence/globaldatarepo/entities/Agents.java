package persistence.globaldatarepo.entities;

import javax.persistence.*;

@Entity
@Table(name = "PC_AgentsMaster", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class Agents {

	public String agentNum;
	public String agentFirstName;
	public String agentMiddleName;
	public String agentLastName;
	public String agentUserName;
	public String agentPassword;
	public String agencyName;
	public Boolean agencyManager;
	public String agentFullName;
	public String agentRegion;
	public String agentPA;
	public String agentSA;
	public String agentCounty;
	public String agentPreferredName;
	public int agentUsedForPolicyCreation;
	public int agentPolicyTypeCreated;
	public int agentClaimedByVM;
	public boolean outOfSync;
	public String agentCountyCode;

	public Agents() {
	}
	
	

	public Agents(String agentNum, String agentFirstName,
			String agentLastName, String agentUserName, String agentPassword, String agencyName, String agentRegion, String agentCounty) {
		this.agentNum = agentNum;
		this.agentFirstName = agentFirstName;
		this.agentLastName = agentLastName;
		this.agentUserName = agentUserName;
		this.agentPassword = agentPassword;
		this.agencyName = agencyName;
		this.agentRegion = agentRegion;
		this.agentCounty = agentCounty;
	}

	public Agents(String agentNum, String agentFirstName,
			String agentLastName, String agentUserName, String agentPassword, String agencyName,
			Boolean agencyManager, String agentRegion, String agentCounty) {
		this.agentNum = agentNum;
		this.agentFirstName = agentFirstName;
		this.agentLastName = agentLastName;
		this.agentUserName = agentUserName;
		this.agencyName = agencyName;
		this.agencyManager = agencyManager;
		this.agentRegion = agentRegion;
		this.agentCounty = agentCounty;
	}
	
	public Agents(String agentNum, String agentFirstName,
			String agentLastName, String agentUserName, String agentPassword, String agencyName,
			Boolean agencyManager, String agentPA, String agentSA, String agentRegion, String agentCounty) {
		this.agentNum = agentNum;
		this.agentFirstName = agentFirstName;
		this.agentLastName = agentLastName;
		this.agentUserName = agentUserName;
		this.agencyName = agencyName;
		this.agencyManager = agencyManager;
		this.agentPA = agentPA;
		this.agentSA = agentSA;
		this.agentRegion = agentRegion;
		this.agentCounty = agentCounty;
	}

	@Id
	@Column(name = "AgentNum", unique = true, nullable = false, length = 50)
	public String getAgentNum() {
		return this.agentNum;
	}

	public void setAgentNum(String agentNum) {
		this.agentNum = agentNum;
	}

	@Column(name = "AgentFirstName", nullable = false, length = 50)
	public String getAgentFirstName() {
		return this.agentFirstName;
	}

	public void setAgentFirstName(String agentFirstName) {
		this.agentFirstName = agentFirstName;
	}

	@Column(name = "AgentLastName", nullable = false, length = 50)
	public String getAgentLastName() {
		return this.agentLastName;
	}

	public void setAgentLastName(String agentLastName) {
		this.agentLastName = agentLastName;
	}

	@Column(name = "AgentUserName", nullable = false, length = 50)
	public String getAgentUserName() {
		return this.agentUserName;
	}

	public void setAgentUserName(String agentUserName) {
		this.agentUserName = agentUserName;
	}
	
	@Column(name = "AgentPassword", nullable = false, length = 50)
	public String getAgentPassword() {
		return this.agentPassword;
	}

	public void setAgentPassword(String agentPassword) {
		this.agentPassword = agentPassword;
	}

	@Column(name = "AgencyName", nullable = true, length = 50)
	public String getAgencyName() {
		return this.agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	@Column(name = "AgencyManager")
	public Boolean getAgencyManager() {
		return this.agencyManager;
	}

	public void setAgencyManager(Boolean agencyManager) {
		this.agencyManager = agencyManager;
	}
	
	@Column(name = "OutOfSync")
	public Boolean getOutOfSync() {
		return this.outOfSync;
	}

	public void setOutOfSync(Boolean outOfSync) {
		this.outOfSync = outOfSync;
	}
	
	@Column(name = "AgentSA")
	public String getAgentSA() {
		return this.agentSA;
	}

	public void setAgentSA(String agentSA) {
		this.agentSA = agentSA;
	}
	
	@Column(name = "AgentPA")
	public String getAgentPA() {
		return this.agentPA;
	}

	public void setAgentPA(String agentPA) {
		this.agentPA = agentPA;
	}

	@Column(name = "AgentMiddleName")
	public String getAgentMiddleName() {
		return agentMiddleName;
	}

	public void setAgentMiddleName(String agentMiddleName) {
		this.agentMiddleName = agentMiddleName;
	}
	
	@Column(name = "AgentRegion")
	public String getAgentRegion() {
		return this.agentRegion;
	}

	public void setAgentRegion(String agentRegion) {
		this.agentRegion = agentRegion;
	}
	
	@Column(name = "AgentUsedForPolicyCreation")
	public int getAgentUsedForPolicyCreation() {
		return this.agentUsedForPolicyCreation;
	}

	public void setAgentUsedForPolicyCreation(int agentUsedForPolicyCreation) {
		this.agentUsedForPolicyCreation = agentUsedForPolicyCreation;
	}
	
	@Column(name = "AgentPolicyTypeCreated")
	public int getAgentPolicyTypeCreated() {
		return this.agentPolicyTypeCreated;
	}

	public void setAgentPolicyTypeCreated(int agentPolicyTypeCreated) {
		this.agentPolicyTypeCreated = agentPolicyTypeCreated;
	}
	
	@Column(name = "AgentClaimedByVM")
	public int getAgentClaimedByVM() {
		return this.agentClaimedByVM;
	}

	public void setAgentClaimedByVM(int agentClaimedByVM) {
		this.agentClaimedByVM = agentClaimedByVM;
	}

	@Transient
	public String getAgentFullName() {
		String returnString = this.agentFirstName + " ";
		if(agentMiddleName != null && !agentMiddleName.trim().isEmpty()) {
			returnString = returnString + agentMiddleName + " ";
		}
		returnString = returnString + agentLastName;
		return returnString;
	}

	@Transient
	public void setAgentFullName(String agentFullName) {
		this.agentFullName = agentFullName;
	}
	
	@Column(name = "AgentCounty")
	public String getAgentCounty() {
		return this.agentCounty;
	}

	public void setAgentCounty(String agentCounty) {
		this.agentCounty = agentCounty;
	}


	@Column(name = "AgentPreferredName", nullable = true, length = 100)
	public String getAgentPreferredName() {
		return agentPreferredName;
	}

	public void setAgentPreferredName(String agentPreferredName) {
		this.agentPreferredName = agentPreferredName;
	}
	
	@Column(name = "AgentCountyCode", nullable = true, length = 100)
	public String getAgentCountyCode() {
		return agentCountyCode;
	}

	public void setAgentCountyCode(String agentCountyCode) {
		this.agentCountyCode = agentCountyCode;
	}
	
	
	
	@Transient
	public String getAgentPreferredNameAndNumber() {
		String returnString = this.agentPreferredName + " (" + this.agentNum + ")";
		return returnString;
	}
}
