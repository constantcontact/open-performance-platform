Ext.define('OppUI.model.uxDashboard.HistogramMax', {
    extend: 'OppUI.model.Base',

    fields: [
        { name: 'wptTimestamp', mapping: 'completedDate', type: 'auto',
            convert: function(value, record) {
                return value * 1000;                
            }
        }, 
        { name: 'TTFB', mapping: 'ttfb.max', type: 'auto' },
        { name: 'VisuallyComplete', mapping: 'visuallyComplete.max', type: 'auto' },
        { name: 'SpeedIndex', mapping: 'speedIndex.max', type: 'auto' },
        { name: 'Page', mapping: 'page', type: 'auto'},
        { name: 'Connection', mapping: 'connection', type: 'auto'},{ name: 'date', mapping: 'completedDate', type: 'auto',
            convert: function(value, record) {
                return new Date(value * 1000);
            }
        }
    ]
});