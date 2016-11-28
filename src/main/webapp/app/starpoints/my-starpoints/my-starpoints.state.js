(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('my-starpoints', {
            parent: 'starpoints',
            url: '/mes-starpoints',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'starPointsApp.starpoints.mystarpoints.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/starpoints/my-starpoints/my-starpoints.html',
                    controller: 'MyStarPointsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('starpoints');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });
    }

})();
