package persistence.guidewire.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pc_policy", schema = "dbo", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "PublicID", "Retired" }),
		@UniqueConstraint(columnNames = "ArchiveFailureDetailsID"),
		@UniqueConstraint(columnNames = { "ProductCode", "ID" }) })
public class PcPolicy {

	private long id;
	private String issueDate;
	private long retired;
	private Boolean infobarDonotReinsRewriteFbm;
	private Boolean infobarUmbrellaFbm;
	private long accountId;
	private String publicId;
	private Long newProducerCodeExt;
	private Boolean infobarCollectionFbm;
	private Integer packageRisk;
	private Long createUserId;
	private BigDecimal priorTotalIncurred;
	private Boolean infobarCargoFbm;
	private Boolean infobarPendingCancelFbm;
	private Integer beanVersion;
	private long producerCodeOfServiceId;
	private Integer primaryLanguage;
	private Long updateUserId;
	private Integer numPriorLosses;
	private Boolean infobarHandRateFbm;
	private String busOpsDescFbm;
	private Boolean infobarFormEFbm;
	private Boolean infobarSpecTentRateFbm;
	private Boolean infobarTreatyExceptionFbm;
	private String productCode;
	private int lossHistoryType;
	private Boolean infobarConstructionFbm;
	private String updateTime;
	private Long movedPolicySourceAccountId;
	private boolean doNotArchive;
	private Boolean infobarCashOnlyFbm;
	private String createTime;
	private Boolean commissionHoldFbm;
	private Boolean infobarSr22Fbm;
	private Boolean infobarReturnedMailFbm;
	private String inceptionDateFbm;
	private Integer yearBusinessStartedFbm;
	private String originalEffectiveDate;
	private Integer priorTotalIncurredCur;
	private Integer archiveState;
	private Long archiveSchemaInfo;
	private String excludeReason;
	private Long archiveFailureId;
	private boolean doNotPurge;
	private BigDecimal priorPremiums;
	private Integer priorPremiumsCur;
	private Boolean excludedFromArchive;
	private Long archiveFailureDetailsId;
	private Integer primaryLocale;
	private Long archivePartition;
	private String archiveDate;
	private Boolean infobarPicturesInDisrFbm;
	private String renewalReviewDate;
	private Long previousProducerCodeOfService;
	private Integer organizationType;
	private Boolean infobarCertificateFbm;
	private Long lineInceptionDatesFbm;
	private Set<PcPolicyPeriod> pcPolicyperiods = new HashSet<PcPolicyPeriod>(0);

	public PcPolicy() {
	}

	public PcPolicy(long id, long retired, long accountId, String publicId, long producerCodeOfServiceId,
			String productCode, int lossHistoryType, String updateTime, boolean doNotArchive, String createTime,
			boolean doNotPurge) {
		this.id = id;
		this.retired = retired;
		this.accountId = accountId;
		this.publicId = publicId;
		this.producerCodeOfServiceId = producerCodeOfServiceId;
		this.productCode = productCode;
		this.lossHistoryType = lossHistoryType;
		this.updateTime = updateTime;
		this.doNotArchive = doNotArchive;
		this.createTime = createTime;
		this.doNotPurge = doNotPurge;
	}

	public PcPolicy(long id, String issueDate, long retired, Boolean infobarDonotReinsRewriteFbm,
			Boolean infobarUmbrellaFbm, long accountId, String publicId, Long newProducerCodeExt,
			Boolean infobarCollectionFbm, Integer packageRisk, Long createUserId, BigDecimal priorTotalIncurred,
			Boolean infobarCargoFbm, Boolean infobarPendingCancelFbm, Integer beanVersion, long producerCodeOfServiceId,
			Integer primaryLanguage, Long updateUserId, Integer numPriorLosses, Boolean infobarHandRateFbm,
			String busOpsDescFbm, Boolean infobarFormEFbm, Boolean infobarSpecTentRateFbm,
			Boolean infobarTreatyExceptionFbm, String productCode, int lossHistoryType, Boolean infobarConstructionFbm,
			String updateTime, Long movedPolicySourceAccountId, boolean doNotArchive, Boolean infobarCashOnlyFbm,
			String createTime, Boolean commissionHoldFbm, Boolean infobarSr22Fbm, Boolean infobarReturnedMailFbm,
			String inceptionDateFbm, Integer yearBusinessStartedFbm, String originalEffectiveDate,
			Integer priorTotalIncurredCur, Integer archiveState, Long archiveSchemaInfo, String excludeReason,
			Long archiveFailureId, boolean doNotPurge, BigDecimal priorPremiums, Integer priorPremiumsCur,
			Boolean excludedFromArchive, Long archiveFailureDetailsId, Integer primaryLocale, Long archivePartition,
			String archiveDate, Boolean infobarPicturesInDisrFbm, String renewalReviewDate,
			Long previousProducerCodeOfService, Integer organizationType, Boolean infobarCertificateFbm,
			Long lineInceptionDatesFbm, Set<PcPolicyPeriod> pcPolicyperiods) {
		this.id = id;
		this.issueDate = issueDate;
		this.retired = retired;
		this.infobarDonotReinsRewriteFbm = infobarDonotReinsRewriteFbm;
		this.infobarUmbrellaFbm = infobarUmbrellaFbm;
		this.accountId = accountId;
		this.publicId = publicId;
		this.newProducerCodeExt = newProducerCodeExt;
		this.infobarCollectionFbm = infobarCollectionFbm;
		this.packageRisk = packageRisk;
		this.createUserId = createUserId;
		this.priorTotalIncurred = priorTotalIncurred;
		this.infobarCargoFbm = infobarCargoFbm;
		this.infobarPendingCancelFbm = infobarPendingCancelFbm;
		this.beanVersion = beanVersion;
		this.producerCodeOfServiceId = producerCodeOfServiceId;
		this.primaryLanguage = primaryLanguage;
		this.updateUserId = updateUserId;
		this.numPriorLosses = numPriorLosses;
		this.infobarHandRateFbm = infobarHandRateFbm;
		this.busOpsDescFbm = busOpsDescFbm;
		this.infobarFormEFbm = infobarFormEFbm;
		this.infobarSpecTentRateFbm = infobarSpecTentRateFbm;
		this.infobarTreatyExceptionFbm = infobarTreatyExceptionFbm;
		this.productCode = productCode;
		this.lossHistoryType = lossHistoryType;
		this.infobarConstructionFbm = infobarConstructionFbm;
		this.updateTime = updateTime;
		this.movedPolicySourceAccountId = movedPolicySourceAccountId;
		this.doNotArchive = doNotArchive;
		this.infobarCashOnlyFbm = infobarCashOnlyFbm;
		this.createTime = createTime;
		this.commissionHoldFbm = commissionHoldFbm;
		this.infobarSr22Fbm = infobarSr22Fbm;
		this.infobarReturnedMailFbm = infobarReturnedMailFbm;
		this.inceptionDateFbm = inceptionDateFbm;
		this.yearBusinessStartedFbm = yearBusinessStartedFbm;
		this.originalEffectiveDate = originalEffectiveDate;
		this.priorTotalIncurredCur = priorTotalIncurredCur;
		this.archiveState = archiveState;
		this.archiveSchemaInfo = archiveSchemaInfo;
		this.excludeReason = excludeReason;
		this.archiveFailureId = archiveFailureId;
		this.doNotPurge = doNotPurge;
		this.priorPremiums = priorPremiums;
		this.priorPremiumsCur = priorPremiumsCur;
		this.excludedFromArchive = excludedFromArchive;
		this.archiveFailureDetailsId = archiveFailureDetailsId;
		this.primaryLocale = primaryLocale;
		this.archivePartition = archivePartition;
		this.archiveDate = archiveDate;
		this.infobarPicturesInDisrFbm = infobarPicturesInDisrFbm;
		this.renewalReviewDate = renewalReviewDate;
		this.previousProducerCodeOfService = previousProducerCodeOfService;
		this.organizationType = organizationType;
		this.infobarCertificateFbm = infobarCertificateFbm;
		this.lineInceptionDatesFbm = lineInceptionDatesFbm;
		this.pcPolicyperiods = pcPolicyperiods;
	}

	@Id

	@Column(name = "ID", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "IssueDate", length = 27)
	public String getIssueDate() {
		return this.issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	@Column(name = "Retired", nullable = false)
	public long getRetired() {
		return this.retired;
	}

	public void setRetired(long retired) {
		this.retired = retired;
	}

	@Column(name = "InfobarDonotReinsRewrite_FBM")
	public Boolean getInfobarDonotReinsRewriteFbm() {
		return this.infobarDonotReinsRewriteFbm;
	}

	public void setInfobarDonotReinsRewriteFbm(Boolean infobarDonotReinsRewriteFbm) {
		this.infobarDonotReinsRewriteFbm = infobarDonotReinsRewriteFbm;
	}

	@Column(name = "InfobarUmbrella_FBM")
	public Boolean getInfobarUmbrellaFbm() {
		return this.infobarUmbrellaFbm;
	}

	public void setInfobarUmbrellaFbm(Boolean infobarUmbrellaFbm) {
		this.infobarUmbrellaFbm = infobarUmbrellaFbm;
	}

	@Column(name = "AccountID", nullable = false)
	public long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	@Column(name = "PublicID", nullable = false, length = 64)
	public String getPublicId() {
		return this.publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	@Column(name = "NewProducerCodeExt")
	public Long getNewProducerCodeExt() {
		return this.newProducerCodeExt;
	}

	public void setNewProducerCodeExt(Long newProducerCodeExt) {
		this.newProducerCodeExt = newProducerCodeExt;
	}

	@Column(name = "InfobarCollection_FBM")
	public Boolean getInfobarCollectionFbm() {
		return this.infobarCollectionFbm;
	}

	public void setInfobarCollectionFbm(Boolean infobarCollectionFbm) {
		this.infobarCollectionFbm = infobarCollectionFbm;
	}

	@Column(name = "PackageRisk")
	public Integer getPackageRisk() {
		return this.packageRisk;
	}

	public void setPackageRisk(Integer packageRisk) {
		this.packageRisk = packageRisk;
	}

	@Column(name = "CreateUserID")
	public Long getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "PriorTotalIncurred", precision = 18)
	public BigDecimal getPriorTotalIncurred() {
		return this.priorTotalIncurred;
	}

	public void setPriorTotalIncurred(BigDecimal priorTotalIncurred) {
		this.priorTotalIncurred = priorTotalIncurred;
	}

	@Column(name = "InfobarCargo_FBM")
	public Boolean getInfobarCargoFbm() {
		return this.infobarCargoFbm;
	}

	public void setInfobarCargoFbm(Boolean infobarCargoFbm) {
		this.infobarCargoFbm = infobarCargoFbm;
	}

	@Column(name = "InfobarPendingCancel_FBM")
	public Boolean getInfobarPendingCancelFbm() {
		return this.infobarPendingCancelFbm;
	}

	public void setInfobarPendingCancelFbm(Boolean infobarPendingCancelFbm) {
		this.infobarPendingCancelFbm = infobarPendingCancelFbm;
	}

	@Column(name = "BeanVersion")
	public Integer getBeanVersion() {
		return this.beanVersion;
	}

	public void setBeanVersion(Integer beanVersion) {
		this.beanVersion = beanVersion;
	}

	@Column(name = "ProducerCodeOfServiceID", nullable = false)
	public long getProducerCodeOfServiceId() {
		return this.producerCodeOfServiceId;
	}

	public void setProducerCodeOfServiceId(long producerCodeOfServiceId) {
		this.producerCodeOfServiceId = producerCodeOfServiceId;
	}

	@Column(name = "PrimaryLanguage")
	public Integer getPrimaryLanguage() {
		return this.primaryLanguage;
	}

	public void setPrimaryLanguage(Integer primaryLanguage) {
		this.primaryLanguage = primaryLanguage;
	}

	@Column(name = "UpdateUserID")
	public Long getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Column(name = "NumPriorLosses")
	public Integer getNumPriorLosses() {
		return this.numPriorLosses;
	}

	public void setNumPriorLosses(Integer numPriorLosses) {
		this.numPriorLosses = numPriorLosses;
	}

	@Column(name = "InfobarHandRate_FBM")
	public Boolean getInfobarHandRateFbm() {
		return this.infobarHandRateFbm;
	}

	public void setInfobarHandRateFbm(Boolean infobarHandRateFbm) {
		this.infobarHandRateFbm = infobarHandRateFbm;
	}

	@Column(name = "BusOpsDesc_FBM", length = 240)
	public String getBusOpsDescFbm() {
		return this.busOpsDescFbm;
	}

	public void setBusOpsDescFbm(String busOpsDescFbm) {
		this.busOpsDescFbm = busOpsDescFbm;
	}

	@Column(name = "InfobarFormE_FBM")
	public Boolean getInfobarFormEFbm() {
		return this.infobarFormEFbm;
	}

	public void setInfobarFormEFbm(Boolean infobarFormEFbm) {
		this.infobarFormEFbm = infobarFormEFbm;
	}

	@Column(name = "InfobarSpecTentRate_FBM")
	public Boolean getInfobarSpecTentRateFbm() {
		return this.infobarSpecTentRateFbm;
	}

	public void setInfobarSpecTentRateFbm(Boolean infobarSpecTentRateFbm) {
		this.infobarSpecTentRateFbm = infobarSpecTentRateFbm;
	}

	@Column(name = "InfobarTreatyException_FBM")
	public Boolean getInfobarTreatyExceptionFbm() {
		return this.infobarTreatyExceptionFbm;
	}

	public void setInfobarTreatyExceptionFbm(Boolean infobarTreatyExceptionFbm) {
		this.infobarTreatyExceptionFbm = infobarTreatyExceptionFbm;
	}

	@Column(name = "ProductCode", nullable = false, length = 64)
	public String getProductCode() {
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	@Column(name = "LossHistoryType", nullable = false)
	public int getLossHistoryType() {
		return this.lossHistoryType;
	}

	public void setLossHistoryType(int lossHistoryType) {
		this.lossHistoryType = lossHistoryType;
	}

	@Column(name = "InfobarConstruction_FBM")
	public Boolean getInfobarConstructionFbm() {
		return this.infobarConstructionFbm;
	}

	public void setInfobarConstructionFbm(Boolean infobarConstructionFbm) {
		this.infobarConstructionFbm = infobarConstructionFbm;
	}

	@Column(name = "UpdateTime", nullable = false, length = 27)
	public String getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "MovedPolicySourceAccountID")
	public Long getMovedPolicySourceAccountId() {
		return this.movedPolicySourceAccountId;
	}

	public void setMovedPolicySourceAccountId(Long movedPolicySourceAccountId) {
		this.movedPolicySourceAccountId = movedPolicySourceAccountId;
	}

	@Column(name = "DoNotArchive", nullable = false)
	public boolean isDoNotArchive() {
		return this.doNotArchive;
	}

	public void setDoNotArchive(boolean doNotArchive) {
		this.doNotArchive = doNotArchive;
	}

	@Column(name = "InfobarCashOnly_FBM")
	public Boolean getInfobarCashOnlyFbm() {
		return this.infobarCashOnlyFbm;
	}

	public void setInfobarCashOnlyFbm(Boolean infobarCashOnlyFbm) {
		this.infobarCashOnlyFbm = infobarCashOnlyFbm;
	}

	@Column(name = "CreateTime", nullable = false, length = 27)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CommissionHold_FBM")
	public Boolean getCommissionHoldFbm() {
		return this.commissionHoldFbm;
	}

	public void setCommissionHoldFbm(Boolean commissionHoldFbm) {
		this.commissionHoldFbm = commissionHoldFbm;
	}

	@Column(name = "InfobarSR22_FBM")
	public Boolean getInfobarSr22Fbm() {
		return this.infobarSr22Fbm;
	}

	public void setInfobarSr22Fbm(Boolean infobarSr22Fbm) {
		this.infobarSr22Fbm = infobarSr22Fbm;
	}

	@Column(name = "InfobarReturnedMail_FBM")
	public Boolean getInfobarReturnedMailFbm() {
		return this.infobarReturnedMailFbm;
	}

	public void setInfobarReturnedMailFbm(Boolean infobarReturnedMailFbm) {
		this.infobarReturnedMailFbm = infobarReturnedMailFbm;
	}

	@Column(name = "InceptionDate_FBM", length = 27)
	public String getInceptionDateFbm() {
		return this.inceptionDateFbm;
	}

	public void setInceptionDateFbm(String inceptionDateFbm) {
		this.inceptionDateFbm = inceptionDateFbm;
	}

	@Column(name = "YearBusinessStarted_FBM")
	public Integer getYearBusinessStartedFbm() {
		return this.yearBusinessStartedFbm;
	}

	public void setYearBusinessStartedFbm(Integer yearBusinessStartedFbm) {
		this.yearBusinessStartedFbm = yearBusinessStartedFbm;
	}

	@Column(name = "OriginalEffectiveDate", length = 27)
	public String getOriginalEffectiveDate() {
		return this.originalEffectiveDate;
	}

	public void setOriginalEffectiveDate(String originalEffectiveDate) {
		this.originalEffectiveDate = originalEffectiveDate;
	}

	@Column(name = "PriorTotalIncurred_cur")
	public Integer getPriorTotalIncurredCur() {
		return this.priorTotalIncurredCur;
	}

	public void setPriorTotalIncurredCur(Integer priorTotalIncurredCur) {
		this.priorTotalIncurredCur = priorTotalIncurredCur;
	}

	@Column(name = "ArchiveState")
	public Integer getArchiveState() {
		return this.archiveState;
	}

	public void setArchiveState(Integer archiveState) {
		this.archiveState = archiveState;
	}

	@Column(name = "ArchiveSchemaInfo")
	public Long getArchiveSchemaInfo() {
		return this.archiveSchemaInfo;
	}

	public void setArchiveSchemaInfo(Long archiveSchemaInfo) {
		this.archiveSchemaInfo = archiveSchemaInfo;
	}

	@Column(name = "ExcludeReason")
	public String getExcludeReason() {
		return this.excludeReason;
	}

	public void setExcludeReason(String excludeReason) {
		this.excludeReason = excludeReason;
	}

	@Column(name = "ArchiveFailureID")
	public Long getArchiveFailureId() {
		return this.archiveFailureId;
	}

	public void setArchiveFailureId(Long archiveFailureId) {
		this.archiveFailureId = archiveFailureId;
	}

	@Column(name = "DoNotPurge", nullable = false)
	public boolean isDoNotPurge() {
		return this.doNotPurge;
	}

	public void setDoNotPurge(boolean doNotPurge) {
		this.doNotPurge = doNotPurge;
	}

	@Column(name = "PriorPremiums", precision = 18)
	public BigDecimal getPriorPremiums() {
		return this.priorPremiums;
	}

	public void setPriorPremiums(BigDecimal priorPremiums) {
		this.priorPremiums = priorPremiums;
	}

	@Column(name = "PriorPremiums_cur")
	public Integer getPriorPremiumsCur() {
		return this.priorPremiumsCur;
	}

	public void setPriorPremiumsCur(Integer priorPremiumsCur) {
		this.priorPremiumsCur = priorPremiumsCur;
	}

	@Column(name = "ExcludedFromArchive")
	public Boolean getExcludedFromArchive() {
		return this.excludedFromArchive;
	}

	public void setExcludedFromArchive(Boolean excludedFromArchive) {
		this.excludedFromArchive = excludedFromArchive;
	}

	@Column(name = "ArchiveFailureDetailsID", unique = true)
	public Long getArchiveFailureDetailsId() {
		return this.archiveFailureDetailsId;
	}

	public void setArchiveFailureDetailsId(Long archiveFailureDetailsId) {
		this.archiveFailureDetailsId = archiveFailureDetailsId;
	}

	@Column(name = "PrimaryLocale")
	public Integer getPrimaryLocale() {
		return this.primaryLocale;
	}

	public void setPrimaryLocale(Integer primaryLocale) {
		this.primaryLocale = primaryLocale;
	}

	@Column(name = "ArchivePartition")
	public Long getArchivePartition() {
		return this.archivePartition;
	}

	public void setArchivePartition(Long archivePartition) {
		this.archivePartition = archivePartition;
	}

	@Column(name = "ArchiveDate", length = 27)
	public String getArchiveDate() {
		return this.archiveDate;
	}

	public void setArchiveDate(String archiveDate) {
		this.archiveDate = archiveDate;
	}

	@Column(name = "InfobarPicturesInDISR_FBM")
	public Boolean getInfobarPicturesInDisrFbm() {
		return this.infobarPicturesInDisrFbm;
	}

	public void setInfobarPicturesInDisrFbm(Boolean infobarPicturesInDisrFbm) {
		this.infobarPicturesInDisrFbm = infobarPicturesInDisrFbm;
	}

	@Column(name = "RenewalReviewDate", length = 27)
	public String getRenewalReviewDate() {
		return this.renewalReviewDate;
	}

	public void setRenewalReviewDate(String renewalReviewDate) {
		this.renewalReviewDate = renewalReviewDate;
	}

	@Column(name = "PreviousProducerCodeOfService")
	public Long getPreviousProducerCodeOfService() {
		return this.previousProducerCodeOfService;
	}

	public void setPreviousProducerCodeOfService(Long previousProducerCodeOfService) {
		this.previousProducerCodeOfService = previousProducerCodeOfService;
	}

	@Column(name = "OrganizationType")
	public Integer getOrganizationType() {
		return this.organizationType;
	}

	public void setOrganizationType(Integer organizationType) {
		this.organizationType = organizationType;
	}

	@Column(name = "InfobarCertificate_FBM")
	public Boolean getInfobarCertificateFbm() {
		return this.infobarCertificateFbm;
	}

	public void setInfobarCertificateFbm(Boolean infobarCertificateFbm) {
		this.infobarCertificateFbm = infobarCertificateFbm;
	}

	@Column(name = "LineInceptionDates_FBM")
	public Long getLineInceptionDatesFbm() {
		return this.lineInceptionDatesFbm;
	}

	public void setLineInceptionDatesFbm(Long lineInceptionDatesFbm) {
		this.lineInceptionDatesFbm = lineInceptionDatesFbm;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pcPolicy")
	public Set<PcPolicyPeriod> getPcPolicyperiods() {
		return this.pcPolicyperiods;
	}

	public void setPcPolicyperiods(Set<PcPolicyPeriod> pcPolicyperiods) {
		this.pcPolicyperiods = pcPolicyperiods;
	}

}
