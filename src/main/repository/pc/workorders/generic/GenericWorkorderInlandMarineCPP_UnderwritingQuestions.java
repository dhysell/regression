package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.custom.CPPInlandMarineUWQuestion;
import repository.gw.helpers.StringsUtils;

import java.util.ArrayList;
import java.util.List;

public class GenericWorkorderInlandMarineCPP_UnderwritingQuestions extends BasePage {

    public GenericWorkorderInlandMarineCPP_UnderwritingQuestions(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void answerQuestions(List<CPPInlandMarineUWQuestion> questions, GeneratePolicyType policyJobType) {

        List<CPPInlandMarineUWQuestion> parentQuestions = new ArrayList<CPPInlandMarineUWQuestion>();
        List<CPPInlandMarineUWQuestion> childQuestions = new ArrayList<CPPInlandMarineUWQuestion>();

        // Initalize parent / child questions
        for (CPPInlandMarineUWQuestion q : questions) {
            if (q.getRequiredAt().equals(policyJobType)) {
                if (q.isDependantQuestion()) {
                    childQuestions.add(q);
                } else {
                    parentQuestions.add(q);
                }
            }
        }

        // Answer all parent questions
        for (CPPInlandMarineUWQuestion question : parentQuestions) {
            answerQuestion(question, true);
        }

        // Answer all applicable child questions
        for (CPPInlandMarineUWQuestion question : childQuestions) {
            CPPInlandMarineUWQuestion parentQuestion = question.getDependentOnQuestion();
            if (question.getDependantOnAnswer().equals(parentQuestion.getCorrectAnswer())) {
                answerQuestion(question, true);
            }
        }

    }

    private void answerQuestion(CPPInlandMarineUWQuestion question, boolean isCorrect) {

        switch (question.getFormatType()) {
            case BooleanRadio:
                answerBooleanRadioQuestion(question, isCorrect);
                break;
            case StringTextBox:
                answerStringTextBoxQuestion(question, question.getCorrectAnswer());
                break;
            case ChoiceSelect:
                break;
            case StringField:
                answerStringTextBoxQuestion(question, question.getCorrectAnswer());
                break;
            case BooleanCheckbox:
                break;
            case IntegerField:
                break;
            case DateField:
                break;
            default:
                break;
        }

    }

    private void answerBooleanRadioQuestion(CPPInlandMarineUWQuestion question, boolean isCorrect) {
        WebElement questionRow = find(By.xpath("//div[contains(text(), '" + question.getQuestionText().replace("\n", "").replace("\r", "") + "')]/parent::td/parent::tr"));

        WebElement radioYes = questionRow.findElement(By.xpath("./td/div/table/tbody/tr/td/label[contains(text(),'Yes')]/parent::td/input"));
        WebElement radioNo = questionRow.findElement(By.xpath("./td/div/table/tbody/tr/td/label[contains(text(),'No')]/parent::td/input"));

        if (isCorrect) {
            if (question.getCorrectAnswer().equals("Yes")) {
                clickAndHoldAndRelease(radioYes);
            } else {
                clickAndHoldAndRelease(radioNo);
            }
        } else {
            if (question.getCorrectAnswer().equals("Yes")) {
                clickAndHoldAndRelease(radioNo);
            } else {
                clickAndHoldAndRelease(radioYes);
            }
        }

    }

    private void answerStringTextBoxQuestion(CPPInlandMarineUWQuestion question, String answer) {
        //if question is a dependent question we need to base the xpath off of the parent question.
        if (question.isDependantQuestion()) {
            clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getDependentOnQuestion().getQuestionText()) + ")]/ancestor::tr/following-sibling::tr/descendant::div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionText()) + ")]/parent::td/following-sibling::td/div")));
        } else {
            clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionText()) + ")]/parent::td/following-sibling::td/div")));
        }
        
        if (finds(By.xpath("//*[@name='c2']")).isEmpty()) {
            
        }
        WebElement textElement = find(By.xpath("//*[@name='c2']"));
        textElement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textElement.sendKeys(answer);
        clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionText()) + ")]")));

    }

//    private void answerChoiceSelectQuestion(CPPInlandMarineUWQuestion question, String answer) {
//        WebElement questionRow = find(By.xpath("//div[contains(text(), '" + question.getQuestionText() + "')]/parent::td/parent::tr"));
//    }
//
//    private void answerBooleanCheckboxQuestion(CPPInlandMarineUWQuestion question, boolean isCorrect) {
//        WebElement questionRow = find(By.xpath("//div[contains(text(), '" + question.getQuestionText() + "')]/parent::td/parent::tr"));
//    }
//
//    private void answerIntegerFieldQuestion(CPPInlandMarineUWQuestion question, String answer) {
//        WebElement questionRow = find(By.xpath("//div[contains(text(), '" + question.getQuestionText() + "')]/parent::td/parent::tr"));
//    }
//
//    private void answerDateFieldQuestion(CPPInlandMarineUWQuestion question, String answer) {
//        WebElement questionRow = find(By.xpath("//div[contains(text(), '" + question.getQuestionText() + "')]/parent::td/parent::tr"));
//    }

}
