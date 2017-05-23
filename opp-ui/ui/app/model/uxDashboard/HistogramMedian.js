Ext.define('OppUI.model.uxDashboard.HistogramMedian', {
    extend: 'OppUI.model.Base',

    fields: [
        { name: 'wptTimestamp', mapping: 'completedDate', type: 'auto',
            convert: function(value, record) {
                return value * 1000;                
            }
        }, 
        { name: 'TTFB', mapping: 'ttfb.median', type: 'auto' },
        { name: 'VisuallyComplete', mapping: 'visuallyComplete.median', type: 'auto' },
        { name: 'SpeedIndex', mapping: 'speedIndex.median', type: 'auto' },
        { name: 'Page', mapping: 'page', type: 'auto'},
        { name: 'Connection', mapping: 'connection', type: 'auto'},
        { name: 'date', mapping: 'completedDate', type: 'auto',
            convert: function(value, record) {
                return new Date(value * 1000);
            }
        }
    ]
});