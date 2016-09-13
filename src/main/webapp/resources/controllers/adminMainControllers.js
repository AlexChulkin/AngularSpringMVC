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

        var selectedPacketsReloadCounter;
        var loadedNoCompts;

        $scope.loadPacketById = function (packetId) {
            var isPacketIdUndefined = packetId == undefined;
            var packetIndex = isPacketIdUndefined ? initialPacketIndex : exchangeService.getPacketIdToInd(packetId);
            var dataParams = isPacketIdUndefined ? {} : {packetId: packetId};
            $http
                .post(contextPath + loadDataUrl, {dataParams: dataParams})
                .then(
                    function success(result) {
                        var data = result.data;
                        prepareCompts(data.compts, packetIndex, packetId);
                        preparePackets(data.packets, isPacketIdUndefined);
                        prepareStates(data.states);
                        prepareComboData(data.comboData);
                        if (!loadedNoCompts && !$scope.isComboDataNotLoaded() && !$scope.isStatesNotLoaded()) {
                            prepareComptsSupplInfo(data.comptSupplInfo);
                        }
                        var selPacket = exchangeService.getSelectedPacket();
                        if (!isPacketIdUndefined && selPacket && packetId == selPacket.id) {
                            var selectedPacketIsReloaded =
                            {
                                oldVal: selPacket,
                                newVal: selPacket,
                                selectedPacketsReloadCounter: ++selectedPacketsReloadCounter
                            };
                            exchangeService.setPacketIsSelectedOrSelectedPacketIsReloaded(selectedPacketIsReloaded);
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

        var prepareCompts = function (uploadedCompts, initialPacketInd, packetId) {
            if (packetId != undefined) {
                exchangeService.setComptIdsToDelete([], packetId);
                exchangeService.setComptIdsToUpdate({}, packetId);
            }
            loadedNoCompts = $scope.isDataEmpty(uploadedCompts);
            var packetInd = initialPacketInd;
            var visitedPackets = {};
            angular.forEach(uploadedCompts, function (el) {
                var id = el.id;
                var label = el.label.toUpperCase();
                var packetId = el.packetId;
                if (!visitedPackets[packetId]) {
                    visitedPackets[packetId] = true;
                    exchangeService.setComptLabels({}, packetId);
                    if (initialPacketIndex == initialPacketInd) {
                        exchangeService.pushToCompts([]);
                        exchangeService.setPacketIdToInd(++packetInd, packetId);
                    } else {
                        exchangeService.setCompts([], packetInd)
                    }
                }
                exchangeService.setComptLabels(true, packetId, label);
                var comptIndex = exchangeService.getComptsLength(packetInd);
                exchangeService.setComptIdToInd(id, comptIndex);
                exchangeService.pushToCompts(el, packetInd);
                if (id > exchangeService.getMaximalComptId()) {
                    exchangeService.setMaximalComptId(id);
                }
            });
            if (packetInd > exchangeService.getMaximalPacketIndex()) {
                exchangeService.setMaximalPacketIndex(packetInd);
            }
        };

        var preparePackets = function (packets, isPacketIdUndefined) {
            var loadedNoPackets = $scope.isDataEmpty(packets);
            exchangeService.setLoadedNoPackets(loadedNoPackets);

            if (!loadedNoPackets) {
                var firstPacket = packets[0];
                if (isPacketIdUndefined) {
                    $scope.selectPacket(firstPacket);
                } else {
                    var selectedPacketId = exchangeService.getSelectedPacketId();
                    if (firstPacket.id === selectedPacketId) {
                        exchangeService.setSelectedPacketStateId(firstPacket.stateId);
                    }
                }
            }
            angular.forEach(packets, function (pkt) {
                var pktId = pkt.id;
                exchangeService.setAllPackets(pkt, pktId);
                if (pktId > exchangeService.getMaximalPacketId()) {
                    exchangeService.setMaximalPacketId(pktId);
                }
                exchangeService.setPacketInitialStateIds(pkt.id, pkt.stateId);
            });
        };

        var prepareStates = function (states) {
            exchangeService.setLoadedNoStates($scope.isDataEmpty(states));
            exchangeService.setAllStates(states);
            exchangeService.setAllStateLabels(states, labelLabel);
        };

        var prepareComboData = function (comboData) {
            exchangeService.setLoadedNoComboData($scope.isDataEmpty(comboData));
            exchangeService.setComboDataDefaultSet(comboData);
            exchangeService.setNewComptCheckedVals();
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
            selectedPacketsReloadCounter = 0;
            loadedNoCompts = null;
            exchangeService.init();
            $scope.loadPacketById();
        };

        $scope.init();
    });

