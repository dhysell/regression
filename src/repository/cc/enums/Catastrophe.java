package repository.cc.enums;

import java.time.LocalDate;

public enum Catastrophe {

    WindOrHailApril2018(LocalDate.of(2018,04,7),LocalDate.of(2018,04,9),"0026 - April 7-10 Wind or Hail - 04/07/2018 - 04/09/2018"),
    WindMay2017(LocalDate.of(2017,05,23),LocalDate.of(2017,05,25),"0025 - May 23-25, 2017 Wind - 05/23/2017 - 05/25/2017"),
    SnowStormJanuary2017(LocalDate.of(2017,01,04),LocalDate.of(2017,01,11),"0024 - January 4-11, 2017- Snow Storm - 01/04/2017 - 01/11/2017"),
    WindstromApril2010(LocalDate.of(2010,04,27),LocalDate.of(2010,04,28),"0001 - Windstorm - 04/27/2010 - 04/28/2010");

    private LocalDate startDate;
    private LocalDate endDate;
    private String selectionText;

    Catastrophe(LocalDate startDate, LocalDate endDate, String selectionText) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.selectionText = selectionText;
    }

    public String getSelectionText() {
        return this.selectionText;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

}
