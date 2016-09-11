/**
 * Created by alexc_000 on 2016-09-06.
 */
angular.module("packetAdminApp")
    .constant("authUrl", "/users/login")
    .constant("mainUrl", "/main")
    .constant("loginUrl", "/login")
    .controller("authCtrl", function ($scope, $http, $location, $cookies, authUrl, mainUrl, loginUrl) {
        $scope.authenticate = function (username, pass) {
            var securityParams = {
                username: username,
                password: pass
            };
            $http.post(contextPath + authUrl, {
                securityParams: securityParams
            }).success(function (role) {
                if (role) {
                    $cookies.put("username", username);
                    $cookies.put("role", role);
                    $scope.role = role;
                    $scope.username = username;
                    $scope.authenticationError = null;
                    $location.path(mainUrl);
                } else {
                    setAuthenticationError({status: "username and/or password are incorrect"});
                }
            }).error(function (error) {
                setAuthenticationError(error);
            }).finally(function (data) {
                $scope.password = null;
            });
        };

        $scope.isUserAuthorized = function () {
            return $scope.role;
        };

        $scope.logout = function () {
            $location.path(loginUrl);
            nullifyAll();
        };

        var setAuthenticationError = function (authenticationError) {
            $cookies.remove("username");
            $cookies.remove("role");
            $scope = {};
            $scope.authenticationError = authenticationError;
            $scope.authenticationError.report = authenticationError.status
                ? 'Authentication failed (' + authenticationError.status + '). Try again.'
                : 'Authentication failed. Try again.';
        };

        var nullifyAll = function () {
            $cookies.remove("username");
            $cookies.remove("role");
            $scope = {};
        };

        var init = function () {
            $scope = {};
            $scope.username = $cookies.get("username");
            $scope.role = $cookies.get("role");
        };

        init();
    });