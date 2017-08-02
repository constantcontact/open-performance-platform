Ext.define('OppUI.model.loadtestDashboard.ApplicationMapping',  {
    extend: 'OppUI.model.Base',
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
    validators: {
        appKey: { type: 'length', min: 2}
    }

});
