package vn.ansv.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.ansv.Entity.ImportError;

@Service
public interface IErrorService {

	@Autowired
	public List<ImportError> getAllByDate();
	
}
