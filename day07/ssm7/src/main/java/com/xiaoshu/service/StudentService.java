package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.StudentMapper;
import com.xiaoshu.dao.TeacherMapper;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentVo;
import com.xiaoshu.entity.Teacher;
import com.xiaoshu.entity.Tj;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class StudentService {

	@Autowired
	private StudentMapper sm;
	
	@Autowired
	private TeacherMapper tm;
	
	public PageInfo<StudentVo> findUserPage(Student user, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<StudentVo> userList = sm.findAll(user);
		PageInfo<StudentVo> pageInfo = new PageInfo<StudentVo>(userList);
		return pageInfo;
	}

	public List<Teacher> findRole() {
		return tm.selectAll();
	}
	
	// 新增
		public void addUser(Student t) throws Exception {
			sm.insert(t);
		};

		// 修改
		public void updateUser(Student t) throws Exception {
			sm.updateByPrimaryKeySelective(t);
		};

		// 删除
		public void deleteUser(Integer sid) throws Exception {
			sm.deleteByPrimaryKey(sid);
		}

		public List<Tj> find() {
			return sm.find();
		};
		
	
}
