'use strict';

describe('Controller Tests', function(){

    beforeEach(mockApiAccountCall);
    beforeEach(mockI18nCalls);

    describe('AllStarPointsController', function(){

        var $scope, createController, q;
        var MockStarpoints;
        var expectedResponse = [{community:'Java', starpoints:200}, {community:'Agile', starpoints:35}];

        var controller;

        function initMockObjects() {
            MockStarpoints = jasmine.createSpyObj('MockStarpoints', ['byCommunityForAllUsers']);
        }

        beforeEach(inject(function($rootScope, $controller, $q){
            $scope = $rootScope.$new();
            q = $q;

            initMockObjects();

            var locals = {
                '$scope': $scope,
                'Starpoints':MockStarpoints
            };

            createController = function(){
                return $controller('AllStarPointsController as vm',locals);
            };
        }));

        it ('should load starpoints by Community for all users',  function(){

            //given
            givenByCommunityForAllUsersCallSuccessful();

            //when
            whenControllerIsExecuted();

            //then
            expectByCommunityForAllUsersHasBeenCalled();
            expectStarpointsByCommunityHasBeenUpdated();
        });

        function givenByCommunityForAllUsersCallSuccessful() {
            MockStarpoints.byCommunityForAllUsers.and.returnValue({$promise: q.when(expectedResponse)});
        }

        function whenControllerIsExecuted() {
            controller = $scope.$apply(createController);
        }

        function expectByCommunityForAllUsersHasBeenCalled() {
            expect(MockStarpoints.byCommunityForAllUsers).toHaveBeenCalled();
        }

        function expectStarpointsByCommunityHasBeenUpdated() {
            expect($scope.vm.allStarpointsByCommunity).toBe(expectedResponse);
        }
    });
});
