package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.dao.DeviceMapper;
import com.xiaoshu.dao.TypeMapper;
import com.xiaoshu.entity.Device;
import com.xiaoshu.entity.QueryVo;
import com.xiaoshu.entity.Type;

@Service
public class DeviceService {

	@Autowired
	private DeviceMapper dm;
	
	@Autowired
	private TypeMapper tm;
	
	public PageInfo<QueryVo> findUserPage(QueryVo qv, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<QueryVo> userList = dm.findAll(qv);
		PageInfo<QueryVo> pageInfo = new PageInfo<QueryVo>(userList);
		return pageInfo;
	}
	
	// 新增
		public void addUser(Device t) throws Exception {
			dm.insert(t);
		};

		// 修改
		public void updateUser(Device t) throws Exception {
			dm.updateByPrimaryKeySelective(t);
		};

		// 删除
		public void deleteUser(Integer deviceid) throws Exception {
			dm.deleteByPrimaryKey(deviceid);
		}

		public List<Type> findRole() {
			return tm.selectAll();
		}

		public List<QueryVo> exp() {
			return dm.findAllDevice();
		};

}
