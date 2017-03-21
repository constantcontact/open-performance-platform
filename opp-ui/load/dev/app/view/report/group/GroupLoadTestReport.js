Ext.define('CCPerf.view.report.group.GroupLoadTestReport' ,{
    extend: 'Ext.panel.Panel',
    alias: 'widget.report-grouploadtest',
    reportExp:'',
    layout:'fit',
    defaults: {
        collapsible: true,
        split: true,
        bodyStyle: 'padding:15px'
    },
    initComponent: function() {

        var store = Ext.create('CCPerf.store.LoadTestSummaryTrendDynamic');
        store.id = 'summaryGroupStore' + new Date().getTime();
        store.getProxy().api.read = '/loadsvc/v1/loadtests/all/summaryTrendByGroup/?' + this.reportExp;
        store.load();

        this.items =  [
        {
            //   title: 'Main Content',
            collapsible: false,
            region: 'center',
            autoScroll: true,
            scope: this,
            layout:'vbox',
            margins: '5 0 0 0',
            items: [
                { xtype: 'group-load-test-report-header', height: 'auto', reportLink:this.reportLink  }
                ,{ xtype: 'cstpanel', flex: 1, childxtype: 'grid-grouploadtest-aggregates', title: 'Latest Trends', store:store }
                // ,{ xtype: 'cstpanel', colspan: 1, childxtype: 'chart-load-test-trend', yaxis: 'resp_pct90', title: '90th Percentile Response Time', loadTestId: loadTestId }
                // ,{ xtype: 'cstpanel', colspan: 1, childxtype: 'chart-load-test-trend', yaxis: 'resp_pct75', title: '75th Percentile Response Time', loadTestId: loadTestId }
                // ,{ xtype: 'cstpanel', colspan: 1, childxtype: 'chart-load-test-trend', yaxis: 'resp_avg', title: 'Average Response Time', loadTestId: loadTestId }
                // ,{ xtype: 'cstpanel', colspan: 1, childxtype: 'chart-load-test-trend', yaxis: 'resp_median', title: 'Median Response Time', loadTestId: loadTestId }
                // ,{ xtype: 'cstpanel', colspan: 1, childxtype: 'chart-load-test-trend', yaxis: 'tps_median', title: 'TPS Median', loadTestId: loadTestId }
                // ,{ xtype: 'cstpanel', colspan: 1, childxtype: 'chart-load-test-trend', yaxis: 'tps_max', title: 'TPS Max', loadTestId: loadTestId }
            ]
            }
        ];

        this.callParent(arguments);
    }
});

