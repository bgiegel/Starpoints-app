'use strict';

describe('Controller Tests', function(){

    beforeEach(mockApiAccountCall);
    beforeEach(mockI18nCalls);

    describe('MyStarPointsController', function(){

        var $scope, createController, q;
        var MockPrincipal, MockStarpoints;
        var testUser = {login: 'testLogin', id:5};
        var expectedResponse = [{community:'Java', starpoints:200}, {community:'Agile', starpoints:35}];

        var controller;

        function initMockObjects() {
            MockPrincipal = jasmine.createSpyObj('MockPrincipal', ['identity']);
            MockStarpoints = jasmine.createSpyObj('MockStarpoints', ['byCommunity']);
        }

        beforeEach(inject(function($rootScope, $controller, $q){
            $scope = $rootScope.$new();
            q = $q;

            initMockObjects();

            var locals = {
                '$scope': $scope,
                'Principal':MockPrincipal,
                'Starpoints':MockStarpoints
            };

            createController = function(){
                return $controller('MyStarPointsController as vm',locals);
            };
        }));

        it ('should load starpoints by Community for the connected user',  function(){

            //given
            givenAConnectedUser();
            givenByCommunityCallSuccessful();

            //when
            whenControllerIsExecuted();

            //then
            expectByCommunityHasBeenCalled();
            expectStarpointsByCommunityHasBeenUpdated();
        });

        function givenAConnectedUser() {
            MockPrincipal.identity.and.returnValue(q.resolve(testUser));
        }

        function givenByCommunityCallSuccessful() {
            MockStarpoints.byCommunity.and.returnValue({$promise: q.when(expectedResponse)});
        }

        function whenControllerIsExecuted() {
            controller = $scope.$apply(createController);
        }

        function expectByCommunityHasBeenCalled() {
            var expectedParams = {
                userId: testUser.id
            };
            expect(MockStarpoints.byCommunity).toHaveBeenCalledWith(expectedParams);
        }

        function expectStarpointsByCommunityHasBeenUpdated() {
            expect($scope.vm.starpointsByCommunity).toBe(expectedResponse);
        }
    });

});
