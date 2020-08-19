package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.CompanyMapper;
import com.xiaoshu.dao.PersonMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Company;
import com.xiaoshu.entity.Person;
import com.xiaoshu.entity.PersonVo;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class PersonService {

	@Autowired
	PersonMapper userMapper;

	
	@Autowired
	CompanyMapper cm;

	@Autowired
	private RedisTemplate redisTemplate;

	// 新增
	public void addUser(Person t) throws Exception {
		userMapper.insert(t);
		redisTemplate.boundHashOps("list").put(t.getpName(), t.toString());
	};

	// 修改
	public void updateUser(Person t) throws Exception {
		userMapper.updateByPrimaryKeySelective(t);
	};

	/*// 删除
	public void deleteUser(Integer id) throws Exception {
		userMapper.deleteByPrimaryKey(id);
	};*/

	
	public PageInfo<PersonVo> findUserPage(PersonVo user, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<PersonVo> userList = userMapper.findAll(user);
		PageInfo<PersonVo> pageInfo = new PageInfo<PersonVo>(userList);
		return pageInfo;
	}


	public List<Company> findRole() {
		// TODO Auto-generated method stub
		return cm.selectAll();
	}

	public List<PersonVo> exp() {
		return userMapper.findAll(null);
	}

	public Company findCname(String cname) {
		// TODO Auto-generated method stub
		Company c = new Company();
		c.setcName(cname);
		Company one = cm.selectOne(c);
		return one;
	}


}
