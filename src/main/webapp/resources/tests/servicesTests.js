/*
 * Copyright (c) 2016.  Alex Chulkin
 */

'use strict';

/**
 * The Exchange and Utils services tests
 */

describe("Exchange and Helper Services Test", function () {

    beforeEach(function () {
        module("packetAdminApp");
    });

    it("Exchangeservice Init() test", function () {
        inject(function (exchangeService) {
            exchangeService.init();
            expect(exchangeService.getComptIdToInd()).toEqual({});
            expect(exchangeService.getLoadError()).toBeNull();
            expect(exchangeService.getLoadedNoStates()).toBeNull();
            expect(exchangeService.getLoadedNoComboData()).toBeNull();
            expect(exchangeService.getMaximalPacketId()).toBeNull();
            expect(exchangeService.getPacketIdToInd()).toEqual({});
            expect(exchangeService.getMaximalPacketIndex()).toBeNull({});
            expect(exchangeService.getAllPackets()).toEqual({});
            expect(exchangeService.getNewPackets()).toEqual({});
            expect(exchangeService.getLoadEmpty()).toBeNull();
            expect(exchangeService.getNewComptLabels()).toEqual({true: {}, false: {}});
            expect(exchangeService.getComptIdsToDelete()).toEqual({});
            expect(exchangeService.getComptIdsToUpdate()).toEqual({});
            expect(exchangeService.getAllStates()).toEqual([]);
            expect(exchangeService.getAllStateLabels()).toEqual([]);
            expect(exchangeService.getAllComboData()).toEqual({});
            expect(exchangeService.getAllCheckedComboData()).toEqual({});
            expect(exchangeService.getPacketInitialStateIds()).toEqual({});
            expect(exchangeService.getCompts()).toEqual([]);
            expect(exchangeService.getSelectedPacket()).toBeNull();
            expect(exchangeService.getSelectedPage()).toBeNull();
            expect(exchangeService.getLoadedNoPacket()).toEqual({});
            expect(exchangeService.getLoadedNoPackets()).toBeNull();
            expect(exchangeService.getMaximalComptId()).toBeNull();
            expect(exchangeService.getPacketIsAlreadySelectedAtLeastOnce()).toBeNull();
            expect(exchangeService.getComboDataDefaultSet()).toEqual([]);
            expect(exchangeService.getNewComptCheckedVals()).toEqual([]);
            expect(exchangeService.getComptLabels()).toEqual({});
        });
    });

    describe('Test mockExchangeService functions', function () {
        var mockExchangeService;
        var mockUtilsService;
        var mockScope;
        var selPkt = {id: "1", stateId: "1"};
        var selPktInd = 1;
        var oldPkt = {id: "2", stateId: "2"};
        var oldPktInd = 2;
        var selCompts = [{id: 1, packetId: 1, label: "lbl1"}, {id: 2, packetId: 1, label: "lbl2"}];
        var oldCompts = [{id: 3, packetId: 2, label: "lbl3"}, {id: 4, packetId: 2, label: "lbl4"}];
        var selComptLabels = {"1": "lbl1", "2": "lbl2"};
        var oldComptLabels = {"3": "lbl3", "4": "lbl4"};

        beforeEach(inject(function ($rootScope, exchangeService, utilsService) {
            mockScope = $rootScope;
            mockUtilsService = utilsService;
            mockExchangeService = exchangeService;
            mockExchangeService.init();
            prepareSpies();
        }));

        describe('setSelectedPacket() test not-null not empty or having unsaved new compts pkt being selected, ' +
            'null pkt being unselected', function () {
            beforeEach(inject(function () {
                prepareSelPkt();
            }));

            it('', function () {
                mockExchangeService.setSelectedPacket(selPkt);
                expect(mockExchangeService.getSelectedPacket()).toEqual(selPkt);
                expect(mockExchangeService.getSelectedPacketId()).toEqual(selPkt.id);
                expect(mockExchangeService.getPacketIsAlreadySelectedAtLeastOnce()).toBeTruthy();
                expect(mockExchangeService.getSelectedCompts()).toEqual(selCompts);
                expect(mockExchangeService.getSelectedComptLabels()).toEqual(selComptLabels);
                expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedCompts:update', selCompts);
                expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedPacket:update', selPkt);
            });
        });

        describe('setSelectedPacket() test not-null empty and having unsaved new compts pkt being selected, ' +
            'null pkt being unselected', function () {
            it('', function () {
                mockExchangeService.setSelectedPacket(selPkt);
                expect(mockExchangeService.getSelectedPacket()).toEqual(selPkt);
                expect(mockExchangeService.getSelectedPacketId()).toEqual(selPkt.id);
                expect(mockExchangeService.getPacketIsAlreadySelectedAtLeastOnce()).toBeTruthy();
                expect(mockExchangeService.getSelectedCompts()).toEqual([]);
                expect(mockExchangeService.getSelectedComptLabels()).toEqual({});
                expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedCompts:update', []);
                expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedPacket:update', selPkt);
            });
        });

        describe('setSelectedPacket() test not-null not empty or having unsaved new compts pkt being selected,' +
            ' undefined pkt being unselected', function () {
            beforeEach(inject(function () {
                mockExchangeService.setSelectedPacket(undefined);
                prepareSelPkt();
            }));

            it('', function () {
                mockExchangeService.setSelectedPacket(selPkt);
                expect(mockExchangeService.getSelectedPacket()).toEqual(selPkt);
                expect(mockExchangeService.getSelectedPacketId()).toEqual(selPkt.id);
                expect(mockExchangeService.getPacketIsAlreadySelectedAtLeastOnce()).toBeTruthy();
                expect(mockExchangeService.getSelectedCompts()).toEqual(selCompts);
                expect(mockExchangeService.getSelectedComptLabels()).toEqual(selComptLabels);
                expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedCompts:update', selCompts);
                expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedPacket:update', selPkt);
            });
        });

        describe('setSelectedPacket() test not-null empty and having unsaved new compts pkt being selected, ' +
            'undefined pkt being unselected', function () {
            beforeEach(inject(function () {
                mockExchangeService.setSelectedPacket(undefined);
            }));

            it('', function () {
                mockExchangeService.setSelectedPacket(selPkt);
                expect(mockExchangeService.getSelectedPacket()).toEqual(selPkt);
                expect(mockExchangeService.getSelectedPacketId()).toEqual(selPkt.id);
                expect(mockExchangeService.getPacketIsAlreadySelectedAtLeastOnce()).toBeTruthy();
                expect(mockExchangeService.getSelectedCompts()).toEqual([]);
                expect(mockExchangeService.getSelectedComptLabels()).toEqual({});
                expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedCompts:update', []);
                expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedPacket:update', selPkt);
            });
        });

        describe('setSelectedPacket() test not equal not null not empty or having unsaved new compts pkt being' +
            ' selected and not-null pkt being unselected', function () {
            beforeEach(inject(function () {
                prepareOldPkt();
                prepareSelPkt();
            }));

            it('', function () {
                mockExchangeService.setSelectedPacket(selPkt);
                expect(mockExchangeService.getCompts(oldPktInd)).toEqual(oldCompts);
                expect(mockExchangeService.getComptLabels(oldPkt.id)).toEqual(oldComptLabels);

                expect(mockExchangeService.getSelectedPacket()).toEqual(selPkt);
                expect(mockExchangeService.getSelectedPacketId()).toEqual(selPkt.id);
                expect(mockExchangeService.getPacketIsAlreadySelectedAtLeastOnce()).toBeTruthy();
                expect(mockExchangeService.getSelectedCompts()).toEqual(selCompts);
                expect(mockExchangeService.getSelectedComptLabels()).toEqual(selComptLabels);
                expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedCompts:update', selCompts);
                expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedPacket:update', selPkt);
            });
        });

        describe('setSelectedPacket() test not equal not null empty and not having unsaved new compts pkt being' +
            ' selected and not-null pkt being unselected', function () {
            beforeEach(inject(function () {
                prepareOldPkt();
            }));

            it('', function () {
                mockExchangeService.setSelectedPacket(selPkt);
                expect(mockExchangeService.getCompts(oldPktInd)).toEqual(oldCompts);
                expect(mockExchangeService.getComptLabels(oldPkt.id)).toEqual(oldComptLabels);

                expect(mockExchangeService.getSelectedPacket()).toEqual(selPkt);
                expect(mockExchangeService.getSelectedPacketId()).toEqual(selPkt.id);
                expect(mockExchangeService.getPacketIsAlreadySelectedAtLeastOnce()).toBeTruthy();
                expect(mockExchangeService.getSelectedCompts()).toEqual([]);
                expect(mockExchangeService.getSelectedComptLabels()).toEqual({});
                expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedCompts:update', []);
                expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedPacket:update', selPkt);
            });
        });

        describe('setSelectedPacket() test null packet being selected', function () {
            it('', function () {
                mockExchangeService.setSelectedPacket(null);
                expect(mockExchangeService.getSelectedPacketId()).toBeNull();
                expect(mockExchangeService.getSelectedCompts()).toEqual([]);
                expect(mockExchangeService.getSelectedComptLabels()).toEqual({});
                expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedCompts:update', []);
                expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedPacket:update', null);
            });
        });

        describe('setSelectedPacket() test not-null not-empty or having unsaved new compts pkt being reselected',
            function () {
                beforeEach(inject(function () {
                    mockExchangeService.setSelectedPacket(selPkt);
                    prepareSelPkt();
                }));

                it('', function () {
                    mockExchangeService.setSelectedPacket(selPkt);
                    expect(mockExchangeService.getSelectedCompts()).toEqual(selCompts);
                    expect(mockExchangeService.getSelectedComptLabels()).toEqual(selComptLabels);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedCompts:update', selCompts);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedPacket:update', selPkt);
                });
            });

        describe('setSelectedPacket() test not-null empty and not having unsaved new compts pkt being reselected',
            function () {
                beforeEach(inject(function () {
                    mockExchangeService.setSelectedPacket(selPkt);
                }));

                it('', function () {
                    mockExchangeService.setSelectedPacket(selPkt);
                    expect(mockExchangeService.getSelectedCompts()).toEqual([]);
                    expect(mockExchangeService.getSelectedComptLabels()).toEqual({});
                    expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedCompts:update', []);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedPacket:update', selPkt);
                });
            });

        describe('getSelectedPacket() test',
            function () {
                it('', function () {
                    mockExchangeService.setSelectedPacket(selPkt);
                    expect(mockExchangeService.getSelectedPacket()).toEqual(selPkt);
                    mockExchangeService.setSelectedPacket(null);
                    expect(mockExchangeService.getSelectedPacket()).toBeNull();
                    mockExchangeService.setSelectedPacket(undefined);
                    expect(mockExchangeService.getSelectedPacket()).toBeUndefined();
                });
            });

        describe('setLoadError() and getLoadError() test',
            function () {
                it('', function () {
                    mockExchangeService.setLoadError(true);
                    expect(mockExchangeService.getLoadError()).toBeTruthy();
                    mockExchangeService.setLoadError(false);
                    expect(mockExchangeService.getLoadError()).toBeFalsy();
                    mockExchangeService.setLoadError(null);
                    expect(mockExchangeService.getLoadError()).toBeNull();
                });
            });

        describe('setLoadedNoStates() and getLoadedNoStates() test',
            function () {
                it('', function () {
                    mockExchangeService.setLoadedNoStates(true);
                    expect(mockExchangeService.getLoadedNoStates()).toBeTruthy();
                    mockExchangeService.setLoadedNoStates(false);
                    expect(mockExchangeService.getLoadedNoStates()).toBeFalsy();
                    mockExchangeService.setLoadedNoStates(null);
                    expect(mockExchangeService.getLoadedNoStates()).toBeNull();
                });
            });

        describe('setLoadedNoComboData() and getLoadedNoComboData() test',
            function () {
                it('', function () {
                    mockExchangeService.setLoadedNoComboData(true);
                    expect(mockExchangeService.getLoadedNoComboData()).toBeTruthy();
                    mockExchangeService.setLoadedNoComboData(false);
                    expect(mockExchangeService.getLoadedNoComboData()).toBeFalsy();
                    mockExchangeService.setLoadedNoComboData(null);
                    expect(mockExchangeService.getLoadedNoComboData()).toBeNull();
                });
            });

        describe('setMaximalPacketId() and getMaximalPacketId() test',
            function () {
                it('', function () {
                    mockExchangeService.setMaximalPacketId(1);
                    expect(mockExchangeService.getMaximalPacketId()).toBe(1);
                    mockExchangeService.setMaximalPacketId(undefined);
                    expect(mockExchangeService.getMaximalPacketId()).toBeUndefined();
                    mockExchangeService.setMaximalPacketId(null);
                    expect(mockExchangeService.getMaximalPacketId()).toBeNull();
                });
            });

        describe('setMaximalPacketIndex() and getMaximalPacketIndex() test',
            function () {
                it('', function () {
                    mockExchangeService.setMaximalPacketIndex(1);
                    expect(mockExchangeService.getMaximalPacketIndex()).toBe(1);
                    mockExchangeService.setMaximalPacketIndex(undefined);
                    expect(mockExchangeService.getMaximalPacketIndex()).toBeUndefined();
                    mockExchangeService.setMaximalPacketIndex(null);
                    expect(mockExchangeService.getMaximalPacketIndex()).toBeNull();
                });
            });

        describe('setLoadEmpty() and getLoadEmpty() test',
            function () {
                it('', function () {
                    mockExchangeService.setLoadEmpty(true);
                    expect(mockExchangeService.getLoadEmpty()).toBeTruthy();
                    mockExchangeService.setLoadEmpty(false);
                    expect(mockExchangeService.getLoadEmpty()).toBeFalsy();
                    mockExchangeService.setLoadEmpty(null);
                    expect(mockExchangeService.getLoadEmpty()).toBeNull();
                });
            });

        describe('setSelectedPacketId() and getSelectedPacketId() test',
            function () {
                it('', function () {
                    mockExchangeService.setSelectedPacketId(1);
                    expect(mockExchangeService.getSelectedPacketId()).toBe(1);
                    mockExchangeService.setSelectedPacketId(undefined);
                    expect(mockExchangeService.getSelectedPacketId()).toBeUndefined();
                    mockExchangeService.setSelectedPacketId(null);
                    expect(mockExchangeService.getSelectedPacketId()).toBeNull();
                });
            });

        describe('setSelectedPage() and getSelectedPage() test',
            function () {
                it('', function () {
                    mockExchangeService.setSelectedPage(1);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedPage:update', 1);
                    expect(mockExchangeService.getSelectedPage()).toBe(1);
                    mockExchangeService.setSelectedPage(undefined);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedPage:update', undefined);
                    expect(mockExchangeService.getSelectedPage()).toBeUndefined();
                    mockExchangeService.setSelectedPage(null);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith('selectedPage:update', null);
                    expect(mockExchangeService.getSelectedPage()).toBeNull();
                });
            });

        describe('setLoadedNoPackets() and getLoadedNoPackets() test',
            function () {
                it('', function () {
                    mockExchangeService.setLoadedNoPackets(true);
                    expect(mockExchangeService.getLoadedNoPackets()).toBeTruthy();
                    mockExchangeService.setLoadedNoPackets(false);
                    expect(mockExchangeService.getLoadedNoPackets()).toBeFalsy();
                    mockExchangeService.setLoadedNoPackets(null);
                    expect(mockExchangeService.getLoadedNoPackets()).toBeNull();
                });
            });

        describe('setMaximalComptId() and getMaximalComptId() test',
            function () {
                it('', function () {
                    mockExchangeService.setMaximalComptId(1);
                    expect(mockExchangeService.getMaximalComptId()).toBe(1);
                    mockExchangeService.setMaximalComptId(undefined);
                    expect(mockExchangeService.getMaximalComptId()).toBeUndefined();
                    mockExchangeService.setMaximalComptId(null);
                    expect(mockExchangeService.getMaximalComptId()).toBeNull();
                });
            });

        describe('getLoadedNoSelectedPacket() and setLoadedNoSelectedPacket() test',
            function () {
                it('', function () {
                    mockExchangeService.setSelectedPacketId(1);
                    mockExchangeService.setLoadedNoSelectedPacket(true);
                    expect(mockExchangeService.getLoadedNoSelectedPacket()).toBeTruthy();
                    mockExchangeService.setLoadedNoSelectedPacket(false);
                    expect(mockExchangeService.getLoadedNoSelectedPacket()).toBeFalsy();
                    mockExchangeService.setLoadedNoSelectedPacket(null);
                    expect(mockExchangeService.getLoadedNoSelectedPacket()).toBeNull();
                    mockExchangeService.setSelectedPacketId(undefined);
                    mockExchangeService.setLoadedNoSelectedPacket(true);
                    expect(mockExchangeService.getLoadedNoSelectedPacket()).toBeFalsy();
                    mockExchangeService.setSelectedPacketId(null);
                    mockExchangeService.setLoadedNoSelectedPacket(true);
                    expect(mockExchangeService.getLoadedNoSelectedPacket()).toBeFalsy();
                });
            });

        describe('setLoadedNoUnSelectedPacket() test',
            function () {
                it('', function () {
                    mockExchangeService.setLoadedNoUnSelectedPacket(true, 1);
                    expect(mockExchangeService.getLoadedNoPacket(1)).toBeTruthy();
                    mockExchangeService.setLoadedNoUnSelectedPacket(false, 1);
                    expect(mockExchangeService.getLoadedNoPacket(1)).toBeFalsy();
                    mockExchangeService.setLoadedNoUnSelectedPacket(null, 1);
                    expect(mockExchangeService.getLoadedNoPacket(1)).toBeNull();
                    mockExchangeService.setLoadedNoUnSelectedPacket(true, undefined);
                    expect(mockExchangeService.getLoadedNoPacket(undefined)).toEqual({1: null});
                });
            });

        describe('getPacketIsAlreadySelectedAtLeastOnce() and setPacketIsAlreadySelectedAtLeastOnce() test',
            function () {
                it('', function () {
                    mockExchangeService.setPacketIsAlreadySelectedAtLeastOnce(true);
                    expect(mockExchangeService.getPacketIsAlreadySelectedAtLeastOnce()).toBeTruthy();
                    mockExchangeService.setPacketIsAlreadySelectedAtLeastOnce(false);
                    expect(mockExchangeService.getPacketIsAlreadySelectedAtLeastOnce()).toBeFalsy();
                    mockExchangeService.setPacketIsAlreadySelectedAtLeastOnce(null);
                    expect(mockExchangeService.getPacketIsAlreadySelectedAtLeastOnce()).toBeNull();
                });
            });

        describe('getComptLabels() and setComptLabels() test',
            function () {
                it('', function () {
                    expect(mockExchangeService.getComptLabels()).toEqual({});
                    expect(mockExchangeService.getComptLabels(1)).toBeUndefined();
                    expect(mockExchangeService.getComptLabels(2)).toBeUndefined();
                    mockExchangeService.setComptLabels({}, 1);
                    expect(mockExchangeService.getComptLabels()).toEqual({1: {}});
                    expect(mockExchangeService.getComptLabels(1)).toEqual({});
                    expect(mockExchangeService.getComptLabels(2)).toBeUndefined();
                    mockExchangeService.setComptLabels({}, 1, 'lbl1');
                    expect(mockExchangeService.getComptLabels()).toEqual({1: {'lbl1': {}}});
                    expect(mockExchangeService.getComptLabels(1, 'lbl1')).toEqual({});
                    expect(mockExchangeService.getComptLabels(1, 'lbl2')).toBeUndefined();
                    expect(mockExchangeService.getComptLabels(1)).toEqual({'lbl1': {}});
                    expect(mockExchangeService.getComptLabels(2)).toBeUndefined();
                    mockExchangeService.setComptLabels({}, 2);
                    expect(mockExchangeService.getComptLabels()).toEqual({1: {'lbl1': {}}, 2: {}});
                    expect(mockExchangeService.getComptLabels(1)).toEqual({'lbl1': {}});
                    expect(mockExchangeService.getComptLabels(2)).toEqual({});
                    mockExchangeService.setComptLabels({}, 2, 'lbl2');
                    expect(mockExchangeService.getComptLabels()).toEqual({1: {'lbl1': {}}, 2: {'lbl2': {}}});
                    expect(mockExchangeService.getComptLabels(2, 'lbl2')).toEqual({});
                    expect(mockExchangeService.getComptLabels(2)).toEqual({'lbl2': {}});
                    mockExchangeService.setComptLabels({});
                    expect(mockExchangeService.getComptLabels()).toEqual({});
                    expect(mockExchangeService.getComptLabels(1)).toBeUndefined();
                    expect(mockExchangeService.getComptLabels(2)).toBeUndefined();
                    mockExchangeService.setComptLabels({}, 2, 'lbl2');
                    expect(mockExchangeService.getComptLabels()).toEqual({2: {'lbl2': {}}});
                    expect(mockExchangeService.getComptLabels(2, 'lbl2')).toEqual({});
                    expect(mockExchangeService.getComptLabels(2)).toEqual({'lbl2': {}});
                });
            });

        describe('getPacketIdToInd() and setPacketIdToInd() test',
            function () {
                it('', function () {
                    expect(mockExchangeService.getPacketIdToInd()).toEqual({});
                    expect(mockExchangeService.getPacketIdToInd(1)).toBeUndefined();
                    mockExchangeService.setPacketIdToInd(0, 1);
                    expect(mockExchangeService.getPacketIdToInd(1)).toBe(0);
                    expect(mockExchangeService.getPacketIdToInd(2)).toBeUndefined();
                    expect(mockExchangeService.getPacketIdToInd()).toEqual({1: 0});
                    mockExchangeService.setPacketIdToInd({2: 1});
                    expect(mockExchangeService.getPacketIdToInd(1)).toBeUndefined();
                    expect(mockExchangeService.getPacketIdToInd(2)).toBe(1);
                    expect(mockExchangeService.getPacketIdToInd()).toEqual({2: 1});
                });
            });

        describe('getComptIdToInd() and setComptIdToInd() test',
            function () {
                it('', function () {
                    expect(mockExchangeService.getComptIdToInd()).toEqual({});
                    expect(mockExchangeService.getComptIdToInd(1)).toBeUndefined();
                    mockExchangeService.setComptIdToInd(0, 1);
                    expect(mockExchangeService.getComptIdToInd(1)).toBe(0);
                    expect(mockExchangeService.getComptIdToInd(2)).toBeUndefined();
                    expect(mockExchangeService.getComptIdToInd()).toEqual({1: 0});
                    mockExchangeService.setComptIdToInd({2: 1});
                    expect(mockExchangeService.getComptIdToInd(1)).toBeUndefined();
                    expect(mockExchangeService.getComptIdToInd(2)).toBe(1);
                    expect(mockExchangeService.getComptIdToInd()).toEqual({2: 1});
                });
            });

        describe('getCompts(), pushToCompts(), getComptsLength() and initializeCompts() test',
            function () {
                it('', function () {
                    expect(mockExchangeService.getCompts()).toEqual([]);
                    expect(mockExchangeService.getCompts(0)).toBeUndefined();
                    mockExchangeService.initializeCompts(0);
                    expect(mockExchangeService.getCompts(0)).toEqual([]);
                    expect(mockExchangeService.getCompts(1)).toBeUndefined();
                    expect(mockExchangeService.getCompts()).toEqual([[]]);
                    mockExchangeService.initializeCompts(1);
                    expect(mockExchangeService.getCompts(0)).toEqual([]);
                    expect(mockExchangeService.getCompts(1)).toEqual([]);
                    expect(mockExchangeService.getCompts(2)).toBeUndefined();
                    expect(mockExchangeService.getCompts()).toEqual([[], []]);
                    mockExchangeService.pushToCompts(selCompts[0], 2);
                    expect(mockExchangeService.getCompts()).toEqual([[], [], [selCompts[0]]]);
                    expect(mockExchangeService.getCompts(2)).toEqual([selCompts[0]]);
                    mockExchangeService.pushToCompts(selCompts[1], 2);
                    expect(mockExchangeService.getCompts()).toEqual([[], [], [selCompts[0], selCompts[1]]]);
                    expect(mockExchangeService.getCompts(2)).toEqual([selCompts[0], selCompts[1]]);
                    expect(mockExchangeService.getComptsLength(2)).toEqual(2);
                    var lengthBeforePush = mockExchangeService.getCompts().length;
                    mockExchangeService.pushToCompts(selCompts);
                    expect(mockExchangeService.getCompts().length).toEqual(lengthBeforePush + 1);
                    expect(mockExchangeService.getCompts()).toEqual([[], [], [selCompts[0], selCompts[1]], selCompts]);
                    expect(mockExchangeService.getCompts(3)).toEqual(selCompts);
                    expect(mockExchangeService.getComptsLength(3)).toEqual(selCompts.length);
                    expect(mockExchangeService.getComptsLength(1)).toBe(0);
                    expect(mockExchangeService.getComptsLength(2)).toBe(2);
                    expect(mockExchangeService.getComptsLength(4)).toBe(0);
                    mockExchangeService.pushToCompts(selCompts[0], 4);
                    expect(mockExchangeService.getCompts()).toEqual([[], [], [selCompts[0], selCompts[1]], selCompts,
                        [selCompts[0]]]);
                    expect(mockExchangeService.getCompts(4)).toEqual([selCompts[0]]);
                });
            });

        describe('setAllPackets(), getAllPackets() and deleteAllPackets() test',
            function () {
                it('', function () {
                    expect(mockExchangeService.getAllPackets()).toEqual({});
                    expect(mockExchangeService.getAllPackets(1)).toBeUndefined();
                    mockExchangeService.setAllPackets(selPkt, 1);
                    expect(mockExchangeService.getLoadedNoPackets()).toBeFalsy();
                    expect(mockScope.$broadcast).toHaveBeenCalledWith('allPackets:update', {1: selPkt});
                    expect(mockExchangeService.getAllPackets()).toEqual({1: selPkt});
                    mockExchangeService.deleteAllPackets(2);
                    expect(mockExchangeService.getAllPackets()).toEqual({1: selPkt});
                    expect(mockScope.$broadcast.calls.count()).toEqual(1);
                    mockExchangeService.deleteAllPackets(1);
                    expect(mockExchangeService.getAllPackets()).toEqual({});
                    expect(mockExchangeService.getLoadedNoPackets()).toBeTruthy();
                    expect(mockScope.$broadcast.calls.count()).toEqual(2);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith('allPackets:update', {});
                    mockExchangeService.setAllPackets(selPkt);
                    expect(mockExchangeService.getLoadedNoPackets()).toBeFalsy();
                    expect(mockScope.$broadcast.calls.count()).toEqual(3);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith('allPackets:update', selPkt);
                });
            });

        describe('setPacketInitialStateIds() and getPacketInitialStateIds() test',
            function () {
                it('', function () {
                    expect(mockExchangeService.getPacketInitialStateIds(1)).toBeUndefined();
                    mockExchangeService.setPacketInitialStateIds(1, 1);
                    expect(mockExchangeService.getPacketInitialStateIds()).toEqual({1: 1});
                    expect(mockExchangeService.getPacketInitialStateIds(1)).toEqual(1);
                    expect(mockExchangeService.getPacketInitialStateIds(2)).toBeUndefined();
                    mockExchangeService.setPacketInitialStateIds(2, 2);
                    expect(mockExchangeService.getPacketInitialStateIds()).toEqual({1: 1, 2: 2});
                    expect(mockExchangeService.getPacketInitialStateIds(2)).toEqual(2);
                    mockExchangeService.setPacketInitialStateIds({3: 3, 4: 4});
                    expect(mockExchangeService.getPacketInitialStateIds()).toEqual({3: 3, 4: 4});
                    expect(mockExchangeService.getPacketInitialStateIds(1)).toBeUndefined();
                    expect(mockExchangeService.getPacketInitialStateIds(2)).toBeUndefined();
                    expect(mockExchangeService.getPacketInitialStateIds(3)).toEqual(3);
                    expect(mockExchangeService.getPacketInitialStateIds(4)).toEqual(4);
                });
            });

        describe('setAllStates(), getAllStates() and getAllStatesLength() test',
            function () {
                it('', function () {
                    mockExchangeService.setAllStates([{id: 1, label: "lbl1"}, {id: 2, label: "lbl2"}]);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith('allStates:update',
                        mockExchangeService.getAllStates());
                    expect(mockScope.$broadcast.calls.count()).toEqual(1);
                    expect(mockExchangeService.getAllStates()).toEqual([{id: 1, label: "lbl1"},
                        {id: 2, label: "lbl2"}]);
                    expect(mockExchangeService.getAllStatesLength()).toEqual(mockExchangeService.getAllStates().length);
                    mockExchangeService.setAllStates([{id: 3, label: "lbl3"}, {id: 4, label: "lbl4"},
                        {id: 5, label: "lbl5"}]);
                    expect(mockScope.$broadcast.calls.count()).toEqual(2);
                    expect(mockScope.$broadcast.calls.argsFor(1)).toEqual(['allStates:update',
                        mockExchangeService.getAllStates()]);
                    expect(mockExchangeService.getAllStates()).toEqual([{id: 3, label: "lbl3"}, {id: 4, label: "lbl4"},
                        {id: 5, label: "lbl5"}]);
                    expect(mockExchangeService.getAllStatesLength()).toEqual(mockExchangeService.getAllStates().length);
                });
            });

        describe('setComboDataDefaultSet() and getComboDataDefaultSet() test',
            function () {
                it('', function () {
                    mockExchangeService.setComboDataDefaultSet([{id: 1, label: "cd1"}, {id: 2, label: "cd2"}]);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith('comboDataDefaultSet:update',
                        mockExchangeService.getComboDataDefaultSet());
                    expect(mockScope.$broadcast.calls.count()).toEqual(1);
                    expect(mockExchangeService.getComboDataDefaultSet()).toEqual(["cd1", "cd2"]);
                    expect(mockExchangeService.getComboDataDefaultSet(0)).toEqual("cd1");
                    expect(mockExchangeService.getComboDataDefaultSet(1)).toEqual("cd2");
                    expect(mockExchangeService.getComboDataDefaultSet(2)).toBeUndefined();
                    mockExchangeService.setComboDataDefaultSet("cd3");
                    expect(mockScope.$broadcast.calls.count()).toEqual(1);
                    mockExchangeService.setComboDataDefaultSet([{id: 1, label: "cd3"}, {id: 2, label: "cd4"},
                        {id: 3, label: "cd5"}]);
                    expect(mockScope.$broadcast.calls.count()).toEqual(2);
                    expect(mockScope.$broadcast.calls.argsFor(1)).toEqual(['comboDataDefaultSet:update',
                        mockExchangeService.getComboDataDefaultSet()]);
                    expect(mockExchangeService.getComboDataDefaultSet()).toEqual(["cd3", "cd4", "cd5"]);
                    expect(mockExchangeService.getComboDataDefaultSet(0)).toEqual("cd3");
                    expect(mockExchangeService.getComboDataDefaultSet(1)).toEqual("cd4");
                    expect(mockExchangeService.getComboDataDefaultSet(2)).toEqual("cd5");
                    expect(mockExchangeService.getComboDataDefaultSet(3)).toBeUndefined();
                });
            });

        describe('setNewComptCheckedVals() and getNewComptCheckedVals() test',
            function () {
                it('', function () {
                    expect(mockExchangeService.getNewComptCheckedVals(1)).toBeUndefined();
                    mockExchangeService.setNewComptCheckedVals(["cd1", "cd2"]);
                    expect(mockExchangeService.getNewComptCheckedVals()).toEqual(["cd1", "cd2"]);
                    expect(mockExchangeService.getNewComptCheckedVals(0)).toEqual("cd1");
                    expect(mockExchangeService.getNewComptCheckedVals(1)).toEqual("cd2");
                    expect(mockExchangeService.getNewComptCheckedVals(2)).toBeUndefined();
                    mockExchangeService.setNewComptCheckedVals("cd2", 0);
                    expect(mockExchangeService.getNewComptCheckedVals()).toEqual(["cd2", "cd2"]);
                    expect(mockExchangeService.getNewComptCheckedVals(0)).toEqual("cd2");
                    expect(mockExchangeService.getNewComptCheckedVals(1)).toEqual("cd2");
                    expect(mockExchangeService.getNewComptCheckedVals(2)).toBeUndefined();
                    mockExchangeService.setNewComptCheckedVals(["cd1", "cd1"]);
                    expect(mockExchangeService.getNewComptCheckedVals()).toEqual(["cd1", "cd1"]);
                    expect(mockExchangeService.getNewComptCheckedVals(0)).toEqual("cd1");
                    expect(mockExchangeService.getNewComptCheckedVals(1)).toEqual("cd1");
                    expect(mockExchangeService.getNewComptCheckedVals(2)).toBeUndefined();
                });
            });

        describe('initializeNewComptCheckedVals() test',
            function () {
                it('', function () {
                    mockExchangeService.initializeNewComptCheckedVals();
                    expect(mockScope.$broadcast).not.toHaveBeenCalledWith();
                    expect(mockExchangeService.getNewComptCheckedVals().length).toBe(0);
                    mockExchangeService.setComboDataDefaultSet([{id: 1, label: "cd1"}, {id: 2, label: "cd2"}]);
                    mockExchangeService.initializeNewComptCheckedVals();
                    expect(mockScope.$broadcast).not.toHaveBeenCalledWith();
                    expect(mockExchangeService.getNewComptCheckedVals().length).toBe(0);
                    mockExchangeService.setAllStates([{id: 1, label: "lbl1"}, {id: 2, label: "lbl2"}]);
                    mockExchangeService.initializeNewComptCheckedVals();
                    expect(mockExchangeService.getNewComptCheckedVals()).toEqual(["cd1", "cd1"]);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith('newComptCheckedVals:update',
                        mockExchangeService.getNewComptCheckedVals());
                });
            });

        describe('setAllComboData(), getAllComboData() and pushToAllComboData() test',
            function () {
                it('', function () {
                    mockExchangeService.setAllComboData(false);
                    expect(mockScope.$broadcast).not.toHaveBeenCalled();
                    expect(mockExchangeService.getAllComboData()).toBeUndefined();
                    expect(mockScope.$broadcast).not.toHaveBeenCalled();

                    mockExchangeService.setAllComboData(false, {});
                    expect(mockScope.$broadcast).not.toHaveBeenCalled();
                    expect(mockExchangeService.getAllComboData()).toEqual({});
                    mockExchangeService.pushToAllComboData(true, "cd1", 1, 1);
                    expect(mockExchangeService.getAllComboData()).toEqual({1: {1: ["cd1"]}});
                    expect(mockScope.$broadcast).toHaveBeenCalledWith('allComboData:update', {1: {1: ["cd1"]}});
                    expect(mockScope.$broadcast.calls.count()).toEqual(1);

                    mockExchangeService.setAllComboData(false, {1: {}});
                    expect(mockScope.$broadcast.calls.count()).toEqual(1);
                    expect(mockExchangeService.getAllComboData()).toEqual({1: {}});
                    mockExchangeService.pushToAllComboData(true, "cd1", 1, 1);
                    expect(mockExchangeService.getAllComboData()).toEqual({1: {1: ["cd1"]}});
                    expect(mockScope.$broadcast).toHaveBeenCalledWith('allComboData:update', {1: {1: ["cd1"]}});
                    expect(mockScope.$broadcast.calls.count()).toEqual(2);

                    mockExchangeService.setAllComboData(false, {1: {1: ["cd2"]}});
                    expect(mockScope.$broadcast.calls.count()).toEqual(2);
                    expect(mockExchangeService.getAllComboData()).toEqual({1: {1: ["cd2"]}});
                    mockExchangeService.pushToAllComboData(true, "cd1", 1, 1);
                    expect(mockExchangeService.getAllComboData()).toEqual({1: {1: ["cd2", "cd1"]}});
                    expect(mockScope.$broadcast).toHaveBeenCalledWith('allComboData:update', {1: {1: ["cd2", "cd1"]}});
                    expect(mockScope.$broadcast.calls.count()).toEqual(3);

                    mockExchangeService.setAllComboData(false, {1: {2: ["cd2"]}});
                    expect(mockScope.$broadcast.calls.count()).toEqual(3);
                    expect(mockExchangeService.getAllComboData()).toEqual({1: {2: ["cd2"]}});
                    mockExchangeService.pushToAllComboData(true, "cd1", 1, 1);
                    expect(mockExchangeService.getAllComboData()).toEqual({1: {2: ["cd2"], 1: ["cd1"]}});
                    expect(mockScope.$broadcast)
                        .toHaveBeenCalledWith('allComboData:update', {1: {2: ["cd2"], 1: ["cd1"]}});
                    expect(mockScope.$broadcast.calls.count()).toEqual(4);

                    mockExchangeService.setAllComboData(false, {2: {2: ["cd2"]}});
                    expect(mockScope.$broadcast.calls.count()).toEqual(4);
                    expect(mockExchangeService.getAllComboData()).toEqual({2: {2: ["cd2"]}});
                    mockExchangeService.pushToAllComboData(true, "cd1", 1, 1);
                    expect(mockExchangeService.getAllComboData()).toEqual({2: {2: ["cd2"]}, 1: {1: ["cd1"]}});
                    expect(mockScope.$broadcast)
                        .toHaveBeenCalledWith('allComboData:update', {2: {2: ["cd2"]}, 1: {1: ["cd1"]}});
                    expect(mockScope.$broadcast.calls.count()).toEqual(5);

                    mockExchangeService.setAllComboData(false, {});
                    expect(mockScope.$broadcast.calls.count()).toEqual(5);
                    expect(mockExchangeService.getAllComboData()).toEqual({});
                    expect(mockExchangeService.getAllComboData(1)).toBeUndefined();
                    var allComboData = {
                        1: {1: ["cd1", "cd2"], 2: ["cd2", "cd1"]},
                        2: {1: ["cd1", "cd2"], 2: ["cd2", "cd1"]}
                    };
                    mockExchangeService.setAllComboData(true, allComboData);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith("allComboData:update", allComboData);
                    expect(mockExchangeService.getAllComboData()).toEqual(allComboData);
                    expect(mockExchangeService.getAllComboData(1)).toEqual({1: ["cd1", "cd2"], 2: ["cd2", "cd1"]});
                    expect(mockExchangeService.getAllComboData(1, 1)).toEqual(["cd1", "cd2"]);
                    mockExchangeService.setAllComboData(false, {1: ["cd1", "cd2"], 2: ["cd1", "cd2"]}, 1);
                    var allComboData2 = {
                        1: {1: ["cd1", "cd2"], 2: ["cd1", "cd2"]},
                        2: {1: ["cd1", "cd2"], 2: ["cd2", "cd1"]}
                    };
                    expect(mockScope.$broadcast.calls.count()).toEqual(6);
                    expect(mockExchangeService.getAllComboData()).toEqual(allComboData2);
                    expect(mockExchangeService.getAllComboData(1)).toEqual({1: ["cd1", "cd2"], 2: ["cd1", "cd2"]});
                    expect(mockExchangeService.getAllComboData(1, 2)).toEqual(["cd1", "cd2"]);
                    expect(mockExchangeService.getAllComboData(3)).toBeUndefined();
                    mockExchangeService.setAllComboData(true, {1: ["cd2", "cd1"], 2: ["cd1", "cd2"]}, 3);
                    var allComboData3 = {
                        1: {1: ["cd1", "cd2"], 2: ["cd1", "cd2"]},
                        2: {1: ["cd1", "cd2"], 2: ["cd2", "cd1"]},
                        3: {1: ["cd2", "cd1"], 2: ["cd1", "cd2"]}
                    };
                    expect(mockScope.$broadcast.calls.count()).toEqual(7);
                    expect(mockScope.$broadcast.calls.argsFor(6)).toEqual(['allComboData:update', allComboData3]);
                    expect(mockExchangeService.getAllComboData()).toEqual(allComboData3);
                    expect(mockExchangeService.getAllComboData(3)).toEqual({1: ["cd2", "cd1"], 2: ["cd1", "cd2"]});
                    expect(mockExchangeService.getAllComboData(3, 1)).toEqual(["cd2", "cd1"]);
                    expect(mockExchangeService.getAllComboData(3, 2)).toEqual(["cd1", "cd2"]);
                    mockExchangeService.setAllComboData(false, ["cd1", "cd1"], 4, 1);
                    var allComboData4 = {
                        1: {1: ["cd1", "cd2"], 2: ["cd1", "cd2"]},
                        2: {1: ["cd1", "cd2"], 2: ["cd2", "cd1"]},
                        3: {1: ["cd2", "cd1"], 2: ["cd1", "cd2"]},
                        4: {1: ["cd1", "cd1"]}
                    };
                    expect(mockScope.$broadcast.calls.count()).toEqual(7);
                    expect(mockExchangeService.getAllComboData()).toEqual(allComboData4);
                    expect(mockExchangeService.getAllComboData(4)).toEqual({1: ["cd1", "cd1"]});
                    expect(mockExchangeService.getAllComboData(4, 1)).toEqual(["cd1", "cd1"]);
                    expect(mockExchangeService.getAllComboData(4, 2)).toBeUndefined();
                });
            });

        describe('setAllCheckedComboData() and getAllCheckedComboData() test',
            function () {
                it('', function () {
                    mockExchangeService.setAllCheckedComboData(false, false, {});
                    expect(mockScope.$broadcast.calls.count()).toEqual(0);
                    expect(mockExchangeService.getAllCheckedComboData()).toEqual({});
                    expect(mockExchangeService.getAllCheckedComboData(1)).toBeUndefined();
                    expect(mockExchangeService.getInitialAllCheckedComboData()).toEqual({});

                    var allCheckedComboData = {
                        1: {1: "cd1", 2: "cd2"},
                        2: {1: "cd1", 2: "cd2"}
                    };
                    mockExchangeService.setAllCheckedComboData(true, true, allCheckedComboData);
                    expect(mockScope.$broadcast.calls.count()).toEqual(2);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith("allCheckedComboData:update",
                        allCheckedComboData);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith("initialAllCheckedComboData:update",
                        allCheckedComboData);
                    expect(mockExchangeService.getInitialAllCheckedComboData()).toEqual(allCheckedComboData);
                    expect(mockExchangeService.getAllCheckedComboData()).toEqual(allCheckedComboData);
                    expect(mockExchangeService.getAllCheckedComboData(1)).toEqual({1: "cd1", 2: "cd2"});
                    expect(mockExchangeService.getAllCheckedComboData(1, 1)).toEqual("cd1");
                    var oldAllCheckedComboData = angular.copy(allCheckedComboData);
                    allCheckedComboData[1][1] = "cd2";
                    expect(mockExchangeService.getInitialAllCheckedComboData()).toEqual(oldAllCheckedComboData);

                    mockExchangeService.setAllCheckedComboData(false, true, {1: "cd1", 2: "cd1"}, 1);
                    var allCheckedComboData2 = {
                        1: {1: "cd1", 2: "cd1"},
                        2: {1: "cd1", 2: "cd2"}
                    };
                    expect(mockScope.$broadcast.calls.count()).toEqual(2);
                    expect(mockExchangeService.getInitialAllCheckedComboData()).toEqual(allCheckedComboData2);
                    expect(mockExchangeService.getAllCheckedComboData()).toEqual(allCheckedComboData2);
                    expect(mockExchangeService.getAllCheckedComboData(1)).toEqual({1: "cd1", 2: "cd1"});
                    expect(mockExchangeService.getAllCheckedComboData(1, 2)).toEqual("cd1");
                    expect(mockExchangeService.getAllCheckedComboData(3)).toBeUndefined();
                    var oldAllCheckedComboData2 = angular.copy(allCheckedComboData2);
                    allCheckedComboData2[1][1] = "cd2";
                    expect(mockExchangeService.getInitialAllCheckedComboData()).toEqual(oldAllCheckedComboData2);

                    mockExchangeService.setAllCheckedComboData(true, false, {1: "cd2", 2: "cd1"}, 3);
                    var allCheckedComboData3 = {
                        1: {1: "cd1", 2: "cd1"},
                        2: {1: "cd1", 2: "cd2"},
                        3: {1: "cd2", 2: "cd1"}
                    };
                    expect(mockScope.$broadcast.calls.count()).toEqual(3);
                    expect(mockScope.$broadcast.calls.argsFor(2)).toEqual(
                        ['allCheckedComboData:update', allCheckedComboData3]);
                    expect(mockExchangeService.getAllCheckedComboData()).toEqual(allCheckedComboData3);
                    expect(mockExchangeService.getAllCheckedComboData(3)).toEqual({1: "cd2", 2: "cd1"});
                    expect(mockExchangeService.getAllCheckedComboData(3, 1)).toEqual("cd2");
                    expect(mockExchangeService.getAllCheckedComboData(3, 2)).toEqual("cd1");

                    mockExchangeService.setAllCheckedComboData(true, true, "cd2", 4, 1);

                    var allCheckedComboData4 = {
                        1: {1: "cd1", 2: "cd1"},
                        2: {1: "cd1", 2: "cd2"},
                        3: {1: "cd2", 2: "cd1"},
                        4: {1: "cd2"}
                    };

                    var initialAllCheckedComboData4 = {
                        1: {1: "cd1", 2: "cd1"},
                        2: {1: "cd1", 2: "cd2"},
                        4: {1: "cd2"}
                    };

                    expect(mockScope.$broadcast.calls.count()).toEqual(5);

                    expect(mockScope.$broadcast.calls.argsFor(3)).toEqual(
                        ['allCheckedComboData:update', allCheckedComboData4]);

                    expect(mockScope.$broadcast.calls.argsFor(4)).toEqual(
                        ['initialAllCheckedComboData:update', initialAllCheckedComboData4]);

                    expect(mockExchangeService.getInitialAllCheckedComboData()).toEqual(initialAllCheckedComboData4);
                    expect(mockExchangeService.getAllCheckedComboData()).toEqual(allCheckedComboData4);
                    expect(mockExchangeService.getAllCheckedComboData(4)).toEqual({1: "cd2"});
                    expect(mockExchangeService.getAllCheckedComboData(4, 1)).toEqual("cd2");
                    expect(mockExchangeService.getAllCheckedComboData(4, 2)).toBeUndefined();
                    var oldInitialAllCheckedComboData4 = angular.copy(initialAllCheckedComboData4);
                    allCheckedComboData4[4][1] = "cd1";
                    expect(mockExchangeService.getInitialAllCheckedComboData()).toEqual(oldInitialAllCheckedComboData4);
                });
            });

        describe('setInitialAllCheckedComboData() and getInitialAllCheckedComboData() test',
            function () {
                it('', function () {
                    var allCheckedComboData = {
                        1: {1: "cd1", 2: "cd1"},
                        2: {1: "cd1", 2: "cd2"},
                        3: {1: "cd2", 2: "cd1"}
                    };
                    mockExchangeService.setInitialAllCheckedComboData();
                    expect(mockScope.$broadcast).toHaveBeenCalledWith("initialAllCheckedComboData:update",
                        {});
                    expect(mockExchangeService.getInitialAllCheckedComboData()).toEqual({});

                    mockExchangeService.setAllCheckedComboData(false, false, allCheckedComboData);
                    mockExchangeService.setInitialAllCheckedComboData();
                    expect(mockScope.$broadcast).toHaveBeenCalledWith("initialAllCheckedComboData:update",
                        allCheckedComboData);
                    expect(mockExchangeService.getInitialAllCheckedComboData()).toEqual(allCheckedComboData);

                    mockExchangeService.setAllCheckedComboData(true, false, undefined);
                    mockExchangeService.setInitialAllCheckedComboData();
                    expect(mockScope.$broadcast).toHaveBeenCalledWith("initialAllCheckedComboData:update",
                        undefined);
                    expect(mockExchangeService.getInitialAllCheckedComboData()).toBeUndefined();

                    mockExchangeService.setAllCheckedComboData(false, true, allCheckedComboData);
                    mockExchangeService.setInitialAllCheckedComboData();
                    expect(mockScope.$broadcast.calls.count()).toEqual(5);
                    expect(mockScope.$broadcast.calls.argsFor(4)).toEqual(
                        ['initialAllCheckedComboData:update', allCheckedComboData]);
                    expect(mockExchangeService.getInitialAllCheckedComboData()).toEqual(allCheckedComboData);

                    var allCheckedComboData2 = {
                        1: {1: "cd1", 2: "cd1"},
                        2: {1: "cd1", 2: "cd2"}
                    };

                    mockExchangeService.setAllCheckedComboData(true, true, allCheckedComboData2);
                    mockExchangeService.setInitialAllCheckedComboData();
                    expect(mockScope.$broadcast.calls.count()).toEqual(8);
                    expect(mockScope.$broadcast.calls.argsFor(7)).toEqual(
                        ['initialAllCheckedComboData:update', allCheckedComboData2]);
                    expect(mockExchangeService.getInitialAllCheckedComboData()).toEqual(allCheckedComboData2);

                });
            });

        describe('setNewPackets(), getNewPackets() and deleteNewPackets() test',
            function () {
                it('', function () {
                    expect(mockExchangeService.getNewPackets()).toEqual({});
                    expect(mockExchangeService.getNewPackets(1)).toBeUndefined();
                    mockExchangeService.setNewPackets(selPkt, 1);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith('newPackets:update', {1: selPkt});
                    expect(mockExchangeService.getNewPackets()).toEqual({1: selPkt});
                    mockExchangeService.deleteNewPackets(2);
                    expect(mockExchangeService.getNewPackets()).toEqual({1: selPkt});
                    expect(mockScope.$broadcast.calls.count()).toEqual(1);
                    mockExchangeService.deleteNewPackets(1);
                    expect(mockExchangeService.getNewPackets()).toEqual({});
                    expect(mockScope.$broadcast.calls.count()).toEqual(1);
                    mockExchangeService.deleteNewPackets(1);
                    expect(mockScope.$broadcast.calls.count()).toEqual(1);
                    mockExchangeService.setNewPackets({1: selPkt, 2: oldPkt});
                    expect(mockScope.$broadcast.calls.count()).toEqual(2);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith('newPackets:update', {1: selPkt, 2: oldPkt});
                });
            });

        describe('getNewComptLabels(), setNewComptLabels() and deleteNewComptLabels() test',
            function () {
                it('', function () {
                    expect(mockExchangeService.getNewComptLabels()).toEqual({true: {}, false: {}});
                    mockExchangeService.setNewPackets({1: selPkt, 2: oldPkt});
                    expect(mockExchangeService.getNewComptLabels(1)).toBeUndefined();
                    expect(mockExchangeService.getNewComptLabels(2)).toBeUndefined();
                    mockExchangeService.setNewComptLabels({}, 1);
                    expect(mockExchangeService.getNewComptLabels()).toEqual({true: {1: {}}, false: {}});
                    expect(mockExchangeService.getNewComptLabels(1)).toEqual({});
                    expect(mockExchangeService.getNewComptLabels(2)).toBeUndefined();
                    mockExchangeService.setNewComptLabels('lbl1', 1, 1);
                    expect(mockExchangeService.getNewComptLabels()).toEqual({true: {1: {1: 'lbl1'}}, false: {}});
                    expect(mockExchangeService.getNewComptLabels(1, 1)).toEqual('lbl1');
                    expect(mockExchangeService.getNewComptLabels(1, 2)).toBeUndefined();
                    expect(mockExchangeService.getNewComptLabels(1)).toEqual({1: 'lbl1'});
                    expect(mockExchangeService.getNewComptLabels(2)).toBeUndefined();
                    mockExchangeService.setNewComptLabels({}, 3);
                    expect(mockExchangeService.getNewComptLabels()).toEqual({true: {1: {1: 'lbl1'}}, false: {3: {}}});
                    expect(mockExchangeService.getNewComptLabels(1)).toEqual({1: 'lbl1'});
                    expect(mockExchangeService.getNewComptLabels(3)).toEqual({});
                    expect(mockExchangeService.getNewComptLabels(2)).toBeUndefined();
                    mockExchangeService.setNewComptLabels('lbl3', 3, 3);
                    expect(mockExchangeService.getNewComptLabels()).toEqual({
                        true: {1: {1: 'lbl1'}},
                        false: {3: {3: 'lbl3'}}
                    });
                    expect(mockExchangeService.getNewComptLabels(3, 3)).toEqual('lbl3');
                    expect(mockExchangeService.getNewComptLabels(3)).toEqual({3: 'lbl3'});
                    mockExchangeService.setNewComptLabels({}, 2);
                    expect(mockExchangeService.getNewComptLabels()).toEqual({
                        true: {1: {1: 'lbl1'}, 2: {}},
                        false: {3: {3: 'lbl3'}}
                    });
                    expect(mockExchangeService.getNewComptLabels(2)).toEqual({});
                    mockExchangeService.deleteNewComptLabels(1, 1);
                    expect(mockExchangeService.getNewComptLabels()).toEqual({
                        true: {1: {}, 2: {}},
                        false: {3: {3: 'lbl3'}}
                    });
                    expect(mockExchangeService.getNewComptLabels(1)).toEqual({});
                    mockExchangeService.deleteNewComptLabels(1, 1);
                    expect(mockExchangeService.getNewComptLabels()).toEqual({
                        true: {1: {}, 2: {}},
                        false: {3: {3: 'lbl3'}}
                    });
                    expect(mockExchangeService.getNewComptLabels(1)).toEqual({});
                    mockExchangeService.deleteNewComptLabels(1);
                    expect(mockExchangeService.getNewComptLabels()).toEqual({true: {2: {}}, false: {3: {3: 'lbl3'}}});
                    expect(mockExchangeService.getNewComptLabels(1)).toBeUndefined();
                    mockExchangeService.deleteNewComptLabels(3, 4);
                    expect(mockExchangeService.getNewComptLabels()).toEqual({true: {2: {}}, false: {3: {3: 'lbl3'}}});
                    mockExchangeService.deleteNewComptLabels(3, 3);
                    expect(mockExchangeService.getNewComptLabels()).toEqual({true: {2: {}}, false: {3: {}}});
                    expect(mockExchangeService.getNewComptLabels(3)).toEqual({});
                    mockExchangeService.deleteNewComptLabels(3);
                    expect(mockExchangeService.getNewComptLabels()).toEqual({true: {2: {}}, false: {}});
                    expect(mockExchangeService.getNewComptLabels(3)).toBeUndefined();
                    mockExchangeService.deleteNewComptLabels();
                    expect(mockExchangeService.getNewComptLabels()).toEqual({true: {}, false: {}});
                    expect(mockExchangeService.getNewComptLabels(2)).toBeUndefined();
                    mockExchangeService.setNewComptLabels('lbl1', 1, 1);
                    expect(mockExchangeService.getNewComptLabels()).toEqual({true: {1: {1: 'lbl1'}}, false: {}});
                    expect(mockExchangeService.getNewComptLabels(1, 1)).toEqual('lbl1');
                    expect(mockExchangeService.getNewComptLabels(1)).toEqual({1: 'lbl1'});
                });
            });

        describe('setComptIdsToDelete(), getComptIdsToDelete(), pushToComptIdsToDelete() and deleteComptIdsToDelete() test',
            function () {
                it('', function () {
                    expect(mockExchangeService.getComptIdsToDelete()).toEqual({});
                    expect(mockExchangeService.getComptIdsToDelete(1)).toEqual([]);
                    mockExchangeService.setComptIdsToDelete(selCompts[0], 1);
                    expect(mockExchangeService.getComptIdsToDelete()).toEqual({1: selCompts[0]});
                    mockExchangeService.deleteComptIdsToDelete(2);
                    expect(mockExchangeService.getComptIdsToDelete()).toEqual({1: selCompts[0]});
                    mockExchangeService.deleteComptIdsToDelete(1);
                    expect(mockExchangeService.getComptIdsToDelete()).toEqual({});
                    mockExchangeService.deleteComptIdsToDelete(1);
                    mockExchangeService.setComptIdsToDelete({1: selCompts[0], 2: [oldCompts[1]], 3: []});
                    expect(mockExchangeService.getComptIdsToDelete()).toEqual({
                        1: selCompts[0],
                        2: [oldCompts[1]], 3: []
                    });
                    expect(mockExchangeService.getComptIdsToDelete(1)).toEqual(selCompts[0]);
                    expect(mockExchangeService.getComptIdsToDelete(2)).toEqual([oldCompts[1]]);
                    expect(mockExchangeService.getComptIdsToDelete(3)).toEqual([]);
                    mockExchangeService.pushToComptIdsToDelete(oldCompts[0], 2);
                    expect(mockExchangeService.getComptIdsToDelete()).toEqual({
                        1: selCompts[0],
                        2: [oldCompts[1], oldCompts[0]], 3: []
                    });
                    mockExchangeService.deleteComptIdsToDelete();
                    expect(mockExchangeService.getComptIdsToDelete()).toEqual({});
                    mockExchangeService.pushToComptIdsToDelete(selCompts[1], 1);
                    expect(mockExchangeService.getComptIdsToDelete()).toEqual({1: [selCompts[1]]});
                });
            });

        describe('deleteComptIdsToUpdate(), getComptIdsToUpdate(), setComptIdsToUpdate()', function () {
            it('', function () {
                expect(mockExchangeService.getComptIdsToUpdate()).toEqual({});
                expect(mockExchangeService.getComptIdsToUpdate(1)).toBeUndefined();
                expect(mockExchangeService.getComptIdsToUpdate(2)).toBeUndefined();
                mockExchangeService.setComptIdsToUpdate({}, 1);
                expect(mockExchangeService.getComptIdsToUpdate()).toEqual({1: {}});
                expect(mockExchangeService.getComptIdsToUpdate(1)).toEqual({});
                expect(mockExchangeService.getComptIdsToUpdate(2)).toBeUndefined();
                mockExchangeService.setComptIdsToUpdate(true, 1, 1);
                expect(mockExchangeService.getComptIdsToUpdate()).toEqual({1: {1: true}});
                expect(mockExchangeService.getComptIdsToUpdate(1)).toEqual({1: true});
                expect(mockExchangeService.getComptIdsToUpdate(2)).toBeUndefined();
                mockExchangeService.setComptIdsToUpdate(true, 2, 2);
                expect(mockExchangeService.getComptIdsToUpdate()).toEqual({1: {1: true}, 2: {2: true}});
                expect(mockExchangeService.getComptIdsToUpdate(1)).toEqual({1: true});
                expect(mockExchangeService.getComptIdsToUpdate(2)).toEqual({2: true});
                mockExchangeService.deleteComptIdsToUpdate(1, 1);
                expect(mockExchangeService.getComptIdsToUpdate()).toEqual({1: {}, 2: {2: true}});
                mockExchangeService.deleteComptIdsToUpdate(2);
                expect(mockExchangeService.getComptIdsToUpdate()).toEqual({1: {}});
                mockExchangeService.deleteComptIdsToUpdate(2);
                expect(mockExchangeService.getComptIdsToUpdate()).toEqual({1: {}});
                mockExchangeService.deleteComptIdsToUpdate();
                expect(mockExchangeService.getComptIdsToUpdate()).toEqual({});
                mockExchangeService.setComptIdsToUpdate({1: {2: true}, 4: {2: true}});
                expect(mockExchangeService.getComptIdsToUpdate()).toEqual({1: {2: true}, 4: {2: true}});
                expect(mockExchangeService.getComptIdsToUpdate(1)).toEqual({2: true});
                expect(mockExchangeService.getComptIdsToUpdate(4)).toEqual({2: true});
            });
        });

        describe('deleteSelectedComptLabels(), setSelectedComptLabels(), getSelectedPacketIsEmpty()' +
            ' and getSelectedComptLabels() test',
            function () {
                it('', function () {
                    mockExchangeService.setSelectedComptLabels({});
                    expect(mockExchangeService.getSelectedComptLabels('lbl1')).toBeUndefined();
                    expect(mockExchangeService.getSelectedComptLabels()).toEqual({});
                    expect(mockExchangeService.getSelectedPacketIsEmpty()).toBeTruthy();
                    mockExchangeService.setSelectedComptLabels(true, 'lbl1');
                    expect(mockExchangeService.getSelectedComptLabels('lbl1')).toBeTruthy();
                    expect(mockExchangeService.getSelectedComptLabels('lbl2')).toBeUndefined();
                    expect(mockExchangeService.getSelectedComptLabels()).toEqual({'lbl1': true});
                    expect(mockExchangeService.getSelectedPacketIsEmpty()).toBeFalsy();
                    mockExchangeService.setSelectedComptLabels(true, 'lbl2');
                    expect(mockExchangeService.getSelectedComptLabels('lbl2')).toBeTruthy();
                    expect(mockExchangeService.getSelectedComptLabels()).toEqual({'lbl1': true, 'lbl2': true});
                    expect(mockExchangeService.getSelectedPacketIsEmpty()).toBeFalsy();
                    mockExchangeService.deleteSelectedComptLabels('lbl2');
                    expect(mockExchangeService.getSelectedComptLabels('lbl2')).toBeUndefined();
                    expect(mockExchangeService.getSelectedPacketIsEmpty()).toBeFalsy();
                    expect(mockExchangeService.getSelectedComptLabels()).toEqual({'lbl1': true});
                    mockExchangeService.deleteSelectedComptLabels('lbl3');
                    expect(mockExchangeService.getSelectedPacketIsEmpty()).toBeFalsy();
                    expect(mockExchangeService.getSelectedComptLabels()).toEqual({'lbl1': true});
                    mockExchangeService.deleteSelectedComptLabels('lbl1');
                    expect(mockExchangeService.getSelectedComptLabels()).toEqual({});
                    expect(mockExchangeService.getSelectedPacketIsEmpty()).toBeTruthy();
                });
            });

        describe('setSelectedCompts(), getSelectedCompts(), getSelectedComptsLength()' +
            ' and pushToSelectedCompts() test',
            function () {
                it('', function () {
                    mockExchangeService.setSelectedCompts([]);
                    expect(mockExchangeService.getSelectedCompts()).toEqual([]);
                    expect(mockExchangeService.getSelectedComptsLength()).toEqual(0);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith("selectedCompts:update", []);
                    mockExchangeService.pushToSelectedCompts(selCompts[0]);
                    expect(mockExchangeService.getSelectedComptsLength()).toEqual(1);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith("selectedCompts:update", [selCompts[0]]);
                    mockExchangeService.setSelectedCompts(selCompts);
                    expect(mockExchangeService.getSelectedCompts()).toEqual(selCompts);
                    expect(mockExchangeService.getSelectedComptsLength()).toEqual(2);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith("selectedCompts:update", selCompts);
                    mockExchangeService.pushToSelectedCompts(selCompts[1]);
                    expect(mockExchangeService.getSelectedCompts()).toEqual([selCompts[0], selCompts[1], selCompts[1]]);
                    expect(mockExchangeService.getSelectedComptsLength()).toEqual(3);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith("selectedCompts:update",
                        mockExchangeService.getSelectedCompts());

                });
            });

        describe('getNumberOfNotNullSelectedCompts(), getSelectedComptsLength() test',
            function () {
                it('', function () {
                    var selectedCompts = [];
                    selectedCompts.push(selCompts[0]);
                    selectedCompts.push(selCompts[1]);
                    selectedCompts.push(oldCompts[0]);
                    selectedCompts.push(oldCompts[1]);

                    mockExchangeService.setSelectedCompts(selectedCompts);
                    mockExchangeService.setSelectedPage(1);
                    mockExchangeService.setSelectedPacketIsEmpty(false);
                    mockExchangeService.setSelectedCompt(2, 4);
                    expect(mockExchangeService.getSelectedPacketIsEmpty()).toBeFalsy();
                    expect(mockScope.$broadcast).toHaveBeenCalledWith("selectedCompts:update",
                        mockExchangeService.getSelectedCompts());

                    mockExchangeService.setSelectedPage(2);
                    mockExchangeService.setSelectedPacketIsEmpty(false);
                    mockExchangeService.setSelectedCompt(2, 4);
                    expect(mockExchangeService.getSelectedPage()).toBe(2);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith("selectedCompts:update",
                        mockExchangeService.getSelectedCompts());

                    mockExchangeService.setSelectedPage(1);
                    mockExchangeService.setSelectedPacketIsEmpty(false);
                    mockExchangeService.setSelectedCompt(2, 3);
                    expect(mockExchangeService.getSelectedPacketIsEmpty()).toBeTruthy();
                    expect(mockScope.$broadcast).toHaveBeenCalledWith("selectedCompts:update",
                        mockExchangeService.getSelectedCompts());

                    mockExchangeService.setSelectedPage(2);
                    mockExchangeService.setSelectedPacketIsEmpty(false);
                    mockExchangeService.setSelectedCompt(2, 3);
                    expect(mockExchangeService.getSelectedPage()).toBe(1);
                    expect(mockScope.$broadcast).toHaveBeenCalledWith("selectedCompts:update",
                        mockExchangeService.getSelectedCompts());
                });
            });

        describe('Utils service test', function () {
            it('IsUndefinedOrNull() test', function () {
                expect(mockUtilsService.isUndefinedOrNull(selCompts[0])).toBeFalsy();
                expect(mockUtilsService.isUndefinedOrNull(2)).toBeFalsy();
                expect(mockUtilsService.isUndefinedOrNull(false)).toBeFalsy();
                expect(mockUtilsService.isUndefinedOrNull('text')).toBeFalsy();
                expect(mockUtilsService.isUndefinedOrNull(null)).toBeTruthy();
                expect(mockUtilsService.isUndefinedOrNull(undefined)).toBeTruthy();
            });

            it('IsEmpty() test', function () {
                expect(mockUtilsService.isEmpty(selCompts)).toBeFalsy();
                expect(mockUtilsService.isEmpty([2, 'false'])).toBeFalsy();
                expect(mockUtilsService.isEmpty([])).toBeTruthy();
            });
        });

        var prepareOldPkt = function () {
            mockExchangeService.setPacketIdToInd(oldPktInd, oldPkt.id);
            mockExchangeService.pushToCompts(oldCompts[0], oldPktInd);
            mockExchangeService.pushToCompts(oldCompts[1], oldPktInd);
            mockExchangeService.setComptLabels(oldComptLabels, oldPkt.id);
            mockExchangeService.setSelectedPacket(oldPkt);
        };

        var prepareSelPkt = function () {
            mockExchangeService.setPacketIdToInd(selPktInd, selPkt.id);
            mockExchangeService.pushToCompts(selCompts[0], selPktInd);
            mockExchangeService.pushToCompts(selCompts[1], selPktInd);
            mockExchangeService.setComptLabels(selComptLabels, selPkt.id);
        };

        var prepareSpies = function () {
            spyOn(mockScope, '$broadcast');
        }
    });

});
