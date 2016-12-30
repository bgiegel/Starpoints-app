(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('LevelDetailController', LevelDetailController);

    LevelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity'];

    function LevelDetailController($scope, $rootScope, $stateParams, entity) {
        var vm = this;

        vm.level = entity;

        var unsubscribe = $rootScope.$on('starPointsApp:levelUpdate', function(event, result) {
            vm.level = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
