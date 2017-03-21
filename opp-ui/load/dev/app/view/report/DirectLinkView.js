Ext.define('CCPerf.view.report.DirectLinkView' ,{
    extend: 'Ext.window.Window',
    modal : true,
    autoShow: true,
    iconCls : "icon-link",
    layout : "absolute",
    width : 400,
    selectOnFocus:true,
    height : 110,
    title : "Report Link",
    closable : false,
    resizable : false,
    items : [{
            id : "txtLink" + this.rndId,
            xtype : "textfield",
            width : 364,
            x : 10,
            y : 10,
            selectOnFocus : true,
            readOnly : true
            //,value : directUrl
    }],
    buttons : [{
            xtype : "button",
            text : "Close",
            handler : function() {
                    this.up("window").close();
            }
    }]
});

/*win.on('show', function()
{  
txtBox = Ext.getCmp("txtLink" + rndId);
//   txtBox.setValue(CCPerf.util.Globals.reports.generateUrl(false));
txtBox.focus(true, 1000);
});*/

