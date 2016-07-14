(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('CommunityDeleteController',CommunityDeleteController);

    CommunityDeleteController.$inject = ['$uibModalInstance', 'entity', 'Community'];

    function CommunityDeleteController($uibModalInstance, entity, Community) {
        var vm = this;

        vm.community = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Community.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
