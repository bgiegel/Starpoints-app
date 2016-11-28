(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ContributionController', ContributionController);

    ContributionController.$inject = ['Contribution', '$state', 'Principal', 'ParseLinks', 'pagingParams', 'paginationConstants'];

    function ContributionController(Contribution, $state, Principal, ParseLinks, pagingParams, paginationConstants) {
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
                if (currentUser.authorities.indexOf("Admin") !== -1) {
                    Contribution.query({
                        page: pagingParams.page - 1,
                        size: vm.itemsPerPage
                    } ,onSuccess);
                } else if (currentUser.authorities.indexOf("Leader") !== -1) {
                    Contribution.fromCommunitiesLeadedBy({
                        leader: currentUser.login,
                        page: pagingParams.page - 1,
                        size: vm.itemsPerPage
                    }, onSuccess);
                } else {
                    Contribution.getAllFromAnAuthor({
                        login: currentUser.login,
                        page: pagingParams.page - 1,
                        size: vm.itemsPerPage
                    }, onSuccess);
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

        function onSuccess(contributions, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
            vm.contributions = contributions;
        }
    }
})();
