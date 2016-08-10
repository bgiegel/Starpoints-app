(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('community', {
            parent: 'entity',
            url: '/community',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'starPointsApp.community.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/community/communities.html',
                    controller: 'CommunityController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('community');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('community-detail', {
            parent: 'entity',
            url: '/community/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'starPointsApp.community.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/community/community-detail.html',
                    controller: 'CommunityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('community');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Community', function($stateParams, Community) {
                    return Community.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('community.new', {
            parent: 'community',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/community/community-dialog.html',
                    controller: 'CommunityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('community', null, { reload: true });
                }, function() {
                    $state.go('community');
                });
            }]
        })
        .state('community.edit', {
            parent: 'community',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/community/community-dialog.html',
                    controller: 'CommunityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Community', function(Community) {
                            return Community.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('community', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('community.delete', {
            parent: 'community',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/community/community-delete-dialog.html',
                    controller: 'CommunityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Community', function(Community) {
                            return Community.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('community', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
