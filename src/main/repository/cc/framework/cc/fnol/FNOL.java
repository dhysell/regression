package repository.cc.framework.cc.fnol;

import repository.cc.framework.ClaimCenterActions;
import repository.cc.framework.utils.helpers.CCIDs;

public abstract class FNOL implements IFNOLActions {

    protected ClaimCenterActions actions;

    public FNOL(ClaimCenterActions actions) {
        this.actions = actions;
    }

    public void build() {
        this.actions.navigator.navigateTo(CCIDs.NewClaim.Navigation.STEPS);
    }
}
