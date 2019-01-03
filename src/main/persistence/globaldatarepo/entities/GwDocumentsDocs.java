package persistence.globaldatarepo.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "GW_DocumentsDocs", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class GwDocumentsDocs {

	private int docId;
	private String docName;
	private String docFormNumber;
	private String docApp;
	private Set<GwDocumentsDocsEventsXref> gwDocumentsDocsEventsXrefs = new HashSet<GwDocumentsDocsEventsXref>(0);

	public GwDocumentsDocs() {
	}

	public GwDocumentsDocs(int docId, String docName, String docApp) {
		this.docId = docId;
		this.docName = docName;
		this.docApp = docApp;
	}

	public GwDocumentsDocs(int docId, String docName, String docFormNumber, String docApp,
			Set<GwDocumentsDocsEventsXref> gwDocumentsDocsEventsXrefs) {
		this.docId = docId;
		this.docName = docName;
		this.docFormNumber = docFormNumber;
		this.docApp = docApp;
		this.gwDocumentsDocsEventsXrefs = gwDocumentsDocsEventsXrefs;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DocID", unique = true, nullable = false)
	public int getDocId() {
		return this.docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	@Column(name = "DocName", nullable = false, length = 150)
	public String getDocName() {
		return this.docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	@Column(name = "DocFormNumber", length = 50)
	public String getDocFormNumber() {
		return this.docFormNumber;
	}

	public void setDocFormNumber(String docFormNumber) {
		this.docFormNumber = docFormNumber;
	}

	@Column(name = "DocApp", nullable = false, length = 50)
	public String getDocApp() {
		return this.docApp;
	}

	public void setDocApp(String docApp) {
		this.docApp = docApp;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gwDocumentsDocs")
	public Set<GwDocumentsDocsEventsXref> getGwDocumentsDocsEventsXrefs() {
		return this.gwDocumentsDocsEventsXrefs;
	}

	public void setGwDocumentsDocsEventsXrefs(Set<GwDocumentsDocsEventsXref> gwDocumentsDocsEventsXrefs) {
		this.gwDocumentsDocsEventsXrefs = gwDocumentsDocsEventsXrefs;
	}

}
