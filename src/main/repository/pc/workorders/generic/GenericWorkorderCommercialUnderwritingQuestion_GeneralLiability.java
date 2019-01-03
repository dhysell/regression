package repository.pc.workorders.generic;


import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.generate.custom.CPPGLExposureUWQuestions;
import repository.gw.generate.custom.CPPGeneralLiabilityExposures;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.Random;

public class GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    private String getQuestionXPath(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question) {
        return "//label[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(exposure.getDescription()) + ")]/ancestor::table[2]/parent::div/parent::td/parent::tr/following-sibling::tr/descendant::div[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionText()) + ")]";
    }

    private String getQuestionXPath_WithParent(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question) {
        return "//label[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(exposure.getDescription()) + ")]/ancestor::table[2]/parent::div/parent::td/parent::tr/following-sibling::tr/descendant::div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getDependentOnQuestion().getQuestionText()) + ")]/parent::td/parent::tr/following-sibling::tr/child::td/div[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionText()) + ")]";
    }

    public String getIncorrectAnswer(CPPGLExposureUWQuestions question) {
        switch (question.getFormatType()) {
            case BooleanCheckbox:
                if (question.getCorrectAnswer().equals("-")) {
                    return "Checked";
                } else {
                    return "-";
                }
            case BooleanRadio:
                if (question.getCorrectAnswer().equals("Yes")) {
                    return "No";
                } else {
                    return "Yes";
                }
            case ChoiceSelect:
                if (question.getCorrectAnswer().equals("-")) {
                    return question.getChoiceOptions().get(NumberUtils.generateRandomNumberInt(0, question.getChoiceOptions().size() - 1));
                } else {
                    int indexof = question.getChoiceOptions().indexOf(question.getCorrectAnswer());
                    if (indexof == 0) {
                        return question.getChoiceOptions().get(1);
                    } else {
                        return question.getChoiceOptions().get(0);
                    }
                }
            case DateField:
            case IntegerField:
            case StringField:
            case StringTextBox:
            default:
                return "Littel Bunny Foo Foo";
        }//end switch
    }

    /**
     * @param question
     * @param CPPCPClassCodeUWQuestions question
     *                                  This Method will set a parent question to the answer needed to generate the child question if needed
     *                                  Fills out specific question to correct answer from the database.
     * @author jlarsen
     */
    public void setUnderwritingQuestion(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question) {

        switch (question.getFormatType()) {
            case BooleanCheckbox:
                setCPCheckBoxQuestion(exposure, question);
                break;
            case BooleanRadio:
                setCPRadioQuestion(exposure, question);
                break;
            case ChoiceSelect:
                setCPSelectQuestion(exposure, question);
                break;
            case DateField:
                setCPDateQuestion(exposure, question);
                break;
            case IntegerField:
                setCPIntegerQuestion(exposure, question);
                break;
            case StringField:
            case StringTextBox:
                setCPTextBoxQuestion(exposure, question);
                break;
        }//end switch

        specialCaseQuestions(exposure, question);


    }//end setUnderwritingQuestion()

    private void specialCaseQuestions(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question) {
        switch (question.getQuestionCode()) {
            case "CG97047Q2200":
            case "CG97050Q2200":
                setUnderwritingQuestion(exposure, question, "10");
                break;
            case "CG98306Q100":
                setUnderwritingQuestion(exposure, question, "1-5%");
                break;
        }
    }

    /**
     * @param question
     * @param yesnochecked
     * @author jlarsen
     * This Method will set a parent question to the answer needed to generate the child question if needed
     * This method sets a specific question with an overridden answer
     */
    public void setUnderwritingQuestion(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question, boolean yesnochecked) {

        switch (question.getFormatType()) {
            case BooleanCheckbox:
                setCPCheckBoxQuestion(exposure, question, yesnochecked);
                break;
            case BooleanRadio:
                setCPRadioQuestion(exposure, question, yesnochecked);
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
    public void setUnderwritingQuestion(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question, String overrideText) {

        switch (question.getFormatType()) {
            case ChoiceSelect:
                setCPSelectQuestion(exposure, question, overrideText);
                break;
            case IntegerField:
                setCPIntegerQuestion(exposure, question, overrideText);
                break;
            case StringField:
            case StringTextBox:
                setCPTextBoxQuestion(exposure, question, overrideText);
                break;
            case BooleanCheckbox:
                setCPCheckBoxQuestion(exposure, question, overrideText.equals("Checked") || overrideText.equals("Yes"));
                break;
            case BooleanRadio:
                setCPRadioQuestion(exposure, question, overrideText.equals("Yes"));
                break;
            case DateField:
                setCPDateQuestion(exposure, question, overrideText);
                break;
            default:
                break;

        }//end switch
    }//end setUnderwritingQuestion()

    public void setUnderwritingQuesiton_AndChildQuestions(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question) {
        setUnderwritingQuestion(exposure, question);
        for (CPPGLExposureUWQuestions childQuestion : question.getChildrenQuestions()) {
            setUnderwritingQuestion(exposure, childQuestion);
        }
    }

    public void setUnderwritingQuesiton_AndChildQuestions(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question, String overrideText) {
        setUnderwritingQuestion(exposure, question, overrideText);
        for (CPPGLExposureUWQuestions childQuestion : question.getChildrenQuestions()) {
            setUnderwritingQuestion(exposure, childQuestion);
        }
    }


    ///////////////////////
    // STRING TEXTBOXES  //
    ///////////////////////
    private void setCPTextBoxQuestion(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question) {
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(exposure, question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }
        //if question is a dependent question we need to base the xpath off of the parent question.
//		String xpathString = ((question.isDependantQuestion()) ? getQuestionXPath_WithParent(exposure, question) : getQuestionXPath(exposure, question)) + "/parent::td/following-sibling::td/div";
//		List<WebElement> foo = finds(By.xpath(((question.isDependantQuestion()) ? getQuestionXPath_WithParent(exposure, question) : getQuestionXPath(exposure, question)) + "/parent::td/following-sibling::td/div"));
        
        clickWhenClickable(find(By.xpath(((question.isDependantQuestion()) ? getQuestionXPath_WithParent(exposure, question) : getQuestionXPath(exposure, question)) + "/parent::td/following-sibling::td/div")));
        
        WebElement textElement = find(By.xpath("//*[@name='c2']"));
        textElement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textElement.sendKeys("Little Bunny Foo Foo");
        clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]")));
    }

    private void setCPTextBoxQuestion(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question, String overrideAnswer) {
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(exposure, question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }
        //if question is a dependent question we need to base the xpath off of the parent question.
        clickWhenClickable(find(By.xpath(((question.isDependantQuestion()) ? getQuestionXPath_WithParent(exposure, question) : getQuestionXPath(exposure, question)) + "/parent::td/following-sibling::td/div")));
        
        WebElement textElement = find(By.xpath("//*[@name='c2']"));
        textElement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textElement.sendKeys(overrideAnswer);
        clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]")));
    }


    ///////////////////////
    // INTEGER TEXTBOXES  //
    ///////////////////////
    private void setCPIntegerQuestion(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question) {
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(exposure, question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }
        //if question is a dependent question we need to base the xpath off of the parent question.
        clickWhenClickable(find(By.xpath(((question.isDependantQuestion()) ? getQuestionXPath_WithParent(exposure, question) : getQuestionXPath(exposure, question)) + "/parent::td/following-sibling::td/div")));
        
        WebElement textElement = find(By.xpath("//*[@name='c2']"));
        textElement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textElement.sendKeys("1000");
        clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]")));
    }

    private void setCPIntegerQuestion(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question, String integer) {
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(exposure, question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }
        //if question is a dependent question we need to base the xpath off of the parent question.
        clickWhenClickable(find(By.xpath(((question.isDependantQuestion()) ? getQuestionXPath_WithParent(exposure, question) : getQuestionXPath(exposure, question)) + "/parent::td/following-sibling::td/div")));
        
        WebElement textElement = find(By.xpath("//*[@name='c2']"));
        textElement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textElement.sendKeys(integer);
        clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]")));
    }


    ///////////////////////
    // DATE TEXTBOXES  //
    ///////////////////////
    private void setCPDateQuestion(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question) {
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(exposure, question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }

        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        //if question is a dependent question we need to base the xpath off of the parent question.
        clickWhenClickable(find(By.xpath(((question.isDependantQuestion()) ? getQuestionXPath_WithParent(exposure, question) : getQuestionXPath(exposure, question)) + "/parent::td/following-sibling::td/div")));
        
        WebElement textElement = find(By.xpath("//*[@name='c2']"));
        textElement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textElement.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", currentSystemDate));
        clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]")));
    }

    private void setCPDateQuestion(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question, String dateToOverride) {
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(exposure, question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }

        //if question is a dependent question we need to base the xpath off of the parent question.
        clickWhenClickable(find(By.xpath(((question.isDependantQuestion()) ? getQuestionXPath_WithParent(exposure, question) : getQuestionXPath(exposure, question)) + "/parent::td/following-sibling::td/div")));
        
        WebElement textElement = find(By.xpath("//*[@name='c2']"));
        textElement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textElement.sendKeys(dateToOverride);
        clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]")));
    }


    ///////////////////////
    //  SELECT OPTIONS   //
    ///////////////////////
    private void setCPSelectQuestion(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question) {
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(exposure, question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }

        String selectItem = question.getCorrectAnswer();
        if (selectItem.equals("-")) {
            selectItem = question.getChoiceOptions().get(new Random().nextInt(question.getChoiceOptions().size()));
        }

        //if question is a dependent question we need to base the xpath off of the parent question.
        clickWhenClickable(find(By.xpath(((question.isDependantQuestion()) ? getQuestionXPath_WithParent(exposure, question) : getQuestionXPath(exposure, question)) + "/parent::td/following-sibling::td/div")));
        
        WebElement textElement = find(By.xpath("//*[@name='c2']"));
        textElement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textElement.sendKeys(selectItem.replace("�", "").trim());
        clickWhenClickable(find(By.xpath("//div[contains(text()," + StringsUtils.xPathSpecialCharacterEscape(question.getQuestionLabel()) + ")]")));
    }


    private void setCPSelectQuestion(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question, String selectItem) {
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(exposure, question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }

        if (selectItem.equals("-")) {
            selectItem = question.getChoiceOptions().get(new Random().nextInt(question.getChoiceOptions().size()));
        }

        //if question is a dependent question we need to base the xpath off of the parent question.
        clickWhenClickable(find(By.xpath(((question.isDependantQuestion()) ? getQuestionXPath_WithParent(exposure, question) : getQuestionXPath(exposure, question)) + "/parent::td/following-sibling::td/div")));
        
        WebElement textElement = find(By.xpath("//*[@name='c2']"));
        textElement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textElement.sendKeys(selectItem.replace("�", "").trim());
        clickWhenClickable(find(By.xpath(getQuestionXPath(exposure, question))));
    }


    ///////////////////////
    //   RADIO BUTTONS   //
    ///////////////////////
    private void setCPRadioQuestion(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question) {
        //if is dependant question
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(exposure, question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }
        
        //re-get table to avoid stale element exception.
        WebElement tableToInteractWith = find(By.xpath("//label[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(exposure.getDescription()) + ")]/ancestor::div[@class = 'x-container x-container-default x-table-layout-ct']/ancestor::tr/following-sibling::tr/descendant::div[contains(@id, 'QuestionSetLV')]"));
        String questionDependentOnText = (question.isDependantQuestion()) ? question.getDependentOnQuestion().getQuestionText() : null;

        tableUtils.setRadioValueForCellInsideTable(tableToInteractWith, questionDependentOnText, question.getQuestionText(), question.getCorrectAnswer().equals("Yes"));
    }

    private void setCPRadioQuestion(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question, boolean yesno) {
        //if is dependant question
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(exposure, question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }
        
        //re-get table to avoid stale element exception.
        WebElement tableToInteractWith = find(By.xpath("//label[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(exposure.getDescription()) + ")]/ancestor::div[@class = 'x-container x-container-default x-table-layout-ct']/ancestor::tr/following-sibling::tr/descendant::div[contains(@id, 'QuestionSetLV')]"));
        String questionDependentOnText = null;
        String questionText = null;
        if (question.isDependantQuestion()) {
            questionDependentOnText = question.getDependentOnQuestion().getQuestionText();
        }
        questionText = question.getQuestionText();
        tableUtils.setRadioValueForCellInsideTable(tableToInteractWith, questionDependentOnText, questionText, yesno);
    }


    ///////////////////////
    //   CHECK BOXES     //
    ///////////////////////
    private void setCPCheckBoxQuestion(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question) {
        //if is dependant question
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(exposure, question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }

        WebElement checkbox = null;

        checkbox = (find(By.xpath(((question.isDependantQuestion()) ? getQuestionXPath_WithParent(exposure, question) : getQuestionXPath(exposure, question)) + "/parent::td/following-sibling::td/div/img")));

        boolean checked = question.getCorrectAnswer().equals("Checked") || question.getCorrectAnswer().equals("Yes");

        if (checked && !checkbox.getAttribute("class").contains("-checked")) {
            tableUtils.setCheckboxInTableByText(find(By.xpath("//label[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(exposure.getDescription()) + ")]/ancestor::div[@class = 'x-container x-container-default x-table-layout-ct']/ancestor::tr/following-sibling::tr/descendant::div[contains(@id, 'QuestionSetLV')]")), question.getQuestionText(), checked);
        } else if (checked && checkbox.getAttribute("class").contains("-checked")) {
            //do nothing
        } else if (!checked && !checkbox.getAttribute("class").contains("-checked")) {
            //do nothing
        } else if (!checked && checkbox.getAttribute("class").contains("-checked")) {
            tableUtils.setCheckboxInTableByText(find(By.xpath("//label[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(exposure.getDescription()) + ")]/ancestor::div[@class = 'x-container x-container-default x-table-layout-ct']/ancestor::tr/following-sibling::tr/descendant::div[contains(@id, 'QuestionSetLV')]")), question.getQuestionText(), checked);
        }

        
    }

    private void setCPCheckBoxQuestion(CPPGeneralLiabilityExposures exposure, CPPGLExposureUWQuestions question, boolean checked) {
        //if is dependant question
        if (question.isDependantQuestion()) {
            setUnderwritingQuestion(exposure, question.getDependentOnQuestion(), question.getDependentOnAnswer());
        }

        WebElement checkbox = null;

        checkbox = (find(By.xpath(((question.isDependantQuestion()) ? getQuestionXPath_WithParent(exposure, question) : getQuestionXPath(exposure, question)) + "/parent::td/following-sibling::td/div/img")));

        if (checked && !checkbox.getAttribute("class").contains("-checked")) {
            tableUtils.setCheckboxInTableByText(find(By.xpath("//label[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(exposure.getDescription()) + ")]/ancestor::div[@class = 'x-container x-container-default x-table-layout-ct']/ancestor::tr/following-sibling::tr/descendant::div[contains(@id, 'QuestionSetLV')]")), question.getQuestionText(), checked);
        } else if (checked && checkbox.getAttribute("class").contains("-checked")) {
            //do nothing
        } else if (!checked && !checkbox.getAttribute("class").contains("-checked")) {
            //do nothing
        } else if (!checked && checkbox.getAttribute("class").contains("-checked")) {
            tableUtils.setCheckboxInTableByText(find(By.xpath("//label[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(exposure.getDescription()) + ")]/ancestor::div[@class = 'x-container x-container-default x-table-layout-ct']/ancestor::tr/following-sibling::tr/descendant::div[contains(@id, 'QuestionSetLV')]")), question.getQuestionText(), checked);
        }

        
    }


}
