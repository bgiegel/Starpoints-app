'use strict';

describe('Controller Tests', function() {

    describe('Contribution Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockContribution, MockActivity, MockCommunity, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockContribution = jasmine.createSpy('MockContribution');
            MockActivity = jasmine.createSpy('MockActivity');
            MockCommunity = jasmine.createSpy('MockCommunity');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Contribution': MockContribution,
                'Activity': MockActivity,
                'Community': MockCommunity,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("ContributionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'starPointsApp:contributionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
