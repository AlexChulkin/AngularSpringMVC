/*
 * Copyright (c) 2016.  Alex Chulkin
 */

'use strict';

/**
 * The "range" and "pageCount" filters for the compts panel controller (pagination).
 */

angular.module("packetAdminApp")
    .filter("range", function ($filter) {
        return function (data, page, size) {
            if (angular.isArray(data) && angular.isNumber(page) && angular.isNumber(size)) {
                var start_index = (page - 1) * size;
                if (data.length <= start_index) {
                    return [];
                } else {
                    return $filter("limitTo")(data.slice(start_index), size);
                }
            } else {
                return data;
            }
        }
    })
    .filter("pageCount", function () {
        return function (data, size) {
            if (angular.isArray(data)) {
                var result = [];
                for (var i = 1; i <= Math.ceil(data.length / size); i++) {
                    result.push(i);
                }
                return result;
            } else {
                return data;
            }
        }
    });