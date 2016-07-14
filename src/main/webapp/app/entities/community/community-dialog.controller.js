(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('CommunityDialogController', CommunityDialogController);

    CommunityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Community', 'Person'];

    function CommunityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Community, Person) {
        var vm = this;

        vm.community = entity;
        vm.clear = clear;
        vm.save = save;
        vm.people = Person.query();
        vm.leaders = Person.query({filter: 'community-is-null'});
        $q.all([vm.community.$promise, vm.leaders.$promise]).then(function() {
            if (!vm.community.leader || !vm.community.leader.id) {
                return $q.reject();
            }
            return Person.get({id : vm.community.leader.id}).$promise;
        }).then(function(leader) {
            vm.leaders.push(leader);
        });

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
