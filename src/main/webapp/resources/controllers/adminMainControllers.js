/*
 * Copyright (c) 2016.  Alex Chulkin
 */

'use strict';

/**
 * The controller of the adminMain.html page.
 */

angular.module("packetAdminApp")
    .constant("packetListPageCount", 10)
    .constant("labelLabel", "Label")
    .constant("loadDataUrl", "/loadData")
    .constant("initialPacketIndex", -1)
    .controller("mainCtrl", function ($scope, $http, packetListPageCount, labelLabel, loadDataUrl,
                                      initialPacketIndex, exchangeService, utilsService) {

        var loadedNoCompts;

        $scope.loadPackets = function (packetId, numOfOtherPkts) {
            var isPacketIdUndefined = angular.isUndefined(packetId);
            var packetIndex = isPacketIdUndefined ? initialPacketIndex : exchangeService.getPacketIdToInd(packetId);
            var dataParams = isPacketIdUndefined ? {} : {packetId: packetId};
            $http
                .post(loadDataUrl, {dataParams: dataParams})
                .then(
                    function success(result) {
                        initializeService(isPacketIdUndefined, packetId);
                        var data = result.data;
                        prepareCompts(data.compts, packetIndex, packetId, isPacketIdUndefined);
                        preparePackets(data.packets, packetId, isPacketIdUndefined, numOfOtherPkts);
                        prepareStates(data.states);
                        prepareComboData(data.comboData);
                        if (!loadedNoCompts && !$scope.isComboDataNotLoaded() && !$scope.isStatesNotLoaded()) {
                            prepareComptsSupplInfo(data.comptSupplInfo, packetId, isPacketIdUndefined);
                        }
                    },
                    function error() {
                        exchangeService.setLoadError(true);
                    }
                );
        };

        $scope.isDataLoadedProperly = function () {
            return !(exchangeService.getLoadError() || exchangeService.getLoadedNoComboData()
            || exchangeService.getLoadedNoStates());
        };

        $scope.isDataLoadError = function () {
            return exchangeService.getLoadError();
        };

        $scope.isComboDataNotLoaded = function () {
            return exchangeService.getLoadedNoComboData();
        };

        $scope.isStatesNotLoaded = function () {
            return exchangeService.getLoadedNoStates();
        };

        $scope.isPacketAlreadySelectedAtLeastOnce = function () {
            return exchangeService.getPacketIsAlreadySelectedAtLeastOnce();
        };

        $scope.selectPacket = function (packet) {
            exchangeService.setSelectedPacket(packet);
            $scope.selectPage(1);
        };

        $scope.selectPage = function (newPage) {
            exchangeService.setSelectedPage(newPage);
        };

        var initializeService = function (isPacketIdUndefined, packetId) {
            if (!isPacketIdUndefined) {
                exchangeService.deleteComptIdsToDelete(packetId);
                exchangeService.deleteComptIdsToUpdate(packetId);
                exchangeService.deleteNewComptLabels(packetId);
                exchangeService.deleteNewPackets(packetId);
                exchangeService.initializeCompts(exchangeService.getPacketIdToInd(packetId));
                exchangeService.setComptLabels({}, packetId);
            } else {
                exchangeService.init();
            }
        };

        var prepareCompts = function (uploadedCompts, initialPacketInd, packetId, isPacketIdUndefined) {
            loadedNoCompts = utilsService.isEmpty(uploadedCompts);
            var packetInd = initialPacketInd;
            var visitedPackets = {};
            angular.forEach(uploadedCompts, function (compt) {
                var comptId = compt.id;
                var label = compt.label.toUpperCase();
                var localPktId = isPacketIdUndefined ? compt.packetId : packetId;
                if (!(localPktId in visitedPackets)) {
                    visitedPackets[localPktId] = true;
                    if (isPacketIdUndefined) {
                        exchangeService.setPacketIdToInd(++packetInd, localPktId);
                    } 
                }
                exchangeService.setComptLabels(true, localPktId, label);
                var comptIndex = exchangeService.getComptsLength(packetInd);
                exchangeService.setComptIdToInd(comptIndex, comptId);
                exchangeService.pushToCompts(compt, packetInd);
                if (comptId > exchangeService.getMaximalComptId()) {
                    exchangeService.setMaximalComptId(comptId);
                }
            });
            if (packetInd > exchangeService.getMaximalPacketIndex()) {
                exchangeService.setMaximalPacketIndex(packetInd);
            }
        };

        var preparePackets = function (packets, packetId, isPacketIdUndefined, numOfOtherPkts) {
            var isPacketsEmpty = utilsService.isEmpty(packets);
            var loadedNoPackets = isPacketsEmpty && (isPacketIdUndefined || numOfOtherPkts === 0);
            var selPktId = exchangeService.getSelectedPacketId();
            var loadedNoSelectedPacket = isPacketsEmpty && !isPacketIdUndefined && packetId === selPktId;
            var loadedNoUnSelectedPacket = isPacketsEmpty && !isPacketIdUndefined && packetId !== selPktId;
            exchangeService.setLoadedNoPackets(loadedNoPackets);
            exchangeService.setLoadedNoSelectedPacket(loadedNoSelectedPacket);
            exchangeService.setLoadedNoUnSelectedPacket(loadedNoUnSelectedPacket, packetId);

            angular.forEach(packets, function (pkt) {
                var pktId = pkt.id;
                exchangeService.setAllPackets(pkt, pktId);
                if (pktId > exchangeService.getMaximalPacketId()) {
                    exchangeService.setMaximalPacketId(pktId);
                }
                exchangeService.setPacketInitialStateIds(pkt.stateId, pktId);
            });

            if (!isPacketsEmpty) {
                var firstOrSingleReloadedPacket = packets[0];
                if (isPacketIdUndefined || firstOrSingleReloadedPacket.id === selPktId) {
                    $scope.selectPacket(firstOrSingleReloadedPacket);
                }
            }
        };

        var prepareStates = function (states) {
            exchangeService.setLoadedNoStates(utilsService.isEmpty(states));
            exchangeService.setAllStates(states);
            exchangeService.setAllStateLabels(states, labelLabel);
        };

        var prepareComboData = function (comboData) {
            exchangeService.setLoadedNoComboData(utilsService.isEmpty(comboData));
            exchangeService.setComboDataDefaultSet(comboData);
            exchangeService.initializeNewComptCheckedVals();
        };

        var prepareComptsSupplInfo = function (comptSupplInfo, packetId, isPacketIdUndefined) {
            var comptSupplInfoLength = comptSupplInfo.length;
            var counter = 0;
            var comptIdsAlreadyLoaded = {};
            angular.forEach(comptSupplInfo, function (item) {
                var broadcast = counter === comptSupplInfoLength - 1;
                var comptId = item.comptId;
                if (!(comptId in comptIdsAlreadyLoaded)) {
                    exchangeService.setAllComboData(true, {}, comptId);
                    comptIdsAlreadyLoaded[comptId] = true;
                }
                var stateId = item.stateId;
                var label = item.label;
                var checked = item.checked;
                exchangeService.pushToAllComboData(broadcast, label, comptId, stateId);
                if (checked) {
                    exchangeService.setAllCheckedComboData(broadcast, !isPacketIdUndefined, label, comptId, stateId);
                }
                counter++;
            });
            if (isPacketIdUndefined) {
                exchangeService.setInitialAllCheckedComboData();
            }
        };

        var init = function () {
            loadedNoCompts = null;
            $scope.loadPackets();
        };

        init();
    });

