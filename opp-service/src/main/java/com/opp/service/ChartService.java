package com.opp.service;

import com.opp.dao.LoadTestAggregateDao;
import com.opp.dao.LoadTestDao;
import com.opp.dao.LoadTestDataDao;
import com.opp.domain.ChartDetails;
import com.opp.domain.LoadTest;
import com.opp.domain.LoadTestAggregateView;
import com.opp.domain.LoadTestTimeChartData;
import com.opp.dto.ChartDataResponse;
import com.opp.dto.maintenance.TimeSeriesChartResponse;
import com.opp.util.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

/**
 * Created by jhermida on 8/26/16.
 */
@Service
public class ChartService {
    @Autowired
    private LoadTestDao loadTestDao;
    @Autowired
    private LoadTestDataDao loadTestDataDao;
    @Autowired
    private LoadTestAggregateDao loadTestAggregateDao;


    public List<LoadTestAggregateView> aggregateChartDataByLoadTestId(ChartDetails chartDetails) {

        Optional<LoadTest> loadTest = loadTestDao.findById(chartDetails.getLoadTestId());

        List<LoadTestAggregateView> aggregates = Collections.EMPTY_LIST;
        if(loadTest.isPresent()) {
            Set<String> trends = new HashSet<>();
            if(!StringUtils.isEmpty(chartDetails.getTrendingBy())) {
                trends.addAll(new HashSet<>(Arrays.asList(chartDetails.getTrendingBy().split(","))));
            } else {
                // default
                trends.add("test_name");
            }

            // add the defaults
            trends.add("vuser_count");
            trends.add("test_sub_name");

            aggregates = loadTestAggregateDao.findByTrends(trends, loadTest.get());
        }

        return aggregates;
    }

    public List<LoadTestTimeChartData> timeseriesChartDataByLoadTestId(ChartDetails chartDetails) {
        Optional<LoadTest> loadTest = loadTestDao.findById(chartDetails.getLoadTestId());

        List<LoadTestTimeChartData> loadTestData = Collections.EMPTY_LIST;

        if(loadTest.isPresent()) {
            loadTestData = loadTestDataDao.getLineTimeSeries(chartDetails, loadTest.get());
        }

        return loadTestData;

    }


    private String buildChartTitle(String yAxis) {
        String chartTitle = null;

        if(yAxis.contains("resp_pct")) {
            // extract the percentile.
            String[] values = yAxis.split("resp_pct");
            int percentile = Integer.parseInt(values[1]);

            chartTitle = percentile + "th Percentile Response Time During Test";

        } else {

            switch (yAxis) {
                case "resp_min":
                    chartTitle = "Min Response Time During Test";
                    break;
                case "resp_max":
                    chartTitle = "Max Response Time During Test";
                    break;
                case "resp_avg":
                    chartTitle = "Average Response Time During Test";
                    break;
                case "resp_median":
                    chartTitle = "Median Response Time During Test";
                    break;
                case "resp_stddev":
                    chartTitle = "Standard Deviation of Response Time During Test";
                    break;
                case "call_count":
                    chartTitle = "Transaction per Minute During Test";
                    break;
                case "error_count":
                    chartTitle = "Errors per Minute During Test";
                    break;
                case "total_bytes_received":
                    chartTitle = "Total Bytes Received per Minute During Test";
                    break;
                case "total_bytes_sent":
                    chartTitle = "Total Bytes Sent per Minute During Test";
                    break;
                case "total_bytes":
                    chartTitle = "Total Bytes per Minute During Test";
                    break;
            }
        }

        return chartTitle;
    }

    public ChartDataResponse mapToAggregateChartResponse(ChartDetails chartDetails, List<LoadTestAggregateView> loadTestAggregates) {

        // get the unique plot by fields.
        Set<String> plotByFields = new TreeSet<>();
        loadTestAggregates.forEach(aggregate ->
                plotByFields.add(ReflectionUtil.getPropertyValueFromObject(aggregate, chartDetails.getPlotBy(), true).get()));

        // build the series object
        List<ChartDataResponse.LineSeries> series = plotByFields.stream()
                .map(plot -> new ChartDataResponse.LineSeries("xaxis", plot)).collect(Collectors.toList());

        // build the data field list for the model.
        List<String> modelFields = new ArrayList<>();
        modelFields.add("xaxis");
        modelFields.addAll(plotByFields);

        // group aggregates by load test id in sorted order.
        Map<Integer, List<LoadTestAggregateView>> loadTestIdGrouping = loadTestAggregates.stream()
                .collect(groupingBy(loadTestAggregate -> loadTestAggregate.getLoadTestId(),
                        TreeMap::new,
                        mapping(Function.identity(), toList())));

        List<Map<String, String>> data = loadTestIdGrouping.entrySet().stream()
                .map(entry -> plotByDataMap(chartDetails, entry.getValue()))
                .collect(Collectors.toList());

        return new ChartDataResponse(data, modelFields, series);
    }

    public ChartDataResponse mapToTimeSeriesChartResponse(ChartDetails chartDetails, List<LoadTestTimeChartData> loadTestData) {

        // get the unique plot by fields.
        Set<String> plotByFields = new TreeSet<>();
        loadTestData.forEach(aggregate ->
                plotByFields.add(ReflectionUtil.getPropertyValueFromObject(aggregate, chartDetails.getPlotBy(), true).get()));

        // build the series object
        List<ChartDataResponse.LineSeries> series = plotByFields.stream()
                .map(plot -> new ChartDataResponse.LineSeries("start_time", plot)).collect(Collectors.toList());

        // build the data field list for the model.
        List<String> modelFields = new ArrayList<>();
        modelFields.add("start_time");
        modelFields.addAll(plotByFields);

        List<Map<String, String>> data = loadTestData.stream()
                .map(loadTestTimeChartData -> plotByTimeSeriesData(chartDetails, loadTestTimeChartData))
                .collect(Collectors.toList());

        TimeSeriesChartResponse timeSeriesChartResponse = new TimeSeriesChartResponse(data, modelFields, series);
        timeSeriesChartResponse.setTitle(buildChartTitle(chartDetails.getyAxis()));

        return new ChartDataResponse(buildChartTitle(chartDetails.getyAxis()), data, modelFields, series);
    }

    private Map<String, String> plotByDataMap(ChartDetails chartDetails,
                                              List<LoadTestAggregateView> loadTestAggregateViews) {
        Map<String, String> plotByDataMap = new LinkedHashMap<>();

        loadTestAggregateViews.forEach(aggregate -> {
            String plotByValue = ReflectionUtil.getPropertyValueFromObject(aggregate, chartDetails.getPlotBy(), true).get();
            String xAxisValue = ReflectionUtil.getPropertyValueFromObject(aggregate, chartDetails.getxAxis(), true).get();
            String yAxisValue = ReflectionUtil.getPropertyValueFromObject(aggregate, chartDetails.getyAxis(), true).get();

            plotByDataMap.putIfAbsent("xaxis", xAxisValue);
            plotByDataMap.put(plotByValue, yAxisValue);
            plotByDataMap.putIfAbsent("id", Integer.toString(aggregate.getLoadTestId()));
        });

        return plotByDataMap;
    }

    private Map<String, String> plotByTimeSeriesData(ChartDetails chartDetails, LoadTestTimeChartData data) {
        Map<String, String> plotByDataMap = new HashMap<>();


        String plotByValue = ReflectionUtil.getPropertyValueFromObject(data, chartDetails.getPlotBy(), true).get();
        String xAxisValue = ReflectionUtil.getPropertyValueFromObject(data, chartDetails.getxAxis(), true).get();
        String yAxisValue = ReflectionUtil.getPropertyValueFromObject(data, chartDetails.getyAxis(), true).get();

        plotByDataMap.put("start_time", xAxisValue);
        plotByDataMap.put(plotByValue, yAxisValue);

        return plotByDataMap;
    }
}
