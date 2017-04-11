Ext.define('OppUI.model.uxDashboard.AppTrend', {
    extend: 'OppUI.model.Base',

    fields: ['timestamp', 'page', 'id', 'connectivity', 'pages',
        { name: 'wptTimestamp', 
            mapping: 'viewData.date', 
            type: 'auto',
            convert: function(value, record) {
                return value * 1000;                
            }
        }, 
        { name: 'TTFB', mapping: 'viewData.TTFB', type: 'auto' },
        { name: 'VisuallyComplete', mapping: 'viewData.visualComplete', type: 'auto' },
        { name: 'SpeedIndex', mapping: 'viewData.SpeedIndex', type: 'auto' },
        { name: 'Pages', 
            mapping: 'viewData.pages', 
            type: 'auto',
            convert: function(value, record) {
                return value.details;
            }
        }
    ]
});