/**
 * Created by alexc_000 on 2016-09-06.
 */
'use strict';

angular.module("packetAdminApp")
    .constant("authUrl", "/users/login")
    .constant("mainUrl", "/main")
    .constant("loginUrl", "/login")
    .constant("timeoutLogoutDelay", 30 * 60 * 1000)
    .controller("loginCtrl", function ($scope, $http, $location, $cookies, $timeout, authUrl, mainUrl, loginUrl,
                                       timeoutLogoutDelay, exchangeService, helperService) {

        var timeoutLogoutPromise;

        $scope.authenticate = function (username, pass) {
            var securityParams = {
                username: username,
                password: pass
            };
            $http.post(authUrl, {
                securityParams: securityParams
            }).success(function (role) {
                if (role) {
                    $scope.data.authorizationIsInProcess = true;
                    timeoutLogoutPromise = $timeout(timeoutLogoutFn, timeoutLogoutDelay);
                    $location.path(mainUrl);
                    $cookies.put("username", username);
                    $cookies.put("role", role);
                    $scope.data.role = role;
                    $scope.data.username = username;
                    $scope.data.authenticationError = null;
                    $scope.data.authorizationIsInProcess = false;
                } else {
                    setAuthenticationError({status: "username and/or password are incorrect"});
                }
            }).error(function (error) {
                setAuthenticationError(error);
            }).finally(function () {
                $scope.data.timeout = false;
                $scope.data.password = null;
            });
        };

        $scope.isUserAuthorized = function () {
            return !helperService.isUndefinedOrNull($scope.data.role) && !$scope.data.authorizationIsInProcess;
        };

        $scope.logout = function () {
            $timeout.cancel(timeoutLogoutPromise);
            exchangeService.init();
            $scope.data = {};
            $cookies.remove("username");
            $cookies.remove("role");
            $scope.redirectToLoginPage();
        };

        $scope.redirectToLoginPage = function () {
            $location.path(loginUrl);
        };

        $scope.redirectToMainPage = function () {
            $location.path(mainUrl);
        };

        var timeoutLogoutFn = function () {
            $scope.logout();
            $scope.data.timeout = true;
        };
        
        var setAuthenticationError = function (authenticationError) {
            $cookies.remove("username");
            $cookies.remove("role");
            $scope.data.authenticationError = authenticationError;
            if ($scope.data.authenticationError) {
                $scope.data.authenticationError.report = authenticationError.status
                    ? 'Authentication failed (' + authenticationError.status + '). Try again.'
                    : 'Authentication failed. Try again.';
            } else {
                $scope.data.authenticationError = {report: 'Authentication failed. Try again.'};
            }
        };

        var init = function () {
            $scope.data = {};
            $scope.data.username = $cookies.get("username");
            $scope.data.role = $cookies.get("role");
            $scope.data.authorizationIsInProcess = false;
        };

        init();
    });