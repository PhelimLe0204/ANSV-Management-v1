package vn.ansv.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ImportErrorMapper implements RowMapper<ImportError> {

	public ImportError mapRow(ResultSet rs, int rowNum) throws SQLException {
		ImportError importError = new ImportError();
		
		importError.setError_id(rs.getInt("error_id"));
		importError.setStt(rs.getInt("stt"));
		importError.setProject_name(rs.getString("project_name"));
		importError.setImport_error(rs.getString("import_error"));
		importError.setImport_date(rs.getDate("import_date"));
		
		return importError;
	}

}
