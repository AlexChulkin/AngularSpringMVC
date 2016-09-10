/**
 * Created by alexc_000 on 2016-08-30.
 */

angular.module("packetAdminApp")
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
    .controller("packetsPanelCtrl", function ($scope, $http, $location, $cookies, exchangeService,
                                              packetListActiveClass, packetListNonActiveClass, updateCompts,
                                              deleteCompts, updatePackets, deletePackets, addPackets,
                                              saveAllChangesToBaseUrl, errorStatus404, narrowPacketCaption,
                                              widePacketCaption, adminRole) {
        var data;

        $scope.showAggregateButtons = function () {
            return !exchangeService.getLoadError() && exchangeService.getLoadedNoStates() === false
                && exchangeService.getLoadedNoComboData() === false;
        };

        $scope.addPacketLocally = function () {
            var maximalPktId = exchangeService.getMaximalPacketId() + 1;
            exchangeService.setMaximalPacketId(maximalPktId);
            var maximalPktIndex = exchangeService.getMaximalPacketIndex() + 1;
            exchangeService.setMaximalPacketIndex(maximalPktIndex);
            var newPacket = {id: maximalPktId, stateId: 1};
            exchangeService.setPacketIdToInd(maximalPktId, maximalPktIndex);
            exchangeService.setNewPackets(maximalPktId, newPacket);
            exchangeService.setAllPackets(maximalPktId, newPacket);
            exchangeService.setLoadEmpty(null);
        };

        $scope.deletePacketLocally = function (packet) {
            var pktId = packet.id;
            if (!exchangeService.getNewPackets(pktId)) {
                data.packetIdsToDelete.push(pktId);
            }
            var isPktNew = pktId in exchangeService.getNewPackets();
            exchangeService.deleteNewComptLabels(isPktNew, pktId);
            exchangeService.deleteAllPackets(pktId);
            exchangeService.deleteComptIdsToDelete(pktId);
            exchangeService.setSelectedPacket(null);
        };

        $scope.reloadRoute = function () {
            $location.reload();
        };

        $scope.getPacketClass = function (packet) {
            var pktId = packet.id;
            var result = exchangeService.getSelectedPacketId() == pktId
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

        $scope.saveAllChangesToBase = function (savedPktId) {
            var dataParams = generateDataParamsForSaving(savedPktId);
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
            angular.forEach(exchangeService.getNewComptLabels(operation === "add", pktId), function (lbl, comptId) {
                result.push({label: lbl, vals: findCheckedValsForCompt(comptId)});
            });
            return result;
        };

        var generateComptParamsListToUpdateForPackets = function (pktId) {
            var result = [];
            angular.forEach(exchangeService.getComptIdsToUpdate(pktId), function (unused, comptId) {
                result.push({id: comptId, vals: findCheckedValsForCompt(comptId)});
            });
            return result;
        };

        var findCheckedValsForCompt = function (comptId) {
            var checkedVals = [];
            angular.forEach(exchangeService.getAllStates(), function (state, ind) {
                checkedVals.push(exchangeService.getAllCheckedComboData(comptId, ind + 1));
            });
            return checkedVals;
        };

        var generateDataParamsForSaving = function (savedPktId) {
            var packetsToUpdateParamsList = [];
            var packetsToAddParamsList = [];
            var comptsToUpdateParamsList = [];

            var packetsToSave = {};
            if (!savedPktId) {
                packetsToSave = exchangeService.getAllPackets();
            } else {
                packetsToSave[savedPktId] = exchangeService.getAllPackets(savedPktId);
            }

            for (var pktId in packetsToSave) {
                var pkt = packetsToSave[pktId];
                var packetConfig = {};
                if (!exchangeService.getNewPackets(pktId)) {
                    var updatedComptParamsList = generateComptParamsListToUpdateForPackets(pktId);
                    if (updatedComptParamsList.length > 0) {
                        comptsToUpdateParamsList = comptsToUpdateParamsList.concat(updatedComptParamsList);
                    }

                    packetConfig.id = pktId;
                    if (pkt.stateId != exchangeService.getPacketInitialStateIds(pktId)) {
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
                var comptIdsTaggedToDelete = exchangeService.getComptIdsTaggedToDelete(pktId);
                if (!$scope.$parent.isDataEmpty(comptIdsTaggedToDelete)) {
                    comptIdsToDelete = comptIdsToDelete.concat(comptIdsTaggedToDelete);
                }
            });

            if (packetsToUpdateParamsList.length == 0 && packetsToAddParamsList.length == 0
                && comptIdsToDelete.length == 0 && comptsToUpdateParamsList.length == 0
                && (savedPktId || data.packetIdsToDelete.length == 0)) {
                return;
            }

            var params = {
                packetsToAddParamsList: packetsToAddParamsList,
                packetsToUpdateParamsList: packetsToUpdateParamsList,
                comptsToUpdateParamsList: comptsToUpdateParamsList,
                comptIdsToDelete: comptIdsToDelete
            };
            if (!savedPktId) {
                params['packetIdsToDelete'] = data.packetIdsToDelete;
            } else {
                params['packetId'] = savedPacketId;
            }
            return params;
        };

        var generateErrorMap = function () {
            var errorMap = {};

            errorMap[updateCompts] = exchangeService.getComptIdsToUpdate();
            errorMap[deleteCompts] = exchangeService.getComptIdsToDelete();
            errorMap[updatePackets] = exchangeService.getNewComptLabels(false);
            errorMap[addPackets] = exchangeService.getNewPackets();
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
            $scope.data.isRoleNotAdmin = $cookies.get("role") !== $scope.data.adminRoleTitle;
        };

        init();
    });
