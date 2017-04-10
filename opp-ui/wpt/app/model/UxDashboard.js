Ext.define('OppUI.model.UxDashboard', {
    extend: 'Ext.data.Model',
    
    fields: [
        { name: 'app', type: 'auto' },
        { name: 'env', type: 'auto' },
        { name: 'page', type: 'auto' },
        { name: 'location', type: 'auto' },
        { name: 'browser', type: 'auto' },
        { name: 'connection', type: 'auto' },
        { name: 'other', type: 'auto' },
        { name: 'name', type: 'auto' }

    ]
});
