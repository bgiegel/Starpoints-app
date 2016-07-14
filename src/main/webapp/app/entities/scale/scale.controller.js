(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ScaleController', ScaleController);

    ScaleController.$inject = ['$scope', '$state', 'Scale'];

    function ScaleController ($scope, $state, Scale) {
        var vm = this;
        
        vm.scales = [];

        loadAll();

        function loadAll() {
            Scale.query(function(result) {
                vm.scales = result;
            });
        }
    }
})();
