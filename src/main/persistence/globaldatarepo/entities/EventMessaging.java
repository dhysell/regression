package persistence.globaldatarepo.entities;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "EventMessagingDetails", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class EventMessaging {

	private int id;
	private Date dateTimeOfCheck;
	private String server;
	private String center;
	private String destinationName;
	private String destinationStatus;
	private int destinationFailedNum;
	private int destinationRetryErrNum;
	private int destinationInFlightNum;
	private int destinationUnsentNum;
	private int destinationBatchedNum;
	private int destinationAwaitingRetryNum;

	public EventMessaging() {
	}

	public EventMessaging(int id, Date dateTimeOfCheck, String server,
			String center, String destinationName, String destinationStatus,
			int destinationFailedNum, int destinationRetryErrNum,
			int destinationInFlightNum, int destinationUnsentNum,
			int destinationBatchedNum, int destinationAwaitingRetryNum) {
		this.id = id;
		this.dateTimeOfCheck = dateTimeOfCheck;
		this.server = server;
		this.center = center;
		this.destinationName = destinationName;
		this.destinationStatus = destinationStatus;
		this.destinationFailedNum = destinationFailedNum;
		this.destinationRetryErrNum = destinationRetryErrNum;
		this.destinationInFlightNum = destinationInFlightNum;
		this.destinationUnsentNum = destinationUnsentNum;
		this.destinationBatchedNum = destinationBatchedNum;
		this.destinationAwaitingRetryNum = destinationAwaitingRetryNum;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DateTimeOfCheck", nullable = false, length = 23)
	public Date getDateTimeOfCheck() {
		return this.dateTimeOfCheck;
	}

	public void setDateTimeOfCheck(Date dateTimeOfCheck) {
		this.dateTimeOfCheck = dateTimeOfCheck;
	}

	@Column(name = "Server", nullable = false, length = 50)
	public String getServer() {
		return this.server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	@Column(name = "Center", nullable = false, length = 50)
	public String getCenter() {
		return this.center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	@Column(name = "DestinationName", nullable = false, length = 100)
	public String getDestinationName() {
		return this.destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	@Column(name = "DestinationStatus", nullable = false, length = 50)
	public String getDestinationStatus() {
		return this.destinationStatus;
	}

	public void setDestinationStatus(String destinationStatus) {
		this.destinationStatus = destinationStatus;
	}

	@Column(name = "DestinationFailedNum", nullable = false)
	public int getDestinationFailedNum() {
		return this.destinationFailedNum;
	}

	public void setDestinationFailedNum(int destinationFailedNum) {
		this.destinationFailedNum = destinationFailedNum;
	}

	@Column(name = "DestinationRetryErrNum", nullable = false)
	public int getDestinationRetryErrNum() {
		return this.destinationRetryErrNum;
	}

	public void setDestinationRetryErrNum(int destinationRetryErrNum) {
		this.destinationRetryErrNum = destinationRetryErrNum;
	}

	@Column(name = "DestinationInFlightNum", nullable = false)
	public int getDestinationInFlightNum() {
		return this.destinationInFlightNum;
	}

	public void setDestinationInFlightNum(int destinationInFlightNum) {
		this.destinationInFlightNum = destinationInFlightNum;
	}

	@Column(name = "DestinationUnsentNum", nullable = false)
	public int getDestinationUnsentNum() {
		return this.destinationUnsentNum;
	}

	public void setDestinationUnsentNum(int destinationUnsentNum) {
		this.destinationUnsentNum = destinationUnsentNum;
	}

	@Column(name = "DestinationBatchedNum", nullable = false)
	public int getDestinationBatchedNum() {
		return this.destinationBatchedNum;
	}

	public void setDestinationBatchedNum(int destinationBatchedNum) {
		this.destinationBatchedNum = destinationBatchedNum;
	}

	@Column(name = "DestinationAwaitingRetryNum", nullable = false)
	public int getDestinationAwaitingRetryNum() {
		return this.destinationAwaitingRetryNum;
	}

	public void setDestinationAwaitingRetryNum(int destinationAwaitingRetryNum) {
		this.destinationAwaitingRetryNum = destinationAwaitingRetryNum;
	}

}
