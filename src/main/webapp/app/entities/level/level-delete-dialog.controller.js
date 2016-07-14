(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('LevelDeleteController',LevelDeleteController);

    LevelDeleteController.$inject = ['$uibModalInstance', 'entity', 'Level'];

    function LevelDeleteController($uibModalInstance, entity, Level) {
        var vm = this;

        vm.level = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Level.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
