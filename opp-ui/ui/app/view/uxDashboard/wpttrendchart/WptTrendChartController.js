Ext.define('OppUI.view.uxDashboard.wpttrendchart.WptTrendChartController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.wpttrendchart',

    buttonMetricClicked: function(button) {
        var view = this.getView(),
            store;

        this.styleButtons(button);
        // parent view is listening for this
        this.fireEvent('plMetricChange', button.getText());
    },

    styleButtons: function(btn) {
        // remove all hovers and focus classes
        btn.removeCls('x-btn-over');
        btn.removeCls('x-focus');
        btn.removeCls('x-btn-focus'); 
        btn.removeCls('x-btn-default-toolbar-small-focus');
        btn.cancelFocus(); // this alone doesn't cut it.  Need to remove above classes

        // set to selected style
        btn.addCls('x-btn-selected');

        // remove selected class from sibling button
        var nextBtn = btn.nextSibling('button');
        if(nextBtn !== null){
            nextBtn.removeCls('x-btn-selected');
        } else {
            btn.previousSibling('button').removeCls('x-btn-selected');
        }
        
    }



});