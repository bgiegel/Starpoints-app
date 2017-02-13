(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('AllStarPointsController', AllStarPointsController);

    AllStarPointsController.$inject = ['Starpoints'];

    function AllStarPointsController(Starpoints) {
        var vm = this;

        vm.allStarpointsByCommunity = [];

        loadAll();

        function loadAll() {
            Starpoints.byCommunityForAllUsers().$promise
                .then(displayStarpointsByCommunity);
        }

        function displayStarpointsByCommunity(response) {
            vm.allStarpointsByCommunity = response;
        }
    }
})();
