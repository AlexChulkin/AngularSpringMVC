/**
 * Created by alexc_000 on 2016-09-30.
 */
'use strict';

describe("Controller Test", function () {
    var mockScope = {};
    var controller;
    var location;
    var cookies;
    var mockTimeout;
    var mainUrl_, loginUrl_, authUrl_;
    var username, password, role;
    var username_label;
    var initial_username_value;
    var role_label;
    var initial_role_value;
    var timeoutLogoutDelay_;
    var backend;
    var initial_username, initial_role;
    var error;
    var errorStatus;

    beforeEach(function () {
        module("packetAdminApp");
    });

    beforeEach(angular.mock.inject(function ($httpBackend, mainUrl, loginUrl, authUrl) {
        mainUrl_ = mainUrl;
        loginUrl_ = loginUrl;
        authUrl_ = authUrl;
        role = "ADMIN";
        backend = $httpBackend;
    }));

    beforeEach(angular.mock.inject(function ($controller, $rootScope, $http, $location, $timeout,
                                             $cookies, timeoutLogoutDelay) {
        timeoutLogoutDelay_ = timeoutLogoutDelay;
        mockScope = $rootScope.$new();
        mockTimeout = $timeout;
        location = $location;
        cookies = $cookies;
        username_label = "username";
        initial_username_value = "username";
        role_label = "role";
        initial_role_value = "role";
        cookies.put(username_label, initial_username_value);
        cookies.put(role_label, initial_role_value);
        controller = $controller("authCtrl", {
            $scope: mockScope,
            $http: $http,
            $timeout: mockTimeout
        });

    }));

    afterEach(function () {
        backend.verifyNoOutstandingExpectation();
        backend.verifyNoOutstandingRequest();
    });

    describe("Controller init", function () {
        it("Init performs correctly", function () {
            initial_username = mockScope.data.username;
            initial_role = mockScope.data.role;
            expect(initial_username).toEqual(initial_username_value);
            expect(initial_role).toEqual(initial_role_value);
        });
    });

    describe("Test controller functions", function () {
        it("Logout performs correctly", function () {
            mockScope.logout();
            mockTimeout.verifyNoPendingTasks();
            expect(mockScope.data).toEqual({});
            expect(cookies.get(username_label)).toBeUndefined();
            expect(cookies.get(role_label)).toBeUndefined();
            expect(location.path()).toEqual(loginUrl_);
        });

        it("Login page redirect performs correctly", function () {
            mockScope.redirectToLoginPage();
            expect(location.path()).toEqual(loginUrl_);
        });

        it("Main page redirect performs correctly", function () {
            mockScope.redirectToMainPage();
            expect(location.path()).toEqual(mainUrl_);
        });
    });

    describe("Successful Authentication", function () {
        beforeEach(angular.mock.inject(function () {
            username = "ADMIN";
            password = "ADMIN";
            backend.expect("POST", authUrl_).respond(role);
            mockScope.authenticate(username, password);
            backend.flush();
        }));

        it("Cookies contain proper values", function () {
            expect(cookies.get(username_label)).toBe(username);
            expect(cookies.get(role_label)).toBe(role);
        });

        it("Scope variables have proper values", function () {
            expect(mockScope.data.username).toBe(username);
            expect(mockScope.data.role).toBe(role);
            expect(mockScope.data.authenticationError).toBeNull();
            expect(mockScope.data.password).toBeNull();
        });

        it("Path is proper", function () {
            expect(location.path()).toEqual(mainUrl_);
        });

        it("Reaction to timeout countdown is proper", function () {
            expect(mockScope.data.timeout).toBeFalsy();
            mockTimeout.flush(timeoutLogoutDelay_);
            mockTimeout.verifyNoPendingTasks();
            expect(location.path()).toEqual(loginUrl_);
            expect(mockScope.data.timeout).toBeTruthy();
        });
    });

    describe('Erroneous authentication', function () {
        afterEach(angular.mock.inject(function () {
            expect(cookies.get(username_label)).toBeUndefined();
            expect(cookies.get(role_label)).toBeUndefined();
            expect(mockScope.data.timeout).toBeFalsy();
            expect(mockScope.data.password).toBeNull();
        }));

        it("Properly elaborates the authentication error", function () {
            errorStatus = 500;
            backend.expect("POST", authUrl_).respond(errorStatus);
            mockScope.authenticate(username, password);
            backend.flush();
            expect(mockScope.data.authenticationError.status).toBeUndefined();
            expect(mockScope.data.authenticationError.report)
                .toBe('Authentication failed. Try again.');
        });

        it("Properly elaborates the improper credentials", function () {
            backend.expect("POST", authUrl_).respond(null);
            password = "FAKE";
            mockScope.authenticate(username, password);
            backend.flush();
            error = {status: "username and/or password are incorrect"};
            expect(mockScope.data.authenticationError.status).toBe(error.status);
            expect(mockScope.data.authenticationError.report)
                .toBe('Authentication failed (' + error.status + '). Try again.');
        });
    })
});

