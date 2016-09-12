/**
 * Created by alexc_000 on 2016-09-06.
 */
angular.module("packetAdminApp")
    .constant("authUrl", "/users/login")
    .constant("mainUrl", "/main")
    .constant("loginUrl", "/login")
    .controller("authCtrl", function ($scope, $http, $location, $cookies, authUrl, mainUrl, loginUrl, exchangeService) {
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
                    $scope.data.role = role;
                    $scope.data.username = username;
                    $scope.data.authenticationError = null;
                    $location.path(mainUrl);
                } else {
                    setAuthenticationError({status: "username and/or password are incorrect"});
                }
            }).error(function (error) {
                setAuthenticationError(error);
            }).finally(function (data) {
                $scope.data.password = null;
            });
        };

        $scope.isUserAuthorized = function () {
            return $scope.data.role;
        };

        $scope.logout = function () {
            exchangeService.init();
            nullifyAll();
            $location.path(loginUrl);
        };

        var setAuthenticationError = function (authenticationError) {
            $cookies.remove("username");
            $cookies.remove("role");
            $scope.data.authenticationError = authenticationError;
            $scope.data.authenticationError.report = authenticationError.status
                ? 'Authentication failed (' + authenticationError.status + '). Try again.'
                : 'Authentication failed. Try again.';
        };

        var nullifyAll = function () {
            $cookies.remove("username");
            $cookies.remove("role");
            $scope.data = {};
        };

        var init = function () {
            $scope.data = {};
            $scope.data.username = $cookies.get("username");
            $scope.data.role = $cookies.get("role");
        };

        init();
    });