Ext.define('CCPerf.store.ReportUxTrendGridStore', {
    extend: 'Ext.data.Store',
     model: 'CCPerf.model.ReportUxTrendGridModel',
  //  sortOnLoad: true,
  //  sorters: { property: 'date', direction : 'DESC' },
    proxy: {
        type: 'ajax',
        api: {
            read: 'api/report-trend-grid.php'
        },
        reader: {
            type: 'json',
            rootProperty: 'report',
            successProperty: 'success'
        },
        actionMethods: {
            create: 'POST', read: 'POST', update: 'POST', destroy: 'POST'
        }
    }
});

/*
 
 {
    "success": true,
    "tests": [
        {"id": 1, "name": 'Ed',    "email": "ed@sencha.com"},
        {"id": 2, "name": 'Tommy', "email": "tommy@sencha.com"}
    ]
}

 */