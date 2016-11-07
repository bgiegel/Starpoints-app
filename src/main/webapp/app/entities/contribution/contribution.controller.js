(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('ContributionController', ContributionController);

    ContributionController.$inject = ['Contribution', 'Principal'];

    function ContributionController(Contribution, Principal) {
        var vm = this;

        vm.contributions = [];

        loadAll();

        function loadAll() {
            /**
             * On vérifie si l'utilisateur est admin. Si ce n'est pas le cas on récupère la liste des communautés
             * qu'il dirige afin qu'il ne puisse créé que des contributions pour sa/ses communauté(s).
             */
            Principal.identity().then(function (currentUser) {
                if (currentUser.authorities.indexOf("ROLE_ADMIN") !== -1) {
                    Contribution.query(onSuccess);
                } else if (currentUser.authorities.indexOf("ROLE_LEADER") !== -1) {
                    Contribution.fromCommunitiesLeadedBy({leader: currentUser.login}, onSuccess);
                } else {
                    Contribution.getAllFromAnAuthor({login: currentUser.login}, onSuccess);
                }
            });
        }

        function onSuccess(contributions) {
            vm.contributions = contributions;
        }
    }
})();
