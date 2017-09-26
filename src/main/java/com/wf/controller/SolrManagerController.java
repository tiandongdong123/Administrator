package com.wf.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.redis.RedisUtil;
import com.utils.SolrService;
import com.xxl.conf.core.XxlConfClient;


/**
 * 下撤solr数据的管理类
 * @author oygy
 * @date 2017-04-12 13:34:27
 *
 */
@Controller
@RequestMapping("solr")
public class SolrManagerController {
	
	private static Logger log = Logger.getLogger(SolrManagerController.class);
	private static final String PERIO="perio";//期刊  4种情况
	private static final String CONFERENCE="conference";//会议 2种情况
	private static final String DEGREE="degree";//学位
	private static final String GAZETTEER_NEW="gazetteer_new";//新方志 2种情况
	private static final String GAZETTEER_OLD="gazetteer_old";//旧方志 2种情况
	private static final String YEARBOOKS="Yearbooks";//年鉴 3种情况
	private static final String PATENT="patent";//专利
	private static final String STANDARDS="standards";//标准
	private static final String LEGISLATIONS="legislations";//法规
	private static final String TECH="tech";//科技报告
	private static final String TECHRESULT="techResult";//科技成果
	private static final String VIDEO="video";//视频
	private static final String APABIEBOOKS="books";//图书
	private static final String SCHOLARS="scholars";//学者
	private static final String UNIT="unit";//机构
	private static final String BLOB="blob";//博文
	private static final String TBOOKS="tbooks";//工具书
	//旧平台的方志
	private static final String WFLocalChronicle_FZ="WFLocalChronicle_FZ";//志书
	private static final String WFLocalChronicleItem_FZ="WFLocalChronicleItem_FZ";//条目
	
	private static final String front="stringIS_";
	
	private RedisUtil redis = new RedisUtil();
	/**
	 * 跳转到下撤界面
	 * @param model
	 * @return
	 */
	@RequestMapping("solrManager")
	public String solrManager(Map<String,Object> model){
		return "/page/solrmanager/solr_manager";
	}
	
	@RequestMapping("deleteSolr")
	@ResponseBody
	public HashMap<String,Object> deleteSolr(HttpServletRequest request,HttpServletResponse response){
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		try{
			String model = request.getParameter("model");// 模块类型
			String param = request.getParameter("param");// 字段名称
			String dataid = request.getParameter("data");// 数据
			if (model == null || "".equals(model.trim())) {
				String msg = "类型为空";
				log.info(msg);
				map.put("msg", msg);
				return map;
			}
			String core = this.getCore(model);
			if (core == null || "".equals(core)) {
				String msg = "根据" + model + "查询到的core为空";
				log.info(msg);
				map.put("msg", msg);
				return map;
			}
			if (dataid == null || "".equals(dataid)) {
				String msg = "没有上传数据";
				log.info(msg);
				map.put("msg", msg);
				return map;
			}
			dataid = this.getIdValues(dataid);
			String [] strs=dataid.split(" ");
			//分组下撤每批400个
			int batchSize=400;
			List<String> list=new ArrayList<String>();
			StringBuffer idBuff=new StringBuffer();
			for(int i=0;i<strs.length;i++){
				idBuff.append(strs[i]).append(" ");
				if((i+1)%batchSize==0){
					list.add(idBuff.toString().trim());
					idBuff=new StringBuffer();
				}
			}
			if(idBuff.length()>0){
				list.add(idBuff.toString().trim());
			}
			String url = "";
			// 旧平台的solr地址和新平台的solr地址不一致
			if (WFLocalChronicle_FZ.equals(model) || WFLocalChronicleItem_FZ.equals(model)) {
				url = XxlConfClient.get("wf-public.oldsolr.url", null);
			} else {
				url = XxlConfClient.get("wf-public.solr.url", null);
			}
			// 开始查询solr
			if(!url.endsWith("/")){
				SolrService.getInstance(url +"/"+ core);
			}else{
				SolrService.getInstance(url + core);
			}
			long allNum=0L;
			for(String id:list){
				String query = "";
				switch(model){
					case PERIO: //期刊
						query=this.perio(param,id);
						break;
					case CONFERENCE: //会议
						query=this.conference(param,id);
						break;
					case DEGREE: //学位
						query=this.degree(id);
						break;
					case GAZETTEER_NEW: //旧方志
						query=this.gazetteer_new(param,id);
						break;
					case GAZETTEER_OLD: //新方志
						query=this.gazetteer_old(id);
						break;
					case YEARBOOKS: //年鉴
						query=this.Yearbooks(param,id);
						break;
					case PATENT: //专利
						query=this.patent(id);
						break;
					case STANDARDS: //标准
						query=this.standards(id);
						break;
					case LEGISLATIONS: //法规
						query=this.legislations(id);
						break;
					case TECH: //科技报告
						query=this.tech(id);
						break;
					case TECHRESULT: //科技成果
						query=this.techresult(id);
						break;
					case VIDEO: //视频
						query=this.video(id);
						break;
					case APABIEBOOKS: //阿帕比电子书
						query=this.apabiebooks(id);
						break;
					case SCHOLARS: //学者
						query=this.scholars(id);
						break;
					case UNIT: //机构
						query=this.unit(id);
						break;
					case BLOB: //博文
						query=this.blob(id);
						break;
					case TBOOKS: //工具书
						query=this.tbooks(id);
						break;
					case WFLocalChronicle_FZ: //旧平台志书
						query=this.localChronicle(id);
						break;
					case WFLocalChronicleItem_FZ: //旧平台条目
						query=this.localChronicle(id);
						break;
					default:
						log.info("未找到正确的类型："+model);
				}

				//如果是年鉴的话必须先查询再删除
				if (YEARBOOKS.equals(model) && "single_id".equals(param)) {
					SolrQuery st = new SolrQuery();
					st.set("collection", core);
					st.setQuery(query.replace("stringIS_single_id","stringIS_series_id"));
					st.set("fl", "stringIS_volume_id");
					st.setRows(100000);//最大10万
					SolrDocumentList vlist=SolrService.getSolrList(st);
					StringBuffer sb=new StringBuffer(" OR stringIS_volume_id:(");
					for(SolrDocument sd:vlist){
						sb.append(sd.getFieldValue("stringIS_volume_id")+" ");
					}
					sb.append(")");
					query=query+sb.toString();
				}
				long counts = SolrService.getSolrCounts(core, query);
				allNum+=counts;
				log.info(core+"模块本批次删除" + counts + "条记录,删除条件是：["+query+"]");
				// 开始删除
				SolrService.deleteByQuery(core, query);
			}

			log.info(core + "模块删除" + allNum + "条记录");
			map.put("msg", "共删除" + allNum + "条记录");
		} catch (Exception e) {
			map.put("msg", "执行下撤过程中异常");
			e.printStackTrace();
		}
		return map;
	}
	
	//旧平台方志
	private String localChronicle(String id) {
		return "Id:(" + id + ")";
	}

	//期刊
	private String perio(String param,String id) {
		if (!StringUtils.isEmpty(id)) {
			if ("perio_id".equals(param)) {
				return front + "perio_id:(" + id + ")";
			} else if ("perio_id_y".equals(param)) {
				return this.perioYear(id);
			} else if ("perio_id_i".equals(param)){
				return this.perioYearIssue(id);
			} else if ("article_id".equals(param)) {
				return front + "article_id:(" + id + ")";
			}
		}
		return "";
	}
	
	private String perioYear(String id){
		Map<String,String> mapM=new HashMap<String,String>();
		String[] ids=id.split(" ");
		for(int i=0;i<ids.length;i++){
			String[] strs=ids[i].split("_");
			if(strs.length==2){
				if(mapM.get(strs[0])!=null){
					String publish_year=mapM.get(strs[0]);
					publish_year=publish_year+" "+strs[1];
					mapM.put(strs[0],publish_year.trim());
				}else{
					mapM.put(strs[0], strs[1].trim());
				}
			}
		}
		//生成solr查询语句
		StringBuffer sb = new StringBuffer("");
		boolean flag=false;
		for (Map.Entry<String, String> en : mapM.entrySet()) {
			if (flag) {
				sb.append(" OR ");
			}else{
				flag=true;
			}
			sb.append("(").append(front).append("perio_id:")
					.append(en.getKey()).append(" AND ").append(front)
					.append("publish_year:(").append(en.getValue())
					.append("))");
		}
		return sb.toString();
	}
	
	private String perioYearIssue(String id){
		Map<String,String> mapM=new HashMap<String,String>();
		String[] ids=id.split(" ");
		for(int i=0;i<ids.length;i++){
			String[] strs=ids[i].split("_");
			if(strs.length==3){
				String key=strs[0]+"_"+strs[1];
				if(mapM.get(key)!=null){
					String issue=mapM.get(key);
					issue=issue+" "+strs[2];
					mapM.put(key,issue.trim());
				}else{
					mapM.put(key, strs[2].trim());
				}
			}
		}
		//生成solr查询语句
		StringBuffer sb = new StringBuffer("");
		boolean flag=false;
		for (Map.Entry<String, String> en : mapM.entrySet()) {
			if (flag) {
				sb.append(" OR ");
			}else{
				flag=true;
			}
			String[] keys=en.getKey().split("_");
			sb.append("(").append(front).append("perio_id:")
					.append(keys[0]).append(" AND ").append(front)
					.append("publish_year:").append(keys[1])
					.append(" AND ").append(front).append("issue_num:("+en.getValue()+"))");
		}
		return sb.toString();
	}
	
	//会议
	private String conference(String param,String id) {
		if (!StringUtils.isEmpty(id)) {
			if ("conf_id".equals(param)) {
				return front + "conf_id:(" + id + ")";
			} else if ("article_id".equals(param)) {
				return front + "article_id:(" + id + ")";
			}
		}
		return "";
	}
	//新方志
	private String gazetteer_new(String param,String id) {
		if (!StringUtils.isEmpty(id)) {
			if ("gazetteers_id".equals(param)) {// 按志书ID
				return front + "gazetteers_id:(" + id + ")";
			} else if ("item_id".equals(param)) {// 按照条目ID
				return front + "item_id:(" + id + ")";
			}
		}
		return "";
	}
	
	//年鉴
	private String Yearbooks(String param,String id){
		if (!StringUtils.isEmpty(id)) {
			if ("single_id".equals(param)) { //单种ID
				return front + "single_id:(" + id + ")";
			} else if ("volume_id".equals(param)) { //单卷ID
				return front + "volume_id:(" + id + ")";
			}else if("item_id".equals(param)){ //条目ID
				return front + "item_id:(" + id + ")";
			}
		}
		return "";
	}
	
	//学位
	private String degree(String id){//论文id
		return front+"article_id:("+id+")";
	}
	
	//旧方志
	private String gazetteer_old(String id){//按志书ID
		return front+"gazetteers_id:("+id+")";
	}
	
	//专利
	private String patent(String id){//专利ID
		return front+"patent_id:("+id+")";
	}
	//标准
	private String standards(String id){//标准ID
		return front+"stand_id:("+id+")";
	}
	//法规
	private String legislations(String id){//法规ID
		return front+"legis_id:("+id+")";
	}
	//科技报告
	private String tech(String id){//科技报告ID
		return front+"report_id:("+id+")";
	}
	//科技成果
	private String techresult(String id){//科技成果ID
		return front+"result_id:("+id+")";
	}
	//视频
	private String video(String id){//视频ID
		return front+"video_sid:("+id+")";
	}
	//阿帕比电子书
	private String apabiebooks(String id){//电子书ID
		return front+"ebook_id:("+id+")";
	}
	//学者
	private String scholars(String id){//学者ID
		return front+"scholar_id:("+id+")";
	}
	//机构
	private String unit(String id){//机构ID
		return front+"org_id:("+id+")";
	}
	//博文
	private String blob(String id){//博文ID
		return front+"record_id:("+id+")";
	}
	//工具书
	private String tbooks(String id){//工具书ID
		return front+"ref_b_id:("+id+")";
	}
	//获取core
	private String getCore(String key) {
		return redis.get(key, 3);//solr核存在db3中
	}
	//获取的值用空格分开
	private String getIdValues(String val){
		val=val.replaceAll("\r", " ");
		val=val.replaceAll("\n", " ");
		val=val.replaceAll("  ", "");
		return val;
	}
}
