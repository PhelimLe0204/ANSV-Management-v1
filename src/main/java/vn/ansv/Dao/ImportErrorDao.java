package vn.ansv.Dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import vn.ansv.Entity.ImportError;
import vn.ansv.Entity.ImportErrorMapper;

@Repository
public class ImportErrorDao extends BaseDao {
	
	private LocalDateTime _now = LocalDateTime.now();

	// Insert lỗi import
	public void insert(ImportError importError) {
		String sql = "INSERT INTO import_error (stt, project_name, import_error, import_date) VALUES (?, ?, ?, ?)";
		_jdbcTemplate.update(sql, importError.getStt(), importError.getProject_name(), importError.getImport_error(), _now);
	}
	
	// Lấy ra toàn bộ lỗi import vừa thực hiện
	public List<ImportError> getAllByDate() {
		List<ImportError> list = new ArrayList<ImportError>();
		
		String sql = "SELECT * FROM import_error WHERE import_date = ?";
		list = _jdbcTemplate.query(sql, new ImportErrorMapper(), _now);
		return list;
	}
	
}
