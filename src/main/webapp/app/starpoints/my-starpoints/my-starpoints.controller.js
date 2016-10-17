(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('MyStarPointsController', MyStarPointsController);

    MyStarPointsController.$inject = ['$scope', '$stateParams', 'Principal', 'Starpoints'];

    function MyStarPointsController ($scope, $stateParams, Principal, Starpoints) {
        var vm = this;

        vm.starpointsByCommunity = [];

        loadAll();

        function loadAll() {
            /**
             * On récupère l'utilisateur actuellement connecté et appel le service Starpoints.byCommunity
             */
            Principal.identity().then(function(currentUser) {
                Starpoints.byCommunity({userId:5}, function(result) {
                    vm.starpointsByCommunity = result;
                });
            });
        }
    }
})();
