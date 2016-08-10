(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('LevelDetailController', LevelDetailController);

    LevelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Level'];

    function LevelDetailController($scope, $rootScope, $stateParams, previousState, entity, Level) {
        var vm = this;

        vm.level = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('starPointsApp:levelUpdate', function(event, result) {
            vm.level = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
