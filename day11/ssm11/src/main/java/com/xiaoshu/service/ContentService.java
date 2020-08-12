package com.xiaoshu.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.CategoryMapper;
import com.xiaoshu.dao.ContentMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Category;
import com.xiaoshu.entity.Content;
import com.xiaoshu.entity.ContentVo;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class ContentService {

	@Autowired
	ContentMapper userMapper;

	@Autowired
	CategoryMapper cm;


	// 新增
	public void addUser(Content t) throws Exception {
		t.setCreatetime(new Date());
		userMapper.insert(t);
	};

	// 修改
	public void updateUser(Content user) throws Exception {
		userMapper.updateByPrimaryKeySelective(user);
	};

	// 删除
	public void deleteUser(Integer contentid) throws Exception {
		userMapper.deleteByPrimaryKey(contentid);
	};


	public PageInfo<ContentVo> findUserPage(ContentVo user, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<ContentVo> userList = userMapper.findAll(user);
		PageInfo<ContentVo> pageInfo = new PageInfo<ContentVo>(userList);
		return pageInfo;
	}


	public List<Category> findRole() {
		return cm.selectAll();
	}

	public List<ContentVo> find() {
		return userMapper.find();
	}


}
