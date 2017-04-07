package com.opp.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opp.domain.ux.WptResult;
import com.opp.domain.ux.WptTest;
import com.opp.domain.ux.WptTestLabel;
import com.opp.domain.ux.WptUINavigation;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.toList;

/**
 * Created by ctobe on 3/22/17.
 */
@Component
public class WptTestDao {


    @Value("${opp.elasticsearch.wptData.index}")
    private String wptEsIndex;

    @Value("${opp.elasticsearch.wptData.type}")
    private String wptEsType;


    @Autowired
    private TransportClient esClient;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * Add new WPT Test
     * @param wptResult
     * @return
     */
    public IndexResponse insert(WptResult wptResult) {
        return esClient.prepareIndex(wptEsIndex, wptEsType, wptResult.getId())
                .setSource(wptResult)
                .get();
    }

    /**
     * Update WPT Test
     * @param wptResult
     */
    public UpdateResponse update(WptResult wptResult) throws ExecutionException, InterruptedException {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("index");
        updateRequest.type("type");
        updateRequest.id("1");
        updateRequest.doc(wptResult);
        return esClient.update(updateRequest).get();
    }

    /**
     * Get WPT Test by id
     * @param wptTestId
     * @return
     */
    public Optional<WptResult> findById(String wptTestId){
        GetResponse response = esClient.prepareGet(wptEsIndex, wptEsType, wptTestId).get();
        if(response.isSourceEmpty()){
            return Optional.empty();
        } else {
            return Optional.of(objectMapper.convertValue(response.getSource(), WptResult.class));
        }
    }
    

    /**
     * Find all WPT Tests
     * @return
     */
    public List<WptTest> findAll() {
        return new ArrayList<>();
    }


    /**
     * Find all WPT Tests
     * @return
     */
    public List<WptResult> findByLabel(String name) {
        SearchResponse resp = esClient.prepareSearch(wptEsIndex).setTypes(wptEsType)
                .setQuery(QueryBuilders.termQuery("label.full", name))
                //.setSource(SearchSourceBuilder.searchSource().fetchSource(new String[]{"id"}, new String[]{}))
                .get();
        return Arrays.stream(resp.getHits().getHits()).map(WptResult.class::cast).collect(toList());
    }



    /**
     * Delete WPT Test by id
     * @param id
     */
    public int delete(String id) {
        return 0;
    }


    public List<WptUINavigation> getNavigation() {
        SearchResponse resp = esClient.prepareSearch(wptEsIndex).setTypes(wptEsType)
                .addAggregation(AggregationBuilders.terms("agg").field("label.full.keyword").size(2000).order(Terms.Order.term(true))
                        .subAggregation(AggregationBuilders.max("max").field("completed"))).execute().actionGet();
        Terms terms = resp.getAggregations().get("agg");
        return terms.getBuckets().stream().map(b -> {
            Max max = b.getAggregations().get("max");
            return new WptUINavigation(b.getKeyAsString(), Double.doubleToLongBits(max.getValue()));
        }).collect(toList());

    }
}
