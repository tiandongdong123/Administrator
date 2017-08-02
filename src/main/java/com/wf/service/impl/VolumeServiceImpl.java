package com.wf.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utils.GetUuid;
import com.wf.bean.PageList;
import com.wf.bean.Volume;
import com.wf.bean.VolumeChapter;
import com.wf.bean.VolumeDocu;
import com.wf.dao.VolumeChapterMapper;
import com.wf.dao.VolumeDocuMapper;
import com.wf.dao.VolumeMapper;
import com.wf.service.VolumeService;
@Service
public class VolumeServiceImpl implements VolumeService{
	@Autowired
	private VolumeMapper volumeMapper;
	@Autowired
	private VolumeChapterMapper vcMapper;
	@Autowired
	private VolumeDocuMapper vdMapper;
	private SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmssSSS");
	/**
	 * 添加文辑
	 */
	@Override
	public Map<String,Object> insert(String publishPerson, Volume volume,String volumeType,String listContent) {
		boolean flag = false;
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> contList = JSONArray.fromObject(listContent);
		String volumeId = GetUuid.getId();
		//---------------------------分章节-----------------------------
		if(volume.getVolumeChapter() == 1){
			//-----------------添加章-----------------
			List<VolumeChapter> cList = new ArrayList<VolumeChapter>();
			for(int i = 0;i < contList.size();i++){
				String chapterTitle = (String) contList.get(i).get("chapterTitle");
				VolumeChapter vc = new VolumeChapter();
				String chapterId = GetUuid.getId();
				vc.setChapterId(chapterId);
				vc.setChapterTitle(chapterTitle);
				vc.setVolumeId(volumeId);
				vc.setNumber(i+1);
				vc.setAddTime(format.format(new Date()));
				//-----------------添加节-----------------
				List<VolumeDocu> dList = new ArrayList<VolumeDocu>();
				List<Map<String,Object>> list1 = (List<Map<String, Object>>) contList.get(i).get("sections");
				for(int j = 0;j < list1.size();j++){
					String docuId = (String) list1.get(j).get("docuId");
					String docuTitle = (String) list1.get(j).get("docuTitle");
					String docuAuthor = (String) list1.get(j).get("docuAuthor");
					String randomId = (String) list1.get(j).get("randomId");
					String docuType = (String) list1.get(j).get("docuType");
					String classType = (String) list1.get(j).get("classType");
					VolumeDocu vd = new VolumeDocu();
					vd.setId(GetUuid.getId());
					vd.setDocuId(docuId);
					vd.setRandomId(randomId);
					vd.setDocuType(docuType);
					vd.setClassType(classType);
					vd.setDocuTitle(docuTitle);
					vd.setDocuAuthor(docuAuthor);
					vd.setChapterId(chapterId);
					vd.setVolumeId(volumeId);
					vd.setNumber(j+1);
					vd.setAddTime(format.format(new Date()));
					dList.add(vd);
				}
				int dResult = vdMapper.insert(dList);//添加节
				if(dResult <= 0){
					map.put("flag", flag);
					return map;
				}
				cList.add(vc);
			}
			 int cResult = vcMapper.insert(cList);//添加章
			 if(cResult <= 0){
				 map.put("flag", flag);
				 return map;
				}
		}else{//----------------------------------不分章节--------------------
			//-----------------添加节-----------------
			List<VolumeDocu> dList1 = new ArrayList<VolumeDocu>();
			for(int j = 0;j < contList.size();j++){
				String docuId = (String) contList.get(j).get("docuId");
				String docuTitle = (String) contList.get(j).get("docuTitle");
				String docuAuthor = (String) contList.get(j).get("docuAuthor");
				String randomId = (String) contList.get(j).get("randomId");
				String docuType = (String) contList.get(j).get("docuType");
				String classType = (String) contList.get(j).get("classType");
				VolumeDocu vd1 = new VolumeDocu();
				vd1.setId(GetUuid.getId());
				vd1.setDocuId(docuId);
				vd1.setRandomId(randomId);
				vd1.setDocuType(docuType);
				vd1.setClassType(classType);
				vd1.setDocuTitle(docuTitle);
				vd1.setDocuAuthor(docuAuthor);
				vd1.setVolumeId(volumeId);
				vd1.setNumber(j+1);
				vd1.setAddTime(format.format(new Date()));
				dList1.add(vd1);
			}
			int dResult = vdMapper.insert(dList1);//添加节
			if(dResult <= 0){
				map.put("flag", flag);
				return map;
			}
		}
		//-----------------生成文辑编号------------------------
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String volumeNo = dateformat.format(date);//要生成的编号 
		String o_max = volumeMapper.queryMaxVolumeNo("O");//获取U最大的批次(用户文集)
		if(o_max!=null){
			String maxDate = o_max.substring(1, 9);
			if(volumeNo.equals(maxDate)){//当前日期已经生成过批次
				volumeNo = Long.valueOf(o_max.substring(1))+1+"";//批次号
				volumeNo = "O" + volumeNo;
			}else{//当前日期没有生成过批次号
				volumeNo = "O" + Long.valueOf(volumeNo)+"0001";//批次号
			}
		}else{
			volumeNo = "O" + Long.valueOf(volumeNo)+"0001";//批次号
		}
		//-----------------添加文辑----------------
		volume.setId(volumeId);
		volume.setVolumeNo(volumeNo);
		volume.setVolumeType(Integer.valueOf(volumeType));
		if(Integer.valueOf(volumeType) == 2){//如果是优选文辑，状态为公开
			volume.setVolumeState(1);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		volume.setPublishPerson(publishPerson);
		volume.setPublishDate(sdf.format(new Date()));
		int docuNumber = vdMapper.queryByVolumeId(volumeId).size();
		volume.setDocuNumber(docuNumber);
		volume.setFulltextReadingNum(0);
		volume.setCollectionNum(0);
		volume.setDownloadNum(0);
		volume.setIssue(1);//默认未发布
		int result = volumeMapper.insert(volume);
		if(result > 0){
			flag = true;
		}
		String id = volume.getId();
		map.put("flag", flag);
		map.put("id", id);
		return map;
	}
	/**
	 * 查询文辑列表
	 */
	@Override
	public PageList queryList(String startTime, String endTime,
			String searchWord, String volumeType, String volumeState,
			String sortColumn,String sortWay,int pageNum, int pageSize) {
		searchWord = searchWord.trim();
		if(StringUtils.isEmpty(startTime)) startTime = null;
		if(StringUtils.isEmpty(endTime)) endTime = null;
		if(StringUtils.isEmpty(searchWord)) searchWord = null;
		if(StringUtils.isEmpty(volumeType)) volumeType = null;
		if(StringUtils.isEmpty(volumeState)) volumeState = null;
		if(StringUtils.isEmpty(sortColumn)) sortColumn = "publish_date";
		if(StringUtils.isEmpty(sortWay)) sortWay = "DESC";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("searchWord", searchWord);
		map.put("volumeType", Integer.valueOf(volumeType));
		map.put("volumeState", volumeState == null ? null:Integer.valueOf(volumeState));
		map.put("sortColumn", sortColumn);
		map.put("sortWay", sortWay);
		map.put("pageNum", (pageNum-1)*pageSize);
		map.put("pageSize", pageSize);
		PageList p = new PageList();
		p.setPageNum(pageNum);
		p.setPageRow(volumeMapper.queryByPage(map));
		p.setPageSize(pageSize);
		p.setTotalRow(volumeMapper.queryAll(map).size());
		return p;
	}
	/**
	 * 删除
	 */
	@Override
	public boolean delete(String id) {
		String volumeId = id;
		boolean flag = false;
		int result = volumeMapper.deleteById(id);
		int cResult = vcMapper.delete(volumeId);
		int dResult = vdMapper.deleteByVolume(volumeId);
		if(result > 0 && cResult >= 0 && dResult > 0){
			flag = true;
		}
		return flag;
	}
	/**
	 * 推优
	 * @param subject
	 * @param price
	 * @return
	 */
	@Override
	public boolean updateVolumeType(String id,String subject,String subjectName,double price) {
		boolean flag = false;
		subject = subject.trim();
		DecimalFormat df = new DecimalFormat("######0.00");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String price1 = df.format(price);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		map.put("subject", subject);
		map.put("subjectName", subjectName);
		map.put("volumePrice", price1);
		map.put("publishDate", sdf.format(new Date()));
		int result = volumeMapper.updateVolumeType(map);
		if(result>0){
			flag = true;
		}
		return flag;
	}
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	@Override
	public Map<String, Object> queryDetails(String id) {
		String volumeId = id;
		Map<String,Object> map = new HashMap<String,Object>();
		Volume volume = volumeMapper.queryDetails(id);
		int volumeChapter = volume.getVolumeChapter();
		if(volumeChapter == 1){//分章节
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			List<Object> clist = vcMapper.queryChapter(volumeId);
			for(int i = 0;i < clist.size();i++){
				Map<String,Object> chapterMap = new HashMap<String,Object>();
				VolumeChapter vc = (VolumeChapter) clist.get(i);
				List<Object> dlist = vdMapper.queryByChapterId(vc.getChapterId());
				chapterMap.put("chapterTitle", vc.getChapterTitle());
				chapterMap.put("sections", dlist);
				list.add(chapterMap);
			}
			map.put("chapters", list);//章节
		}else{//不分章节
			List<Object> dlist = vdMapper.queryByVolumeId(volumeId);
			map.put("sections", dlist);//节
		}
		map.put("volume", volume);//文辑
		map.put("volumeChapter", volumeChapter);//是否分章节
		return map;
	}
	/**
	 * 发布和再发布文辑到solr
	 * @return
	 */
	@Override
	public boolean updateIssue(String id,String issue) {
		boolean flag = false;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		map.put("issue", Integer.valueOf(issue));
		int result = volumeMapper.updateIssue(map);
		if(result>0){
			flag = true;
		}
		return flag;
	}
	/**
	 * 修改价格
	 */
	@Override
	public boolean updatePrice(String price, String volumeId) {
		boolean flag = false;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", volumeId);
		map.put("volumePrice", price);
		int result = volumeMapper.updatePrice(map);
		if(result>0){
			flag = true;
		}
		return flag;
	}
	/**
	 * 修改文辑
	 */
	@Override
	public boolean updateVolume(Volume volume,String listContent) {
		boolean flag = false;
		List<Map<String,Object>> contList = JSONArray.fromObject(listContent);
		String volumeId = volume.getId();
		int result = volumeMapper.updateVolume(volume);
		if(result > 0){
			flag = true;
		}
		if(volume.getVolumeChapter() == 1){//分章节
			//--------------------删除章和节------------------
			int cResult = vcMapper.delete(volumeId);
			int dResult = vdMapper.deleteByVolume(volumeId);
			//--------------------重新添加章节----------------
			//-----------------添加章-----------------
			List<VolumeChapter> cList = new ArrayList<VolumeChapter>();
			for(int i = 0;i < contList.size();i++){
				String chapterTitle = (String) contList.get(i).get("chapterTitle");
				VolumeChapter vc = new VolumeChapter();
				String chapterId = GetUuid.getId();
				vc.setChapterId(chapterId);
				vc.setChapterTitle(chapterTitle);
				vc.setVolumeId(volumeId);
				vc.setNumber(i+1);
				vc.setAddTime(format.format(new Date()));
				//-----------------添加节-----------------
				List<VolumeDocu> dList = new ArrayList<VolumeDocu>();
				List<Map<String,Object>> list1 = (List<Map<String, Object>>) contList.get(i).get("sections");
				for(int j = 0;j < list1.size();j++){
					String docuId = (String) list1.get(j).get("docuId");
					String randomId = (String) list1.get(j).get("randomId");
					String docuType = (String) list1.get(j).get("docuType");
					String classType = (String) list1.get(j).get("classType");
					String docuTitle = (String) list1.get(j).get("docuTitle");
					String docuAuthor = (String) list1.get(j).get("docuAuthor");
					VolumeDocu vd = new VolumeDocu();
					vd.setId(GetUuid.getId());
					vd.setDocuId(docuId);
					vd.setRandomId(randomId);
					vd.setDocuType(docuType);
					vd.setClassType(classType);
					vd.setDocuTitle(docuTitle);
					vd.setDocuAuthor(docuAuthor);
					vd.setChapterId(chapterId);
					vd.setVolumeId(volumeId);
					vd.setNumber(j+1);
					vd.setAddTime(format.format(new Date()));
					dList.add(vd);
				}
				int dResult1 = vdMapper.insert(dList);//添加节
				cList.add(vc);
			}
			 int cResult1 = vcMapper.insert(cList);//添加章
		}else{//不分章节
			//--------------------删除节------------------
			int dResult = vdMapper.deleteByVolume(volumeId);
			//-------------------重新添加节----------------
			//-----------------添加节-----------------
			List<VolumeDocu> dList1 = new ArrayList<VolumeDocu>();
			for(int j = 0;j < contList.size();j++){
				String docuId = (String) contList.get(j).get("docuId");
				String randomId = (String) contList.get(j).get("randomId");
				String docuTitle = (String) contList.get(j).get("docuTitle");
				String docuAuthor = (String) contList.get(j).get("docuAuthor");
				String docuType = (String) contList.get(j).get("docuType");
				String classType = (String) contList.get(j).get("classType");
				VolumeDocu vd1 = new VolumeDocu();
				vd1.setId(GetUuid.getId());
				vd1.setDocuId(docuId);
				vd1.setRandomId(randomId);
				vd1.setDocuType(docuType);
				vd1.setClassType(classType);
				vd1.setDocuTitle(docuTitle);
				vd1.setDocuAuthor(docuAuthor);
				vd1.setVolumeId(volumeId);
				vd1.setNumber(j+1);
				vd1.setAddTime(format.format(new Date()));
				dList1.add(vd1);
			}
			int dResult1 = vdMapper.insert(dList1);//添加节
		}
		return flag;
	}
	@Override
	public List<Object> exportVolumeDocu(String startTime, String endTime,
			String searchWord, String volumeType, String volumeState,
			String sortColumn, String sortWay) {
		
		List<Object> list=new ArrayList<Object>();
		try {
			
			if(StringUtils.isEmpty(startTime)) startTime = null;
			if(StringUtils.isEmpty(endTime)) endTime = null;
			if(StringUtils.isEmpty(searchWord)) searchWord = null;
			if(StringUtils.isEmpty(volumeType)) volumeType = null;
			if(StringUtils.isEmpty(volumeState)) volumeState = null;
			if(StringUtils.isEmpty(sortColumn)) sortColumn = "publish_date";
			if(StringUtils.isEmpty(sortWay)) sortWay = "DESC";
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			map.put("searchWord", searchWord);
			map.put("volumeType", Integer.valueOf(volumeType));
			map.put("volumeState", volumeState == null ? null:Integer.valueOf(volumeState));
			map.put("sortColumn", sortColumn);
			map.put("sortWay", sortWay);
			
			list=volumeMapper.queryAll(map);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

}
