Ext.define('CCPerf.store.LoadTestSummaryTrendDynamic', {
    extend: 'Ext.data.Store',
    model: 'CCPerf.model.TestLoadGroupModel',
    autoLoad: false,
    sortOnLoad: true,
    sorters: { property: 'start_time', direction : 'DESC' },
    proxy: {
        type: 'ajax',
        api: {
            read: ''
        },
        reader: {
            type: 'json'
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