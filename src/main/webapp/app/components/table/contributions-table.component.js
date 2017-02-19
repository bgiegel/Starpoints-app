(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .component('contributionsTable', {
            templateUrl: 'app/components/table/contributions-table.html',
            bindings: {
                contributions:'<'
            }
        });

})();
