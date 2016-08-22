/**
 * Created by achulkin on 04.06.14.   
 */
var app = angular.module("packetApp");
app.constant("packetListActiveClass", "btn-primary btn-sm")
    .constant("packetListNonActiveClass", "btn-sm")
    .constant("packetListPageCount", 9)
    .constant("labelLabel", "Label")
    .constant("updateCompts", "UPDATE_COMPTS")
    .constant("deleteCompts", "DELETE_COMPTS")
    .constant("updatePackets", "UPDATE_PACKETS")
    .constant("deletePackets", "DELETE_PACKETS")
    .constant("addPackets", "ADD_PACKETS")
    .constant("loadDataPath", "/loadData")
    .constant("saveAllChangesToBasePath", "/saveAllChangesToBase")
    .constant("loadPacketByIdPath", "/loadPacketById")
    .constant("savePacketByIdPath", "/savePacketById")
    .constant("initialPacketIndex", -1)
    .controller("packetCtrl", function ($scope, $http, $window, $watch, packetListActiveClass,
                                        packetListNonActiveClass, packetListPageCount, labelLabel,
                                        updateCompts, deleteCompts, updatePackets, deletePackets,
                                        addPackets, loadDataPath, saveAllChangesToBasePath,
                                        loadPacketByIdPath, savePacketByIdPath, initialPacketIndex) {

        var comptIdToInd = {};
        var packetIdToInd = {};
        var comptLabels = {};
        var compts = [];
        var maximalComptIndex = 0;
        var maximalPacketIndex = 0;
        var comptIdsTaggedToDelete = {};
        var comptIdsToUpdate = {};
        var comptLabelsToAddToNewPackets = {};
        var comptLabelsToAddToExistingPackets = {};
        var selectedComptLabelsToAdd = {};
        var newPackets = {};
        var packetIdsToDelete = [];

        var packetInitialStateIds = {};

        $scope.pageSize = packetListPageCount;

        $scope.data = {};
        $scope.data.comboDataDefaultSet = [];
        $scope.data.selectedCompts = [];
        $scope.data.newComptCheckedVals = [];
        $scope.data.allPackets = {};
        $scope.data.allStates = [];
        $scope.data.allComboData = {};
        $scope.data.allCheckedComboData = {};
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


        $http
            .get(contextPath + loadDataPath, {withCredentials: true})
            .success(function (data) {
                prepareCompts(data, initialPacketIndex);

                preparePackets(data);
                prepareStates(data);
                prepareComboData(data);
                $scope.data.allComboData[comptId] =;
                $scope.data.allCheckedComboData[comptId] =;
                if (!$scope.data.loadedNoCompts
                    && !$scope.data.loadedNoComboData
                    && !$scope.data.loadedNoStates) {

                    prepareComptsSupplInfo(data);
                }
            })
            .error(function (error) {
                $scope.data.loadError = error;
            });

        $scope.$watch('data.selectedComptLabels', function (value) {
            $scope.data.noComptsSelected = angular.equals({}, value);
        });

        $scope.$watch('data.allPackets', function (value) {
            $scope.data.loadedNoPackets = angular.equals({}, value);
        });

        $scope.$watch('data.selectedPacket', function (value) {
            if (value != null) {
                $scope.data.selectedPacketId = value.id;
                if (!newPackets[$scope.data.selectedPacketId]) {
                    selectedComptLabelsToAdd = comptLabelsToAddToExistingPackets;
                } else {
                    selectedComptLabelsToAdd = comptLabelsToAddToNewPackets;
                }
            } else {
                $scope.data.selectedPacketId = null;
                selectedComptLabelsToAdd = null;
            }
        });

        prepareCompts = function (data, initialPacketInd) {
            $scope.data.loadedNoCompts = isDataEmpty(data.compts);
            var packetInd = initialPacketInd;
            var visitedPacket = {};
            angular.forEach(data.compts, function (el) {
                var id = el.id;
                var label = el.label.toUpperCase();
                var packetId = el.packetId;
                if (!visitedPacket[packetId]) {
                    visitedPacket[packetId] = true;
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
        };

        preparePackets = function (data) {
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
        };

        prepareStates = function (data) {
            $scope.data.loadedNoStates = isDataEmpty(data.states);
            $scope.data.allStates = data.states;
            $scope.data.allStateLabels = [labelLabel];
            angular.forEach($scope.data.allStates, function (state) {
                $scope.data.allStateLabels.push(state.label);
            });
        };

        prepareComboData = function (data) {
            $scope.data.loadedNoComboData = isDataEmpty(data.comboData);
            angular.forEach(data.comboData, function (cd) {
                $scope.data.comboDataDefaultSet.push(cd.label);
            });
            angular.forEach($scope.data.allStates, function () {
                $scope.data.newComptCheckedVals.push($scope.data.comboDataDefaultSet[0]);
            });
        };

        prepareComptsSupplInfo = function (data) {
            $scope.data.loadedNoComptSupplInfo = isDataEmpty(data.comptSupplInfo);
            var comboDataVisitedCompts = {};
            var checkedComboDataVisitedCompts = {};

            angular.forEach(data.comptSupplInfo, function (el) {
                var comptId = el.comptId;
                var stateId = el.stateId;
                var label = el.label;
                if (!comboDataVisitedCompts[comptId]) {
                    comboDataVisitedCompts[comptId] = true;
                    $scope.data.allComboData[comptId] = {};
                }
                $scope.data.allComboData[comptId][stateId] = $scope.data.allComboData[comptId][stateId] || [];
                $scope.data.allComboData[comptId][stateId].push(label);
                var checked = el.checked;
                if (checked) {
                    if (!checkedComboDataVisitedCompts[comptId]) {
                        checkedComboDataVisitedCompts[comptId] = true;
                        $scope.data.allCheckedComboData[comptId] = {};
                    }
                    $scope.data.allCheckedComboData[comptId][stateId] = label;
                }
            });
        };

        $scope.addComptLocally = function () {
            var comptId = ++maximalComptIndex;
            var usualLabel = $scope.data.newLabel;
            var upperCaseLabel = usualLabel.toUpperCase();
            var newCompt = {id: comptId, label: usualLabel};
            $scope.data.selectedComptLabels[upperCaseLabel] = true;
            comptIdToInd[comptId] = $scope.data.selectedCompts.length;
            $scope.data.selectedCompts.push(newCompt);
            var pktId = $scope.data.selectedPacketId;
            selectedComptLabelsToAdd[pktId] = selectedComptLabelsToAdd[pktId] || {};
            selectedComptLabelsToAdd[pktId][comptId] = usualLabel;
            $scope.data.allComboData[comptId] = {};
            $scope.data.allCheckedComboData[comptId] = {};
            for (var i = 1; i <= $scope.data.allStates.length; i++) {
                $scope.data.allComboData[comptId][i] = $scope.data.comboDataDefaultSet;
                $scope.data.allCheckedComboData[comptId][i] = $scope.data.newComptCheckedVals[i - 1];
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
            var pktId = $scope.data.selectedPacketId;

            delete $scope.data.selectedComptLabels[comptLabel.toUpperCase()];
            $scope.data.selectedCompts[comptIdToInd[comptId]] = null;
            delete $scope.data.allCheckedComboData[comptId];
            delete $scope.data.allComboData[comptId];
            if (comptIdsToUpdate[pktId]) {
                delete comptIdsToUpdate[pktId][comptId];
            }
            if (selectedComptLabelsToAdd[pktId] && selectedComptLabelsToAdd[pktId][comptId]) {
                delete selectedComptLabelsToAdd[pktId][comptId];
            } else {
                comptIdsTaggedToDelete[pktId] = comptIdsTaggedToDelete[pktId] || [];
                comptIdsTaggedToDelete[pktId].push(comptId);
            }
        };

        $scope.deletePacketLocally = function (packet) {
            var packetId = packet.id;
            if (!newPackets[packetId]) {
                packetIdsToDelete.push(packetId);
            }
            delete selectedComptLabelsToAdd[packetId];
            delete $scope.data.allPackets[packetId];
            delete comptIdsTaggedToDelete[packetId];
            $scope.data.selectedPacket = null;
            $scope.data.isPacketSelected = false;
        };

        $scope.updateComptLocally = function (compt) {
            var pktId = $scope.data.selectedPacketId;
            var comptId = compt.id;
            if (selectedComptLabelsToAdd[pktId][comptId]) {
                return;
            }
            comptIdsToUpdate[pktId] = comptIdsToUpdate[pktId] || {};
            comptIdsToUpdate[pktId][comptId] = true;
        };

        isDataEmpty = function (data) {
            if (!data || !angular.isArray(data) || data.length == 0) {
                $scope.data.dataEmpty = true;
                return true;
            }
            return false;
        };

        findCheckedValsForCompt = function (comptId) {
            var checkedVals = [];
            angular.forEach($scope.data.allStates, function (state, ind) {
                checkedVals.push($scope.data.allCheckedComboData[comptId][ind + 1]);
            });
            return checkedVals;
        };

        $scope.loadPacketById = function (packetId) {
            var packetIndex = packetIdToInd[packetId];
            $http
                .get(contextPath + loadDataPath, {packetId: packetId, withCredentials: true})
                .success(function (data) {
                    prepareCompts(data, packetIndex - 1);
                    preparePackets(data);
                    prepareStates(data);
                    prepareComboData(data);
                    if (!$scope.data.loadedNoCompts
                        && !$scope.data.loadedNoComboData
                        && !$scope.data.loadedNoStates) {

                        prepareComptsSupplInfo(data);
                    }
                })
                .error(function (error) {
                });
        };
        $scope.saveAllChangesToBase = function () {
            var packetsToUpdateParamsList = [];
            var packetsToAddParamsList = [];
            var comptsToUpdateParamsList = [];

            angular.forEach($scope.data.allPackets, function (pkt, pktId) {
                var packetConfig = {};
                var comptConfig = {};
                if (!newPackets[pktId]) {
                    comptConfig.updatedComptParamsList = generateToUpdateComptParamsListForPacketsToUpdate(pktId);
                    if (comptConfig.updatedComptParamsList.length > 0) {
                        comptsToUpdateParamsList.push(comptConfig);
                    }

                    packetConfig.id = pktId;
                    if (pkt.stateId != packetInitialStateIds[pktId]) {
                        packetConfig.stateId = pkt.stateId;
                    }
                    packetConfig.addedComptParamsList = generateToAddComptParamsListForPacketsToUpdate(pktId);
                    if (packetConfig.stateId || packetConfig.addedComptParamsList.length > 0) {
                        packetsToUpdateParamsList.push(packetConfig);
                    }
                } else {
                    packetConfig.stateId = pkt.stateId;
                    packetConfig.addedComptParamsList = generateComptParamsListForPacketsToAdd(pktId);
                    packetsToAddParamsList.push(packetConfig);
                }
            });

            var comptIdsToDelete = [];
            angular.forEach($scope.data.allPackets, function (unused, pktId) {
                comptIdsToDelete = comptIdsToDelete.concat(comptIdsTaggedToDelete[pktId]);
            });

            if (packetsToUpdateParamsList.length == 0 && packetsToAddParamsList.length == 0
                && comptIdsToDelete.length == 0 && packetIdsToDelete.length == 0
                && comptIdsToUpdate.length == 0) {
                return;
            }

            var saveAllChangesConfig = {
                withCredentials: true,
                params: {
                    packetsToAddParamsList: packetsToAddParamsList,
                    packetsToUpdateParamsList: packetsToUpdateParamsList,
                    comptsToUpdateParamsList: comptsToUpdateParamsList,
                    packetIdsToDelete: packetIdsToDelete,
                    comptIdsToDelete: comptIdsToDelete
                }
            };
            var errorMap = {};

            errorMap[updateCompts] = comptIdsToUpdate;
            errorMap[deleteCompts] = comptIdsTaggedToDelete;
            errorMap[updatePackets] = comptLabelsToAddToExistingPackets;
            errorMap[addPackets] = comptLabelsToAddToNewPackets;
            errorMap[deletePackets] = packetIdsToDelete;


            $http
                .post(contextPath + saveAllChangesToBasePath, saveAllChangesConfig)
                .success(function (data) {
                    angular.forEach(data, function (el) {
                        var key = String(el);
                        if (errorMap[key]) {
                            delete errorMap[key];
                        }
                    });
                    angular.forEach(errorMap, function (v, k) {
                        if (k != deletePackets) {
                            v = {};
                        } else {
                            v = [];
                        }
                    });
                })
                .error(function (error) {
                });
            comptLabelsToAddToNewPackets = {};
            comptLabelsToAddToExistingPackets = {};
            comptIdsToUpdate = {};
            comptIdsTaggedToDelete = {};
            packetIdsToDelete = [];
        };

        generateComptParamsListForPacketsToAdd = function (pktId) {
            var result = [];
            angular.forEach(comptLabelsToAddToNewPackets[pktId], function (lbl, comptId) {
                result.push({label: lbl, vals: findCheckedValsForCompt(comptId)});
            });
            return result;
        };

        generateToAddComptParamsListForPacketsToUpdate = function (pktId) {
            var result = [];
            angular.forEach(comptLabelsToAddToExistingPackets[pktId], function (lbl, comptId) {
                result.push({label: lbl, vals: findCheckedValsForCompt(comptId)});
            });
            return result;
        };

        generateToUpdateComptParamsListForPacketsToUpdate = function (pktId) {
            var result = [];
            angular.forEach(comptIdsToUpdate[pktId], function (unused, comptId) {
                result.push({id: comptId, vals: findCheckedValsForCompt(comptId)});
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