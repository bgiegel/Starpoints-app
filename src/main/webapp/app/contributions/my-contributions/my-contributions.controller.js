(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('MyContributionsController', MyContributionsController);

    MyContributionsController.$inject = ['$stateParams', 'AlertService', '$state', 'pagingParams', 'paginationConstants', 'Principal', 'Contribution'];

    function MyContributionsController ($stateParams, AlertService, $state, pagingParams, paginationConstants, Principal, Contribution) {
        var vm = this;

        //pagination
        vm.page = 1;
        vm.totalItems = null;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadPage = loadPage;
        vm.transition = transition;

        //model
        vm.contributions = [];

        vm.quarter = {
            shouldFilter: $stateParams.shouldFilter,
            id: $stateParams.quarterId,
            year: $stateParams.year
        };

        vm.loadContributions = loadContributions;

        loadContributions();

        function loadUserContributionsByQuarter(currentUser, quarterRequest) {
            var request = {
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                quarter: quarterRequest,
                login: currentUser.login
            };
            Contribution.fromUserByQuarter(request).$promise
                .then(displayContributions)
                .catch(displayErrorMessage);
        }

        function loadUserContributions(currentUser) {
            var request = {
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                login: currentUser.login
            };
            Contribution.getAllFromAnAuthor(request).$promise
                .then(displayContributions)
                .catch(displayErrorMessage);
        }

        function loadContributions() {
            /**
             * On récupère l'utilisateur actuellement connecté et appel le service retournant les contributions d'un utilisateur
             */
            Principal.identity().then(function(currentUser) {
                if(vm.quarter.shouldFilter){
                    var quarterRequest = vm.quarter.id + '-' + vm.quarter.year.getFullYear();
                    loadUserContributionsByQuarter(currentUser, quarterRequest);
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
                shouldFilter: vm.quarter.shouldFilter,
                quarterId: vm.quarter.id,
                year:vm.quarter.year
            });
        }

        function displayContributions(response) {
            vm.totalItems = response.headers.totalItems;
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
            vm.contributions = response.data;
        }

        function displayErrorMessage(error) {
            AlertService.error(error.data.message);
        }
    }
})();
