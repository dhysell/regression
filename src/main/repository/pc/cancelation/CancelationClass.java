package repository.pc.cancelation;

import org.testng.Assert;
import persistence.globaldatarepo.entities.Underwriters;
import repository.gw.enums.Cancellation.CancellationOptions;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.Cancellation.WithdrawTransaction;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import repository.pc.workorders.cancellation.StartCancellation;

import java.util.Date;

public class CancelationClass {

	
	//FIELDS
	private GeneratePolicy policy = null;
	private repository.pc.cancelation.CancelAs cancelAs = repository.pc.cancelation.CancelAs.Agent;
	private CancellationSourceReasonExplanation reason = CancellationSourceReasonExplanation.Photos;
	private String resonDescription = "Foo Boo Goo";
	private String refundMethod = "";
	private Date cancellationEffectiveDate = new Date();
	private Date cancellationScheduledDate = new Date();
	private int cancellationEffectiveDays = 0;
	private double deliquentAmount = 666.66;
	
	private CancellationOptions cancellationOptions = CancellationOptions.ScheduleCancel; 
	private WithdrawTransaction withdrawTransaction = WithdrawTransaction.WithDocuments;
	
	
	
	//METHODS
	public CancelationClass(GeneratePolicy policy) {
		this.policy = policy;
	}
		
	public repository.pc.cancelation.CancelAs getCancelAs() {
		return cancelAs;
	}
	public void setCancelAs(repository.pc.cancelation.CancelAs cancelAs) {
		this.cancelAs = cancelAs;
	}
	public CancellationSourceReasonExplanation getReason() {
		return reason;
	}
	public void setReason(CancellationSourceReasonExplanation reason) {
		this.reason = reason;
	}
	public String getResonDescription() {
		return resonDescription;
	}
	public void setResonDescription(String resonDescription) {
		this.resonDescription = resonDescription;
	}
	public String getRefundMethod() {
		return refundMethod;
	}
	public void setRefundMethod(String refundMethod) {
		this.refundMethod = refundMethod;
	}
	public Date getCancellationEffectiveDate() {
		return cancellationEffectiveDate;
	}
	public void setCancellationEffectiveDate(Date cancellationEffectiveDate) {
		this.cancellationEffectiveDate = cancellationEffectiveDate;
	}
	public Date getCancellationScheduledDate() {
		return cancellationScheduledDate;
	}
	public void setCancellationScheduledDate(Date cancellationScheduledDate) {
		this.cancellationScheduledDate = cancellationScheduledDate;
	}
	public int getCancellationEffectiveDays() {
		return cancellationEffectiveDays;
	}
	public void setCancellationEffectiveDays(int cancellationEffectiveDays) {
		this.cancellationEffectiveDays = cancellationEffectiveDays;
	}
	public double getDeliquentAmount() {
		return deliquentAmount;
	}
	public void setDeliquentAmount(double deliquentAmount) {
		this.deliquentAmount = deliquentAmount;
	}
	public CancellationOptions getCancellationOptions() {
		return cancellationOptions;
	}
	public void setCancellationOptions(CancellationOptions cancellationOptions) {
		this.cancellationOptions = cancellationOptions;
	}
	public WithdrawTransaction getWithdrawTransaction() {
		return withdrawTransaction;
	}
	public void setWithdrawTransaction(WithdrawTransaction withdrawTransaction) {
		this.withdrawTransaction = withdrawTransaction;
	}

	
	
	public void CancelPolicy(GeneratePolicy policy) {
		StartCancellation startCancelation = new StartCancellation(policy.getDriver());
		
		// TODO do this actually work? Will it work like I intend?
		if(this.getCancelAs().equals(repository.pc.cancelation.CancelAs.Agent)) {
			new Login(policy.getDriver()).loginAndSearchPolicy_asAgent(policy);
		} else {
			new Login(policy.getDriver()).loginAndSearchPolicy_asUW(policy);
		}
		
		
	}
	
	
	public static class Cancellation_Builder {
		private repository.pc.cancelation.CancelAs cancelAs_Builder = repository.pc.cancelation.CancelAs.Agent;
		private CancellationSourceReasonExplanation reason_Builder = CancellationSourceReasonExplanation.Photos;
		private String resonDescription_Builder = "Foo Boo Goo";
		private Date cancellationEffectiveDate_Builder = new Date();
		private Date cancellationScheduledDate_Builder = new Date();
		private int cancellationEffectiveDays_Builder = 0;
		private double deliquentAmount_Builder = 666.66;
		
		
		private CancellationOptions cancellationOptions = CancellationOptions.ScheduleCancel; 
		private WithdrawTransaction withdrawTransaction = WithdrawTransaction.WithDocuments;
		
		public Cancellation_Builder() {
			
		}
		
		public Cancellation_Builder cancelAsAgent() {
			this.cancelAs_Builder = repository.pc.cancelation.CancelAs.Agent;
			return this;
		}
		
		public Cancellation_Builder cancelAsUnderwriter(Underwriters uw) {
			this.cancelAs_Builder = CancelAs.Underwriter;
			
			return this;
		}
		
		public Cancellation_Builder withCancelatinReason(CancellationSourceReasonExplanation reason) {
			this.reason_Builder = reason;
			return this;
		}
		
		public Cancellation_Builder withCancelationReasonDescrition(String description) {
			this.resonDescription_Builder = description;
			return this;
		}
		
		public Cancellation_Builder withCancellationEffDate(Date effectiveDate) {
			this.cancellationEffectiveDate_Builder = effectiveDate;
			return this;
		}
		
		public Cancellation_Builder withCancellationScheduledDate(Date scheduledDate) {
			this.cancellationScheduledDate_Builder = scheduledDate;
			return this;
		}
		
		public Cancellation_Builder withCancellationNumberOfDays(int numdays) {
			this.cancellationEffectiveDays_Builder = numdays;
			return this;
		}
		
		public Cancellation_Builder withDeliquentAmount(double deliquentAmount) {
			this.deliquentAmount_Builder = deliquentAmount;
			return this;
		}
		
		public Cancellation_Builder cancelNow() {
			this.cancellationOptions = CancellationOptions.CancelNow;
			return this;
		}
		
		public Cancellation_Builder scheduleCancel() {
			this.cancellationOptions = CancellationOptions.ScheduleCancel;
			return this;
		}
		
		public Cancellation_Builder withdrawWithOUTDocument() {
			this.withdrawTransaction = WithdrawTransaction.WithOUTDocuments;
			return this;
		}
		
		public Cancellation_Builder withdrawWithDocuments() {
			this.withdrawTransaction = WithdrawTransaction.WithDocuments;
			return this;
		}
		
		public GeneratePolicy cancel(GeneratePolicy policy) {
            GuidewireHelpers helpers = new GuidewireHelpers(policy.getWebDriver());
            if (!helpers.getTypeToGenerate(policy).equals(GeneratePolicyType.PolicyIssued)) {
				Assert.fail("Policy Must Be Issued Inorder to be able to be canceled! WOULD YOU LIKE TO TRY AGAIN?");
			}
			new CancelationClass(policy).CancelPolicy(policy);
			return policy;
		}
		
	}//END Cancellation_Builder


    // TODO FIX
}
