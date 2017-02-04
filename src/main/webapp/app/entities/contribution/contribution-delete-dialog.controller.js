(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ContributionDeleteController',ContributionDeleteController);

    ContributionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Contribution'];

    function ContributionDeleteController($uibModalInstance, entity, Contribution) {
        var vm = this;

        vm.contribution = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Contribution.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
