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
    var response;
    var errorStatus;
    var role_;
    var saveAllChangesToBaseUrl_;
    var dataParams;
    var adminRole;

    beforeEach(function () {
        module("packetAdminApp");
    });

    beforeEach(inject(function ($httpBackend, $cookies, saveAllChangesToBaseUrl, labelLabel, initialPacketIndex, role) {
        mockCookies = $cookies;
        backend = $httpBackend;
        saveAllChangesToBaseUrl_ = saveAllChangesToBaseUrl;
        role_ = role;
        adminRole = "ADMIN";

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
        it("", (inject(function ($controller, $rootScope, $http) {
            expect(mockScope.data).not.toBeNull();
            expect(mockScope.data.adminRoleTitle).toBeDefined();
            expect(mockScope.data.adminRoleTitle).toEqual(adminRole);
            expect(mockScope.data.isRoleNotAdmin).toBeDefined();
            expect(mockScope.data.isRoleNotAdmin).toEqual(mockCookies.get(role_) !== mockScope.data.adminRoleTitle);
        })));
    });

    describe("Ensure that saveAllChangesToBase() with no args builds the data params properly with full stuff:" +
        " pkt add/upd/del, compt upd/del",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildDataParams_fullStuff(fakeSavedPktId);
                buildResponse_success();
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(response);
            }));

            it("", (inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http);
                buildMockScopeData();
                mockScope.deletePacketLocally(fakeDeletedPktId);
                mockScope.saveAllChangesToBase(fakeSavedPktId);
                backend.flush();
            })));
        });

    describe("Ensure that saveAllChangesToBase() with arg = pktId builds the data params properly with full stuff:" +
        " pkt add/upd/del, compt upd/del",
        function () {
            beforeEach(inject(function () {
                buildSpiesReturnValues_fullStuff();
                buildDataParams_fullStuff();
                buildResponse_success();
                buildSpiesOnMockHelperService(false);
                buildSpiesOnMockExchangeService();
                backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(response);
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
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(response);
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
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(response);
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
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(response);
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
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(response);
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
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(response);
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
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(response);
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
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(response);
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
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(response);
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
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(response);
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
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(response);
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
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(response);
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
            backend.expect("POST", saveAllChangesToBaseUrl_, {dataParams: dataParams}).respond(response);
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
        response = [];
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
