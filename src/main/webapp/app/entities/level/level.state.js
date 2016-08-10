(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('level', {
            parent: 'entity',
            url: '/level',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'starPointsApp.level.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/level/levels.html',
                    controller: 'LevelController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('level');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('level-detail', {
            parent: 'entity',
            url: '/level/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'starPointsApp.level.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/level/level-detail.html',
                    controller: 'LevelDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('level');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Level', function($stateParams, Level) {
                    return Level.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'level',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('level-detail.edit', {
            parent: 'level-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/level/level-dialog.html',
                    controller: 'LevelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Level', function(Level) {
                            return Level.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('level.new', {
            parent: 'level',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/level/level-dialog.html',
                    controller: 'LevelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                value: null,
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('level', null, { reload: true });
                }, function() {
                    $state.go('level');
                });
            }]
        })
        .state('level.edit', {
            parent: 'level',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/level/level-dialog.html',
                    controller: 'LevelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Level', function(Level) {
                            return Level.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('level', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('level.delete', {
            parent: 'level',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/level/level-delete-dialog.html',
                    controller: 'LevelDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Level', function(Level) {
                            return Level.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('level', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
