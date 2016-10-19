(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('AllStarPointsController', AllStarPointsController);

    AllStarPointsController.$inject = ['$scope', '$stateParams', 'Starpoints'];

    function AllStarPointsController($scope, $stateParams, Starpoints) {
        var vm = this;

        vm.allStarpointsByCommunity = [];

        loadAll();

        function loadAll() {
            Starpoints.byCommunityForAllUsers(function (result) {
                vm.allStarpointsByCommunity = result;
            });
        }
    }
})();
