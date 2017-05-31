Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.groupreport.LoadTestGroupReportController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtestgroupreport',

    beforeGroupReportClose: function(tab) {            
        this.getView().up('loadtest')
            .getController()
            .updateUrlGroupTabState({name: tab.getTitle(), filters: tab.getFilters()}, false);
    }
});
