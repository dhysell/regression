package persistence;

import java.util.List;

import org.testng.annotations.Test;

import persistence.globaldatarepo.entities.TransitionRenewalTeam;
import persistence.globaldatarepo.helpers.TransitionRenewalTeamHelper;

public class TransitionRenewalTeamTest {

	@Test()
	public void testGetRandomTR() throws Exception {
		
		TransitionRenewalTeam randomTR = TransitionRenewalTeamHelper.getRandomTRMember();
		System.out.println(randomTR.getTrfirstName() + " " +  randomTR.getTrlastName());
		System.out.println(randomTR.getTrpassword());
	}
	
	@Test()
	public void testGetTRMemberWithLoadFactor() throws Exception {
		
		TransitionRenewalTeam TRMember = TransitionRenewalTeamHelper.getRandomTRMemberWithLoadFactorGreaterThanZero();
		System.out.println(TRMember.getTrfirstName() + " " +  TRMember.getTrlastName());
		System.out.println(TRMember.getTrpassword());
	}
	
	@Test()
	public void testGetTRMembersWithLoadFactor() throws Exception {
		
		List<TransitionRenewalTeam> TRMembers = TransitionRenewalTeamHelper.getTRMembersWithLoadFactorGreaterThanZero();
		for (TransitionRenewalTeam TRMember : TRMembers){
			System.out.println(TRMember.getTrfirstName() + " " +  TRMember.getTrlastName());
			System.out.println(TRMember.getTrpassword());
		}
	}

}
