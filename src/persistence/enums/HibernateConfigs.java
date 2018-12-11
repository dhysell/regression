package persistence.enums;

public enum HibernateConfigs {
	GlobalDataRepository("qawizproglobaldatarepository.hibernate.cfg.xml"),
	GuidewireAB8DEV("guidewireAB8DEV.hibernate.cfg.xml"),
	GuidewirePC8DEV("guidewirePC8DEV.hibernate.cfg.xml"),
	GuidewirePC8DEV2("guidewirePC8DEV2.hibernate.cfg.xml"),
	GuidewirePC8IT("guidewirePC8IT.hibernate.cfg.xml"),
	GuidewirePC8IT2("guidewirePC8IT2.hibernate.cfg.xml"),
	GuidewirePC8QA("guidewirePC8QA.hibernate.cfg.xml"),	
	GuidewirePC8QA2("guidewirePC8QA2.hibernate.cfg.xml"),
	GuidewirePC8UAT("guidewirePC8UAT.hibernate.cfg.xml"),
	GuidewirePC8REGR01("guidewirePC8REGR01.hibernate.cfg.xml"),
	GuidewirePC8REGR02("guidewirePC8REGR02.hibernate.cfg.xml"),
	GuidewirePC8REGR03("guidewirePC8REGR03.hibernate.cfg.xml"),
	GuidewirePC8REGR04("guidewirePC8REGR04.hibernate.cfg.xml");
	
	String value;
	
	private HibernateConfigs(String type){
		this.value = type;
	}
	
	public String getPathValue(){
		return "persistence/config/" + this.value;
	}
	
	public String getValue() {
		return this.value;
	}
}
