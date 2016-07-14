(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('LevelDialogController', LevelDialogController);

    LevelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Level'];

    function LevelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Level) {
        var vm = this;

        vm.level = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.level.id !== null) {
                Level.update(vm.level, onSaveSuccess, onSaveError);
            } else {
                Level.save(vm.level, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('starPointsApp:levelUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
