package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.GoodsMapper;
import com.xiaoshu.dao.TypeMapper;
import com.xiaoshu.entity.Goods;
import com.xiaoshu.entity.QueryVo;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.Type;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class GoodsService {
	
	@Autowired
	private GoodsMapper gm;
	
	@Autowired
	private TypeMapper tm;
	
	public PageInfo<QueryVo> findUserPage(QueryVo qv, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<QueryVo> userList = gm.findAll(qv);
		PageInfo<QueryVo> pageInfo = new PageInfo<QueryVo>(userList);
		return pageInfo;
	}

	public List<Type> findRole() {
		return tm.selectAll();
	}
	
	// 新增
		public void addUser(Goods t) throws Exception {
			gm.insert(t);
		};

		// 修改
		public void updateUser(Goods t) throws Exception {
			gm.updateByPrimaryKeySelective(t);
		};

		// 删除
		public void deleteUser(Integer gId) throws Exception {
			gm.deleteByPrimaryKey(gId);
		};
}
