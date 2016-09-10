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

        var selectedPacketsReloadCounter = 0;
        var loadedNoCompts = null;

        $scope.loadPacketById = function (packetId) {

            var packetIndex = !packetId ? initialPacketIndex : exchangeService.getPacketIdToInd[packetId];
            var dataParams = !packetId ? {} : {packetId: packetId};
            $http
                .post(contextPath + loadDataUrl, {dataParams: dataParams})
                .then(
                    function success(result) {
                        var data = result.data;
                        prepareCompts(data.compts, packetIndex);
                        preparePackets(data.packets, !packetId);
                        prepareStates(data.states);
                        prepareComboData(data.comboData);
                        if (!loadedNoCompts && !$scope.isComboDataNotLoaded() && !$scope.isStatesNotLoaded()) {
                            prepareComptsSupplInfo(data.comptSupplInfo);
                        }
                        var selPacket = exchangeService.getSelectedPacket();
                        if (packetId && selPacket && packetId == selPacket.id) {
                            var packetIsSelectedOrSelectedPacketIsReloaded =
                            {
                                oldVal: selPacket,
                                newVal: selPacket,
                                selectedPacketsReloadCounter: ++selectedPacketsReloadCounter
                            };
                            exchangeService.setPacketIsSelectedOrSelectedPacketIsReloaded
                            (packetIsSelectedOrSelectedPacketIsReloaded);
                        }
                    },
                    function error(error) {
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
            var oldSelectedPacket = exchangeService.getSelectedPacket();
            exchangeService.setSelectedPacket(packet);
            var packetIsSelectedOrSelectedPacketIsReloaded = {oldVal: oldSelectedPacket, newVal: packet};
            exchangeService.setPacketIsSelectedOrSelectedPacketIsReloaded(packetIsSelectedOrSelectedPacketIsReloaded);
            $scope.selectPage(1);
        };

        $scope.selectPage = function (newPage) {
            exchangeService.setSelectedPage(newPage);
        };

        $scope.isDataEmpty = function (data) {
            if (!data || !angular.isArray(data) || data.length == 0) {
                return true;
            }
            return false;
        };

        var prepareCompts = function (uploadedCompts, initialPacketInd) {
            loadedNoCompts = $scope.isDataEmpty(uploadedCompts);
            var packetInd = initialPacketInd;
            var visitedPacket = {};
            angular.forEach(uploadedCompts, function (el) {
                var id = el.id;
                var label = el.label.toUpperCase();
                var packetId = el.packetId;
                if (!visitedPacket[packetId]) {
                    visitedPacket[packetId] = true;
                    exchangeService.setComptLabels({}, packetId);
                    if (initialPacketIndex == initialPacketInd) {
                        exchangeService.pushToCompts([]);
                        exchangeService.setPacketIdToInd(++packetInd, packetId);
                    } else {
                        exchangeService.setCompts([], packetInd)
                    }
                }

                exchangeService.setComptLabels(true, packetId, label);
                exchangeService.setComptIdToInd[id, exchangeService.getComptsLength(packetInd)];
                exchangeService.pushToCompts(packetInd, el);
                if (id > exchangeService.getMaximalComptId()) {
                    exchangeService.setMaximalComptId(id);
                }
            });
            if (packetInd > exchangeService.getMaximalPacketIndex()) {
                exchangeService.setMaximalPacketIndex(packetInd);
            }
        };

        var preparePackets = function (packets, isPacketIdNull) {
            exchangeService.setLoadedNoPackets($scope.isDataEmpty(packets));

            if (!exchangeService.getLoadedNoPackets() && isPacketIdNull) {
                $scope.selectPacket(packets[0]);
            }
            angular.forEach(packets, function (pkt) {
                var pktId = pkt.id;
                addItemToAllPackets(pktId, pkt);
                if (pktId > exchangeService.getMaximalPacketId()) {
                    exchangeService.setMaximalPacketId(pktId);
                }
                exchangeService.setPacketInitialStateIds(pkt.stateId, pkt.id);
            });
        };

        var addItemToAllPackets = function (pktId, pkt) {
            exchangeService.setAllPackets(pkt, pktId);
        };

        var prepareStates = function (states) {
            exchangeService.setLoadedNoStates($scope.isDataEmpty(states));
            exchangeService.setAllStates(states);
            exchangeService.pushToAllStateLabels(stateLabel);
            angular.forEach(exchangeService.getAllStates(), function (state) {
                exchangeService.pushToAllStateLabels(state.label);
            });
        };

        var prepareComboData = function (comboData) {
            exchangeService.setLoadedNoComboData($scope.isDataEmpty(comboData));
            angular.forEach(comboData, function (cd) {
                exchangeService.pushToComboDataDefaultSet(cd.label);
            });
            angular.forEach(exchangeService.getAllStates(), function () {
                exchangeService.pushToNewComptCheckedVals(exchangeService.getComboDataDefaultSet(0));
            });
        };

        var prepareComptsSupplInfo = function (comptSupplInfo) {
            exchangeService.setLoadedNoComptsSupplInfo($scope.isDataEmpty(comptSupplInfo));

            angular.forEach(comptSupplInfo, function (item) {
                var comptId = item.comptId;
                var stateId = item.stateId;
                var label = item.label;
                if (!exchangeService.getAllComboData(comptId)) {
                    exchangeService.setAllComboData({}, comptId);
                }
                if (!exchangeService.getAllComboData(comptId, stateId)) {
                    exchangeService.setAllComboData([], comptId, stateId);
                }
                exchangeService.pushToAllComboData(label, comptId, stateId);
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
            $scope.loadPacketById(null);
        };

        $scope.init();
    });

