/*
 * Copyright (c) 2016.  Alex Chulkin
 */

'use strict';

/**
 * The blacklist directive for the comptsPanel controller.
 */

angular.module("packetAdminApp")
    .directive('blacklist', function (exchangeService) {
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
    });