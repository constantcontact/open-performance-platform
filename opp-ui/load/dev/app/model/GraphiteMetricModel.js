Ext.define('CCPerf.model.GraphiteMetricModel',  {
    extend: 'Ext.data.Model',
    fields: [{ name:'graphiteId', type: 'int', useNull: true }, {name:'applicationId' , type: 'int'}, 'appKey', 'name', 'graphitePath'],
    /* this validations stops the request from being made until the data is valid */
    validations: [{
        type: 'length',
        field: 'name',
        min: 2
    },
    {
        type: 'length',
        field: 'graphitePath',
        min: 5
    }],
    idProperty:'graphiteId'

});
