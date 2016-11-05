/*
 * Copyright (c) 2016.  Alex Chulkin
 */

'use strict';

/**
 * The script for the admin.jsp.
 */

angular.module("packetAdminApp", ["ngRoute", "ngCookies"])
    .config(function ($routeProvider) {
        $routeProvider.when("/login", {
            templateUrl: "/resources/views/adminLogin.html"
        });

        $routeProvider.when("/main", {
            templateUrl: "/resources/views/adminMain.html"
        });

        $routeProvider.otherwise({
            redirectTo: "/login"
        });
    });