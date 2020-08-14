package com.xiaoshu.service;

import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.MajorMapper;
import com.xiaoshu.dao.StuMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Major;
import com.xiaoshu.entity.Stu;
import com.xiaoshu.entity.StuVo;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class StuService {

	@Autowired
	StuMapper userMapper;

	@Autowired
	MajorMapper mm;

	// 新增
	public void addUser(Stu t) throws Exception {
		userMapper.insert(t);
	};

	// 修改
	public void updateUser(Stu t) throws Exception {
		userMapper.updateByPrimaryKeySelective(t);
	};

	// 删除
	public void deleteUser(Integer sId) throws Exception {
		userMapper.deleteByPrimaryKey(sId);
	};


	public PageInfo<StuVo> findUserPage(StuVo user, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<StuVo> userList = userMapper.findAll(user);
		PageInfo<StuVo> pageInfo = new PageInfo<StuVo>(userList);
		return pageInfo;
	}



	public List<Major> findRole() {
		return mm.selectAll();
	}

	public List<StuVo> exp() {
		return userMapper.findAll(null);
	}

	public Major findMname(String mName) {
		Major major = new Major();
		major.setmName(mName);
		Major one = mm.selectOne(major);
		return one;
	}

	public List<StuVo> findEcharts() {
		return userMapper.findEcharts();
	}

	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private JmsTemplate js;
	
	@Autowired
	private Destination queueTextDestination;
	
	public void addMajor(final Major m) {
		mm.insert(m);
		redisTemplate.boundHashOps("list").put("li", m.toString());
		js.send(queueTextDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				String json = JSONObject.toJSONString(m);
				return session.createTextMessage(json);
			}
		});
	}


}
