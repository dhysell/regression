package repository.gw.generate.custom;

import persistence.globaldatarepo.entities.IMUWQuestions;
import persistence.globaldatarepo.helpers.IMUWQuestionsHelper;
import repository.gw.enums.*;

public class CPPInlandMarineUWQuestion {

	private InlandMarineCPP.InlandMarineCoveragePart coveragePart;
	private String questionText;
	private String questionCode;
	private int questionSequence;
	private repository.gw.enums.QuestionType questionType;
	private repository.gw.enums.FormatType formatType;
	private repository.gw.enums.GeneratePolicyType requiredAt;
	private String parentQuestionCode;
	private CPPInlandMarineUWQuestion dependentOnQuestion = null;
	private String dependantOnAnswer;
	private boolean isDependantQuestion = false;
	private boolean handleIncorrectAnswer;
	private String correctAnswer;
	private String failureMesssage;
	private repository.gw.enums.BlockingAction blockingAction;
	
	CPPInlandMarineUWQuestion(IMUWQuestions question) throws Exception {
		this.coveragePart = setCoveragePartFromQuestionLocation(question);
		this.questionText = question.getQuestionLabel();
		this.questionCode = question.getQuestionCode();
		this.questionSequence = question.getSequence();
		this.questionType = repository.gw.enums.QuestionType.valueOf(question.getQuestionType());
		this.formatType = setFormatType(question.getFormat());
		this.requiredAt = (question.getRequiredAt().equals("QQ")) ? repository.gw.enums.GeneratePolicyType.QuickQuote : repository.gw.enums.GeneratePolicyType.FullApp;
		this.parentQuestionCode = question.getParentQuestionCode();
		this.setDependantOnAnswer(question.getDependentOnQuestionAnswer());
		this.dependentOnQuestion = (question.getDependentOnQuestion().equals("-")) ? null : new CPPInlandMarineUWQuestion(IMUWQuestionsHelper.getUWQuestionWithQuesitonCode(this.parentQuestionCode));
		this.isDependantQuestion = (this.dependentOnQuestion == null) ? false : true;
		this.handleIncorrectAnswer = question.getHandleIncorrectAnswer().equals("Yes");
		this.correctAnswer = setCorrectAnswerString(question.getCorrectAnswer());
		this.failureMesssage = question.getFailureMessage();
		this.blockingAction = (question.getBlockingAction().equals("Block user")) ? repository.gw.enums.BlockingAction.Blockuser : repository.gw.enums.BlockingAction.NONE;
		
	}
	
	public InlandMarineCPP.InlandMarineCoveragePart getCoveragePart() {
		return coveragePart;
	}

	public void setCoveragePart(InlandMarineCPP.InlandMarineCoveragePart coveragePart) {
		this.coveragePart = coveragePart;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getQuestionCode() {
		return questionCode;
	}

	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}

	public int getQuestionSequence() {
		return questionSequence;
	}

	public void setQuestionSequence(int questionSequence) {
		this.questionSequence = questionSequence;
	}

	public repository.gw.enums.QuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(repository.gw.enums.QuestionType questionType) {
		this.questionType = questionType;
	}

	public repository.gw.enums.FormatType getFormatType() {
		return formatType;
	}

	public void setFormatType(repository.gw.enums.FormatType formatType) {
		this.formatType = formatType;
	}

	public repository.gw.enums.GeneratePolicyType getRequiredAt() {
		return requiredAt;
	}

	public void setRequiredAt(GeneratePolicyType requiredAt) {
		this.requiredAt = requiredAt;
	}

	public String getParentQuestionCode() {
		return parentQuestionCode;
	}

	public void setParentQuestionCode(String parentQuestionCode) {
		this.parentQuestionCode = parentQuestionCode;
	}

	public CPPInlandMarineUWQuestion getDependentOnQuestion() {
		return dependentOnQuestion;
	}

	public void setDependentOnQuestion(CPPInlandMarineUWQuestion dependentOnQuestion) {
		this.dependentOnQuestion = dependentOnQuestion;
	}

	public boolean isDependantQuestion() {
		return isDependantQuestion;
	}

	public void setDependantQuestion(boolean isDependantQuestion) {
		this.isDependantQuestion = isDependantQuestion;
	}

	public boolean isHandleIncorrectAnswer() {
		return handleIncorrectAnswer;
	}

	public void setHandleIncorrectAnswer(boolean handleIncorrectAnswer) {
		this.handleIncorrectAnswer = handleIncorrectAnswer;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public String getFailureMesssage() {
		return failureMesssage;
	}

	public void setFailureMesssage(String failureMesssage) {
		this.failureMesssage = failureMesssage;
	}

	public repository.gw.enums.BlockingAction getBlockingAction() {
		return blockingAction;
	}

	public void setBlockingAction(BlockingAction blockingAction) {
		this.blockingAction = blockingAction;
	}
	
	// PRIVATE METHODS

	private InlandMarineCPP.InlandMarineCoveragePart setCoveragePartFromQuestionLocation(IMUWQuestions question) {
		switch(question.getLocationOfQuestion()) {
			case "Commercial Articles":
				return InlandMarineCPP.InlandMarineCoveragePart.CommercialArticles_CM_00_20;
			case "Camera And Musical Instrument Dealers":
				return InlandMarineCPP.InlandMarineCoveragePart.CameraAndMusicalInstrumentDealers_CM_00_21;
			case "Accounts Receivable": 
				return InlandMarineCPP.InlandMarineCoveragePart.AccountsReceivable_CM_00_66;
			case "Valuable Papers And Records":
				return InlandMarineCPP.InlandMarineCoveragePart.ValuablePapers_CM_00_67;
			case "Motor Truck Cargo Coverage":
				return InlandMarineCPP.InlandMarineCoveragePart.MotorTruckCargo;
			case "Contractors Equipment":
				return InlandMarineCPP.InlandMarineCoveragePart.ContractorsEquipment_IH_00_68;
			case "Computer Systems":
				return InlandMarineCPP.InlandMarineCoveragePart.ComputerSystems_IH_00_75;
			case "Trip Transit":
				return InlandMarineCPP.InlandMarineCoveragePart.TripTransit_IH_00_78;
			case "Bailees Customers":
				return InlandMarineCPP.InlandMarineCoveragePart.BaileesCustomers_IH_00_85;
			default:
				return null;
		}
	}
	
	private repository.gw.enums.FormatType setFormatType(String formatType) {
		switch(formatType) {
		case "Boolean Radio":
			return repository.gw.enums.FormatType.BooleanRadio;
		case "String Text Box":
			return repository.gw.enums.FormatType.StringTextBox;
		case "Choice Select":
			return repository.gw.enums.FormatType.ChoiceSelect;
		case "String Field":
			return repository.gw.enums.FormatType.StringField;
		case "Boolean Checkbox":
			return repository.gw.enums.FormatType.BooleanCheckbox;
		case "Integer Field":
			return repository.gw.enums.FormatType.IntegerField;
		case "Date Field":
			return FormatType.DateField;
		default:
			return null;
		}
	}
	
	private String setCorrectAnswerString(String answer) {
		if(answer.equals("-")) {
			if(this.questionType.equals(repository.gw.enums.QuestionType.Integer)) {
				return "1";
			} else if(this.questionType.equals(QuestionType.String)) {
				return "Little Bunny Foo Foo";
			}
		}
		return answer;

	}

	public String getDependantOnAnswer() {
		return dependantOnAnswer;
	}

	public void setDependantOnAnswer(String dependantOnAnswer) {
		this.dependantOnAnswer = dependantOnAnswer;
	}
	
}
