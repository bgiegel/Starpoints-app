(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .directive('jhSortBy', jhSortBy);

    function jhSortBy() {
        return {
            restrict: 'A',
            scope: false,
            require: '^jhSort',
            link: linkFunc
        };

        function linkFunc(scope, element, attrs, parentCtrl) {
            element.bind('click', function () {
                parentCtrl.sort(attrs.jhSortBy);
            });
        }
    }
})();
