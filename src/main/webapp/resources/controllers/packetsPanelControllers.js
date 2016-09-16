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
    .constant("role", "role")
    .controller("packetsPanelCtrl", function ($scope, $http, $window, $cookies, exchangeService,
                                              packetListActiveClass, packetListNonActiveClass, updateCompts,
                                              deleteCompts, updatePackets, deletePackets, addPackets,
                                              saveAllChangesToBaseUrl, errorStatus404, narrowPacketCaption,
                                              widePacketCaption, role, adminRole) {

        var packetIdsToDelete;

        $scope.$on('allPackets:update', function (event, data) {
            $scope.data.allPackets = data;
        });

        $scope.$on('newPackets:update', function (event, data) {
            $scope.data.newPackets = data;
        });

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
            exchangeService.setPacketIdToInd(maximalPktIndex, maximalPktId);
            exchangeService.setNewPackets(newPacket, maximalPktId);
            exchangeService.setAllPackets(newPacket, maximalPktId);
            exchangeService.setLoadEmpty(false);
        };

        $scope.deletePacketLocally = function (packet) {
            var pktId = packet.id;
            var isPktNew = pktId in $scope.data.newPackets;
            if (!isPktNew) {
                packetIdsToDelete.push(pktId);
            }
            exchangeService.deleteNewComptLabels(pktId);
            exchangeService.deleteAllPackets(pktId);
            exchangeService.deleteComptIdsToDelete(pktId);
            if (pktId === exchangeService.getSelectedPacketId()) {
                exchangeService.setSelectedPacket(null);
            }    
        };

        $scope.reloadRoute = function () {
            $window.location.reload();
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
            var errorMap = generateErrorMap(savedPktId);

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
                        clearCollectionsForSaving(errorMap, savedPktId);
                    },
                    function error(error) {
                        if (String(error.status).startsWith("4")) {
                            clearCollectionsForSaving(errorMap, savedPktId);
                        }
                    }
                );
        };

        var generateComptParamsListForPackets = function (pktId) {
            var result = [];
            angular.forEach(exchangeService.getNewComptLabels(pktId), function (lbl, comptId) {
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
            if (angular.isUndefined(savedPktId)) {
                packetsToSave = $scope.data.allPackets;
            } else {
                packetsToSave[savedPktId] = $scope.data.allPackets[savedPktId];
            }

            for (var pktId in packetsToSave) {
                var pkt = packetsToSave[pktId];
                var packetConfig = {};
                if (!(pktId in $scope.data.newPackets)) {
                    var updatedComptParamsList = generateComptParamsListToUpdateForPackets(pktId);
                    if (updatedComptParamsList.length > 0) {
                        comptsToUpdateParamsList = comptsToUpdateParamsList.concat(updatedComptParamsList);
                    }

                    packetConfig.id = pktId;
                    if (pkt.stateId != exchangeService.getPacketInitialStateIds(pktId)) {
                        packetConfig.stateId = pkt.stateId;
                    }
                    packetConfig.newComptParamsList = generateComptParamsListForPackets(pktId);
                    if (packetConfig.stateId || packetConfig.newComptParamsList.length > 0) {
                        packetsToUpdateParamsList.push(packetConfig);
                    }
                } else {
                    packetConfig.stateId = pkt.stateId;
                    packetConfig.newComptParamsList = generateComptParamsListForPackets(pktId);
                    packetsToAddParamsList.push(packetConfig);
                }
            }
            var comptIdsToDelete = [];
            angular.forEach(packetsToSave, function (unused, pktId) {
                var comptIdsTaggedToDelete = exchangeService.getComptIdsToDelete(pktId);
                if (!exchangeService.isEmpty(comptIdsTaggedToDelete)) {
                    comptIdsToDelete = comptIdsToDelete.concat(comptIdsTaggedToDelete);
                }
            });

            if (packetsToUpdateParamsList.length == 0 && packetsToAddParamsList.length == 0
                && comptIdsToDelete.length == 0 && comptsToUpdateParamsList.length == 0
                && (savedPktId || packetIdsToDelete.length == 0)) {
                return;
            }

            var params = {
                packetsToAddParamsList: packetsToAddParamsList,
                packetsToUpdateParamsList: packetsToUpdateParamsList,
                comptsToUpdateParamsList: comptsToUpdateParamsList,
                comptIdsToDelete: comptIdsToDelete
            };
            if (angular.isUndefined(savedPktId)) {
                params['packetIdsToDelete'] = packetIdsToDelete;
            } else {
                params['packetId'] = savedPktId;
            }
            return params;
        };

        var generateErrorMap = function (savedPktId) {
            var pktIdIsUndefined = angular.isUndefined(savedPktId);

            var errorMap = {};

            errorMap[updateCompts] = true;
            errorMap[deleteCompts] = true;
            errorMap[updatePackets] = true;
            errorMap[addPackets] = true;
            if (pktIdIsUndefined) {
                errorMap[deletePackets] = true;
            }

            return errorMap;
        };

        var purgePacketIdsToDelete = function () {
            packetIdsToDelete = [];
        };

        var clearCollectionsForSaving = function (errorMap, savedPktId) {
            if (updateCompts in errorMap) {
                exchangeService.deleteComptIdsToUpdate(savedPktId);
            }
            if (deleteCompts in errorMap) {
                exchangeService.deleteComptIdsToDelete(savedPktId);
            }
            if (updatePackets in errorMap) {
                exchangeService.deleteNewComptLabels(savedPktId);
            }
            if (addPackets in errorMap) {
                exchangeService.deleteNewPackets(savedPktId);
            }
            if (deletePackets in errorMap) {
                purgePacketIdsToDelete();
            }
        };

        var init = function () {
            $scope.data = {};
            $scope.data.adminRoleTitle = adminRole;
            $scope.data.isRoleNotAdmin = $cookies.get(role) !== $scope.data.adminRoleTitle;
            $scope.data.allPackets = {};
            $scope.data.newPackets = {};
            packetIdsToDelete = [];
        };

        init();
    });
