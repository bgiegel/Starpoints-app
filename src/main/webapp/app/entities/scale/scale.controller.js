(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ScaleController', ScaleController);

    ScaleController.$inject = ['$state', 'Scale', 'ParseLinks', 'pagingParams', 'paginationConstants'];

    function ScaleController ($state, Scale, ParseLinks, pagingParams, paginationConstants) {
        var vm = this;

        vm.scales = [];

        //pagination
        vm.page = 1;
        vm.totalItems = null;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadPage = loadPage;
        vm.transition = transition;


        loadAll();

        function loadAll() {
            Scale.query({
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

        function onSuccess(scales, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
            vm.scales = scales;
        }
    }
})();
