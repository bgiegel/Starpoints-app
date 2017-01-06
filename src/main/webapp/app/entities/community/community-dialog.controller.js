(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('CommunityDialogController', CommunityDialogController);

    CommunityDialogController.$inject = ['$timeout', '$scope', '$uibModalInstance', 'entity', 'Community', 'User', 'Principal', 'AlertService'];

    function CommunityDialogController($timeout, $scope, $uibModalInstance, entity, Community, User, Principal, AlertService) {
        var vm = this;

        vm.clear = clear;
        vm.save = save;
        vm.addMember = addMember;
        vm.removeMember = removeMember;

        vm.community = entity;
        vm.isNotLeaderNorAdmin = true;
        vm.users = User.getUsersNames();
        vm.userSearchFieldValue = "";

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

            if(!vm.community.leader.id) {
                AlertService.error("Ce leader est inconnu. Veuillez en sélectionner un dans la liste.");
            }else {
                if (vm.community.id !== null) {
                    Community.update(vm.community, onSaveSuccess, onSaveError);
                } else {
                    Community.save(vm.community, onSaveSuccess, onSaveError);
                }
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

        function removeMember($index) {
            vm.community.members.splice($index, 1);
        }

        /**
         * @param selectedUser l'utilisatteur qui a été sélectionné dans le champs de recherche
         * @returns boolean si l'utilisateur est déjà présent dans la liste de membres. Faux dans le cas contraire.
         */
        function notAlreadyMember(selectedUser) {
            return vm.community.members.some(function (member) {
                return member.id !== selectedUser.id;
            });
        }

        /**
         * Ajoute un membre à la communauté en cours de création (ou d'édition)
         * @param selectedUser l'utilisateur sélectionné par la composant de recherche d'utilisateur.
         */
        function addMember(selectedUser) {
            if(!members){
                vm.community.members = [];
            }
            var members = vm.community.members;
            if(members.length==0 || notAlreadyMember(selectedUser)){
                members.push(selectedUser);
            }
            vm.userSearchFieldValue = "";
        }
    }
})();
