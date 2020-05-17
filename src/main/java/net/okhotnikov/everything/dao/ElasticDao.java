package net.okhotnikov.everything.dao;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static net.okhotnikov.everything.util.Literals.DATE_FORMAT;

/**
 * Created by Sergey Okhotnikov.
 */
@Repository
public class ElasticDao {

    private final RestHighLevelClient client;

    public ElasticDao(RestHighLevelClient client) {
        this.client = client;
    }

    public GetResponse get(String index, String id) throws IOException {
        return client.get(new GetRequest(index, id), RequestOptions.DEFAULT);
    }

    public MultiGetResponse multiGet(String index, Set<String> ids) throws IOException {
        MultiGetRequest request = new MultiGetRequest();
        ids.forEach(id-> request.add(new MultiGetRequest.Item(index,id)));
        return client.mget(request, RequestOptions.DEFAULT);
    }

    public long count(String index) throws IOException {
        CountRequest countRequest = new CountRequest(index);
        countRequest.query(QueryBuilders.matchAllQuery());
        return client.count(countRequest,RequestOptions.DEFAULT).getCount();
    }

    public SearchResponse all(String index) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        return client.search(new SearchRequest(new String[]{index},searchSourceBuilder),RequestOptions.DEFAULT);
    }

    public void put(String index, String id, String json) throws IOException {
        IndexRequest request = new IndexRequest(index);
        request.id(id);
        request.source(json, XContentType.JSON);
        client.index(request,RequestOptions.DEFAULT);
    }

    public void delete(String id, String index) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(index,id);
        DeleteResponse response = client.delete(deleteRequest,RequestOptions.DEFAULT);
    }

    public void update(String index, String id, Map<String, Object> data) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(index, id).doc(data);
        client.update(updateRequest,RequestOptions.DEFAULT);
    }

    public List<Map<String, Object>> getByField(String index, String field, String value, int size) throws IOException {
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(field, value);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(matchQueryBuilder)
                .from(0)
                .size(size);

        SearchHit[] hits =
                client.search(new SearchRequest(new String[]{index},sourceBuilder),RequestOptions.DEFAULT)
                .getHits().getHits();

        return Arrays
                .stream(hits)
                .map(SearchHit::getSourceAsMap)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getAfter(String index, String field, String value) throws IOException {
        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder(field).gt(value).format(DATE_FORMAT);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(rangeQueryBuilder)
                .from(0);

        SearchHit[] hits =
                client.search(new SearchRequest(new String[]{index},sourceBuilder),RequestOptions.DEFAULT)
                        .getHits().getHits();

        return Arrays
                .stream(hits)
                .map(SearchHit::getSourceAsMap)
                .collect(Collectors.toList());
    }
}