/**
 * Created by achulkin on 04.06.14.   
 */
var app = angular.module("packetApp");
app.constant("packetListActiveClass", "btn-primary btn-sm")
    .constant("packetListNonActiveClass", "btn-sm")
    .constant("packetListPageCount", 8)
    .constant("labelLabel", "Label")
    .controller("packetCtrl", function ($scope, $http, $window, packetListActiveClass,
                                        packetListNonActiveClass, packetListPageCount, labelLabel) {
        var simpleConfig = {withCredentials: true};

        var selectedPacket = null;
        var selectedPacketId = null;

        var comptIdToInd = {};
        var packetIdToInd = {};

        var comptLabels = {};

        var compts = [];

        var maximalComptIndex = 0;

        var deletedComptIds = {};
        var updatedComptIds = {};

        var newComptLabels = {};

        var updatedPackets = {};
        var deletedPacketIds = [];

        $scope.pageSize = packetListPageCount;

        $scope.data = {};
        $scope.data.comboDataDefaultSet = [];
        $scope.data.selectedCompts = [];
        $scope.data.stateLabels = [labelLabel];
        $scope.data.newValues = [];
        $scope.data.packets = {};
        $scope.data.states = [];
        $scope.data.comboData = {};
        $scope.data.checkedVals = {};
        $scope.data.selectedComptLabels = {};

        $scope.data.loadError = false;
        $scope.data.loadEmpty = false;

        $http.get(contextPath + '/getAllCompts', simpleConfig).success(function (data) {
            if (isDataEmpty(data)) {
                $scope.data.loadEmpty = true;
                return;
            }
            var packetInd = -1;
            angular.forEach(data, function (el) {
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

            $http.get(contextPath + '/getAllPackets', simpleConfig).success(function (data) {
                if (isDataEmpty(data)) {
                    $scope.data.loadEmpty = true;
                    return;
                }
                $scope.selectPacket(data[0]);
                angular.forEach(data, function (pkt) {
                    $scope.data.packets[pkt.id] = pkt;
                });
                $http.get(contextPath + '/getAllStates', simpleConfig).success(function (data) {
                    if (isDataEmpty(data)) {
                        $scope.data.loadEmpty = true;
                        return;
                    }
                    $scope.data.states = data;
                    angular.forEach($scope.data.states, function (state) {
                        $scope.data.stateLabels.push(state.label);
                });

                    $http.get(contextPath + '/getAllComboData', simpleConfig).success(function (data) {
                        if (isDataEmpty(data)) {
                            $scope.data.loadEmpty = true;
                            return;
                        }
                        angular.forEach(data, function (cd) {
                            $scope.data.comboDataDefaultSet.push(cd.label);
                        });
                        angular.forEach($scope.data.states, function () {
                            $scope.data.newValues.push($scope.data.comboDataDefaultSet[0]);
                        });

                        $http.get(contextPath + '/getAllComptsSupplInfo', simpleConfig).success(function (data) {
                            if (isDataEmpty(data)) {
                                $scope.data.loadEmpty = true;
                                return;
                            }
                            angular.forEach(data, function (el) {
                                var comptId = el.comptId;
                                var stateId = el.stateId;
                                var label = el.label;
                                if (!$scope.data.comboData[comptId]) {
                                    $scope.data.comboData[comptId] = {};
                                }
                                if (!$scope.data.comboData[comptId][stateId]) {
                                    $scope.data.comboData[comptId][stateId] = [];
                                }
                                $scope.data.comboData[comptId][stateId].push(label);
                                var checked = el.checked;
                                if (checked) {
                                    if (!$scope.data.checkedVals[comptId]) {
                                        $scope.data.checkedVals[comptId] = {};
                                }
                                    $scope.data.checkedVals[comptId][stateId] = label;
                                }
                        });
                    }).error(function (error) {
                        $scope.data.loadError = error;
                    });
                }).error(function (error) {
                    $scope.data.loadError = error;
                });
            }).error(function (error) {
                $scope.data.loadError = error;
            });
        }).error(function (error) {
            $scope.data.loadError = error;
        });
        }).error(function (error) {
            $scope.data.loadError = error;
        });

        $scope.addComptLocally = function () {
            var comptId = ++maximalComptIndex;
            var usualLabel = $scope.data.newLabel;
            var upperCaseLabel = usualLabel.toUpperCase();
            var newCompt = {id: comptId, label: usualLabel, new: true};
            $scope.data.selectedComptLabels[upperCaseLabel] = true;
            comptIdToInd[comptId] = $scope.data.selectedCompts.length;
            $scope.data.selectedCompts.push(newCompt);
            if (!newComptLabels[selectedPacketId]) {
                newComptLabels[selectedPacketId] = {};
            }
            newComptLabels[selectedPacketId][usualLabel] = comptId;

            $scope.data.comboData[comptId] = {};
            $scope.data.checkedVals[comptId] = {};
            for (var i = 1; i <= $scope.data.states.length; i++) {
                $scope.data.comboData[comptId][i] = $scope.data.comboDataDefaultSet;
                $scope.data.checkedVals[comptId][i] = $scope.data.newValues[i - 1];
            }
            $scope.data.newLabel = null;
        };

        $scope.deleteComptLocally = function (compt) {
            var label = compt.label;
            var id = compt.id;
            var isNew = compt.new;
            var selectedPacketInd = packetIdToInd[selectedPacketId];

            delete $scope.data.selectedComptLabels[label.toUpperCase()];
            $scope.data.selectedCompts[comptIdToInd[id]] = null;
            delete $scope.data.checkedVals[id];
            delete $scope.data.comboData[id];
            if (!!updatedComptIds[selectedPacketId]) {
                delete updatedComptIds[selectedPacketId][id];
            }
            if (!!newComptLabels[selectedPacketId]) {
                delete newComptLabels[selectedPacketId][label];
            }
            if (!isNew) {
                if (!deletedComptIds[selectedPacketId]) {
                    deletedComptIds[selectedPacketId] = [];
                }
                deletedComptIds[selectedPacketId].push(id);
            }
        };

        $scope.deletePacketLocally = function (packet) {
            var packetId = packet.id;
            deletedPacketIds.push(packetId);
            delete $scope.data.packets[packetId];
            delete updatedPackets[packetId];
            $scope.data.packetDeleted = true;
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
            if (compt.new) {
                return;
            }
            if (!updatedComptIds[selectedPacketId]) {
                updatedComptIds[selectedPacketId] = {};
            }
            updatedComptIds[selectedPacketId][compt.id] = true;
        };

        $scope.saveAllToBase = function () {
            deletePacketsInBase();
            updatePacketLocally();
            updatePacketsInBase();
            deleteComptsInBase();
            addComptsToBase();
            updateComptsInBase();
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
            angular.forEach($scope.data.states, function (state, ind) {
                checkedVals.push($scope.data.checkedVals[comptId][ind + 1]);
            });
            return checkedVals;
        };

        deleteComptsInBase = function () {
            if (angular.equals({}, deletedComptIds)) {
                return;
            }
            var deletedComptIdsForConfig = [];
            angular.forEach($scope.data.packets, function (unused, pktId) {
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

        updateComptsInBase = function () {
            var updatedCompts = [];
            for (var pktId in $scope.data.packets) {
                angular.forEach(updatedComptIds[pktId], function (unused, comptId) {
                    updatedCompts.push({id: comptId, vals: getCheckedValsForCompt(comptId)});
                });
            }
            if (isDataEmpty(updatedCompts)) {
                return;
            }
            var updateConfig = {withCredentials: true, params: {comptParamsList: updatedCompts}};
            $http.post(contextPath + '/updateCompts', updateConfig)
                .success(function (data) {
                })
                .error(function (error) {
                });
            updatedComptIds = {};
        };

        addComptsToBase = function () {
            var newCompts = [];
            for (var pktId in $scope.data.packets) {
                angular.forEach(newComptLabels[pktId], function (comptId, lbl) {
                    newCompts.push({label: lbl, vals: getCheckedValsForCompt(comptId)});
                    var comptIndToUntag = comptIdToInd[comptId];
                    $scope.data.selectedCompts[comptIndToUntag].new = false;
                });
            }
            if (isDataEmpty(newCompts)) {
                return;
            }
            var addConfig = {withCredentials: true, params: {packetId: selectedPacketId, comptParamsList: newCompts}};
            $http.post(contextPath + '/addCompts', addConfig)
                .success(function (data) {
                })
                .error(function (error) {
                });
            newComptLabels = {};
        };

        updatePacketsInBase = function () {
            if (angular.equals({}, updatedPackets)) {
                return;
            }
            var updatedPacketsForConfig = [];
            angular.forEach(updatedPackets, function (packet) {
                updatedPacketsForConfig.push(packet);
            });
            var updateConfig = {withCredentials: true, params: {updatedPackets: updatedPacketsForConfig}};
            $http.post(contextPath + '/updatePackets', updateConfig)
                .success(function (data) {
                })
                .error(function (error) {
                });
            updatedPackets = {};
        };

        $scope.selectPacket = function (packet) {
            $scope.data.packetDeleted = null;
            updatePacketLocally();
            selectedPacket = packet;
            selectedPacketId = selectedPacket.id;
            var selectedPacketInd = packetIdToInd[selectedPacketId];
            $scope.data.selectedCompts = compts[selectedPacketInd];
            $scope.data.selectedComptLabels = comptLabels[selectedPacketId];

            $scope.data.selectedStateIndex = selectedPacket.stateId;
            $scope.selectPage(1);
        };

        updatePacketLocally = function () {
            if (!!selectedPacket && !$scope.data.packetDeleted) {
                var newStateId = $scope.data.selectedStateIndex;
                if (selectedPacket.stateId !== newStateId) {
                    selectedPacket.stateId = newStateId;
                    updatedPackets[selectedPacketId] = {
                        id: selectedPacketId,
                        stateId: selectedPacket.stateId
                    }
                } else {
                    delete updatedPackets[selectedPacketId];
                }
            }
            var pcs = $scope.data.packets;
        };

        $scope.selectPage = function (newPage) {
            $scope.selectedPage = newPage;
        };

        $scope.getPacketClass = function (packet) {
            return selectedPacket != null ? selectedPacket.id == packet.id ? packetListActiveClass
                : packetListNonActiveClass : packetListNonActiveClass;
        };

        $scope.getPageClass = function (page) {
            return $scope.selectedPage == page ? packetListActiveClass : packetListNonActiveClass;
        };

        $scope.notNull = function (item) {
            return !!item;
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