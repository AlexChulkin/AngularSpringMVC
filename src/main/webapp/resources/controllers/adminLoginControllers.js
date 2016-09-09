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
                securityParams: securityParams,
                withCredentials: true
            }).success(function (role) {
                if (role) {
                    $scope.role = role;
                    $scope.username = username;
                    $scope.authenticationError = undefined;
                    $cookies.put("username", $scope.username);
                    $cookies.put("role", $scope.role);
                    $cookies.remove("authenticationError");
                    $location.path(mainUrl);
                } else {
                    setAuthenticationError({status: "Username and/or password are incorrect"});
                }
            }).error(function (error) {
                setAuthenticationError(error);
            }).finally(function (data) {
                $scope.password = undefined;
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
            $scope.username = undefined;
            $scope.role = undefined;
            $scope.authenticationError = authenticationError;
            $cookies.put("authenticationError", $scope.authenticationError);
        };

        var nullifyAll = function () {
            $cookies.remove("username");
            $cookies.remove("role");
            $cookies.remove("authenticationError");
            $scope.username = undefined;
            $scope.role = undefined;
            $scope.authenticationError = undefined;
        };

        var init = function () {
            $scope.username = $cookies.get("username");
            $scope.role = $cookies.get("role");
            $scope.authenticationError = $cookies.get("authenticationError");
        };

        init();
    });