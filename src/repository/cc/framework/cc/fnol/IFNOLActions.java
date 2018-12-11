package repository.cc.framework.cc.fnol;

public interface IFNOLActions {
    void create();

    void searchOrCreatePolicy(String policyNumber);

    void fillBasicInformation();

    void fillLossDetailInformation();

}
