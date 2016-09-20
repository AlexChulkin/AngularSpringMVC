/**
 * Created by alexc_000 on 2016-09-20.
 */
angular.module("packetAdminApp")
    .directive('blacklist', ['exchangeService', function (exchangeService) {
        return {
            require: 'ngModel',
            link: function (scope, elem, attr, ctrl) {
                ctrl.$parsers.unshift(function (label) {
                    var updatedLabel = label.replace(/\s{2,}/g, " ");
                    var upperCaseLabel = updatedLabel.toUpperCase();
                    var valid = !exchangeService.getSelectedComptLabels(upperCaseLabel);
                    ctrl.$setValidity('blacklist', valid);
                    return valid ? updatedLabel : undefined;
                });
            }
        }
    }]);