package com.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;

/**
 * Solr工具类
 * 
 */
public class SolrService {
	private static SolrClient server=null;
	private static String hostUrl;
	private static final Logger log = Logger.getLogger(SolrService.class);
	
	public SolrService(String host){
		server=new HttpSolrClient(host);
	}
	
	public static synchronized void getInstance(String host) {
		server=new HttpSolrClient(host);
	}
	
	public static String getHost(){
		return hostUrl;
	}
	
	/**
	 * 查询数据
	 * @param solrQuery
	 */
	public static synchronized List<SolrInputDocument> getList(SolrQuery solrQuery){
		List<SolrInputDocument> sids = new ArrayList<SolrInputDocument>();
		QueryResponse response = null;
		try {
			response = server.query(solrQuery, METHOD.POST);
			SolrDocumentList list=response.getResults();
			log.info("查询条数："+list.size());
			for(SolrDocument sd:list){
				SolrInputDocument input=new SolrInputDocument();
				Map<String,Object> solrMap=sd.getFieldValueMap();//获取查询的数据
				for (String key : solrMap.keySet()) {
					Object obj=sd.getFieldValue(key);
					input.addField(key, obj);
				}
				sids.add(input);
			}
		} catch (Exception e) {
			log.error("检索失败:", e);
		}
		return sids;
	}
	
	/**
	 * 查询数据
	 * @param solrQuery
	 */
	public static synchronized SolrDocumentList getSolrList(SolrQuery solrQuery){
		SolrDocumentList list=new SolrDocumentList();
		try {
			QueryResponse response = server.query(solrQuery, METHOD.POST);
			list=response.getResults();
			log.info("查询条数："+list.size());
		} catch (Exception e) {
			log.error("检索失败:", e);
		}
		return list;
	}
	
	/**
	 * 查询总数
	 * @param solrQuery
	 */
	public static synchronized long getSolrCounts(String core,String query){
		long counts=0L;
		try {
			SolrQuery st = new SolrQuery();
			st.set("collection", core);
			st.setQuery(query);
			st.set("fl", "_version_");
			st.setRows(0);
			QueryResponse response = server.query(st, METHOD.POST);
			counts=response.getResults().getNumFound();
			return counts;
		} catch (Exception e) {
			log.error("检索失败:", e);
		}
		return counts;
	}
	

	/**
	 * 单个删除
	 * 
	 * @param id
	 */
	public static synchronized void deleteIndex(String id) {
		try {
			server.deleteByQuery("id:" + id);
			server.commit();
		} catch (Exception e) {
			rollback();
			log.error("删除索引失败.", e);
			e.printStackTrace();
		}
	}

	/**
	 * 根据solr查询条件从索引删除
	 * 
	 * @param query
	 */
	public static synchronized void deleteByQuery(String collection,String query) {
		try {
			server.deleteByQuery(query);
			server.commit();
		} catch (Exception e) {
			rollback();
			log.error("删除索引失败.", e);
			e.printStackTrace();
		}
	}

	/**
	 * 回滚操作
	 */
	public static UpdateResponse rollback() {
		try {
			log.error("创建索引异常,数据已回滚.");
			return server.rollback();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 批量添加
	 */
	public static synchronized void createList(List<Map<String, Object>> list) {
		try {
			List<SolrInputDocument> sids = new ArrayList<SolrInputDocument>();
			SolrInputDocument doc = null;
			for (Map<String, Object> map : list) {
				doc = new SolrInputDocument();
				for (String key : map.keySet()) {
					Object obj=map.get(key);
					String value="0";
					if(obj instanceof String){
						value=(String) obj;
					}
					if(obj!=null&&!StringUtils.isEmpty(value)){
						doc.addField(key, map.get(key));
					}else{
						doc.addField(key,null);
					}
				}
				doc.setField("_version_", 0);
				sids.add(doc);
			}
			UpdateRequest req = new UpdateRequest();
			req.add(sids);
			req.setCommitWithin(-1);
			req.process(server);
			server.commit(false, false);
		} catch (Exception e) {
			log.error("批量创建索引失败", e);
		}
	}
	
	/**
	 * 批量更新
	 */
	public static synchronized void updateList(List<Map<String, Object>> list) {
		try {
			List<SolrInputDocument> sids = new ArrayList<SolrInputDocument>();
			SolrInputDocument doc = null;
			for (Map<String, Object> map : list) {
				doc = new SolrInputDocument();
				for (String key : map.keySet()) {
					Object obj = map.get(key);
					if (key.equals("Id")) {
						doc.addField(key, obj);
					} else {
						Map<String, Object> oper = new HashMap<>();
						oper.put("set", obj);
						doc.addField(key, oper);
					}
				}
				doc.setField("_version_", 0);
				sids.add(doc);
			}
			UpdateRequest req = new UpdateRequest();
			System.out.println(sids.toString());
			req.add(sids);
			req.setCommitWithin(-1);
			req.process(server);
			server.commit(false, false);
		} catch (Exception e) {
			log.error("批量创建索引失败", e);
		}
	}

	/**
	 * 批量add专题聚焦、科技动态、基金会议、万方资讯
	 */
	public static synchronized void createIndexFound(List<Map<String, Object>> list) {
		try {
			Map<String,String> tokenMap=new HashMap<String,String>();
			List<SolrInputDocument> sids = new ArrayList<SolrInputDocument>();
			SolrInputDocument aSolrInputDocument = null;
			for (Map<String, Object> map : list) {
				aSolrInputDocument = new SolrInputDocument();
				for (String key : map.keySet()) {
					aSolrInputDocument.addField(key, map.get(key));
					if(key.contains("auto_")){
						tokenMap.put(key, key);
					}
				}
				sids.add(aSolrInputDocument);
			}
			UpdateRequest req = new UpdateRequest();
			Map<String,String[]> paramMap=new HashMap<String,String[]>();
			List<String> ls=new ArrayList<String>();
			for(String key:tokenMap.keySet()){
				ls.add(key);
			}
			String fields = StringUtils.join(ls, ",");
			paramMap.put("langid.fl", new String[] {fields});
			paramMap.put("mtf-langid.prependFields", new String[] {fields});
			if (paramMap != null) {
				req.setParams(new ModifiableSolrParams(paramMap));
			}
			req.add(sids);
			req.setCommitWithin(-1);
			req.process(server);
			server.commit(false, false);
		} catch (Exception e) {
			log.error("批量创建索引失败", e);
		}
	}
	
	/**
	 * 插入数据
	 * @param list 插入的数据集
	 * @param host 要插入的核地址
	 */
	public static synchronized void addIndexList(List<SolrInputDocument> list){
		try {
			UpdateRequest req = new UpdateRequest();
			req.add(list);
			req.setCommitWithin(-1);
			req.process(server);
			server.commit(true, true,false);
		} catch (Exception e) {
			log.error("创建索引失败:", e);
		}
	}
	
}