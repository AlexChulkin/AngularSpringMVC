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
    .controller("packetCtrl", function ($scope, $http, $window, packetListActiveClass,
                                        packetListNonActiveClass, packetListPageCount, labelLabel,
                                        updateCompts, deleteCompts, updatePackets, deletePackets,
                                        addPackets, loadDataPath, saveAllChangesToBasePath,
                                        loadPacketByIdPath, initialPacketIndex) {

        var comptIdToInd = {};
        var packetIdToInd = {};
        var comptLabels = {};
        var compts = [];
        var maximalComptIndex = 0;
        var maximalPacketIndex = 0;
        var comptIdsTaggedToDelete = {};
        var comptIdsToUpdate = {};
        var newComptLabels = {true: {}, false: {}};
        var newComptLabelsForSelectedPacketType = {};

        var newPackets = {};
        var packetIdsToDelete = [];

        var packetInitialStateIds = {};

        var loadError = false;
        var loadedNoCompts = false;
        var loadedNoStates = false;
        var loadedNoComboData = false;
        var loadedNoComptSupplInfo = false;
        var ifComptsIsSelected = true;
        var ifPacketsIsNotLoaded = false;

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

        $scope.data.stateIdOfSelectedPacket = null;

        $scope.data.selectedPacket = null;
        $scope.data.selectedPacketId = null;

        $scope.$watch('data.selectedPacket', function (newValue, oldValue) {
            if (newValue) {
                if (oldValue) {
                    var oldSelectedPacketId = oldValue.id;
                    compts[packetIdToInd[oldSelectedPacketId]] = $scope.data.selectedCompts;
                    comptLabels[oldSelectedPacketId] = $scope.data.selectedComptLabels;
                }
                $scope.data.selectedPacketId = newValue.id;
                $scope.data.selectedCompts = compts[packetIdToInd[$scope.data.selectedPacketId]] || [];
                $scope.data.selectedComptLabels = comptLabels[$scope.data.selectedPacketId] || {};
                newComptLabelsForSelectedPacketType = newComptLabels[$scope.data.selectedPacketId in newPackets];
            } else {
                $scope.data.selectedPacketId = null;
                $scope.data.selectedCompts = [];
                $scope.data.selectedComptLabels = {};
                ifComptsIsSelected = false;
                newComptLabelsForSelectedPacketType = {};
            }
        });

        $scope.$watchCollection('data.selectedComptLabels', function (newValue, oldValue) {
            ifComptsIsSelected = !angular.equals({}, $scope.data.selectedComptLabels);
        });

        $scope.isComptsSelected = function () {
            return ifComptsIsSelected;
        };

        $scope.isPacketSelected = function () {
            return $scope.data.selectedPacket;
        };

        $scope.isDataLoadedProperly = function () {
            return !($scope.isDataLoadError() || $scope.isComboDataNotLoaded() || $scope.isStatesNotLoaded());
        };

        $scope.isDataLoadError = function () {
            return loadError && loadError == true;
        };

        $scope.isComboDataNotLoaded = function () {
            return loadedNoComboData && loadedNoComboData == true;
        };

        $scope.isStatesNotLoaded = function () {
            return loadedNoStates && loadedNoStates == true;
        };

        $scope.isPacketsNotLoaded = function () {
            return ifPacketsIsNotLoaded;
        };

        prepareCompts = function (uploadedCompts, initialPacketInd) {
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

        preparePackets = function (packets) {
            if (!isDataEmpty(packets)) {
                $scope.selectPacket(packets[0]);
            } else {
                ifPacketsIsNotLoaded = true;
            }
            angular.forEach(packets, function (pkt) {
                var pktId = pkt.id;
                $scope.data.allPackets[pktId] = pkt;
                if (pktId > maximalPacketIndex) {
                    maximalPacketIndex = pktId;
                }
                packetInitialStateIds[pkt.id] = pkt.stateId;
            });
        };

        prepareStates = function (states) {
            $scope.data.loadedNoStates = isDataEmpty(states);
            $scope.data.allStates = states;
            $scope.data.allStateLabels = [labelLabel];
            angular.forEach($scope.data.allStates, function (state) {
                $scope.data.allStateLabels.push(state.label);
            });
        };

        prepareComboData = function (comboData) {
            $scope.data.loadedNoComboData = isDataEmpty(comboData);
            angular.forEach(comboData, function (cd) {
                $scope.data.comboDataDefaultSet.push(cd.label);
            });
            angular.forEach($scope.data.allStates, function () {
                $scope.data.newComptCheckedVals.push($scope.data.comboDataDefaultSet[0]);
            });
        };

        prepareComptsSupplInfo = function (comptSupplInfo) {
            $scope.data.loadedNoComptSupplInfo = isDataEmpty(comptSupplInfo);
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

        $scope.addComptLocally = function () {
            var comptId = ++maximalComptIndex;
            var usualLabel = $scope.data.newLabel;
            var upperCaseLabel = usualLabel.toUpperCase();
            var newCompt = {id: comptId, label: usualLabel};
            $scope.data.selectedComptLabels[upperCaseLabel] = true;
            comptIdToInd[comptId] = $scope.data.selectedCompts.length;
            $scope.data.selectedCompts.push(newCompt);
            var pktId = $scope.data.selectedPacketId;
            newComptLabelsForSelectedPacketType[pktId]
                = newComptLabelsForSelectedPacketType[pktId] || {};
            newComptLabelsForSelectedPacketType[pktId][comptId] = usualLabel;
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
            var isNewPacket = newPackets[$scope.data.selectedPacketId];

            delete $scope.data.selectedComptLabels[comptLabel.toUpperCase()];
            $scope.data.selectedCompts[comptIdToInd[comptId]] = null;
            delete $scope.data.allCheckedComboData[comptId];
            delete $scope.data.allComboData[comptId];
            if (comptIdsToUpdate[pktId]) {
                delete comptIdsToUpdate[pktId][comptId];
            }
            if (newComptLabelsForSelectedPacketType[pktId]) {
                delete newComptLabelsForSelectedPacketType[pktId][comptId];
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
            delete newComptLabelsForSelectedPacketType[packetId];
            delete $scope.data.allPackets[packetId];
            delete comptIdsTaggedToDelete[packetId];
            $scope.data.selectedPacket = null;
        };

        $scope.updateComptLocally = function (compt) {
            var pktId = $scope.data.selectedPacketId;
            var comptId = compt.id;
            if (newComptLabelsForSelectedPacketType[pktId] &&
                newComptLabelsForSelectedPacketType[pktId][comptId]) {
                return;
            }
            comptIdsToUpdate[pktId] = comptIdsToUpdate[pktId] || {};
            comptIdsToUpdate[pktId][comptId] = true;
        };

        isDataEmpty = function (data) {
            if (!data || !angular.isArray(data) || data.length == 0) {
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

        setDefaultLoadDataErrors = function () {
            loadError = false;
            loadedNoCompts = false;
            loadedNoStates = false;
            loadedNoComboData = false;
            loadedNoComptSupplInfo = false;
            ifComptsIsSelected = true;
            ifPacketsIsNotLoaded = false;
        };

        $scope.loadPacketById = function (packetId) {
            setDefaultLoadDataErrors();
            var packetIndex = packetId == null ? initialPacketIndex : packetIdToInd[packetId] - 1;
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
                        if (!$scope.data.loadedNoCompts
                            && !$scope.data.loadedNoComboData
                            && !$scope.data.loadedNoStates) {

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
                    packetConfig.addedComptParamsList = generateNewComptParamsListForPacketsToUpdate(pktId);
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
            errorMap[addPackets] = newComptLabels[true];
            errorMap[deletePackets] = packetIdsToDelete;

            $http
                .post(contextPath + saveAllChangesToBasePath, {params: params})
                .then(
                    function success(data) {
                        angular.forEach(data, function (el) {
                            var key = String(el);
                            if (errorMap[key]) {
                                delete errorMap[key];
                                if (key == addPackets || key == updatePackets) {
                                    alert("Error occurred while trying to save data in DB. The STATES table is empty." +
                                        "The updated compts were not persisted to the DB. Try to solve the problem" +
                                        " and then re-push the saving button. DON'T RESTORE THE DATA FROM THE BASE! " +
                                        "Otherwise you may loose your changes.");
                                }
                                else if (key == updateCompts) {
                                    alert("Error occurred while trying to save data in DB. The COMBO_DATA table" +
                                        " is empty. At least one of the following: 1.The compts adding and/or " +
                                        "state change for the existing packets; 2.Added new pakets together with" +
                                        " new compts inside; were not persisted. Try to solve the problem and then " +
                                        "re-push the saving button. DON'T RESTORE THE DATA FROM THE BASE! Otherwise" +
                                        " you may loose your changes.");
                                }
                            }
                        });
                        angular.forEach(errorMap, function (v, k) {
                            if (k != deletePackets) {
                                v = {};
                            } else {
                                v = [];
                            }
                        });
                    },
                    function error(error) {
                    }
                );
        };

        generateComptParamsListForPacketsToAdd = function (pktId) {
            var result = [];
            angular.forEach(newComptLabels[true][pktId], function (lbl, comptId) {
                result.push({label: lbl, vals: findCheckedValsForCompt(comptId)});
            });
            return result;
        };

        generateNewComptParamsListForPacketsToUpdate = function (pktId) {
            var result = [];
            angular.forEach(newComptLabels[false][pktId], function (lbl, comptId) {
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
            $scope.selectPage(1);
        };

        $scope.selectPage = function (newPage) {
            $scope.selectedPage = newPage;
        };

        $scope.getPacketClass = function (packet) {
            return $scope.data.selectedPacketId == packet.id ? packetListActiveClass : packetListNonActiveClass; 
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

        init = function () {
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