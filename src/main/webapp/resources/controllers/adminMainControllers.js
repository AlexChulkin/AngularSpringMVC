/**
 * Created by achulkin on 04.06.14.
 */
angular.module("packetAdminApp")
    .constant("packetListPageCount", 10)
    .constant("labelLabel", "Label")
    .constant("loadDataUrl", "/loadData")
    .constant("initialPacketIndex", -1)
    .controller("mainCtrl", function ($scope, $http, packetListPageCount, labelLabel, loadDataUrl,
                                      initialPacketIndex) {

        var data;

        $scope.loadPacketById = function (packetId) {

            var packetIndex = !packetId ? initialPacketIndex : $scope.data.packetIdToInd[packetId];
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
                        if (!$scope.isComptsNotLoaded()
                            && !$scope.isComboDataNotLoaded()
                            && !$scope.isStatesNotLoaded()) {

                            prepareComptsSupplInfo(data.comptSupplInfo);
                        }
                        var selPacket = $scope.data.selectedPacket;
                        if (packetId && selPacket && packetId == selPacket.id) {
                            $scope.data.packetIsSelectedOrSelectedPacketIsReloaded
                                =
                            {
                                oldVal: selPacket,
                                newVal: selPacket,
                                selectedPacketsReloadCounter: ++$scope.data.selectedPacketsReloadCounter
                            };
                        }
                    },
                    function error(error) {
                        $scope.data.loadError = true;
                    }
                );
        };

        $scope.isDataLoadedProperly = function () {
            return !($scope.isDataLoadError() || $scope.isComboDataNotLoaded() || $scope.isStatesNotLoaded());
        };

        $scope.isDataLoadError = function () {
            return $scope.data.loadError;
        };

        $scope.isComptsNotLoaded = function () {
            return data.loadedNoCompts;
        };

        $scope.isComboDataNotLoaded = function () {
            return $scope.data.loadedNoComboData;
        };

        $scope.isStatesNotLoaded = function () {
            return $scope.data.loadedNoStates;
        };

        $scope.isPacketAlreadySelectedAtLeastOnce = function () {
            return $scope.data.packetIsAlreadySelectedAtLeastOnce;
        };

        $scope.selectPacket = function (packet) {
            var oldSelectedPacket = $scope.data.selectedPacket;
            $scope.data.selectedPacket = packet;
            $scope.data.packetIsSelectedOrSelectedPacketIsReloaded = {oldVal: oldSelectedPacket, newVal: packet};
            $scope.selectPage(1);
        };

        $scope.selectPage = function (newPage) {
            $scope.data.selectedPage = newPage;
        };

        $scope.isDataEmpty = function (data) {
            if (!data || !angular.isArray(data) || data.length == 0) {
                return true;
            }
            return false;
        };

        var prepareCompts = function (uploadedCompts, initialPacketInd) {
            data.loadedNoCompts = $scope.isDataEmpty(uploadedCompts);
            var packetInd = initialPacketInd;
            var visitedPacket = {};
            angular.forEach(uploadedCompts, function (el) {
                var id = el.id;
                var label = el.label.toUpperCase();
                var packetId = el.packetId;
                if (!visitedPacket[packetId]) {
                    visitedPacket[packetId] = true;
                    $scope.data.comptLabels[packetId] = {};
                    if (initialPacketIndex == initialPacketInd) {
                        $scope.data.compts.push([]);
                        $scope.data.packetIdToInd[packetId] = ++packetInd;
                    } else {
                        $scope.data.compts[packetInd] = [];
                    }
                }

                $scope.data.comptLabels[packetId][label] = true;
                $scope.data.comptIdToInd[id] = $scope.data.compts[packetInd].length;
                $scope.data.compts[packetInd].push(el);
                if (id > $scope.data.maximalComptId) {
                    $scope.data.maximalComptId = id;
                }
            });
            if (packetInd > $scope.data.maximalPacketIndex) {
                $scope.data.maximalPacketIndex = packetInd;
            }
        };

        var preparePackets = function (packets, isPacketIdNull) {
            $scope.data.loadedNoPackets = $scope.isDataEmpty(packets);

            if (!$scope.data.loadedNoPackets && isPacketIdNull) {
                $scope.selectPacket(packets[0]);
            }
            angular.forEach(packets, function (pkt) {
                var pktId = pkt.id;
                $scope.data.allPackets[pktId] = pkt;
                if (pktId > $scope.data.maximalPacketId) {
                    $scope.data.maximalPacketId = pktId;
                }
                $scope.data.packetInitialStateIds[pkt.id] = pkt.stateId;
            });
        };

        var prepareStates = function (states) {
            $scope.data.loadedNoStates = $scope.isDataEmpty(states);
            $scope.data.allStates = states;
            $scope.data.allStateLabels = [labelLabel];
            angular.forEach($scope.data.allStates, function (state) {
                $scope.data.allStateLabels.push(state.label);
            });
        };

        var prepareComboData = function (comboData) {
            $scope.data.loadedNoComboData = $scope.isDataEmpty(comboData);
            angular.forEach(comboData, function (cd) {
                $scope.data.comboDataDefaultSet.push(cd.label);
            });
            angular.forEach($scope.data.allStates, function () {
                $scope.data.newComptCheckedVals.push($scope.data.comboDataDefaultSet[0]);
            });
        };

        var prepareComptsSupplInfo = function (comptSupplInfo) {
            $scope.data.loadedNoComptsSupplInfo = $scope.isDataEmpty(comptSupplInfo);

            angular.forEach(comptSupplInfo, function (item) {
                var comptId = item.comptId;
                var stateId = item.stateId;
                var label = item.label;
                $scope.data.allComboData[comptId] = $scope.data.allComboData[comptId] || {};
                $scope.data.allComboData[comptId][stateId] = $scope.data.allComboData[comptId][stateId] || [];
                $scope.data.allComboData[comptId][stateId].push(label);
                var checked = item.checked;
                if (checked) {
                    $scope.data.allCheckedComboData[comptId] = $scope.data.allCheckedComboData[comptId] || {};
                    $scope.data.allCheckedComboData[comptId][stateId] = label;
                }
            });
        };

        var setDefaultDataAndScopeData = function () {
            data = {};
            $scope.data = {};
            $scope.data.comptIdToInd = {};
            $scope.data.packetIdToInd = {};
            $scope.data.comptLabels = {};
            $scope.data.compts = [];
            $scope.data.packetInitialStateIds = {};
            $scope.data.maximalComptId = 0;
            $scope.data.maximalPacketId = 0;
            $scope.data.maximalPacketIndex = 0;
            $scope.data.comboDataDefaultSet = [];
            $scope.data.newComptCheckedVals = [];
            $scope.data.allPackets = {};
            $scope.data.newPackets = {};
            $scope.data.allStates = [];
            $scope.data.allComboData = {};
            $scope.data.allCheckedComboData = {};
            $scope.data.comptIdsToUpdate = {};
            $scope.data.newComptLabels = {true: {}, false: {}};
            $scope.data.comptIdsTaggedToDelete = {};
            $scope.data.packetIsSelectedOrSelectedPacketIsReloaded = {oldVal: null, newVal: null};
            $scope.data.selectedPacketsReloadCounter = 0;
        };

        $scope.init = function () {
            setDefaultDataAndScopeData();
            $scope.loadPacketById(null);
        };

        $scope.init();
    });

