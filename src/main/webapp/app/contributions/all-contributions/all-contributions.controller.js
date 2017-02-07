(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('AllContributionsController', AllContributionsController);

    AllContributionsController.$inject = ['Contribution', 'moment', '$stateParams', 'AlertService', '$state', 'pagingParams', 'paginationConstants'];

    function AllContributionsController(Contribution, moment, $stateParams, AlertService, $state, pagingParams, paginationConstants) {
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
        vm.exportContributions = exportContributions;

        loadContributions();

        function loadAllContributionsByQuarter(quarterRequest) {
            var request = {
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                quarter: quarterRequest
            };
            Contribution.byQuarter(request).$promise
                .then(displayContributions)
                .catch(displayErrorMessage);
        }

        function loadAllContributions() {
            var request = {
                page: pagingParams.page - 1,
                size: vm.itemsPerPage
            };
            Contribution.getAll(request).$promise
                .then(displayContributions)
                .catch(displayErrorMessage);
        }

        function exportAllContributionsByQuarter(quarterRequest) {
            var request = {
                page: 0,
                size: 100000,
                quarter: quarterRequest
            };
            return Contribution.byQuarter(request).$promise
                .then(formatData)
                .catch(displayErrorMessage);
        }

        function exportAllContributions() {
            var request = {
                page: 0,
                size: 100000
            };
            return Contribution.getAll(request).$promise
                .then(formatData)
                .catch(displayErrorMessage);
        }

        function loadContributions() {
            if (vm.quarter.shouldFilter) {
                var quarterRequest = vm.quarter.id + '-' + vm.quarter.year.getFullYear();
                loadAllContributionsByQuarter(quarterRequest);
            } else {
                loadAllContributions();
            }
        }

        function exportContributions() {
            var datetime = moment().format("DDMMYYYY_HHmmss");
            if (vm.quarter.shouldFilter) {
                var quarterRequest = vm.quarter.id + '-' + vm.quarter.year.getFullYear();
                vm.filename= 'export_contributions_'+quarterRequest+'_'+datetime;
                return exportAllContributionsByQuarter(quarterRequest);
            } else {
                vm.filename= 'export_contributions_complet_'+datetime;
                return exportAllContributions();
            }
        }


        function loadPage(page) {
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

        /**
         * Formatte les champs activité, communauté et auteur pour ne garder que les informations
         * qu'on veut afficher.
         */
        function formatData(response) {
            response.data.forEach(function(element) {
                element.activity = element.activity.name;
                element.community = element.community.name;
                element.author = element.author.firstName + ' ' + element.author.lastName;
            });

            return response.data;
        }

        function displayErrorMessage(error) {
            AlertService.error(error.data.message);
        }
    }
})();
