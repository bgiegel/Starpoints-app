(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('CommunityController', CommunityController);

    CommunityController.$inject = ['$scope', '$state', 'Community'];

    function CommunityController ($scope, $state, Community) {
        var vm = this;
        
        vm.communities = [];

        loadAll();

        function loadAll() {
            Community.query(function(result) {
                vm.communities = result;
            });
        }
    }
})();
