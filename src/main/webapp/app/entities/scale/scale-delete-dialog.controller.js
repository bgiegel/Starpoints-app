(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ScaleDeleteController',ScaleDeleteController);

    ScaleDeleteController.$inject = ['$uibModalInstance', 'entity', 'Scale'];

    function ScaleDeleteController($uibModalInstance, entity, Scale) {
        var vm = this;

        vm.scale = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Scale.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
