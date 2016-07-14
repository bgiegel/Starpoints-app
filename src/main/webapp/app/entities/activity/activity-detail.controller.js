(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ActivityDetailController', ActivityDetailController);

    ActivityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Activity', 'Level'];

    function ActivityDetailController($scope, $rootScope, $stateParams, entity, Activity, Level) {
        var vm = this;

        vm.activity = entity;

        var unsubscribe = $rootScope.$on('starPointsApp:activityUpdate', function(event, result) {
            vm.activity = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
