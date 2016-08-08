/**
 * Created by achulkin on 04.06.14.   
 */
var app = angular.module("packetApp", [])
        .constant("packetListActiveClass", "btn-primary")
        .constant("packetListPageCount", 8)
        .constant("labelLabel", "Label")
        .constant("comptIdLabel", "ID")
    .constant("defaultSelectedStateIndex", 1);


app.controller("packetCtrl", function ($scope, $http, $window, packetListActiveClass, packetListPageCount, labelLabel,
                                       comptIdLabel, defaultSelectedStateIndex) {
    var simpleConfig = {withCredentials: true};
    var selectedPacket = null;


    $http.get(contextPath + '/getAllPackets', simpleConfig).success(function (data) {
        $scope.data = {};
        $scope.data.packets = {};

        angular.forEach(data, function (pkt) {
            $scope.data.packets[pkt.id] = pkt;
        });

        $http.get(contextPath + '/getAllCompts', simpleConfig).success(function (data) {
            $scope.selectedPage = 1;
            $scope.pageSize = packetListPageCount;

            $scope.data.compts = {};
            $scope.data.selectedCompts = $scope.data.compts;
            $scope.data.selectedStateIndex = defaultSelectedStateIndex;
            $scope.data.maximalIndex = 0;
            angular.forEach(data, function (el) {
                var id = el.id;
                var label = el.label.toUpperCase();
                var packetId = el.packetId;
                if (!$scope.data.compts[packetId]) {
                    $scope.data.compts[packetId] = {};
                }
                $scope.data.compts[packetId][label] = el;
                if (id > $scope.data.maximalIndex) {
                    $scope.data.maximalIndex = id;
                }
            });

            $scope.data.allComboData = [];
            $scope.data.persistedRecentlyRemovedItemIds = [];
            $scope.data.updatedItemIds = {};
            $scope.data.newComptLabels = {};

            $http.get(contextPath + '/getAllStates', simpleConfig).success(function (data) {
                if (!data) {
                    $scope.data.errorStates = true;
                    return;
                }
                $scope.data.states = data;

                $scope.data.stateLabels = [];
                $scope.data.stateLabels.push(comptIdLabel);
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
        $scope.data.selectedCompts[upperCaseLabel] = {id: comptId, label: usualLabel, new: true};
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

        delete $scope.data.selectedCompts[label];
        delete $scope.data.checkedVals[id];
        delete $scope.data.comboData[id];
        delete $scope.data.updatedItemIds[id];

        if (!isNew) {
            $scope.data.persistedRecentlyRemovedItemIds.push(id);
        }
    };

    $scope.markComptAsUpdated = function (compt) {
        if ($scope.data.selectedCompts[compt.label].new) {
            return;
        }
        var comptId = compt.id;
        $scope.data.updatedItemIds[comptId] = comptId;
    };

    $scope.saveAllToBase = function () {
        $scope.updatePacketStateInBase($scope.data.selectedStateIndex);

        $scope.removeComptsFromBase();
        $scope.data.persistedRecentlyRemovedItemIds = [];

        var newCompts = [];
        angular.forEach($scope.data.newComptLabels, function (id, lbl) {
            newCompts.push({label: lbl, vals: $scope.getCheckedValsForCompt(id)});
            $scope.data.selectedCompts[lbl].new = false;
        });
        $scope.addComptsToBase(newCompts);
        $scope.data.newComptLabels = {};

        var updatedCompts = [];
        angular.forEach($scope.data.updatedItemIds, function (comptId, id) {
            console.log(id);
            updatedCompts.push({id: comptId, vals: $scope.getCheckedValsForCompt(comptId)});
        });
        $scope.updateComptsInBase(updatedCompts);
        $scope.data.updatedItemIds = [];
    };

    $scope.getCheckedValsForCompt = function (comptId) {
        var checkedVals = [];
        angular.forEach($scope.data.states, function (state, ind) {
            checkedVals.push($scope.data.checkedVals[comptId][ind + 1]);
        });
        return checkedVals;
    };

    $scope.removeComptsFromBase = function () {
        if (!$scope.data.persistedRecentlyRemovedItemIds) {
            return;
        }

        var removeConfig = {withCredentials: true, params: {idsToRemove: $scope.data.persistedRecentlyRemovedItemIds}};
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
        var addConfig = {withCredentials: true, params: {packetId: selectedPacket.id, comptsParamsList: newCompts}};
        $http.post(contextPath + '/addCompts', addConfig).success(function (data) {
        }).error(function (error) {
        });
    };

    $scope.updatePacketStateInBase = function (newStateId) {
        if (selectedPacket == null || selectedPacket.stateId == newStateId) {
            return;
        }
        var updateConfig = {withCredentials: true, params: {packetId: selectedPacket.id, newStateId: newStateId}};
        $http.post(contextPath + '/updatePacketState', updateConfig).success(function (data) {
        }).error(function (error) {
        });
    };

    $scope.selectPacket = function (packet) {
        selectedPacket = packet;
        $scope.data.selectedCompts = $scope.data.compts[selectedPacket.id];
        $scope.data.selectedStateIndex = selectedPacket.stateId;
        $scope.selectedPage = 1;
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
                ngModel.$setValidity('blacklist', !$scope.data.selectedCompts[upperCaseLabel]);
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