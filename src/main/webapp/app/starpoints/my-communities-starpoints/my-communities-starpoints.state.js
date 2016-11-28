(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('my-communities-starpoints', {
            parent: 'starpoints',
            url: '/my-communities-starpoints',
            data: {
                authorities: ['Utilisateur'],
                pageTitle: 'starPointsApp.starpoints.mycommunitiesstarpoints.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/starpoints/my-communities-starpoints/my-communities-starpoints.html',
                    controller: 'MyCommunitiesStarPointsController',
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
