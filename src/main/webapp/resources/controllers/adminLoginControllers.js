/**
 * Created by alexc_000 on 2016-09-06.
 */
angular.module("packetAdminApp")
    .constant("authUrl", "/users/login")
    .constant("mainUrl", "/main")
    .constant("loginUrl", "/login")
    .constant("timeoutLogoutDelay", 30 * 60 * 1000)
    .controller("authCtrl", function ($scope, $http, $location, $cookies, $timeout,
                                      authUrl, mainUrl, loginUrl, timeoutLogoutDelay, exchangeService) {

        var timeoutLogoutPromise;

        $scope.authenticate = function (username, pass) {
            var securityParams = {
                username: username,
                password: pass
            };
            $http.post(contextPath + authUrl, {
                securityParams: securityParams
            }).success(function (role) {
                if (role) {
                    timeoutLogoutPromise = $timeout(timeoutLogoutFn, timeoutLogoutDelay);
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
            }).finally(function () {
                $scope.data.timeout = false;
                $scope.data.password = null;
            });
        };

        $scope.isUserAuthorized = function () {
            return !exchangeService.isUndefinedOrNull($scope.data.role);
        };

        $scope.logout = function () {
            $timeout.cancel(timeoutLogoutPromise);
            exchangeService.init();
            nullifyAll();
            $location.path(loginUrl);
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
            $scope.data.authenticationError.report = authenticationError.status
                ? 'Authentication failed (' + authenticationError.status + '). Try again.'
                : 'Authentication failed. Try again.';
        };

        var nullifyAll = function () {
            $scope.data = {};
            $cookies.remove("username");
            $cookies.remove("role");
        };

        var init = function () {
            $scope.data = {};
            $scope.data.username = $cookies.get("username");
            $scope.data.role = $cookies.get("role");
        };

        init();
    });