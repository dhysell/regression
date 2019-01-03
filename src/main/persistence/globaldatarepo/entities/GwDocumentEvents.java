package persistence.globaldatarepo.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "GW_DocumentsEvents", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class GwDocumentEvents {

	private int eventId;
	private String eventName;
	private String eventApp;
	private String eventProduct;
	private String eventLineOfBusiness;
	private Set<GwDocumentsDocsEventsXref> gwDocumentsDocsEventsXrefs = new HashSet<GwDocumentsDocsEventsXref>(0);

	public GwDocumentEvents() {
	}

	public GwDocumentEvents(int eventId, String eventName, String eventApp) {
		this.eventId = eventId;
		this.eventName = eventName;
		this.eventApp = eventApp;
	}

	public GwDocumentEvents(int eventId, String eventName, String eventApp, String eventProduct,
			String eventLineOfBusiness, Set<GwDocumentsDocsEventsXref> gwDocumentsDocsEventsXrefs) {
		this.eventId = eventId;
		this.eventName = eventName;
		this.eventApp = eventApp;
		this.eventProduct = eventProduct;
		this.eventLineOfBusiness = eventLineOfBusiness;
		this.gwDocumentsDocsEventsXrefs = gwDocumentsDocsEventsXrefs;
	}

	@Id

	@Column(name = "EventID", unique = true, nullable = false)
	public int getEventId() {
		return this.eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	@Column(name = "EventName", nullable = false, length = 150)
	public String getEventName() {
		return this.eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	@Column(name = "EventApp", nullable = false, length = 50)
	public String getEventApp() {
		return this.eventApp;
	}

	public void setEventApp(String eventApp) {
		this.eventApp = eventApp;
	}

	@Column(name = "EventProduct", length = 50)
	public String getEventProduct() {
		return this.eventProduct;
	}

	public void setEventProduct(String eventProduct) {
		this.eventProduct = eventProduct;
	}

	@Column(name = "EventLineOfBusiness", length = 50)
	public String getEventLineOfBusiness() {
		return this.eventLineOfBusiness;
	}

	public void setEventLineOfBusiness(String eventLineOfBusiness) {
		this.eventLineOfBusiness = eventLineOfBusiness;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gwDocumentsEvents")
	public Set<GwDocumentsDocsEventsXref> getGwDocumentsDocsEventsXrefs() {
		return this.gwDocumentsDocsEventsXrefs;
	}

	public void setGwDocumentsDocsEventsXrefs(Set<GwDocumentsDocsEventsXref> gwDocumentsDocsEventsXrefs) {
		this.gwDocumentsDocsEventsXrefs = gwDocumentsDocsEventsXrefs;
	}

}
