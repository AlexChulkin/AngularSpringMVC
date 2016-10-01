/**
 * Created by alexc_000 on 2016-09-30.
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