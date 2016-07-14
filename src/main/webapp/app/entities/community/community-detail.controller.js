(function() {
    'use strict';

    angular
        .module('starPointsApp')
        .controller('CommunityDetailController', CommunityDetailController);

    CommunityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Community', 'Person'];

    function CommunityDetailController($scope, $rootScope, $stateParams, entity, Community, Person) {
        var vm = this;

        vm.community = entity;

        var unsubscribe = $rootScope.$on('starPointsApp:communityUpdate', function(event, result) {
            vm.community = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
