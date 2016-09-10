(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ContributionDialogController', ContributionDialogController);

    ContributionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Contribution', 'Activity', 'Community', 'User', 'Principal'];

    function ContributionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Contribution, Activity, Community, User, Principal) {
        var vm = this;

        vm.contribution = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.activities = Activity.query();
        vm.communities = Community.query();
        vm.members = [];

        vm.loadCommunityMembers = loadCommunityMembers;

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

        function loadCommunityMembers(){
            var selectedCommunity = vm.contribution.community;
            if(selectedCommunity) {
                vm.members = selectedCommunity.members;
            }else {
                vm.members = [];
            }
        }

        /**
         * On vérifie si l'utilisateur est admin. Si ce n'est pas le cas on récupère la liste des communautés
         * qu'il dirige afin qu'il ne puisse créé que des contributions pour sa/ses communauté(s).
         */
        Principal.identity().then(function(currentUser) {
            if(currentUser.authorities.indexOf("ROLE_ADMIN") === -1){
                Community.leadedBy({user: currentUser.login}, function(leaderCommunities){
                    if(leaderCommunities.length > 0){
                        vm.communities = leaderCommunities;
                        vm.contribution.community = vm.communities[0];
                        loadCommunityMembers();
                    }
                });
            }
        });
    }
})();
