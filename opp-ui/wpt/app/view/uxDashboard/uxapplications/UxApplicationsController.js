Ext.define('OppUI.view.uxDashboard.uxapplications.UxApplicationsController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.uxapplications',

    filters: {
        applications: [],
        pages: [],
        envs: [],
        clearApplications: function() { this.applications = []; },
        clearPages: function() { this.pages = []; },
        clearEnvs: function() { this.envs = []; },
        clearAll: function() { this.clearApplication(); this.clearPages(); this.clearEnvs(); }
    },
    
    onRemoteUxApplicationsLoad: function(remoteUxApplications) {
        var me, references, view, menus, applications, data;

        me = this;
        view = me.getView();
        references = me.getReferences();
        data = [];

        // Populate Navigation
        menus = {
            applications: {
                items: [],
                listeners: {
                    checkchange: me.onApplicationItemCheck,
                    scope: me
                }
            }
        }

        // add to the local store.
        for(var i = 0; i < remoteUxApplications.getData().items.length; i++) {
            data.push(remoteUxApplications.getData().items[i].getData());
        }
        view.store.getProxy().setData(data);
        view.store.load();

        // find all the unique applications
        applications = {};
        remoteUxApplications.each(function(application){
            var app = application.data.application;
            applications[app] = undefined;
            
        });

        for (app in applications) {
            if (applications.hasOwnProperty(app)) {
                menus['applications'].items.push(Ext.apply({
                    text: app,
                    value: app,
                    listeners: menus['applications'].listeners
                }, view.menuItemDefaults));
            }
        }

        Ext.batchLayouts(function () {
            references.applicationsButton.menu.add(menus.applications.items);
        });

    },

    onApplicationItemCheck: function (menuItem) {
        var view, me, reference, pages, envs, secondaryMenus;
        console.log(menuItem);

        me = this;
        view = me.getView();
        references = me.getReferences();
        secondaryMenus = {
            pages: {
                items: [],
                listeners: {
                    checkchange: me.onPageItemCheck,
                    scope: me
                }
            },
             envs: {
                items: [],
                listeners: {
                    checkchange: me.onEnvItemCheck,
                    scope: me
                }
            }
        }

        view.store.clearFilter();
        if(menuItem.checked) {
            this.filters.applications.push(menuItem.value);
            
            view.store.filterBy(function(record) {
                var app = record.get('application');
                for(var i = 0; i < me.filters.applications.length; i++) {
                    if(me.filters.applications[i] === app) {
                        return true;
                    }
                }
                return false;
            });
        } else {
            var index = this.filters.applications.indexOf(menuItem.value);
            console.log(index);
            this.filters.applications.splice(index, 1);

            if(this.filters.applications.length > 0) {
                view.store.filterBy(function(record) {
                    var app = record.get('app');
                    for(var i = 0; i < me.filters.applications.length; i++) {
                        if(me.filters.applications[i] === app) {
                            return true;
                        }
                    }
                    return false;
                });
            }           
        }
        

        console.log(this.filters.applications);

        this.filters.clearPages();
        this.filters.clearEnvs();

        pages = {};
        references.pagesButton.menu.removeAll();

        view.store.each(function(record, id) {
            pages[record.data.page] = undefined;
        });

        for(var page in pages) {
            if(pages.hasOwnProperty(page)) {
                secondaryMenus['pages'].items.push(Ext.apply({
                    text: page,
                    value: page,
                    listeners: secondaryMenus['pages'].listeners
                }, view.menuItemDefaults));
            }
        }

        envs = {};
        references.envsButton.menu.removeAll();

        view.store.each(function(record, id) {
            envs[record.data.environment] = undefined;
        });

        for(var env in envs) {
            if(envs.hasOwnProperty(env)) {
                secondaryMenus['envs'].items.push(Ext.apply({
                    text: env,
                    value: env,
                    listeners: secondaryMenus['envs'].listeners
                }, view.menuItemDefaults));
            }
        }
        Ext.batchLayouts(function(){
            references.pagesButton.menu.add(secondaryMenus.pages.items);
            references.envsButton.menu.add(secondaryMenus.envs.items);
        });
    },
    /**
     * 
     */
    onPageItemCheck: function(pageItem) {
        var view, me, envs, secondaryMenus;

        me = this;
        view = this.getView();
        secondaryMenus = {
            envs: {
                items: [],
                listeners: {
                    checkchange: me.onEnvItemCheck,
                    scope: me
                }
            }
        }

        if(pageItem.checked) {
            this.filters.pages.push(pageItem.value);

            view.store.clearFilter();
            if(this.filters.applications.length > 0) {
                view.store.filterBy(function(record) {
                    var app = record.get('application');
                    for(var i = 0; i < me.filters.applications.length; i++) {
                        if(me.filters.applications[i] === app) {
                            return true;
                        }
                    }
                    return false;
                });
            }

            view.store.filterBy(function(record) {
                var page = record.get('page');
                for(var i = 0; i < me.filters.pages.length; i++) {
                    if(me.filters.pages[i] === page) {
                        return true;
                    }
                }
                return false;
            });
        } else {
            var index = this.filters.pages.indexOf(pageItem.value);
            this.filters.pages.splice(index, 1);

            // clear the filters and refilter the apps and pages.
            view.store.clearFilter();
            if(this.filters.applications.length > 0) {
                view.store.filterBy(function(record) {
                    var app = record.get('application');
                    for(var i = 0; i < me.filters.applications.length; i++) {
                        if(me.filters.applications[i] === app) {
                            return true;
                        }
                    }
                    return false;
                });
            }

            if(this.filters.pages.length > 0) {
                view.store.filterBy(function(record) {
                    var page = record.get('page');
                    for(var i = 0; i < me.filters.pages.length; i++) {
                        if(me.filters.pages[i] === page) {
                            return true;
                        }
                    }
                    return false;
                });
            }
        }

        envs = {};
        references.envsButton.menu.removeAll();

        view.store.each(function(record, id) {
            envs[record.data.environment] = undefined;
        });

        for(var env in envs) {
            if(envs.hasOwnProperty(env)) {
                secondaryMenus['envs'].items.push(Ext.apply({
                    text: env,
                    value: env,
                    listeners: secondaryMenus['envs'].listeners
                }, view.menuItemDefaults));
            }
        }

        Ext.batchLayouts(function(){
            references.envsButton.menu.add(secondaryMenus.envs.items);
        });
    },

    onEnvItemCheck: function(envItem) {
        var view, me;
        me = this;
        view = this.getView();

        if(envItem.checked) {
            this.filters.envs.push(envItem.value);
            view.store.filterBy(function(record) {
                var env = record.get('environment');

                for(var i = 0; i < me.filters.envs.length; i++) {
                    if(me.filters.envs[i] === env) {
                        return true;
                    }
                }
                return false;
            });
        } else {
            var index = this.filters.pages.indexOf(envItem.value);
            this.filters.envs.splice(index, 1);
            this.reBuildGridByCurrentFilterState();
        }
    },

    reBuildGridByCurrentFilterState: function() {
        var view, me;
        me = this;
        view = this.getView();

        view.store.clearFilter();
        if(this.filters.applications.length > 0) {
            view.store.filterBy(function(record) {
                var app = record.get('application');
                for(var i = 0; i < me.filters.applications.length; i++) {
                    if(me.filters.applications[i] === app) {
                        return true;
                    }
                }
                return false;
            });
        }

        if(this.filters.pages.length > 0) {
            view.store.filterBy(function(record) {
                var page = record.get('page');
                for(var i = 0; i < me.filters.pages.length; i++) {
                    if(me.filters.pages[i] === page) {
                        return true;
                    }
                }
                return false;
            });
        }

        if (this.filters.envs.length > 0) {
            view.store.filterBy(function(record) {
                var env = record.get('environment');
                for(var i = 0; i < me.filters.envs.length; i++) {
                    if(me.filters.envs[i] === env) {
                        return true;
                    }
                }
                return false;
            });
        }
    },

    search: function(field, e) {
        if(e.getKey() === window.parseInt(e.ENTER)) {
            var searchString, store, view;

            searchString = field.getValue();
            view = this.getView();

            store = view.store;
            
            if (!searchString || searchString.length === 0) {
                //this.up('grid').getController().reBuildGridByCurrentFilterState();
                this.reBuildGridByCurrentFilterState();
            } else {
                store.filterBy(function(record) {
                    var app, env, page, location, browser, connection; 

                    app = record.get('application');
                    env = record.get('environment');
                    page = record.get('page');
                    location = record.get('location');
                    browser = record.get('browser');
                    connection = record.get('connection');

                    if (app.indexOf(searchString) >= 0 ||
                        env.indexOf(searchString) >= 0 ||
                        page.indexOf(searchString) >= 0  ||
                        location.indexOf(searchString) >= 0 ||
                        browser.indexOf(searchString) >= 0  ||
                        connection.indexOf(searchString) >= 0) {
                        return true;
                    }

                    return false;
                })
            }
        }
    }      
});
