(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('person', {
            parent: 'entity',
            url: '/person',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'starPointsApp.person.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/person/people.html',
                    controller: 'PersonController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('person');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('person-detail', {
            parent: 'entity',
            url: '/person/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'starPointsApp.person.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/person/person-detail.html',
                    controller: 'PersonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('person');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Person', function($stateParams, Person) {
                    return Person.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'person',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('person-detail.edit', {
            parent: 'person-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/person/person-dialog.html',
                    controller: 'PersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Person', function(Person) {
                            return Person.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('person.new', {
            parent: 'person',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/person/person-dialog.html',
                    controller: 'PersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                email: null,
                                firstName: null,
                                lastName: null,
                                entryDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('person', null, { reload: 'person' });
                }, function() {
                    $state.go('person');
                });
            }]
        })
        .state('person.edit', {
            parent: 'person',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/person/person-dialog.html',
                    controller: 'PersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Person', function(Person) {
                            return Person.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('person', null, { reload: 'person' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('person.delete', {
            parent: 'person',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/person/person-delete-dialog.html',
                    controller: 'PersonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Person', function(Person) {
                            return Person.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('person', null, { reload: 'person' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
