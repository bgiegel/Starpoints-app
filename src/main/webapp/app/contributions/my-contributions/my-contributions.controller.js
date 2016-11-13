(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('MyContributionsController', MyContributionsController);

    MyContributionsController.$inject = ['$stateParams', 'ParseLinks', 'AlertService', '$state', 'pagingParams', 'paginationConstants', 'Principal', 'Contribution'];

    function MyContributionsController ($stateParams, ParseLinks, AlertService, $state, pagingParams, paginationConstants, Principal, Contribution) {
        var vm = this;

        //pagination
        vm.page = 1;
        vm.totalItems = null;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadPage = loadPage;
        vm.transition = transition;

        //datePicker
        vm.popup = {
            opened:false
        };
        vm.format= 'yyyy';
        vm.dateOptions = {
            formatYear: 'yyyy',
            maxDate: new Date(),
            minDate: new Date(2000, 1, 1),
            startingDay: 1,
            minMode: 'year'
        };

        //model
        vm.contributions = [];
        vm.filterByQuarter=$stateParams.filterByQuarter;
        vm.quarterId = $stateParams.quarterId;
        vm.year = $stateParams.year;

        vm.loadContributions = loadContributions;

        loadContributions();

        vm.openDatePicker = function() {
            vm.popup.opened = true;
        };

        function loadUserContributionsByQuarter(currentUser) {
            var quarter = vm.quarterId + '-' + vm.year.getFullYear();
            Contribution.fromUserByQuarter({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                quarter:quarter,
                login: currentUser.login
            }, onSuccess, onError);
        }

        function loadUserContributions(currentUser) {
            Contribution.getAllFromAnAuthor({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                login: currentUser.login
            }, onSuccess, onError);
        }

        function loadContributions() {
            /**
             * On récupère l'utilisateur actuellement connecté et appel le service retournant les contributions d'un utilisateur
             */
            Principal.identity().then(function(currentUser) {
                if(vm.filterByQuarter){
                    loadUserContributionsByQuarter(currentUser);
                }else {
                    loadUserContributions(currentUser);
                }
            });
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                filterByQuarter: vm.filterByQuarter,
                quarterId: vm.quarterId,
                year:vm.year
            });
        }

        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
            vm.contributions = data;
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }
    }
})();
