/**
 * Created by alexc_000 on 2016-10-12.
 */
describe("Compts Panel Controller Test", function () {
    var mockExchangeService, controller, mockScope;
    var fakeComptIdsToUpdate, fakeComptIdsToDelete, fakeAllStates, fakeAllStateLabels,
        fakeNewComptLabels, fakeSelectedCompts, fakeSelectedPkt, fakeSelectedPage, fakeAllComboData,
        fakeLoadedNoPackets, fakeLoadedNoSelectedPacket, fakeInitialAllCheckedComboData, fakeComboDataDefaultSet,
        fakeNewComptCheckedVals, fakeSelectedPktIsEmpty, fakeAllCheckedComboData, fakeInitialStateId, fakeAllPackets,
        fakeNewPackets, fakeMinOrMaxValue, fakeNewComptId, fakeLabel, fakeUpperCaseLabel, fakeSelectedComptsLength,
        fakeAllStatesLength, fakeDeletedCompt, fakeDeletedComptId, fakeDeletedComptLabel, fakeSelectedPktId,
        fakeStateId;
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
            setSelectedCompt: function (value, label) {
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
            },
            getSelectedComptsLength: function () {
            }
        };
    }));

    beforeEach(inject(function ($controller, $rootScope, $http) {
        buildController($controller, $rootScope, $http)
    }));

    describe("Init fn test", function () {
        it("", (inject(function () {
            expect(mockScope.data).toBeDefined();
            expect(mockScope.data).not.toBeNull();
            expect(mockScope.data.pageSize).toEqual(packetListPageCount_);
            expect(mockScope.comptLabelMatchPattern).toEqual(new RegExp(formLabelPattern_));
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

        it('addComptLocally() proper work test with not-null newComptLabels', function () {
            var fakeNewComptLabel = fakeNewComptLabels["5"];
            mockScope.$broadcast('comboDataDefaultSet:update', fakeComboDataDefaultSet);
            mockScope.$broadcast('newComptCheckedVals:update', fakeNewComptCheckedVals);
            mockScope.data.newLabel = fakeNewComptLabel;
            var res = mockScope.addComptLocally();
            expect(mockExchangeService.getMaximalComptId).toHaveBeenCalledWith();
            expect(mockExchangeService.setMaximalComptId).toHaveBeenCalledWith(fakeNewComptId);
            expect(mockExchangeService.setSelectedComptLabels).toHaveBeenCalledWith(true,
                fakeNewComptLabel.toUpperCase());
            expect(mockExchangeService.getSelectedComptsLength).toHaveBeenCalledWith();
            expect(mockExchangeService.setComptIdToInd).toHaveBeenCalledWith(fakeSelectedComptsLength, fakeNewComptId);
            expect(mockExchangeService.pushToSelectedCompts).toHaveBeenCalledWith({
                id: fakeNewComptId,
                label: fakeNewComptLabel
            });
            expect(mockExchangeService.getSelectedPacketId).toHaveBeenCalledWith();
            expect(mockExchangeService.getNewComptLabels).toHaveBeenCalledWith();
            expect(mockExchangeService.setNewComptLabels).toHaveBeenCalledWith(fakeNewComptLabels);
            expect(mockExchangeService.getNewComptLabels).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.setNewComptLabels).toHaveBeenCalledWith(fakeNewComptLabels, fakeSelectedPktId);
            expect(mockExchangeService.setNewComptLabels).toHaveBeenCalledWith(fakeNewComptLabels["5"],
                fakeSelectedPktId, fakeNewComptId);
            expect(mockExchangeService.setAllComboData).toHaveBeenCalledWith(false, {}, fakeNewComptId);
            expect(mockExchangeService.setAllCheckedComboData).toHaveBeenCalledWith(false, false, {}, fakeNewComptId);
            expect(mockExchangeService.getAllStatesLength).toHaveBeenCalledWith();
            for (var i = 1; i <= fakeAllStatesLength; i++) {
                expect(mockExchangeService.setAllComboData).toHaveBeenCalledWith(i === fakeAllStatesLength,
                    fakeComboDataDefaultSet, fakeNewComptId, i);
                expect(mockExchangeService.setAllCheckedComboData).toHaveBeenCalledWith(i === fakeAllStatesLength,
                    false, fakeNewComptCheckedVals[i - 1], fakeNewComptId, i);
            }
            expect(mockScope.data.newLabel).toBeNull();
        });
    });

    describe('addComptLocally() proper work test with null newComptLabels', function () {
        it('', function () {
            var fakeNewComptLabel = fakeNewComptLabels["5"];
            mockScope.$broadcast('newComptCheckedVals:update', fakeNewComptCheckedVals);
            buildSpiesReturnValues_NullComptIdsToUpdateAndNewComptLabels();
            buildSpiesOnMockExchangeService();
            mockScope.data.newLabel = fakeNewComptLabel;
            mockScope.addComptLocally();
            expect(mockExchangeService.getNewComptLabels).toHaveBeenCalledWith();
            expect(mockExchangeService.setNewComptLabels).toHaveBeenCalledWith({});
            expect(mockExchangeService.getNewComptLabels).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.setNewComptLabels).toHaveBeenCalledWith({}, fakeSelectedPktId);
        })
    });

    describe('deleteComptLocally() test', function () {
        it('deleteComptLocally() with not-null compts to update and not-null new compt labels proper work test',
            function () {
                buildSpiesReturnValues_fullStuff();
                buildSpiesOnMockExchangeService();
                mockScope.deleteComptLocally(fakeDeletedCompt);
                expect(mockExchangeService.getSelectedPacketId).toHaveBeenCalledWith();
                expect(mockExchangeService.deleteSelectedComptLabels).toHaveBeenCalledWith(
                    fakeDeletedComptLabel.toUpperCase());
                expect(mockExchangeService.getComptIdToInd).toHaveBeenCalledWith(fakeDeletedComptId);
                expect(mockExchangeService.setSelectedCompt).toHaveBeenCalledWith(fakeMinOrMaxValue, null,
                    mockScope.data.pageSize);
                expect(mockExchangeService.getComptIdsToUpdate).toHaveBeenCalledWith(fakeSelectedPkt.id);
                expect(mockExchangeService.deleteComptIdsToUpdate).toHaveBeenCalledWith(fakeSelectedPkt.id,
                    fakeDeletedComptId);
                expect(mockExchangeService.getNewComptLabels).toHaveBeenCalledWith(fakeSelectedPkt.id);
                expect(mockExchangeService.getNewComptLabels).toHaveBeenCalledWith(fakeSelectedPkt.id,
                    fakeDeletedComptId);
                expect(mockExchangeService.deleteNewComptLabels).toHaveBeenCalledWith(fakeSelectedPkt.id,
                    fakeDeletedComptId);
            });

        it('deleteComptLocally() with null compts to update and null new compt labels proper work test', function () {
            buildSpiesReturnValues_NullComptIdsToUpdateAndNewComptLabels();
            buildSpiesOnMockExchangeService();
            mockScope.deleteComptLocally(fakeDeletedCompt);
            expect(mockExchangeService.getComptIdsToUpdate).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.getNewComptLabels).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.getComptIdsToDelete).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.setComptIdsToDelete).toHaveBeenCalledWith(fakeComptIdsToDelete,
                fakeSelectedPktId);
            expect(mockExchangeService.pushToComptIdsToDelete).toHaveBeenCalledWith(fakeDeletedComptId,
                fakeSelectedPktId);
        });

        it('deleteComptLocally() with null compts to update and null new compt labels proper work test', function () {
            buildSpiesReturnValues_NullComptIdsToUpdateAndNewComptLabelsAndComptsToDelete();
            buildSpiesOnMockExchangeService();
            mockScope.deleteComptLocally(fakeDeletedCompt);
            expect(mockExchangeService.getComptIdsToUpdate).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.getNewComptLabels).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.getComptIdsToDelete).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.setComptIdsToDelete).toHaveBeenCalledWith([], fakeSelectedPktId);
            expect(mockExchangeService.pushToComptIdsToDelete).toHaveBeenCalledWith(fakeDeletedComptId, 
                fakeSelectedPktId);
        });
    });

    describe('updateComptLocally() test', function () {
        it('updateComptLocally() with not-null compts to update test', function () {
            buildSpiesReturnValues_fullStuff();
            buildSpiesOnMockExchangeService();
            mockScope.updateComptLocally(fakeDeletedCompt, fakeStateId);
            expect(mockExchangeService.getSelectedPacketId).toHaveBeenCalledWith();
            expect(mockExchangeService.getNewComptLabels).toHaveBeenCalledWith(fakeSelectedPktId,
                fakeDeletedComptId);
            expect(mockExchangeService.getNewComptLabels).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.deleteComptIdsToUpdate).not.toHaveBeenCalled();
            expect(mockExchangeService.getComptIdsToUpdate).not.toHaveBeenCalled();
        });

        it('updateComptLocally() with null compts to update and null new compt labels and empty initial ' +
            'checked combo data proper work test', function () {
            buildSpiesReturnValues_NullComptIdsToUpdateAndNewComptLabels();
            buildSpiesOnMockExchangeService();
            buildEmptyInitialAllCheckedComboData();
            mockScope.updateComptLocally(fakeDeletedCompt, fakeStateId);
            expect(mockExchangeService.getSelectedPacketId).toHaveBeenCalledWith();
            expect(mockExchangeService.getNewComptLabels).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.deleteComptIdsToUpdate).not.toHaveBeenCalled();
            expect(mockExchangeService.getComptIdsToUpdate).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.setComptIdsToUpdate).toHaveBeenCalledWith({}, fakeSelectedPktId);
            expect(mockExchangeService.setComptIdsToUpdate).toHaveBeenCalledWith(true, fakeSelectedPktId,
                fakeDeletedComptId);
        });

        it('updateComptLocally() with null compts to update and null new compt labels and not empty initial ' +
            'checked combo data  and not equal initial all checked combo data and checked combo data ' +
            'proper work test', function () {
            buildSpiesReturnValues_NullComptIdsToUpdateAndNewComptLabels();
            buildSpiesOnMockExchangeService();
            buildNotEmptyInitialAllCheckedComboDataAndNotEqualInitialAllCheckedComboDataAndAllCheckedComboData
            (fakeDeletedComptId, fakeStateId);
            mockScope.updateComptLocally(fakeDeletedCompt, fakeStateId);
            expect(mockExchangeService.getSelectedPacketId).toHaveBeenCalledWith();
            expect(mockExchangeService.getNewComptLabels).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.deleteComptIdsToUpdate).not.toHaveBeenCalled();
            expect(mockExchangeService.getComptIdsToUpdate).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.setComptIdsToUpdate).toHaveBeenCalledWith({}, fakeSelectedPktId);
            expect(mockExchangeService.setComptIdsToUpdate).toHaveBeenCalledWith(true, fakeSelectedPktId,
                fakeDeletedComptId);
        });

        it('updateComptLocally() with null compts to update and null new compt labels and not empty initial ' +
            'checked combo data  and not equal initial all checked combo data and checked combo data ' +
            'proper work test', function () {
            buildSpiesReturnValues_NullComptIdsToUpdateAndNewComptLabels();
            buildSpiesOnMockExchangeService();
            buildEqualInitialAllCheckedComboDataAndAllCheckedComboData(fakeDeletedComptId, fakeStateId);
            mockScope.updateComptLocally(fakeDeletedCompt, fakeStateId);
            expect(mockExchangeService.getSelectedPacketId).toHaveBeenCalledWith();
            expect(mockExchangeService.getNewComptLabels).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.deleteComptIdsToUpdate).toHaveBeenCalledWith
            (fakeSelectedPktId, fakeDeletedComptId);
            expect(mockExchangeService.getComptIdsToUpdate).not.toHaveBeenCalled();
            expect(mockExchangeService.setComptIdsToUpdate).not.toHaveBeenCalled();
        });

        it('updateComptLocally() with not-null compts to update and null new compt labels and not empty initial ' +
            'checked combo data  and not equal initial all checked combo data and checked combo data ' +
            'proper work test', function () {
            buildSpiesReturnValues_NullNewComptLabels();
            buildSpiesOnMockExchangeService();
            buildNotEmptyInitialAllCheckedComboDataAndNotEqualInitialAllCheckedComboDataAndAllCheckedComboData
            (fakeDeletedComptId, fakeStateId);
            mockScope.updateComptLocally(fakeDeletedCompt, fakeStateId);
            expect(mockExchangeService.getSelectedPacketId).toHaveBeenCalledWith();
            expect(mockExchangeService.getNewComptLabels).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.deleteComptIdsToUpdate).not.toHaveBeenCalled();
            expect(mockExchangeService.getComptIdsToUpdate).toHaveBeenCalledWith(fakeSelectedPktId);
            expect(mockExchangeService.setComptIdsToUpdate).not.toHaveBeenCalledWith({}, fakeSelectedPktId);
            expect(mockExchangeService.setComptIdsToUpdate).toHaveBeenCalledWith(true, fakeSelectedPktId,
                fakeDeletedComptId);
        });
    });

    describe("getError() test", function () {
        beforeEach(inject(function ($controller, $rootScope, $http) {
            buildController($controller, $rootScope, $http);
        }));

        it('', function () {
            var required = "required";
            var maxlength = "maxlength";
            var blacklist = "blacklist";
            var pattern = "pattern";

            var input = {};
            expect(mockScope.getError(input, required)).toBeNull();

            input = {$error: {}};
            expect(mockScope.getError(input, required)).toBeNull();
            input = {$error: {required: true}};
            expect(mockScope.getError(input, required)).toBeNull();
            input = {$error: {required: true}, $dirty: true};
            expect(mockScope.getError(input, required)).toBe("You did not enter a label");

            input = {$error: {}};
            expect(mockScope.getError(input, maxlength)).toBeNull();
            input = {$error: {maxlength: true}};
            expect(mockScope.getError(input, maxlength)).toBe("Label is too long");

            input = {$error: {}};
            expect(mockScope.getError(input, blacklist)).toBeNull();
            input = {$error: {blacklist: true}};
            expect(mockScope.getError(input, blacklist)).toBe("Label is not unique");

            input = {$error: {}};
            expect(mockScope.getError(input, pattern)).toBeNull();
            input = {$error: {pattern: true}};
            expect(mockScope.getError(input, pattern)).toBe("Label should contain latin letters, digits, underscore and spaces only");
        });
    });

    var buildSpiesReturnValues_fullStuff = function () {
        fakeSelectedCompts = [{id: 3, packetId: 1, label: "lbl3"}, {id: 4, packetId: 1, label: "lbl4"},
            {id: 5, packetId: 1, label: "lbl5"}, {id: 6, packetId: 1, label: "lbl6"}];
        fakeDeletedCompt = fakeSelectedCompts[0];
        fakeDeletedComptId = fakeDeletedCompt.id;
        fakeDeletedComptLabel = fakeDeletedCompt.label;
        fakeSelectedComptsLength = fakeSelectedCompts.length;
        fakeSelectedPkt = {id: "1", stateId: "1"};
        fakeSelectedPktId = fakeSelectedPkt.id;
        fakeSelectedPktIsEmpty = false;
        fakeLoadedNoPackets = false;
        fakeLoadedNoSelectedPacket = false;
        fakeSelectedPage = 1;
        fakeComptIdsToDelete = [1, 2];
        fakeComptIdsToUpdate = {"3": "true", "4": "true"};
        fakeAllStates = [{id: "1", label: "state1"}, {id: "2", label: "state2"}];
        fakeAllStatesLength = fakeAllStates.length;
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
        fakeMinOrMaxValue = 1;
        fakeNewComptId = fakeMinOrMaxValue + 1;
        fakeLabel = 'a';
        fakeUpperCaseLabel = 'A';
        fakeStateId = fakeAllStates[0].id;
    };

    var buildSpiesReturnValues_NullComptIdsToUpdateAndNewComptLabels = function () {
        buildSpiesReturnValues_fullStuff();
        fakeComptIdsToUpdate = null;
        fakeNewComptLabels = null;
    };

    var buildSpiesReturnValues_NullNewComptLabels = function () {
        buildSpiesReturnValues_fullStuff();
        fakeNewComptLabels = null;
    };

    var buildSpiesReturnValues_NullComptIdsToUpdateAndNewComptLabelsAndComptsToDelete = function () {
        buildSpiesReturnValues_fullStuff();
        fakeComptIdsToDelete = null;
        fakeComptIdsToUpdate = null;
        fakeNewComptLabels = null;
    };

    var buildEmptyInitialAllCheckedComboData = function () {
        mockScope.data.initialAllCheckedComboData = {};
    };

    var buildNotEmptyInitialAllCheckedComboDataAndNotEqualInitialAllCheckedComboDataAndAllCheckedComboData =
        function (comptId, stateId) {
            mockScope.data.initialAllCheckedComboData = {};
            mockScope.data.initialAllCheckedComboData[comptId] = fakeInitialAllCheckedComboData;
            mockScope.data.initialAllCheckedComboData[comptId][stateId] = fakeInitialAllCheckedComboData;
            mockScope.data.allCheckedComboData = {};
            mockScope.data.allCheckedComboData[comptId] = {};
            mockScope.data.allCheckedComboData[comptId][stateId] = {};
        };

    var buildEqualInitialAllCheckedComboDataAndAllCheckedComboData = function (comptId, stateId) {
        mockScope.data.initialAllCheckedComboData = {};
        mockScope.data.initialAllCheckedComboData[comptId] = {};
        mockScope.data.initialAllCheckedComboData[comptId][stateId] = fakeInitialAllCheckedComboData;
        mockScope.data.allCheckedComboData = {};
        mockScope.data.allCheckedComboData[comptId] = {};
        mockScope.data.allCheckedComboData[comptId][stateId] = fakeInitialAllCheckedComboData;
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
        spyOn(mockExchangeService, 'getComptIdToInd').and.returnValue(fakeMinOrMaxValue);
        spyOn(mockExchangeService, 'getMaximalComptId').and.returnValue(fakeMinOrMaxValue);
        spyOn(mockExchangeService, 'setMaximalComptId');
        spyOn(mockExchangeService, 'getSelectedPacket').and.returnValue(fakeSelectedPkt);
        spyOn(mockExchangeService, 'getSelectedPage').and.returnValue(fakeSelectedPage);
        spyOn(mockExchangeService, 'getLoadedNoPackets').and.returnValue(fakeLoadedNoPackets);
        spyOn(mockExchangeService, 'getLoadedNoSelectedPacket').and.returnValue(fakeLoadedNoSelectedPacket);
        spyOn(mockExchangeService, 'setSelectedComptLabels');
        spyOn(mockExchangeService, 'setSelectedCompt');
        spyOn(mockExchangeService, 'pushToSelectedCompts');
        spyOn(mockExchangeService, 'setComptIdsToUpdate');
        spyOn(mockExchangeService, 'setComptIdsToDelete');
        spyOn(mockExchangeService, 'pushToComptIdsToDelete');
        spyOn(mockExchangeService, 'setNewComptLabels');
        spyOn(mockExchangeService, 'setAllComboData');
        spyOn(mockExchangeService, 'setAllCheckedComboData');
        spyOn(mockExchangeService, 'getAllStatesLength').and.returnValue(fakeAllStatesLength);
        spyOn(mockExchangeService, 'deleteSelectedComptLabels');
        spyOn(mockExchangeService, 'getSelectedComptsLength').and.returnValue(fakeSelectedComptsLength);
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