/*
 * Copyright (c) 2016.  Alex Chulkin
 */

'use strict';

/**
 * The packetsPanelControllers tests
 */

describe("Packets Panel Controller Test", function () {
    var backend;
    var mockExchangeService, mockUtilsService, controller, mockScope, mockCookies;
    var fakeSelectedPktId, fakeComptIdsToUpdate, fakeComptIdsToDelete, fakeAllStates, fakeNewComptLabels,
        fakeAllCheckedComboData, fakeInitialStateId, fakeAllPackets, fakeNewPackets, fakeSavedPktId, fakeDeletedPktId,
        fakeLoadErrorValue, fakeLoadedNoComboDataValue, fakeLoadedNoStatesValue, fakeMinOrMaxValue, fakeNewPktId;
    var forbiddenCallTriggered;
    var httpResponse;
    var httpError, errorStatusBadRequest_, errorStatusNotFound_, errorRequestTimeout;
    var role_, adminRole_;
    var saveAllChangesToBaseUrl_;
    var dataParams;
    var addPackets_, updatePackets_, updateCompts_;
    var addOrUpdatePacketsErrorPrefix_, addOrUpdatePacketsErrorSuffix_, updateComptsError_, addPacketsErrorRoot_,
        updatePacketsErrorRoot_;
    var packetListActiveClass_, packetListNonActiveClass_, narrowPacketCaption_, widePacketCaption_;

    var mockWindow = {
        location: {
            reload: function () {
            }
        }
    };

    beforeEach(angular.mock.module(function ($provide) {
        $provide.value('$window', mockWindow);
    }));

    beforeEach(function () {
        module("packetAdminApp");
    });

    beforeEach(inject(function ($httpBackend, $cookies, saveAllChangesToBaseUrl, role, adminRole, packetListActiveClass,
                                packetListNonActiveClass, updatePacketsErrorRoot, addPackets, updatePackets,
                                updateCompts, addOrUpdatePacketsErrorPrefix, addOrUpdatePacketsErrorSuffix,
                                updateComptsError, addPacketsErrorRoot, errorStatusBadRequest, errorStatusNotFound,
                                narrowPacketCaption, widePacketCaption) {
        mockCookies = $cookies;
        backend = $httpBackend;
        saveAllChangesToBaseUrl_ = saveAllChangesToBaseUrl;
        role_ = role;
        adminRole_ = adminRole;

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

        packetListActiveClass_ = packetListActiveClass;
        packetListNonActiveClass_ = packetListNonActiveClass;
        narrowPacketCaption_ = narrowPacketCaption;
        widePacketCaption_ = widePacketCaption;

        fakeSelectedPktId = 1;
        fakeSavedPktId = 1;
        fakeDeletedPktId = 2;
        fakeNewPktId = 3;

        mockUtilsService = {
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
            getMaximalPacketIndex: function () {
            },
            setMaximalPacketIndex: function (packetInd) {
            },
            getSelectedPacketId: function () {
            },
            setSelectedPacketId: function () {
            },
            setSelectedPacket: function (pkt) {
            },
            getLoadedNoStates: function () {
            },
            getLoadedNoComboData: function () {
            },
            getLoadError: function () {
            },
            getMaximalPacketId: function () {
            },
            getAllCheckedComboData: function (comptId, stateId) {
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

    describe("Init fn test", function () {
        beforeEach(inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http)
        }));
        it("", (inject(function () {
            expect(mockScope.data).toBeDefined();
            expect(mockScope.data).not.toBeNull();
            expect(mockScope.data.adminRoleTitle).toBeDefined();
            expect(mockScope.data.adminRoleTitle).toEqual(adminRole_);
            expect(mockScope.data.isRoleNotAdmin).toBeDefined();
            expect(mockScope.data.isRoleNotAdmin).toEqual(mockCookies.get(role_) !== mockScope.data.adminRoleTitle);
        })));
    });

    describe('All Packets update listener test', function () {
        beforeEach(inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildSpiesReturnValues_fullStuff();
        }));

        it('', function () {
            mockScope.$broadcast('allPackets:update', fakeAllPackets);
            expect(mockScope.data.allPackets).not.toBeNull();
            expect(mockScope.data.allPackets).toEqual(fakeAllPackets);
        });
    });

    describe('New Packets update listener test', function () {
        beforeEach(inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildSpiesReturnValues_fullStuff();
        }));

        it('', function () {
            mockScope.$broadcast('newPackets:update', fakeNewPackets);
            expect(mockScope.data.newPackets).not.toBeNull();
            expect(mockScope.data.newPackets).toEqual(fakeNewPackets);
        });
    });

    describe('Add packet locally function test', function () {
        beforeEach(inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildSpiesOnMockExchangeService();
            mockScope.addPacketLocally();
        }));

        it('', function () {
            expect(mockExchangeService.getMaximalPacketId).toHaveBeenCalledWith();
            var fakePktId = fakeMinOrMaxValue + 1;
            var fakePktIndex = fakeMinOrMaxValue + 1;
            expect(mockExchangeService.setMaximalPacketId).toHaveBeenCalledWith(fakePktId);
            expect(mockExchangeService.getMaximalPacketIndex).toHaveBeenCalledWith();
            expect(mockExchangeService.setMaximalPacketIndex).toHaveBeenCalledWith(fakePktIndex);
            var fakeNewPkt = {id: fakePktId, stateId: 1};
            expect(mockExchangeService.setPacketIdToInd).toHaveBeenCalledWith(fakePktIndex, fakePktId);
            expect(mockExchangeService.setNewPackets).toHaveBeenCalledWith(fakeNewPkt, fakePktId);
            expect(mockExchangeService.setAllPackets).toHaveBeenCalledWith(fakeNewPkt, fakePktId);
            expect(mockExchangeService.setLoadEmpty).toHaveBeenCalledWith(false);
        });
    });

    describe('Delete packet locally function test delete old and unselected pkt', function () {
        beforeEach(inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildSpiesReturnValues_fullStuff();
            buildSpiesOnMockExchangeService();
            buildMockScopeData();
            buildDataParams_fullStuff();
            mockScope.deletePacketLocally(fakeDeletedPktId);
        }));

        it('', function () {
            dataParams.packetIdsToDelete = [fakeDeletedPktId];
            if (!(fakeDeletedPktId in mockScope.data.newPackets)) {
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
                mockScope.saveAllChangesToBase();
                backend.flush();
            }
            expect(mockExchangeService.deleteNewComptLabels).toHaveBeenCalledWith(fakeDeletedPktId);
            expect(mockExchangeService.deleteAllPackets).toHaveBeenCalledWith(fakeDeletedPktId);
            expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith(fakeDeletedPktId);
            expect(mockExchangeService.getSelectedPacketId).toHaveBeenCalledWith();
            if (fakeDeletedPktId === fakeSelectedPktId) {
                expect(mockExchangeService.setSelectedPacket).toHaveBeenCalledWith(null);
            } else {
                expect(mockExchangeService.setSelectedPacket).not.toHaveBeenCalled();
            }
        });
    });

    describe('Delete packet locally function test delete unselected new pkt', function () {
        beforeEach(inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildSpiesReturnValues_fullStuff();
            buildSpiesOnMockExchangeService();
            buildMockScopeData();
            buildDataParams_fullStuff();
            mockScope.deletePacketLocally(fakeNewPktId);
        }));

        it('', function () {
            dataParams.packetIdsToDelete = [fakeNewPktId];
            if (!(fakeNewPktId in mockScope.data.newPackets)) {
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
                mockScope.saveAllChangesToBase();
                backend.flush();
            }
            expect(mockExchangeService.deleteNewComptLabels).toHaveBeenCalledWith(fakeNewPktId);
            expect(mockExchangeService.deleteAllPackets).toHaveBeenCalledWith(fakeNewPktId);
            expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith(fakeNewPktId);
            expect(mockExchangeService.getSelectedPacketId).toHaveBeenCalledWith();
            if (fakeDeletedPktId === fakeSelectedPktId) {
                expect(mockExchangeService.setSelectedPacket).toHaveBeenCalledWith(null);
            } else {
                expect(mockExchangeService.setSelectedPacket).not.toHaveBeenCalled();
            }
        });
    });

    describe('Delete packet locally function test delete selected old pkt', function () {
        beforeEach(inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildSpiesReturnValues_fullStuff();
            buildSpiesOnMockExchangeService();
            buildMockScopeData();
            buildDataParams_fullStuff();
            mockScope.deletePacketLocally(fakeSelectedPktId);
        }));

        it('', function () {
            dataParams.packetIdsToDelete = [fakeSelectedPktId];
            if (!(fakeSelectedPktId in mockScope.data.newPackets)) {
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(httpResponse);
                mockScope.saveAllChangesToBase();
                backend.flush();
            }
            expect(mockExchangeService.deleteNewComptLabels).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.deleteAllPackets).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.getSelectedPacketId).toHaveBeenCalledWith();
            if (fakeSelectedPktId === fakeSelectedPktId) {
                expect(mockExchangeService.setSelectedPacket).toHaveBeenCalledWith(null);
            } else {
                expect(mockExchangeService.setSelectedPacket).not.toHaveBeenCalled();
            }
        });
    });

    describe('Show aggregate buttons function negative test load error', function () {
        beforeEach(inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildSpiesReturnValues_loadedData(true, false, false);
            buildSpiesOnMockExchangeService();
        }));

        it('', function () {
            var res = mockScope.showAggregateButtons();
            expect(res).toEqual((!mockExchangeService.getLoadError()
            && (mockExchangeService.getLoadedNoComboData() === false)
            && (mockExchangeService.getLoadedNoStates() === false)));
            expect(mockExchangeService.getLoadError).toHaveBeenCalledWith();
            expect(mockExchangeService.getLoadedNoComboData).not.toHaveBeenCalled();
            expect(mockExchangeService.getLoadedNoStates).not.toHaveBeenCalled();
        });
    });

    describe('Show aggregate buttons function  negative test loaded no combodata', function () {
        beforeEach(inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildSpiesReturnValues_loadedData(false, true, false);
            buildSpiesOnMockExchangeService();
        }));

        it('', function () {
            var res = mockScope.showAggregateButtons();
            expect(res).toEqual((!mockExchangeService.getLoadError()
            && (mockExchangeService.getLoadedNoComboData() === false)
            && (mockExchangeService.getLoadedNoStates() === false)));
            expect(mockExchangeService.getLoadError).toHaveBeenCalledWith();
            expect(mockExchangeService.getLoadedNoComboData).toHaveBeenCalledWith();
            expect(mockExchangeService.getLoadedNoStates).toHaveBeenCalledWith();
        });
    });

    describe('Show aggregate buttons function  negative test loaded no states', function () {
        beforeEach(inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildSpiesReturnValues_loadedData(false, false, true);
            buildSpiesOnMockExchangeService();
        }));

        it('', function () {
            var res = mockScope.showAggregateButtons();
            expect(res).toEqual((!mockExchangeService.getLoadError()
            && (mockExchangeService.getLoadedNoComboData() === false)
            && (mockExchangeService.getLoadedNoStates() === false)));
            expect(mockExchangeService.getLoadError).toHaveBeenCalledWith();
            expect(mockExchangeService.getLoadedNoComboData).toHaveBeenCalledWith();
            expect(mockExchangeService.getLoadedNoStates).toHaveBeenCalledWith();
        });
    });

    describe('Show aggregate buttons function positive test', function () {
        beforeEach(inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildSpiesOnMockExchangeService();
        }));

        it('', function () {
            var res = mockScope.showAggregateButtons();
            expect(res).toEqual((!mockExchangeService.getLoadError()
            && (mockExchangeService.getLoadedNoComboData() === false)
            && (mockExchangeService.getLoadedNoStates() === false)));
            expect(mockExchangeService.getLoadError).toHaveBeenCalledWith();
            expect(mockExchangeService.getLoadedNoComboData).toHaveBeenCalledWith();
            expect(mockExchangeService.getLoadedNoStates).toHaveBeenCalledWith();
        });
    });

    describe('getPacketClass function test for selected pkt and narrow caption', function () {
        beforeEach(inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildSpiesOnMockExchangeService();
        }));

        it('', function () {
            var selectedPkt = {id: fakeSelectedPktId};
            var result = mockScope.getPacketClass(selectedPkt);
            expect(mockExchangeService.getSelectedPacketId).toHaveBeenCalledWith();
            expect(result).toEqual(packetListActiveClass_ + " " + narrowPacketCaption_);
        });
    });

    describe('getPacketClass function test for unselected pkt and wide caption', function () {
        beforeEach(inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            buildSpiesOnMockExchangeService();
        }));

        it('', function () {
            var selectedPkt = {id: fakeSelectedPktId + 10};
            var result = mockScope.getPacketClass(selectedPkt);
            expect(mockExchangeService.getSelectedPacketId).toHaveBeenCalledWith();
            expect(result).toEqual(packetListNonActiveClass_ + " " + widePacketCaption_);
        });
    });

    describe("Ensure that saveAllChangesToBase() with no args http error response is properly elaborated: " +
        "error status = bad request",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildHttpError(errorStatusBadRequest_);
                buildDataParams_fullStuff();
                buildSpiesOnMockUtilsService(false);
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
                buildSpiesOnMockUtilsService(false);
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
                buildSpiesOnMockUtilsService(false);
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
                buildSpiesOnMockUtilsService(false);
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
                buildSpiesOnMockUtilsService(false);
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
                buildSpiesOnMockUtilsService(false);
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
                buildSpiesOnMockUtilsService(false);
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
                buildSpiesOnMockUtilsService(false);
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
                buildSpiesOnMockUtilsService(false);
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
                buildSpiesOnMockUtilsService(false);
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
                buildSpiesOnMockUtilsService(false);
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
                buildSpiesOnMockUtilsService(false);
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
                buildSpiesOnMockUtilsService(false);
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
                buildSpiesOnMockUtilsService(false);
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
                buildSpiesOnMockUtilsService(false);
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
                buildSpiesOnMockUtilsService(false);
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
                buildSpiesOnMockUtilsService(false);
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
                buildSpiesOnMockUtilsService(false);
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
                buildSpiesOnMockUtilsService(false);
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
            buildSpiesOnMockUtilsService(true);
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
            buildSpiesOnMockUtilsService(true);
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
            buildSpiesOnMockUtilsService(false);
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
            buildSpiesOnMockUtilsService(false);
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
            buildSpiesOnMockUtilsService(false);
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
            buildSpiesOnMockUtilsService(false);
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
            buildSpiesOnMockUtilsService(false);
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
            buildSpiesOnMockUtilsService(false);
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
            buildSpiesOnMockUtilsService(false);
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
            buildSpiesOnMockUtilsService(false);
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
            buildSpiesOnMockUtilsService(false);
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
            buildSpiesOnMockUtilsService(false);
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
            buildSpiesOnMockUtilsService(false);
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
            buildSpiesOnMockUtilsService(false);
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

    describe('Reload button test', function () {
        beforeEach(inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
            spyOn(mockWindow.location, 'reload');
            mockScope.reloadRoute();
        }));

        it('', function () {
            expect(mockWindow.location.reload).toHaveBeenCalledWith();
        });
    });

    var buildSpiesOnMockExchangeService = function () {
        spyOn(mockExchangeService, 'setPacketIdToInd');
        spyOn(mockExchangeService, 'setAllPackets');
        spyOn(mockExchangeService, 'setMaximalPacketIndex');
        spyOn(mockExchangeService, 'setMaximalPacketId');
        spyOn(mockExchangeService, 'getSelectedPacketId').and.returnValue(fakeSelectedPktId);
        spyOn(mockExchangeService, 'setSelectedPacketId');
        spyOn(mockExchangeService, 'setSelectedPacket');
        spyOn(mockExchangeService, 'deleteComptIdsToDelete');
        spyOn(mockExchangeService, 'deleteComptIdsToUpdate');
        spyOn(mockExchangeService, 'deleteNewComptLabels');
        spyOn(mockExchangeService, 'deleteNewPackets');
        spyOn(mockExchangeService, 'deleteAllPackets');
        spyOn(mockExchangeService, 'setLoadEmpty');
        spyOn(mockExchangeService, 'setNewPackets');
        spyOn(mockExchangeService, 'getAllCheckedComboData').and.returnValue(fakeAllCheckedComboData);
        spyOn(mockExchangeService, 'getPacketInitialStateIds').and.returnValue(fakeInitialStateId);
        spyOn(mockExchangeService, 'getComptIdsToDelete').and.returnValue(fakeComptIdsToDelete);
        spyOn(mockExchangeService, 'getComptIdsToUpdate').and.returnValue(fakeComptIdsToUpdate);
        spyOn(mockExchangeService, 'getAllStates').and.returnValue(fakeAllStates);
        spyOn(mockExchangeService, 'getNewComptLabels').and.returnValue(fakeNewComptLabels);
        spyOn(mockExchangeService, 'getLoadError').and.returnValue(fakeLoadErrorValue);
        spyOn(mockExchangeService, 'getLoadedNoComboData').and.returnValue(fakeLoadedNoComboDataValue);
        spyOn(mockExchangeService, 'getLoadedNoStates').and.returnValue(fakeLoadedNoStatesValue);
        spyOn(mockExchangeService, 'getMaximalPacketId').and.returnValue(fakeMinOrMaxValue);
        spyOn(mockExchangeService, 'getMaximalPacketIndex').and.returnValue(fakeMinOrMaxValue);
    };

    var buildSpiesReturnValues_fullStuff = function () {
        fakeComptIdsToDelete = [1, 2];
        fakeComptIdsToUpdate = {"3": true, "4": true};
        fakeAllStates = [{id: "1", label: "state1"}, {id: "2", label: "state2"}];
        fakeAllCheckedComboData = "cd1";
        fakeNewComptLabels = {"5": "lbl5", "6": "lbl6"};
        fakeInitialStateId = 2;
        fakeAllPackets = {"1": {id: "1", stateId: "1"}, "2": {id: "2", stateId: "2"}, "3": {id: "3", stateId: "2"}};
        fakeNewPackets = {"3": {id: "3", stateId: "2"}};
    };

    var buildSpiesReturnValues_loadedData = function (loadErrorVal, loadedNoComboDataVal, loadedNoStatesVal) {
        fakeLoadErrorValue = loadErrorVal;
        fakeLoadedNoComboDataValue = loadedNoComboDataVal;
        fakeLoadedNoStatesValue = loadedNoStatesVal;
    };

    var buildHttpError = function (status) {
        httpError = status;
    };

    var buildSpiesReturnValues_woutDelCompts = function () {
        fakeComptIdsToDelete = [];
        fakeComptIdsToUpdate = {"3": true, "4": true};
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
        fakeComptIdsToUpdate = {"3": true, "4": true};
        fakeAllStates = [{id: 1, label: "state1"}, {id: 2, label: "state2"}];
        fakeAllCheckedComboData = "cd1";
        fakeNewComptLabels = {};
        fakeInitialStateId = 2;
        fakeAllPackets = {"1": {id: 1, stateId: 1}, "2": {id: 2, stateId: 2}};
        fakeNewPackets = {};
    };

    var buildSpiesReturnValues_woutUpdPktState = function () {
        fakeComptIdsToDelete = [1, 2];
        fakeComptIdsToUpdate = {"3": true, "4": true};
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
        fakeComptIdsToUpdate = {"3": true, "4": true};
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
        httpResponse = {};
    };

    var buildResponse_error_addPackets = function () {
        httpResponse = {"ADD_PACKETS": true};
    };

    var buildResponse_error_updatePackets = function () {
        httpResponse = {"UPDATE_PACKETS": true};
    };

    var buildResponse_error_updateCompts = function () {
        httpResponse = {"UPDATE_COMPTS": true};
    };

    var buildResponse_allErrors = function () {
        httpResponse = {'ADD_PACKETS': true, "UPDATE_PACKETS": true, "UPDATE_COMPTS": true};
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

    var buildSpiesOnMockUtilsService = function (isEmpty) {
        spyOn(mockUtilsService, 'isEmpty').and.returnValue(isEmpty);
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
            utilsService: mockUtilsService
        });
    }
});
