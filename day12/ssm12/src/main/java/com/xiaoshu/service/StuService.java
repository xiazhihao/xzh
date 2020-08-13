package com.xiaoshu.service;

import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.persistence.AttributeOverride;

import org.springframework.beans.factory.annotation.Autowired;

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
	public void deleteUser(Integer sdId) throws Exception {
		userMapper.deleteByPrimaryKey(sdId);
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

	public List<StuVo> find() {
		return userMapper.find();
	}
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	Destination myMessageListener;
	
	
	public void insertMajor(final Major m) {
		mm.insert(m);
		Major major = new Major();
		major.setMdname(m.getMdname());
		Major one = mm.selectOne(major);
		jmsTemplate.send(myMessageListener, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				String json = JSONObject.toJSONString(m);
				return session.createTextMessage(json);
			}
		});
	}


}
