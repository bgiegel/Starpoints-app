(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('CommunityDialogController', CommunityDialogController);

    CommunityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Community', 'User', 'Principal'];

    function CommunityDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, Community, User, Principal) {
        var vm = this;

        vm.community = entity;
        vm.clear = clear;
        vm.save = save;
        vm.isNotLeaderNorAdmin = true;
        vm.members = User.query();

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        Principal.identity().then(function(currentUser) {
            var notLeader = vm.community.leader && currentUser.login !== vm.community.leader.login;
            var notAdmin = currentUser.authorities.indexOf("ROLE_ADMIN") === -1;
            vm.isNotLeaderNorAdmin = notLeader && notAdmin;
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.community.id !== null) {
                Community.update(vm.community, onSaveSuccess, onSaveError);
            } else {
                Community.save(vm.community, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('starPointsApp:communityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }
    }
})();
