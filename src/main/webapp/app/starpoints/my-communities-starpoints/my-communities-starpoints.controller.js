(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('MyCommunitiesStarPointsController', MyCommunitiesStarPointsController);

    MyCommunitiesStarPointsController.$inject = ['Principal', 'Starpoints'];

    function MyCommunitiesStarPointsController (Principal, Starpoints) {
        var vm = this;

        vm.starpointsByCommunity = [];

        loadStarpoints();

        function loadStarpoints() {
            /**
             * On récupère l'utilisateur actuellement connecté et on appelle le service Starpoints.byCommunityLeadedBy
             */
            Principal.identity().then(function(currentUser) {
                Starpoints.byCommunityLeadedBy({leaderId:currentUser.id}).$promise
                    .then(displayStarpoints);
            });
        }

        function displayStarpoints(result) {
            vm.starpointsByCommunity = result;
        }
    }
})();
