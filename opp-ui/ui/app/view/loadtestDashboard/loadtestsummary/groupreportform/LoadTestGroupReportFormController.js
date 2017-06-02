Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.groupreportform.LoadTestGroupReportFormController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtestgroupreportform',


    showSampleData: function() {
        var store, columnFilter, textFilter;

        columnFilter = this.getView().down('#filterCombobox').getValue();
        textFilter = this.getView().down('#filterField').getValue();

        if(columnFilter && textFilter) {
            store = this.getView().down('grid').getStore();
        
            store.clearFilter();
            store.filterBy(function(record) {
                if(record.get(columnFilter).indexOf(textFilter) >= 0) {
                    return record;
                }
            });
        }
    },

    createReport: function(createReportButton) {
        var form = createReportButton.up('form'),
            queryStr = this.buildGroupedReportQueryStr(form),
            groupReport = this.buildGroupReportObject(form),
            formVals = form.getValues(),
            reportName = (formVals['report-name'] === '') ? 'Grouped Report' : formVals['report-name'],
            view = this.getView();
            groupReport.name = reportName;

            Ext.ComponentQuery.query('loadtestsummarytab')[0].getController().createGroupReport(groupReport);
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
    },

    populateSampleGrid: function(grid) {
        var defaultStore;

        defaultStore = Ext.ComponentQuery.query('loadtestsummarygrid')[0].getStore();
        
        grid.store.getProxy().setData(defaultStore.getProxy().getReader().rawData);
        grid.store.filterBy(function(record) {
            return undefined;
        });
        grid.store.load();
    }    
});
