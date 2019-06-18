package com.wf.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanfangdata.encrypt.PasswordHelper;
import com.wf.bean.Department;
import com.wf.bean.SystemMenu;
import com.wf.bean.Wfadmin;
import com.wf.dao.SystemMenuMapper;
import com.wf.dao.UserMapper;
import com.wf.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private SystemMenuMapper systemMenuMapper;

	/**
	 *	查询后台登录用户信息 
	 */
	@Override
	public Wfadmin getuser(String userName,String passWord) {
		Map<String,String> map = new HashMap<String, String>();
		try {
			map.put("userName", userName);

			map.put("passWord", PasswordHelper.encryptPassword(passWord));

			Wfadmin us = userMapper.selectUserInfo(map);
			Department de=new Department();
			de.setDeptName(us==null?"":us.getDepartment());
			if(us!=null){
				us.setDept(de);
			}
			return us;
		} catch (Exception e) {
			e.printStackTrace();
			return new Wfadmin();
		}
	}
	
	/**
	 *	查询角色权限
	 */
	@Override
	public Map<String,String> getAdminPurview(String wfAdminId) {
		Map<String,String> map = userMapper.getAdminPurview(wfAdminId);
		return map;
	}

	@Override
	public SystemMenu findPurviewById(String id) {
		SystemMenu sm=systemMenuMapper.findPurviewById(id);
		return sm;
	}

}
