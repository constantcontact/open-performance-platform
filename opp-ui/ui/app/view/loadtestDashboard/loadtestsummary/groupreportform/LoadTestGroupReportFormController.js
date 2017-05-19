Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.groupreportform.LoadTestGroupReportFormController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtestgroupreportform',


    showSampleData: function() {
        var store, columnFilter, textFilter;

        // console.log(this.getView().down('#filterCombobox'));
        columnFilter = this.getView().down('#filterCombobox').getValue();
        textFilter = this.getView().down('#filterField').getValue();

        if(columnFilter && textFilter) {
            // store = this.getView()
            //         .up('loadtest')
            //         .getViewModel()
            //         .getStore('remoteSummaryTrendFilter');
            store = Ext.ComponentQuery.query('loadtest')[0].getViewModel().getStore('remoteSummaryTrendFilter');
        
            store.clearFilter();
            store.filterBy(function(record) {
                if(record.get(columnFilter).indexOf(textFilter) >= 0) {
                    return record;
                }
            });
        }
    },

    createReport: function(createReportButton) {
        // var store, columnFilter, textFilter, groupReportName, view;
        // view = this.getView();

        // columnFilter = view.down('#filterCombobox').getValue();
        // textFilter = view.down('#filterField').getValue();
        // groupReportName = view.down('#groupReportName').getValue();

        // if(columnFilter && textFilter && groupReportName) {

        //     view.up('loadtest').down('loadtestsummarytab').createGroupReportTab(groupReportName, columnFilter, textFilter);
        //     view.up().close();
        // }

        var form = createReportButton.up('form'),
            queryStr = this.buildGroupedReportQueryStr(form),
            groupReport = this.buildGroupReportObject(form),
            formVals = form.getValues(),
            reportName = (formVals['report-name'] === '') ? 'Grouped Report' : formVals['report-name'],
            view = this.getView();
            groupReport.name = reportName;

            console.log(reportName + ' ' + queryStr);
            console.log(groupReport);
            console.log(groupReport.filters);

            // view.up('loadtest').down('loadtestsummarytab').createGroupReport(groupReport);
            // view.up().close();

            Ext.ComponentQuery.query('loadtestsummarytab')[0].createGroupReport(groupReport);
            view.up().close();
    },
    buildGroupedReportQueryStr: function(form) {
        var data = form.getValues();
        var qStr = '';
        if(typeof data['columnName'] === 'string'){
            // process non-array
            qStr = data['columnName'] + '=' + data['criteria'];
        } else {
            // I have more than one criteria, so this is an array... loop through it
            for(var i=0; i<data['columnName'].length; i++){
                if(data['columnName'][i].trim() !== ''){
                    if(qStr !== '') qStr += '&';
                    qStr += data['columnName'][i] + '=' + data['criteria'][i];
                }
            }
        }
        return qStr;
    },
    buildGroupReportObject: function(form) {
        var groupReport = {name: '', filters: {}}, data = form.getValues();

        // process non-array
        if(typeof data['columnName'] === 'string') {
            groupReport.filters[[data['columnName']]] = data['criteria']; 
        } else {
             // I have more than one criteria, so this is an array... loop through it
            for(var i=0; i<data['columnName'].length; i++){
                if(data['columnName'][i].trim() !== ''){
                    groupReport.filters[data['columnName'][i]] = data['criteria'][i];
                }
            }
        }

        return groupReport;
    }
});
