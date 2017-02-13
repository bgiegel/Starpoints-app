'use strict';

describe('Controller Tests', function(){

    beforeEach(mockApiAccountCall);
    beforeEach(mockI18nCalls);

    describe('MyCommunitiesStarPointsController', function(){

        var $scope, createController, q;
        var MockPrincipal, MockStarpoints;
        var testUser = {login: 'testLogin', id:5};
        var expectedResponse = [{community:'Java', starpoints:200}, {community:'Agile', starpoints:35}];

        var controller;

        function initMockObjects() {
            MockPrincipal = jasmine.createSpyObj('MockPrincipal', ['identity']);
            MockStarpoints = jasmine.createSpyObj('MockStarpoints', ['byCommunityLeadedBy']);
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
                return $controller('MyCommunitiesStarPointsController as vm',locals);
            };
        }));

        it ('should load starpoints by Community for the connected user',  function(){

            //given
            givenAConnectedUser();
            givenByCommunityLeadedByCallSuccessful();

            //when
            whenControllerIsExecuted();

            //then
            expectByCommunityLeadedByHasBeenCalled();
            expectStarpointsByCommunityLeadedByHasBeenUpdated();
        });

        function givenAConnectedUser() {
            MockPrincipal.identity.and.returnValue(q.resolve(testUser));
        }

        function givenByCommunityLeadedByCallSuccessful() {
            MockStarpoints.byCommunityLeadedBy.and.returnValue({$promise: q.when(expectedResponse)});
        }

        function whenControllerIsExecuted() {
            controller = $scope.$apply(createController);
        }

        function expectByCommunityLeadedByHasBeenCalled() {
            var expectedParams = {
                leaderId: testUser.id
            };
            expect(MockStarpoints.byCommunityLeadedBy).toHaveBeenCalledWith(expectedParams);
        }

        function expectStarpointsByCommunityLeadedByHasBeenUpdated() {
            expect($scope.vm.starpointsByCommunity).toBe(expectedResponse);
        }
    });

});
