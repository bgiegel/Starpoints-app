(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ActivityController', ActivityController);

    ActivityController.$inject = ['$state', 'Activity', 'ParseLinks', 'pagingParams', 'paginationConstants'];

    function ActivityController ($state, Activity, ParseLinks, pagingParams, paginationConstants) {
        var vm = this;

        vm.activities = [];

        //pagination
        vm.page = 1;
        vm.totalItems = null;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadPage = loadPage;
        vm.transition = transition;

        loadAll();

        function loadAll() {
            Activity.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage
            }, onSuccess);
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page
            });
        }

        function onSuccess(activities, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
            vm.activities = activities;
        }
    }
})();
