(function() {
    'use strict';
    angular
        .module('starPointsApp')
        .factory('Community', Community);

    Community.$inject = ['$resource'];

    function parseJson(data) {
        if (data) {
            data = angular.fromJson(data);
        }
        return data;
    }

    function Community ($resource) {
        var resourceUrl =  'api/communities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    return parseJson(data);
                }
            },
            'leadedBy': {
                method: 'GET',
                isArray: true,
                url:'api/communities-leaded-by/:user',
                transformResponse: function (data) {
                    return parseJson(data);
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
