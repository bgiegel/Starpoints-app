(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ContributionDetailController', ContributionDetailController);

    ContributionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Contribution', 'Activity', 'Community'];

    function ContributionDetailController($scope, $rootScope, $stateParams, entity, Contribution, Activity, Community) {
        var vm = this;

        vm.contribution = entity;

        var unsubscribe = $rootScope.$on('starPointsApp:contributionUpdate', function(event, result) {
            vm.contribution = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
