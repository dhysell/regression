package repository.gw.generate.custom;

import java.util.ArrayList;
import java.util.List;

public class SquirePersonalAutoCoverages {

	// coverages
	private repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit liability = repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit.FiftyHigh;
	private repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit medical = repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit.FiveK;
	private boolean uninsured = true;
	private repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit uninsuredLimit = repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit.TwentyFive;
	private boolean underinsured = false;
	private repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit underinsuredLimit = repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit.Fifty;

	// additional coverages
	private boolean accidentalDeath = false;
	private String accidentalDeathCount = "";
	private boolean driveOtherCar = false;

	// exclusions and conditions
	private boolean driverExclusion_304 = false;
	private String driverExclusionText = null;
	private boolean additionalInsured_361 = false;
	private String additionalInsuredText = null;
	private boolean specialEndorsement_305 = false;
	private List<String> specialEndorsementText = null;
	private boolean modification_301 = false;
	private String modificationText = null;
	
	private List<repository.gw.generate.custom.Endorsement301> end301List = new ArrayList<repository.gw.generate.custom.Endorsement301>();
	
	// constructors
	public SquirePersonalAutoCoverages() {

	}

	public SquirePersonalAutoCoverages(repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit liability, repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit medical) {
		this.liability = liability;
		this.medical = medical;
	}

	public SquirePersonalAutoCoverages(repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit liability, repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit medical, boolean uninsured,
                                       repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit uninsuredLimit, boolean underinsured, repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit underinsuredLimit) {
		this.liability = liability;
		this.medical = medical;
		this.uninsured = uninsured;
		this.uninsuredLimit = uninsuredLimit;
		this.underinsured = underinsured;
		this.underinsuredLimit = underinsuredLimit;
	}

	// getters and setters
	public repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit getLiability() {
		return liability;
	}

	public void setLiability(repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit liability) {
		this.liability = liability;
	}

	public repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit getMedical() {
		return medical;
	}

	public void setMedical(repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit medical) {
		this.medical = medical;
	}

	public boolean hasUninsured() {
		return uninsured;
	}

	public void setUninsured(boolean uninsured) {
		this.uninsured = uninsured;
	}

	public repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit getUninsuredLimit() {
		return uninsuredLimit;
	}

	public void setUninsuredLimit(repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit uninsuredLimit) {
		this.uninsuredLimit = uninsuredLimit;
	}

	public boolean hasUnderinsured() {
		return underinsured;
	}

	public void setUnderinsured(boolean underinsured) {
		this.underinsured = underinsured;
	}

	public repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit getUnderinsuredLimit() {
		return underinsuredLimit;
	}

	public void setUnderinsuredLimit(repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit underinsuredLimit) {
		this.underinsuredLimit = underinsuredLimit;
	}

	public boolean hasAccidentalDeath() {
		return accidentalDeath;
	}

	public void setAccidentalDeath(boolean accidentalDeath) {
		this.accidentalDeath = accidentalDeath;
	}

	public boolean hasDriveOtherCar() {
		return driveOtherCar;
	}

	public void setDriveOtherCar(boolean driveOtherCar) {
		this.driveOtherCar = driveOtherCar;
	}

	public boolean hasDriverExclusion_304() {
		return driverExclusion_304;
	}

	public void setDriverExclusion_304(boolean driverExclusion_304) {
		this.driverExclusion_304 = driverExclusion_304;
	}

	public String getDriverExclusionText() {
		return driverExclusionText;
	}

	public void setDriverExclusionText(String driverExclusionText) {
		this.driverExclusionText = driverExclusionText;
	}

	public boolean hasAdditionalInsured_361() {
		return additionalInsured_361;
	}

	public void setAdditionalInsured_361(boolean additionalInsured_361) {
		this.additionalInsured_361 = additionalInsured_361;
	}

	public String getAdditionalInsuredText() {
		return additionalInsuredText;
	}

	public void setAdditionalInsuredText(String additionalInsuredText) {
		this.additionalInsuredText = additionalInsuredText;
	}

	public boolean hasSpecialEndorsement_305() {
		return specialEndorsement_305;
	}

	public void setSpecialEndorsement_305(boolean specialEndorsement_305) {
		this.specialEndorsement_305 = specialEndorsement_305;
	}

	public List<String> getSpecialEndorsementText() {
		return specialEndorsementText;
	}

	public void setSpecialEndorsementText(List<String> specialEndorsementText) {
		this.specialEndorsementText = specialEndorsementText;
	}

	public boolean hasModification_301() {
		return modification_301;
	}

	public void setModification_301(boolean modification_301) {
		this.modification_301 = modification_301;
	}

	public String getModificationText() {
		return modificationText;
	}

	public void setModificationText(String modificationText) {
		this.modificationText = modificationText;
	}

	public List<repository.gw.generate.custom.Endorsement301> getEnd301List() {
		return end301List;
	}

	public void setEnd301List(List<Endorsement301> end301List) {
		this.end301List = end301List;
	}

	public String getAccidentalDeathCount() {
		return accidentalDeathCount;
	}

	public void setAccidentalDeathCount(String accidentalDeathCount) {
		this.accidentalDeathCount = accidentalDeathCount;
	}	

}
