(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ScaleDetailController', ScaleDetailController);

    ScaleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Scale', 'Activity'];

    function ScaleDetailController($scope, $rootScope, $stateParams, previousState, entity, Scale, Activity) {
        var vm = this;

        vm.scale = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('starPointsApp:scaleUpdate', function(event, result) {
            vm.scale = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
