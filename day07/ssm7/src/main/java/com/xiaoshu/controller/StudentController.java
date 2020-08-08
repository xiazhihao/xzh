package com.xiaoshu.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentVo;
import com.xiaoshu.entity.Teacher;
import com.xiaoshu.entity.Tj;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.StudentService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.WriterUtil;

@Controller
@RequestMapping("student")
public class StudentController {
	
	@Autowired
	private OperationService operationService;
	
	@Autowired
	private StudentService ss;
	
	
	@RequestMapping("studentIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Teacher> roleList = ss.findRole();
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		request.setAttribute("operationList", operationList);
		request.setAttribute("roleList", roleList);
		return "student";
	}
	
	
	@RequestMapping(value="userList",method=RequestMethod.POST)
	public void userList(HttpServletRequest request,HttpServletResponse response,Student s,String offset,String limit) throws Exception{
		try {
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<StudentVo> userList= ss.findUserPage(s,pageNum,pageSize);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total",userList.getTotal() );
			jsonObj.put("rows", userList.getList());
	        WriterUtil.write(response,jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// 新增或修改
		@RequestMapping("reserveUser")
		public void reserveUser(HttpServletRequest request,Student user,HttpServletResponse response,MultipartFile picFile){
			Integer userId = user.getSid();
			JSONObject result=new JSONObject();
			try {
				if (userId != null) {   // userId不为空 说明是修改
					if(picFile!=null){
						String filename = picFile.getOriginalFilename();
						if(filename!=""){
							String fei = filename.substring(filename.lastIndexOf("."));
							String newName = System.currentTimeMillis()+fei;
							picFile.transferTo(new File("e:/pic/"+newName));
							user.setPic(newName);
						}else{
							
						}
					}
					user.setSid(userId);
					ss.updateUser(user);
					result.put("success", true);
					
				}else {   // 添加
					if(picFile!=null){
						String filename = picFile.getOriginalFilename();
						if(filename!=""){
							String fei = filename.substring(filename.lastIndexOf("."));
							String newName = System.currentTimeMillis()+fei;
							picFile.transferTo(new File("e:/pic/"+newName));
							user.setPic(newName);
						}else{
							
						}
					}
						ss.addUser(user);
						result.put("success", true);
				}
			} catch (Exception e) {
				e.printStackTrace();
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
					ss.deleteUser(Integer.parseInt(id));
				}
				result.put("success", true);
				result.put("delNums", ids.length);
			} catch (Exception e) {
				e.printStackTrace();
				result.put("errorMsg", "对不起，删除失败");
			}
			WriterUtil.write(response, result.toString());
		}
		
		@RequestMapping("bing")
		public void bing(HttpServletRequest request,HttpServletResponse response){
			JSONObject result=new JSONObject();
			try {
				List<Tj> li = ss.find();
				result.put("success", true);
				result.put("delNums", li);
			} catch (Exception e) {
				e.printStackTrace();
				result.put("errorMsg", "对不起，删除失败");
			}
			WriterUtil.write(response, result.toString());
		}

}
