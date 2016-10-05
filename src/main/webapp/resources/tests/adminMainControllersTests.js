/**
 * Created by alexc_000 on 2016-10-01.
 */

describe("Login Controller Test", function () {
    var backend;
    var mockExchangeService, mockHelperService, controller, mockScope;
    var initialPacketIndex_, labelLabel_, loadDataUrl_;
    var fakeComptsLength, fakeMinimalValue, fakePacketIndValue, fakeSelectedPktId, fakeLoadErrorValue,
        fakeLoadedNoComboDataValue, fakeLoadedNoStatesValue, fakePacketIsAlreadySelectedAtLeastOnceValue;
    var response;
    var isPacketIdUndefined;
    var errorStatus;

    beforeEach(function () {
        module("packetAdminApp");
    });

    beforeEach(inject(function ($httpBackend, loadDataUrl, labelLabel, initialPacketIndex) {
        labelLabel_ = labelLabel;
        initialPacketIndex_ = initialPacketIndex;
        backend = $httpBackend;
        loadDataUrl_ = loadDataUrl;
        fakeComptsLength = 0;
        fakeMinimalValue = -1;
        fakePacketIndValue = 0;
        fakeSelectedPktId = 1;
        fakeLoadErrorValue = false;
        fakeLoadedNoComboDataValue = false;
        fakeLoadedNoStatesValue = false;
        fakePacketIsAlreadySelectedAtLeastOnceValue = true;
        errorStatus = 500;

        mockHelperService = {
            isEmpty: function () {
            }
        };

        mockExchangeService = {
            init: function () {
            },
            setComptLabels: function (value, pktId, label) {
            },
            pushToCompts: function (value, packetInd) {
            },
            setPacketIdToInd: function (value, packetId) {
            },
            getComptsLength: function (packetInd) {
            },
            setComptIdToInd: function (value, id) {
            },
            setMaximalComptId: function (value) {
            },
            getMaximalComptId: function () {
            },
            setLoadedNoPackets: function (value) {
            },
            setLoadedNoSelectedPacket: function (value) {
            },
            setLoadedNoUnSelectedPacket: function (value, pktId) {
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
            setSelectedPage: function (pkt) {
            },
            setPacketInitialStateIds: function () {
            },
            setAllStates: function (value) {
            },
            setAllStateLabels: function (states, labelLabel) {
            },
            setLoadedNoStates: function (value) {
            },
            setLoadedNoComboData: function (value) {
            },
            setComboDataDefaultSet: function (comboData) {
            },
            initializeNewComptCheckedVals: function () {
            },
            getAllComboData: function (comptId, stateId) {
            },
            getAllCheckedComboData: function (comptId, stateId) {
            },
            setAllComboData: function (value, comptId, stateId) {
            },
            setAllCheckedComboData: function (value, comptId, stateId) {
            },
            pushToAllComboData: function (value, comptId, stateId, comptSupplInfoIndex, comptSupplInfoLength) {
            },
            getLoadedNoComboData: function () {
            },
            getLoadError: function () {
            },
            setLoadError: function (value) {
            },
            getLoadedNoStates: function () {
            },
            getPacketIsAlreadySelectedAtLeastOnce: function () {
            },
            deleteComptIdsToDelete: function (packetId) {
            },
            deleteComptIdsToUpdate: function (packetId, comptId) {
            },
            deleteNewComptLabels: function (packetId, comptId) {
            },
            deleteNewPackets: function (packetId) {
            },
            setCompts: function (value, packetInd) {
            },
            getPacketIdToInd: function (pktId) {
            }
        };
        spyOn(mockExchangeService, 'init');
        spyOn(mockExchangeService, 'setComptLabels');
        spyOn(mockExchangeService, 'pushToCompts');
        spyOn(mockExchangeService, 'setPacketIdToInd');
        spyOn(mockExchangeService, 'getComptsLength').and.returnValue(fakeComptsLength);
        spyOn(mockExchangeService, 'setComptIdToInd');
        spyOn(mockExchangeService, 'setMaximalComptId');
        spyOn(mockExchangeService, 'getMaximalComptId').and.returnValue(fakeMinimalValue);
        spyOn(mockExchangeService, 'setLoadedNoPackets');
        spyOn(mockExchangeService, 'setLoadedNoSelectedPacket');
        spyOn(mockExchangeService, 'setLoadedNoUnSelectedPacket');
        spyOn(mockExchangeService, 'setAllPackets');
        spyOn(mockExchangeService, 'setMaximalPacketId');
        spyOn(mockExchangeService, 'getMaximalPacketId').and.returnValue(fakeMinimalValue);
        spyOn(mockExchangeService, 'setMaximalPacketIndex');
        spyOn(mockExchangeService, 'getMaximalPacketIndex').and.returnValue(fakeMinimalValue);
        spyOn(mockExchangeService, 'getSelectedPacketId').and.returnValue(fakeSelectedPktId);
        spyOn(mockExchangeService, 'setSelectedPacket');
        spyOn(mockExchangeService, 'setSelectedPage');
        spyOn(mockExchangeService, 'setPacketInitialStateIds');
        spyOn(mockExchangeService, 'setAllStates');
        spyOn(mockExchangeService, 'setAllStateLabels');
        spyOn(mockExchangeService, 'setLoadedNoStates');
        spyOn(mockExchangeService, 'setLoadedNoComboData');
        spyOn(mockExchangeService, 'setComboDataDefaultSet');
        spyOn(mockExchangeService, 'initializeNewComptCheckedVals');
        spyOn(mockExchangeService, 'getAllComboData');
        spyOn(mockExchangeService, 'getAllCheckedComboData');
        spyOn(mockExchangeService, 'setAllComboData');
        spyOn(mockExchangeService, 'setAllCheckedComboData');
        spyOn(mockExchangeService, 'pushToAllComboData');
        spyOn(mockExchangeService, 'setCompts');
        spyOn(mockExchangeService, 'getPacketIdToInd').and.returnValue(fakePacketIndValue);
        spyOn(mockExchangeService, 'deleteComptIdsToDelete');
        spyOn(mockExchangeService, 'deleteComptIdsToUpdate');
        spyOn(mockExchangeService, 'deleteNewComptLabels');
        spyOn(mockExchangeService, 'deleteNewPackets');
        spyOn(mockExchangeService, 'setLoadError');
        spyOn(mockExchangeService, 'getLoadError').and.returnValue(fakeLoadErrorValue);
        spyOn(mockExchangeService, 'getLoadedNoComboData').and.returnValue(fakeLoadedNoComboDataValue);
        spyOn(mockExchangeService, 'getLoadedNoStates').and.returnValue(fakeLoadedNoStatesValue);
        spyOn(mockExchangeService, 'getPacketIsAlreadySelectedAtLeastOnce')
            .and.returnValue(fakePacketIsAlreadySelectedAtLeastOnceValue);
    }));

    afterEach(function () {
        backend.verifyNoOutstandingExpectation();
        backend.verifyNoOutstandingRequest();
    });

    describe('Test controller load all packets function with not-empty packets', function () {
        beforeEach(function () {
            buildLoadManyPacketsResponse();
        });

        describe("Init fn", function () {
            beforeEach(inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http, false, {}, response);
                backend.flush();
            }));
            it("", (inject(function ($controller, $rootScope, $http) {
                initOrLoadAllPacketsTestFn(false, false, false, false);
            })));
        });

        describe("Load all packets fn", function () {
            beforeEach(inject(function ($controller, $rootScope, $http) {
                buildController($controller, $rootScope, $http, false, {}, 400);
                backend.flush();
                backend.expect("POST", loadDataUrl_, {dataParams: {}}).respond(response);

            }));
            it("", (inject(function ($controller, $rootScope, $http) {
                mockScope.loadPackets();
                backend.flush();
                initOrLoadAllPacketsTestFn(false, false, false, false);
            })));
        });
    });

    describe("Test load one packet controller fn with loaded pkts, compts, states and comboData", function () {
        beforeEach(angular.mock.inject(function ($controller, $rootScope, $http) {
            buildLoadOnePacketResponse();
            buildController($controller, $rootScope, $http, false, {}, 400);
            backend.flush();
            backend.expect("POST", loadDataUrl_, {dataParams: {packetId: 1}}).respond(response);
        }));
        it("", function () {
            mockScope.loadPackets(1, 0);
            backend.flush();
            initOrLoadAllPacketsTestFn(false, false, false, false, 1, 0);
        });
    });

    describe("Test load one packet controller fn when loading unselected packet", function () {
        beforeEach(angular.mock.inject(function ($controller, $rootScope, $http) {
            buildLoadOneNotSelectedPacketResponse();
            buildController($controller, $rootScope, $http, false, {}, 400);
            backend.flush();
            backend.expect("POST", loadDataUrl_, {dataParams: {packetId: 1}}).respond(response);
        }));
        it("", function () {
            mockScope.loadPackets(1, 0);
            backend.flush();
            initOrLoadAllPacketsTestFn(false, false, false, false, 1, 0);
        });
    });

    describe("Test load packets controller fn with not loaded pkts, compts, states and comboData", function () {
        beforeEach(angular.mock.inject(function ($controller, $rootScope, $http) {
            buildNoComptsNoStatesNoPacketsNoComboDataResponse();
            buildController($controller, $rootScope, $http, true, {}, 400);
            backend.flush();
            backend.expect("POST", loadDataUrl_, {dataParams: {}}).respond(response);
        }));
        it("", function () {
            mockScope.loadPackets();
            backend.flush();
            initOrLoadAllPacketsTestFn(true, true, true, true);
        });
    });

    describe("Test isDataLoadedProperly fn", function () {
        beforeEach(angular.mock.inject(function ($controller, $rootScope, $http) {
            buildLoadManyPacketsResponse();
            buildController($controller, $rootScope, $http, false, {}, response);
            backend.flush();
        }));
        it("", function () {
            var res = mockScope.isDataLoadedProperly();
            expect(res).toEqual(!(mockExchangeService.getLoadError() && mockExchangeService.getLoadedNoComboData()
            && mockExchangeService.getLoadedNoStates()));
            expect(mockExchangeService.getLoadError).toHaveBeenCalledWith();
            expect(mockExchangeService.getLoadedNoComboData).toHaveBeenCalledWith();
            expect(mockExchangeService.getLoadedNoStates).toHaveBeenCalledWith();
        });
    });

    describe("Test isComboDataNotLoaded fn", function () {
        beforeEach(angular.mock.inject(function ($controller, $rootScope, $http) {
            buildLoadManyPacketsResponse();
            buildController($controller, $rootScope, $http, false, {}, response);
            backend.flush();
        }));
        it("", function () {
            var res = mockScope.isComboDataNotLoaded();
            expect(res).toEqual(mockExchangeService.getLoadedNoComboData());
            expect(mockExchangeService.getLoadedNoComboData).toHaveBeenCalledWith();
        });
    });

    describe("Test isDataLoadError fn", function () {
        beforeEach(angular.mock.inject(function ($controller, $rootScope, $http) {
            buildLoadManyPacketsResponse();
            buildController($controller, $rootScope, $http, false, {}, response);
            backend.flush();
        }));
        it("", function () {
            var res = mockScope.isDataLoadError();
            expect(res).toEqual(mockExchangeService.getLoadError());
            expect(mockExchangeService.getLoadError).toHaveBeenCalledWith();
        });
    });

    describe("Test isStatesNotLoaded fn", function () {
        beforeEach(angular.mock.inject(function ($controller, $rootScope, $http) {
            buildLoadManyPacketsResponse();
            buildController($controller, $rootScope, $http, false, {}, response);
            backend.flush();
        }));
        it("", function () {
            var res = mockScope.isStatesNotLoaded();
            expect(res).toEqual(mockExchangeService.getLoadedNoStates());
            expect(mockExchangeService.getLoadedNoStates).toHaveBeenCalledWith();
        });
    });

    describe("Test isPacketAlreadySelectedAtLeastOnce fn", function () {
        beforeEach(angular.mock.inject(function ($controller, $rootScope, $http) {
            buildLoadManyPacketsResponse();
            buildController($controller, $rootScope, $http, false, {}, response);
            backend.flush();
        }));
        it("", function () {
            var res = mockScope.isPacketAlreadySelectedAtLeastOnce();
            expect(res).toEqual(mockExchangeService.getPacketIsAlreadySelectedAtLeastOnce());
            expect(mockExchangeService.getPacketIsAlreadySelectedAtLeastOnce).toHaveBeenCalledWith();
        });
    });

    describe("Test selectPacket fn", function () {
        beforeEach(angular.mock.inject(function ($controller, $rootScope, $http) {
            buildLoadManyPacketsResponse();
            buildController($controller, $rootScope, $http, false, {}, response);
            backend.flush();
        }));
        it("", function () {
            var selPkt = response.packets[0];
            var automaticallySelPage = 1;
            mockScope.selectPacket(selPkt);
            expect(mockExchangeService.setSelectedPacket).toHaveBeenCalledWith(selPkt);
            expect(mockExchangeService.setSelectedPage).toHaveBeenCalledWith(automaticallySelPage);
        });
    });

    describe("Test selectPage fn", function () {
        beforeEach(angular.mock.inject(function ($controller, $rootScope, $http) {
            buildLoadManyPacketsResponse();
            buildController($controller, $rootScope, $http, false, {}, response);
            backend.flush();
        }));
        it("", function () {
            var automaticallySelPage = 1;
            mockScope.selectPage(automaticallySelPage);
            expect(mockExchangeService.setSelectedPage).toHaveBeenCalledWith(automaticallySelPage);
        });
    });

    describe("Test setLoadError fn", function () {
        beforeEach(angular.mock.inject(function ($controller, $rootScope, $http) {
            response = errorStatus;
            buildController($controller, $rootScope, $http, false, {}, response);
            backend.flush();
        }));
        it("", function () {
            var setLoadErrorVal = true;
            expect(mockExchangeService.setLoadError).toHaveBeenCalledWith(setLoadErrorVal);
        });
    });

    var initOrLoadAllPacketsTestFn = function (isPacketsEmpty, loadedNoStates, loadedNoComboData, loadedNoCompts,
                                               packetId, numOfOtherPkts) {
        it("Service init performs correctly", function () {
            if (angular.isUndefined(packetId)) {
                expect(mockExchangeService.init).toHaveBeenCalledWith();
            } else {
                expect(mockExchangeService.deleteComptIdsToDelete).toHaveBeenCalledWith(packetId);
                expect(mockExchangeService.deleteComptIdsToUpdate).toHaveBeenCalledWith(packetId);
                expect(mockExchangeService.deleteNewComptLabels).toHaveBeenCalledWith(packetId);
                expect(mockExchangeService.deleteNewPackets).toHaveBeenCalledWith(packetId);
            }
        });

        it("Prepare compts performs correctly", function () {
            isPacketIdUndefined = angular.isUndefined(packetId);
            var packetInd = isPacketIdUndefined ? initialPacketIndex_ : fakePacketIndValue;
            var visitedPackets = {};
            var comptIndices = [0, 1, 0];
            angular.forEach(response.compts, function (compt) {
                var localPktId = isPacketIdUndefined ? compt.packetId : packetId;
                var comptId = compt.id;
                var label = compt.label.toUpperCase();
                if (!(localPktId in visitedPackets)) {
                    visitedPackets[localPktId] = true;
                    expect(mockExchangeService.setComptLabels).toHaveBeenCalledWith({}, localPktId);
                    if (isPacketIdUndefined) {
                        expect(mockExchangeService.pushToCompts).toHaveBeenCalledWith([]);
                        expect(mockExchangeService.setPacketIdToInd).toHaveBeenCalledWith(++packetInd, localPktId);
                    } else {
                        expect(mockExchangeService.setCompts).toHaveBeenCalledWith([], packetInd);
                    }
                }
                expect(mockExchangeService.setComptLabels).toHaveBeenCalledWith(true, localPktId, label);
                expect(mockExchangeService.getComptsLength).toHaveBeenCalledWith(packetInd);
                expect(mockExchangeService.setComptIdToInd).toHaveBeenCalledWith(fakeComptsLength, comptId);
                expect(mockExchangeService.pushToCompts).toHaveBeenCalledWith(compt, packetInd);
                expect(mockExchangeService.getMaximalComptId).toHaveBeenCalledWith();
                expect(mockExchangeService.setMaximalComptId).toHaveBeenCalledWith(comptId);
            });
            expect(mockExchangeService.getMaximalPacketIndex).toHaveBeenCalledWith();
            expect(mockExchangeService.setMaximalPacketIndex).toHaveBeenCalledWith(packetInd);
        });

        it("Prepare packets performs correctly", function () {
            isPacketIdUndefined = angular.isUndefined(packetId);
            expect(mockHelperService.isEmpty).toHaveBeenCalledWith(response.packets);
            expect(mockExchangeService.getSelectedPacketId).toHaveBeenCalledWith();
            expect(mockExchangeService.setLoadedNoPackets).toHaveBeenCalledWith(isPacketsEmpty
                && (isPacketIdUndefined || numOfOtherPkts === 0));
            expect(mockExchangeService.setLoadedNoPackets).not.toHaveBeenCalledWith(!(isPacketsEmpty
            && (isPacketIdUndefined || numOfOtherPkts === 0)));
            expect(mockExchangeService.setLoadedNoSelectedPacket).toHaveBeenCalledWith
            (isPacketsEmpty && !isPacketIdUndefined && packetId === fakeSelectedPktId);
            expect(mockExchangeService.setLoadedNoSelectedPacket).not.toHaveBeenCalledWith
            (!(isPacketsEmpty && !isPacketIdUndefined && packetId === fakeSelectedPktId));
            expect(mockExchangeService.setLoadedNoUnSelectedPacket).toHaveBeenCalledWith
            (isPacketsEmpty && !isPacketIdUndefined && packetId !== fakeSelectedPktId, packetId);
            expect(mockExchangeService.setLoadedNoUnSelectedPacket).not.toHaveBeenCalledWith
            (!(isPacketsEmpty && !isPacketIdUndefined && packetId !== fakeSelectedPktId, packetId));

            angular.forEach(response.packets, function (pkt) {
                var pktId = pkt.id;
                expect(mockExchangeService.setAllPackets).toHaveBeenCalledWith(pkt, pktId);
                expect(mockExchangeService.getMaximalPacketId).toHaveBeenCalledWith();
                expect(mockExchangeService.setMaximalPacketId).toHaveBeenCalledWith(pktId);
                expect(mockExchangeService.setPacketInitialStateIds).toHaveBeenCalledWith(pkt.stateId, pktId);
            });
            if (!isPacketsEmpty && (isPacketIdUndefined || response.packets[0].id === fakeSelectedPktId)) {
                expect(mockExchangeService.setSelectedPacket).toHaveBeenCalledWith(response.packets[0]);
            }
        });

        it("Prepare states performs correctly", function () {
            expect(mockHelperService.isEmpty).toHaveBeenCalledWith(response.states);
            expect(mockExchangeService.setLoadedNoStates).toHaveBeenCalledWith(loadedNoStates);
            expect(mockExchangeService.setLoadedNoStates).not.toHaveBeenCalledWith(!loadedNoStates);
            expect(mockExchangeService.setAllStates).toHaveBeenCalledWith(response.states);
            expect(mockExchangeService.setAllStateLabels).toHaveBeenCalledWith(response.states, labelLabel_);
        });

        it("Prepare ComboData performs correctly", function () {
            expect(mockHelperService.isEmpty).toHaveBeenCalledWith(response.comboData);
            expect(mockExchangeService.setLoadedNoComboData).toHaveBeenCalledWith(loadedNoComboData);
            expect(mockExchangeService.setLoadedNoStates).not.toHaveBeenCalledWith(!loadedNoComboData);
            expect(mockExchangeService.setComboDataDefaultSet).toHaveBeenCalledWith(response.comboData);
            expect(mockExchangeService.initializeNewComptCheckedVals).toHaveBeenCalledWith();
        });

        it("Prepare ComptSupplInfo performs correctly", function () {
            if (!loadedNoCompts && !loadedNoComboData && !loadedNoStates) {
                var comptSupplInfoLength = response.comptSupplInfo.length;
                var i = 0;
                var visitedComptIds = {};
                angular.forEach(response.comptSupplInfo, function (item) {
                    var comptId = item.comptId;
                    var stateId = item.stateId;
                    var label = item.label;
                    var checked = item.checked;
                    expect(mockExchangeService.getAllComboData).toHaveBeenCalledWith(comptId);
                    if (!visitedComptIds[comptId]) {
                        expect(mockExchangeService.setAllComboData).toHaveBeenCalledWith({}, comptId);
                        visitedComptIds[comptId] = true;
                    }
                    expect(mockExchangeService.getAllComboData).toHaveBeenCalledWith(comptId, stateId);

                    if (i % 4 === 0 || i % 4 === 1) {
                        expect(mockExchangeService.setAllComboData).toHaveBeenCalledWith([], comptId, stateId);
                    }
                    expect(mockExchangeService.pushToAllComboData).toHaveBeenCalledWith
                    (label, comptId, stateId, i, comptSupplInfoLength);

                    if (checked) {
                        expect(mockExchangeService.getAllCheckedComboData).toHaveBeenCalledWith(comptId);
                        if (i % 4 === 1) {
                            expect(mockExchangeService.setAllCheckedComboData).toHaveBeenCalledWith({}, comptId);
                        }
                        expect(mockExchangeService.setAllCheckedComboData).toHaveBeenCalledWith(label, comptId, stateId);
                    }
                    i++;
                });
            } else {
                expect(mockExchangeService.getAllCheckedComboData).not.toHaveBeenCalled();
                expect(mockExchangeService.setAllCheckedComboData).not.toHaveBeenCalled();
                expect(mockExchangeService.setAllComboData).not.toHaveBeenCalled();
                expect(mockExchangeService.getAllComboData).not.toHaveBeenCalled();
                expect(mockExchangeService.pushToAllComboData).not.toHaveBeenCalled();
            }
        });
    };

    var buildLoadedCompts = function (packetIds) {
        var compts = [];
        for (var i = 1; i <= packetIds.length; i++) {
            var compt = {};
            compt.id = i;
            compt.label = 'compt' + String(i);
            compt.packetId = packetIds[i - 1];
            compts.push(compt);
        }
        return compts;
    };

    var buildLoadedPackets = function (stateIds) {
        var packets = [];
        for (var i = 1; i <= stateIds.length; i++) {
            var packet = {};
            packet.id = i;
            packet.stateId = stateIds[i - 1];
            packets.push(packet);
        }
        return packets;
    };

    var buildLoadedStates = function (num) {
        var states = [];
        for (var i = 1; i <= num; i++) {
            var state = {};
            state.id = i;
            state.label = 'state' + String(i);
            states.push(state);
        }
        return states;
    };

    var buildLoadedComboData = function (num) {
        var comboData = [];
        for (var i = 1; i <= num; i++) {
            var cd = {};
            cd.id = i;
            cd.label = 'cd' + String(i);
            comboData.push(cd);
        }
        return comboData;
    };

    var buildLoadedComptSupplInfo = function (comptIds, stateIds, comboDataIds, checkeds) {
        var comptSupplInfo = [];
        for (var i = 1; i <= comptIds.length; i++) {
            csi = {};
            csi.id = i;
            csi.label = 'cd' + comboDataIds[i - 1];
            csi.comptId = comptIds[i - 1];
            csi.stateId = stateIds[i - 1];
            csi.checked = checkeds[i - 1];
            comptSupplInfo.push(csi);
        }
        return comptSupplInfo;
    };

    var buildLoadManyPacketsResponse = function () {
        response = {};
        response.compts = buildLoadedCompts([1, 1, 2]);
        response.packets = buildLoadedPackets([1, 2]);
        response.states = buildLoadedStates(2);
        response.comboData = buildLoadedComboData(2);
        response.comptSupplInfo = buildLoadedComptSupplInfo(
            [1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3],
            [1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2],
            [1, 1, 2, 2, 1, 1, 2, 2, 1, 1, 2, 2],
            [true, false, false, true, true, false, false, true, true, false, false, true]
        );
    };

    var buildLoadOnePacketResponse = function () {
        response = {};
        response.compts = buildLoadedCompts([1, 1]);
        response.packets = buildLoadedPackets([1]);
        response.states = buildLoadedStates(2);
        response.comboData = buildLoadedComboData(2);
        response.comptSupplInfo = buildLoadedComptSupplInfo(
            [1, 1, 1, 1, 2, 2, 2, 2],
            [1, 2, 1, 2, 1, 2, 1, 2],
            [1, 1, 2, 2, 1, 1, 2, 2],
            [true, false, false, true, true, false, false, true]);
    };

    var buildLoadOneNotSelectedPacketResponse = function () {
        response = {};
        response.compts = buildLoadedCompts([2, 2]);
        response.packets = buildLoadedPackets([1]);
        response.states = buildLoadedStates(2);
        response.comboData = buildLoadedComboData(2);
        response.comptSupplInfo = buildLoadedComptSupplInfo(
            [1, 1, 1, 1, 2, 2, 2, 2],
            [1, 2, 1, 2, 1, 2, 1, 2],
            [1, 1, 2, 2, 1, 1, 2, 2],
            [true, false, false, true, true, false, false, true]);
    };

    var buildNoComptsNoStatesNoPacketsNoComboDataResponse = function () {
        response = {};
        response.compts = [];
        response.packets = [];
        response.states = [];
        response.comboData = [];
    };


    var buildController = function ($controller, $rootScope, $http, isEmptyVal, dataParams, response) {
        spyOn(mockHelperService, 'isEmpty').and.returnValue(isEmptyVal);
        mockScope = $rootScope.$new();
        backend.expect("POST", loadDataUrl_, {dataParams: dataParams}).respond(response);
        controller = $controller("mainCtrl", {
            $scope: mockScope,
            $http: $http,
            exchangeService: mockExchangeService,
            helperService: mockHelperService
        });
    }
});