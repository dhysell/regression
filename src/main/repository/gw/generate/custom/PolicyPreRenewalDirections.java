package repository.gw.generate.custom;

import org.testng.Assert;
import repository.gw.enums.PreRenewalDirectionExplanation;

import java.util.ArrayList;
import java.util.List;

public class PolicyPreRenewalDirections {
	private List<PreRenewalDirection> directionList = new ArrayList<PreRenewalDirection>();

	public List<PreRenewalDirection> getDirectionList() {
		return directionList;
	}

	public void setDirectionList(List<PreRenewalDirection> directionList) {
		this.directionList = directionList;
	}

	public PolicyPreRenewalDirections(List<PreRenewalDirection> directionList) {
		this.directionList = directionList;
	}


	public boolean isAPreRenewalDirection(PreRenewalDirectionExplanation renewalDirection) {
		if(renewalDirection == null) {
			Assert.fail("ENUM PASSED IN WAS NULL");
		}
		if(directionList == null) {
			Assert.fail("DIRECTION LIST NULL");
		}
		for(PreRenewalDirection direction : directionList) {
			if(direction.getExplanationList() == null) {
				Assert.fail("EXPLANATION LIST WAS NULL");
			}
			for(PreRenewalDirection_Explanation explanation : direction.getExplanationList()) {
				if(explanation == null) {
					Assert.fail("EXPLANATION WAS NULL");
				}
				if(explanation.getCodeAndDescription().equals(renewalDirection)) {
					return true;
				}
			}
		}
		return false;
	}

	public PreRenewalDirection getPreRenewalDirection(PreRenewalDirectionExplanation renewalDirection) {
		for(PreRenewalDirection direction : directionList) {
			for(PreRenewalDirection_Explanation explanation : direction.getExplanationList()) {
				if(explanation.getCodeAndDescription().equals(renewalDirection)) {
					return direction;
				}
			}
		}
		return null;
	}








}
