package repository.gw.enums;

public enum CurrentlyHaveInsurance {
    Yes("Yes"),
    No("No"),
    YesWithFB("Yes with Farm Bureau of Idaho");
    
    private String haveInsurance;
    
     CurrentlyHaveInsurance(String haveInsurance){
        this.haveInsurance = haveInsurance;
    }
    
    public String getString(){
        return haveInsurance;
    }
}
