/**
 * Created by alexc_000 on 2016-08-30.
 */

angular.module("packetAppAdmin")
    .constant("packetListActiveClass", "btn-primary btn-sm")
    .constant("packetListNonActiveClass", "btn-sm")
    .constant("updateCompts", "UPDATE_COMPTS")
    .constant("deleteCompts", "DELETE_COMPTS")
    .constant("updatePackets", "UPDATE_PACKETS")
    .constant("deletePackets", "DELETE_PACKETS")
    .constant("addPackets", "ADD_PACKETS")
    .constant("saveAllChangesToBaseUrl", "/saveAllChangesToBase")
    .constant("initialPacketIndex", -1)
    .constant("errorStatus404", 404)
    .constant("narrowPacketCaption", "narrow-packet-caption")
    .constant("widePacketCaption", "wide-packet-caption")
    .constant("adminRole", "ADMIN")
    .controller("packetsPanelCtrl", function ($scope, $http, $location, packetListActiveClass, packetListNonActiveClass,
                                              updateCompts, deleteCompts, updatePackets, deletePackets, addPackets,
                                              saveAllChangesToBaseUrl, errorStatus404, narrowPacketCaption,
                                              widePacketCaption, adminRole) {
        var data;

        $scope.showAggregateButtons = function () {
            return !$scope.$parent.data.loadError && $scope.$parent.data.loadedNoStates === false
                && $scope.$parent.data.loadedNoComboData === false;
        };

        $scope.addPacketLocally = function () {
            var newPacket = {id: ++$scope.$parent.data.maximalPacketId, stateId: 1};
            $scope.$parent.data.packetIdToInd[$scope.$parent.data.maximalPacketId] 
                = ++$scope.$parent.data.maximalPacketIndex;
            $scope.$parent.data.newPackets[$scope.$parent.data.maximalPacketId] = newPacket;
            $scope.$parent.data.allPackets[$scope.$parent.data.maximalPacketId] = newPacket;
            $scope.$parent.data.loadEmpty = null;
        };

        $scope.deletePacketLocally = function (packet) {
            var packetId = packet.id;
            if (!$scope.$parent.data.newPackets[packetId]) {
                data.packetIdsToDelete.push(packetId);
            }
            var isPktNew = packetId in $scope.$parent.data.newPackets;
            delete $scope.$parent.data.newComptLabels[isPktNew][packetId];
            delete $scope.$parent.data.allPackets[packetId];
            delete $scope.$parent.data.comptIdsTaggedToDelete[packetId];
            $scope.$parent.data.selectedPacket = null;
        };

        $scope.reloadRoute = function () {
            $scope.$parent.init();
        };

        $scope.getPacketClass = function (packet) {
            var pktId = packet.id;
            var result = $scope.$parent.data.selectedPacket && $scope.$parent.data.selectedPacket.id == pktId
                ? packetListActiveClass
                : packetListNonActiveClass;
            result += " ";

            if (pktId < 10) {
                result += narrowPacketCaption;
            } else {
                result += widePacketCaption;
            }
            return result;
        };

        $scope.saveAllChangesToBase = function (savedPacketId) {
            var dataParams = generateDataParamsForSaving(savedPacketId);
            var errorMap = generateErrorMap();

            $http
                .post(contextPath + saveAllChangesToBaseUrl, {dataParams: dataParams})
                .then(
                    function success(data) {
                        angular.forEach(data, function (el) {
                            var key = String(el);
                            if (errorMap[key]) {
                                delete errorMap[key];
                                if (key == addPackets || key == updatePackets) {
                                    alert("Error occurred while trying to save data in DB. The STATES table is empty." +
                                    "The " + key == addPackets ? "new" : "updated" + " packets were not persisted " +
                                    "to the DB. Try to solve the problem and then re-push the saving button. " +
                                    "DON'T RESTORE THE DATA FROM THE BASE! Otherwise you may loose your changes.");
                                }
                                else if (key == updateCompts) {
                                    alert("Error occurred while trying to save data in DB. The COMBO_DATA table" +
                                        " is empty. At least one of the following took place: " +
                                        "1. The compts adding and/or " +
                                        "state change for the existing packets were not persisted; " +
                                        "2. Added new pakets together with the new compts inside were not persisted. " +
                                        "Try to solve the problem and then re-push the saving button. " +
                                        "DON'T RESTORE THE DATA FROM THE BASE! Otherwise you may loose your changes.");
                                }
                            }
                        });
                        clearCollectionsForSaving(errorMap);
                    },
                    function error(error) {
                        if (error.status == errorStatus404) {
                            clearCollectionsForSaving(errorMap);
                        }
                    }
                );
        };

        var generateComptParamsListForPackets = function (pktId, operation) {
            var result = [];
            angular.forEach($scope.$parent.data.newComptLabels[operation == "add"][pktId], function (lbl, comptId) {
                result.push({label: lbl, vals: findCheckedValsForCompt(comptId)});
            });
            return result;
        };

        var generateComptParamsListToUpdateForPackets = function (pktId) {
            var result = [];
            angular.forEach($scope.$parent.data.comptIdsToUpdate[pktId], function (unused, comptId) {
                result.push({id: comptId, vals: findCheckedValsForCompt(comptId)});
            });
            return result;
        };

        var findCheckedValsForCompt = function (comptId) {
            var checkedVals = [];
            angular.forEach($scope.$parent.data.allStates, function (state, ind) {
                checkedVals.push($scope.$parent.data.allCheckedComboData[comptId][ind + 1]);
            });
            return checkedVals;
        };

        var generateDataParamsForSaving = function (savedPacketId) {
            var packetsToUpdateParamsList = [];
            var packetsToAddParamsList = [];
            var comptsToUpdateParamsList = [];

            var packetsToSave = {};
            if (!savedPacketId) {
                packetsToSave = $scope.$parent.data.allPackets;
            } else {
                packetsToSave[savedPacketId] = $scope.$parent.data.allPackets[savedPacketId];
            }

            for (var pktId in packetsToSave) {
                var pkt = packetsToSave[pktId];
                var packetConfig = {};
                if (!$scope.$parent.data.newPackets[pktId]) {
                    var updatedComptParamsList = generateComptParamsListToUpdateForPackets(pktId);
                    if (updatedComptParamsList.length > 0) {
                        comptsToUpdateParamsList = comptsToUpdateParamsList.concat(updatedComptParamsList);
                    }

                    packetConfig.id = pktId;
                    if (pkt.stateId != $scope.$parent.data.packetInitialStateIds[pktId]) {
                        packetConfig.stateId = pkt.stateId;
                    }
                    packetConfig.newComptParamsList = generateComptParamsListForPackets(pktId, "update");
                    if (packetConfig.stateId || packetConfig.newComptParamsList.length > 0) {
                        packetsToUpdateParamsList.push(packetConfig);
                    }
                } else {
                    packetConfig.stateId = pkt.stateId;
                    packetConfig.newComptParamsList = generateComptParamsListForPackets(pktId, "add");
                    packetsToAddParamsList.push(packetConfig);
                }
            }
            var comptIdsToDelete = [];
            angular.forEach(packetsToSave, function (unused, pktId) {
                if (!$scope.$parent.isDataEmpty($scope.$parent.data.comptIdsTaggedToDelete[pktId])) {
                    comptIdsToDelete = comptIdsToDelete.concat($scope.$parent.data.comptIdsTaggedToDelete[pktId]);
                }
            });

            if (packetsToUpdateParamsList.length == 0 && packetsToAddParamsList.length == 0
                && comptIdsToDelete.length == 0 && comptsToUpdateParamsList.length == 0
                && (savedPacketId || data.packetIdsToDelete.length == 0)) {
                return;
            }

            var params = {
                packetsToAddParamsList: packetsToAddParamsList,
                packetsToUpdateParamsList: packetsToUpdateParamsList,
                comptsToUpdateParamsList: comptsToUpdateParamsList,
                comptIdsToDelete: comptIdsToDelete
            };
            if (!savedPacketId) {
                params['packetIdsToDelete'] = data.packetIdsToDelete;
            } else {
                params['packetId'] = savedPacketId;
            }
            return params;
        };

        var generateErrorMap = function () {
            var errorMap = {};

            errorMap[updateCompts] = $scope.$parent.data.comptIdsToUpdate;
            errorMap[deleteCompts] = $scope.$parent.data.comptIdsTaggedToDelete;
            errorMap[updatePackets] = $scope.$parent.data.newComptLabels[false];
            errorMap[addPackets] = $scope.$parent.data.newPackets;
            errorMap[deletePackets] = data.packetIdsToDelete;

            return errorMap;
        };

        var clearCollectionsForSaving = function (errorMap) {
            angular.forEach(errorMap, function (v, k) {
                if (k != deletePackets) {
                    v = {};
                } else {
                    v = [];
                }
            });
        };

        var init = function () {
            data = {};
            data.packetIdsToDelete = [];
            $scope.data = {};
            $scope.data.adminRoleTitle = adminRole;
        };

        init();
    });
