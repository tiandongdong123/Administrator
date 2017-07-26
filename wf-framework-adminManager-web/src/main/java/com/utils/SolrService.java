package com.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

/**
 * Solr工具类
 * 
 */
public class SolrService {
	private static SolrClient server=null;
	private static final Logger log = Logger.getLogger(SolrService.class);
	
	public SolrService(String host){
		server=new HttpSolrClient(host);
	}
	
	public static synchronized void getInstance(String host) {
		server=new HttpSolrClient(host);
	}
	
	/**
	 * 查询数据
	 * @param solrQuery
	 */
	public static synchronized List<SolrInputDocument> getIndexList(SolrQuery solrQuery){
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
					String val=obj.toString();
					//处理分词
					if(key.contains("stringITS")&&val.contains("|")){
						obj=val.substring(val.lastIndexOf("|")+1);
					}
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
			st.set("fl", "id");
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
}