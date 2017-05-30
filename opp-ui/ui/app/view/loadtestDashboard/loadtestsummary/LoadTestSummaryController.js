Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.LoadTestSummaryController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtestsummary',

    showGroupReportForm: function() {
        var window, store;

         store = this.getView()
                    .up('loadtest')
                    .getViewModel()
                    .getStore('remoteSummaryTrendFilter');

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

       

        // clear the grid when the build group
        // report button is clicked. 
        store.filterBy(function(record){
            return undefined;
        });

        //this.getView().up('loadtest').add(window);
        //this.getView().up('loadtestsummarytab').add(window);
        window.show();
    },

    search: function() {},
    specialkey: function(field, e) {
        if(e.getKey() === window.parseInt(e.ENTER)) {
            var searchString, store, view;

            searchString = field.getValue();
            view = this.getView();

            store = view.up('loadtest')
                        .getViewModel()
                        .getStore('remoteSummaryTrend');
            
            if (!searchString || searchString.length === 0) {
                //this.up('grid').getController().reBuildGridByCurrentFilterState();
                store.clearFilter();
            } else {
                store.filterBy(function(record) {
                    var app, env; 

                    app = record.get('appUnderTest');
                    env = record.get('environment');
 
                    if (app.indexOf(searchString) >= 0 ||
                        env.indexOf(searchString) >= 0) {
                        return true;
                    }

                    return false;
                });
            }
        }
    }
});
