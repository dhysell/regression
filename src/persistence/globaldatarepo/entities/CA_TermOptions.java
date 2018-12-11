package persistence.globaldatarepo.entities;
// default package
// Generated Nov 9, 2016 6:02:51 PM by Hibernate Tools 5.2.0.Beta1

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * CaTermOptions generated by hbm2java
 */
@Entity
@Table(name = "CA_TermOptions", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class CA_TermOptions {

	private Integer id;
	private String coverage;
	private String coverageTerm;
	private String optionValue;
	private String description;
	private String userSelectable;
	private String default_;

	public CA_TermOptions() {
	}

	public CA_TermOptions(String coverage, String coverageTerm, String optionValue, String description, String userSelectable, String default_) {
		this.coverage = coverage;
		this.coverageTerm = coverageTerm;
		this.optionValue = optionValue;
		this.description = description;
		this.userSelectable = userSelectable;
		this.default_ = default_;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "Coverage", length = 500)
	public String getCoverage() {
		return this.coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	@Column(name = "CoverageTerm", length = 500)
	public String getCoverageTerm() {
		return this.coverageTerm;
	}

	public void setCoverageTerm(String coverageTerm) {
		this.coverageTerm = coverageTerm;
	}

	@Column(name = "OptionValue", length = 500)
	public String getOptionValue() {
		return this.optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	@Column(name = "Description", length = 500)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "UserSelectable", length = 500)
	public String getUserSelectable() {
		return this.userSelectable;
	}

	public void setUserSelectable(String userSelectable) {
		this.userSelectable = userSelectable;
	}

	@Column(name = "Default", length = 500)
	public String getDefault_() {
		return this.default_;
	}

	public void setDefault_(String default_) {
		this.default_ = default_;
	}

}
