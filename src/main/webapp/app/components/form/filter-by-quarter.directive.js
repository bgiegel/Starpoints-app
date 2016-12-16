(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .directive('filterByQuarter', filterByQuarter);

    function filterByQuarter() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/form/filter-by-quarter.html',
            controller: filterByQuarterController,
            controllerAs:'vm',
            scope: {
                quarter:'=',
                loadContributions:'&'
            }
        };
    }

    function filterByQuarterController() {
        var vm = this;
        //datePicker
        vm.popup = {
            opened:false
        };
        vm.format= 'yyyy';
        vm.dateOptions = {
            formatYear: 'yyyy',
            minDate: new Date(2000, 1, 1),
            maxDate:new Date(),
            startingDay: 1,
            minMode: 'year'
        };

        vm.openDatePicker = function() {
            vm.popup.opened = true;
        };

    }

})();
