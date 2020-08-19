package com.xiaoshu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Company;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Person;
import com.xiaoshu.entity.PersonVo;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.PersonService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.service.UserService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.TimeUtil;
import com.xiaoshu.util.WriterUtil;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("person")
public class PersonController extends LogController{
	static Logger logger = Logger.getLogger(PersonController.class);

	@Autowired
	private PersonService userService;
	
	
	@Autowired
	private OperationService operationService;
	
	
	@RequestMapping("personIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Company> roleList = userService.findRole();
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		request.setAttribute("operationList", operationList);
		request.setAttribute("roleList", roleList);
		return "person";
	}
	
	
	@RequestMapping(value="userList",method=RequestMethod.POST)
	public void userList(HttpServletRequest request,HttpServletResponse response,String offset,String limit,PersonVo p) throws Exception{
		try {
			
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<PersonVo> userList= userService.findUserPage(p,pageNum,pageSize);
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total",userList.getTotal() );
			jsonObj.put("rows", userList.getList());
	        WriterUtil.write(response,jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户展示错误",e);
			throw e;
		}
	}
	
	
	// 新增或修改
	@RequestMapping("reserveUser")
	public void reserveUser(HttpServletRequest request,Person user,HttpServletResponse response){
		Integer userId = user.getPid();
		JSONObject result=new JSONObject();
		try {
			if (userId != null) {   // userId不为空 说明是修改
					user.setPid(userId);
					userService.updateUser(user);
					result.put("success", true);
				
			}else {   // 添加
					userService.addUser(user);
					result.put("success", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存用户信息错误",e);
			result.put("success", true);
			result.put("errorMsg", "对不起，操作失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	/*
	@RequestMapping("deleteUser")
	public void delUser(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			String[] ids=request.getParameter("ids").split(",");
			for (String id : ids) {
				userService.deleteUser(Integer.parseInt(id));
			}
			result.put("success", true);
			result.put("delNums", ids.length);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户信息错误",e);
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}
	*/
	
	@RequestMapping("impPerson")
	public void impPerson(HttpServletRequest request,HttpServletResponse response,MultipartFile impFile){
		JSONObject result=new JSONObject();
		try {
			HSSFWorkbook wb = new HSSFWorkbook(impFile.getInputStream());
			HSSFSheet at = wb.getSheetAt(0);
			int num = at.getLastRowNum();
			for (int i = 1; i <= num; i++) {
				Person p = new Person();
				HSSFRow row = at.getRow(i);
				p.setpName(row.getCell(0).getStringCellValue());
				if(!p.getpName().contains("张")){
					continue;
				}
				p.setpGender(row.getCell(1).getStringCellValue());
				String cname = row.getCell(2).getStringCellValue();
				Company c = userService.findCname(cname);
				p.setpCid(c.getCid());
				if(p.getpCid()!=4){
					continue;
				}
				p.setpSalary(row.getCell(3).getStringCellValue());
				p.setpAge((int)row.getCell(4).getNumericCellValue());
				p.setpEntrytime(row.getCell(5).getDateCellValue());
				userService.addUser(p);
			}
			result.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户信息错误",e);
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	@RequestMapping("expPerson")
	public void delUser(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet();
			HSSFRow fr = sheet.createRow(0);
			String[] title ={"学员编号","学员姓名","学员性别","入职公司","月薪","学员年龄","入职日期"};
			for (int i = 0; i < title.length; i++) {
				fr.createCell(i).setCellValue(title[i]);
			}
			List<PersonVo> li = userService.exp();
			for (int i = 0; i < li.size(); i++) {
				PersonVo vo = li.get(i);
				if(vo.getpGender().equals("男")){
					HSSFRow row = sheet.createRow(i+1);
					row.createCell(0).setCellValue(vo.getPid());
					row.createCell(1).setCellValue(vo.getpName());
					row.createCell(2).setCellValue(vo.getpGender());
					row.createCell(3).setCellValue(vo.getcName());
					row.createCell(4).setCellValue(vo.getpSalary());
					row.createCell(5).setCellValue(vo.getpAge());
					row.createCell(6).setCellValue(TimeUtil.formatTime(vo.getpEntrytime(), "yyyy-MM-dd"));
				}
			}
			File file = new File("d:/xzh.xls");
			FileOutputStream os = new FileOutputStream(file);
			wb.write(os);
			wb.close();
			result.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户信息错误",e);
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}
}
