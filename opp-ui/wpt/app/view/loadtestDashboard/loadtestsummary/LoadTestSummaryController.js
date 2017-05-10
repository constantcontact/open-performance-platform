Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.LoadTestSummaryController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtestsummary',


    initViewModel: function() {
        var store = this.getView()
                    .getViewModel()
                    .getStore('remoteSummaryTrendFilter');
        
        // store.filterBy(function(record){
        //     return null;
        // });  
    },
    onRemoteSummaryTrend: function() {
        
    },
    showGroupReportForm: function() {
        var window, store;

        window = Ext.create('Ext.window.Window', {
            title: 'Grouped Report Creator',
            layout: 'vbox',
            itemId: 'createGroupReportForm',
            height:500,
            autoScroll:true,
            autoDestroy: true,
            closeAction: 'destroy',
            items: {  xtype:'loadtestgroupreportform' }
        });

        store = this.getView()
                    .getViewModel()
                    .getStore('remoteSummaryTrendFilter');

        // clear the grid when the build group
        // report button is clicked. 
        store.filterBy(function(record){
            return undefined;
        });

        this.getView().add(window).show();
    }
});
