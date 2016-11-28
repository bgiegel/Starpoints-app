(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('UserManagementDialogController',UserManagementDialogController);

    UserManagementDialogController.$inject = ['$uibModalInstance', 'entity', 'User', 'JhiLanguageService'];

    function UserManagementDialogController ($uibModalInstance, entity, User, JhiLanguageService) {
        var vm = this;

        vm.clear = clear;
        vm.save = save;
        vm.openCalendar = openCalendar;

        vm.authorities = ['Utilisateur', 'Leader', 'Admin'];
        vm.languages = null;
        vm.user = entity;

        vm.datePickerOpenStatus = {};


        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function save () {
            vm.isSaving = true;
            if (vm.user.id !== null) {
                User.update(vm.user, onSaveSuccess, onSaveError);
            } else {
                User.save(vm.user, onSaveSuccess, onSaveError);
            }
        }

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        vm.datePickerOpenStatus.entryDate = false;
    }
})();
