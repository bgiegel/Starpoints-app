(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('CommunityController', CommunityController);

    CommunityController.$inject = ['Principal', 'Community'];

    function CommunityController (Principal, Community) {
        var vm = this;

        vm.communities = [];
        vm.isLeaderOrAdmin = isLeaderOrAdmin;

        loadAll();

        Principal.identity().then(function(currentUser) {
            vm.currentUser = currentUser;
        });

        function loadAll() {
            Community.query(function(result) {
                vm.communities = result;
            });
        }

        function isLeaderOrAdmin(communityLeader) {
            var leader = vm.currentUser.login === communityLeader;
            var admin = vm.currentUser.authorities.indexOf("Admin") === 1;
            return leader || admin;
        }
    }
})();
