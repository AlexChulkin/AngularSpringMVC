/*
 * Copyright (c) 2016.  Alex Chulkin
 */

'use strict';

/**
 * The adminLoginControllers tests
 */

describe("Login Controller Test", function () {
    var mockScope;
    var controller;
    var mockLocation;
    var mockCookies;
    var mockTimeout;
    var mainUrl_, loginUrl_, authUrl_;
    var username, password, role;
    var username_label;
    var initial_username_value;
    var role_label;
    var initial_role_value;
    var timeoutLogoutDelay_;
    var backend;
    var error;
    var errorStatus;
    var mockExchangeService, mockUtilsService;

    beforeEach(function () {
        module("packetAdminApp");
    });

    beforeEach(angular.mock.inject(function ($httpBackend, mainUrl, loginUrl, authUrl) {
        mainUrl_ = mainUrl;
        loginUrl_ = loginUrl;
        authUrl_ = authUrl;
        role = "ADMIN";
        backend = $httpBackend;
        errorStatus = 400;
    }));

    beforeEach(angular.mock.inject(function ($controller, $rootScope, $http, $location, $timeout, $cookies,
                                             timeoutLogoutDelay) {
        timeoutLogoutDelay_ = timeoutLogoutDelay;
        mockScope = $rootScope.$new();
        mockTimeout = $timeout;
        mockLocation = $location;
        mockCookies = $cookies;
        username_label = "username";
        initial_username_value = "username";
        role_label = "role";
        initial_role_value = "role";
        mockCookies.put(username_label, initial_username_value);
        mockCookies.put(role_label, initial_role_value);

        mockUtilsService = {
            isUndefinedOrNull: function () {
            }
        };
        spyOn(mockUtilsService, 'isUndefinedOrNull').and.returnValue(false);

        mockExchangeService = {
            init: function () {
            }
        };
        spyOn(mockExchangeService, 'init');

        controller = $controller("loginCtrl", {
            $scope: mockScope,
            $http: $http,
            $timeout: mockTimeout,
            exchangeService: mockExchangeService,
            utilsService: mockUtilsService
        });
    }));

    afterEach(function () {
        backend.verifyNoOutstandingExpectation();
        backend.verifyNoOutstandingRequest();
    });

    describe("Test controller functions", function () {
        it("Init performs correctly", function () {
            expect(mockScope.data.username).toEqual(initial_username_value);
            expect(mockScope.data.role).toEqual(initial_role_value);
        });

        it("Logout performs correctly", function () {
            mockScope.logout();
            expect(mockExchangeService.init).toHaveBeenCalledWith();
            mockTimeout.verifyNoPendingTasks();
            expect(mockScope.data).toEqual({});
            expect(mockCookies.get(username_label)).toBeUndefined();
            expect(mockCookies.get(role_label)).toBeUndefined();
            expect(mockLocation.path()).toEqual(loginUrl_);
        });

        it("isUserAuthorized() performs correctly", function () {
            mockScope.data.authorizationIsInProcess = true;
            expect(mockScope.isUserAuthorized()).toBeFalsy();
            expect(mockUtilsService.isUndefinedOrNull).toHaveBeenCalledWith(mockScope.data.role);
            mockScope.data.authorizationIsInProcess = false;
            expect(mockScope.isUserAuthorized()).toBeTruthy();
        });

        it("Login page redirect performs correctly", function () {
            mockScope.redirectToLoginPage();
            expect(mockLocation.path()).toEqual(loginUrl_);
        });

        it("Main page redirect performs correctly", function () {
            mockScope.redirectToMainPage();
            expect(mockLocation.path()).toEqual(mainUrl_);
        });
    });

    describe("Successful Authentication", function () {
        beforeEach(function () {
            username = "ADMIN";
            password = "ADMIN";
            backend.expect("POST", authUrl_, {securityParams: {username: username, password: password}}).respond(role);
            mockScope.authenticate(username, password);
            backend.flush();
        });

        it("Cookies contain proper values", function () {
            expect(mockCookies.get(username_label)).toBe(username);
            expect(mockCookies.get(role_label)).toBe(role);
        });

        it("Scope variables have proper values", function () {
            expect(mockScope.data.username).toBe(username);
            expect(mockScope.data.role).toBe(role);
            expect(mockScope.data.authenticationError).toBeNull();
            expect(mockScope.data.password).toBeNull();
        });

        it("Path is proper", function () {
            expect(mockLocation.path()).toEqual(mainUrl_);
        });

        it("Reaction to timeout countdown is proper", function () {
            expect(mockScope.data.timeout).toBeFalsy();
            mockTimeout.flush(timeoutLogoutDelay_);
            mockTimeout.verifyNoPendingTasks();
            expect(mockLocation.path()).toEqual(loginUrl_);
            expect(mockScope.data.timeout).toBeTruthy();
        });
    });

    describe('Erroneous authentication', function () {
        afterEach(angular.mock.inject(function () {
            expect(mockCookies.get(username_label)).toBeUndefined();
            expect(mockCookies.get(role_label)).toBeUndefined();
            expect(mockScope.data.timeout).toBeFalsy();
            expect(mockScope.data.password).toBeNull();
        }));

        it("Properly elaborates the authentication error", function () {
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

