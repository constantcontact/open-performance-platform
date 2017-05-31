Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.groupreport.LoadTestGroupReportHeader', {
    extend: 'Ext.container.Container',
    alias: 'widget.loadtestgroupreportheader',
    reportLink:'',
    initComponent: function() {
        var url = this.reportLink;
        this.html =  '<p><b>Direct URL:</b> <a href="'+url+'">'+url+'</a></p><p><b>TIP:</b> Double click on any row to drill down to that test run.</p>';
        this.callParent(arguments);
    }
});
