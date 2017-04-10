Ext.define('OppUI.model.UxApplications', {
    extend: 'Ext.data.Model',
    
    // fields: [
    //     { name: 'app', type: 'auto' },
    //     { name: 'env', type: 'auto' },
    //     { name: 'page', type: 'auto' },
    //     { name: 'location', type: 'auto' },
    //     { name: 'browser', type: 'auto' },
    //     { name: 'connection', type: 'auto' },
    //     { name: 'other', type: 'auto' },
    //     { name: 'name', type: 'auto' }

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
            type: 'auto', 
            convert: function(value, record){
                return new Date(value);
            } 
        }

    ]
});
