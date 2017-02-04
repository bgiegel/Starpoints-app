(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ContributionController', ContributionController);

    ContributionController.$inject = ['Contribution', '$state', 'Principal', 'pagingParams', 'paginationConstants'];

    function ContributionController(Contribution, $state, Principal, pagingParams, paginationConstants) {
        var vm = this;

        vm.contributions = [];

        //pagination
        vm.page = 1;
        vm.totalItems = null;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadPage = loadPage;
        vm.transition = transition;

        loadAll();

        function loadAll() {
            /**
             * On vérifie si l'utilisateur est admin. Si ce n'est pas le cas on récupère la liste des communautés
             * qu'il dirige afin qu'il ne puisse créé que des contributions pour sa/ses communauté(s).
             */
            Principal.identity().then(function (currentUser) {
                if (currentUser.authorities.indexOf("ROLE_ADMIN") !== -1) {
                    Contribution.getAll({
                        page: pagingParams.page - 1,
                        size: vm.itemsPerPage
                    }).$promise.then(displayContributions);
                } else if (currentUser.authorities.indexOf("ROLE_LEADER") !== -1) {
                    Contribution.fromCommunitiesLeadedBy({
                        leader: currentUser.login,
                        page: pagingParams.page - 1,
                        size: vm.itemsPerPage
                    }).$promise.then(displayContributions);
                } else {
                    Contribution.getAllFromAnAuthor({
                        login: currentUser.login,
                        page: pagingParams.page - 1,
                        size: vm.itemsPerPage
                    }).$promise.then(displayContributions);
                }
            });
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

        function displayContributions(response) {
            vm.totalItems = response.headers.totalItems;
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
            vm.contributions = response.data;
        }
    }
})();
