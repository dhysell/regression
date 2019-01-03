package repository.gw.generate.custom;

import persistence.globaldatarepo.entities.GLClassCodes;
import persistence.globaldatarepo.entities.GLUnderwriterQuestions;
import persistence.globaldatarepo.helpers.GLClassCodeHelper;
import persistence.globaldatarepo.helpers.UWQuestionsHelper;
import repository.gw.enums.BasisType;

import java.util.ArrayList;
import java.util.List;

public class CPPGeneralLiabilityExposures {
	
	private String effectiveDate;
	private String expirationDate;
	private repository.gw.generate.custom.PolicyLocation location;
	private String classCode;
	private String description;
	private int basis = 100;
	private repository.gw.enums.BasisType basisType;
	
	List<repository.gw.generate.custom.CPPGLExposureUWQuestions> underWritingQuestions_Exposures = new ArrayList<repository.gw.generate.custom.CPPGLExposureUWQuestions>();
	
	public CPPGeneralLiabilityExposures() {
		//default constructor
	}
	
	public CPPGeneralLiabilityExposures(repository.gw.generate.custom.PolicyLocation location) throws Exception {
		this.location = location;
		GLClassCodes randExposure = GLClassCodeHelper.getRandomGLClassCode();
		this.classCode = randExposure.getCode();
		this.description = randExposure.getClassification();
		this.basisType = repository.gw.enums.BasisType.valueOfName(randExposure.getCodeExpBase());
		List<GLUnderwriterQuestions> uwQuestions = UWQuestionsHelper.getUWQuestionsClassCode(classCode);
		if (uwQuestions != null) {
			for (GLUnderwriterQuestions question : uwQuestions) {
				repository.gw.generate.custom.CPPGLExposureUWQuestions objectQuestion = new repository.gw.generate.custom.CPPGLExposureUWQuestions(question);
				underWritingQuestions_Exposures.add(objectQuestion);
			}
		}
	}
	
	public CPPGeneralLiabilityExposures(repository.gw.generate.custom.PolicyLocation location, String classCode) throws Exception {
		System.out.println("GETTING CLASS CODE QUESTIONS FROM DATABASE.....MAY TAKE A SECOND OR SIX!!");
		this.location = location;
		GLClassCodes exposure = GLClassCodeHelper.getGLClassCodeByCode(classCode);
		this.classCode = exposure.getCode();
		this.description = exposure.getClassification();
		this.basisType = repository.gw.enums.BasisType.valueOfName(exposure.getCodeExpBase());
		List<GLUnderwriterQuestions> uwQuestions = UWQuestionsHelper.getUWQuestionsClassCode(classCode);
		if (uwQuestions != null) {
			for (GLUnderwriterQuestions question : uwQuestions) {
				repository.gw.generate.custom.CPPGLExposureUWQuestions objectQuestion = new repository.gw.generate.custom.CPPGLExposureUWQuestions(question);
				underWritingQuestions_Exposures.add(objectQuestion);
			}
		}
	}
	
	

	public CPPGeneralLiabilityExposures(repository.gw.generate.custom.PolicyLocation location, String classCode, String description, int basis) throws Exception {
		this.location = location;
		GLClassCodes exposure = GLClassCodeHelper.getGLClassCodeByCode(classCode);
		this.classCode = exposure.getCode();
		this.description = exposure.getClassification();
		this.basisType = repository.gw.enums.BasisType.valueOfName(exposure.getCodeExpBase());
		this.basis = basis;
		List<GLUnderwriterQuestions> uwQuestions = UWQuestionsHelper.getUWQuestionsClassCode(classCode);
		if (uwQuestions != null) {
			for (GLUnderwriterQuestions question : uwQuestions) {
				repository.gw.generate.custom.CPPGLExposureUWQuestions objectQuestion = new repository.gw.generate.custom.CPPGLExposureUWQuestions(question);
				underWritingQuestions_Exposures.add(objectQuestion);
			}
		}
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public repository.gw.generate.custom.PolicyLocation getLocation() {
		return location;
	}

	public void setLocation(PolicyLocation location) {
		this.location = location;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getBasis() {
		return basis;
	}

	public void setBasis(int basis) {
		this.basis = basis;
	}

	public repository.gw.enums.BasisType getBasisType() {
		return basisType;
	}

	public void setBasisType(BasisType basisType) {
		this.basisType = basisType;
	}

	public List<repository.gw.generate.custom.CPPGLExposureUWQuestions> getUnderWritingQuestions() {
		return underWritingQuestions_Exposures;
	}

	public void setUnderWritingQuestions(List<CPPGLExposureUWQuestions> underWritingQuestions) {
		this.underWritingQuestions_Exposures = underWritingQuestions;
	}
	


}
