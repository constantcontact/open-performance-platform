Ext.define('OppUI.model.uxDashboard.HistogramMin', {
    extend: 'OppUI.model.Base',

    fields: [
        { name: 'wptTimestamp', mapping: 'completedDate', type: 'auto',
            convert: function(value, record) {
                return value * 1000;                
            }
        }, 
        { name: 'TTFB', mapping: 'ttfb.min', type: 'auto' },
        { name: 'VisuallyComplete', mapping: 'visuallyComplete.min', type: 'auto' },
        { name: 'SpeedIndex', mapping: 'speedIndex.min', type: 'auto' },
        { name: 'Page', mapping: 'page', type: 'auto'},
        { name: 'Connection', mapping: 'connection', type: 'auto'},
        { name: 'date', mapping: 'completedDate', type: 'auto',
            convert: function(value, record) {
                return new Date(value * 1000);
            }
        }
    ]
});