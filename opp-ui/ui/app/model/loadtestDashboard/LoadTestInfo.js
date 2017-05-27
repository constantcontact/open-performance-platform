Ext.define('OppUI.model.loadtestDashboard.LoadTestInfo', {
    extend: 'OppUI.model.Base',
    fields: [
        {name: "loadTestId", mapping: 'id' },
        {name: "appUnderTest" },
        {name: "appUnderTestVersion" },
        {name: "comments"},
        {name: "description"},
        {name: "environment"},
        {name: "startTime", type: 'date', dateFormat: 'timestamp'},
        {name: "endTime", type: 'date', dateFormat: 'timestamp'},
        {name: "testName" },
        {name: "testSubName" },
        {name: "testTool" },
        {name: "testToolVersion" },
        {name: "vuserCount" },
        {name: "slaGroupId" },
        {name: 'externalTestId' }
     ]
});