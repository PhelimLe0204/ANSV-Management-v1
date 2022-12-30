package vn.ansv.Controller.PM;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import vn.ansv.Entity.Project;

@Controller(value = "PM_HomeController")
@RequestMapping("PM")
public class ProjectManagerController extends ProjectManagerBaseController {

	@RequestMapping(value = "/import_file")
	public String importFile() {
		return "view_import";
	}

	// Hàm lấy số tuần
	public int getWeekOfYear(Date date) {

		int current_year = Calendar.getInstance().get(Calendar.YEAR); // Get the curent year

		LocalDate dateTest = LocalDate.of(current_year, Month.FEBRUARY, 01);
		// Xác định ngày đầu tiên trong năm
		LocalDate firstDayOfYear = dateTest.with(TemporalAdjusters.firstDayOfYear());
		System.out.println("--- firstDayOfYear: " + firstDayOfYear);
		DayOfWeek dayOfWeek = firstDayOfYear.getDayOfWeek(); // Xác định ngày thứ mấy trong tuần
		// In ra giá trị nằm trong khoảng từ 1 đến 7 tương ứng thứ 2 - Chủ nhật
		System.out.println("--- dayOfWeek.getValue(): " + dayOfWeek.getValue());
		System.out.println("--- First week of this year has: " + (8 - dayOfWeek.getValue()) + " days.");

		/* === Determine week of year === */
		Calendar calendar = new GregorianCalendar();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		// Xác định tuần đầu tiên trong năm có mấy ngày
		calendar.setMinimalDaysInFirstWeek(8 - dayOfWeek.getValue());
		calendar.setTime(date);
		int week = calendar.get(Calendar.WEEK_OF_YEAR);

		return week;
	}

	@RequestMapping(value = "/upload_project/{file_name}", method = RequestMethod.GET)
	public String processExcel2003(@PathVariable String file_name, Model model, HttpServletRequest request)
			throws IOException {
//		String file_name = request.getParameter("file_import_name");
		Date now = new Date();
		int current_week = getWeekOfYear(now); // Gọi hàm lấy số tuần => Lấy số tuần hiện tại

		// Thực hiện import
		String status_import = _importServiceImpl.importProject(file_name, current_week);

		if (status_import == "1") {
			model.addAttribute("import_error", _errorService.getAllByDate());
			return "error_notification/import_error";
		}
//		_importServiceImpl.exportProject(19, 1);

		int current_year = Calendar.getInstance().get(Calendar.YEAR); // Get the curent year
		return "redirect:/chief/dashboard/" + current_week + "_" + current_year;
	}

	@RequestMapping(value = { "/dashboard/{week}_{year}" }, method = RequestMethod.GET)
	public ModelAndView PmHome(@PathVariable int week, @PathVariable int year, HttpSession session) {
		String pic_id = (String) session.getAttribute("user_id");
		InitPM(week, year, pic_id);

		Date now = new Date();
		int current_week = getWeekOfYear(now); // Gọi hàm lấy số tuần => Lấy số tuần hiện tại

		// Dữ liệu khái quát hiển thị lên dashboard (datatable)
		_mvShare.addObject("project_table_pic", _projectService.getDashboardPM(week, year, pic_id));
		_mvShare.addObject("current_week", current_week);
		_mvShare.setViewName("PM/pm_dashboard");
		return _mvShare;
	}

	@RequestMapping(value = { "/detail/{week}_{year}_{id}" }, method = RequestMethod.GET)
	public ModelAndView PmDetail(@PathVariable int week, @PathVariable int year, @PathVariable int id,
			HttpSession session) {
		String pic_id = (String) session.getAttribute("user_id");
		InitPM(week, year, pic_id);
		_mvShare.addObject("detail", _projectService.getByIdAndPic(id, pic_id));
		_mvShare.setViewName("PM/detail");
		return _mvShare;
	}

	// Thực thi xoá dự án
	@RequestMapping("/delete_project/{week}_{year}_{id}")
	public String doDeleteProject(@PathVariable int week, @PathVariable int year, @PathVariable int id) {
		_picService.delete(id);
		_projectService.delete(id);
		Date now = new Date();
		int current_week = getWeekOfYear(now) - 1; // Gọi hàm lấy số tuần => Lấy số tuần trước đó
		int current_year = Calendar.getInstance().get(Calendar.YEAR); // Get the curent year
		String week_link = Integer.toString(current_week);
		if (current_week < 10) {
			week_link = "0" + current_week;
		}

		// Sau khi insert thành công sẽ điều hướng về tuần chứa báo cáo đó
		return "redirect:/PM/dashboard/" + week_link + "_" + current_year;
	}

	// Link đến form update dự án
	@RequestMapping(value = { "/update_project/{week}_{year}_{id}" }, method = RequestMethod.GET)
	public ModelAndView PmUpdateProject(@PathVariable int week, @PathVariable int year, @PathVariable int id,
			HttpSession session, Model model) {
		String pic_id = (String) session.getAttribute("user_id");
		FormPM();

		Project project = new Project();

		project = _projectService.getMorebyId(id, pic_id);
		model.addAttribute("projectUpdatePM", project);

		_mvShare.setViewName("PM/update");
		return _mvShare;
	}

	// Thực thi update dự án
	@RequestMapping("/updateProjectPM/{week}_{year}_{id}")
	public String doUpdateProject(@ModelAttribute("projectUpdatePM") Project projectUpdatePM, @PathVariable int week,
			@PathVariable int year, @PathVariable int id, HttpSession session, Model model) {

		Date now = new Date();
		int current_week = getWeekOfYear(now); // Gọi hàm lấy số tuần => Lấy số tuần hiện tại

		String week_link = Integer.toString(week);
		if (week < 10) {
			week_link = "0" + week;
		}

		if (projectUpdatePM.getWeek() == current_week) {
			_projectService.update_tk(projectUpdatePM);

		} else {
			if (current_week < 10) {
				week_link = "0" + current_week;
			}
			String pic_id = (String) session.getAttribute("user_id");

			_projectService.updateInteractive(projectUpdatePM.getId(), "old");
			String am_id = _picService.getPICByProjectId(projectUpdatePM.getId());
			projectUpdatePM.setNote(Integer.toString(projectUpdatePM.getId()));
			projectUpdatePM.setId(_projectService.getMaxId() + 1);
			projectUpdatePM.setWeek(current_week);
			projectUpdatePM.setInteractive("update");

			_projectService.saveDep(projectUpdatePM);
			_picService.save(projectUpdatePM.getId(), pic_id);
			_picService.save(projectUpdatePM.getId(), am_id);
			return "redirect:/PM/dashboard/" + week_link + "_" + year;

		}

		return "redirect:/PM/detail/" + week_link + "_" + year + "_" + id;
	}

	@RequestMapping("/home/{week}_{year}")
	public String home(@PathVariable int week, @PathVariable int year) {

		Date now = new Date();
		int current_week = getWeekOfYear(now) - 1; // Gọi hàm lấy số tuần => Lấy số tuần trước
		int current_year = Calendar.getInstance().get(Calendar.YEAR); // Get the curent year
		String week_link = Integer.toString(current_week);
		if (current_week < 10) {
			week_link = "0" + current_week;
		}
		return "redirect:/PM/dashboard/" + week_link + "_" + current_year;
	}

}
