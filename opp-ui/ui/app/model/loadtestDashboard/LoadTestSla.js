Ext.define('OppUI.model.loadtestDashboard.LoadTestSla', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'loadTestId' },
        { name: 'groupId' },
        { name: 'groupName' },
        { name: 'name' },
        { name: 'min' },
        { name: 'max' },
        { name: 'avg' },
        { name: 'median' },
        { name: 'pct75' },
        { name: 'pct90' },
        { name: 'customName' },
        { name: 'customValue' },
        { name: 'marginOfError' },
        { name: 'slaGroupId', defaultValue: 0 }
    ],
    proxy: {
        type: 'ajax',
        api: {
            read: null, // gets updated later on the fly
            create: null, // gets updated later on the fly
            update: null // gets updated later on the fly
        },
        actionMethods: {
            create: 'POST',
            update: 'PUT',
            read: 'GET',
            destroy: 'DELETE'
        },
        reader: {
            type: 'json',
            successProperty: 'success'
        },
        writer: {
            type: 'json',
            allowSingle: false, // force array each time
            writeAllFields: true,
            transform: {
                fn: function(data, request) {
                    // if creating a new object, don't send the id
                    if (request.getAction() === 'create') {
                        data.forEach((row) => { delete row.id });
                    }
                    return data;
                },
                scope: this
            }
        }

    }
});