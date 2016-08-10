'use strict';

describe('Controller Tests', function() {

    describe('Activity Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockActivity, MockLevel;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockActivity = jasmine.createSpy('MockActivity');
            MockLevel = jasmine.createSpy('MockLevel');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Activity': MockActivity,
                'Level': MockLevel
            };
            createController = function() {
                $injector.get('$controller')("ActivityDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'starPointsApp:activityUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
