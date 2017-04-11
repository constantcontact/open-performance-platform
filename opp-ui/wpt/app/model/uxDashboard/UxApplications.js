Ext.define('OppUI.model.uxDashboard.UxApplications', {
    extend: 'Ext.data.Model',

    fields: [
        { name: 'full', type: 'auto' },
        { name: 'environment', type: 'auto' },
        { name: 'application', type: 'auto' },
        { name: 'page', type: 'auto' },
        { name: 'location', type: 'auto' },
        { name: 'browser', type: 'auto' },
        { name: 'connection', type: 'auto' },
        { name: 'misc', type: 'auto' },
        { 
            name: 'testDate', 
            //type: 'auto',
            type: 'date', 
            convert: function(value, record){
                return new Date(value);
            }
        }
    ]
});
