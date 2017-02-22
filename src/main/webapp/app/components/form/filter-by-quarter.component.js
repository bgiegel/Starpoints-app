(function () {
    'use strict';

    angular
        .module('starPointsApp')
        .component('filterByQuarter', {
            templateUrl: 'app/components/form/filter-by-quarter.html',
            controller: filterByQuarterController,
            bindings: {
                quarter:'<',
                filterContributions:'&'
            }
        });


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
