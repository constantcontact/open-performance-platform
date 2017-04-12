Ext.define('OppUI.model.loadtestDashboard.ApplicationMapping',  {
    extend: 'Ext.data.Model',
    fields: [{ name:'id', type: 'int', useNull: true }, 
        'appKey', 
        'newrelic', 
        'appdynamics', 
        'webpagetest', 
        'codeCoverageId', 
        'securityId', 
        'regressionResultsId', 
        'kqiAppName', 
        'splunk', 
        'dynatrace', 
        'teamName',
        { name: 'isClientSide', type: 'bool' },
        { name: 'isServerSide', type: 'bool' },
        { name: 'inCdPipelineClient', type: 'bool' },
        { name: 'inCdPipelineServer', type: 'bool' }
    ],
    /* this validations stops the request from being made until the data is valid */
    validations: [{
        type: 'length',
        field: 'appKey',
        min: 1
    }]

});
