(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('all-starpoints', {
            parent: 'starpoints',
            url: '/all-starpoints',
            data: {
                authorities: ['Utilisateur', 'Admin'],
                pageTitle: 'starPointsApp.starpoints.allstarpoints.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/starpoints/all-starpoints/all-starpoints.html',
                    controller: 'AllStarPointsController',
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
