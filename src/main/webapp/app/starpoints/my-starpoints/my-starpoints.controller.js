(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('MyStarPointsController', MyStarPointsController);

    MyStarPointsController.$inject = ['Principal', 'Starpoints'];

    function MyStarPointsController (Principal, Starpoints) {
        var vm = this;

        vm.starpointsByCommunity = [];

        loadAll();

        function loadAll() {
            /**
             * On récupère l'utilisateur actuellement connecté et appel le service Starpoints.byCommunity
             */
            Principal.identity().then(function(currentUser) {
                Starpoints.byCommunity({userId:currentUser.id}).$promise
                    .then(displayStarpointsByCommunity);
            });
        }

        function displayStarpointsByCommunity(response) {
            vm.starpointsByCommunity = response;
        }
    }
})();
