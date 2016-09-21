(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('UserManagementDeleteController', UserManagementDeleteController);

    UserManagementDeleteController.$inject = ['$uibModalInstance', 'entity', 'User'];

    function UserManagementDeleteController($uibModalInstance, entity, User) {
        var vm = this;

        vm.user = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function onDeletionError(response) {
            if (response.status === 400 && response.data === 'impossible to delete leader') {
                vm.errorLeaderDeletion = true;
            }
        }

        function onDeletionSucess() {
            $uibModalInstance.close(true);
        }

        function confirmDelete(login) {
            User.delete({login: login}, onDeletionSucess, onDeletionError);
        }
    }
})();
