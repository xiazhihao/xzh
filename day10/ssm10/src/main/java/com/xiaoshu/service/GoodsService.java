package com.xiaoshu.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.GoodsMapper;
import com.xiaoshu.dao.GoodsTypeMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Goods;
import com.xiaoshu.entity.GoodsType;
import com.xiaoshu.entity.GoodsVo;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class GoodsService {

	@Autowired
	GoodsMapper userMapper;

	@Autowired
	GoodsTypeMapper goodsTypeMapper;
	

	
	// 新增
	public void addUser(Goods t) throws Exception {
		List<GoodsType> selectAll = goodsTypeMapper.findGoodsType();
		for (int i = 0; i < selectAll.size(); i++) {
			GoodsType goodsType = selectAll.get(i);
			if(goodsType.getId()==(t.getTypeid())){
				t.setCreatetime(goodsType.getCreatetime());
			}
		}
		userMapper.insert(t);
	};

	// 修改
	public void updateUser(Goods t) throws Exception {
		List<GoodsType> selectAll = goodsTypeMapper.findGoodsType();
		for (int i = 0; i < selectAll.size(); i++) {
			GoodsType goodsType = selectAll.get(i);
			if(goodsType.getId()==(t.getTypeid())){
				t.setCreatetime(goodsType.getCreatetime());
			}
		}
		userMapper.updateByPrimaryKeySelective(t);
	};

	// 删除
	public void deleteUser(Integer id) throws Exception {
		userMapper.deleteByPrimaryKey(id);
	};

	

	public PageInfo<GoodsVo> findUserPage(GoodsVo user, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<GoodsVo> userList = userMapper.findAll(user);
		PageInfo<GoodsVo> pageInfo = new PageInfo<GoodsVo>(userList);
		return pageInfo;
	}


	public List<GoodsType> findGoodsType() {
		// TODO Auto-generated method stub
		return goodsTypeMapper.findGoodsType();
	}

	public Object existUserWithUserName(Integer code) {
		Goods g = new Goods();
		g.setCode(code);
		Goods selectOne = userMapper.selectOne(g);
		return selectOne;
	}
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	public void addType(GoodsType user) {
		user.setCreatetime(new Date());
		goodsTypeMapper.insertType(user);
		List<GoodsType> list = goodsTypeMapper.findGoodsType();
		for (int i = 0; i < list.size(); i++) {
			GoodsType gt = list.get(i);
			if(gt.getTypename().equals(user.getTypename())){
				user.setId(gt.getId());
			}
		}
		redisTemplate.boundHashOps("list").put(user.getId()+"", user.toString());
	}

}
