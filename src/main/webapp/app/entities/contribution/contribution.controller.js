(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ContributionController', ContributionController);

    ContributionController.$inject = ['$scope', '$state', 'Contribution'];

    function ContributionController ($scope, $state, Contribution) {
        var vm = this;
        
        vm.contributions = [];

        loadAll();

        function loadAll() {
            Contribution.query(function(result) {
                vm.contributions = result;
            });
        }
    }
})();
