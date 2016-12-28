(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('my-contributions', {
            parent: 'contributions',
            url: '/my-contributions',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'starPointsApp.mycontributions.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/contributions/my-contributions/my-contributions.html',
                    controller: 'MyContributionsController',
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
                filterByQuarter:false,
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
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        }).state('my-contributions.new', {
                parent: 'my-contributions',
                url: '/my-contributions/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/contribution/contribution-dialog.html',
                        controller: 'ContributionDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    deliverableDate: null,
                                    deliverableUrl: null,
                                    deliverableName: null,
                                    comment: null,
                                    preparatoryDate1: null,
                                    preparatoryDate2: null,
                                    id: null
                                };
                            },
                            fromUser:true
                        }
                    }).result.then(function () {
                            $state.go('my-contributions', null, {reload: 'my-contributions'});
                        }, function () {
                            $state.go('my-contributions');
                        });
                }]
            });
    }

})();
