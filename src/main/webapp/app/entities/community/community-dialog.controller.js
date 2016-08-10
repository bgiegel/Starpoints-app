(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('CommunityDialogController', CommunityDialogController);

    CommunityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Community', 'User'];

    function CommunityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Community, User) {
        var vm = this;

        vm.community = entity;
        vm.clear = clear;
        vm.save = save;
        vm.members = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.community.id !== null) {
                Community.update(vm.community, onSaveSuccess, onSaveError);
            } else {
                Community.save(vm.community, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('starPointsApp:communityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
