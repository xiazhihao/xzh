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
import com.xiaoshu.entity.Major;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.Stu;
import com.xiaoshu.entity.StuVo;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.service.StuService;
import com.xiaoshu.service.UserService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.TimeUtil;
import com.xiaoshu.util.WriterUtil;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("stu")
public class StuController extends LogController{
	static Logger logger = Logger.getLogger(StuController.class);

	@Autowired
	private StuService userService;
	
	@Autowired
	private OperationService operationService;
	
	
	@RequestMapping("stuIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Major> roleList = userService.findRole();
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		request.setAttribute("operationList", operationList);
		request.setAttribute("roleList", roleList);
		return "stu";
	}
	
	
	@RequestMapping(value="userList",method=RequestMethod.POST)
	public void userList(HttpServletRequest request,HttpServletResponse response,String offset,String limit,StuVo s) throws Exception{
		try {
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<StuVo> userList= userService.findUserPage(s,pageNum,pageSize);
			
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
	public void reserveUser(HttpServletRequest request,Stu user,HttpServletResponse response){
		Integer userId = user.getsId();
		JSONObject result=new JSONObject();
		try {
			if (userId != null) {   // userId不为空 说明是修改
					user.setsId(userId);
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

	@RequestMapping("addMajor")
	public void addMajor(HttpServletRequest request,HttpServletResponse response,Major m){
		JSONObject result=new JSONObject();
		try {
			userService.addMajor(m);
			result.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户信息错误",e);
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}

	@RequestMapping("echartsStu")
	public void echartsStu(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			List<StuVo> li = userService.findEcharts();
			result.put("success", true);
			result.put("li", li);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户信息错误",e);
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}

	@RequestMapping("impStu")
	public void impStu(HttpServletRequest request,HttpServletResponse response,MultipartFile stuFile){
		JSONObject result=new JSONObject();
		try {
			HSSFWorkbook wb = new HSSFWorkbook(stuFile.getInputStream());
			HSSFSheet sheet = wb.getSheetAt(0);
			int num = sheet.getLastRowNum();
			for (int i = 1; i <= num; i++) {
				HSSFRow row = sheet.getRow(i);
				Stu s = new Stu();
				s.setsName(row.getCell(0).getStringCellValue());
				s.setsSex(row.getCell(1).getStringCellValue());
				s.setsHobby(row.getCell(2).getStringCellValue());
				s.setsBirth(row.getCell(3).getDateCellValue());
				String mName = row.getCell(4).getStringCellValue();
				Major m = userService.findMname(mName);
				s.setmId(m.getmId());
				userService.addUser(s);
			}
			result.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导入用户信息错误",e);
			result.put("errorMsg", "对不起，导入失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	@RequestMapping("expStu")
	public void expStu(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet();
			HSSFRow fr = sheet.createRow(0);
			String[] title = {"学生编号","学生姓名","学生性别","学生爱好","学生生日","专业"};
			for (int i = 0; i < title.length; i++) {
				fr.createCell(i).setCellValue(title[i]);
			}
			List<StuVo> li = userService.exp();
			int a=0;
			for (int i = 0; i < li.size(); i++) {
				StuVo s = li.get(i);
				if(s.getsHobby().length()>4){
					HSSFRow row = sheet.createRow(a+1);
					row.createCell(0).setCellValue(s.getsId());
					row.createCell(1).setCellValue(s.getsName());
					row.createCell(2).setCellValue(s.getsSex());
					row.createCell(3).setCellValue(s.getsHobby());
					row.createCell(4).setCellValue(TimeUtil.formatTime(s.getsBirth(), "yyyy-MM-dd"));
					row.createCell(5).setCellValue(s.getmName());
				}
				File file = new File("e:/h1911.xls");
				FileOutputStream os = new FileOutputStream(file);
				wb.write(os);
				wb.close();
			}
			result.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户信息错误",e);
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}
}
