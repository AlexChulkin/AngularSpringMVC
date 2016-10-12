/**
 * Created by alexc_000 on 2016-10-12.
 */
describe("Compts Panel Controller Test", function () {
    var mockExchangeService, controller, mockScope;
    var fakeSelectedPktId, fakeComptIdsToUpdate, fakeComptIdsToDelete, fakeAllStates, fakeAllStateLabels,
        fakeNewComptLabels, fakeSelectedCompts, fakeSelectedPkt, fakeSelectedPage, fakeAllComboData,
        fakeLoadedNoPackets, fakeLoadedNoSelectedPacket, fakeInitialAllCheckedComboData, fakeComboDataDefaultSet,
        fakeNewComptCheckedVals, fakeSelectedPktIsEmpty, fakeAllCheckedComboData, fakeInitialStateId, fakeAllPackets,
        fakeNewPackets;
    var formLabelPattern_;
    var packetListPageCount_;
    var pageListActiveClass_, pageListNonActiveClass_;

    beforeEach(function () {
        module("packetAdminApp");
    });

    beforeEach(inject(function (pageListActiveClass, pageListNonActiveClass, packetListPageCount, exchangeService,
                                formLabelPattern) {
        formLabelPattern_ = formLabelPattern;
        packetListPageCount_ = packetListPageCount;
        pageListActiveClass_ = pageListActiveClass;
        pageListNonActiveClass_ = pageListNonActiveClass;

        mockExchangeService = {
            getSelectedPacketIsEmpty: function () {
            },
            setComptIdToInd: function (value, id) {
            },
            getComptIdToInd: function (id) {
            },
            getMaximalComptId: function () {
            },
            setMaximalComptId: function (value) {
            },
            getSelectedPacketId: function () {
            },
            getSelectedPacket: function () {
            },
            getSelectedPage: function () {
            },
            getLoadedNoPackets: function () {
            },
            getLoadedNoSelectedPacket: function () {
            },
            setSelectedComptLabels: function (value, label) {
            },
            pushToSelectedCompts: function (value) {
            },
            deleteComptIdsToUpdate: function (packetId, comptId) {
            },
            deleteNewComptLabels: function (packetId, comptId) {
            },
            getComptIdsToUpdate: function (pktId) {
            },
            setComptIdsToUpdate: function (value, pktId, comptId) {
            },
            getComptIdsToDelete: function (pktId) {
            },
            setComptIdsToDelete: function (value, pktId) {
            },
            pushToComptIdsToDelete: function (value, pktId) {
            },
            getNewComptLabels: function (pktId) {
            },
            setNewComptLabels: function (value, pktId, comptId) {
            },
            setAllComboData: function (broadcast, value, comptId, stateId) {
            },
            setAllCheckedComboData: function (broadcast, updInitialVals, value, comptId, stateId) {
            },
            getAllStatesLength: function () {
            },
            deleteSelectedComptLabels: function (label) {
            }
        };
    }));

    beforeEach(inject(function ($controller, $rootScope, $http) {
        buildController($controller, $rootScope, $http)
    }));

    describe("Init fn test", function () {
        it("", (inject(function () {
            var regex = new Regexp(formLabelPattern_);
            expect(mockScope.data).toBeDefined();
            expect(mockScope.data).not.toBeNull();
            expect(mockScope.data.pageSize).toEqual(packetListPageCount_);
            expect(mockScope.data.comptLabelMatchPattern).toEqual(regex);
        })));
    });

    describe('The update listener tests', function () {
        beforeEach(inject(function () {
            buildSpiesReturnValues_fullStuff();
        }));

        it('Selected compts', function () {
            mockScope.$broadcast('selectedCompts:update', fakeSelectedCompts);
            expect(mockScope.data.selectedCompts).toEqual(fakeSelectedCompts);
        });

        it('Selected page', function () {
            mockScope.$broadcast('selectedPage:update', fakeSelectedPage);
            expect(mockScope.data.selectedPage).toEqual(fakeSelectedPage);
        });

        it('Selected packet', function () {
            mockScope.$broadcast('selectedPacket:update', fakeSelectedPkt);
            expect(mockScope.data.selectedPacket).toEqual(fakeSelectedPkt);
        });

        it('All states', function () {
            mockScope.$broadcast('allStates:update', fakeAllStates);
            expect(mockScope.data.allStates).toEqual(fakeAllStates);
        });

        it('All state labels', function () {
            mockScope.$broadcast('allStateLabels:update', fakeAllStateLabels);
            expect(mockScope.data.allStateLabels).toEqual(fakeAllStateLabels);
        });

        it('All comboData labels', function () {
            mockScope.$broadcast('allComboData:update', fakeAllComboData);
            expect(mockScope.data.allComboData).toEqual(fakeAllComboData);
        });

        it('All checked comboData', function () {
            mockScope.$broadcast('allCheckedComboData:update', fakeAllCheckedComboData);
            expect(mockScope.data.allCheckedComboData).toEqual(fakeAllCheckedComboData);
        });

        it('All initial checked comboData', function () {
            mockScope.$broadcast('initialAllCheckedComboData:update', fakeInitialAllCheckedComboData);
            expect(mockScope.data.initialAllCheckedComboData).toEqual(fakeInitialAllCheckedComboData);
        });

        it('ComboData default set', function () {
            mockScope.$broadcast('comboDataDefaultSet:update', fakeComboDataDefaultSet);
            expect(mockScope.data.comboDataDefaultSet).toEqual(fakeComboDataDefaultSet);
        });

        it('New compt checked values', function () {
            mockScope.$broadcast('newComptCheckedVals:update', fakeNewComptCheckedVals);
            expect(mockScope.data.newComptCheckedVals).toEqual(fakeNewComptCheckedVals);
        });
    });

    describe('The function tests', function () {
        beforeEach(inject(function () {
            buildSpiesReturnValues_fullStuff();
            buildSpiesOnMockExchangeService();
        }));

        it('IsSelectedPacketEmpty() proper work test', function () {
            var res = mockScope.isSelectedPacketEmpty();
            expect(mockExchangeService.getSelectedPacketIsEmpty).toHaveBeenCalledWith();
            expect(res).toEqual(fakeSelectedPktIsEmpty);
        });

        it('isPacketSelected() proper work test', function () {
            var res = mockScope.isPacketSelected();
            expect(mockExchangeService.getSelectedPacket).toHaveBeenCalledWith();
            expect(res).toEqual(fakeSelectedPkt);
        });

        it('isPacketsNotLoaded() proper work test', function () {
            var res = mockScope.isPacketsNotLoaded();
            expect(mockExchangeService.getLoadedNoPackets).toHaveBeenCalledWith();
            expect(res).toEqual(fakeLoadedNoPackets);
        });

        it('isSelectedPacketNotLoaded() proper work test', function () {
            var res = mockScope.isSelectedPacketNotLoaded();
            expect(mockExchangeService.getLoadedNoSelectedPacket).toHaveBeenCalledWith();
            expect(res).toEqual(fakeLoadedNoSelectedPacket);
        });

        it('getPageClass() proper work test', function () {
            var res = mockScope.getPageClass(fakeSelectedPage);
            expect(mockExchangeService.getSelectedPage).toHaveBeenCalledWith();
            expect(res).toBe(pageListActiveClass_);
            res = mockScope.getPageClass(fakeSelectedPage + 1);
            expect(mockExchangeService.getSelectedPage).toHaveBeenCalledWith();
            expect(res).toBe(pageListNonActiveClass_);
        });

        it('notNull() proper work test', function () {
            var res = mockScope.notNull(undefined);
            expect(res).toBeTruthy();
            res = mockScope.notNull({});
            expect(res).toBeTruthy();
            res = mockScope.notNull(2);
            expect(res).toBeTruthy();
            res = mockScope.notNull(null);
            expect(res).toBeFalsy();
        });
    });

    var buildSpiesReturnValues_fullStuff = function () {
        fakeSelectedCompts = [{id: 3, packetId: 1, label: "lbl3"}, {id: 4, packetId: 1, label: "lbl4"},
            {id: 5, packetId: 1, label: "lbl5"}, {id: 6, packetId: 1, label: "lbl6"}];
        fakeSelectedPkt = {id: "1", stateId: "1"};
        fakeSelectedPktIsEmpty = false;
        fakeLoadedNoPackets = false;
        fakeLoadedNoSelectedPacket = false;
        fakeSelectedPage = 1;
        fakeComptIdsToDelete = [1, 2];
        fakeComptIdsToUpdate = {"3": "true", "4": "true"};
        fakeAllStates = [{id: "1", label: "state1"}, {id: "2", label: "state2"}];
        fakeAllStateLabels = ["state1", "state2"];
        fakeAllCheckedComboData = {1: "cd1", 2: "cd1", 3: "cd1", 4: "cd1"};
        fakeInitialAllCheckedComboData = {1: "cd1", 2: "cd1", 3: "cd1", 4: "cd1"};
        fakeComboDataDefaultSet = ["cd1", "cd2"];
        fakeNewComptCheckedVals = ["cd1", "cd2"];
        fakeAllComboData = {1: "cd1", 2: "cd2"};
        fakeNewComptLabels = {"5": "lbl5", "6": "lbl6"};
        fakeInitialStateId = 2;
        fakeAllPackets = {"1": {id: "1", stateId: "1"}, "2": {id: "2", stateId: "2"}, "3": {id: "3", stateId: "2"}};
        fakeNewPackets = {"3": {id: "3", stateId: "2"}};
    };

    var buildSpiesOnMockExchangeService = function () {
        spyOn(mockExchangeService, 'getSelectedPacketId').and.returnValue(fakeSelectedPktId);
        spyOn(mockExchangeService, 'deleteComptIdsToUpdate');
        spyOn(mockExchangeService, 'deleteNewComptLabels');
        spyOn(mockExchangeService, 'getComptIdsToDelete').and.returnValue(fakeComptIdsToDelete);
        spyOn(mockExchangeService, 'getComptIdsToUpdate').and.returnValue(fakeComptIdsToUpdate);
        spyOn(mockExchangeService, 'getNewComptLabels').and.returnValue(fakeNewComptLabels);
        spyOn(mockExchangeService, 'getSelectedPacketIsEmpty').and.returnValue(fakeSelectedPktIsEmpty);
        spyOn(mockExchangeService, 'setComptIdToInd');
        spyOn(mockExchangeService, 'getComptIdToInd');
        spyOn(mockExchangeService, 'getMaximalComptId');
        spyOn(mockExchangeService, 'setMaximalComptId');
        spyOn(mockExchangeService, 'getSelectedPacket').and.returnValue(fakeSelectedPkt);
        spyOn(mockExchangeService, 'getSelectedPage').and.returnValue(fakeSelectedPage);
        spyOn(mockExchangeService, 'getLoadedNoPackets').and.returnValue(fakeLoadedNoPackets);
        spyOn(mockExchangeService, 'getLoadedNoSelectedPacket').and.returnValue(fakeLoadedNoSelectedPacket);
        spyOn(mockExchangeService, 'setSelectedComptLabels');
        spyOn(mockExchangeService, 'pushToSelectedCompts');
        spyOn(mockExchangeService, 'setComptIdsToUpdate');
        spyOn(mockExchangeService, 'setComptIdsToDelete');
        spyOn(mockExchangeService, 'pushToComptIdsToDelete');
        spyOn(mockExchangeService, 'setNewComptLabels');
        spyOn(mockExchangeService, 'setAllComboData');
        spyOn(mockExchangeService, 'setAllCheckedComboData');
        spyOn(mockExchangeService, 'getAllStatesLength');
        spyOn(mockExchangeService, 'deleteSelectedComptLabels');
    };

    var buildController = function ($controller, $rootScope, $http) {
        mockScope = $rootScope.$new();
        controller = $controller("comptsPanelCtrl", {
            $scope: mockScope,
            $http: $http,
            exchangeService: mockExchangeService
        });
    }
});