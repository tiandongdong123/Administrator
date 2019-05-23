package com.wf.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redis.RedisUtil;
import com.redis.UserRedisUtil;
import com.utils.CookieUtil;
import com.wf.bean.PageList;
import com.wf.bean.Wfadmin;
import com.wf.dao.DepartmentMapper;
import com.wf.dao.RoleMapper;
import com.wf.dao.WfadminMapper;
import com.wf.service.AdminService;
@Service
public class AdminServiceImpl implements AdminService {
	private static final Logger log = Logger.getLogger(AdminServiceImpl.class);

	@Autowired
	private WfadminMapper admin;
	@Autowired
	private DepartmentMapper dept;
	@Autowired
	private RoleMapper role;
	
	private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public PageList getAdmin(String adminname, Integer pagenum, Integer pagesize) {
		PageList pl = new PageList();
		int num = 0;
		int pnum = (pagenum-1) * pagesize;
		String param = null;
		if(StringUtils.isNotBlank(adminname)){
			adminname=adminname.replace("_", "\\_");
			adminname=adminname.replace("%", "\\%");
			param = "%"+adminname+"%";
		}
		try {
			List<Object> wf = this.admin.getAdmin(param,"%"+adminname+"%","%"+adminname+"%","%"+adminname+"%",pnum,pagesize);
			num = this.admin.getAdminNum(param,"%"+adminname+"%","%"+adminname+"%","%"+adminname+"%");
			pl.setPageRow(wf);
			pl.setPageNum(pagenum);
			pl.setPageSize(pagesize);
			pl.setPageTotal(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pl;
	}
	@Override
	public boolean deleteAdmin(String[] ids) {
		int result = 0;
		boolean re = false;
		try {
			 result = this.admin.deleteAdmin(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result>0){
			re=true;
		}
		return re;
	}
	@Override
	public boolean closeAdmin(String[] ids) {
		int result = 0;
		boolean re = false;
		try {
			result = this.admin.closeAdmin(ids);
			if (ids != null && ids.length > 0) {
				List<String> list = admin.getAdminByAdminIds(ids);
				if (list != null && list.size() > 0) {
					String[] strs = new String[list.size()];
					for (int i = 0; i < list.size(); i++) {
						strs[i] = CookieUtil.LAYOUT + list.get(i);
					}
					UserRedisUtil.del(0, strs);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result>0){
			re=true;
		}
		return re;
	}
	@Override
	public boolean openAdmin(String[] ids) {
		int result = 0;
		boolean re = false;
		try {
			 result = this.admin.openAdmin(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result>0){
			re=true;
		}
		return re;
	}
	@Override
	public List<Object> getDept() {
		List<Object> deptname = new ArrayList<Object>();
		try {
			deptname = this.dept.getAllDept();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deptname;
	}
	@Override
	public List<Object> getRole() {
		List<Object> rolename = new ArrayList<Object>();
		try {
			rolename = this.role.getAllRole();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rolename;
	}
	@Override
	public boolean checkAdminId(String id) {
		boolean rt = false;
		List<Object> li = new ArrayList<Object>();
		try {
			li = this.admin.checkAdminId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(li!=null&&li.size()>0){
			rt = true;
		}
		return rt;
	}

	@Override
	public boolean doAddAdmin(Wfadmin admin) {
		boolean rt = false;
		int x = 0;
		Date date = new Date();
		String d  = sf.format(date);
		admin.setId(UUID.randomUUID().toString());
		admin.setRegistration_time(date);
		try {
			x = this.admin.doAddAdmin(admin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(x>0){
			rt=true;
		}
		return rt;
	}
	@Override
	public Wfadmin getAdminById(String id) {
		Wfadmin admin = new Wfadmin();
		try {
			admin = this.admin.getAdminById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return admin;
	}
	@Override
	public boolean doUpdateAdmin(Wfadmin admin) {
		boolean rt = false;
		int x = 0;
		try {
			x = this.admin.doUpdateAdmin(admin);
		} catch (Exception e) {
			e.printStackTrace();
    }
		if(x>0){
			rt=true;
		}
		return rt;
	}
}
