(function() {
    'use strict';
    angular
        .module('starPointsApp')
        .factory('Community', Community);

    Community.$inject = ['$resource'];

    function Community ($resource) {
        var resourceUrl =  'api/communities/:id';

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
