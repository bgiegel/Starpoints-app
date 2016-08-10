(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ScaleDetailController', ScaleDetailController);

    ScaleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Scale', 'Activity'];

    function ScaleDetailController($scope, $rootScope, $stateParams, entity, Scale, Activity) {
        var vm = this;

        vm.scale = entity;

        var unsubscribe = $rootScope.$on('starPointsApp:scaleUpdate', function(event, result) {
            vm.scale = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
