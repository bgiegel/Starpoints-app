(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ScaleDialogController', ScaleDialogController);

    ScaleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Scale', 'Activity'];

    function ScaleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Scale, Activity) {
        var vm = this;

        vm.scale = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.activities = Activity.query({filter: 'scale-is-null'});
        $q.all([vm.scale.$promise, vm.activities.$promise]).then(function() {
            if (!vm.scale.activity || !vm.scale.activity.id) {
                return $q.reject();
            }
            return Activity.get({id : vm.scale.activity.id}).$promise;
        }).then(function(activity) {
            vm.activities.push(activity);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.scale.id !== null) {
                Scale.update(vm.scale, onSaveSuccess, onSaveError);
            } else {
                Scale.save(vm.scale, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('starPointsApp:scaleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
