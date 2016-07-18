(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ContributionDialogController', ContributionDialogController);

    ContributionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Contribution', 'Activity', 'Community', 'Person'];

    function ContributionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Contribution, Activity, Community, Person) {
        var vm = this;

        vm.contribution = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.activities = Activity.query({filter: 'contribution-is-null'});
        vm.communities = Community.query({filter: 'contribution-is-null'});
        vm.people = Person.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.contribution.id !== null) {
                Contribution.update(vm.contribution, onSaveSuccess, onSaveError);
            } else {
                Contribution.save(vm.contribution, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('starPointsApp:contributionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.deliverableDate = false;
        vm.datePickerOpenStatus.preparatoryDate1 = false;
        vm.datePickerOpenStatus.preparatoryDate2 = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
