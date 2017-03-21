Ext.define('CCPerf.store.TestsLoadStore', {
    extend: 'Ext.data.Store',
     model: 'CCPerf.model.TestLoadModel',
   // autoLoad: true,
    sortOnLoad: true,
    sorters: { property: 'start_time', direction : 'DESC' },
    proxy: {
        type: 'ajax',
        api: {
            read: '/loadsvc/v1/loadtesttrends/summarytrend'
        },
        reader: {
            type: 'json',
            rootProperty: 'results',
            successProperty: 'success'
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
