package repository.gw.generate.custom;

import persistence.globaldatarepo.entities.GLUnderwriterQuestions;
import persistence.globaldatarepo.helpers.UWQuestionsHelper;
import repository.gw.enums.BlockingAction;
import repository.gw.enums.FormatType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.QuestionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CPPGLCoveragesUWQuestions {
	
	private int classCode;
	private String classCodeName;
	private int questionSequence;
	private repository.gw.enums.QuestionType questionsType;
	private String questionText;
	private List<String> choiceOptions;
	private repository.gw.enums.GeneratePolicyType requiredAt;
	private CPPGLCoveragesUWQuestions dependentOnQuestion = null;
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
	
	private repository.gw.enums.FormatType formatType = repository.gw.enums.FormatType.BooleanRadio;
	private String uwIssueType = null;
	
	
	

	public CPPGLCoveragesUWQuestions(GLUnderwriterQuestions question) throws Exception {
		this.classCode = Integer.valueOf(question.getClassCode());
		this.classCodeName = question.getClassCodeName();
		this.questionSequence = Integer.valueOf(question.getQuestionSequence());
		this.questionsType = repository.gw.enums.QuestionType.valueOf(question.getQuestionsType());
		this.questionText = question.getQuestionLabel().replace("\"", "").trim();
		this.choiceOptions = getChoiceOptions(question);
		this.requiredAt = (question.getRequiredAt().equals("QQ")) ? repository.gw.enums.GeneratePolicyType.QuickQuote : repository.gw.enums.GeneratePolicyType.FullApp;
		this.dependentOnQuestion = (question.getDependentOnQuestion().equals("-")) ? null : new CPPGLCoveragesUWQuestions(UWQuestionsHelper.getUWQuestionsQuestionParentCode(question.getParentQuestionCode()));
		this.dependentOnAnswer = question.getDependentOnAnswer();
		this.handleIncorrectAnswer = question.getHandleIncorrectAnswer().equals("Yes");
		this.correctAnswer = setCorrectAnswerString(question.getCorrectAnswer());
		this.failureMessage = question.getFailureMessage().replace("\"", "").trim();
		this.blockingAction = (question.getBlockingAction().equals("Block user")) ? repository.gw.enums.BlockingAction.Blockuser : repository.gw.enums.BlockingAction.NONE;
		this.isDependantQuestion = (this.dependentOnQuestion == null) ? false : true;
		this.questionCode = question.getQuestionCode();
		this.parentQuestionCode = question.getParentQuestionCode();
		this.duplicateQuestions = getDuplicateQuestions(question);
		this.formatType = setFormatType(question.getFormat());
		this.uwIssueType = question.getUWIssueType();
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
			return repository.gw.enums.FormatType.DateField;
		default:
			return null;
		}
	}
	
	
	private List<Integer> getDuplicateQuestions(GLUnderwriterQuestions question) {
		if(question.getDuplicateQuestion() == null || question.getDuplicateQuestion().equals("-")) {
			return null;
		}
		List<Integer> intList = new ArrayList<Integer>();
		for(String s : new ArrayList<String>(Arrays.asList(question.getDuplicateQuestion().replaceAll("[\\D]", "").split("(?<=\\G.{5})")))) intList.add(Integer.valueOf(s));
		
		intList.remove(0);
		
		if(intList.isEmpty()) {
			return null;
		} else {
			return intList;
		}
	}






	private String setCorrectAnswerString(String answer) {
		if(answer.equals("-")) {
			if(this.questionsType.equals(repository.gw.enums.QuestionType.Integer)) {
				return "1";
			} else if(this.questionsType.equals(repository.gw.enums.QuestionType.String)) {
				return "Little Bunny Foo Foo";
			}
		}
		return answer;

	}


	public CPPGLCoveragesUWQuestions(int classCode, String classCodeName, int questionSequence, repository.gw.enums.QuestionType questionsType, String questionLabel, List<String> choiceOptions, repository.gw.enums.GeneratePolicyType requiredAt, CPPGLCoveragesUWQuestions dependentOnQuestion, String dependentOnAnswer, boolean handleIncorrectAnswer, String correctAnswer, String failureMessage, repository.gw.enums.BlockingAction blockingAction) {
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


	public int getClassCode() {
		return classCode;
	}
	public void setClassCode(int classCode) {
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
	public CPPGLCoveragesUWQuestions getDependentOnQuestion() {
		return dependentOnQuestion;
	}
	public void setDependentOnQuestion(CPPGLCoveragesUWQuestions dependentOnQuestion) {
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


	private List<String> getChoiceOptions(GLUnderwriterQuestions question) {
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



	public String getUwIssueType() {
		return uwIssueType;
	}



	public void setUwIssueType(String uwIssueType) {
		this.uwIssueType = uwIssueType;
	}
}
