package com.opp.config.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ctobe on 9/7/16.
 */
@Component
@ConfigurationProperties(prefix="opp.summaryTrends")
public class SummaryTrendsConfiguration {
    List<String> searchableColumns;
    List<String> trendOnColumns;

    public List<String> getSearchableColumns() {
        return searchableColumns;
    }

    public void setSearchableColumns(List<String> searchableColumns) {
        this.searchableColumns = searchableColumns;
    }

    public List<String> getTrendOnColumns() {
        return trendOnColumns;
    }

    public void setTrendOnColumns(List<String> trendOnColumns) {
        this.trendOnColumns = trendOnColumns;
    }
}
