(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ActivityDialogController', ActivityDialogController);

    ActivityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Activity', 'Level'];

    function ActivityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Activity, Level) {
        var vm = this;

        vm.activity = entity;
        vm.clear = clear;
        vm.save = save;
        vm.levels = Level.query({filter: 'activity-is-null'});
        $q.all([vm.activity.$promise, vm.levels.$promise]).then(function() {
            if (!vm.activity.level || !vm.activity.level.id) {
                return $q.reject();
            }
            return Level.get({id : vm.activity.level.id}).$promise;
        }).then(function(level) {
            vm.levels.push(level);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.activity.id !== null) {
                Activity.update(vm.activity, onSaveSuccess, onSaveError);
            } else {
                Activity.save(vm.activity, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('starPointsApp:activityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
