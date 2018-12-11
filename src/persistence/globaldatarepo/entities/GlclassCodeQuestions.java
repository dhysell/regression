package persistence.globaldatarepo.entities;

// Generated Aug 3, 2016 10:06:25 AM by Hibernate Tools 4.0.0

import javax.persistence.*;

/**
 * GlclassCodeQuestions generated by hbm2java
 */
@Entity
@Table(name = "GLClassCodeQuestions", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class GlclassCodeQuestions {

	private int id;
	private String classCode;
	private String classCodeName;
	private String locationOfQuestion;
	private String parentQuestionCode;
	private String questionCode;
	private String sequence;
	private String questionType;
	private String questionLabel;
	private String format;
	private String choiceOptions;
	private String requiredAt;
	private String dependentOnQuestion;
	private String dependentOnQuestionAnswer;
	private String handleIncorrectAnswer;
	private String correctAnswer;
	private String failureMessage;
	private String blockingAction;
	private String createUwissueType;
	private String riskPoints;
	private String availabilityScript;
	private String startEffectiveDate;
	private String endEffectiveDate;
	private String availability;

	public GlclassCodeQuestions() {
	}

	public GlclassCodeQuestions(int id) {
		this.id = id;
	}

	public GlclassCodeQuestions(int id, String classCode, String classCodeName, String locationOfQuestion, String parentQuestionCode, String questionCode, String sequence, String questionType, String questionLabel, String format, String choiceOptions, String requiredAt, String dependentOnQuestion, String dependentOnQuestionAnswer, String handleIncorrectAnswer, String correctAnswer, String failureMessage, String blockingAction, String createUwissueType, String riskPoints, String availabilityScript, String startEffectiveDate, String endEffectiveDate, String availability) {
		this.id = id;
		this.classCode = classCode;
		this.classCodeName = classCodeName;
		this.locationOfQuestion = locationOfQuestion;
		this.parentQuestionCode = parentQuestionCode;
		this.questionCode = questionCode;
		this.sequence = sequence;
		this.questionType = questionType;
		this.questionLabel = questionLabel;
		this.format = format;
		this.choiceOptions = choiceOptions;
		this.requiredAt = requiredAt;
		this.dependentOnQuestion = dependentOnQuestion;
		this.dependentOnQuestionAnswer = dependentOnQuestionAnswer;
		this.handleIncorrectAnswer = handleIncorrectAnswer;
		this.correctAnswer = correctAnswer;
		this.failureMessage = failureMessage;
		this.blockingAction = blockingAction;
		this.createUwissueType = createUwissueType;
		this.riskPoints = riskPoints;
		this.availabilityScript = availabilityScript;
		this.startEffectiveDate = startEffectiveDate;
		this.endEffectiveDate = endEffectiveDate;
		this.availability = availability;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "ClassCode", length = 500)
	public String getClassCode() {
		return this.classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	@Column(name = "ClassCodeName", length = 500)
	public String getClassCodeName() {
		return this.classCodeName;
	}

	public void setClassCodeName(String classCodeName) {
		this.classCodeName = classCodeName;
	}

	@Column(name = "LocationOfQuestion", length = 500)
	public String getLocationOfQuestion() {
		return this.locationOfQuestion;
	}

	public void setLocationOfQuestion(String locationOfQuestion) {
		this.locationOfQuestion = locationOfQuestion;
	}

	@Column(name = "ParentQuestionCode", length = 500)
	public String getParentQuestionCode() {
		return this.parentQuestionCode;
	}

	public void setParentQuestionCode(String parentQuestionCode) {
		this.parentQuestionCode = parentQuestionCode;
	}

	@Column(name = "QuestionCode", length = 500)
	public String getQuestionCode() {
		return this.questionCode;
	}

	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}

	@Column(name = "Sequence", length = 500)
	public String getSequence() {
		return this.sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	@Column(name = "QuestionType", length = 500)
	public String getQuestionType() {
		return this.questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	@Column(name = "QuestionLabel", length = 500)
	public String getQuestionLabel() {
		return this.questionLabel;
	}

	public void setQuestionLabel(String questionLabel) {
		this.questionLabel = questionLabel;
	}

	@Column(name = "Format", length = 500)
	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Column(name = "ChoiceOptions", length = 500)
	public String getChoiceOptions() {
		return this.choiceOptions;
	}

	public void setChoiceOptions(String choiceOptions) {
		this.choiceOptions = choiceOptions;
	}

	@Column(name = "RequiredAt", length = 500)
	public String getRequiredAt() {
		return this.requiredAt;
	}

	public void setRequiredAt(String requiredAt) {
		this.requiredAt = requiredAt;
	}

	@Column(name = "DependentOnQuestion", length = 500)
	public String getDependentOnQuestion() {
		return this.dependentOnQuestion;
	}

	public void setDependentOnQuestion(String dependentOnQuestion) {
		this.dependentOnQuestion = dependentOnQuestion;
	}

	@Column(name = "DependentOnQuestionAnswer", length = 500)
	public String getDependentOnQuestionAnswer() {
		return this.dependentOnQuestionAnswer;
	}

	public void setDependentOnQuestionAnswer(String dependentOnQuestionAnswer) {
		this.dependentOnQuestionAnswer = dependentOnQuestionAnswer;
	}

	@Column(name = "HandleIncorrectAnswer", length = 500)
	public String getHandleIncorrectAnswer() {
		return this.handleIncorrectAnswer;
	}

	public void setHandleIncorrectAnswer(String handleIncorrectAnswer) {
		this.handleIncorrectAnswer = handleIncorrectAnswer;
	}

	@Column(name = "CorrectAnswer", length = 500)
	public String getCorrectAnswer() {
		return this.correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	@Column(name = "FailureMessage", length = 500)
	public String getFailureMessage() {
		return this.failureMessage;
	}

	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}

	@Column(name = "BlockingAction", length = 500)
	public String getBlockingAction() {
		return this.blockingAction;
	}

	public void setBlockingAction(String blockingAction) {
		this.blockingAction = blockingAction;
	}

	@Column(name = "CreateUWIssueType", length = 500)
	public String getCreateUwissueType() {
		return this.createUwissueType;
	}

	public void setCreateUwissueType(String createUwissueType) {
		this.createUwissueType = createUwissueType;
	}

	@Column(name = "RiskPoints", length = 500)
	public String getRiskPoints() {
		return this.riskPoints;
	}

	public void setRiskPoints(String riskPoints) {
		this.riskPoints = riskPoints;
	}

	@Column(name = "AvailabilityScript", length = 500)
	public String getAvailabilityScript() {
		return this.availabilityScript;
	}

	public void setAvailabilityScript(String availabilityScript) {
		this.availabilityScript = availabilityScript;
	}

	@Column(name = "StartEffectiveDate", length = 500)
	public String getStartEffectiveDate() {
		return this.startEffectiveDate;
	}

	public void setStartEffectiveDate(String startEffectiveDate) {
		this.startEffectiveDate = startEffectiveDate;
	}

	@Column(name = "EndEffectiveDate", length = 500)
	public String getEndEffectiveDate() {
		return this.endEffectiveDate;
	}

	public void setEndEffectiveDate(String endEffectiveDate) {
		this.endEffectiveDate = endEffectiveDate;
	}

	@Column(name = "Availability", length = 500)
	public String getAvailability() {
		return this.availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

}