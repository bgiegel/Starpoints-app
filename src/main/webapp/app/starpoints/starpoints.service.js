(function() {
    'use strict';
    angular
        .module('starPointsApp')
        .factory('Starpoints', Starpoints);

    Starpoints.$inject = ['$resource'];
    function Starpoints ($resource) {
        return $resource('api/starpoints-by-community/:userId', {}, {
            'byCommunity': {
                method: 'GET',
                url: 'api/starpoints-by-community/:userId',
                isArray: true,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'byCommunityForAllUsers': {
                method: 'GET',
                url: 'api/starpoints-by-community',
                isArray: true,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            }
        });
    }
})();
