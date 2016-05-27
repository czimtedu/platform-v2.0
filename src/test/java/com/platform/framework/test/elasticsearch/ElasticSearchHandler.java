/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.framework.test.elasticsearch;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author lufengc
 * @date 2016/2/18 16:00
 */
public class ElasticSearchHandler {

    private Client client;

    public ElasticSearchHandler() throws UnknownHostException {
        //使用本机做为节点
        this("127.0.0.1");
    }

    public ElasticSearchHandler(String ipAddress) throws UnknownHostException {
        //集群连接超时设置
        /*Settings settings = Settings.settingsBuilder()
                .put("client.transport.ping_timeout", "10s")
                .put("cluster.name", "cluster1")
                .build();
        client = TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ipAddress), 9300));*/
        client = TransportClient.builder().build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ipAddress), 9300));
    }


    /**
     * 建立索引
     *
     * @param index    为索引库名，一个es集群中可以有多个索引库。 名称必须为小写
     * @param type     Type为索引类型，是用来区分同索引库下不同类型的数据的，一个索引库下可以有多个索引类型。
     * @param jsonData json格式的数据集合
     * @return
     */
    public void createIndex(String index, String type, List<String> jsonData) {
        //创建索引库 需要注意的是.setRefresh(true)这里一定要设置,否则第一次建立索引查找不到数据
        IndexRequestBuilder requestBuilder = client.prepareIndex(index, type).setRefresh(true);
        for (String aJsondata : jsonData) {
            requestBuilder.setSource(aJsondata).execute().actionGet();
        }

    }

    /**
     * 创建索引
     *
     * @param index    为索引库名，一个es集群中可以有多个索引库。 名称必须为小写
     * @param type     Type为索引类型，是用来区分同索引库下不同类型的数据的，一个索引库下可以有多个索引类型。
     * @param jsonData json格式的数据
     * @return
     */
    public IndexResponse createIndex(String index, String type, String jsonData) {
        return client.prepareIndex(index, type).setRefresh(true)
                .setSource(jsonData)
                .execute()
                .actionGet();
    }

    /**
     * 执行搜索
     *
     * @param queryBuilder
     * @param index
     * @param type
     * @return
     */
    public List<Medicine> searcher(QueryBuilder queryBuilder, String index, String type) {
        List<Medicine> list = new ArrayList<>();
        SearchResponse searchResponse = client.prepareSearch(index).setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .execute()
                .actionGet();
        SearchHits hits = searchResponse.getHits();
        System.out.println("查询到记录数=" + hits.getTotalHits());
        SearchHit[] searchHists = hits.getHits();
        if (searchHists.length > 0) {
            for (SearchHit hit : searchHists) {
                Integer id = (Integer) hit.getSource().get("id");
                String name = (String) hit.getSource().get("name");
                String function = (String) hit.getSource().get("funciton");
                list.add(new Medicine(id, name, function));
            }
        }
        return list;
    }


    public static void main(String[] args) throws UnknownHostException {
        ElasticSearchHandler esHandler = new ElasticSearchHandler();
        String index = "indexdemo";
        String type = "typedemo";
        List<String> jsonData = DataFactory.getInitJsonData();
        esHandler.createIndex(index, type, jsonData);
        //查询条件
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("止咳糖浆");
        /*QueryBuilder queryBuilder = QueryBuilders.boolQuery()
          .must(QueryBuilders.termQuery("id", 1));*/
        List<Medicine> result = esHandler.searcher(queryBuilder, index, type);
        for (Medicine medicine : result) {
            System.out.println("(" + medicine.getId() + ")药品名称:" + medicine.getName() + "\t\t" + medicine.getFunction());
        }
    }
}
