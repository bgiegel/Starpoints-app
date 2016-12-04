(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .directive('findUser', findUser);

    function findUser() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/form/find-user.html',
            controller: ['$scope', findUserController],
            scope: {
                usersList: '=users',
                searchedUser: '=ngModel',
                onSelectAction: '&',
                required: '='
            },
            require: "^form", //injecte le formulaire du parent en parametre de la fonction parent
            link: function (scope, element, attrs, form) {
                scope.form = form; //save parent form
            }
        };

        function findUserController($scope) {
            /**
             * Fonction qui filtre les utilisateurs du composant typeahead. Le filtre se fait sur le champs firstName et lastName
             * sans tenir compte de la casse.
             * @param usersList la liste des utilisateurs a filtrer
             * @param typedValue la valeur qui a été saisie par l'utilisateur
             * @returns {*} la liste filtré à partir de la saisie utilisateur"
             */
            $scope.filterByName = function (usersList, typedValue) {
                return usersList.filter(function (user) {
                    var matchesFirstName = user.firstName.toLowerCase().indexOf(typedValue.toLowerCase()) != -1;
                    var matchesLastName = user.lastName.toLowerCase().indexOf(typedValue.toLowerCase()) != -1;

                    return matchesFirstName || matchesLastName;
                });
            }
        }
    }

})();
