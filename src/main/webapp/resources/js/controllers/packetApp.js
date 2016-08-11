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
        $scope.pageSize = packetListPageCount;
        var comptIdToInd = {};
        var packetIdToInd = {};
    
        $http.get(contextPath + '/getAllCompts', simpleConfig).success(function (data) {
            $scope.data = {};
            $scope.data.comptsLabels = {};
            $scope.data.selectedComptsLabels = {};
            $scope.data.compts = [];
            $scope.data.selectedCompts = [];

            $scope.data.maximalIndex = 0;
            var ind = -1;


            angular.forEach(data, function (el) {
                var id = el.id;
                var label = el.label.toUpperCase();
                var packetId = el.packetId;
                if (!$scope.data.comptsLabels[packetId]) {
                    $scope.data.comptsLabels[packetId] = {};
                    $scope.data.compts.push([]);
                    packetIdToInd[packetId] = ++ind;
                }
                $scope.data.comptsLabels[packetId][label] = 1;

                comptIdToInd[id] = $scope.data.compts[ind].length;
                $scope.data.compts[ind].push(el);
                if (id > $scope.data.maximalIndex) {
                    $scope.data.maximalIndex = id;
                }
            });

            $scope.data.allComboData = [];
            $scope.data.persistedRecentlyRemovedItemIds = [];
            $scope.data.updatedItemIds = {};
            $scope.data.newComptLabels = {};

            $http.get(contextPath + '/getAllPackets', simpleConfig).success(function (data) {
                $scope.data.packets = {};

                var isFirstPacketSelected = false;
                angular.forEach(data, function (pkt) {
                    if (!isFirstPacketSelected) {
                        $scope.selectPacket(pkt);
                        isFirstPacketSelected = true;
                    }
                    $scope.data.packets[pkt.id] = pkt;
                });
                $http.get(contextPath + '/getAllStates', simpleConfig).success(function (data) {
                    if (!data) {
                        $scope.data.errorStates = true;
                        return;
                    }
                    $scope.data.states = data;

                    $scope.data.stateLabels = [];
                    $scope.data.stateLabels.push(labelLabel);
                    $scope.data.newValues = [];
                    angular.forEach($scope.data.states, function (state) {
                        $scope.data.stateLabels.push(state.label);
                    });
                    $http.get(contextPath + '/getAllComboData', simpleConfig).success(function (data) {
                        if (!data) {
                            $scope.data.errorComboData = true;
                            return;
                        }
                        angular.forEach(data, function (cd) {
                            $scope.data.allComboData.push(cd.label);
                        });
                        angular.forEach($scope.data.states, function () {
                            $scope.data.newValues.push($scope.data.allComboData[0]);
                        });
                        $http.get(contextPath + '/getAllComptsSupplInfo', simpleConfig).success(function (data) {
                            if (!data) {
                                $scope.data.errorComptsSupplData = true;
                                return;
                                }
                            $scope.data.checkedVals = {};
                            $scope.data.comboData = {};
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
            var comptId = ++$scope.data.maximalIndex;
            var usualLabel = $scope.data.newLabel;
            var upperCaseLabel = usualLabel.toUpperCase();
            var newCompt = {id: comptId, label: usualLabel, new: true};
            $scope.data.selectedComptsLabels[upperCaseLabel] = 1;
            comptIdToInd[comptId] = $scope.data.selectedCompts.length;
            $scope.data.selectedCompts.push(newCompt);
            $scope.data.newComptLabels[usualLabel] = comptId;

            $scope.data.comboData[comptId] = {};
            $scope.data.checkedVals[comptId] = {};
            for (var i = 1; i <= $scope.data.states.length; i++) {
                $scope.data.comboData[comptId][i] = $scope.data.allComboData;
                $scope.data.checkedVals[comptId][i] = $scope.data.newValues[i - 1];
            }
            $scope.data.newLabel = null;
        };

        $scope.deleteCompt = function (compt) {
            var label = compt.label;
            var id = compt.id;
            var isNew = compt.new;

            delete $scope.data.selectedComptsLabels[label.toUpperCase()];
            $scope.data.selectedCompts[comptIdToInd[id]] = null;
            delete $scope.data.checkedVals[id];
            delete $scope.data.comboData[id];
            delete $scope.data.updatedItemIds[id];
            delete $scope.data.newComptLabels[label];
            delete comptIdToInd[id];

            if (!isNew) {
                $scope.data.persistedRecentlyRemovedItemIds.push(id);
            }
        };

        $scope.markComptAsUpdated = function (compt) {
            if (compt.new) {
                return;
            }
            var comptId = compt.id;
            $scope.data.updatedItemIds[comptId] = 1;
        };

        $scope.saveAllToBase = function () {
            $scope.updatePacketStateInBase($scope.data.selectedStateIndex);
            $scope.removeComptsFromBase();
            $scope.data.persistedRecentlyRemovedItemIds = [];

            var newCompts = [];
            angular.forEach($scope.data.newComptLabels, function (id, lbl) {
                newCompts.push({label: lbl, vals: $scope.getCheckedValsForCompt(id)});
                var indToUntag = comptIdToInd[$scope.data.newComptLabels[lbl]];
                $scope.data.selectedCompts[indToUntag].new = false;
            });
            $scope.addComptsToBase(newCompts);
            $scope.data.newComptLabels = {};

            var updatedCompts = [];
            angular.forEach($scope.data.updatedItemIds, function (unused, comptId) {
                updatedCompts.push({id: comptId, vals: $scope.getCheckedValsForCompt(comptId)});
            });
            $scope.updateComptsInBase(updatedCompts);
            $scope.data.updatedItemIds = {};
        };

        $scope.getCheckedValsForCompt = function (comptId) {
            var checkedVals = [];
            angular.forEach($scope.data.states, function (state, ind) {
                checkedVals.push($scope.data.checkedVals[comptId][ind + 1]);
            });
            return checkedVals;
        };

        $scope.removeComptsFromBase = function () {
            if ($scope.data.persistedRecentlyRemovedItemIds.length == 0) {
                return;
            }

            var removeConfig
                = {withCredentials: true, params: {idsToRemove: $scope.data.persistedRecentlyRemovedItemIds}};
            $http.post(contextPath + '/removeCompts', removeConfig).success(function (data) {
            }).error(function (error) {
            });
        };

        $scope.updateComptsInBase = function (updatedCompts) {
            if (!updatedCompts) {
                return;
            }
            var updateConfig = {withCredentials: true, params: {comptsParamsList: updatedCompts}};
            $http.post(contextPath + '/updateCompts', updateConfig).success(function (data) {
            }).error(function (error) {
            });
        };

        $scope.addComptsToBase = function (newCompts) {
            if (!newCompts) {
                return;
            }
            var addConfig
                = {withCredentials: true, params: {packetId: selectedPacket.id, comptsParamsList: newCompts}};
            $http.post(contextPath + '/addCompts', addConfig).success(function (data) {
            }).error(function (error) {
            });
        };

        $scope.updatePacketStateInBase = function (newStateId) {
            if (selectedPacket.stateId == newStateId) {
                return;
            }
            var updateConfig
                = {withCredentials: true, params: {packetId: selectedPacket.id, newStateId: newStateId}};
            $http.post(contextPath + '/updatePacketState', updateConfig).success(function (data) {
            }).error(function (error) {
            });
        };

        $scope.selectPacket = function (packet) {
            selectedPacket = packet;
            var packetId = selectedPacket.id;
            var packetInd = packetIdToInd[packetId];
            $scope.data.selectedCompts = $scope.data.compts[packetInd];
            $scope.data.selectedComptsLabels = $scope.data.comptsLabels[packetId];

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

        $scope.isAddingNotAllowed = function () {
            return selectedPacket == null;
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
                ngModel.$setValidity('blacklist', !$scope.data.selectedComptsLabels[upperCaseLabel]);
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