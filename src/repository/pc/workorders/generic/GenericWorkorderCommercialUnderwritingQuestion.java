package repository.pc.workorders.generic;


import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.generate.custom.CPPCPClassCodeUWQuestions;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;

import java.util.Date;
import java.util.Random;

public class GenericWorkorderCommercialUnderwritingQuestion extends BasePage {
	
	private WebDriver driver;

    public GenericWorkorderCommercialUnderwritingQuestion(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * @param question
     * @param CPPCPClassCodeUWQuestions question
     *                                  This Method will set a parent question to the answer needed to generate the child question if needed
     *                                  Fills out specific question to correct answer from the database.
     * @author jlarsen
     */
    public void setUnderwritingQuestion(CPPCPClassCodeUWQuestions question) {

        switch (question.getFormatType()) {
            case BooleanCheckbox:
                setCPCheckBoxQuestion(question);
                break;
            case BooleanRadio:
                setCPRadioQuestion(question);
                break;
            case ChoiceSelect:
                setCPSelectQuestion(question);
                break;
            case DateField:
                setCPDateQuestion(question);
                break;
            case IntegerField:
                setCPIntegerQuestion(question);
                break;
            case StringField:
            case StringTextBox:
                setCPTextBoxQuestion(question);
                break;
        }//end switch
    }//end setUnderwritingQuestion()

    /**
     * @param question
     * @param yesnochecked
     * @author jlarsen
     * This Method will set a parent question to the answer needed to generate the child question if needed
     * This method sets a specific question with an overridden answer
     */
    public void setUnderwritingQuestion(CPPCPClassCodeUWQuestions question, boolean yesnochecked) {

        switch (question.getFormatType()) {
            case BooleanCheckbox:
                setCPCheckBoxQuestion(question, yesnochecked);
                break;
            case BooleanRadio:
                setCPRadioQuestion(question, yesnochecked);
                break;
            default:
                break;
        }//end switch
    }//end setUnderwritingQuestion()


    /**
     * @param question
     * @param overrideText
     * @author jlarsen
     * This Method will set a parent question to the answer needed to generate the child question if needed
     * This method sets a specific question with an overridden answer
     */
    public void setUnderwritingQuestion(CPPCPClassCodeUWQuestions question, String overrideText) {

        switch (question.getFormatType()) {
            case ChoiceSelect:
                setCPSelectQuestion(question, overrideText);
                break;
            case IntegerField:
                setCPIntegerQuestion(question, overrideText);
                break;
            case StringField:
            case StringTextBox:
                setCPTextBoxQuestion(question, overrideText);
                break;
            case BooleanCheckbox:
                setCPCheckBoxQuestion(question, overrideText.equals("Checked"));
                break;
            case BooleanRadio:
                setCPRadioQuestion(question, overrideText.equals("Yes"));
                break;
            case DateField:
                setCPDateQuestion(question, overrideText);
                break;
            default:
                break;

        }//end switch
    }//end setUnderwritingQuestion()


    ///////////////////////
    // STRING TEXTBOXES  //
    ///////////////////////
    private void setCPTextBoxQuestion(CPPCPClassCodeUWQuestions question) {
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }
        //if question is a dependent question we need to base the xpath off of the parent question.
        if (question.isDependantQuestion()) {
            clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getDependentOnQuestion().getQuestionLabel()) + ")]/ancestor::tr/following-sibling::tr/descendant::div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div")));
        } else {
            clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div")));
        }
        
        WebElement textElement = find(By.xpath("//*[@name='c2']"));
        textElement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textElement.sendKeys("Little Bunny Foo Foo");
        clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]")));
    }

    private void setCPTextBoxQuestion(CPPCPClassCodeUWQuestions question, String overrideAnswer) {
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }
        //if question is a dependent question we need to base the xpath off of the parent question.
        if (question.isDependantQuestion()) {
            clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getDependentOnQuestion().getQuestionLabel()) + ")]/ancestor::tr/following-sibling::tr/descendant::div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div")));
        } else {
            clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div")));
        }
        
        WebElement textElement = find(By.xpath("//*[@name='c2']"));
        textElement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textElement.sendKeys(overrideAnswer);
        clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]")));
    }


    ///////////////////////
    // INTEGER TEXTBOXES  //
    ///////////////////////
    private void setCPIntegerQuestion(CPPCPClassCodeUWQuestions question) {
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }
        //if question is a dependent question we need to base the xpath off of the parent question.
        if (question.isDependantQuestion()) {
            clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getDependentOnQuestion().getQuestionLabel()) + ")]/ancestor::tr/following-sibling::tr/descendant::div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div")));
        } else {
            clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div")));
        }
        
        WebElement textElement = find(By.xpath("//*[@name='c2']"));
        textElement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textElement.sendKeys("1000");
        clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]")));
    }

    private void setCPIntegerQuestion(CPPCPClassCodeUWQuestions question, String integer) {
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }
        //if question is a dependent question we need to base the xpath off of the parent question.
        if (question.isDependantQuestion()) {
            clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getDependentOnQuestion().getQuestionLabel()) + ")]/ancestor::tr/following-sibling::tr/descendant::div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div")));
        } else {
            clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div")));
        }
        
        WebElement textElement = find(By.xpath("//*[@name='c2']"));
        textElement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textElement.sendKeys(integer);
        clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]")));
    }


    ///////////////////////
    // DATE TEXTBOXES  //
    ///////////////////////
    private void setCPDateQuestion(CPPCPClassCodeUWQuestions question) {
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }

        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        //if question is a dependent question we need to base the xpath off of the parent question.
        if (question.isDependantQuestion()) {
            clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getDependentOnQuestion().getQuestionLabel()) + ")]/ancestor::tr/following-sibling::tr/descendant::div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div")));
        } else {
            clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div")));
        }
        
        WebElement textElement = find(By.xpath("//*[@name='c2']"));
        textElement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textElement.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", currentSystemDate));
        clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]")));
    }

    private void setCPDateQuestion(CPPCPClassCodeUWQuestions question, String dateToOverride) {
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }

        //if question is a dependent question we need to base the xpath off of the parent question.
        if (question.isDependantQuestion()) {
            clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getDependentOnQuestion().getQuestionLabel()) + ")]/ancestor::tr/following-sibling::tr/descendant::div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div")));
        } else {
            clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div")));
        }
        
        WebElement textElement = find(By.xpath("//*[@name='c2']"));
        textElement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textElement.sendKeys(dateToOverride);
        clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]")));
    }


    ///////////////////////
    //  SELECT OPTIONS   //
    ///////////////////////
    private void setCPSelectQuestion(CPPCPClassCodeUWQuestions question) {
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }

        String selectItem = question.getCorrectAnswer();
        if (selectItem.equals("-")) {
            selectItem = question.getChoiceOptions().get(new Random().nextInt(question.getChoiceOptions().size()));
        }

        //if question is a dependent question we need to base the xpath off of the parent question.
        if (question.isDependantQuestion()) {
            clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getDependentOnQuestion().getQuestionLabel()) + ")]/ancestor::tr/following-sibling::tr/descendant::div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div")));
        } else {
            clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div")));
        }
        
        WebElement textElement = find(By.xpath("//*[@name='c2']"));
        textElement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textElement.sendKeys(selectItem);
        clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]")));
    }


    private void setCPSelectQuestion(CPPCPClassCodeUWQuestions question, String selectItem) {
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }

        if (selectItem.equals("-")) {
            selectItem = question.getChoiceOptions().get(new Random().nextInt(question.getChoiceOptions().size()));
        }

        //if question is a dependent question we need to base the xpath off of the parent question.
        if (question.isDependantQuestion()) {
            clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getDependentOnQuestion().getQuestionLabel()) + ")]/ancestor::tr/following-sibling::tr/descendant::div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div")));
        } else {
            clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div")));
        }
        
        WebElement textElement = find(By.xpath("//*[@name='c2']"));
        textElement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textElement.sendKeys(selectItem);
        clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]")));
    }


    ///////////////////////
    //   RADIO BUTTONS   //
    ///////////////////////
    private void setCPRadioQuestion(CPPCPClassCodeUWQuestions question) {
        //if is dependant question
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }
        
        if (question.getCorrectAnswer().equals("Yes")) {

            if (question.isDependantQuestion()) {
                clickAndHoldAndRelease(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getDependentOnQuestion().getQuestionLabel()) + ")]/ancestor::tr/following-sibling::tr/descendant::div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/descendant::label[contains(text(), 'Yes')]")));
            } else {
                clickAndHoldAndRelease(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/descendant::label[contains(text(), 'Yes')]")));
            }
        } else {
            if (question.isDependantQuestion()) {
                clickAndHoldAndRelease(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getDependentOnQuestion().getQuestionLabel()) + ")]/ancestor::tr/following-sibling::tr/descendant::div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/descendant::label[contains(text(), 'No')]")));
            } else {
                clickAndHoldAndRelease(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/descendant::label[contains(text(), 'No')]")));
            }
        }
        
    }

    private void setCPRadioQuestion(CPPCPClassCodeUWQuestions question, boolean yesno) {
        //if is dependant question
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }
        
        if (yesno) {

            if (question.isDependantQuestion()) {
                clickAndHoldAndRelease(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getDependentOnQuestion().getQuestionLabel()) + ")]/ancestor::tr/following-sibling::tr/descendant::div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/descendant::label[contains(text(), 'Yes')]")));
            } else {
                clickAndHoldAndRelease(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/descendant::label[contains(text(), 'Yes')]")));
            }
        } else {
            if (question.isDependantQuestion()) {
                clickAndHoldAndRelease(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getDependentOnQuestion().getQuestionLabel()) + ")]/ancestor::tr/following-sibling::tr/descendant::div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/descendant::label[contains(text(), 'No')]")));
            } else {
                clickAndHoldAndRelease(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/descendant::label[contains(text(), 'No')]")));
            }
        }
        
    }


    ///////////////////////
    //   CHECK BOXES     //
    ///////////////////////
    private void setCPCheckBoxQuestion(CPPCPClassCodeUWQuestions question) {
        //if is dependant question
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }

        WebElement checkbox = null;

        if (question.isDependantQuestion()) {
            checkbox = find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getDependentOnQuestion().getQuestionLabel()) + ")]/ancestor::tr/following-sibling::tr/descendant::div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div/img"));
        } else {
            checkbox = find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div/img"));
        }

        boolean checked = question.getCorrectAnswer().equals("Checked");

        if (checked && !checkbox.getAttribute("class").contains("-checked")) {
            clickAndHoldAndRelease(checkbox);
        } else if (checked && checkbox.getAttribute("class").contains("-checked")) {
            //do nothing
        } else if (!checked && !checkbox.getAttribute("class").contains("-checked")) {
            //do nothing
        } else if (!checked && checkbox.getAttribute("class").contains("-checked")) {
            clickAndHoldAndRelease(checkbox);
        }

        
    }

    private void setCPCheckBoxQuestion(CPPCPClassCodeUWQuestions question, boolean checked) {
        //if is dependant question
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }

        WebElement checkbox = null;

        if (question.isDependantQuestion()) {
            checkbox = find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getDependentOnQuestion().getQuestionLabel()) + ")]/ancestor::tr/following-sibling::tr/descendant::div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div/img"));
        } else {
            checkbox = find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]/parent::td/following-sibling::td/div/img"));
        }

        if (checked && !checkbox.getAttribute("class").contains("-checked")) {
            clickAndHoldAndRelease(checkbox);
        } else if (checked && checkbox.getAttribute("class").contains("-checked")) {
            //do nothing
        } else if (!checked && !checkbox.getAttribute("class").contains("-checked")) {
            //do nothing
        } else if (!checked && checkbox.getAttribute("class").contains("-checked")) {
            clickAndHoldAndRelease(checkbox);
        }

        
    }


}//END OF FILE
