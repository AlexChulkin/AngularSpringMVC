/**
 * Created by achulkin on 04.06.14.   
 */
var app = angular.module("packetApp");
app.constant("packetListActiveClass", "btn-primary btn-sm")
    .constant("packetListNonActiveClass", "btn-sm")
    .constant("packetListPageCount", 10)
    .constant("labelLabel", "Label")
    .constant("updateCompts", "UPDATE_COMPTS")
    .constant("deleteCompts", "DELETE_COMPTS")
    .constant("updatePackets", "UPDATE_PACKETS")
    .constant("deletePackets", "DELETE_PACKETS")
    .constant("addPackets", "ADD_PACKETS")
    .constant("loadDataPath", "/loadData")
    .constant("saveAllChangesToBasePath", "/saveAllChangesToBase")
    .constant("loadPacketByIdPath", "/loadPacketById")
    .constant("initialPacketIndex", -1)
    .constant("errorStatus404", 404)
    .constant("swallowPacketCaption", "swallow-packet-caption")
    .constant("widePacketCaption", "wide-packet-caption")
    .controller("packetCtrl", function ($scope, $http, $window, packetListActiveClass,
                                        packetListNonActiveClass, packetListPageCount, labelLabel,
                                        updateCompts, deleteCompts, updatePackets, deletePackets,
                                        addPackets, loadDataPath, saveAllChangesToBasePath,
                                        loadPacketByIdPath, initialPacketIndex, errorStatus404,
                                        swallowPacketCaption, widePacketCaption) {

        var comptIdToInd = {};
        var packetIdToInd = {};
        var comptLabels = {};
        var compts = [];
        var maximalComptId = 0;
        var maximalPacketId = 0;
        var maximalPacketIndex = 0;
        var comptIdsTaggedToDelete = {};
        var comptIdsToUpdate = {};
        var newComptLabels = {true: {}, false: {}};


        var newPackets = {};
        var packetIdsToDelete = [];

        var packetInitialStateIds = {};

        var loadError = null;
        var loadedNoCompts = null;
        var loadedNoStates = null;
        var loadedNoComboData = null;
        var loadedNoComptSupplInfo = null;
        var ifComptsIsSelected = null;
        var loadedNoPackets = null;
        var isSelectedPacketNew = null;
        var ifPacketIsNotSelectedSoFar = true;

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

        $scope.data.selectedPacket = null;
        $scope.data.selectedPacketId = null;

        $scope.$watch('data.selectedPacket', function (newValue, oldValue) {
            if (newValue) {
                if (oldValue) {
                    var oldSelectedPacketId = oldValue.id;
                    compts[packetIdToInd[oldSelectedPacketId]] = $scope.data.selectedCompts;
                    comptLabels[oldSelectedPacketId] = $scope.data.selectedComptLabels;
                }
                ifPacketIsNotSelectedSoFar = false;
                $scope.data.selectedPacketId = newValue.id;
                isSelectedPacketNew = $scope.data.selectedPacketId in newPackets;
                $scope.data.selectedCompts = compts[packetIdToInd[$scope.data.selectedPacketId]] || [];
                $scope.data.selectedComptLabels = comptLabels[$scope.data.selectedPacketId] || {};
            } else {
                $scope.data.selectedPacketId = null;
                $scope.data.selectedCompts = [];
                $scope.data.selectedComptLabels = {};
                // ifComptsIsSelected = false;
                isSelectedPacketNew = null;
            }
        });

        $scope.$watchCollection('data.selectedComptLabels', function (value) {
            ifComptsIsSelected = !angular.equals({}, value);
        });

        $scope.isComptsSelected = function () {
            return ifComptsIsSelected;
        };

        $scope.isPacketSelected = function () {
            return $scope.data.selectedPacket;
        };

        $scope.isPacketNeverSelectedSoFar = function () {
            return ifPacketIsNotSelectedSoFar;
        };

        $scope.isDataLoadedProperly = function () {
            return !($scope.isDataLoadError() || $scope.isComboDataNotLoaded() || $scope.isStatesNotLoaded());
        };

        $scope.showAggregateButtons = function () {
            return !loadError && loadedNoCompts === false && loadedNoComboData === false;
        };

        $scope.isDataLoadError = function () {
            return loadError;
        };

        $scope.isComptsNotLoaded = function () {
            return loadedNoCompts;
        };
        
        $scope.isComboDataNotLoaded = function () {
            return loadedNoComboData;
        };

        $scope.isStatesNotLoaded = function () {
            return loadedNoStates;
        };

        $scope.isPacketsNotLoaded = function () {
            return loadedNoPackets;
        };

        $scope.addComptLocally = function () {
            var comptId = ++maximalComptId;
            var usualLabel = $scope.data.newLabel;
            var upperCaseLabel = usualLabel.toUpperCase();
            var newCompt = {id: comptId, label: usualLabel};
            $scope.data.selectedComptLabels[upperCaseLabel] = true;
            comptIdToInd[comptId] = $scope.data.selectedCompts.length;
            $scope.data.selectedCompts.push(newCompt);
            var pktId = $scope.data.selectedPacketId;
            newComptLabels[isSelectedPacketNew][pktId]
                = newComptLabels[isSelectedPacketNew][pktId] || {};
            newComptLabels[isSelectedPacketNew][pktId][comptId] = usualLabel;
            $scope.data.allComboData[comptId] = {};
            $scope.data.allCheckedComboData[comptId] = {};
            for (var i = 1; i <= $scope.data.allStates.length; i++) {
                $scope.data.allComboData[comptId][i] = $scope.data.comboDataDefaultSet;
                $scope.data.allCheckedComboData[comptId][i] = $scope.data.newComptCheckedVals[i - 1];
            }
            $scope.data.newLabel = null;
        };

        $scope.addPacketLocally = function () {
            var newPacket = {id: ++maximalPacketId, stateId: 1};
            packetIdToInd[maximalPacketId] = ++maximalPacketIndex;
            newPackets[maximalPacketId] = newPacket;
            $scope.data.allPackets[maximalPacketId] = newPacket;
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
            if (newComptLabels[isSelectedPacketNew][pktId]) {
                delete newComptLabels[isSelectedPacketNew][pktId][comptId];
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
            var isPktNew = packetId in newPackets;
            delete newComptLabels[isPktNew][packetId];
            delete $scope.data.allPackets[packetId];
            delete comptIdsTaggedToDelete[packetId];
            $scope.data.selectedPacket = null;
        };

        $scope.updateComptLocally = function (compt) {
            var pktId = $scope.data.selectedPacketId;
            var comptId = compt.id;
            if (newComptLabels[isSelectedPacketNew][pktId] &&
                newComptLabels[isSelectedPacketNew][pktId][comptId]) {
                return;
            }
            comptIdsToUpdate[pktId] = comptIdsToUpdate[pktId] || {};
            comptIdsToUpdate[pktId][comptId] = true;
        };

        $scope.loadPacketById = function (packetId) {
            var packetIndex = packetId == null ? initialPacketIndex : packetIdToInd[packetId];
            var params = packetId == null ? {} : {packetId: packetId};
            $http
                .post(contextPath + loadDataPath, {params: params})
                .then(
                    function success(result) {
                        var data = result.data;
                        prepareCompts(data.compts, packetIndex);
                        preparePackets(data.packets);
                        prepareStates(data.states);
                        prepareComboData(data.comboData);
                        if (!$scope.isComptsNotLoaded()
                            && !$scope.isComboDataNotLoaded()
                            && !$scope.isStatesNotLoaded()) {

                            prepareComptsSupplInfo(data.comptSupplInfo);
                        }
                    },
                    function error(error) {
                        $scope.data.loadError = true;
                    }
                );
        };

        $scope.saveAllChangesToBase = function (savedPacketId) {
            var packetsToUpdateParamsList = [];
            var packetsToAddParamsList = [];
            var comptsToUpdateParamsList = [];

            var packetsToSave = {};
            if (!savedPacketId) {
                packetsToSave = $scope.data.allPackets;
            } else {
                packetsToSave[savedPacketId] = $scope.data.allPackets[savedPacketId];
            }

            angular.forEach(packetsToSave, function (pkt, pktId) {
                var packetConfig = {};
                if (!newPackets[pktId]) {
                    var updatedComptParamsList = generateToUpdateComptParamsListForPacketsToUpdate(pktId);
                    if (updatedComptParamsList.length > 0) {
                        comptsToUpdateParamsList = comptsToUpdateParamsList.concat(updatedComptParamsList);
                    }

                    packetConfig.id = pktId;
                    if (pkt.stateId != packetInitialStateIds[pktId]) {
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
            });

            var comptIdsToDelete = [];
            angular.forEach(packetsToSave, function (unused, pktId) {
                if (!isDataEmpty(comptIdsTaggedToDelete[pktId])) {
                    comptIdsToDelete = comptIdsToDelete.concat(comptIdsTaggedToDelete[pktId]);
                }
            });

            if (packetsToUpdateParamsList.length == 0 && packetsToAddParamsList.length == 0
                && comptIdsToDelete.length == 0 && comptsToUpdateParamsList.length == 0
                && (savedPacketId || packetIdsToDelete.length == 0)) {
                return;
            }

            var params = {
                packetsToAddParamsList: packetsToAddParamsList,
                packetsToUpdateParamsList: packetsToUpdateParamsList,
                comptsToUpdateParamsList: comptsToUpdateParamsList,
                comptIdsToDelete: comptIdsToDelete
            };
            if (!savedPacketId) {
                params['packetIdsToDelete'] = packetIdsToDelete;
            } else {
                params['packetId'] = savedPacketId;
            }
            var saveAllChangesConfig = {
                params: params
            };
            var errorMap = {};

            errorMap[updateCompts] = comptIdsToUpdate;
            errorMap[deleteCompts] = comptIdsTaggedToDelete;
            errorMap[updatePackets] = newComptLabels[false];
            errorMap[addPackets] = newPackets;
            errorMap[deletePackets] = packetIdsToDelete;

            $http
                .post(contextPath + saveAllChangesToBasePath, saveAllChangesConfig)
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
                                        "1.The compts adding and/or " +
                                        "state change for the existing packets were not persisted; " +
                                        "2.Added new pakets together with the new compts inside were not persisted. " +
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

        $scope.selectPacket = function (packet) {
            $scope.data.selectedPacket = packet;
            $scope.selectPage(1);
        };

        $scope.selectPage = function (newPage) {
            $scope.selectedPage = newPage;
        };

        $scope.getPacketClass = function (packet) {
            var pktId = packet.id;
            var result = $scope.data.selectedPacketId == packet.id
                ? packetListActiveClass
                : packetListNonActiveClass;
            result += " ";

            if (pktId < 10) {
                result += swallowPacketCaption;
            } else {
                result += widePacketCaption;
            }
            return result;
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

        var prepareCompts = function (uploadedCompts, initialPacketInd) {
            loadedNoCompts = isDataEmpty(uploadedCompts);
            var packetInd = initialPacketInd;
            var visitedPacket = {};
            angular.forEach(uploadedCompts, function (el) {
                var id = el.id;
                var label = el.label.toUpperCase();
                var packetId = el.packetId;
                if (!visitedPacket[packetId]) {
                    visitedPacket[packetId] = true;
                    comptLabels[packetId] = {};
                    if (initialPacketIndex == initialPacketInd) {
                        compts.push([]);
                        packetIdToInd[packetId] = ++packetInd;
                    } else {
                        compts[packetInd] = [];
                    }
                }

                comptLabels[packetId][label] = true;
                comptIdToInd[id] = compts[packetInd].length;
                compts[packetInd].push(el);
                if (id > maximalComptId) {
                    maximalComptId = id;
                }
            });
            if (packetInd > maximalPacketIndex) {
                maximalPacketIndex = packetInd;
            }
        };

        var preparePackets = function (packets) {
            loadedNoPackets = isDataEmpty(packets);
            if (!loadedNoPackets) {
                $scope.selectPacket(packets[0]);
            } 
            angular.forEach(packets, function (pkt) {
                var pktId = pkt.id;
                $scope.data.allPackets[pktId] = pkt;
                if (pktId > maximalPacketId) {
                    maximalPacketId = pktId;
                }
                packetInitialStateIds[pkt.id] = pkt.stateId;
            });
        };

        var prepareStates = function (states) {
            loadedNoStates = isDataEmpty(states);
            $scope.data.allStates = states;
            $scope.data.allStateLabels = [labelLabel];
            angular.forEach($scope.data.allStates, function (state) {
                $scope.data.allStateLabels.push(state.label);
            });
        };

        var prepareComboData = function (comboData) {
            loadedNoComboData = isDataEmpty(comboData);
            angular.forEach(comboData, function (cd) {
                $scope.data.comboDataDefaultSet.push(cd.label);
            });
            angular.forEach($scope.data.allStates, function () {
                $scope.data.newComptCheckedVals.push($scope.data.comboDataDefaultSet[0]);
            });
        };

        var prepareComptsSupplInfo = function (comptSupplInfo) {
            loadedNoComptSupplInfo = isDataEmpty(comptSupplInfo);
            var comboDataVisitedCompts = {};
            var checkedComboDataVisitedCompts = {};

            angular.forEach(comptSupplInfo, function (el) {
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

        var isDataEmpty = function (data) {
            if (!data || !angular.isArray(data) || data.length == 0) {
                return true;
            }
            return false;
        };

        var findCheckedValsForCompt = function (comptId) {
            var checkedVals = [];
            angular.forEach($scope.data.allStates, function (state, ind) {
                checkedVals.push($scope.data.allCheckedComboData[comptId][ind + 1]);
            });
            return checkedVals;
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

        var generateComptParamsListForPackets = function (pktId, operation) {
            var result = [];
            angular.forEach(newComptLabels[operation == "add"][pktId], function (lbl, comptId) {
                result.push({label: lbl, vals: findCheckedValsForCompt(comptId)});
            });
            return result;
        };

        var generateToUpdateComptParamsListForPacketsToUpdate = function (pktId) {
            var result = [];
            angular.forEach(comptIdsToUpdate[pktId], function (unused, comptId) {
                result.push({id: comptId, vals: findCheckedValsForCompt(comptId)});
            });
            return result;
        };

        var init = function () {
            $scope.loadPacketById(null);
        };

        init();
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