(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('scale', {
                parent: 'entity',
                url: '/scale',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'starPointsApp.scale.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/scale/scales.html',
                        controller: 'ScaleController',
                        controllerAs: 'vm'
                    }
                },
                params: {
                    page: {
                        value: '1',
                        squash: true
                    }
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('scale');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('scale-detail', {
                parent: 'entity',
                url: '/scale/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'starPointsApp.scale.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/scale/scale-detail.html',
                        controller: 'ScaleDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('scale');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Scale', function ($stateParams, Scale) {
                        return Scale.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'scale',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('scale-detail.edit', {
                parent: 'scale-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/scale/scale-dialog.html',
                        controller: 'ScaleDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Scale', function (Scale) {
                                return Scale.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                            $state.go('^', {}, {reload: false});
                        }, function () {
                            $state.go('^');
                        });
                }]
            })
            .state('scale.new', {
                parent: 'scale',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/scale/scale-dialog.html',
                        controller: 'ScaleDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    startDate: null,
                                    endDate: null,
                                    value: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                            $state.go('scale', null, {reload: 'scale'});
                        }, function () {
                            $state.go('scale');
                        });
                }]
            })
            .state('scale.edit', {
                parent: 'scale',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/scale/scale-dialog.html',
                        controller: 'ScaleDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Scale', function (Scale) {
                                return Scale.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                            $state.go('scale', null, {reload: 'scale'});
                        }, function () {
                            $state.go('^');
                        });
                }]
            })
            .state('scale.delete', {
                parent: 'scale',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/scale/scale-delete-dialog.html',
                        controller: 'ScaleDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Scale', function (Scale) {
                                return Scale.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                            $state.go('scale', null, {reload: 'scale'});
                        }, function () {
                            $state.go('^');
                        });
                }]
            });
    }

})();
