Ext.define('CCPerf.model.ReportLoadSlaModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'loadTestId'},
        {name: 'groupId'},
        {name: 'groupName'},
        {name: 'slaId'},
        {name: 'name'},
        {name: 'min'},
        {name: 'max'},
        {name: 'avg'},
        {name: 'median'},
        {name: 'pct75'},
        {name: 'pct90'},
        {name: 'customName'},
        {name: 'customValue'},
        {name: 'marginOfError'},
        {name: 'slaGroupId' }
     ]
});
