package vn.ansv.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.ansv.Dao.ImportErrorDao;
import vn.ansv.Entity.ImportError;

@Service
public class ErrorServiceImpl implements IErrorService {

	@Autowired
	private ImportErrorDao importErrorDao;

	public List<ImportError> getAllByDate() {
		return importErrorDao.getAllByDate();
	}
	
}
