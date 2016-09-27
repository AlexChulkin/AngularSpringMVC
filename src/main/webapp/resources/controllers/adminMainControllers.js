/**
 * Created by achulkin on 04.06.14.
 */
angular.module("packetAdminApp")
    .constant("packetListPageCount", 10)
    .constant("labelLabel", "Label")
    .constant("loadDataUrl", "/loadData")
    .constant("initialPacketIndex", -1)
    .controller("mainCtrl", function ($scope, $http, packetListPageCount, labelLabel, loadDataUrl,
                                      initialPacketIndex, exchangeService) {

        var loadedNoCompts;

        $scope.loadPackets = function (packetId) {
            var isPacketIdUndefined = angular.isUndefined(packetId);
            var packetIndex = isPacketIdUndefined ? initialPacketIndex : exchangeService.getPacketIdToInd(packetId);
            var dataParams = isPacketIdUndefined ? {} : {packetId: packetId};
            $http
                .post(contextPath + loadDataUrl, {dataParams: dataParams})
                .then(
                    function success(result) {
                        initializeService(isPacketIdUndefined, packetId);
                        var data = result.data;
                        prepareCompts(data.compts, packetIndex, packetId, isPacketIdUndefined);
                        preparePackets(data.packets, packetId, isPacketIdUndefined);
                        prepareStates(data.states);
                        prepareComboData(data.comboData);
                        if (!loadedNoCompts && !$scope.isComboDataNotLoaded() && !$scope.isStatesNotLoaded()) {
                            prepareComptsSupplInfo(data.comptSupplInfo);
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
            } else {
                exchangeService.init();
            }
        };

        var prepareCompts = function (uploadedCompts, initialPacketInd, packetId, isPacketIdUndefined) {
            loadedNoCompts = exchangeService.isEmpty(uploadedCompts);
            var packetInd = initialPacketInd;
            var visitedPackets = {};
            angular.forEach(uploadedCompts, function (el) {
                var comptId = el.id;
                var label = el.label.toUpperCase();
                var localPktId = isPacketIdUndefined ? el.packetId : packetId;

                if (!visitedPackets[localPktId]) {
                    visitedPackets[localPktId] = true;
                    exchangeService.setComptLabels({}, localPktId);
                    if (isPacketIdUndefined) {
                        exchangeService.pushToCompts([]);
                        exchangeService.setPacketIdToInd(++packetInd, localPktId);
                    } else {
                        exchangeService.setCompts([], packetInd);
                    }
                }
                exchangeService.setComptLabels(true, localPktId, label);
                var comptIndex = exchangeService.getComptsLength(packetInd);
                exchangeService.setComptIdToInd(comptIndex, comptId);
                exchangeService.pushToCompts(el, packetInd);
                if (comptId > exchangeService.getMaximalComptId()) {
                    exchangeService.setMaximalComptId(comptId);
                }
            });
            if (packetInd > exchangeService.getMaximalPacketIndex()) {
                exchangeService.setMaximalPacketIndex(packetInd);
            }
        };

        var preparePackets = function (packets, packetId, isPacketIdUndefined) {
            var isPacketsEmpty = exchangeService.isEmpty(packets);
            var noPackets = isPacketsEmpty && isPacketIdUndefined;
            var loadedNoSelectedPacket = isPacketsEmpty && !isPacketIdUndefined
                && packetId === exchangeService.getSelectedPacketId();
            var loadedNoUnSelectedPacket = isPacketsEmpty && !isPacketIdUndefined
                && packetId !== exchangeService.getSelectedPacketId();
            exchangeService.setNoPackets(noPackets);
            exchangeService.setLoadedNoSelectedPacket(loadedNoSelectedPacket);
            exchangeService.setLoadedNoUnSelectedPacket(loadedNoUnSelectedPacket, packetId);

            angular.forEach(packets, function (pkt) {
                var pktId = pkt.id;
                exchangeService.setAllPackets(pkt, pktId);
                if (pktId > exchangeService.getMaximalPacketId()) {
                    exchangeService.setMaximalPacketId(pktId);
                }
                exchangeService.setPacketInitialStateIds(pkt.stateId, pkt.id);
            });

            if (!isPacketsEmpty) {
                var firstOrSingleReloadedPacket = packets[0];
                if (isPacketIdUndefined || firstOrSingleReloadedPacket.id === exchangeService.getSelectedPacketId()) {
                    $scope.selectPacket(firstOrSingleReloadedPacket);
                }
            }
        };

        var prepareStates = function (states) {
            exchangeService.setLoadedNoStates(exchangeService.isEmpty(states));
            exchangeService.setAllStates(states);
            exchangeService.setAllStateLabels(states, labelLabel);
        };

        var prepareComboData = function (comboData) {
            exchangeService.setLoadedNoComboData(exchangeService.isEmpty(comboData));
            exchangeService.setComboDataDefaultSet(comboData);
            exchangeService.initializeNewComptCheckedVals();
        };

        var prepareComptsSupplInfo = function (comptSupplInfo) {
            var comptSupplInfoLength = comptSupplInfo.length;
            var i = 0;
            var visitedComptIds = {};
            angular.forEach(comptSupplInfo, function (item) {
                var comptId = item.comptId;
                var stateId = item.stateId;
                var label = item.label;
                if (!exchangeService.getAllComboData(comptId) || !visitedComptIds[comptId]) {
                    exchangeService.setAllComboData({}, comptId);
                    visitedComptIds[comptId] = true;
                }
                if (!exchangeService.getAllComboData(comptId, stateId)) {
                    exchangeService.setAllComboData([], comptId, stateId);
                }
                exchangeService.pushToAllComboData(label, comptId, stateId, i, comptSupplInfoLength);
                var checked = item.checked;
                if (checked) {
                    if (!exchangeService.getAllCheckedComboData(comptId)) {
                        exchangeService.setAllCheckedComboData({}, comptId);
                    }
                    exchangeService.setAllCheckedComboData(label, comptId, stateId);
                }
            });
        };

        $scope.init = function () {
            loadedNoCompts = null;
            $scope.loadPackets();
        };

        $scope.init();
    });

