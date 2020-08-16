package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.StudentMapper;
import com.xiaoshu.dao.TeacherMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentVo;
import com.xiaoshu.entity.Teacher;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class StudentService {

	@Autowired
	StudentMapper userMapper;

	@Autowired
	TeacherMapper tm;
	
	// 新增
	public void addUser(Student t) throws Exception {
		userMapper.insert(t);
	};

	// 修改
	public void updateUser(Student t) throws Exception {
		userMapper.updateByPrimaryKeySelective(t);
	};

	// 删除
	public void deleteUser(Integer id) throws Exception {
		userMapper.deleteByPrimaryKey(id);
	};

	

	public PageInfo<StudentVo> findUserPage(StudentVo user, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<StudentVo> userList = userMapper.findAll(user);
		PageInfo<StudentVo> pageInfo = new PageInfo<StudentVo>(userList);
		return pageInfo;
	}


	public List<Teacher> findRole() {
		// TODO Auto-generated method stub
		return tm.selectAll();
	}

	public List<StudentVo> findEcharts() {
		return userMapper.findEcharts();
	}

	public void addTeacher(Teacher t) {
		tm.insert(t);
	}


}
