(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('AllContributionsController', AllContributionsController);

    AllContributionsController.$inject = ['Contribution', 'ParseLinks', '$stateParams', 'AlertService', '$state', 'pagingParams', 'paginationConstants', 'Principal'];

    function AllContributionsController(Contribution, ParseLinks, $stateParams, AlertService, $state, pagingParams, paginationConstants) {
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
            Contribution.byQuarter({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                quarter: quarterRequest
            }, onSuccess, onError);
        }

        function loadAllContributions() {
            Contribution.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage
            } ,onSuccess, onError);
        }

        function exportAllContributionsByQuarter(quarterRequest) {
            return Contribution.byQuarter({
                page: 0,
                size: 100000,
                quarter: quarterRequest
            }).$promise.then(onExportSuccess).catch(onError);
        }

        function exportAllContributions() {
            return Contribution.query({
                page: 0,
                size: 10000
            }).$promise.then(onExportSuccess).catch(onError);
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
            if (vm.quarter.shouldFilter) {
                var quarterRequest = vm.quarter.id + '-' + vm.quarter.year.getFullYear();
                return exportAllContributionsByQuarter(quarterRequest);
            } else {
                return exportAllContributions();
            }
        }


        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                filterByQuarter: vm.shouldFilter,
                quarterId: vm.id,
                year: vm.year
            });
        }

        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
            vm.contributions = data;
        }

        function onExportSuccess(data) {
            data.forEach(function(element) {
                element.activity = element.activity.name;
                element.community = element.community.name;
                element.author = element.author.firstName + ' ' + element.author.lastName;
            });

            return data;
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }
    }
})();
