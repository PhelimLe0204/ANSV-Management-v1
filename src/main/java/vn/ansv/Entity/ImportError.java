package vn.ansv.Entity;

import java.sql.Date;

public class ImportError {

	private int error_id;
	private int stt;
	private String project_name;
	private String import_error;
	private Date import_date;
	
	public ImportError() {
		super();
	}
	
	public int getError_id() {
		return error_id;
	}
	
	public void setError_id(int error_id) {
		this.error_id = error_id;
	}
	
	public int getStt() {
		return stt;
	}

	public void setStt(int stt) {
		this.stt = stt;
	}

	public String getProject_name() {
		return project_name;
	}
	
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	
	public String getImport_error() {
		return import_error;
	}
	
	public void setImport_error(String import_error) {
		this.import_error = import_error;
	}
	
	public Date getImport_date() {
		return import_date;
	}
	
	public void setImport_date(Date import_date) {
		this.import_date = import_date;
	}
	
}
