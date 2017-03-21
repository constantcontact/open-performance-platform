Ext.define('CCPerf.view.report.group.GroupLoadTestReportHeader', {
    extend: 'Ext.container.Container',
    alias: 'widget.group-load-test-report-header',
    reportLink:'',
    initComponent: function() {
        var baseurl = window.location.origin + window.location.pathname;
        var url = baseurl += "#" + encodeURI(this.reportLink);
        this.html =  '<p><b>Direct URL:</b> <a href="'+url+'">'+url+'</a></p><p><b>TIP:</b> Double click on any row to drill down to that test run.</p>';
        this.callParent(arguments);
    }
});

