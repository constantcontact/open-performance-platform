Ext.define('OppUI.model.loadtestDashboard.LoadTestSla',  {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'load_test_id'},
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
        {name: 'custom_name'},
        {name: 'custom_value'},
        {name: 'margin_of_error'},
        {name: 'sla_group_id' }
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