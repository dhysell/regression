package repository.gw.generate.custom;

import persistence.globaldatarepo.entities.FormsCA;
import persistence.globaldatarepo.helpers.CAFormsHelpers;

public class Form {
	
	
	private String code;
	private String number;
	private String edition;
	private String name;
	private String products;
	private boolean submision;
	private boolean issuance;
	private boolean policyChange;
	private boolean cancellation;
	private boolean reinstatement;
	private boolean renewal;
	private boolean rewrite;
	private boolean rewriteNewAccount;
	private boolean available;
	private boolean canFormChange;
	private String policyLine;
	private String formInference;
	private String coverageConditionExclusion;
	
	
	
	public Form() {
		
	}
	
	public Form(String name) throws Exception {
		CAFormsHelpers.getCommercialAutoFormByName(name);
	}
	
	public Form(FormsCA caForm) {
		this.code = caForm.getCode();
		this.number = caForm.getNumber();
		this.edition = caForm.getEdition();
		this.name = caForm.getName();
		this.products = caForm.getProducts();
		this.submision = (caForm.getSubmision().equals("Yes"));
		this.issuance = (caForm.getIssuance().equals("Yes"));
		this.policyChange = (caForm.getPolicyChange().equals("Yes"));
		this.cancellation = (caForm.getCancellation().equals("Yes"));
		this.reinstatement = (caForm.getReinstatement().equals("Yes"));
		this.renewal = (caForm.getRenewal().equals("Yes"));
		this.rewrite = (caForm.getRewrite().equals("Yes"));
		this.rewriteNewAccount = (caForm.getRewriteNewAccount().equals("Yes"));
		this.available = (caForm.getAvailable().equals("Yes"));
		this.canFormChange = (caForm.getCanFormChange().equals("Yes"));
		this.policyLine = caForm.getPolicyLine();
		this.formInference = caForm.getFormInference();
		this.coverageConditionExclusion = caForm.getCoverageConditionExclusion();
	}
	
	
	
	
	
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProducts() {
		return products;
	}
	public void setProducts(String products) {
		this.products = products;
	}
	public boolean isSubmision() {
		return submision;
	}
	public void setSubmision(boolean submision) {
		this.submision = submision;
	}
	public boolean isIssuance() {
		return issuance;
	}
	public void setIssuance(boolean issuance) {
		this.issuance = issuance;
	}
	public boolean isPolicyChange() {
		return policyChange;
	}
	public void setPolicyChange(boolean policyChange) {
		this.policyChange = policyChange;
	}
	public boolean isCancellation() {
		return cancellation;
	}
	public void setCancellation(boolean cancellation) {
		this.cancellation = cancellation;
	}
	public boolean isReinstatement() {
		return reinstatement;
	}
	public void setReinstatement(boolean reinstatement) {
		this.reinstatement = reinstatement;
	}
	public boolean isRenewal() {
		return renewal;
	}
	public void setRenewal(boolean renewal) {
		this.renewal = renewal;
	}
	public boolean isRewrite() {
		return rewrite;
	}
	public void setRewrite(boolean rewrite) {
		this.rewrite = rewrite;
	}
	public boolean isRewriteNewAccount() {
		return rewriteNewAccount;
	}
	public void setRewriteNewAccount(boolean rewriteNewAccount) {
		this.rewriteNewAccount = rewriteNewAccount;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public boolean isCanFormChange() {
		return canFormChange;
	}
	public void setCanFormChange(boolean canFormChange) {
		this.canFormChange = canFormChange;
	}
	public String getPolicyLine() {
		return policyLine;
	}
	public void setPolicyLine(String policyLine) {
		this.policyLine = policyLine;
	}
	public String getFormInference() {
		return formInference;
	}
	public void setFormInference(String formInference) {
		this.formInference = formInference;
	}
	public String getCoverageConditionExclusion() {
		return coverageConditionExclusion;
	}
	public void setCoverageConditionExclusion(String coverageConditionExclusion) {
		this.coverageConditionExclusion = coverageConditionExclusion;
	}
	
	
	
	
	
	
	
	
	
	
	

}
