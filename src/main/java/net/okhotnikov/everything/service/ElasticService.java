package net.okhotnikov.everything.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.okhotnikov.everything.dao.ElasticDao;
import net.okhotnikov.everything.util.DataUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static net.okhotnikov.everything.util.Literals.*;

/**
 * Created by Sergey Okhotnikov.
 */
@Service
public class ElasticService {

    public static final String BOOK = "book";
    public static final String RULES = "rules";
    public static final String USERS = "users";
    public static final DateTimeFormatter DATE_FORMATTER =DateTimeFormatter.ofPattern(DATE_FORMAT);
    public static final String BOOK_ORDER = "number";

    @Value("${app.quotations.response}")
    private int quotationsCount;

    private final ElasticDao dao;
    private final ObjectMapper mapper;


    public ElasticService(ElasticDao dao, ObjectMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    public Map<String, Object> get(String id) throws IOException {
        return get(id,BOOK);
    }

    public Map<String, Object> get(String id,String index) throws IOException {
        return dao.get(index,id).getSource();
    }

    public <T> T get(String id, String index, TypeReference<T> typeReference) throws IOException {
        Map<String, Object> res = get(id,index);
        return res == null ? null : mapper.convertValue(get(id, index),typeReference);
    }

    public <T> T getByUniqueField(String index, String field, String value, TypeReference<T> typeReference) throws IOException {
        List<Map<String,Object>> res = dao.getByField(index,field,value,1);
        return mapper.convertValue(res.get(0),typeReference);
    }


    public <T> List<T> getAfter(String index, String field, LocalDate value, TypeReference<T> typeReference) throws IOException{
        List<Map<String,Object>> res = dao.getValidAfter(index,field,value.format(DATE_FORMATTER));

        return res
                .stream()
                .map(val -> mapper.convertValue(val, typeReference))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> multiGet(Set<String> ids) throws IOException {
        return Arrays
                .stream(dao.multiGet(BOOK,ids)
                .getResponses())
                .map(res->res.getResponse().getSource())
                .collect(Collectors.toList());
    }

    public void update(String index, String id, Map<String,Object> data) throws IOException {
        dao.update(index,id,data);
    }

    public int countChapters() throws IOException {
        return (int) dao.count(BOOK);
    }

    public List<Map<String, Object>> getQuotations() throws  IOException {
        int count = countChapters();
        Set<String> ids = new HashSet<>();
        for(int i=0;i<quotationsCount; i++){
            ids.add(String.valueOf( ThreadLocalRandom.current().nextInt(1, count + 1)));
        }
        List<Map<String, Object>>  res = multiGet(ids);
        res.forEach(DataUtil::retainQuotations);
        return res;
    }

    public List<Map<String, Object>> getRules() throws IOException {
        SearchResponse response = dao.all(RULES);
        return  Arrays
                .stream(response.getHits().getHits())
                .map(SearchHit::getSourceAsMap)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getChaptersWithText(String text) throws IOException {
        return dao.getWithText(BOOK,text, BOOK_ORDER, SortOrder.DESC);
    }
}
