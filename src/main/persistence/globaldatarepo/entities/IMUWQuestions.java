package persistence.globaldatarepo.entities;

// default package
// Generated Aug 10, 2017 2:38:16 PM by Hibernate Tools 5.2.0.Beta1

import javax.persistence.*;
import java.sql.Clob;

/**
 * Imuwquestions generated by hbm2java
 */
@Entity
@Table(name = "IMUWQuestions", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class IMUWQuestions{

	private int id;
	private String table;
	private String locationOfQuestion;
	private String parentQuestionCode;
	private String questionCode;
	private int sequence;
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
	private String createUwIssueType;
	private Clob availabilityScript;
	private String availability;
	private String imClassCode;
	private String questionCodeChecking;
	private String sequenceChecking;
	private String startOfSequenceChecking;
	private String characterCount;
	private Double characterNumberCount;

	public IMUWQuestions() {
	}

	public IMUWQuestions(int id) {
		this.id = id;
	}

	public IMUWQuestions(int id, String table, String locationOfQuestion, String parentQuestionCode, String questionCode, int sequence, String questionType, String questionLabel, String format, String choiceOptions, String requiredAt,
			String dependentOnQuestion, String dependentOnQuestionAnswer, String handleIncorrectAnswer, String correctAnswer, String failureMessage, String blockingAction, String createUwIssueType, Clob availabilityScript, String availability,
			String imClassCode, String questionCodeChecking, String sequenceChecking, String startOfSequenceChecking, String characterCount, Double characterNumberCount) {
		this.id = id;
		this.table = table;
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
		this.createUwIssueType = createUwIssueType;
		this.availabilityScript = availabilityScript;
		this.availability = availability;
		this.imClassCode = imClassCode;
		this.questionCodeChecking = questionCodeChecking;
		this.sequenceChecking = sequenceChecking;
		this.startOfSequenceChecking = startOfSequenceChecking;
		this.characterCount = characterCount;
		this.characterNumberCount = characterNumberCount;
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

	@Column(name = "TableForm")
	public String getTable() {
		return this.table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	@Column(name = "LocationOfQuestion")
	public String getLocationOfQuestion() {
		return this.locationOfQuestion;
	}

	public void setLocationOfQuestion(String locationOfQuestion) {
		this.locationOfQuestion = locationOfQuestion;
	}

	@Column(name = "ParentQuestionCode")
	public String getParentQuestionCode() {
		return this.parentQuestionCode;
	}

	public void setParentQuestionCode(String parentQuestionCode) {
		this.parentQuestionCode = parentQuestionCode;
	}

	@Column(name = "QuestionCode")
	public String getQuestionCode() {
		return this.questionCode;
	}

	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}

	@Column(name = "Sequence")
	public int getSequence() {
		return this.sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	@Column(name = "QuestionType")
	public String getQuestionType() {
		return this.questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	@Column(name = "QuestionLabel")
	public String getQuestionLabel() {
		return this.questionLabel;
	}

	public void setQuestionLabel(String questionLabel) {
		this.questionLabel = questionLabel;
	}

	@Column(name = "Format")
	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Column(name = "ChoiceOptions")
	public String getChoiceOptions() {
		return this.choiceOptions;
	}

	public void setChoiceOptions(String choiceOptions) {
		this.choiceOptions = choiceOptions;
	}

	@Column(name = "RequiredAt")
	public String getRequiredAt() {
		return this.requiredAt;
	}

	public void setRequiredAt(String requiredAt) {
		this.requiredAt = requiredAt;
	}

	@Column(name = "DependentOnQuestion")
	public String getDependentOnQuestion() {
		return this.dependentOnQuestion;
	}

	public void setDependentOnQuestion(String dependentOnQuestion) {
		this.dependentOnQuestion = dependentOnQuestion;
	}

	@Column(name = "DependentOnQuestionAnswer")
	public String getDependentOnQuestionAnswer() {
		return this.dependentOnQuestionAnswer;
	}

	public void setDependentOnQuestionAnswer(String dependentOnQuestionAnswer) {
		this.dependentOnQuestionAnswer = dependentOnQuestionAnswer;
	}

	@Column(name = "HandleIncorrectAnswer")
	public String getHandleIncorrectAnswer() {
		return this.handleIncorrectAnswer;
	}

	public void setHandleIncorrectAnswer(String handleIncorrectAnswer) {
		this.handleIncorrectAnswer = handleIncorrectAnswer;
	}

	@Column(name = "CorrectAnswer")
	public String getCorrectAnswer() {
		return this.correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	@Column(name = "FailureMessage")
	public String getFailureMessage() {
		return this.failureMessage;
	}

	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}

	@Column(name = "BlockingAction")
	public String getBlockingAction() {
		return this.blockingAction;
	}

	public void setBlockingAction(String blockingAction) {
		this.blockingAction = blockingAction;
	}

	@Column(name = "CreateUWIssueType")
	public String getCreateUwIssueType() {
		return this.createUwIssueType;
	}

	public void setCreateUwIssueType(String createUwIssueType) {
		this.createUwIssueType = createUwIssueType;
	}

	@Column(name = "AvailabilityScript")
	public Clob getAvailabilityScript() {
		return this.availabilityScript;
	}

	public void setAvailabilityScript(Clob availabilityScript) {
		this.availabilityScript = availabilityScript;
	}

	@Column(name = "Availability")
	public String getAvailability() {
		return this.availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	@Column(name = "IMClassCode")
	public String getImClassCode() {
		return this.imClassCode;
	}

	public void setImClassCode(String imClassCode) {
		this.imClassCode = imClassCode;
	}

	@Column(name = "QuestionCodeChecking")
	public String getQuestionCodeChecking() {
		return this.questionCodeChecking;
	}

	public void setQuestionCodeChecking(String questionCodeChecking) {
		this.questionCodeChecking = questionCodeChecking;
	}

	@Column(name = "SequenceChecking")
	public String getSequenceChecking() {
		return this.sequenceChecking;
	}

	public void setSequenceChecking(String sequenceChecking) {
		this.sequenceChecking = sequenceChecking;
	}

	@Column(name = "StartofSequenceChecking")
	public String getStartOfSequenceChecking() {
		return this.startOfSequenceChecking;
	}

	public void setStartOfSequenceChecking(String startOfSequenceChecking) {
		this.startOfSequenceChecking = startOfSequenceChecking;
	}

	@Column(name = "CharacterCount")
	public String getCharacterCount() {
		return this.characterCount;
	}

	public void setCharacterCount(String characterCount) {
		this.characterCount = characterCount;
	}

	@Column(name = "CharacterNumberCount", precision = 53, scale = 0)
	public Double getCharacterNumberCount() {
		return this.characterNumberCount;
	}

	public void setCharacterNumberCount(Double characterNumberCount) {
		this.characterNumberCount = characterNumberCount;
	}

}
