package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.controller.StudentController;
import com.xiaoshu.dao.SchoolMapper;
import com.xiaoshu.dao.StudentMapper;
import com.xiaoshu.entity.School;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentVo;

@Service
public class StudentService {

	//UserMapper userMapper;

	@Autowired
	private StudentMapper sm;
	
	@Autowired
	private SchoolMapper scm;
	
	/*// 查询所有
	public List<User> findUser(User t) throws Exception {
		return userMapper.select(t);
	};

	// 数量
	public int countUser(User t) throws Exception {
		return userMapper.selectCount(t);
	};

	// 通过ID查询
	public User findOneUser(Integer id) throws Exception {
		return userMapper.selectByPrimaryKey(id);
	};
*/
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
	};


	public PageInfo<StudentVo> findUserPage(StudentVo s, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<StudentVo> userList = sm.findAll(s);
		PageInfo<StudentVo> pageInfo = new PageInfo<StudentVo>(userList);
		return pageInfo;
	}

	public List<School> findRole() {
		// TODO Auto-generated method stub
		return scm.selectAll();
	}

	public Student findBySname(String sname) {
		List<Student> list = sm.selectAll();
		Student sd = new Student();
		for (int i = 0; i < list.size(); i++) {
			Student s = list.get(i);
			if(s.getSname().equals(sname)){
				sd.setSname(sname);
				return sd;
			}
		}
		return null;
	}

	public List<StudentVo> exp() {
		return sm.findAll(null);
	}


}
