(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .directive('contributionsTable', contributionsTable);

    function contributionsTable() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/table/contributions-table.html',
            controller: contributionsTableController,
            scope: {
                contributions:'='
            }
        };
    }

    function contributionsTableController() {

    }

})();
