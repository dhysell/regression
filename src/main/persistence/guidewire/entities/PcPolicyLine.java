package persistence.guidewire.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Clob;

/**
 * PcPolicyline generated by hbm2java
 */
@Entity
@Table(name = "pc_policyline", schema = "dbo", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "BranchID", "FixedID", "Subtype", "ID" }),
		@UniqueConstraint(columnNames = { "BranchID", "PatternCode", "ID" }),
		@UniqueConstraint(columnNames = "PublicID") })
public class PcPolicyLine {

	private long id;
	private String retroactiveDate;
	private String referenceDateInternal;
	private Long cpblanketAutoNumberSeq;
	private Long equipmentAutoNumberSeq;
	private Boolean initialExclusionsCreated;
	private String effectiveDate;
	private String autoSymbolsManualEditDate;
	private String publicId;
	private Long businessVehicleAutoNumberSeq;
	private Integer changeType;
	private Long minimumPremium;
	private String customAutoSymbolDesc;
	private String expirationDate;
	private Long archivePartition;
	private Boolean splitLimits;
	private Long createUserId;
	private BigDecimal manuscriptPremium;
	private PcPolicyPeriod pcPolicyPeriod;
	private String claimsMadeOrigEffDate;
	private String patternCode;
	private Boolean locationLimits;
	private Integer beanVersion;
	private Integer glcoverageForm;
	private Clob manuscriptOptionDesc;
	private Integer policyType;
	private Long updateUserId;
	private Boolean addtlCovgBldgExt;
	private String updateTime;
	private Boolean initialCoveragesCreated;
	private Integer numAddInsured;
	private Long basedOnId;
	private Boolean addtlCovgExt;
	private Boolean viewBundledCoverages;
	private int subtype;
	private Integer blanketType;
	private Boolean pollutionCleanupExp;
	private Long governingClass;
	private Integer smallBusinessType;
	private Boolean initialConditionsCreated;
	private String createTime;
	private Boolean liquorLiabilityExt;
	private Long personalVehicleAutoNumberSeq;
	private Integer fleet;
	private long fixedId;
	private Boolean addtlCovgLocExt;
	private Boolean pddeductibleShowWarningExt;
	private Boolean pddeductibleExt;
	private Integer manuscriptPremiumCur;
	private Integer preferredCoverageCurrency;
	private Integer pimpolicyType;
	private Long exposureUwquestions;
	private Boolean waterCraftExists;
	private Boolean livestockExists;
	private Integer holiabilityDiscountExt;
	private Boolean hasWatercraftExposure;
	private Integer personalAutoDiscountExt;
	private String autoDiscountReasonExt;
	private Integer standardFireDiscountExt;
	private Boolean cargoExists;
	private String holchangeReasonExt;
	private Long busActivityExposureAutoNumSeq;
	private String hopchangeReasonExt;
	private Long cargoSeq;
	private Boolean personalEquipmentExists;
	private Integer hopolicyType;
	private Boolean recreationVehicleExists;
	private Integer pimdiscountExt;
	private Long recreationVehicleSeq;
	private Long farmEquipmentSeq;
	private String standardFireChangeReasonExt;
	private Long personalEquipmentSeq;
	private String clueStatusExt;
	private Boolean hasLocationExposure;
	private Long watercraftSeq;
	private Boolean hasVehicleExposure;
	private String pimchangeReasonExt;
	private Boolean hasBusinessActivityExposure;
	private Boolean farmEquipmentExists;
	private Long livestockSeq;
	private String userRemovedCoverages;
	private Long watercraftExposureAutoNumSeq;
	private Long vehicleExposureAutoNumSeq;
	private Integer hopropertyDiscountExt;

	public PcPolicyLine() {
	}

	public PcPolicyLine(long id, String publicId, PcPolicyPeriod pcPolicyPeriod, String patternCode, String updateTime, int subtype,
			String createTime, long fixedId) {
		this.id = id;
		this.publicId = publicId;
		this.pcPolicyPeriod = pcPolicyPeriod;
		this.patternCode = patternCode;
		this.updateTime = updateTime;
		this.subtype = subtype;
		this.createTime = createTime;
		this.fixedId = fixedId;
	}

	public PcPolicyLine(long id, String retroactiveDate, String referenceDateInternal, Long cpblanketAutoNumberSeq,
			Long equipmentAutoNumberSeq, Boolean initialExclusionsCreated, String effectiveDate,
			String autoSymbolsManualEditDate, String publicId, Long businessVehicleAutoNumberSeq, Integer changeType,
			Long minimumPremium, String customAutoSymbolDesc, String expirationDate, Long archivePartition,
			Boolean splitLimits, Long createUserId, BigDecimal manuscriptPremium, PcPolicyPeriod pcPolicyPeriod,
			String claimsMadeOrigEffDate, String patternCode, Boolean locationLimits, Integer beanVersion,
			Integer glcoverageForm, Clob manuscriptOptionDesc, Integer policyType, Long updateUserId,
			Boolean addtlCovgBldgExt, String updateTime, Boolean initialCoveragesCreated, Integer numAddInsured,
			Long basedOnId, Boolean addtlCovgExt, Boolean viewBundledCoverages, int subtype, Integer blanketType,
			Boolean pollutionCleanupExp, Long governingClass, Integer smallBusinessType,
			Boolean initialConditionsCreated, String createTime, Boolean liquorLiabilityExt,
			Long personalVehicleAutoNumberSeq, Integer fleet, long fixedId, Boolean addtlCovgLocExt,
			Boolean pddeductibleShowWarningExt, Boolean pddeductibleExt, Integer manuscriptPremiumCur,
			Integer preferredCoverageCurrency, Integer pimpolicyType, Long exposureUwquestions,
			Boolean waterCraftExists, Boolean livestockExists, Integer holiabilityDiscountExt,
			Boolean hasWatercraftExposure, Integer personalAutoDiscountExt, String autoDiscountReasonExt,
			Integer standardFireDiscountExt, Boolean cargoExists, String holchangeReasonExt,
			Long busActivityExposureAutoNumSeq, String hopchangeReasonExt, Long cargoSeq,
			Boolean personalEquipmentExists, Integer hopolicyType, Boolean recreationVehicleExists,
			Integer pimdiscountExt, Long recreationVehicleSeq, Long farmEquipmentSeq,
			String standardFireChangeReasonExt, Long personalEquipmentSeq, String clueStatusExt,
			Boolean hasLocationExposure, Long watercraftSeq, Boolean hasVehicleExposure, String pimchangeReasonExt,
			Boolean hasBusinessActivityExposure, Boolean farmEquipmentExists, Long livestockSeq,
			String userRemovedCoverages, Long watercraftExposureAutoNumSeq, Long vehicleExposureAutoNumSeq,
			Integer hopropertyDiscountExt) {
		this.id = id;
		this.retroactiveDate = retroactiveDate;
		this.referenceDateInternal = referenceDateInternal;
		this.cpblanketAutoNumberSeq = cpblanketAutoNumberSeq;
		this.equipmentAutoNumberSeq = equipmentAutoNumberSeq;
		this.initialExclusionsCreated = initialExclusionsCreated;
		this.effectiveDate = effectiveDate;
		this.autoSymbolsManualEditDate = autoSymbolsManualEditDate;
		this.publicId = publicId;
		this.businessVehicleAutoNumberSeq = businessVehicleAutoNumberSeq;
		this.changeType = changeType;
		this.minimumPremium = minimumPremium;
		this.customAutoSymbolDesc = customAutoSymbolDesc;
		this.expirationDate = expirationDate;
		this.archivePartition = archivePartition;
		this.splitLimits = splitLimits;
		this.createUserId = createUserId;
		this.manuscriptPremium = manuscriptPremium;
		this.pcPolicyPeriod = pcPolicyPeriod;
		this.claimsMadeOrigEffDate = claimsMadeOrigEffDate;
		this.patternCode = patternCode;
		this.locationLimits = locationLimits;
		this.beanVersion = beanVersion;
		this.glcoverageForm = glcoverageForm;
		this.manuscriptOptionDesc = manuscriptOptionDesc;
		this.policyType = policyType;
		this.updateUserId = updateUserId;
		this.addtlCovgBldgExt = addtlCovgBldgExt;
		this.updateTime = updateTime;
		this.initialCoveragesCreated = initialCoveragesCreated;
		this.numAddInsured = numAddInsured;
		this.basedOnId = basedOnId;
		this.addtlCovgExt = addtlCovgExt;
		this.viewBundledCoverages = viewBundledCoverages;
		this.subtype = subtype;
		this.blanketType = blanketType;
		this.pollutionCleanupExp = pollutionCleanupExp;
		this.governingClass = governingClass;
		this.smallBusinessType = smallBusinessType;
		this.initialConditionsCreated = initialConditionsCreated;
		this.createTime = createTime;
		this.liquorLiabilityExt = liquorLiabilityExt;
		this.personalVehicleAutoNumberSeq = personalVehicleAutoNumberSeq;
		this.fleet = fleet;
		this.fixedId = fixedId;
		this.addtlCovgLocExt = addtlCovgLocExt;
		this.pddeductibleShowWarningExt = pddeductibleShowWarningExt;
		this.pddeductibleExt = pddeductibleExt;
		this.manuscriptPremiumCur = manuscriptPremiumCur;
		this.preferredCoverageCurrency = preferredCoverageCurrency;
		this.pimpolicyType = pimpolicyType;
		this.exposureUwquestions = exposureUwquestions;
		this.waterCraftExists = waterCraftExists;
		this.livestockExists = livestockExists;
		this.holiabilityDiscountExt = holiabilityDiscountExt;
		this.hasWatercraftExposure = hasWatercraftExposure;
		this.personalAutoDiscountExt = personalAutoDiscountExt;
		this.autoDiscountReasonExt = autoDiscountReasonExt;
		this.standardFireDiscountExt = standardFireDiscountExt;
		this.cargoExists = cargoExists;
		this.holchangeReasonExt = holchangeReasonExt;
		this.busActivityExposureAutoNumSeq = busActivityExposureAutoNumSeq;
		this.hopchangeReasonExt = hopchangeReasonExt;
		this.cargoSeq = cargoSeq;
		this.personalEquipmentExists = personalEquipmentExists;
		this.hopolicyType = hopolicyType;
		this.recreationVehicleExists = recreationVehicleExists;
		this.pimdiscountExt = pimdiscountExt;
		this.recreationVehicleSeq = recreationVehicleSeq;
		this.farmEquipmentSeq = farmEquipmentSeq;
		this.standardFireChangeReasonExt = standardFireChangeReasonExt;
		this.personalEquipmentSeq = personalEquipmentSeq;
		this.clueStatusExt = clueStatusExt;
		this.hasLocationExposure = hasLocationExposure;
		this.watercraftSeq = watercraftSeq;
		this.hasVehicleExposure = hasVehicleExposure;
		this.pimchangeReasonExt = pimchangeReasonExt;
		this.hasBusinessActivityExposure = hasBusinessActivityExposure;
		this.farmEquipmentExists = farmEquipmentExists;
		this.livestockSeq = livestockSeq;
		this.userRemovedCoverages = userRemovedCoverages;
		this.watercraftExposureAutoNumSeq = watercraftExposureAutoNumSeq;
		this.vehicleExposureAutoNumSeq = vehicleExposureAutoNumSeq;
		this.hopropertyDiscountExt = hopropertyDiscountExt;
	}

	@Id

	@Column(name = "ID", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "RetroactiveDate", length = 27)
	public String getRetroactiveDate() {
		return this.retroactiveDate;
	}

	public void setRetroactiveDate(String retroactiveDate) {
		this.retroactiveDate = retroactiveDate;
	}

	@Column(name = "ReferenceDateInternal", length = 27)
	public String getReferenceDateInternal() {
		return this.referenceDateInternal;
	}

	public void setReferenceDateInternal(String referenceDateInternal) {
		this.referenceDateInternal = referenceDateInternal;
	}

	@Column(name = "CPBlanketAutoNumberSeq")
	public Long getCpblanketAutoNumberSeq() {
		return this.cpblanketAutoNumberSeq;
	}

	public void setCpblanketAutoNumberSeq(Long cpblanketAutoNumberSeq) {
		this.cpblanketAutoNumberSeq = cpblanketAutoNumberSeq;
	}

	@Column(name = "EquipmentAutoNumberSeq")
	public Long getEquipmentAutoNumberSeq() {
		return this.equipmentAutoNumberSeq;
	}

	public void setEquipmentAutoNumberSeq(Long equipmentAutoNumberSeq) {
		this.equipmentAutoNumberSeq = equipmentAutoNumberSeq;
	}

	@Column(name = "InitialExclusionsCreated")
	public Boolean getInitialExclusionsCreated() {
		return this.initialExclusionsCreated;
	}

	public void setInitialExclusionsCreated(Boolean initialExclusionsCreated) {
		this.initialExclusionsCreated = initialExclusionsCreated;
	}

	@Column(name = "EffectiveDate", length = 27)
	public String getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@Column(name = "AutoSymbolsManualEditDate", length = 27)
	public String getAutoSymbolsManualEditDate() {
		return this.autoSymbolsManualEditDate;
	}

	public void setAutoSymbolsManualEditDate(String autoSymbolsManualEditDate) {
		this.autoSymbolsManualEditDate = autoSymbolsManualEditDate;
	}

	@Column(name = "PublicID", unique = true, nullable = false, length = 64)
	public String getPublicId() {
		return this.publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	@Column(name = "BusinessVehicleAutoNumberSeq")
	public Long getBusinessVehicleAutoNumberSeq() {
		return this.businessVehicleAutoNumberSeq;
	}

	public void setBusinessVehicleAutoNumberSeq(Long businessVehicleAutoNumberSeq) {
		this.businessVehicleAutoNumberSeq = businessVehicleAutoNumberSeq;
	}

	@Column(name = "ChangeType")
	public Integer getChangeType() {
		return this.changeType;
	}

	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}

	@Column(name = "MinimumPremium")
	public Long getMinimumPremium() {
		return this.minimumPremium;
	}

	public void setMinimumPremium(Long minimumPremium) {
		this.minimumPremium = minimumPremium;
	}

	@Column(name = "CustomAutoSymbolDesc")
	public String getCustomAutoSymbolDesc() {
		return this.customAutoSymbolDesc;
	}

	public void setCustomAutoSymbolDesc(String customAutoSymbolDesc) {
		this.customAutoSymbolDesc = customAutoSymbolDesc;
	}

	@Column(name = "ExpirationDate", length = 27)
	public String getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	@Column(name = "ArchivePartition")
	public Long getArchivePartition() {
		return this.archivePartition;
	}

	public void setArchivePartition(Long archivePartition) {
		this.archivePartition = archivePartition;
	}

	@Column(name = "SplitLimits")
	public Boolean getSplitLimits() {
		return this.splitLimits;
	}

	public void setSplitLimits(Boolean splitLimits) {
		this.splitLimits = splitLimits;
	}

	@Column(name = "CreateUserID")
	public Long getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "ManuscriptPremium", precision = 18)
	public BigDecimal getManuscriptPremium() {
		return this.manuscriptPremium;
	}

	public void setManuscriptPremium(BigDecimal manuscriptPremium) {
		this.manuscriptPremium = manuscriptPremium;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BranchID", nullable = false)
	public PcPolicyPeriod getPcPolicyPeriod() {
		return this.pcPolicyPeriod;
	}

	public void setPcPolicyPeriod(PcPolicyPeriod pcPolicyPeriod) {
		this.pcPolicyPeriod = pcPolicyPeriod;
	}

	@Column(name = "ClaimsMadeOrigEffDate", length = 27)
	public String getClaimsMadeOrigEffDate() {
		return this.claimsMadeOrigEffDate;
	}

	public void setClaimsMadeOrigEffDate(String claimsMadeOrigEffDate) {
		this.claimsMadeOrigEffDate = claimsMadeOrigEffDate;
	}

	@Column(name = "PatternCode", nullable = false, length = 64)
	public String getPatternCode() {
		return this.patternCode;
	}

	public void setPatternCode(String patternCode) {
		this.patternCode = patternCode;
	}

	@Column(name = "LocationLimits")
	public Boolean getLocationLimits() {
		return this.locationLimits;
	}

	public void setLocationLimits(Boolean locationLimits) {
		this.locationLimits = locationLimits;
	}

	@Column(name = "BeanVersion")
	public Integer getBeanVersion() {
		return this.beanVersion;
	}

	public void setBeanVersion(Integer beanVersion) {
		this.beanVersion = beanVersion;
	}

	@Column(name = "GLCoverageForm")
	public Integer getGlcoverageForm() {
		return this.glcoverageForm;
	}

	public void setGlcoverageForm(Integer glcoverageForm) {
		this.glcoverageForm = glcoverageForm;
	}

	@Column(name = "ManuscriptOptionDesc")
	public Clob getManuscriptOptionDesc() {
		return this.manuscriptOptionDesc;
	}

	public void setManuscriptOptionDesc(Clob manuscriptOptionDesc) {
		this.manuscriptOptionDesc = manuscriptOptionDesc;
	}

	@Column(name = "PolicyType")
	public Integer getPolicyType() {
		return this.policyType;
	}

	public void setPolicyType(Integer policyType) {
		this.policyType = policyType;
	}

	@Column(name = "UpdateUserID")
	public Long getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Column(name = "AddtlCovgBldgExt")
	public Boolean getAddtlCovgBldgExt() {
		return this.addtlCovgBldgExt;
	}

	public void setAddtlCovgBldgExt(Boolean addtlCovgBldgExt) {
		this.addtlCovgBldgExt = addtlCovgBldgExt;
	}

	@Column(name = "UpdateTime", nullable = false, length = 27)
	public String getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "InitialCoveragesCreated")
	public Boolean getInitialCoveragesCreated() {
		return this.initialCoveragesCreated;
	}

	public void setInitialCoveragesCreated(Boolean initialCoveragesCreated) {
		this.initialCoveragesCreated = initialCoveragesCreated;
	}

	@Column(name = "NumAddInsured")
	public Integer getNumAddInsured() {
		return this.numAddInsured;
	}

	public void setNumAddInsured(Integer numAddInsured) {
		this.numAddInsured = numAddInsured;
	}

	@Column(name = "BasedOnID")
	public Long getBasedOnId() {
		return this.basedOnId;
	}

	public void setBasedOnId(Long basedOnId) {
		this.basedOnId = basedOnId;
	}

	@Column(name = "AddtlCovgExt")
	public Boolean getAddtlCovgExt() {
		return this.addtlCovgExt;
	}

	public void setAddtlCovgExt(Boolean addtlCovgExt) {
		this.addtlCovgExt = addtlCovgExt;
	}

	@Column(name = "ViewBundledCoverages")
	public Boolean getViewBundledCoverages() {
		return this.viewBundledCoverages;
	}

	public void setViewBundledCoverages(Boolean viewBundledCoverages) {
		this.viewBundledCoverages = viewBundledCoverages;
	}

	@Column(name = "Subtype", nullable = false)
	public int getSubtype() {
		return this.subtype;
	}

	public void setSubtype(int subtype) {
		this.subtype = subtype;
	}

	@Column(name = "BlanketType")
	public Integer getBlanketType() {
		return this.blanketType;
	}

	public void setBlanketType(Integer blanketType) {
		this.blanketType = blanketType;
	}

	@Column(name = "PollutionCleanupExp")
	public Boolean getPollutionCleanupExp() {
		return this.pollutionCleanupExp;
	}

	public void setPollutionCleanupExp(Boolean pollutionCleanupExp) {
		this.pollutionCleanupExp = pollutionCleanupExp;
	}

	@Column(name = "GoverningClass")
	public Long getGoverningClass() {
		return this.governingClass;
	}

	public void setGoverningClass(Long governingClass) {
		this.governingClass = governingClass;
	}

	@Column(name = "SmallBusinessType")
	public Integer getSmallBusinessType() {
		return this.smallBusinessType;
	}

	public void setSmallBusinessType(Integer smallBusinessType) {
		this.smallBusinessType = smallBusinessType;
	}

	@Column(name = "InitialConditionsCreated")
	public Boolean getInitialConditionsCreated() {
		return this.initialConditionsCreated;
	}

	public void setInitialConditionsCreated(Boolean initialConditionsCreated) {
		this.initialConditionsCreated = initialConditionsCreated;
	}

	@Column(name = "CreateTime", nullable = false, length = 27)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "LiquorLiabilityExt")
	public Boolean getLiquorLiabilityExt() {
		return this.liquorLiabilityExt;
	}

	public void setLiquorLiabilityExt(Boolean liquorLiabilityExt) {
		this.liquorLiabilityExt = liquorLiabilityExt;
	}

	@Column(name = "PersonalVehicleAutoNumberSeq")
	public Long getPersonalVehicleAutoNumberSeq() {
		return this.personalVehicleAutoNumberSeq;
	}

	public void setPersonalVehicleAutoNumberSeq(Long personalVehicleAutoNumberSeq) {
		this.personalVehicleAutoNumberSeq = personalVehicleAutoNumberSeq;
	}

	@Column(name = "Fleet")
	public Integer getFleet() {
		return this.fleet;
	}

	public void setFleet(Integer fleet) {
		this.fleet = fleet;
	}

	@Column(name = "FixedID", nullable = false)
	public long getFixedId() {
		return this.fixedId;
	}

	public void setFixedId(long fixedId) {
		this.fixedId = fixedId;
	}

	@Column(name = "AddtlCovgLocExt")
	public Boolean getAddtlCovgLocExt() {
		return this.addtlCovgLocExt;
	}

	public void setAddtlCovgLocExt(Boolean addtlCovgLocExt) {
		this.addtlCovgLocExt = addtlCovgLocExt;
	}

	@Column(name = "PDDeductibleShowWarningExt")
	public Boolean getPddeductibleShowWarningExt() {
		return this.pddeductibleShowWarningExt;
	}

	public void setPddeductibleShowWarningExt(Boolean pddeductibleShowWarningExt) {
		this.pddeductibleShowWarningExt = pddeductibleShowWarningExt;
	}

	@Column(name = "PDDeductibleExt")
	public Boolean getPddeductibleExt() {
		return this.pddeductibleExt;
	}

	public void setPddeductibleExt(Boolean pddeductibleExt) {
		this.pddeductibleExt = pddeductibleExt;
	}

	@Column(name = "ManuscriptPremium_cur")
	public Integer getManuscriptPremiumCur() {
		return this.manuscriptPremiumCur;
	}

	public void setManuscriptPremiumCur(Integer manuscriptPremiumCur) {
		this.manuscriptPremiumCur = manuscriptPremiumCur;
	}

	@Column(name = "PreferredCoverageCurrency")
	public Integer getPreferredCoverageCurrency() {
		return this.preferredCoverageCurrency;
	}

	public void setPreferredCoverageCurrency(Integer preferredCoverageCurrency) {
		this.preferredCoverageCurrency = preferredCoverageCurrency;
	}

	@Column(name = "PIMPolicyType")
	public Integer getPimpolicyType() {
		return this.pimpolicyType;
	}

	public void setPimpolicyType(Integer pimpolicyType) {
		this.pimpolicyType = pimpolicyType;
	}

	@Column(name = "ExposureUWQuestions")
	public Long getExposureUwquestions() {
		return this.exposureUwquestions;
	}

	public void setExposureUwquestions(Long exposureUwquestions) {
		this.exposureUwquestions = exposureUwquestions;
	}

	@Column(name = "WaterCraftExists")
	public Boolean getWaterCraftExists() {
		return this.waterCraftExists;
	}

	public void setWaterCraftExists(Boolean waterCraftExists) {
		this.waterCraftExists = waterCraftExists;
	}

	@Column(name = "LivestockExists")
	public Boolean getLivestockExists() {
		return this.livestockExists;
	}

	public void setLivestockExists(Boolean livestockExists) {
		this.livestockExists = livestockExists;
	}

	@Column(name = "HOLiabilityDiscount_Ext")
	public Integer getHoliabilityDiscountExt() {
		return this.holiabilityDiscountExt;
	}

	public void setHoliabilityDiscountExt(Integer holiabilityDiscountExt) {
		this.holiabilityDiscountExt = holiabilityDiscountExt;
	}

	@Column(name = "HasWatercraftExposure")
	public Boolean getHasWatercraftExposure() {
		return this.hasWatercraftExposure;
	}

	public void setHasWatercraftExposure(Boolean hasWatercraftExposure) {
		this.hasWatercraftExposure = hasWatercraftExposure;
	}

	@Column(name = "PersonalAutoDiscount_Ext")
	public Integer getPersonalAutoDiscountExt() {
		return this.personalAutoDiscountExt;
	}

	public void setPersonalAutoDiscountExt(Integer personalAutoDiscountExt) {
		this.personalAutoDiscountExt = personalAutoDiscountExt;
	}

	@Column(name = "AutoDiscountReason_Ext", length = 5000)
	public String getAutoDiscountReasonExt() {
		return this.autoDiscountReasonExt;
	}

	public void setAutoDiscountReasonExt(String autoDiscountReasonExt) {
		this.autoDiscountReasonExt = autoDiscountReasonExt;
	}

	@Column(name = "StandardFireDiscount_Ext")
	public Integer getStandardFireDiscountExt() {
		return this.standardFireDiscountExt;
	}

	public void setStandardFireDiscountExt(Integer standardFireDiscountExt) {
		this.standardFireDiscountExt = standardFireDiscountExt;
	}

	@Column(name = "CargoExists")
	public Boolean getCargoExists() {
		return this.cargoExists;
	}

	public void setCargoExists(Boolean cargoExists) {
		this.cargoExists = cargoExists;
	}

	@Column(name = "HOLChangeReason_Ext", length = 5000)
	public String getHolchangeReasonExt() {
		return this.holchangeReasonExt;
	}

	public void setHolchangeReasonExt(String holchangeReasonExt) {
		this.holchangeReasonExt = holchangeReasonExt;
	}

	@Column(name = "BusActivityExposureAutoNumSeq")
	public Long getBusActivityExposureAutoNumSeq() {
		return this.busActivityExposureAutoNumSeq;
	}

	public void setBusActivityExposureAutoNumSeq(Long busActivityExposureAutoNumSeq) {
		this.busActivityExposureAutoNumSeq = busActivityExposureAutoNumSeq;
	}

	@Column(name = "HOPChangeReason_Ext", length = 5000)
	public String getHopchangeReasonExt() {
		return this.hopchangeReasonExt;
	}

	public void setHopchangeReasonExt(String hopchangeReasonExt) {
		this.hopchangeReasonExt = hopchangeReasonExt;
	}

	@Column(name = "CargoSeq")
	public Long getCargoSeq() {
		return this.cargoSeq;
	}

	public void setCargoSeq(Long cargoSeq) {
		this.cargoSeq = cargoSeq;
	}

	@Column(name = "PersonalEquipmentExists")
	public Boolean getPersonalEquipmentExists() {
		return this.personalEquipmentExists;
	}

	public void setPersonalEquipmentExists(Boolean personalEquipmentExists) {
		this.personalEquipmentExists = personalEquipmentExists;
	}

	@Column(name = "HOPolicyType")
	public Integer getHopolicyType() {
		return this.hopolicyType;
	}

	public void setHopolicyType(Integer hopolicyType) {
		this.hopolicyType = hopolicyType;
	}

	@Column(name = "RecreationVehicleExists")
	public Boolean getRecreationVehicleExists() {
		return this.recreationVehicleExists;
	}

	public void setRecreationVehicleExists(Boolean recreationVehicleExists) {
		this.recreationVehicleExists = recreationVehicleExists;
	}

	@Column(name = "PIMDiscount_Ext")
	public Integer getPimdiscountExt() {
		return this.pimdiscountExt;
	}

	public void setPimdiscountExt(Integer pimdiscountExt) {
		this.pimdiscountExt = pimdiscountExt;
	}

	@Column(name = "RecreationVehicleSeq")
	public Long getRecreationVehicleSeq() {
		return this.recreationVehicleSeq;
	}

	public void setRecreationVehicleSeq(Long recreationVehicleSeq) {
		this.recreationVehicleSeq = recreationVehicleSeq;
	}

	@Column(name = "FarmEquipmentSeq")
	public Long getFarmEquipmentSeq() {
		return this.farmEquipmentSeq;
	}

	public void setFarmEquipmentSeq(Long farmEquipmentSeq) {
		this.farmEquipmentSeq = farmEquipmentSeq;
	}

	@Column(name = "StandardFireChangeReason_Ext", length = 5000)
	public String getStandardFireChangeReasonExt() {
		return this.standardFireChangeReasonExt;
	}

	public void setStandardFireChangeReasonExt(String standardFireChangeReasonExt) {
		this.standardFireChangeReasonExt = standardFireChangeReasonExt;
	}

	@Column(name = "PersonalEquipmentSeq")
	public Long getPersonalEquipmentSeq() {
		return this.personalEquipmentSeq;
	}

	public void setPersonalEquipmentSeq(Long personalEquipmentSeq) {
		this.personalEquipmentSeq = personalEquipmentSeq;
	}

	@Column(name = "ClueStatusExt", length = 300)
	public String getClueStatusExt() {
		return this.clueStatusExt;
	}

	public void setClueStatusExt(String clueStatusExt) {
		this.clueStatusExt = clueStatusExt;
	}

	@Column(name = "HasLocationExposure")
	public Boolean getHasLocationExposure() {
		return this.hasLocationExposure;
	}

	public void setHasLocationExposure(Boolean hasLocationExposure) {
		this.hasLocationExposure = hasLocationExposure;
	}

	@Column(name = "WatercraftSeq")
	public Long getWatercraftSeq() {
		return this.watercraftSeq;
	}

	public void setWatercraftSeq(Long watercraftSeq) {
		this.watercraftSeq = watercraftSeq;
	}

	@Column(name = "HasVehicleExposure")
	public Boolean getHasVehicleExposure() {
		return this.hasVehicleExposure;
	}

	public void setHasVehicleExposure(Boolean hasVehicleExposure) {
		this.hasVehicleExposure = hasVehicleExposure;
	}

	@Column(name = "PIMChangeReason_Ext", length = 5000)
	public String getPimchangeReasonExt() {
		return this.pimchangeReasonExt;
	}

	public void setPimchangeReasonExt(String pimchangeReasonExt) {
		this.pimchangeReasonExt = pimchangeReasonExt;
	}

	@Column(name = "HasBusinessActivityExposure")
	public Boolean getHasBusinessActivityExposure() {
		return this.hasBusinessActivityExposure;
	}

	public void setHasBusinessActivityExposure(Boolean hasBusinessActivityExposure) {
		this.hasBusinessActivityExposure = hasBusinessActivityExposure;
	}

	@Column(name = "FarmEquipmentExists")
	public Boolean getFarmEquipmentExists() {
		return this.farmEquipmentExists;
	}

	public void setFarmEquipmentExists(Boolean farmEquipmentExists) {
		this.farmEquipmentExists = farmEquipmentExists;
	}

	@Column(name = "LivestockSeq")
	public Long getLivestockSeq() {
		return this.livestockSeq;
	}

	public void setLivestockSeq(Long livestockSeq) {
		this.livestockSeq = livestockSeq;
	}

	@Column(name = "UserRemovedCoverages", length = 5000)
	public String getUserRemovedCoverages() {
		return this.userRemovedCoverages;
	}

	public void setUserRemovedCoverages(String userRemovedCoverages) {
		this.userRemovedCoverages = userRemovedCoverages;
	}

	@Column(name = "WatercraftExposureAutoNumSeq")
	public Long getWatercraftExposureAutoNumSeq() {
		return this.watercraftExposureAutoNumSeq;
	}

	public void setWatercraftExposureAutoNumSeq(Long watercraftExposureAutoNumSeq) {
		this.watercraftExposureAutoNumSeq = watercraftExposureAutoNumSeq;
	}

	@Column(name = "VehicleExposureAutoNumSeq")
	public Long getVehicleExposureAutoNumSeq() {
		return this.vehicleExposureAutoNumSeq;
	}

	public void setVehicleExposureAutoNumSeq(Long vehicleExposureAutoNumSeq) {
		this.vehicleExposureAutoNumSeq = vehicleExposureAutoNumSeq;
	}

	@Column(name = "HOPropertyDiscount_Ext")
	public Integer getHopropertyDiscountExt() {
		return this.hopropertyDiscountExt;
	}

	public void setHopropertyDiscountExt(Integer hopropertyDiscountExt) {
		this.hopropertyDiscountExt = hopropertyDiscountExt;
	}

}
