/**
 * Created by alexc_000 on 2016-10-05.
 */
describe("Packets Panel Controller Test", function () {
    var backend;
    var mockExchangeService, mockHelperService, controller, mockScope, mockCookies;
    var fakeComptsLength, fakeMinimalValue, fakeSelectedPktId, fakeLoadErrorValue,
        fakeLoadedNoComboDataValue, fakeLoadedNoStatesValue, fakePacketIsAlreadySelectedAtLeastOnceValue,
        fakeComptIdsToUpdate, fakeComptIdsToDelete, fakeAllStates, fakeAllCheckedComboData, fakeNewComptLabels,
        fakeInitialStateId, fakeAllPackets, fakeNewPackets, fakeSavedPktId, fakeDeletedPktId, forbiddenCallTriggered;
    var httpResponse;
    var httpError, errorStatus, errorStatusBadRequest_, errorStatusNotFound_, errorRequestTimeout;
    var role_;
    var saveAllChangesToBaseUrl_;
    var dataParams;
    var adminRole;
    var addPackets_, updatePackets_, updateCompts_;
    var addOrUpdatePacketsErrorPrefix_, addOrUpdatePacketsErrorSuffix_, updateComptsError_, addPacketsErrorRoot_,
        updatePacketsErrorRoot_;

    beforeEach(function () {
        module("packetAdminApp");
    });

    beforeEach(inject(function ($httpBackend, $cookies, saveAllChangesToBaseUrl, role, updatePacketsErrorRoot,
                                addPackets, updatePackets, updateCompts, addOrUpdatePacketsErrorPrefix,
                                addOrUpdatePacketsErrorSuffix, updateComptsError, addPacketsErrorRoot,
                                errorStatusBadRequest, errorStatusNotFound) {
        mockCookies = $cookies;
        backend = $httpBackend;
        saveAllChangesToBaseUrl_ = saveAllChangesToBaseUrl;
        role_ = role;
        adminRole = "ADMIN";

        errorStatusBadRequest_ = errorStatusBadRequest;
        errorStatusNotFound_ = errorStatusNotFound;
        errorRequestTimeout = 408;

        addPackets_ = addPackets;
        updatePackets_ = updatePackets;
        updateCompts_ = updateCompts;

        addOrUpdatePacketsErrorPrefix_ = addOrUpdatePacketsErrorPrefix;
        addOrUpdatePacketsErrorSuffix_ = addOrUpdatePacketsErrorSuffix;
        updateComptsError_ = updateComptsError;
        addPacketsErrorRoot_ = addPacketsErrorRoot;
        updatePacketsErrorRoot_ = updatePacketsErrorRoot;

        fakeComptsLength = 0;
        fakeMinimalValue = -1;
        fakeSelectedPktId = 1;
        fakeLoadErrorValue = false;
        fakeLoadedNoComboDataValue = false;
        fakeLoadedNoStatesValue = false;
        fakePacketIsAlreadySelectedAtLeastOnceValue = true;
        fakeSavedPktId = 1;
        fakeDeletedPktId = 2;


        mockHelperService = {
            isEmpty: function () {
            }
        };

        mockExchangeService = {
            setPacketIdToInd: function (value, packetId) {
            },
            setAllPackets: function (value, pktId) {
            },
            setMaximalPacketId: function (value) {
            },
            getMaximalPacketId: function () {
            },
            getMaximalPacketIndex: function () {
            },
            setMaximalPacketIndex: function (packetInd) {
            },
            getSelectedPacketId: function () {
            },
            setSelectedPacket: function (pkt) {
            },
            getAllCheckedComboData: function (comptId, stateId) {
            },
            getLoadedNoComboData: function () {
            },
            getLoadError: function () {
            },
            getLoadedNoStates: function () {
            },
            deleteComptIdsToDelete: function (packetId) {
            },
            deleteComptIdsToUpdate: function (packetId, comptId) {
            },
            deleteNewComptLabels: function (packetId, comptId) {
            },
            deleteNewPackets: function (packetId) {
            },
            getAllStates: function () {
            },
            getComptIdsToUpdate: function (pktId) {
            },
            getPacketInitialStateIds: function (pktId) {
            },
            getComptIdsToDelete: function (pktId) {
            },
            setNewPackets: function (newPacket, maximalPktId) {
            },
            setLoadEmpty: function (isLoadedEmpty) {
            },
            deleteAllPackets: function (pktId) {
            },
            getNewComptLabels: function (pktId) {
            }
        };
    }));

    describe("Init fn", function () {
        beforeEach(inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http)
        }));
        it("", (inject(function () {
            expect(mockScope.data).not.toBeNull();
            expect(mockScope.data.adminRoleTitle).toBeDefined();
            expect(mockScope.data.adminRoleTitle).toEqual(adminRole);
            expect(mockScope.data.isRoleNotAdmin).toBeDefined();
            expect(mockScope.data.isRoleNotAdmin).toEqual(mockCookies.get(role_) !== mockScope.data.adminRoleTitle);
        })));
    });

    describe("Ensure that saveAllChangesToBase() with no args http error response is properly elaborated: " +
        "error status = bad request",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildHttpError(errorStatusBadRequest_);
                buildDataParams_fullStuff();
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                buildSpyOnWindowService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpError);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase();
                backend.flush();

                expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith();
                expect(mockExchangeService.deleteNewPackets).toHaveBeenCalledWith();
                expect(mockExchangeService.deleteComptIdsToUpdate).toHaveBeenCalledWith();
                expect(mockExchangeService.deleteNewComptLabels).toHaveBeenCalledWith();

                dataParams.packetIdsToDelete = [];
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
                mockScope.saveAllChangesToBase();
                backend.flush();
            })));
        });

    describe("Ensure that saveAllChangesToBase() with no args http error response is properly elaborated: " +
        "error status = not found",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildHttpError(errorStatusNotFound_);
                buildDataParams_fullStuff();
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                buildSpyOnWindowService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpError);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase();
                backend.flush();

                expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith();
                expect(mockExchangeService.deleteNewPackets).toHaveBeenCalledWith();
                expect(mockExchangeService.deleteComptIdsToUpdate).toHaveBeenCalledWith();
                expect(mockExchangeService.deleteNewComptLabels).toHaveBeenCalledWith();

                dataParams.packetIdsToDelete = [];
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
                mockScope.saveAllChangesToBase();
                backend.flush();
            })));
        });

    describe("Ensure that saveAllChangesToBase() with arg = pktId http error response is properly elaborated: " +
        "error status = bad request",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildHttpError(errorStatusBadRequest_);
                buildDataParams_fullStuff(fakeSavedPktId);
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                buildSpyOnWindowService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpError);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase(fakeSavedPktId);
                backend.flush();

                expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith(fakeSavedPktId);
                expect(mockExchangeService.deleteNewPackets).toHaveBeenCalledWith(fakeSavedPktId);
                expect(mockExchangeService.deleteComptIdsToUpdate).toHaveBeenCalledWith(fakeSavedPktId);
                expect(mockExchangeService.deleteNewComptLabels).toHaveBeenCalledWith(fakeSavedPktId);
            })));
        });

    describe("Ensure that saveAllChangesToBase() with arg = pktId http error response is properly elaborated: " +
        "error status = not found",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildHttpError(errorStatusNotFound_);
                buildDataParams_fullStuff(fakeSavedPktId);
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                buildSpyOnWindowService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpError);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase(fakeSavedPktId);
                backend.flush();

                expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith(fakeSavedPktId);
                expect(mockExchangeService.deleteNewPackets).toHaveBeenCalledWith(fakeSavedPktId);
                expect(mockExchangeService.deleteComptIdsToUpdate).toHaveBeenCalledWith(fakeSavedPktId);
                expect(mockExchangeService.deleteNewComptLabels).toHaveBeenCalledWith(fakeSavedPktId);
            })));
        });

    describe("Ensure that saveAllChangesToBase() with no args http error response is properly elaborated: " +
        "error status = not found",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildHttpError(errorStatusNotFound_);
                buildDataParams_fullStuff();
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                buildSpyOnWindowService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpError);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase();
                backend.flush();

                expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith();
                expect(mockExchangeService.deleteNewPackets).toHaveBeenCalledWith();
                expect(mockExchangeService.deleteComptIdsToUpdate).toHaveBeenCalledWith();
                expect(mockExchangeService.deleteNewComptLabels).toHaveBeenCalledWith();

                dataParams.packetIdsToDelete = [];
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
                mockScope.saveAllChangesToBase();
                backend.flush();
            })));
        });

    describe("Ensure that saveAllChangesToBase() with no args http error response is properly elaborated: " +
        "error status = request timeout",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildHttpError(errorRequestTimeout);
                buildDataParams_fullStuff();
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                buildSpyOnWindowService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpError);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase();
                backend.flush();

                expect(mockExchangeService.deleteComptIdsToDelete).not.toHaveBeenCalledWith();
                expect(mockExchangeService.deleteNewPackets).not.toHaveBeenCalledWith();
                expect(mockExchangeService.deleteComptIdsToUpdate).not.toHaveBeenCalledWith();
                expect(mockExchangeService.deleteNewComptLabels).not.toHaveBeenCalledWith();

                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
                mockScope.saveAllChangesToBase();
                backend.flush();
            })));
        });

    describe("Ensure that saveAllChangesToBase() with arg = pktId http error response is properly elaborated: " +
        "error status = request timeout",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildHttpError(errorRequestTimeout);
                buildDataParams_fullStuff(fakeSavedPktId);
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                buildSpyOnWindowService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpError);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase(fakeSavedPktId);
                backend.flush();

                expect(mockExchangeService.deleteComptIdsToDelete).not.toHaveBeenCalledWith(fakeSavedPktId);
                expect(mockExchangeService.deleteNewPackets).not.toHaveBeenCalledWith(fakeSavedPktId);
                expect(mockExchangeService.deleteComptIdsToUpdate).not.toHaveBeenCalledWith(fakeSavedPktId);
                expect(mockExchangeService.deleteNewComptLabels).not.toHaveBeenCalledWith(fakeSavedPktId);

                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
                mockScope.saveAllChangesToBase(fakeSavedPktId);
                backend.flush();
            })));
        });

    describe("Ensure that saveAllChangesToBase() with arg = pktId" +
        "  http response is properly elaborated: add Packets error",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildDataParams_fullStuff(fakeSavedPktId);
                buildResponse_error_addPackets();
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                buildSpyOnWindowService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase(fakeSavedPktId);
                backend.flush();
                expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith(fakeSavedPktId);
                expect(window.alert).toHaveBeenCalledWith(addOrUpdatePacketsErrorPrefix_ + addPacketsErrorRoot_
                    + addOrUpdatePacketsErrorSuffix_);
                expect(mockExchangeService.deleteNewPackets).not.toHaveBeenCalledWith();
            })));
        });

    describe("Ensure that saveAllChangesToBase() w/out args http response is properly elaborated: update Packets error",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildDataParams_fullStuff();
                buildResponse_error_updatePackets();
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                buildSpyOnWindowService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase();
                backend.flush();
                dataParams.packetIdsToDelete = [];
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
                mockScope.saveAllChangesToBase();
                backend.flush();
                expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith();
                expect(window.alert).toHaveBeenCalledWith(addOrUpdatePacketsErrorPrefix_ + updatePacketsErrorRoot_
                    + addOrUpdatePacketsErrorSuffix_);
                expect(mockExchangeService.deleteNewComptLabels).not.toHaveBeenCalledWith();
            })));
        });

    describe("Ensure that saveAllChangesToBase() with arg = pktId" +
        " http response is properly elaborated: update Packets error",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildDataParams_fullStuff(fakeSavedPktId);
                buildResponse_error_updatePackets();
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                buildSpyOnWindowService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase(fakeSavedPktId);
                backend.flush();
                expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith(fakeSavedPktId);
                expect(window.alert).toHaveBeenCalledWith(addOrUpdatePacketsErrorPrefix_ + updatePacketsErrorRoot_
                    + addOrUpdatePacketsErrorSuffix_);
                expect(mockExchangeService.deleteNewComptLabels).not.toHaveBeenCalledWith();
            })));
        });

    describe("Ensure that saveAllChangesToBase() w/out args http response is properly elaborated: update compts error",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildDataParams_fullStuff();
                buildResponse_error_updateCompts();
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                buildSpyOnWindowService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase();
                backend.flush();
                expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith();
                expect(window.alert).toHaveBeenCalledWith(updateComptsError_);
                expect(mockExchangeService.deleteComptIdsToUpdate).not.toHaveBeenCalledWith();
                dataParams.packetIdsToDelete = [];
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
                mockScope.saveAllChangesToBase();
                backend.flush();

            })));
        });

    describe("Ensure that saveAllChangesToBase() with arg = pktId" +
        " http response is properly elaborated: update compts error",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildDataParams_fullStuff(fakeSavedPktId);
                buildResponse_error_updateCompts();
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                buildSpyOnWindowService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase(fakeSavedPktId);
                backend.flush();
                expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith(fakeSavedPktId);
                expect(window.alert).toHaveBeenCalledWith(updateComptsError_);
                expect(mockExchangeService.deleteComptIdsToUpdate).not.toHaveBeenCalledWith();
            })));
        });

    describe("Ensure that saveAllChangesToBase() w/out args http response is properly elaborated: no errors",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildDataParams_fullStuff();
                buildResponse_success();
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                buildSpyOnWindowService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase();
                backend.flush();
                dataParams.packetIdsToDelete = [];
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
                mockScope.saveAllChangesToBase();
                backend.flush();
                expect(mockExchangeService.deleteNewPackets).toHaveBeenCalledWith();
                expect(mockExchangeService.deleteComptIdsToUpdate).toHaveBeenCalledWith();
                expect(mockExchangeService.deleteNewComptLabels).toHaveBeenCalledWith();
                expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith();
            })));
        });

    describe("Ensure that saveAllChangesToBase() with arg = pktId http response is properly elaborated: no errors",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildDataParams_fullStuff(fakeSavedPktId);
                buildResponse_success();
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                buildSpyOnWindowService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase(fakeSavedPktId);
                backend.flush();
                expect(mockExchangeService.deleteNewPackets).toHaveBeenCalledWith(fakeSavedPktId);
                expect(mockExchangeService.deleteComptIdsToUpdate).toHaveBeenCalledWith(fakeSavedPktId);
                expect(mockExchangeService.deleteNewComptLabels).toHaveBeenCalledWith(fakeSavedPktId);
                expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith(fakeSavedPktId);
            })));
        });

    describe("Ensure that saveAllChangesToBase()'s http response is properly elaborated: all errors possible:" +
        "add/upd/del packets, upd/del compts",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildDataParams_fullStuff();
                buildResponse_allErrors();
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                buildSpyOnWindowService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase();
                backend.flush();
                expect(window.alert).toHaveBeenCalledWith(addOrUpdatePacketsErrorPrefix_ + addPacketsErrorRoot_
                    + addOrUpdatePacketsErrorSuffix_);
                expect(window.alert).toHaveBeenCalledWith(addOrUpdatePacketsErrorPrefix_ + updatePacketsErrorRoot_
                    + addOrUpdatePacketsErrorSuffix_);
                expect(window.alert).toHaveBeenCalledWith(updateComptsError_);
                expect(mockExchangeService.deleteNewPackets).not.toHaveBeenCalledWith();
                expect(mockExchangeService.deleteComptIdsToUpdate).not.toHaveBeenCalledWith();
                expect(mockExchangeService.deleteNewComptLabels).not.toHaveBeenCalledWith();
                expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith();
                dataParams.packetIdsToDelete = [];
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
                mockScope.saveAllChangesToBase();
                backend.flush();
            })));
        });

    describe("Ensure that saveAllChangesToBase()'s http response is properly elaborated: all errors possible:" +
        "add/upd/del packets, upd/del compts",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildDataParams_fullStuff(fakeSavedPktId);
                buildResponse_allErrors();
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                buildSpyOnWindowService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase(fakeSavedPktId);
                backend.flush();
                expect(window.alert).toHaveBeenCalledWith(addOrUpdatePacketsErrorPrefix_ + addPacketsErrorRoot_
                    + addOrUpdatePacketsErrorSuffix_);
                expect(window.alert).toHaveBeenCalledWith(addOrUpdatePacketsErrorPrefix_ + updatePacketsErrorRoot_
                    + addOrUpdatePacketsErrorSuffix_);
                expect(window.alert).toHaveBeenCalledWith(updateComptsError_);
                expect(mockExchangeService.deleteNewPackets).not.toHaveBeenCalledWith(fakeSavedPktId);
                expect(mockExchangeService.deleteComptIdsToUpdate).not.toHaveBeenCalledWith(fakeSavedPktId);
                expect(mockExchangeService.deleteNewComptLabels).not.toHaveBeenCalledWith(fakeSavedPktId);
                expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith(fakeSavedPktId);
            })));
        });

    describe("Ensure that saveAllChangesToBase() with no args builds the data params properly with full stuff:" +
        " pkt add/upd/del, compt upd/del",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildDataParams_fullStuff(fakeSavedPktId);
                buildResponse_allErrors();
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                buildSpyOnWindowService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase(fakeSavedPktId);
                backend.flush();
                expect(window.alert).toHaveBeenCalledWith(addOrUpdatePacketsErrorPrefix_ + addPacketsErrorRoot_
                    + addOrUpdatePacketsErrorSuffix_);
                expect(window.alert).toHaveBeenCalledWith(addOrUpdatePacketsErrorPrefix_ + updatePacketsErrorRoot_
                    + addOrUpdatePacketsErrorSuffix_);
                expect(window.alert).toHaveBeenCalledWith(updateComptsError_);
                expect(mockExchangeService.deleteNewPackets).not.toHaveBeenCalledWith(fakeSavedPktId);
                expect(mockExchangeService.deleteComptIdsToUpdate).not.toHaveBeenCalledWith(fakeSavedPktId);
                expect(mockExchangeService.deleteNewComptLabels).not.toHaveBeenCalledWith(fakeSavedPktId);
                expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith(fakeSavedPktId);
            })));
        });

    describe("Ensure that saveAllChangesToBase() with arg = pktId builds the data params properly with full stuff:" +
        " pkt add/upd/del, compt upd/del",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildDataParams_fullStuff(fakeSavedPktId);
                buildResponse_success();
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase(fakeSavedPktId);
                backend.flush();
                expect(mockExchangeService.deleteNewPackets).not.toHaveBeenCalledWith();
                expect(mockExchangeService.deleteComptIdsToUpdate).not.toHaveBeenCalledWith();
                expect(mockExchangeService.deleteNewComptLabels).not.toHaveBeenCalledWith();
                expect(mockExchangeService.deleteComptIdsToDelete).not.toHaveBeenCalledWith();
            })));
        });

    describe("Ensure that saveAllChangesToBase() with no args builds the data params properly with full stuff:" +
        " pkt add/upd/del, compt upd/del",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildDataParams_fullStuff();
                buildResponse_success();
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase();
                backend.flush();
            })));
        });

    describe("Ensure that saveAllChangesToBase() with no args builds data params properly" +
        " w/out compts delete", function () {
        beforeEach(inject(function () {
            buildSpiesReturnValues_woutDelCompts();
            buildDataParams_woutDelCompts();
            buildResponse_success();
            buildSpiesOnMockHelperService(true);
            buildSpiesOnMockExchangeService();
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
        }));

        it("", (inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildMockScopeData();
            mockScope.deletePacketLocally(fakeDeletedPktId);
            mockScope.saveAllChangesToBase();
            backend.flush();
        })));
    });

    describe("Ensure that saveAllChangesToBase() with arg = pktId builds data params properly " +
        "w/out compts delete", function () {
        beforeEach(inject(function () {
            buildSpiesReturnValues_woutDelCompts();
            buildDataParams_woutDelCompts(fakeSavedPktId);
            buildResponse_success();
            buildSpiesOnMockHelperService(true);
            buildSpiesOnMockExchangeService();
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
        }));

        it("", (inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildMockScopeData();
            mockScope.deletePacketLocally(fakeDeletedPktId);
            mockScope.saveAllChangesToBase(fakeSavedPktId);
            backend.flush();
        })));
    });

    describe("Ensure that saveAllChangesToBase() builds data params properly w/out compts update", function () {
        beforeEach(inject(function () {
            buildSpiesReturnValues_woutUpdCompts();
            buildDataParams_woutUpdCompts();
            buildResponse_success();
            buildSpiesOnMockHelperService(false);
            buildSpiesOnMockExchangeService();
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
        }));
        it("", inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildMockScopeData();
            mockScope.deletePacketLocally(2);
            mockScope.saveAllChangesToBase();
            backend.flush();
        }))
    });

    describe("Ensure that saveAllChangesToBase() builds data params properly " +
        "w/out adding packets and compts", function () {
        beforeEach(inject(function () {
            buildSpiesReturnValues_woutAddPktsAndAddCompts();
            buildDataParams_woutAddPktsAndAddCompts();
            buildResponse_success();
            buildSpiesOnMockHelperService(false);
            buildSpiesOnMockExchangeService();
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
        }));
        it("", inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildMockScopeData();
            mockScope.deletePacketLocally(2);
            mockScope.saveAllChangesToBase();
            backend.flush();
        }))
    });

    describe("Ensure that saveAllChangesToBase() w/out args builds data params properly " +
        "w/out updating the packet and with new packet being empty", function () {
        beforeEach(inject(function () {
            buildSpiesReturnValues_woutUpdPktAndWithEmptyNewPkt();
            buildDataParams_woutUpdPktAndWithEmptyNewPkt();
            buildResponse_success();
            buildSpiesOnMockHelperService(false);
            buildSpiesOnMockExchangeService();
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
        }));
        it("", inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildMockScopeData();
            mockScope.deletePacketLocally(2);
            mockScope.saveAllChangesToBase();
            backend.flush();
        }))
    });

    describe("Ensure that saveAllChangesToBase() with arg = pktId builds data params properly " +
        "w/out updating the packet and with new packet being empty", function () {
        beforeEach(inject(function () {
            buildSpiesReturnValues_woutUpdPktAndWithEmptyNewPkt();
            buildDataParams_woutUpdPktAndWithEmptyNewPkt(fakeSavedPktId);
            buildResponse_success();
            buildSpiesOnMockHelperService(false);
            buildSpiesOnMockExchangeService();
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
        }));
        it("", inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildMockScopeData();
            mockScope.deletePacketLocally(fakeDeletedPktId);
            mockScope.saveAllChangesToBase(fakeSavedPktId);
            backend.flush();
        }))
    });

    describe("Ensure that saveAllChangesToBase() w/out args builds data params properly " +
        "w/out updating the packet state", function () {
        beforeEach(inject(function () {
            buildSpiesReturnValues_woutUpdPktState();
            buildDataParams_woutUpdPktState();
            buildResponse_success();
            buildSpiesOnMockHelperService(false);
            buildSpiesOnMockExchangeService();
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
        }));
        it("", inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildMockScopeData();
            mockScope.deletePacketLocally(fakeDeletedPktId);
            mockScope.saveAllChangesToBase();
            backend.flush();
        }))
    });

    describe("Ensure that saveAllChangesToBase() with arg = pktId builds data params properly " +
        "w/out updating the packet state", function () {
        beforeEach(inject(function () {
            buildSpiesReturnValues_woutUpdPktState();
            buildDataParams_woutUpdPktState(fakeSavedPktId);
            buildResponse_success();
            buildSpiesOnMockHelperService(false);
            buildSpiesOnMockExchangeService();
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
        }));
        it("", inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildMockScopeData();
            mockScope.deletePacketLocally(fakeDeletedPktId);
            mockScope.saveAllChangesToBase(fakeSavedPktId);
            backend.flush();
        }))
    });

    describe("Ensure that saveAllChangesToBase() w/out args builds data params properly " +
        "w/out packets removal", function () {
        beforeEach(inject(function () {
            buildSpiesReturnValues_woutDelPkts();
            buildDataParams_woutDelPkts();
            buildResponse_success();
            buildSpiesOnMockHelperService(false);
            buildSpiesOnMockExchangeService();
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
        }));
        it("", inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildMockScopeData();
            mockScope.saveAllChangesToBase();
            backend.flush();
        }))
    });

    describe("Ensure that saveAllChangesToBase() with arg = pktId builds data params properly " +
        "w/out packets removal", function () {
        beforeEach(inject(function () {
            buildSpiesReturnValues_woutDelPkts();
            buildDataParams_woutDelPkts(fakeSavedPktId);
            buildResponse_success();
            buildSpiesOnMockHelperService(false);
            buildSpiesOnMockExchangeService();
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
        }));
        it("", inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildMockScopeData(fakeSavedPktId);
            mockScope.saveAllChangesToBase(fakeSavedPktId);
            backend.flush();
        }))
    });

    describe("Ensure that saveAllChangesToBase() w/out args builds data params properly " +
        "w/out packets removal", function () {
        beforeEach(inject(function () {
            buildSpiesReturnValues_woutDelPkts();
            buildDataParams_woutDelPkts();
            buildResponse_success();
            buildSpiesOnMockHelperService(false);
            buildSpiesOnMockExchangeService();
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
        }));
        it("", inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildMockScopeData();
            mockScope.saveAllChangesToBase();
            backend.flush();
        }))
    });

    describe("Ensure that saveAllChangesToBase() with arg = pktId builds data params properly " +
        "w/out packets removal", function () {
        beforeEach(inject(function () {
            buildSpiesReturnValues_woutDelPkts();
            buildDataParams_woutDelPkts(fakeSavedPktId);
            buildResponse_success();
            buildSpiesOnMockHelperService(false);
            buildSpiesOnMockExchangeService();
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
        }));
        it("", inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildMockScopeData();
            mockScope.saveAllChangesToBase(fakeSavedPktId);
            backend.flush();
        }))
    });

    describe("Ensure that saveAllChangesToBase() with no args builds data params properly " +
        "w/out upd/del compts as well as w/out add/upd/del packets", function () {
        beforeEach(inject(function () {
            buildSpiesReturnValues_woutEverything();
            buildSpiesOnMockHelperService(false);
            buildSpiesOnMockExchangeService();
            backend.when(saveAllChangesToBaseUrl_).respond(function () {
                forbiddenCallTriggered = true;
            });
        }));
        it("", inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildMockScopeData();
            mockScope.saveAllChangesToBase();
            expect(forbiddenCallTriggered).toBeFalsy();
        }))
    });

    describe("Ensure that saveAllChangesToBase() with arg = pktId builds data params properly " +
        "w/out upd/del compts as well as w/out add/upd/del packets", function () {
        beforeEach(inject(function () {
            buildSpiesReturnValues_woutEverything();
            buildSpiesOnMockHelperService(false);
            buildSpiesOnMockExchangeService();
            backend.when(saveAllChangesToBaseUrl_).respond(function () {
                forbiddenCallTriggered = true;
            });
        }));
        it("", inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildMockScopeData();
            mockScope.saveAllChangesToBase(fakeSavedPktId);
            expect(forbiddenCallTriggered).toBeFalsy();
        }))
    });

    var buildSpiesOnMockExchangeService = function () {
        spyOn(mockExchangeService, 'setPacketIdToInd');
        spyOn(mockExchangeService, 'setAllPackets');
        spyOn(mockExchangeService, 'getMaximalPacketId').and.returnValue(fakeMinimalValue);
        spyOn(mockExchangeService, 'setMaximalPacketIndex');
        spyOn(mockExchangeService, 'getMaximalPacketIndex').and.returnValue(fakeMinimalValue);
        spyOn(mockExchangeService, 'getSelectedPacketId').and.returnValue(fakeSelectedPktId);
        spyOn(mockExchangeService, 'setSelectedPacket');
        spyOn(mockExchangeService, 'deleteComptIdsToDelete');
        spyOn(mockExchangeService, 'deleteComptIdsToUpdate');
        spyOn(mockExchangeService, 'deleteNewComptLabels');
        spyOn(mockExchangeService, 'deleteNewPackets');
        spyOn(mockExchangeService, 'deleteAllPackets');
        spyOn(mockExchangeService, 'getLoadError').and.returnValue(fakeLoadErrorValue);
        spyOn(mockExchangeService, 'getLoadedNoComboData').and.returnValue(fakeLoadedNoComboDataValue);
        spyOn(mockExchangeService, 'getLoadedNoStates').and.returnValue(fakeLoadedNoStatesValue);
        spyOn(mockExchangeService, 'setLoadEmpty');
        spyOn(mockExchangeService, 'setNewPackets');
        spyOn(mockExchangeService, 'getAllCheckedComboData').and.returnValue(fakeAllCheckedComboData);
        spyOn(mockExchangeService, 'getPacketInitialStateIds').and.returnValue(fakeInitialStateId);
        spyOn(mockExchangeService, 'getComptIdsToDelete').and.returnValue(fakeComptIdsToDelete);
        spyOn(mockExchangeService, 'getComptIdsToUpdate').and.returnValue(fakeComptIdsToUpdate);
        spyOn(mockExchangeService, 'getAllStates').and.returnValue(fakeAllStates);
        spyOn(mockExchangeService, 'getNewComptLabels').and.returnValue(fakeNewComptLabels);
    };

    var buildSpiesReturnValues_fullStuff = function () {
        fakeComptIdsToDelete = [1, 2];
        fakeComptIdsToUpdate = {"3": "true", "4": "true"};
        fakeAllStates = [{id: "1", label: "state1"}, {id: "2", label: "state2"}];
        fakeAllCheckedComboData = "cd1";
        fakeNewComptLabels = {"5": "lbl5", "6": "lbl6"};
        fakeInitialStateId = 2;
        fakeAllPackets = {"1": {id: "1", stateId: "1"}, "2": {id: "2", stateId: "2"}, "3": {id: "3", stateId: "2"}};
        fakeNewPackets = {"3": {id: "3", stateId: "2"}};
    };

    var buildHttpError = function (status) {
        httpError = status;
    };

    var buildSpiesReturnValues_woutDelCompts = function () {
        fakeComptIdsToDelete = [];
        fakeComptIdsToUpdate = {"3": "true", "4": "true"};
        fakeAllStates = [{id: "1", label: "state1"}, {id: "2", label: "state2"}];
        fakeAllCheckedComboData = "cd1";
        fakeNewComptLabels = {"5": "lbl5", "6": "lbl6"};
        fakeInitialStateId = 2;
        fakeAllPackets = {"1": {id: "1", stateId: "1"}, "2": {id: "2", stateId: "2"}, "3": {id: "3", stateId: "2"}};
        fakeNewPackets = {"3": {id: "3", stateId: "2"}};
    };

    var buildSpiesReturnValues_woutUpdCompts = function () {
        fakeComptIdsToDelete = [1, 2];
        fakeComptIdsToUpdate = {};
        fakeAllStates = [{id: 1, label: "state1"}, {id: 2, label: "state2"}];
        fakeAllCheckedComboData = "cd1";
        fakeNewComptLabels = {"5": "lbl5", "6": "lbl6"};
        fakeInitialStateId = 2;
        fakeAllPackets = {"1": {id: 1, stateId: 1}, "2": {id: 2, stateId: 2}, "3": {id: 3, stateId: 2}};
        fakeNewPackets = {"3": {id: 3, stateId: 2}};
    };

    var buildSpiesReturnValues_woutAddPktsAndAddCompts = function () {
        fakeComptIdsToDelete = [1, 2];
        fakeComptIdsToUpdate = {"3": "true", "4": "true"};
        fakeAllStates = [{id: 1, label: "state1"}, {id: 2, label: "state2"}];
        fakeAllCheckedComboData = "cd1";
        fakeNewComptLabels = {};
        fakeInitialStateId = 2;
        fakeAllPackets = {"1": {id: 1, stateId: 1}, "2": {id: 2, stateId: 2}};
        fakeNewPackets = {};
    };

    var buildSpiesReturnValues_woutUpdPktState = function () {
        fakeComptIdsToDelete = [1, 2];
        fakeComptIdsToUpdate = {"3": "true", "4": "true"};
        fakeAllStates = [{id: 1, label: "state1"}, {id: 2, label: "state2"}];
        fakeAllCheckedComboData = "cd1";
        fakeNewComptLabels = {"5": "lbl5", "6": "lbl6"};
        fakeInitialStateId = 1;
        fakeAllPackets = {"1": {id: 1, stateId: 1}, "2": {id: 2, stateId: 1}, "3": {id: 3, stateId: 1}};
        fakeNewPackets = {"3": {id: 3, stateId: 2}};
    };

    var buildSpiesReturnValues_woutUpdPktAndWithEmptyNewPkt = function () {
        fakeComptIdsToDelete = [1, 2];
        fakeComptIdsToUpdate = {};
        fakeAllStates = [{id: 1, label: "state1"}, {id: 2, label: "state2"}];
        fakeAllCheckedComboData = "cd1";
        fakeNewComptLabels = {};
        fakeInitialStateId = 1;
        fakeAllPackets = {"1": {id: 1, stateId: 1}, "2": {id: 2, stateId: 1}, "3": {id: 3, stateId: 1}};
        fakeNewPackets = {"3": {id: 3, stateId: 2}};
    };

    var buildSpiesReturnValues_woutDelPkts = function () {
        fakeComptIdsToDelete = [1, 2];
        fakeComptIdsToUpdate = {"3": "true", "4": "true"};
        fakeAllStates = [{id: 1, label: "state1"}, {id: 2, label: "state2"}];
        fakeAllCheckedComboData = "cd1";
        fakeNewComptLabels = {"5": "lbl5", "6": "lbl6"};
        fakeInitialStateId = 2;
        fakeAllPackets = {"1": {id: 1, stateId: 1}, "2": {id: 2, stateId: 2}, "3": {id: 3, stateId: 2}};
        fakeNewPackets = {"3": {id: 3, stateId: 2}};
    };

    var buildSpiesReturnValues_woutEverything = function () {
        fakeComptIdsToDelete = [];
        fakeComptIdsToUpdate = {};
        fakeAllStates = [{id: 1, label: "state1"}, {id: 2, label: "state2"}];
        fakeAllCheckedComboData = "cd1";
        fakeNewComptLabels = {};
        fakeInitialStateId = 1;
        fakeAllPackets = {"1": {id: 1, stateId: 1}, "2": {id: 2, stateId: 1}};
        fakeNewPackets = {};
    };

    var buildResponse_success = function () {
        httpResponse = {"ADD_PACKETS": "false", "UPDATE_PACKETS": "false", "UPDATE_COMPTS": "false"};
    };

    var buildResponse_error_addPackets = function () {
        httpResponse = {"ADD_PACKETS": "true", "UPDATE_PACKETS": "false", "UPDATE_COMPTS": "false"};
    };

    var buildResponse_error_updatePackets = function () {
        httpResponse = {"ADD_PACKETS": "false", "UPDATE_PACKETS": "true", "UPDATE_COMPTS": "false"};
    };

    var buildResponse_error_updateCompts = function () {
        httpResponse = {"ADD_PACKETS": "false", "UPDATE_PACKETS": "false", "UPDATE_COMPTS": "true"};
    };

    var buildResponse_allErrors = function () {
        httpResponse = {'ADD_PACKETS': "true", "UPDATE_PACKETS": "true", "UPDATE_COMPTS": "true"};
    };

    var buildDataParams_fullStuff = function (savedPktId) {
        var packetsToAddParamsList = [];
        if (!savedPktId) {
            packetsToAddParamsList.push({
                newComptParamsList: [{label: "lbl5", vals: ["cd1", "cd1"]},
                    {label: "lbl6", vals: ["cd1", "cd1"]}], stateId: "2"
            });
        }

        var packetsToUpdateParamsList = [];
        packetsToUpdateParamsList.push({
            newComptParamsList: [{label: "lbl5", vals: ["cd1", "cd1"]},
                {label: "lbl6", vals: ["cd1", "cd1"]}], id: "1", stateId: "1"
        });

        if (!savedPktId) {
            packetsToUpdateParamsList.push({
                newComptParamsList: [{label: "lbl5", vals: ["cd1", "cd1"]},
                    {label: "lbl6", vals: ["cd1", "cd1"]}], id: "2"
            });
        }

        var comptsToUpdateParamsList = [];
        comptsToUpdateParamsList.push({id: "3", vals: ["cd1", "cd1"]});
        comptsToUpdateParamsList.push({id: "4", vals: ["cd1", "cd1"]});
        if (!savedPktId) {
            comptsToUpdateParamsList.push({id: "3", vals: ["cd1", "cd1"]});
            comptsToUpdateParamsList.push({id: "4", vals: ["cd1", "cd1"]});
        }

        var comptIdsToDelete;
        if (savedPktId) {
            comptIdsToDelete = [1, 2];
        } else {
            comptIdsToDelete = [1, 2, 1, 2, 1, 2]
        }

        var packetIdsToDelete = [2];

        dataParams = {
            packetsToAddParamsList: packetsToAddParamsList,
            packetsToUpdateParamsList: packetsToUpdateParamsList,
            comptsToUpdateParamsList: comptsToUpdateParamsList,
            comptIdsToDelete: comptIdsToDelete
        };
        if (angular.isUndefined(savedPktId)) {
            dataParams['packetIdsToDelete'] = packetIdsToDelete;
        } else {
            dataParams['packetId'] = savedPktId;
        }
    };

    var buildDataParams_woutDelCompts = function (savedPktId) {
        var packetsToAddParamsList = [];
        if (!savedPktId) {
            packetsToAddParamsList.push({
                newComptParamsList: [{label: "lbl5", vals: ["cd1", "cd1"]},
                    {label: "lbl6", vals: ["cd1", "cd1"]}], stateId: "2"
            });
        }

        var packetsToUpdateParamsList = [];
        packetsToUpdateParamsList.push({
            newComptParamsList: [{label: "lbl5", vals: ["cd1", "cd1"]},
                {label: "lbl6", vals: ["cd1", "cd1"]}], id: "1", stateId: "1"
        });
        if (!savedPktId) {
            packetsToUpdateParamsList.push({
                newComptParamsList: [{label: "lbl5", vals: ["cd1", "cd1"]},
                    {label: "lbl6", vals: ["cd1", "cd1"]}], id: "2"
            });
        }

        var comptsToUpdateParamsList = [];
        comptsToUpdateParamsList.push({id: "3", vals: ["cd1", "cd1"]});
        comptsToUpdateParamsList.push({id: "4", vals: ["cd1", "cd1"]});
        if (!savedPktId) {
            comptsToUpdateParamsList.push({id: "3", vals: ["cd1", "cd1"]});
            comptsToUpdateParamsList.push({id: "4", vals: ["cd1", "cd1"]});
        }
        var comptIdsToDelete = [];

        var packetIdsToDelete = [2];

        dataParams = {
            packetsToAddParamsList: packetsToAddParamsList,
            packetsToUpdateParamsList: packetsToUpdateParamsList,
            comptsToUpdateParamsList: comptsToUpdateParamsList,
            comptIdsToDelete: comptIdsToDelete
        };
        if (angular.isUndefined(savedPktId)) {
            dataParams['packetIdsToDelete'] = packetIdsToDelete;
        } else {
            dataParams['packetId'] = savedPktId;
        }
    };

    var buildDataParams_woutUpdCompts = function (savedPktId) {
        var packetsToAddParamsList = [];
        packetsToAddParamsList.push({
            newComptParamsList: [{label: "lbl5", vals: ["cd1", "cd1"]},
                {label: "lbl6", vals: ["cd1", "cd1"]}], stateId: "2"
        });

        var packetsToUpdateParamsList = [];
        packetsToUpdateParamsList.push({
            newComptParamsList: [{label: "lbl5", vals: ["cd1", "cd1"]},
                {label: "lbl6", vals: ["cd1", "cd1"]}], id: "1", stateId: "1"
        });
        packetsToUpdateParamsList.push({
            newComptParamsList: [{label: "lbl5", vals: ["cd1", "cd1"]},
                {label: "lbl6", vals: ["cd1", "cd1"]}], id: "2"
        });

        var comptsToUpdateParamsList = [];

        var comptIdsToDelete = [1, 2, 1, 2, 1, 2];

        var packetIdsToDelete = [2];

        dataParams = {
            packetsToAddParamsList: packetsToAddParamsList,
            packetsToUpdateParamsList: packetsToUpdateParamsList,
            comptsToUpdateParamsList: comptsToUpdateParamsList,
            comptIdsToDelete: comptIdsToDelete
        };
        if (angular.isUndefined(savedPktId)) {
            dataParams['packetIdsToDelete'] = packetIdsToDelete;
        } else {
            dataParams['packetId'] = savedPktId;
        }
    };

    var buildDataParams_woutAddPktsAndAddCompts = function (savedPktId) {
        var packetsToAddParamsList = [];

        var packetsToUpdateParamsList = [];
        packetsToUpdateParamsList.push({newComptParamsList: [], id: "1", stateId: "1"});

        var comptsToUpdateParamsList = [];
        comptsToUpdateParamsList.push({id: "3", vals: ["cd1", "cd1"]});
        comptsToUpdateParamsList.push({id: "4", vals: ["cd1", "cd1"]});
        comptsToUpdateParamsList.push({id: "3", vals: ["cd1", "cd1"]});
        comptsToUpdateParamsList.push({id: "4", vals: ["cd1", "cd1"]});

        var comptIdsToDelete = [1, 2, 1, 2];

        var packetIdsToDelete = [2];

        dataParams = {
            packetsToAddParamsList: packetsToAddParamsList,
            packetsToUpdateParamsList: packetsToUpdateParamsList,
            comptsToUpdateParamsList: comptsToUpdateParamsList,
            comptIdsToDelete: comptIdsToDelete
        };
        if (angular.isUndefined(savedPktId)) {
            dataParams['packetIdsToDelete'] = packetIdsToDelete;
        } else {
            dataParams['packetId'] = savedPktId;
        }
    };

    var buildDataParams_woutUpdPktState = function (savedPktId) {
        var packetsToAddParamsList = [];
        if (!savedPktId) {
            packetsToAddParamsList.push({
                newComptParamsList: [{label: "lbl5", vals: ["cd1", "cd1"]},
                    {label: "lbl6", vals: ["cd1", "cd1"]}], stateId: "1"
            });
        }

        var packetsToUpdateParamsList = [];
        packetsToUpdateParamsList.push({
            newComptParamsList: [{label: "lbl5", vals: ["cd1", "cd1"]},
                {label: "lbl6", vals: ["cd1", "cd1"]}], id: "1"
        });

        if (!savedPktId) {
            packetsToUpdateParamsList.push({
                newComptParamsList: [{label: "lbl5", vals: ["cd1", "cd1"]},
                    {label: "lbl6", vals: ["cd1", "cd1"]}], id: "2"
            });
        }

        var comptsToUpdateParamsList = [];
        comptsToUpdateParamsList.push({id: "3", vals: ["cd1", "cd1"]});
        comptsToUpdateParamsList.push({id: "4", vals: ["cd1", "cd1"]});
        if (!savedPktId) {
            comptsToUpdateParamsList.push({id: "3", vals: ["cd1", "cd1"]});
            comptsToUpdateParamsList.push({id: "4", vals: ["cd1", "cd1"]});
        }

        var comptIdsToDelete;
        if (savedPktId) {
            comptIdsToDelete = [1, 2];
        } else {
            comptIdsToDelete = [1, 2, 1, 2, 1, 2]
        }

        var packetIdsToDelete = [2];

        dataParams = {
            packetsToAddParamsList: packetsToAddParamsList,
            packetsToUpdateParamsList: packetsToUpdateParamsList,
            comptsToUpdateParamsList: comptsToUpdateParamsList,
            comptIdsToDelete: comptIdsToDelete
        };
        if (angular.isUndefined(savedPktId)) {
            dataParams['packetIdsToDelete'] = packetIdsToDelete;
        } else {
            dataParams['packetId'] = savedPktId;
        }
    };

    var buildDataParams_woutUpdPktAndWithEmptyNewPkt = function (savedPktId) {
        var packetsToAddParamsList = [];
        if (!savedPktId) {
            packetsToAddParamsList.push({newComptParamsList: [], stateId: "1"});
        }

        var packetsToUpdateParamsList = [];

        var comptsToUpdateParamsList = [];

        var comptIdsToDelete;
        if (!savedPktId) {
            comptIdsToDelete = [1, 2, 1, 2, 1, 2];
        } else {
            comptIdsToDelete = [1, 2];
        }

        var packetIdsToDelete = [2];

        dataParams = {
            packetsToAddParamsList: packetsToAddParamsList,
            packetsToUpdateParamsList: packetsToUpdateParamsList,
            comptsToUpdateParamsList: comptsToUpdateParamsList,
            comptIdsToDelete: comptIdsToDelete
        };
        if (angular.isUndefined(savedPktId)) {
            dataParams['packetIdsToDelete'] = packetIdsToDelete;
        } else {
            dataParams['packetId'] = savedPktId;
        }
    };

    var buildDataParams_woutDelPkts = function (savedPktId) {
        var packetsToAddParamsList = [];
        if (!savedPktId) {
            packetsToAddParamsList.push({
                newComptParamsList: [{label: "lbl5", vals: ["cd1", "cd1"]},
                    {label: "lbl6", vals: ["cd1", "cd1"]}], stateId: "2"
            });
        }

        var packetsToUpdateParamsList = [];
        packetsToUpdateParamsList.push({
            newComptParamsList: [{label: "lbl5", vals: ["cd1", "cd1"]},
                {label: "lbl6", vals: ["cd1", "cd1"]}], id: "1", stateId: "1"
        });
        if (!savedPktId) {
            packetsToUpdateParamsList.push({
                newComptParamsList: [{label: "lbl5", vals: ["cd1", "cd1"]},
                    {label: "lbl6", vals: ["cd1", "cd1"]}], id: "2"
            });
        }

        var comptsToUpdateParamsList = [];
        comptsToUpdateParamsList.push({id: "3", vals: ["cd1", "cd1"]});
        comptsToUpdateParamsList.push({id: "4", vals: ["cd1", "cd1"]});
        if (!savedPktId) {
            comptsToUpdateParamsList.push({id: "3", vals: ["cd1", "cd1"]});
            comptsToUpdateParamsList.push({id: "4", vals: ["cd1", "cd1"]});
        }

        var comptIdsToDelete;

        if (!savedPktId) {
            comptIdsToDelete = [1, 2, 1, 2, 1, 2];
        } else {
            comptIdsToDelete = [1, 2];
        }

        var packetIdsToDelete = [];

        dataParams = {
            packetsToAddParamsList: packetsToAddParamsList,
            packetsToUpdateParamsList: packetsToUpdateParamsList,
            comptsToUpdateParamsList: comptsToUpdateParamsList,
            comptIdsToDelete: comptIdsToDelete
        };
        if (angular.isUndefined(savedPktId)) {
            dataParams['packetIdsToDelete'] = packetIdsToDelete;
        } else {
            dataParams['packetId'] = savedPktId;
        }
    };

    var buildSpiesOnMockHelperService = function (isEmpty) {
        spyOn(mockHelperService, 'isEmpty').and.returnValue(isEmpty);
    };

    var buildSpyOnWindowService = function () {
        spyOn(window, 'alert');
    };

    var buildMockScopeData = function () {
        mockScope.data = {};
        mockScope.data.allPackets = fakeAllPackets;
        mockScope.data.newPackets = fakeNewPackets;
    };

    var buildController = function ($controller, $rootScope, $http) {
        mockScope = $rootScope.$new();
        controller = $controller("packetsPanelCtrl", {
            $scope: mockScope,
            $http: $http,
            exchangeService: mockExchangeService,
            helperService: mockHelperService
        });
    }
});
