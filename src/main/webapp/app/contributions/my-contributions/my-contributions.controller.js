(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('MyContributionsController', MyContributionsController);

    MyContributionsController.$inject = ['ParseLinks', '$stateParams', 'AlertService', '$state', 'pagingParams', 'paginationConstants', 'Principal', 'Contribution'];

    function MyContributionsController (ParseLinks, $stateParams, AlertService, $state, pagingParams, paginationConstants, Principal, Contribution) {
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
            Contribution.fromUserByQuarter({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                quarter:quarterRequest,
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
                filterByQuarter: vm.shouldFilter,
                quarterId: vm.id,
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
