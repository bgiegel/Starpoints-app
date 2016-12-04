/**
 * Composant qui permet d'afficher des erreurs dans les cas suivant :
 *  - champs est obligatoire
 *  - nombre max de caractères dépassé
 *  - nombre en dehors de limites minimum et maximum
 */
(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .directive('fieldErrors', fieldErrors);

    function fieldErrors() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/form/field-errors.html',
            controller: ['$scope', fieldErrorsController],
            scope: {
                field: '=',
                maxLengthValue:'@',
                min:'@',
                max:'@'
            }
        };
    }

    function fieldErrorsController($scope) {
    }

})();
