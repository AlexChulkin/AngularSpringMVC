/**
 * Created by achulkin on 04.06.14.   
 */
var app = angular.module("packetApp");
app.constant("packetListActiveClass", "btn-primary")
    .constant("packetListPageCount", 8)
    .constant("labelLabel", "Label")
    .controller("packetCtrl", function ($scope, $http, $window, packetListActiveClass,
                                        packetListPageCount, labelLabel) {
        var simpleConfig = {withCredentials: true};

        var selectedPacket = null;

        var comptIdToInd = {};
        var packetIdToInd = {};

        var comptLabels = {};

        var compts = [];

        var maximalIndex = 0;

        var removedItemIds = [];
        var updatedItemIds = {};

        var newComptLabels = {};

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
                return;
            }
            var ind = -1;
            angular.forEach(data, function (el) {
                var id = el.id;
                var label = el.label.toUpperCase();
                var packetId = el.packetId;
                if (!comptLabels[packetId]) {
                    comptLabels[packetId] = {};
                    compts.push([]);
                    packetIdToInd[packetId] = ++ind;
                }
                comptLabels[packetId][label] = 1;
                comptIdToInd[id] = compts[ind].length;
                compts[ind].push(el);
                if (id > maximalIndex) {
                    maximalIndex = id;
                }
            });

            $http.get(contextPath + '/getAllPackets', simpleConfig).success(function (data) {
                if (isDataEmpty(data)) {
                    return;
                }
                var isFirstPacketSelected = false;
                angular.forEach(data, function (pkt) {
                    if (!isFirstPacketSelected) {
                        $scope.selectPacket(pkt);
                        isFirstPacketSelected = true;
                }
                    $scope.data.packets[pkt.id] = pkt;
            });
                $http.get(contextPath + '/getAllStates', simpleConfig).success(function (data) {
                    if (isDataEmpty(data)) {
                        return;
                    }
                    $scope.data.states = data;
                    angular.forEach($scope.data.states, function (state) {
                        $scope.data.stateLabels.push(state.label);
                });

                    $http.get(contextPath + '/getAllComboData', simpleConfig).success(function (data) {
                        if (isDataEmpty(data)) {
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

        $scope.addNewCompt = function () {
            var comptId = ++maximalIndex;
            var usualLabel = $scope.data.newLabel;
            var upperCaseLabel = usualLabel.toUpperCase();
            var newCompt = {id: comptId, label: usualLabel, new: true};
            $scope.data.selectedComptLabels[upperCaseLabel] = 1;
            comptIdToInd[comptId] = $scope.data.selectedCompts.length;
            $scope.data.selectedCompts.push(newCompt);
            newComptLabels[usualLabel] = comptId;

            $scope.data.comboData[comptId] = {};
            $scope.data.checkedVals[comptId] = {};
            for (var i = 1; i <= $scope.data.states.length; i++) {
                $scope.data.comboData[comptId][i] = $scope.data.comboDataDefaultSet;
                $scope.data.checkedVals[comptId][i] = $scope.data.newValues[i - 1];
            }
            $scope.data.newLabel = null;
        };

        $scope.deleteCompt = function (compt) {
            var label = compt.label;
            var id = compt.id;
            var isNew = compt.new;

            delete $scope.data.selectedComptLabels[label.toUpperCase()];
            $scope.data.selectedCompts[comptIdToInd[id]] = null;
            delete $scope.data.checkedVals[id];
            delete $scope.data.comboData[id];
            delete updatedItemIds[id];
            delete newComptLabels[label];
            delete comptIdToInd[id];

            if (!isNew) {
                removedItemIds.push(id);
            }
        };

        $scope.markComptAsUpdated = function (compt) {
            if (compt.new) {
                return;
            }
            var comptId = compt.id;
            updatedItemIds[comptId] = 1;
        };

        $scope.saveAllToBase = function () {
            updatePacketStateInBase($scope.data.selectedStateIndex);
            removeComptsFromBase();
            removedItemIds = [];

            var newCompts = [];
            angular.forEach(newComptLabels, function (id, lbl) {
                newCompts.push({label: lbl, vals: getCheckedValsForCompt(id)});
                var indToUntag = comptIdToInd[newComptLabels[lbl]];
                $scope.data.selectedCompts[indToUntag].new = false;
            });
            addComptsToBase(newCompts);
            newComptLabels = {};

            var updatedCompts = [];
            angular.forEach(updatedItemIds, function (unused, comptId) {
                updatedCompts.push({id: comptId, vals: getCheckedValsForCompt(comptId)});
            });
            updateComptsInBase(updatedCompts);
            updatedItemIds = {};
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

        removeComptsFromBase = function () {
            if (removedItemIds.length == 0) {
                return;
            }

            var removeConfig = {withCredentials: true, params: {idsToRemove: removedItemIds}};
            $http.post(contextPath + '/removeCompts', removeConfig).success(function (data) {
            }).error(function (error) {
            });
        };

        updateComptsInBase = function (updatedCompts) {
            if (isDataEmpty(updatedCompts)) {
                return;
            }
            var updateConfig = {withCredentials: true, params: {comptsParamsList: updatedCompts}};
            $http.post(contextPath + '/updateCompts', updateConfig).success(function (data) {
            }).error(function (error) {
            });
        };

        addComptsToBase = function (newCompts) {
            if (isDataEmpty(newCompts)) {
                return;
            }
            var addConfig = {withCredentials: true, params: {packetId: selectedPacket.id, comptsParamsList: newCompts}};
            $http.post(contextPath + '/addCompts', addConfig).success(function (data) {
            }).error(function (error) {
            });
        };

        updatePacketStateInBase = function (newStateId) {
            if (selectedPacket.stateId == newStateId) {
                return;
            }
            var updateConfig = {withCredentials: true, params: {packetId: selectedPacket.id, newStateId: newStateId}};
            $http.post(contextPath + '/updatePacketState', updateConfig).success(function (data) {
            }).error(function (error) {
            });
        };

        $scope.selectPacket = function (packet) {
            selectedPacket = packet;
            var packetId = selectedPacket.id;
            var packetInd = packetIdToInd[packetId];
            $scope.data.selectedCompts = compts[packetInd];
            $scope.data.selectedComptLabels = comptLabels[packetId];

            $scope.data.selectedStateIndex = selectedPacket.stateId;
            $scope.selectPage(1);
        };

        $scope.selectPage = function (newPage) {
            $scope.selectedPage = newPage;
        };

        $scope.getPacketClass = function (packet) {
            return selectedPacket != null ? selectedPacket.id == packet.id ? packetListActiveClass : "" : "";
        };

        $scope.getPageClass = function (page) {
            return $scope.selectedPage == page ? packetListActiveClass : "";
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