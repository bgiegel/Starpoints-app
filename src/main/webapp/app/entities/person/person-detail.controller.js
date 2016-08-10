(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('PersonDetailController', PersonDetailController);

    PersonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Person', 'Community', 'Contribution'];

    function PersonDetailController($scope, $rootScope, $stateParams, previousState, entity, Person, Community, Contribution) {
        var vm = this;

        vm.person = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('starPointsApp:personUpdate', function(event, result) {
            vm.person = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
