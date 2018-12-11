package repository.gw.helpers;

import repository.gw.elements.Guidewire8Select;

import java.util.ArrayList;
import java.util.List;

public class ClaimHelpers {

    /**
     * This method will make a random selection from a select box while excluding specified options.
     *
     * @param selectBox       - Guidwire8SelectBox element to be used.
     * @param doNotSelectList - List of items to be excluded from selection.
     * @return String - Selected option.
     */

    public static String setDynamicSelectBoxRestricted(Guidewire8Select selectBox, List<String> doNotSelectList) {
        List<String> options = selectBox.getList();
        List<String> validOptions = new ArrayList<>();
        boolean match = false;

        for (String option : options) {
            match = false;
            for (String doNotSelect : doNotSelectList) {
                if (option.trim().equalsIgnoreCase(doNotSelect.trim())) {
                    match = true;
                }
            }
            if (!match) {
                validOptions.add(option);
            }
        }

        String selection = validOptions.get(repository.gw.helpers.NumberUtils.generateRandomNumberInt(0, validOptions.size() - 1));

        selectBox.selectByVisibleText(selection);

        return selection;
    }

    /**
     * This method will make a random selection from a select box while excluding a single option.
     *
     * @param selectBox       - Guidwire8SelectBox element to be used.
     * @param doNotSelectList - Item to be excluded from selection.
     * @return String - Selected option.
     */

    public static String setDynamicSelectBoxRestricted(Guidewire8Select selectBox, String doNotSelect) {
        List<String> options = selectBox.getList();
        List<String> validOptions = new ArrayList<>();
        boolean match = false;

        for (String option : options) {

            match = option.trim().equalsIgnoreCase(doNotSelect.trim());

            if (!match) {
                validOptions.add(option);
            }
        }

        String selection = validOptions.get(NumberUtils.generateRandomNumberInt(0, validOptions.size() - 1));

        selectBox.selectByVisibleText(selection);

        return selection;
    }
}
