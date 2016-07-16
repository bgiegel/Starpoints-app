(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('PersonDetailController', PersonDetailController);

    PersonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Person', 'Community', 'Contribution'];

    function PersonDetailController($scope, $rootScope, $stateParams, entity, Person, Community, Contribution) {
        var vm = this;

        vm.person = entity;

        var unsubscribe = $rootScope.$on('starPointsApp:personUpdate', function(event, result) {
            vm.person = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
