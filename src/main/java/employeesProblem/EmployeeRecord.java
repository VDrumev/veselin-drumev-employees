package employeesProblem;

import java.util.Date;

public class EmployeeRecord {

	String emplId;
	String projectId;
	Date from;
	Date to;
	
	public EmployeeRecord() {
		super();
	}
	
	public EmployeeRecord(String emplId, String projectId, Date from, Date to) {
		super();
		this.emplId = emplId.trim();
		this.projectId = projectId.trim();
		this.from = from;
		this.to = to;
	}

	public String getEmplId() {
		return emplId;
	}

	public void setEmplId(String emplId) {
		this.emplId = emplId.trim();
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId.trim();
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
		
		if ( to == null ) {
			this.to = new Date(); // if we received null then we automatically set this to Today's date
		}
	}
	
	
}
