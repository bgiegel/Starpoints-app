(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ContributionDetailController', ContributionDetailController);

    ContributionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Contribution'];

    function ContributionDetailController($scope, $rootScope, $stateParams, entity, Contribution) {
        var vm = this;

        vm.contribution = entity;
        vm.updateStatus = updateStatus;

        var unsubscribe = $rootScope.$on('starPointsApp:contributionUpdate', function(event, result) {
            vm.contribution = result;
        });
        $scope.$on('$destroy', unsubscribe);

        function updateStatus (status) {
            vm.contribution.status = status;
            Contribution.update(vm.contribution);
        }
    }
})();
