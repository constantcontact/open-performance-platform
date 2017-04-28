/**
 * The main application class. An instance of this class is created by app.js when it calls
 * Ext.application(). This is the ideal place to handle application launch and initialization
 * details.
 */
Ext.define('OppUI.Application', {
    extend: 'Ext.app.Application',

    name: 'OppUI',

    // The tab we want to activate if there is no "#tag" in the URL.
    defaultToken: '!ux/dashboard',

    views: [
        'OppUI.view.main.Main'
    ],

    launch: function () {
        // Let's add a CSS class to body if flex box wrap is not implemented or broken
        // http://flexboxlayouts.com/flexboxlayout_tricks.html
        if (Ext.browser.is.Gecko && Ext.browser.version.major < 28) {
            Ext.getBody().addCls('x-flex-wrap-broken');
        }
    }
});
