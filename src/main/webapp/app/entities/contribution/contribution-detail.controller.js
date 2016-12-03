(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ContributionDetailController', ContributionDetailController);

    ContributionDetailController.$inject = ['$scope', '$rootScope', 'previousState', 'entity'];

    function ContributionDetailController($scope, $rootScope, previousState, entity) {
        var vm = this;

        vm.contribution = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('starPointsApp:contributionUpdate', function(event, result) {
            vm.contribution = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
