/**
 * Created by alexc_000 on 2016-09-06.
 */
angular.module("packetAppAdmin")
    .constant("authUrl", "/users/login")
    .constant("mainUrl", "/main")
    .constant("loginUrl", "/login")
    .controller("authCtrl", function ($scope, $http, $location, authUrl, mainUrl, loginUrl) {
        $scope.authenticate = function (username, pass) {
            var securityParams = {
                username: username,
                password: pass
            };
            $http.post(contextPath + authUrl, {
                securityParams: securityParams,
                withCredentials: true
            }).success(function (role) {
                if (role) {
                    init();
                    $scope.role = role;
                    $scope.username = username;
                    $location.path(mainUrl);
                } else {
                    init();
                    $scope.authenticationError = {status: "Username and/or password are incorrect"};
                }
            }).error(function (error) {
                init();
                $scope.authenticationError = error;
            });
        };

        $scope.isUserAuthorized = function () {
            return $scope.username !== null;
        };

        $scope.logout = function () {
            $location.path(loginUrl);
            init();
        };

        var init = function () {
            $scope.username = null;
            $scope.password = null;
            $scope.role = null;
            $scope.authenticationError = null;
        };

        init();
    });