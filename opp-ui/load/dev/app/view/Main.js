Ext.define('CCPerf.view.Main', {
    extend: 'Ext.container.Container',
    requires:[
   //     'Ext.form.field.ComboBox',
   //     'Ext.data.*',
   //     'Ext.chart.*',
        'Ext.ux.TabReorderer',

    'Ext.chart.CartesianChart',
    'Ext.chart.axis.Numeric',
    'Ext.chart.axis.Category',
    'Ext.chart.series.Line',
    'Ext.chart.interactions.ItemHighlight'
    ],
    id:'app-main-wrap',
    xtype: 'app-main',
	layout: {
        type: 'border',
        padding: 0
    },
    defaults: {
        split: true
    },
    items: [{
    	region: 'north',
        id: 'header',
        border:false,
        header:false,
        items: {
            defaults: {
                border:false
            },
            layout: {
                type: 'hbox',
                pack: 'start',
                align: 'stretch'
            },
            items: [
                {html:'<div class="title titleContent x-border-layout-ct"><span class="title-text">Open Performance</span><br /><span class="subtitle-text">PLATFORM</span></div>', flex:1},
                { html:'<a href="/ux" class="ux-link">Switch to UX</a>'}
            ]
        }
     },
    {
        xtype:'tabpanel',
        id:'tabPanelMain',
        region: 'center',
        autoScroll:true,
        layout:'fit',
      //  html: 'hello',
        defaults: {
            padding: 5
        },
        items: [{ xtype: 'testloadlist', reorderable: false }],
        listeners: {
            beforeremove: function( tabpanel, tab, eOpts ){
                if(tab.hidden){
                    // do nothing
                    // fixing EXTJS 5.1 BUG
                    // todo: remove this after extjs 5.1 bug is fixed with loading tabs
                } else {
                    var urlIndex = tabpanel.items.indexOf(tab) - 1; // subtract 1 because the "Load Tests" list tab is always there
                    var locationArr = location.hash.replace('#', '').split('||');
                    if (locationArr.length > 0) {
                        locationArr.splice(urlIndex, 1); // remove index from array
                    }
                    location.hash = locationArr.join('||'); // rebuild hash
                }
            }
        }

    }],
    initComponent: function(){
        var tabreorderer = Ext.create('Ext.ux.TabReorderer', {
                listeners: {
                    // reorder hash on drag and drop
                    Drop: function(boxreorderer, container, dragCmp, prevIndex, newIndex, eOpts) {
                        var locationArr = location.hash.replace('#', '').split('||'); // get hash
                        if(locationArr.length > 0) {
                            // reorder array
                            locationArr.splice(newIndex-1, 0, locationArr.splice(prevIndex-1, 1)[0]);
                        }
                        location.hash = locationArr.join('||'); // rebuild hash
                    }
                }
            });
        this.items[1].plugins = tabreorderer;



        this.callParent(arguments);
    }
});