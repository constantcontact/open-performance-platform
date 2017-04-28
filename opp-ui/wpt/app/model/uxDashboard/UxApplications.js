Ext.define('OppUI.model.uxDashboard.UxApplications', {
    extend: 'OppUI.model.Base',

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
            type: 'date', 
            convert: function(value, record){
                return new Date(value);
            },
            dateFormat: 'timestamp'
        }
    ]
});
