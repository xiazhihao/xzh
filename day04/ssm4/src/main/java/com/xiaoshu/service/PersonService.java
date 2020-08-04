package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.dao.CompanyMapper;
import com.xiaoshu.dao.PersonMapper;
import com.xiaoshu.entity.Company;
import com.xiaoshu.entity.QueryVo;
import com.xiaoshu.entity.User;

@Service
public class PersonService {

	@Autowired
	private PersonMapper pm;
	
	@Autowired
	private CompanyMapper cm;
	
	public PageInfo<QueryVo> findUserPage(QueryVo qv, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<QueryVo> userList = pm.findAll(qv);
		PageInfo<QueryVo> pageInfo = new PageInfo<QueryVo>(userList);
		return pageInfo;
	}
	
	// 新增
		public void addUser(QueryVo t) throws Exception {
			pm.insert(t);
		};

		// 修改
		public void updateUser(QueryVo t) throws Exception {
			pm.updateByPrimaryKeySelective(t);
		};

		// 删除
		public void deleteUser(Integer id) throws Exception {
			pm.deleteByPrimaryKey(id);
		}

		public List<Company> findRole() {
			// TODO Auto-generated method stub
			return cm.selectAll();
		};
}
