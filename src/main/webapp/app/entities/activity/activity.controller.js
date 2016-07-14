(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ActivityController', ActivityController);

    ActivityController.$inject = ['$scope', '$state', 'Activity'];

    function ActivityController ($scope, $state, Activity) {
        var vm = this;
        
        vm.activities = [];

        loadAll();

        function loadAll() {
            Activity.query(function(result) {
                vm.activities = result;
            });
        }
    }
})();
