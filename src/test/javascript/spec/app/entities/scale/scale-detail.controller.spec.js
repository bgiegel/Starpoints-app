'use strict';

describe('Controller Tests', function() {

    describe('Scale Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockScale, MockActivity;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockScale = jasmine.createSpy('MockScale');
            MockActivity = jasmine.createSpy('MockActivity');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Scale': MockScale,
                'Activity': MockActivity
            };
            createController = function() {
                $injector.get('$controller')("ScaleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'starPointsApp:scaleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
