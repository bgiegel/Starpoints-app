(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('all-contributions', {
            parent: 'contributions',
            url: '/contributions',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'starPointsApp.allcontributions.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/contributions/all-contributions/all-contributions.html',
                    controller: 'AllContributionsController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                shouldFilter:false,
                quarterId:'Q1',
                year:new Date()
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort)
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contribution');
                    $translatePartialLoader.addPart('contributionStatusType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });
    }

})();
