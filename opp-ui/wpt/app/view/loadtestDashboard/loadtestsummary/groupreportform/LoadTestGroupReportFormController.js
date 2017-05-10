Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.groupreportform.LoadTestGroupReportFormController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtestgroupreportform',


    showSampleData: function() {
        var store, columnFilter, textFilter;

        // console.log(this.getView().down('#filterCombobox'));
        columnFilter = this.getView().down('#filterCombobox').getValue();
        textFilter = this.getView().down('#filterField').getValue();

        if(columnFilter && textFilter) {
            store = this.getView()
                    .up('loadtest')
                    .getViewModel()
                    .getStore('remoteSummaryTrendFilter');
        
            store.clearFilter();
            store.filterBy(function(record) {
                if(record.get(columnFilter).indexOf(textFilter) >= 0) {
                    return record;
                }
            });
        }
    },

    createReport: function() {
        var store, columnFilter, textFilter, groupReportName, view;
        view = this.getView();

        columnFilter = view.down('#filterCombobox').getValue();
        textFilter = view.down('#filterField').getValue();
        groupReportName = view.down('#groupReportName').getValue();

        if(columnFilter && textFilter && groupReportName) {

            view.up('loadtest').down('loadtestsummarytab').createGroupReportTab(groupReportName, columnFilter, textFilter);
            view.up().close();
        }
    }
});
