package persistence.globaldatarepo.entities;

import javax.persistence.*;

@Entity
@Table(name = "GW_DocumentsDocsEventsXREF", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class GwDocumentsDocsEventsXref {

	private int docEventId;
	private GwDocumentEvents gwDocumentsEvents;
	private GwDocumentsDocs gwDocumentsDocs;

	public GwDocumentsDocsEventsXref() {
	}

	public GwDocumentsDocsEventsXref(int docEventId, GwDocumentEvents gwDocumentsEvents,
			GwDocumentsDocs gwDocumentsDocs) {
		this.docEventId = docEventId;
		this.gwDocumentsEvents = gwDocumentsEvents;
		this.gwDocumentsDocs = gwDocumentsDocs;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DocEventID", unique = true, nullable = false)
	public int getDocEventId() {
		return this.docEventId;
	}

	public void setDocEventId(int docEventId) {
		this.docEventId = docEventId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EventID", nullable = false)
	public GwDocumentEvents getGwDocumentsEvents() {
		return this.gwDocumentsEvents;
	}

	public void setGwDocumentsEvents(GwDocumentEvents gwDocumentsEvents) {
		this.gwDocumentsEvents = gwDocumentsEvents;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DocID", nullable = false)
	public GwDocumentsDocs getGwDocumentsDocs() {
		return this.gwDocumentsDocs;
	}

	public void setGwDocumentsDocs(GwDocumentsDocs gwDocumentsDocs) {
		this.gwDocumentsDocs = gwDocumentsDocs;
	}

}
