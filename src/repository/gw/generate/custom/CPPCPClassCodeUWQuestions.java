package repository.gw.generate.custom;

import persistence.globaldatarepo.entities.CPUWQuestions;
import persistence.globaldatarepo.helpers.CPUWQuestionsHelper;
import repository.gw.enums.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CPPCPClassCodeUWQuestions {

	private String classCode;
	private String classCodeName;
	private int questionSequence;
	private repository.gw.enums.QuestionType questionsType;
	private String questionText;
	private List<String> choiceOptions;
	private repository.gw.enums.GeneratePolicyType requiredAt;
	private CPPCPClassCodeUWQuestions dependentOnQuestion = null;
	private boolean setDependantQuestion = false; //to be set in code
	private String dependentOnAnswer;
	private boolean handleIncorrectAnswer;
	private String correctAnswer;
	private String failureMessage;
	private repository.gw.enums.BlockingAction blockingAction;
	private boolean isDependantQuestion = false;
	private String questionCode;
	private String parentQuestionCode;
	private List<Integer> duplicateQuestions = new ArrayList<Integer>();
	private List<CPPCPClassCodeUWQuestions> childQuestions = new ArrayList<CPPCPClassCodeUWQuestions>();
	
	private repository.gw.enums.FormatType formatType = repository.gw.enums.FormatType.BooleanRadio;
	private repository.gw.enums.UnderwriterIssueType uwIssueType = null;
	
	
	

	public CPPCPClassCodeUWQuestions(CPUWQuestions question) throws Exception {
		System.out.println("creating question");
		this.classCode = question.getClassCode();
		this.classCodeName = question.getClassCodeName();
		this.questionSequence = Integer.valueOf(question.getSequence());
		this.questionsType = repository.gw.enums.QuestionType.valueOf(question.getQuestionType());
		this.questionText = question.getQuestionLabel().replace("\"", "").trim();
		this.choiceOptions = getChoiceOptions(question);
		this.requiredAt = (question.getRequiredAt().equals("QQ")) ? repository.gw.enums.GeneratePolicyType.QuickQuote : repository.gw.enums.GeneratePolicyType.FullApp;
		this.dependentOnQuestion = (question.getDependentOnQuestion().equals("-")) ? null : new CPPCPClassCodeUWQuestions(CPUWQuestionsHelper.getUWQuestionsQuestionParentCode(question.getParentQuestionCode()));
		this.dependentOnAnswer = question.getDependentOnAnswer();
		this.handleIncorrectAnswer = question.getHandleIncorrectAnswer().equals("Yes");
		this.correctAnswer = setCorrectAnswerString(question.getCorrectAnswer());
		this.failureMessage = question.getFailureMessage().replace("\"", "").trim();
		this.blockingAction = (question.getBlockingAction().equals("Block user")) ? repository.gw.enums.BlockingAction.Blockuser : repository.gw.enums.BlockingAction.NONE;
		this.isDependantQuestion = (this.dependentOnQuestion == null) ? false : true;
		this.questionCode = question.getQuestionCode();
		this.parentQuestionCode = question.getParentQuestionCode();
		this.duplicateQuestions = null;
		this.formatType = setFormatType(question.getFormat());
		this.uwIssueType = setUWIssueType(question.getCreateUwissueType());
		//this.childQuestions = getChildQuestionsList();
	}
	
	
	private repository.gw.enums.UnderwriterIssueType setUWIssueType(String issueType) {
		switch(issueType) {
			case "Question Block Quote Release":
				return repository.gw.enums.UnderwriterIssueType.BlockQuoteRelease;
			case "Question non-blocking":
				return repository.gw.enums.UnderwriterIssueType.Informational;
			case "Question blocking bind":
				return repository.gw.enums.UnderwriterIssueType.BlockSubmit;
			default:
				return null;
		}
	}

//	private List<CPPCPClassCodeUWQuestions> getChildQuestionsList() throws Exception {
//		List<CPUWQuestions> dbQuestionList = CPUWQuestionsHelper.getAllchildQuestions(questionCode);
//		List<CPPCPClassCodeUWQuestions> returnList = new ArrayList<CPPCPClassCodeUWQuestions>();
//		for(CPUWQuestions dbQuestion : dbQuestionList) {
//			returnList.add(new CPPCPClassCodeUWQuestions(dbQuestion));
//		}
//		if(returnList.isEmpty()) {
//			return null;
//		} else {
//			return returnList;
//		}
//	}
	
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
			return repository.gw.enums.FormatType.DateField;
		default:
			return null;
		}
	}
	
	







	private String setCorrectAnswerString(String answer) {
		if(answer.equals("-")) {
			if(this.questionsType.equals(repository.gw.enums.QuestionType.Integer)) {
				return "1";
			} else if(this.questionsType.equals(repository.gw.enums.QuestionType.String)) {
				return "foo for Brett";
			}
		}
		return answer;

	}


	public CPPCPClassCodeUWQuestions(String classCode, String classCodeName, int questionSequence, repository.gw.enums.QuestionType questionsType, String questionLabel, List<String> choiceOptions, repository.gw.enums.GeneratePolicyType requiredAt, CPPCPClassCodeUWQuestions dependentOnQuestion, String dependentOnAnswer, boolean handleIncorrectAnswer, String correctAnswer, String failureMessage, repository.gw.enums.BlockingAction blockingAction) {
		this.classCode = classCode;
		this.classCodeName = classCodeName;
		this.questionSequence = questionSequence;
		this.questionsType = questionsType;
		this.questionText = questionLabel;
		this.choiceOptions = choiceOptions;
		this.requiredAt = requiredAt;
		this.dependentOnQuestion = dependentOnQuestion;
		this.dependentOnAnswer = dependentOnAnswer;
		this.handleIncorrectAnswer = handleIncorrectAnswer;
		this.correctAnswer = correctAnswer;
		this.failureMessage = failureMessage;
		this.blockingAction = blockingAction;
	}


	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getClassCodeName() {
		return classCodeName;
	}
	public void setClassCodeName(String classCodeName) {
		this.classCodeName = classCodeName;
	}
	public int getQuestionSequence() {
		return questionSequence;
	}
	public void setQuestionSequence(int questionSequence) {
		this.questionSequence = questionSequence;
	}
	public repository.gw.enums.QuestionType getQuestionsType() {
		return questionsType;
	}
	public void setQuestionsType(QuestionType questionsType) {
		this.questionsType = questionsType;
	}
	public String getQuestionLabel() {
		return questionText;
	}
	public void setQuestionLabel(String questionLabel) {
		this.questionText = questionLabel;
	}
	public List<String> getChoiceOptions() {
		return choiceOptions;
	}
	public void setChoiceOptions(List<String> choiceOptions) {
		this.choiceOptions = choiceOptions;
	}
	public repository.gw.enums.GeneratePolicyType getRequiredAt() {
		return requiredAt;
	}
	public void setRequiredAt(GeneratePolicyType requiredAt) {
		this.requiredAt = requiredAt;
	}
	public CPPCPClassCodeUWQuestions getDependentOnQuestion() {
		return dependentOnQuestion;
	}
	public void setDependentOnQuestion(CPPCPClassCodeUWQuestions dependentOnQuestion) {
		this.dependentOnQuestion = dependentOnQuestion;
	}
	public String isDependentOnAnswer() {
		return dependentOnAnswer;
	}
	public void setDependentOnAnswer(String dependentOnAnswer) {
		this.dependentOnAnswer = dependentOnAnswer;
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
	public String getFailureMessage() {
		return failureMessage;
	}
	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}
	public repository.gw.enums.BlockingAction getBlockingAction() {
		return blockingAction;
	}
	public void setBlockingAction(BlockingAction blockingAction) {
		this.blockingAction = blockingAction;
	}

	public String getQuestionText() {
		return questionText;
	}


	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}


	public boolean isSetDependantQuestion() {
		return setDependantQuestion;
	}


	public void setSetDependantQuestion(boolean setDependantQuestion) {
		this.setDependantQuestion = setDependantQuestion;
	}


	private List<String> getChoiceOptions(CPUWQuestions question) {
		if(question.getChoiceOptions().equals("-")) {
			return null;
		}
		String foo = question.getChoiceOptions();
		foo = foo.replace("\"", "");
		foo = foo.replace("\n", "");
		List<String> tempList = new ArrayList<String>();
		List<String> choices = Arrays.asList(question.getChoiceOptions().replace("\"", "").split("�"));
		for(String choice : choices) {
			if(!choice.equals("")) {
				tempList.add(choice.replace("\n", "").trim());
			}
		}
		return tempList;
	}

	public boolean isDependantQuestion() {
		return isDependantQuestion;
	}

	public void setDependantQuestion(boolean isDependantQuestion) {
		this.isDependantQuestion = isDependantQuestion;
	}

	public String getDependentOnAnswer() {
		
		return dependentOnAnswer;
	}






	public String getQuestionCode() {
		return questionCode;
	}






	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}






	public String getParentQuestionCode() {
		return parentQuestionCode;
	}






	public void setParentQuestionCode(String parentQuestionCode) {
		this.parentQuestionCode = parentQuestionCode;
	}






	public List<Integer> getDuplicateQuestions() {
		return duplicateQuestions;
	}






	public void setDuplicateQuestions(List<Integer> duplicateQuestions) {
		this.duplicateQuestions = duplicateQuestions;
	}






	public repository.gw.enums.FormatType getFormatType() {
		return formatType;
	}






	public void setFormatType(FormatType formatType) {
		this.formatType = formatType;
	}



	public repository.gw.enums.UnderwriterIssueType getUwIssueType() {
		return uwIssueType;
	}



	public void setUwIssueType(UnderwriterIssueType uwIssueType) {
		this.uwIssueType = uwIssueType;
	}

	public void setChildQuestions(List<CPPCPClassCodeUWQuestions> childQuestions) {
		this.childQuestions = childQuestions;
	}

	public List<CPPCPClassCodeUWQuestions> getChildQuestions() {
		return childQuestions;
	}







}
