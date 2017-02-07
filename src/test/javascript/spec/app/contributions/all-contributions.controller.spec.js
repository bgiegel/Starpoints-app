'use strict';

describe('Controller Tests', function(){

    beforeEach(mockApiAccountCall);
    beforeEach(mockI18nCalls);

    describe('AllContributionsController', function(){

        var $scope, createController, q;
        var MockStateParams, MockContribution, MockPagingParams,
            MockPaginationConstants, MockAlertService, MockState;
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

            MockContribution = jasmine.createSpyObj('MockContribution', ['getAll', 'byQuarter']);
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
                'Contribution':MockContribution
            };

            createController = function(){
                return $controller('AllContributionsController as vm',locals);
            };
        }));

        it ('should load contributions without filtering by quarter',  function(){

            //given
            givenGetAllCallSuccessful();
            givenFilterByQuarterIsNotActivated();

            //when
            whenControllerIsExecuted();

            //then
            expectGetAllHasBeenCalled();
            expectContributionsHasBeenUpdated();
        });

        it ('should filter contributions by quarter',  function(){

            //given
            givenByQuarterCallSuccessful();
            givenFilterByQuarterIsActivated();

            //when
            whenControllerIsExecuted();

            //then
            expectByQuarterHasBeenCalled();
            expectContributionsHasBeenUpdated();
        });

        it ('should display a message if Contribution service call is not successful',  function(){

            //given
            givenGetAllCallUnsuccessful();
            givenFilterByQuarterIsNotActivated();

            //when
            whenControllerIsExecuted();

            //then
            expect(MockAlertService.error).toHaveBeenCalledWith(error.data.message)
        });

        it ('should display page number 2 with current quarter filtering params',  function(){
            //given
            givenByQuarterCallSuccessful();
            givenFilterByQuarterIsActivated();
            MockState.transitionTo.and.callThrough();

            //when
            whenControllerIsExecuted();
            controller.loadPage(2);

            //then
            expect($scope.vm.page).toEqual(2);
            expectTransitionHasBeenMadeWithPageAndCurrentQuarterParams();
        });

        it ('should export all contributions',  function(){
            //given
            givenGetAllCallSuccessful();
            givenFilterByQuarterIsNotActivated();

            //when
            whenControllerIsExecuted();
            controller.exportContributions();

            //then
            expectFileNameHasBeenSet();
            expectGetAllHasBeenCalledToReturnAllContributions()
        });

        it ('should export specific quarter contributions',  function(){
            //given
            givenByQuarterCallSuccessful();
            givenFilterByQuarterIsActivated();

            //when
            whenControllerIsExecuted();
            controller.exportContributions();

            //then
            expectFileNameHasBeenSetWithQuarterRequest();
            expectByQuarterHasBeenCalledWithQuarterRequest();
        });

        function givenGetAllCallSuccessful() {
            MockContribution.getAll.and.returnValue({$promise: q.when(expectedResponse)});
        }

        function givenGetAllCallUnsuccessful() {
            MockContribution.getAll.and.returnValue({$promise: q.reject(error)});
        }

        function givenByQuarterCallSuccessful() {
            MockContribution.byQuarter.and.returnValue({$promise: q.when(expectedResponse)});
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

        function expectTransitionHasBeenMadeWithPageAndCurrentQuarterParams() {
            var expectedParams = {
                page: 2,
                shouldFilter: true,
                quarterId: 'Q1',
                year: new Date(2016, 1, 1)
            };
            expect(MockState.transitionTo).toHaveBeenCalledWith('currentState', expectedParams)
        }
        function expectGetAllHasBeenCalled() {
            var expectedParams = {
                page: 0,
                size: 10
            };
            expect(MockContribution.getAll).toHaveBeenCalledWith(expectedParams);
        }

        function expectGetAllHasBeenCalledToReturnAllContributions() {
            var expectedParams = {
                page: 0,
                size: 100000
            };
            expect(MockContribution.getAll).toHaveBeenCalledWith(expectedParams);
        }

        function expectByQuarterHasBeenCalled() {
            var expectedParams = {
                page: 0,
                size: 10,
                quarter:'Q1-2016'
            };
            expect(MockContribution.byQuarter).toHaveBeenCalledWith(expectedParams);
        }

        function expectByQuarterHasBeenCalledWithQuarterRequest() {
            var expectedParams = {
                page: 0,
                size: 100000,
                quarter:'Q1-2016'
            };
            expect(MockContribution.byQuarter).toHaveBeenCalledWith(expectedParams);
        }

        function expectContributionsHasBeenUpdated() {
            expect($scope.vm.contributions).toBe(expectedResponse.data);
        }

        function expectFileNameHasBeenSet() {
            var datetime = moment().format('DDMMYYYY_HHmmss');
            expect($scope.vm.filename).toEqual('export_contributions_complet_' + datetime);
        }

        function expectFileNameHasBeenSetWithQuarterRequest() {
            var datetime = moment().format('DDMMYYYY_HHmmss');
            var quarterRequest='Q1-2016';
            expect($scope.vm.filename).toEqual('export_contributions_' + quarterRequest + '_' + datetime);
        }
    });


});
