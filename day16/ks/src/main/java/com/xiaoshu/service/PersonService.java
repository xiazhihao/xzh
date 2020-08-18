package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.BankMapper;
import com.xiaoshu.dao.PersonMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Bank;
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
	BankMapper bm;
	
	@Autowired
	private RedisTemplate redisTemplate;
	// 新增
	public void addUser(Person t) throws Exception {
		userMapper.insert(t);
		List<Person> all = userMapper.selectAll();
		for (int i = 0; i < all.size(); i++) {
			Person person = all.get(i);
			if(person.getpName().equals(t.getpName())){
				t.setpId(person.getpId());
			}
		}
		redisTemplate.boundHashOps("list").put(t.getpId()+"", t.toString());
	};

	// 修改
	public void updateUser(Person t) throws Exception {
		userMapper.updateByPrimaryKeySelective(t);
	};

	// 删除
	public void deleteUser(Integer pId) throws Exception {
		userMapper.deleteByPrimaryKey(pId);
	};



	public PageInfo<PersonVo> findUserPage(PersonVo user, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<PersonVo> userList = userMapper.findAll(user);
		PageInfo<PersonVo> pageInfo = new PageInfo<PersonVo>(userList);
		return pageInfo;
	}

	public List<Bank> findRole() {
		return bm.selectAll();
	}

	public List<PersonVo> findEcharts() {
		return userMapper.findEcharts();
	}

	public Bank findbName(String bName) {
		Bank b = new Bank();
		b.setbName(bName);
		Bank one = bm.selectOne(b);
		return one;
	}



}
