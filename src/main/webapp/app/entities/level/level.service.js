(function() {
    'use strict';
    angular
        .module('starPointsApp')
        .factory('Level', Level);

    Level.$inject = ['$resource'];

    function Level ($resource) {
        var resourceUrl =  'api/levels/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
