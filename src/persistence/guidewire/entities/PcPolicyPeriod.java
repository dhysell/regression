package persistence.guidewire.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Clob;

@Entity
@Table(name = "pc_policyperiod", schema = "dbo", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "PolicyID", "Retired", "ID" }),
		@UniqueConstraint(columnNames = "ArchiveFailureDetailsID"),
		@UniqueConstraint(columnNames = { "PublicID", "Retired" }),
		@UniqueConstraint(columnNames = { "ID", "PeriodStart", "PeriodEnd" }),
		@UniqueConstraint(columnNames = { "PolicyTermID", "Retired", "MostRecentModel", "TemporaryBranch", "PeriodEnd",
				"CancellationDate", "PolicyID", "ID" }),
		@UniqueConstraint(columnNames = { "PeriodID", "ModelNumber", "ID", "Retired" }),
		@UniqueConstraint(columnNames = { "PeriodID", "ModelNumberIndex", "Retired" }),
		@UniqueConstraint(columnNames = { "PeriodID", "MostRecentModelIndex", "Retired" }),
		@UniqueConstraint(columnNames = { "MostRecentModel", "PolicyID", "ID", "Retired", "ModelNumber",
				"TemporaryBranch" }),
		@UniqueConstraint(columnNames = { "ID", "Retired", "JobID", "PolicyID", "TemporaryBranch",
				"MostRecentModel" }) })
public class PcPolicyPeriod {

	private long id;
	private PcPolicy pcPolicy;
	private boolean allowGapsBefore;
	private Long locationAutoNumberSeq;
	private boolean mostRecentModel;
	private Long archiveFailureId;
	private long retired;
	private boolean validQuote;
	private String invoiceStreamCode;
	private Integer baseState;
	private Long minimumPremium;
	private BigDecimal billImmediatelyPercentage;
	private String cancellationDate;
	private Integer branchNumber;
	private Boolean resetDateQuoteNeededExt;
	private Integer billingMethod;
	private String basedOnDate;
	private String excludeReason;
	private BigDecimal depositOverridePct;
	private String duesCountyFbm;
	private String periodEnd;
	private Boolean importedFbm;
	private Integer beanVersion;
	private boolean futurePeriods;
	private Long archiveFailureDetailsId;
	private BigDecimal depositAmount;
	private String modelDate;
	private Boolean trnsfrrdAnthrPolicyExt;
	private Integer lockingColumn;
	private String primaryInsuredName;
	private String policyNumber;
	private Long basedOnId;
	private Boolean failedOoseevaluation;
	private Boolean waiveDepositChange;
	private Integer termNumberFbm;
	private boolean temporaryBranch;
	private BigDecimal transactionCostRpt;
	private boolean locked;
	private boolean quoteHidden;
	private String modelNumberIndex;
	private long policyTermId;
	private Long uwcompany;
	private long periodId;
	private Integer refundCalcMethod;
	private Boolean failedOosevalidation;
	private Integer modelNumber;
	private Long countyFbmid;
	private BigDecimal totalCostRpt;
	private String seriesCheckingPatternCode;
	private Integer allocationOfRemainder;
	private String publicId;
	private Long archivePartition;
	private Long archiveSchemaInfo;
	private Boolean transRenewal;
	private String branchName;
	private BigDecimal transactionPremiumRpt;
	private Long createUserId;
	private BigDecimal premiumStabilityFbm;
	private String editEffectiveDate;
	private Boolean assignedRisk;
	private BigDecimal totalMembershipAmtExt;
	private String writtenDate;
	private Integer segment;
	private String archiveDate;
	private Long officeFbmid;
	private Boolean migrateAgentOfService;
	private String trnsfrrdFromPrvPolicyExt;
	private BigDecimal depositCollected;
	private Long updateUserId;
	private Long pnicontactDenorm;
	private BigDecimal totalPremiumRpt;
	private Boolean claimCenterDownFlagFbm;
	private boolean preempted;
	private String updateTime;
	private Boolean overrideBillingAllocation;
	private boolean editLocked;
	private long producerCodeOfRecordId;
	private Boolean excludedFromArchive;
	private Long jobId;
	private int status;
	private Boolean validReinsurance;
	private String createTime;
	private String primaryInsuredNameDenorm;
	private Integer termNumber;
	private String mostRecentModelIndex;
	private String periodStart;
	private Integer archiveState;
	private String trnsfrrdAnthrPolicyDateExt;
	private String altBillingAccountNumber;
	private String rateAsOfDate;
	private String singleCheckingPatternCode;
	private Boolean outOfForceMoreThan6monthsFbm;
	private int preferredCoverageCurrency;
	private int preferredSettlementCurrency;
	private Integer depositCollectedCur;
	private Integer depositAmountCur;
	private Integer totalPremiumRptCur;
	private Integer totalCostRptCur;
	private Integer transactionPremiumRptCur;
	private Integer transactionCostRptCur;
	private boolean doNotPurge;
	private BigDecimal estimatedPremium;
	private Integer estimatedPremiumCur;
	private boolean customBilling;
	private Integer yearBusinessStartedFbm;
	private String busOpsDescFbm;
	private Boolean lineSelectionVisitedOnce;
	private Clob busOpsOtherOpsDescription;
	private Boolean locationScreenVisitedOnce;
	private Boolean convertedFromQuickModeFbm;
	private Boolean busOpsOtherOps;

	public PcPolicyPeriod() {
	}

	public PcPolicyPeriod(long id, PcPolicy pcPolicy, boolean allowGapsBefore, boolean mostRecentModel, long retired,
			boolean validQuote, String periodEnd, boolean futurePeriods, boolean temporaryBranch, boolean locked,
			boolean quoteHidden, String modelNumberIndex, long policyTermId, long periodId, String publicId,
			String editEffectiveDate, boolean preempted, String updateTime, boolean editLocked,
			long producerCodeOfRecordId, int status, String createTime, String mostRecentModelIndex, String periodStart,
			int preferredCoverageCurrency, int preferredSettlementCurrency, boolean doNotPurge, boolean customBilling) {
		this.id = id;
		this.pcPolicy = pcPolicy;
		this.allowGapsBefore = allowGapsBefore;
		this.mostRecentModel = mostRecentModel;
		this.retired = retired;
		this.validQuote = validQuote;
		this.periodEnd = periodEnd;
		this.futurePeriods = futurePeriods;
		this.temporaryBranch = temporaryBranch;
		this.locked = locked;
		this.quoteHidden = quoteHidden;
		this.modelNumberIndex = modelNumberIndex;
		this.policyTermId = policyTermId;
		this.periodId = periodId;
		this.publicId = publicId;
		this.editEffectiveDate = editEffectiveDate;
		this.preempted = preempted;
		this.updateTime = updateTime;
		this.editLocked = editLocked;
		this.producerCodeOfRecordId = producerCodeOfRecordId;
		this.status = status;
		this.createTime = createTime;
		this.mostRecentModelIndex = mostRecentModelIndex;
		this.periodStart = periodStart;
		this.preferredCoverageCurrency = preferredCoverageCurrency;
		this.preferredSettlementCurrency = preferredSettlementCurrency;
		this.doNotPurge = doNotPurge;
		this.customBilling = customBilling;
	}

	public PcPolicyPeriod(long id, PcPolicy pcPolicy, boolean allowGapsBefore, Long locationAutoNumberSeq,
			boolean mostRecentModel, Long archiveFailureId, long retired, boolean validQuote, String invoiceStreamCode,
			Integer baseState, Long minimumPremium, BigDecimal billImmediatelyPercentage, String cancellationDate,
			Integer branchNumber, Boolean resetDateQuoteNeededExt, Integer billingMethod, String basedOnDate,
			String excludeReason, BigDecimal depositOverridePct, String duesCountyFbm, String periodEnd,
			Boolean importedFbm, Integer beanVersion, boolean futurePeriods, Long archiveFailureDetailsId,
			BigDecimal depositAmount, String modelDate, Boolean trnsfrrdAnthrPolicyExt, Integer lockingColumn,
			String primaryInsuredName, String policyNumber, Long basedOnId, Boolean failedOoseevaluation,
			Boolean waiveDepositChange, Integer termNumberFbm, boolean temporaryBranch, BigDecimal transactionCostRpt,
			boolean locked, boolean quoteHidden, String modelNumberIndex, long policyTermId, Long uwcompany,
			long periodId, Integer refundCalcMethod, Boolean failedOosevalidation, Integer modelNumber,
			Long countyFbmid, BigDecimal totalCostRpt, String seriesCheckingPatternCode, Integer allocationOfRemainder,
			String publicId, Long archivePartition, Long archiveSchemaInfo, Boolean transRenewal, String branchName,
			BigDecimal transactionPremiumRpt, Long createUserId, BigDecimal premiumStabilityFbm,
			String editEffectiveDate, Boolean assignedRisk, BigDecimal totalMembershipAmtExt, String writtenDate,
			Integer segment, String archiveDate, Long officeFbmid, Boolean migrateAgentOfService,
			String trnsfrrdFromPrvPolicyExt, BigDecimal depositCollected, Long updateUserId, Long pnicontactDenorm,
			BigDecimal totalPremiumRpt, Boolean claimCenterDownFlagFbm, boolean preempted, String updateTime,
			Boolean overrideBillingAllocation, boolean editLocked, long producerCodeOfRecordId,
			Boolean excludedFromArchive, Long jobId, int status, Boolean validReinsurance, String createTime,
			String primaryInsuredNameDenorm, Integer termNumber, String mostRecentModelIndex, String periodStart,
			Integer archiveState, String trnsfrrdAnthrPolicyDateExt, String altBillingAccountNumber,
			String rateAsOfDate, String singleCheckingPatternCode, Boolean outOfForceMoreThan6monthsFbm,
			int preferredCoverageCurrency, int preferredSettlementCurrency, Integer depositCollectedCur,
			Integer depositAmountCur, Integer totalPremiumRptCur, Integer totalCostRptCur,
			Integer transactionPremiumRptCur, Integer transactionCostRptCur, boolean doNotPurge,
			BigDecimal estimatedPremium, Integer estimatedPremiumCur, boolean customBilling,
			Integer yearBusinessStartedFbm, String busOpsDescFbm, Boolean lineSelectionVisitedOnce,
			Clob busOpsOtherOpsDescription, Boolean locationScreenVisitedOnce, Boolean convertedFromQuickModeFbm,
			Boolean busOpsOtherOps) {
		this.id = id;
		this.pcPolicy = pcPolicy;
		this.allowGapsBefore = allowGapsBefore;
		this.locationAutoNumberSeq = locationAutoNumberSeq;
		this.mostRecentModel = mostRecentModel;
		this.archiveFailureId = archiveFailureId;
		this.retired = retired;
		this.validQuote = validQuote;
		this.invoiceStreamCode = invoiceStreamCode;
		this.baseState = baseState;
		this.minimumPremium = minimumPremium;
		this.billImmediatelyPercentage = billImmediatelyPercentage;
		this.cancellationDate = cancellationDate;
		this.branchNumber = branchNumber;
		this.resetDateQuoteNeededExt = resetDateQuoteNeededExt;
		this.billingMethod = billingMethod;
		this.basedOnDate = basedOnDate;
		this.excludeReason = excludeReason;
		this.depositOverridePct = depositOverridePct;
		this.duesCountyFbm = duesCountyFbm;
		this.periodEnd = periodEnd;
		this.importedFbm = importedFbm;
		this.beanVersion = beanVersion;
		this.futurePeriods = futurePeriods;
		this.archiveFailureDetailsId = archiveFailureDetailsId;
		this.depositAmount = depositAmount;
		this.modelDate = modelDate;
		this.trnsfrrdAnthrPolicyExt = trnsfrrdAnthrPolicyExt;
		this.lockingColumn = lockingColumn;
		this.primaryInsuredName = primaryInsuredName;
		this.policyNumber = policyNumber;
		this.basedOnId = basedOnId;
		this.failedOoseevaluation = failedOoseevaluation;
		this.waiveDepositChange = waiveDepositChange;
		this.termNumberFbm = termNumberFbm;
		this.temporaryBranch = temporaryBranch;
		this.transactionCostRpt = transactionCostRpt;
		this.locked = locked;
		this.quoteHidden = quoteHidden;
		this.modelNumberIndex = modelNumberIndex;
		this.policyTermId = policyTermId;
		this.uwcompany = uwcompany;
		this.periodId = periodId;
		this.refundCalcMethod = refundCalcMethod;
		this.failedOosevalidation = failedOosevalidation;
		this.modelNumber = modelNumber;
		this.countyFbmid = countyFbmid;
		this.totalCostRpt = totalCostRpt;
		this.seriesCheckingPatternCode = seriesCheckingPatternCode;
		this.allocationOfRemainder = allocationOfRemainder;
		this.publicId = publicId;
		this.archivePartition = archivePartition;
		this.archiveSchemaInfo = archiveSchemaInfo;
		this.transRenewal = transRenewal;
		this.branchName = branchName;
		this.transactionPremiumRpt = transactionPremiumRpt;
		this.createUserId = createUserId;
		this.premiumStabilityFbm = premiumStabilityFbm;
		this.editEffectiveDate = editEffectiveDate;
		this.assignedRisk = assignedRisk;
		this.totalMembershipAmtExt = totalMembershipAmtExt;
		this.writtenDate = writtenDate;
		this.segment = segment;
		this.archiveDate = archiveDate;
		this.officeFbmid = officeFbmid;
		this.migrateAgentOfService = migrateAgentOfService;
		this.trnsfrrdFromPrvPolicyExt = trnsfrrdFromPrvPolicyExt;
		this.depositCollected = depositCollected;
		this.updateUserId = updateUserId;
		this.pnicontactDenorm = pnicontactDenorm;
		this.totalPremiumRpt = totalPremiumRpt;
		this.claimCenterDownFlagFbm = claimCenterDownFlagFbm;
		this.preempted = preempted;
		this.updateTime = updateTime;
		this.overrideBillingAllocation = overrideBillingAllocation;
		this.editLocked = editLocked;
		this.producerCodeOfRecordId = producerCodeOfRecordId;
		this.excludedFromArchive = excludedFromArchive;
		this.jobId = jobId;
		this.status = status;
		this.validReinsurance = validReinsurance;
		this.createTime = createTime;
		this.primaryInsuredNameDenorm = primaryInsuredNameDenorm;
		this.termNumber = termNumber;
		this.mostRecentModelIndex = mostRecentModelIndex;
		this.periodStart = periodStart;
		this.archiveState = archiveState;
		this.trnsfrrdAnthrPolicyDateExt = trnsfrrdAnthrPolicyDateExt;
		this.altBillingAccountNumber = altBillingAccountNumber;
		this.rateAsOfDate = rateAsOfDate;
		this.singleCheckingPatternCode = singleCheckingPatternCode;
		this.outOfForceMoreThan6monthsFbm = outOfForceMoreThan6monthsFbm;
		this.preferredCoverageCurrency = preferredCoverageCurrency;
		this.preferredSettlementCurrency = preferredSettlementCurrency;
		this.depositCollectedCur = depositCollectedCur;
		this.depositAmountCur = depositAmountCur;
		this.totalPremiumRptCur = totalPremiumRptCur;
		this.totalCostRptCur = totalCostRptCur;
		this.transactionPremiumRptCur = transactionPremiumRptCur;
		this.transactionCostRptCur = transactionCostRptCur;
		this.doNotPurge = doNotPurge;
		this.estimatedPremium = estimatedPremium;
		this.estimatedPremiumCur = estimatedPremiumCur;
		this.customBilling = customBilling;
		this.yearBusinessStartedFbm = yearBusinessStartedFbm;
		this.busOpsDescFbm = busOpsDescFbm;
		this.lineSelectionVisitedOnce = lineSelectionVisitedOnce;
		this.busOpsOtherOpsDescription = busOpsOtherOpsDescription;
		this.locationScreenVisitedOnce = locationScreenVisitedOnce;
		this.convertedFromQuickModeFbm = convertedFromQuickModeFbm;
		this.busOpsOtherOps = busOpsOtherOps;
	}

	@Id

	@Column(name = "ID", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PolicyID", nullable = false)
	public PcPolicy getPcPolicy() {
		return this.pcPolicy;
	}

	public void setPcPolicy(PcPolicy pcPolicy) {
		this.pcPolicy = pcPolicy;
	}

	@Column(name = "AllowGapsBefore", nullable = false)
	public boolean isAllowGapsBefore() {
		return this.allowGapsBefore;
	}

	public void setAllowGapsBefore(boolean allowGapsBefore) {
		this.allowGapsBefore = allowGapsBefore;
	}

	@Column(name = "LocationAutoNumberSeq")
	public Long getLocationAutoNumberSeq() {
		return this.locationAutoNumberSeq;
	}

	public void setLocationAutoNumberSeq(Long locationAutoNumberSeq) {
		this.locationAutoNumberSeq = locationAutoNumberSeq;
	}

	@Column(name = "MostRecentModel", nullable = false)
	public boolean isMostRecentModel() {
		return this.mostRecentModel;
	}

	public void setMostRecentModel(boolean mostRecentModel) {
		this.mostRecentModel = mostRecentModel;
	}

	@Column(name = "ArchiveFailureID")
	public Long getArchiveFailureId() {
		return this.archiveFailureId;
	}

	public void setArchiveFailureId(Long archiveFailureId) {
		this.archiveFailureId = archiveFailureId;
	}

	@Column(name = "Retired", nullable = false)
	public long getRetired() {
		return this.retired;
	}

	public void setRetired(long retired) {
		this.retired = retired;
	}

	@Column(name = "ValidQuote", nullable = false)
	public boolean isValidQuote() {
		return this.validQuote;
	}

	public void setValidQuote(boolean validQuote) {
		this.validQuote = validQuote;
	}

	@Column(name = "InvoiceStreamCode")
	public String getInvoiceStreamCode() {
		return this.invoiceStreamCode;
	}

	public void setInvoiceStreamCode(String invoiceStreamCode) {
		this.invoiceStreamCode = invoiceStreamCode;
	}

	@Column(name = "BaseState")
	public Integer getBaseState() {
		return this.baseState;
	}

	public void setBaseState(Integer baseState) {
		this.baseState = baseState;
	}

	@Column(name = "MinimumPremium")
	public Long getMinimumPremium() {
		return this.minimumPremium;
	}

	public void setMinimumPremium(Long minimumPremium) {
		this.minimumPremium = minimumPremium;
	}

	@Column(name = "BillImmediatelyPercentage", precision = 4, scale = 1)
	public BigDecimal getBillImmediatelyPercentage() {
		return this.billImmediatelyPercentage;
	}

	public void setBillImmediatelyPercentage(BigDecimal billImmediatelyPercentage) {
		this.billImmediatelyPercentage = billImmediatelyPercentage;
	}

	@Column(name = "CancellationDate", length = 27)
	public String getCancellationDate() {
		return this.cancellationDate;
	}

	public void setCancellationDate(String cancellationDate) {
		this.cancellationDate = cancellationDate;
	}

	@Column(name = "BranchNumber")
	public Integer getBranchNumber() {
		return this.branchNumber;
	}

	public void setBranchNumber(Integer branchNumber) {
		this.branchNumber = branchNumber;
	}

	@Column(name = "ResetDateQuoteNeededExt")
	public Boolean getResetDateQuoteNeededExt() {
		return this.resetDateQuoteNeededExt;
	}

	public void setResetDateQuoteNeededExt(Boolean resetDateQuoteNeededExt) {
		this.resetDateQuoteNeededExt = resetDateQuoteNeededExt;
	}

	@Column(name = "BillingMethod")
	public Integer getBillingMethod() {
		return this.billingMethod;
	}

	public void setBillingMethod(Integer billingMethod) {
		this.billingMethod = billingMethod;
	}

	@Column(name = "BasedOnDate", length = 27)
	public String getBasedOnDate() {
		return this.basedOnDate;
	}

	public void setBasedOnDate(String basedOnDate) {
		this.basedOnDate = basedOnDate;
	}

	@Column(name = "ExcludeReason")
	public String getExcludeReason() {
		return this.excludeReason;
	}

	public void setExcludeReason(String excludeReason) {
		this.excludeReason = excludeReason;
	}

	@Column(name = "DepositOverridePct", precision = 12, scale = 3)
	public BigDecimal getDepositOverridePct() {
		return this.depositOverridePct;
	}

	public void setDepositOverridePct(BigDecimal depositOverridePct) {
		this.depositOverridePct = depositOverridePct;
	}

	@Column(name = "DuesCounty_FBM")
	public String getDuesCountyFbm() {
		return this.duesCountyFbm;
	}

	public void setDuesCountyFbm(String duesCountyFbm) {
		this.duesCountyFbm = duesCountyFbm;
	}

	@Column(name = "PeriodEnd", nullable = false, length = 27)
	public String getPeriodEnd() {
		return this.periodEnd;
	}

	public void setPeriodEnd(String periodEnd) {
		this.periodEnd = periodEnd;
	}

	@Column(name = "Imported_FBM")
	public Boolean getImportedFbm() {
		return this.importedFbm;
	}

	public void setImportedFbm(Boolean importedFbm) {
		this.importedFbm = importedFbm;
	}

	@Column(name = "BeanVersion")
	public Integer getBeanVersion() {
		return this.beanVersion;
	}

	public void setBeanVersion(Integer beanVersion) {
		this.beanVersion = beanVersion;
	}

	@Column(name = "FuturePeriods", nullable = false)
	public boolean isFuturePeriods() {
		return this.futurePeriods;
	}

	public void setFuturePeriods(boolean futurePeriods) {
		this.futurePeriods = futurePeriods;
	}

	@Column(name = "ArchiveFailureDetailsID", unique = true)
	public Long getArchiveFailureDetailsId() {
		return this.archiveFailureDetailsId;
	}

	public void setArchiveFailureDetailsId(Long archiveFailureDetailsId) {
		this.archiveFailureDetailsId = archiveFailureDetailsId;
	}

	@Column(name = "DepositAmount", precision = 18)
	public BigDecimal getDepositAmount() {
		return this.depositAmount;
	}

	public void setDepositAmount(BigDecimal depositAmount) {
		this.depositAmount = depositAmount;
	}

	@Column(name = "ModelDate", length = 27)
	public String getModelDate() {
		return this.modelDate;
	}

	public void setModelDate(String modelDate) {
		this.modelDate = modelDate;
	}

	@Column(name = "TrnsfrrdAnthrPolicyExt")
	public Boolean getTrnsfrrdAnthrPolicyExt() {
		return this.trnsfrrdAnthrPolicyExt;
	}

	public void setTrnsfrrdAnthrPolicyExt(Boolean trnsfrrdAnthrPolicyExt) {
		this.trnsfrrdAnthrPolicyExt = trnsfrrdAnthrPolicyExt;
	}

	@Column(name = "LockingColumn")
	public Integer getLockingColumn() {
		return this.lockingColumn;
	}

	public void setLockingColumn(Integer lockingColumn) {
		this.lockingColumn = lockingColumn;
	}

	@Column(name = "PrimaryInsuredName")
	public String getPrimaryInsuredName() {
		return this.primaryInsuredName;
	}

	public void setPrimaryInsuredName(String primaryInsuredName) {
		this.primaryInsuredName = primaryInsuredName;
	}

	@Column(name = "PolicyNumber", length = 40)
	public String getPolicyNumber() {
		return this.policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	@Column(name = "BasedOnID")
	public Long getBasedOnId() {
		return this.basedOnId;
	}

	public void setBasedOnId(Long basedOnId) {
		this.basedOnId = basedOnId;
	}

	@Column(name = "FailedOOSEEvaluation")
	public Boolean getFailedOoseevaluation() {
		return this.failedOoseevaluation;
	}

	public void setFailedOoseevaluation(Boolean failedOoseevaluation) {
		this.failedOoseevaluation = failedOoseevaluation;
	}

	@Column(name = "WaiveDepositChange")
	public Boolean getWaiveDepositChange() {
		return this.waiveDepositChange;
	}

	public void setWaiveDepositChange(Boolean waiveDepositChange) {
		this.waiveDepositChange = waiveDepositChange;
	}

	@Column(name = "TermNumber_FBM")
	public Integer getTermNumberFbm() {
		return this.termNumberFbm;
	}

	public void setTermNumberFbm(Integer termNumberFbm) {
		this.termNumberFbm = termNumberFbm;
	}

	@Column(name = "TemporaryBranch", nullable = false)
	public boolean isTemporaryBranch() {
		return this.temporaryBranch;
	}

	public void setTemporaryBranch(boolean temporaryBranch) {
		this.temporaryBranch = temporaryBranch;
	}

	@Column(name = "TransactionCostRPT", precision = 18)
	public BigDecimal getTransactionCostRpt() {
		return this.transactionCostRpt;
	}

	public void setTransactionCostRpt(BigDecimal transactionCostRpt) {
		this.transactionCostRpt = transactionCostRpt;
	}

	@Column(name = "Locked", nullable = false)
	public boolean isLocked() {
		return this.locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	@Column(name = "QuoteHidden", nullable = false)
	public boolean isQuoteHidden() {
		return this.quoteHidden;
	}

	public void setQuoteHidden(boolean quoteHidden) {
		this.quoteHidden = quoteHidden;
	}

	@Column(name = "ModelNumberIndex", nullable = false, length = 64)
	public String getModelNumberIndex() {
		return this.modelNumberIndex;
	}

	public void setModelNumberIndex(String modelNumberIndex) {
		this.modelNumberIndex = modelNumberIndex;
	}

	@Column(name = "PolicyTermID", nullable = false)
	public long getPolicyTermId() {
		return this.policyTermId;
	}

	public void setPolicyTermId(long policyTermId) {
		this.policyTermId = policyTermId;
	}

	@Column(name = "UWCompany")
	public Long getUwcompany() {
		return this.uwcompany;
	}

	public void setUwcompany(Long uwcompany) {
		this.uwcompany = uwcompany;
	}

	@Column(name = "PeriodID", nullable = false)
	public long getPeriodId() {
		return this.periodId;
	}

	public void setPeriodId(long periodId) {
		this.periodId = periodId;
	}

	@Column(name = "RefundCalcMethod")
	public Integer getRefundCalcMethod() {
		return this.refundCalcMethod;
	}

	public void setRefundCalcMethod(Integer refundCalcMethod) {
		this.refundCalcMethod = refundCalcMethod;
	}

	@Column(name = "FailedOOSEValidation")
	public Boolean getFailedOosevalidation() {
		return this.failedOosevalidation;
	}

	public void setFailedOosevalidation(Boolean failedOosevalidation) {
		this.failedOosevalidation = failedOosevalidation;
	}

	@Column(name = "ModelNumber")
	public Integer getModelNumber() {
		return this.modelNumber;
	}

	public void setModelNumber(Integer modelNumber) {
		this.modelNumber = modelNumber;
	}

	@Column(name = "County_FBMID")
	public Long getCountyFbmid() {
		return this.countyFbmid;
	}

	public void setCountyFbmid(Long countyFbmid) {
		this.countyFbmid = countyFbmid;
	}

	@Column(name = "TotalCostRPT", precision = 18)
	public BigDecimal getTotalCostRpt() {
		return this.totalCostRpt;
	}

	public void setTotalCostRpt(BigDecimal totalCostRpt) {
		this.totalCostRpt = totalCostRpt;
	}

	@Column(name = "SeriesCheckingPatternCode", length = 64)
	public String getSeriesCheckingPatternCode() {
		return this.seriesCheckingPatternCode;
	}

	public void setSeriesCheckingPatternCode(String seriesCheckingPatternCode) {
		this.seriesCheckingPatternCode = seriesCheckingPatternCode;
	}

	@Column(name = "AllocationOfRemainder")
	public Integer getAllocationOfRemainder() {
		return this.allocationOfRemainder;
	}

	public void setAllocationOfRemainder(Integer allocationOfRemainder) {
		this.allocationOfRemainder = allocationOfRemainder;
	}

	@Column(name = "PublicID", nullable = false, length = 64)
	public String getPublicId() {
		return this.publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	@Column(name = "ArchivePartition")
	public Long getArchivePartition() {
		return this.archivePartition;
	}

	public void setArchivePartition(Long archivePartition) {
		this.archivePartition = archivePartition;
	}

	@Column(name = "ArchiveSchemaInfo")
	public Long getArchiveSchemaInfo() {
		return this.archiveSchemaInfo;
	}

	public void setArchiveSchemaInfo(Long archiveSchemaInfo) {
		this.archiveSchemaInfo = archiveSchemaInfo;
	}

	@Column(name = "TransRenewal")
	public Boolean getTransRenewal() {
		return this.transRenewal;
	}

	public void setTransRenewal(Boolean transRenewal) {
		this.transRenewal = transRenewal;
	}

	@Column(name = "BranchName")
	public String getBranchName() {
		return this.branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	@Column(name = "TransactionPremiumRPT", precision = 18)
	public BigDecimal getTransactionPremiumRpt() {
		return this.transactionPremiumRpt;
	}

	public void setTransactionPremiumRpt(BigDecimal transactionPremiumRpt) {
		this.transactionPremiumRpt = transactionPremiumRpt;
	}

	@Column(name = "CreateUserID")
	public Long getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "PremiumStability_FBM", precision = 4, scale = 3)
	public BigDecimal getPremiumStabilityFbm() {
		return this.premiumStabilityFbm;
	}

	public void setPremiumStabilityFbm(BigDecimal premiumStabilityFbm) {
		this.premiumStabilityFbm = premiumStabilityFbm;
	}

	@Column(name = "EditEffectiveDate", nullable = false, length = 27)
	public String getEditEffectiveDate() {
		return this.editEffectiveDate;
	}

	public void setEditEffectiveDate(String editEffectiveDate) {
		this.editEffectiveDate = editEffectiveDate;
	}

	@Column(name = "AssignedRisk")
	public Boolean getAssignedRisk() {
		return this.assignedRisk;
	}

	public void setAssignedRisk(Boolean assignedRisk) {
		this.assignedRisk = assignedRisk;
	}

	@Column(name = "TotalMembershipAmtExt", precision = 6)
	public BigDecimal getTotalMembershipAmtExt() {
		return this.totalMembershipAmtExt;
	}

	public void setTotalMembershipAmtExt(BigDecimal totalMembershipAmtExt) {
		this.totalMembershipAmtExt = totalMembershipAmtExt;
	}

	@Column(name = "WrittenDate", length = 27)
	public String getWrittenDate() {
		return this.writtenDate;
	}

	public void setWrittenDate(String writtenDate) {
		this.writtenDate = writtenDate;
	}

	@Column(name = "Segment")
	public Integer getSegment() {
		return this.segment;
	}

	public void setSegment(Integer segment) {
		this.segment = segment;
	}

	@Column(name = "ArchiveDate", length = 27)
	public String getArchiveDate() {
		return this.archiveDate;
	}

	public void setArchiveDate(String archiveDate) {
		this.archiveDate = archiveDate;
	}

	@Column(name = "Office_FBMID")
	public Long getOfficeFbmid() {
		return this.officeFbmid;
	}

	public void setOfficeFbmid(Long officeFbmid) {
		this.officeFbmid = officeFbmid;
	}

	@Column(name = "MigrateAgentOfService")
	public Boolean getMigrateAgentOfService() {
		return this.migrateAgentOfService;
	}

	public void setMigrateAgentOfService(Boolean migrateAgentOfService) {
		this.migrateAgentOfService = migrateAgentOfService;
	}

	@Column(name = "TrnsfrrdFromPrvPolicyExt", length = 30)
	public String getTrnsfrrdFromPrvPolicyExt() {
		return this.trnsfrrdFromPrvPolicyExt;
	}

	public void setTrnsfrrdFromPrvPolicyExt(String trnsfrrdFromPrvPolicyExt) {
		this.trnsfrrdFromPrvPolicyExt = trnsfrrdFromPrvPolicyExt;
	}

	@Column(name = "DepositCollected", precision = 18)
	public BigDecimal getDepositCollected() {
		return this.depositCollected;
	}

	public void setDepositCollected(BigDecimal depositCollected) {
		this.depositCollected = depositCollected;
	}

	@Column(name = "UpdateUserID")
	public Long getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Column(name = "PNIContactDenorm")
	public Long getPnicontactDenorm() {
		return this.pnicontactDenorm;
	}

	public void setPnicontactDenorm(Long pnicontactDenorm) {
		this.pnicontactDenorm = pnicontactDenorm;
	}

	@Column(name = "TotalPremiumRPT", precision = 18)
	public BigDecimal getTotalPremiumRpt() {
		return this.totalPremiumRpt;
	}

	public void setTotalPremiumRpt(BigDecimal totalPremiumRpt) {
		this.totalPremiumRpt = totalPremiumRpt;
	}

	@Column(name = "ClaimCenterDownFlag_FBM")
	public Boolean getClaimCenterDownFlagFbm() {
		return this.claimCenterDownFlagFbm;
	}

	public void setClaimCenterDownFlagFbm(Boolean claimCenterDownFlagFbm) {
		this.claimCenterDownFlagFbm = claimCenterDownFlagFbm;
	}

	@Column(name = "Preempted", nullable = false)
	public boolean isPreempted() {
		return this.preempted;
	}

	public void setPreempted(boolean preempted) {
		this.preempted = preempted;
	}

	@Column(name = "UpdateTime", nullable = false, length = 27)
	public String getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "OverrideBillingAllocation")
	public Boolean getOverrideBillingAllocation() {
		return this.overrideBillingAllocation;
	}

	public void setOverrideBillingAllocation(Boolean overrideBillingAllocation) {
		this.overrideBillingAllocation = overrideBillingAllocation;
	}

	@Column(name = "EditLocked", nullable = false)
	public boolean isEditLocked() {
		return this.editLocked;
	}

	public void setEditLocked(boolean editLocked) {
		this.editLocked = editLocked;
	}

	@Column(name = "ProducerCodeOfRecordID", nullable = false)
	public long getProducerCodeOfRecordId() {
		return this.producerCodeOfRecordId;
	}

	public void setProducerCodeOfRecordId(long producerCodeOfRecordId) {
		this.producerCodeOfRecordId = producerCodeOfRecordId;
	}

	@Column(name = "ExcludedFromArchive")
	public Boolean getExcludedFromArchive() {
		return this.excludedFromArchive;
	}

	public void setExcludedFromArchive(Boolean excludedFromArchive) {
		this.excludedFromArchive = excludedFromArchive;
	}

	@Column(name = "JobID")
	public Long getJobId() {
		return this.jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	@Column(name = "Status", nullable = false)
	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name = "ValidReinsurance")
	public Boolean getValidReinsurance() {
		return this.validReinsurance;
	}

	public void setValidReinsurance(Boolean validReinsurance) {
		this.validReinsurance = validReinsurance;
	}

	@Column(name = "CreateTime", nullable = false, length = 27)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "PrimaryInsuredNameDenorm")
	public String getPrimaryInsuredNameDenorm() {
		return this.primaryInsuredNameDenorm;
	}

	public void setPrimaryInsuredNameDenorm(String primaryInsuredNameDenorm) {
		this.primaryInsuredNameDenorm = primaryInsuredNameDenorm;
	}

	@Column(name = "TermNumber")
	public Integer getTermNumber() {
		return this.termNumber;
	}

	public void setTermNumber(Integer termNumber) {
		this.termNumber = termNumber;
	}

	@Column(name = "MostRecentModelIndex", nullable = false, length = 64)
	public String getMostRecentModelIndex() {
		return this.mostRecentModelIndex;
	}

	public void setMostRecentModelIndex(String mostRecentModelIndex) {
		this.mostRecentModelIndex = mostRecentModelIndex;
	}

	@Column(name = "PeriodStart", nullable = false, length = 27)
	public String getPeriodStart() {
		return this.periodStart;
	}

	public void setPeriodStart(String periodStart) {
		this.periodStart = periodStart;
	}

	@Column(name = "ArchiveState")
	public Integer getArchiveState() {
		return this.archiveState;
	}

	public void setArchiveState(Integer archiveState) {
		this.archiveState = archiveState;
	}

	@Column(name = "TrnsfrrdAnthrPolicyDateExt", length = 27)
	public String getTrnsfrrdAnthrPolicyDateExt() {
		return this.trnsfrrdAnthrPolicyDateExt;
	}

	public void setTrnsfrrdAnthrPolicyDateExt(String trnsfrrdAnthrPolicyDateExt) {
		this.trnsfrrdAnthrPolicyDateExt = trnsfrrdAnthrPolicyDateExt;
	}

	@Column(name = "AltBillingAccountNumber")
	public String getAltBillingAccountNumber() {
		return this.altBillingAccountNumber;
	}

	public void setAltBillingAccountNumber(String altBillingAccountNumber) {
		this.altBillingAccountNumber = altBillingAccountNumber;
	}

	@Column(name = "RateAsOfDate", length = 27)
	public String getRateAsOfDate() {
		return this.rateAsOfDate;
	}

	public void setRateAsOfDate(String rateAsOfDate) {
		this.rateAsOfDate = rateAsOfDate;
	}

	@Column(name = "SingleCheckingPatternCode", length = 64)
	public String getSingleCheckingPatternCode() {
		return this.singleCheckingPatternCode;
	}

	public void setSingleCheckingPatternCode(String singleCheckingPatternCode) {
		this.singleCheckingPatternCode = singleCheckingPatternCode;
	}

	@Column(name = "outOfForceMoreThan6Months_FBM")
	public Boolean getOutOfForceMoreThan6monthsFbm() {
		return this.outOfForceMoreThan6monthsFbm;
	}

	public void setOutOfForceMoreThan6monthsFbm(Boolean outOfForceMoreThan6monthsFbm) {
		this.outOfForceMoreThan6monthsFbm = outOfForceMoreThan6monthsFbm;
	}

	@Column(name = "PreferredCoverageCurrency", nullable = false)
	public int getPreferredCoverageCurrency() {
		return this.preferredCoverageCurrency;
	}

	public void setPreferredCoverageCurrency(int preferredCoverageCurrency) {
		this.preferredCoverageCurrency = preferredCoverageCurrency;
	}

	@Column(name = "PreferredSettlementCurrency", nullable = false)
	public int getPreferredSettlementCurrency() {
		return this.preferredSettlementCurrency;
	}

	public void setPreferredSettlementCurrency(int preferredSettlementCurrency) {
		this.preferredSettlementCurrency = preferredSettlementCurrency;
	}

	@Column(name = "DepositCollected_cur")
	public Integer getDepositCollectedCur() {
		return this.depositCollectedCur;
	}

	public void setDepositCollectedCur(Integer depositCollectedCur) {
		this.depositCollectedCur = depositCollectedCur;
	}

	@Column(name = "DepositAmount_cur")
	public Integer getDepositAmountCur() {
		return this.depositAmountCur;
	}

	public void setDepositAmountCur(Integer depositAmountCur) {
		this.depositAmountCur = depositAmountCur;
	}

	@Column(name = "TotalPremiumRPT_cur")
	public Integer getTotalPremiumRptCur() {
		return this.totalPremiumRptCur;
	}

	public void setTotalPremiumRptCur(Integer totalPremiumRptCur) {
		this.totalPremiumRptCur = totalPremiumRptCur;
	}

	@Column(name = "TotalCostRPT_cur")
	public Integer getTotalCostRptCur() {
		return this.totalCostRptCur;
	}

	public void setTotalCostRptCur(Integer totalCostRptCur) {
		this.totalCostRptCur = totalCostRptCur;
	}

	@Column(name = "TransactionPremiumRPT_cur")
	public Integer getTransactionPremiumRptCur() {
		return this.transactionPremiumRptCur;
	}

	public void setTransactionPremiumRptCur(Integer transactionPremiumRptCur) {
		this.transactionPremiumRptCur = transactionPremiumRptCur;
	}

	@Column(name = "TransactionCostRPT_cur")
	public Integer getTransactionCostRptCur() {
		return this.transactionCostRptCur;
	}

	public void setTransactionCostRptCur(Integer transactionCostRptCur) {
		this.transactionCostRptCur = transactionCostRptCur;
	}

	@Column(name = "DoNotPurge", nullable = false)
	public boolean isDoNotPurge() {
		return this.doNotPurge;
	}

	public void setDoNotPurge(boolean doNotPurge) {
		this.doNotPurge = doNotPurge;
	}

	@Column(name = "EstimatedPremium", precision = 18)
	public BigDecimal getEstimatedPremium() {
		return this.estimatedPremium;
	}

	public void setEstimatedPremium(BigDecimal estimatedPremium) {
		this.estimatedPremium = estimatedPremium;
	}

	@Column(name = "EstimatedPremium_cur")
	public Integer getEstimatedPremiumCur() {
		return this.estimatedPremiumCur;
	}

	public void setEstimatedPremiumCur(Integer estimatedPremiumCur) {
		this.estimatedPremiumCur = estimatedPremiumCur;
	}

	@Column(name = "CustomBilling", nullable = false)
	public boolean isCustomBilling() {
		return this.customBilling;
	}

	public void setCustomBilling(boolean customBilling) {
		this.customBilling = customBilling;
	}

	@Column(name = "YearBusinessStarted_FBM")
	public Integer getYearBusinessStartedFbm() {
		return this.yearBusinessStartedFbm;
	}

	public void setYearBusinessStartedFbm(Integer yearBusinessStartedFbm) {
		this.yearBusinessStartedFbm = yearBusinessStartedFbm;
	}

	@Column(name = "BusOpsDesc_FBM", length = 240)
	public String getBusOpsDescFbm() {
		return this.busOpsDescFbm;
	}

	public void setBusOpsDescFbm(String busOpsDescFbm) {
		this.busOpsDescFbm = busOpsDescFbm;
	}

	@Column(name = "LineSelectionVisitedOnce")
	public Boolean getLineSelectionVisitedOnce() {
		return this.lineSelectionVisitedOnce;
	}

	public void setLineSelectionVisitedOnce(Boolean lineSelectionVisitedOnce) {
		this.lineSelectionVisitedOnce = lineSelectionVisitedOnce;
	}

	@Column(name = "BusOpsOtherOpsDescription")
	public Clob getBusOpsOtherOpsDescription() {
		return this.busOpsOtherOpsDescription;
	}

	public void setBusOpsOtherOpsDescription(Clob busOpsOtherOpsDescription) {
		this.busOpsOtherOpsDescription = busOpsOtherOpsDescription;
	}

	@Column(name = "LocationScreenVisitedOnce")
	public Boolean getLocationScreenVisitedOnce() {
		return this.locationScreenVisitedOnce;
	}

	public void setLocationScreenVisitedOnce(Boolean locationScreenVisitedOnce) {
		this.locationScreenVisitedOnce = locationScreenVisitedOnce;
	}

	@Column(name = "ConvertedFromQuickMode_FBM")
	public Boolean getConvertedFromQuickModeFbm() {
		return this.convertedFromQuickModeFbm;
	}

	public void setConvertedFromQuickModeFbm(Boolean convertedFromQuickModeFbm) {
		this.convertedFromQuickModeFbm = convertedFromQuickModeFbm;
	}

	@Column(name = "BusOpsOtherOps")
	public Boolean getBusOpsOtherOps() {
		return this.busOpsOtherOps;
	}

	public void setBusOpsOtherOps(Boolean busOpsOtherOps) {
		this.busOpsOtherOps = busOpsOtherOps;
	}

}
