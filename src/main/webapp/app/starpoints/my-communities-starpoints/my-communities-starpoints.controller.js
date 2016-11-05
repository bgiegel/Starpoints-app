(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('MyCommunitiesStarPointsController', MyCommunitiesStarPointsController);

    MyCommunitiesStarPointsController.$inject = ['Principal', 'Starpoints'];

    function MyCommunitiesStarPointsController (Principal, Starpoints) {
        var vm = this;

        vm.starpointsByCommunity = [];

        loadAll();

        function loadAll() {
            /**
             * On récupère l'utilisateur actuellement connecté et appel le service Starpoints.byCommunityLeadedBy
             */
            Principal.identity().then(function(currentUser) {
                Starpoints.byCommunityLeadedBy({leaderId:currentUser.id}, function(result) {
                    vm.starpointsByCommunity = result;
                });
            });
        }
    }
})();
