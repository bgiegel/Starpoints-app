(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('LevelController', LevelController);

    LevelController.$inject = ['Level'];

    function LevelController (Level) {
        var vm = this;

        vm.levels = [];

        loadAll();

        function loadAll() {
            Level.query(function(result) {
                vm.levels = result;
            });
        }
    }
})();
