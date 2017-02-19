/**
 * Composant qui permet d'afficher des erreurs dans les cas suivant :
 *  - champs est obligatoire
 *  - nombre max de caractères dépassé
 *  - nombre en dehors de limites minimum et maximum
 *  - etc...
 */
(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .component('fieldErrors', {
            templateUrl: 'app/components/form/field-errors.html',
            bindings: {
                field: '<',
                maxLengthValue:'@',
                min:'@',
                max:'@'
            }
        });

})();
