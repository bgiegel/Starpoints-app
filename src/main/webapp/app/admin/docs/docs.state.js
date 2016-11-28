(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig ($stateProvider) {
        $stateProvider.state('docs', {
            parent: 'admin',
            url: '/docs',
            data: {
                authorities: ['Admin'],
                pageTitle: 'global.menu.admin.apidocs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/docs/docs.html'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', function ($translate) {
                    return $translate.refresh();
                }]
            }
        });
    }
})();
