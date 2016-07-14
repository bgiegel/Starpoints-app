(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('contribution', {
            parent: 'entity',
            url: '/contribution',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'starPointsApp.contribution.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contribution/contributions.html',
                    controller: 'ContributionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contribution');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('contribution-detail', {
            parent: 'entity',
            url: '/contribution/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'starPointsApp.contribution.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contribution/contribution-detail.html',
                    controller: 'ContributionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contribution');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Contribution', function($stateParams, Contribution) {
                    return Contribution.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('contribution.new', {
            parent: 'contribution',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
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
                        }
                    }
                }).result.then(function() {
                    $state.go('contribution', null, { reload: true });
                }, function() {
                    $state.go('contribution');
                });
            }]
        })
        .state('contribution.edit', {
            parent: 'contribution',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contribution/contribution-dialog.html',
                    controller: 'ContributionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Contribution', function(Contribution) {
                            return Contribution.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('contribution', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contribution.delete', {
            parent: 'contribution',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contribution/contribution-delete-dialog.html',
                    controller: 'ContributionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Contribution', function(Contribution) {
                            return Contribution.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('contribution', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
