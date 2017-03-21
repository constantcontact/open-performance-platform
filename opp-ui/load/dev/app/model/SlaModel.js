Ext.define('CCPerf.model.SlaModel',  {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'loadTestId'},
        {name: 'groupId'},
        {name: 'groupName'},
        {name: 'id'},
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
     ],
     proxy: {
        type: 'ajax',
        api: {
            read: null, // gets updated later on the fly
            create: null, // gets updated later on the fly
            update: null // gets updated later on the fly
        },
        actionMethods : {
             create  : 'POST',
             update  : 'PUT',
             read    : 'GET',
             destroy : 'DELETE'
         },
        reader: {
            type: 'json',
            rootProperty: 'res',
            successProperty: 'success'
        },
        writer: {
            type: 'json'
        }
    }
});
