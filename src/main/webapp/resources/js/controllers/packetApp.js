/**
 * Created by achulkin on 04.06.14.   
 */
var app = angular.module("packetApp");
app.constant("packetListActiveClass", "btn-primary btn-sm")
    .constant("packetListNonActiveClass", "btn-sm")
    .constant("packetListPageCount", 9)
    .constant("labelLabel", "Label")
    .controller("packetCtrl", function ($scope, $http, $window, $watch, packetListActiveClass,
                                        packetListNonActiveClass, packetListPageCount, labelLabel) {
        var simpleConfig = {withCredentials: true};

        var comptIdToInd = {};
        var packetIdToInd = {};
        var comptLabels = {};
        var compts = [];
        var maximalComptIndex = 0;
        var maximalPacketIndex = 0;
        var deletedComptIds = {};
        var updatedComptIds = {};
        var newComptLabels = {};
        var newPackets = {};
        var deletedPacketIds = [];

        var packetInitialStateIds = {};

        $scope.pageSize = packetListPageCount;

        $scope.data = {};
        $scope.data.comboDataDefaultSet = [];
        $scope.data.selectedCompts = [];
        $scope.data.allStateLabels = [labelLabel];
        $scope.data.newComptCheckedVals = [];
        $scope.data.allPackets = {};
        $scope.data.allStates = [];
        $scope.data.allComboData = {};
        $scope.data.allCheckedVals = {};
        $scope.data.selectedComptLabels = {};

        $scope.data.noComptsSelected = true;
        $scope.data.loadedNoPackets = true;
        $scope.data.loadedNoCompts = true;
        $scope.data.loadedNoStates = true;
        $scope.data.loadedNoComboData = true;
        $scope.data.loadedNoComptSupplInfo = true;
        $scope.data.stateIdOfSelectedPacket = null;
        $scope.data.isPacketSelected = false;

        $scope.data.selectedPacket = null;
        $scope.data.selectedPacketId = null;


        $http.get(contextPath + '/getAllData', simpleConfig).success(function (data) {
            if (!isDataEmpty(data.compts)) {
                $scope.data.loadedNoCompts = false;
            }
            var packetInd = -1;
            angular.forEach(data.compts, function (el) {
                var id = el.id;
                var label = el.label.toUpperCase();
                var packetId = el.packetId;
                if (!comptLabels[packetId]) {
                    comptLabels[packetId] = {};
                    compts.push([]);
                    packetIdToInd[packetId] = ++packetInd;
                }
                comptLabels[packetId][label] = true;
                comptIdToInd[id] = compts[packetInd].length;
                compts[packetInd].push(el);
                if (id > maximalComptIndex) {
                    maximalComptIndex = id;
                }
            });
            if (!isDataEmpty(data.packets)) {
                $scope.selectPacket(data.packets[0]);
            }
            angular.forEach(data.packets, function (pkt) {
                var pktId = pkt.id;
                $scope.data.allPackets[pktId] = pkt;
                if (pktId > maximalPacketIndex) {
                    maximalPacketIndex = pktId;
                }
                packetInitialStateIds[pkt.id] = pkt.stateId;
            });
            if (!isDataEmpty(data.states)) {
                $scope.data.loadedNoStates = false;
            }
            $scope.data.allStates = data.states;
            angular.forEach($scope.data.allStates, function (state) {
                $scope.data.allStateLabels.push(state.label);
            });

            if (!isDataEmpty(data.comboData)) {
                $scope.data.loadedNoComboData = false;
            }
            angular.forEach(data.comboData, function (cd) {
                $scope.data.comboDataDefaultSet.push(cd.label);
            });
            angular.forEach($scope.data.allStates, function () {
                $scope.data.newComptCheckedVals.push($scope.data.comboDataDefaultSet[0]);
            });

            if ($scope.data.loadedNoCompts || $scope.data.loadedNoComboData || $scope.data.loadedNoStates) {
                return;
            }
            if (!isDataEmpty(data.comptSupplInfo)) {
                $scope.data.loadedNoComptSupplInfo = false;
            }
            angular.forEach(data.comptSupplInfo, function (el) {
                var comptId = el.comptId;
                var stateId = el.stateId;
                var label = el.label;
                $scope.data.allComboData[comptId] = $scope.data.allComboData[comptId] || {};
                $scope.data.allComboData[comptId][stateId] = $scope.data.allComboData[comptId][stateId] || [];
                $scope.data.allComboData[comptId][stateId].push(label);
                var checked = el.checked;
                if (checked) {
                    $scope.data.allCheckedVals[comptId] = $scope.data.allCheckedVals[comptId] || {};
                    $scope.data.allCheckedVals[comptId][stateId] = label;
                }
            });
        }).error(function (error) {
            $scope.data.loadError = error;
        });

        $scope.$watch('data.selectedComptLabels', function (value) {
            $scope.data.noComptsSelected = angular.equals({}, value);
        });

        $scope.$watch('data.allPackets', function (value) {
            $scope.data.loadedNoPackets = angular.equals({}, value);
        });

        $scope.$watch('data.selectedPacket', function (value) {
            value != null ? $scope.data.selectedPacketId = value.id
                : $scope.data.selectedPacketId = null;
        });

        $scope.addComptLocally = function () {
            var comptId = ++maximalComptIndex;
            var usualLabel = $scope.data.newLabel;
            var upperCaseLabel = usualLabel.toUpperCase();
            var newCompt = {id: comptId, label: usualLabel, new: true};
            $scope.data.selectedComptLabels[upperCaseLabel] = true;
            comptIdToInd[comptId] = $scope.data.selectedCompts.length;
            $scope.data.selectedCompts.push(newCompt);
            newComptLabels[$scope.data.selectedPacketId] = newComptLabels[$scope.data.selectedPacketId] || {};
            newComptLabels[$scope.data.selectedPacketId][comptId] = usualLabel;

            $scope.data.allComboData[comptId] = {};
            $scope.data.allCheckedVals[comptId] = {};
            for (var i = 1; i <= $scope.data.allStates.length; i++) {
                $scope.data.allComboData[comptId][i] = $scope.data.comboDataDefaultSet;
                $scope.data.allCheckedVals[comptId][i] = $scope.data.newComptCheckedVals[i - 1];
            }
            $scope.data.newLabel = null;
        };

        $scope.addPacketLocally = function () {
            var newPacket = {id: ++maximalPacketIndex, stateId: 1};
            newPackets[maximalPacketIndex] = newPacket;
            $scope.data.allPackets[maximalPacketIndex] = newPacket;
            $scope.data.loadEmpty = null;
        };

        $scope.deleteComptLocally = function (compt) {
            var comptLabel = compt.label;
            var comptId = compt.id;

            delete $scope.data.selectedComptLabels[comptLabel.toUpperCase()];
            $scope.data.selectedCompts[comptIdToInd[comptId]] = null;
            delete $scope.data.allCheckedVals[comptId];
            delete $scope.data.allComboData[comptId];
            if (updatedComptIds[$scope.data.selectedPacketId]) {
                delete updatedComptIds[$scope.data.selectedPacketId][comptId];
            }
            if (newComptLabels[$scope.data.selectedPacketId] && newComptLabels[$scope.data.selectedPacketId][comptId]) {
                delete newComptLabels[$scope.data.selectedPacketId][comptId];
            } else {
                deletedComptIds[$scope.data.selectedPacketId] = deletedComptIds[$scope.data.selectedPacketId] || [];
                deletedComptIds[$scope.data.selectedPacketId].push(comptId);
            }
        };

        $scope.deletePacketLocally = function (packet) {
            var packetId = packet.id;
            if (!newPackets[packetId]) {
                deletedPacketIds.push(packetId);
            }
            delete $scope.data.allPackets[packetId];
            delete newComptLabels[packetId];
            delete deletedComptIds[packetId];
            $scope.data.selectedPacket = null;
            $scope.data.isPacketSelected = false;
        };

        deletePacketsInBase = function () {
            if (deletedPacketIds.length == 0) {
                return;
            }
            var deleteConfig = {withCredentials: true, params: {packetIds: deletedPacketIds}};
            $http.post(contextPath + '/deletePackets', deleteConfig)
                .success(function (data) {
                })
                .error(function (error) {
                });
        };

        $scope.updateComptLocally = function (compt) {
            var comptId = compt.id;
            if (newComptLabels[$scope.data.selectedPacketId][comptId]) {
                return;
            }
            updatedComptIds[$scope.data.selectedPacketId] = updatedComptIds[$scope.data.selectedPacketId] || {};
            updatedComptIds[$scope.data.selectedPacketId][comptId] = true;
        };

        $scope.saveAllToBase = function () {
            deleteComptsInBase();
            deletePacketsInBase();
            saveOrUpdatePacketsInBase();
        };

        isDataEmpty = function (data) {
            if (!data || !angular.isArray(data) || data.length == 0) {
                $scope.data.dataEmpty = true;
                return true;
            }
            return false;
        };

        getCheckedValsForCompt = function (comptId) {
            var checkedVals = [];
            angular.forEach($scope.data.allStates, function (state, ind) {
                checkedVals.push($scope.data.allCheckedVals[comptId][ind + 1]);
            });
            return checkedVals;
        };

        deleteComptsInBase = function () {
            if (angular.equals({}, deletedComptIds)) {
                return;
            }
            var deletedComptIdsForConfig = [];
            angular.forEach($scope.data.allPackets, function (unused, pktId) {
                deletedComptIdsForConfig = deletedComptIdsForConfig.concat(deletedComptIds[pktId]);
            });
            if (deletedComptIdsForConfig.length == 0) {
                return;
            }
            var deleteConfig = {withCredentials: true, params: {comptIds: deletedComptIdsForConfig}};
            $http.post(contextPath + '/deleteCompts', deleteConfig)
                .success(function (data) {
                })
                .error(function (error) {
                });
            deletedComptIds = {};
        };

        saveOrUpdatePacketsInBase = function () {
            var updatePacketParamsList = [];
            var createPacketParamsList = [];
            angular.forEach($scope.data.allPackets, function (pkt, pktId) {
                var config = {};
                if (!newPackets[pktId]) {
                    config.id = pktId;
                    if (config.stateId != packetInitialStateIds[pktId]) {
                        config.stateId = pkt.stateId;
                    }
                    config.updatedComptParamsList = generatePacketUpdateUpdatedComptParamsList(pktId);
                    config.addedComptParamsList = generatePacketUpdateAddedComptParamsList(pktId);
                    if (config.stateId || config.updatedComptParamsList.length > 0
                        || config.addedComptParamsList.length > 0) {
                        updatePacketParamsList.push(config);
                    }
                } else {
                    config.stateId = pkt.stateId;
                    config.addedComptParamsList = generatePacketCreateComptParamsList(pktId);
                    createPacketParamsList.push(config);
                }
            });
            if (updatePacketParamsList.length == 0 && createPacketParamsList.length == 0) {
                return;
            }
            var addConfig = {
                withCredentials: true,
                params: {createPacketParamsList: createPacketParamsList, updatePacketParamsList: updatePacketParamsList}
            };
            $http.post(contextPath + '/saveOrUpdatePackets', addConfig)
                .success(function (data) {
                })
                .error(function (error) {
                });
            newComptLabels = {};
            updatedComptIds = {};
        };

        generatePacketCreateComptParamsList = function (pktId) {
            var result = [];
            angular.forEach(newComptLabels[pktId], function (lbl, comptId) {
                result.push({label: lbl, vals: getCheckedValsForCompt(comptId)});
            });
            return result;
        };

        generatePacketUpdateAddedComptParamsList = function (pktId) {
            var result = [];
            angular.forEach(newComptLabels[pktId], function (lbl, comptId) {
                result.push({label: lbl, vals: getCheckedValsForCompt(comptId)});
            });
            return result;
        };

        generatePacketUpdateUpdatedComptParamsList = function (pktId) {
            var result = [];
            angular.forEach(updatedComptIds[pktId], function (unused, comptId) {
                result.push({id: comptId, vals: getCheckedValsForCompt(comptId)});
            });
            return result;
        };

        $scope.selectPacket = function (packet) {
            $scope.data.selectedPacket = packet;
            $scope.data.isPacketSelected = true;
            $scope.data.selectedCompts = compts[packetIdToInd[$scope.data.selectedPacketId]] || [];

            $scope.data.selectedComptLabels = comptLabels[$scope.data.selectedPacketId] || {};

            $scope.selectPage(1);
        };

        $scope.selectPage = function (newPage) {
            $scope.selectedPage = newPage;
        };

        $scope.getPacketClass = function (packet) {
            return $scope.data.selectedPacket != null ? $scope.data.selectedPacketId == packet.id ? packetListActiveClass
                : packetListNonActiveClass : packetListNonActiveClass;
        };

        $scope.getPageClass = function (page) {
            return $scope.selectedPage == page ? packetListActiveClass : packetListNonActiveClass;
        };

        $scope.notNull = function (item) {
            return item;
        };

        $scope.reloadRoute = function () {
            $window.location.reload();
        };
    });

app.directive('blacklist', function () {
    return {
        require: 'ngModel',
        link: function ($scope, elem, attr, ngModel) {
            ngModel.$parsers.unshift(function (label) {
                var label = label.replace(/\s{2,}/g, " ");
                var upperCaseLabel = label.toUpperCase();
                ngModel.$setValidity('blacklist', !$scope.data.selectedComptLabels[upperCaseLabel]);
                return label;
            });
        }
    };
});

app.directive('regex', function () {
    return {
        require: 'ngModel',
        link: function ($scope, elem, attr, ngModel) {
            ngModel.$parsers.unshift(function (label) {
                ngModel.$setValidity('regex', label.search(/[^\w\s]/g) == -1);
                return label;
            });
        }
    };
});