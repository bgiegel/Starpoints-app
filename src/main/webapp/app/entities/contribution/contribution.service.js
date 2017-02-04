(function() {
    'use strict';
    angular
        .module('starPointsApp')
        .factory('Contribution', Contribution);

    Contribution.$inject = ['$resource', 'DateUtils'];

    function parseJson(data, headers) {
        var response = {};
        if (data) {
            response.data = angular.fromJson(data);
            if(headers){
                response.headers = {
                    totalItems:headers('X-Total-Count'),
                    links:headers('link')
                };
            }

        }
        return response;
    }

    function Contribution ($resource, DateUtils) {
        var resourceUrl =  'api/contributions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.deliverableDate = DateUtils.convertLocalDateFromServer(data.deliverableDate);
                        data.preparatoryDate1 = DateUtils.convertLocalDateFromServer(data.preparatoryDate1);
                        data.preparatoryDate2 = DateUtils.convertLocalDateFromServer(data.preparatoryDate2);
                    }
                    return data;
                }
            },
            'getAll': {
                method: 'GET',
                transformResponse: function (data, headers) {
                    return parseJson(data, headers);
                }
            },
            'fromCommunitiesLeadedBy': {
                method: 'GET',
                url:'api/contributions-from-communities-leaded-by/:leader',
                transformResponse: function (data, headers) {
                    return parseJson(data, headers);
                }
            },
            'getAllFromAnAuthor': {
                method: 'GET',
                url:'api/contributions/author/:login',
                transformResponse: function (data, headers) {
                    return parseJson(data, headers);
                }
            },
            'fromUserByQuarter': {
                method: 'GET',
                url:'api/contributions-by-quarter/:quarter/:login',
                transformResponse: function (data, headers) {
                    return parseJson(data, headers);
                }
            },
            'byQuarter': {
                method: 'GET',
                url:'api/contributions-by-quarter/:quarter',
                transformResponse: function (data, headers) {
                    return parseJson(data, headers);
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            }
        });
    }
})();
