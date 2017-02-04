'use strict';

describe('Controller Tests', function(){

    beforeEach(mockApiAccountCall);
    beforeEach(mockI18nCalls);

    describe('MyContributionsController', function(){

        var $scope, createController, q;
        var MockStateParams, MockPrincipal, MockContribution, MockPagingParams,
            MockPaginationConstants, MockAlertService, MockState;
        var testUser = {login: 'testLogin'};
        var expectedResponse = {
            data:[{id:'1', name:'contrib1'}, {id:'2', name:'contrib2'}],
            headers:[{'X-Total-Count':'5'}]
        };
        var error = {data : { message: "Une erreur s'est produite."}};
        var controller;

        function initMockObjects() {
            MockStateParams = jasmine.createSpy('MockStateParams');
            MockAlertService = jasmine.createSpyObj('MockAlertService', ['error']);
            MockState = jasmine.createSpyObj('MockState', ['transitionTo']);
            MockState.$current = 'currentState';
            MockPrincipal = jasmine.createSpyObj('MockPrincipal', ['identity']);

            MockContribution = jasmine.createSpyObj('MockContribution', ['getAllFromAnAuthor', 'fromUserByQuarter']);
            MockPagingParams = jasmine.createSpy('MockPagingParams');

            MockPagingParams.page = 1;
            MockPaginationConstants = jasmine.createSpy('MockPaginationConstants');

            MockPaginationConstants.itemsPerPage = 10;
        }

        beforeEach(inject(function($rootScope, $controller, $q){
            $scope = $rootScope.$new();
            q = $q;

            initMockObjects();

            var locals = {
                '$scope': $scope,
                '$stateParams':MockStateParams,
                'AlertService':MockAlertService,
                '$state':MockState,
                'pagingParams':MockPagingParams,
                'paginationConstants':MockPaginationConstants,
                'Principal':MockPrincipal,
                'Contribution':MockContribution
            };

            createController = function(){
                return $controller('MyContributionsController as vm',locals);
            };
        }));

        it ('should load contributions without filtering by quarter',  function(){

            //given
            givenAConnectedUser();
            givenGetAllFromAnAuthorCallSuccessful();
            givenFilterByQuarterIsNotActivated();

            //when
            whenControllerIsExecuted();

            //then
            expectGetAllFromAnAuthorHasBeenCalled();
            expectContributionsHasBeenUpdated();
        });

        it ('should filter contributions by quarter',  function(){

            //given
            givenAConnectedUser();
            givenFromUserByQuarterCallSuccessful();
            givenFilterByQuarterIsActivated();

            //when
            whenControllerIsExecuted();

            //then
            expectFromUserByQuarterHasBeenCalledWithQuarterRequest();
            expectContributionsHasBeenUpdated();
        });

        it ('should display a message if Contribution service call is not successful',  function(){

            //given
            givenAConnectedUser();
            givenGetAllFromAnAuthorCallUnsuccessful();
            givenFilterByQuarterIsNotActivated();

            //when
            whenControllerIsExecuted();

            //then
            expect(MockAlertService.error).toHaveBeenCalledWith(error.data.message)
        });

        it ('should display a message if Contribution service call is not successful',  function(){

            //given
            givenAConnectedUser();
            givenFromUserByQuarterCallSuccessful();
            givenFilterByQuarterIsActivated();
            MockState.transitionTo.and.callThrough();

            //when
            whenControllerIsExecuted();
            controller.loadPage(2);

            //then
            expect($scope.vm.page).toEqual(2);
            var expectedParams = {
                page: 2,
                shouldFilter: true,
                quarterId: 'Q1',
                year:new Date(2016,1,1)
            };
            expect(MockState.transitionTo).toHaveBeenCalledWith('currentState', expectedParams)
        });

        function givenAConnectedUser() {
            MockPrincipal.identity.and.returnValue(q.resolve(testUser));
        }

        function givenGetAllFromAnAuthorCallSuccessful() {
            MockContribution.getAllFromAnAuthor.and.returnValue({$promise: q.when(expectedResponse)});
        }

        function givenGetAllFromAnAuthorCallUnsuccessful() {
            MockContribution.getAllFromAnAuthor.and.returnValue({$promise: q.reject(error)});
        }

        function givenFromUserByQuarterCallSuccessful() {
            MockContribution.fromUserByQuarter.and.returnValue({$promise: q.when(expectedResponse)});
        }

        function givenFilterByQuarterIsNotActivated() {
            buildQuarter(false);
        }
        function givenFilterByQuarterIsActivated() {
            buildQuarter(true);
        }

        function buildQuarter(shouldFilter) {
            MockStateParams.shouldFilter = shouldFilter;
            MockStateParams.quarterId = 'Q1';
            MockStateParams.year = new Date(2016,1,1);
        }

        function whenControllerIsExecuted() {
            controller = $scope.$apply(createController);
        }

        function expectGetAllFromAnAuthorHasBeenCalled() {
            var expectedParams = {
                page: 0,
                size: 10,
                login: testUser.login
            };
            expect(MockContribution.getAllFromAnAuthor).toHaveBeenCalledWith(expectedParams);
        }
        function expectFromUserByQuarterHasBeenCalledWithQuarterRequest() {
            var expectedParams = {
                page: 0,
                size: 10,
                login: testUser.login,
                quarter:'Q1-2016'
            };
            expect(MockContribution.fromUserByQuarter).toHaveBeenCalledWith(expectedParams);
        }

        function expectContributionsHasBeenUpdated() {
            expect($scope.vm.contributions).toBe(expectedResponse.data);
        }
    });

});
