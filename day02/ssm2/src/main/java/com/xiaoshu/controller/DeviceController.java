package com.xiaoshu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.Device;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.QueryVo;
import com.xiaoshu.entity.Type;
import com.xiaoshu.service.DeviceService;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.TimeUtil;
import com.xiaoshu.util.WriterUtil;

@Controller
@RequestMapping("device")
public class DeviceController {
	
	@Autowired
	private DeviceService ds;
	
	@Autowired
	private OperationService operationService;
	
	
	@RequestMapping("deviceIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Type> roleList = ds.findRole();
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		request.setAttribute("operationList", operationList);
		request.setAttribute("roleList", roleList);
		return "device";
	}
	
	@RequestMapping(value="userList",method=RequestMethod.POST)
	public void userList(HttpServletRequest request,HttpServletResponse response,String offset,String limit,QueryVo qv) throws Exception{
		try {
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<QueryVo> userList= ds.findUserPage(qv,pageNum,pageSize);
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
		public void reserveUser(HttpServletRequest request,Device d,HttpServletResponse response){
			Integer userId = d.getDeviceid();
			JSONObject result=new JSONObject();
			try {
				if (userId != null) {   // userId不为空 说明是修改
						d.setDeviceid(userId);
						ds.updateUser(d);
						result.put("success", true);
					
				}else {   // 添加
						d.setCreatetime(new Date());
						ds.addUser(d);
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
					ds.deleteUser(Integer.parseInt(id));
				}
				result.put("success", true);
				result.put("delNums", ids.length);
			} catch (Exception e) {
				e.printStackTrace();
				result.put("errorMsg", "对不起，删除失败");
			}
			WriterUtil.write(response, result.toString());
		}
		
		@RequestMapping("exp")
		public void exp(HttpServletRequest request,HttpServletResponse response) throws Exception{
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet();
			HSSFRow fr = sheet.createRow(0);
			String[] title={"编号","设备名称","设备类型名称","内存","机身颜色","价格","设备状态","创建时间"};
			for (int i = 0; i < title.length; i++) {
				fr.createCell(i).setCellValue(title[i]);
			}
			List<QueryVo> li  =  ds.exp();
			for (int i = 0; i < li.size(); i++) {
				QueryVo qv = li.get(i);
				HSSFRow row = sheet.createRow(i+1);
				row.createCell(0).setCellValue(qv.getDeviceid());
				row.createCell(1).setCellValue(qv.getDevicename());
				row.createCell(2).setCellValue(qv.getTypename());
				row.createCell(3).setCellValue(qv.getDeviceram());
				row.createCell(4).setCellValue(qv.getColor());
				row.createCell(5).setCellValue(qv.getPrice());
				row.createCell(6).setCellValue(qv.getStatus());
				row.createCell(7).setCellValue(TimeUtil.formatTime(qv.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
			}
			File file = new File("d:/h1918a.xls");
			FileOutputStream os = new FileOutputStream(file);
			wb.write(os);
			wb.close();
		}
}
