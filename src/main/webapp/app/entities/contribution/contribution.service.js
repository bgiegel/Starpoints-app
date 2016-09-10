(function() {
    'use strict';
    angular
        .module('starPointsApp')
        .factory('Contribution', Contribution);

    Contribution.$inject = ['$resource', 'DateUtils'];

    function parseJson(data) {
        if (data) {
            data = angular.fromJson(data);
        }
        return data;
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
            'fromCommunitiesLeadedBy': {
                method: 'GET',
                isArray: true,
                url:'api/contributions-from-communities-leaded-by/:leader',
                transformResponse: function (data) {
                    return parseJson(data);
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.deliverableDate = DateUtils.convertLocalDateToServer(data.deliverableDate);
                    data.preparatoryDate1 = DateUtils.convertLocalDateToServer(data.preparatoryDate1);
                    data.preparatoryDate2 = DateUtils.convertLocalDateToServer(data.preparatoryDate2);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.deliverableDate = DateUtils.convertLocalDateToServer(data.deliverableDate);
                    data.preparatoryDate1 = DateUtils.convertLocalDateToServer(data.preparatoryDate1);
                    data.preparatoryDate2 = DateUtils.convertLocalDateToServer(data.preparatoryDate2);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
