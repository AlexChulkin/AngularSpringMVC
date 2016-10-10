/**
 * Created by alexc_000 on 2016-09-01.
 */
'use strict';

angular.module("packetAdminApp")
    .service('exchangeService', function ($rootScope, helperService) {
    var comptIdToInd;
    var loadError;
    var loadedNoStates;
    var loadedNoComboData;
    var maximalPacketId;
    var packetIdToInd;
    var maximalPacketIndex;
    var allPackets;
    var newPackets;
    var loadEmpty;
    var newComptLabels;
    var comptIdsToDelete;
    var comptIdsToUpdate;
    var allStates;
    var allStateLabels;
    var allComboData;
    var allCheckedComboData;
    var packetInitialStateIds;
    var selectedCompts;
    var compts;
    var selectedPacketId;
    var selectedPacket;
    var selectedPage;
        var loadedNoPackets;
    var loadedNoPacket;
    var maximalComptId;
    var packetIsAlreadySelectedAtLeastOnce;
    var newComptCheckedVals;
    var comptLabels;
    var comboDataDefaultSet;
    var selectedComptLabels;
        var selectedPacketIsEmpty;
        var initialAllCheckedComboData;

    var init = function () {
        setComptIdToInd({});
        setLoadError(null);
        setLoadedNoStates(null);
        setLoadedNoComboData(null);
        setMaximalPacketId(null);
        setPacketIdToInd({});
        setMaximalPacketIndex(null);
        setAllPackets({});
        setNewPackets({});
        setLoadEmpty(null);
        setNewComptLabels({true: {}, false: {}});
        setComptIdsToDelete({});
        setComptIdsToUpdate({});
        setAllStates([]);
        setAllStateLabels([]);
        setAllComboData(true, {});
        setAllCheckedComboData(true, true, {});
        setPacketInitialStateIds({});
        setCompts([]);
        setSelectedPacket(null);
        setSelectedPage(null);
        setLoadedNoPacket({});
        setLoadedNoPackets(null);
        setMaximalComptId(null);
        setPacketIsAlreadySelectedAtLeastOnce(null);
        setComboDataDefaultSet([]);
        setNewComptCheckedVals([]);
        setComptLabels({});
    };

    var setSelectedPacket = function (newPacket) {
        var oldPacket = selectedPacket;
        selectedPacket = newPacket;
        if (!checkIfTheSamePacketIsReselected(oldPacket, newPacket)) {
            if (newPacket) {
                if (oldPacket) {
                    var oldSelectedPacketId = oldPacket.id;
                    compts[packetIdToInd[oldSelectedPacketId]] = selectedCompts;
                    comptLabels[oldSelectedPacketId] = selectedComptLabels;
                }
                setSelectedPacketId(newPacket.id);
                packetIsAlreadySelectedAtLeastOnce = true;
                setSelectedCompts(compts[packetIdToInd[selectedPacketId]] || []);
                setSelectedComptLabels(comptLabels[selectedPacketId] || {});
            } else {
                setSelectedCompts([]);
                setSelectedComptLabels({});
                setSelectedPacketId(null);
            }
        } else {
            setSelectedCompts(compts[packetIdToInd[selectedPacketId]] || []);
            setSelectedComptLabels(comptLabels[selectedPacketId] || {});
        }
        $rootScope.$broadcast('selectedPacket:update', selectedPacket);
    };

    var checkIfTheSamePacketIsReselected = function (pkt1, pkt2) {
        return !helperService.isUndefinedOrNull(pkt1) && !helperService.isUndefinedOrNull(pkt2) && pkt1.id === pkt2.id;
    };

    var getSelectedPacket = function () {
        return selectedPacket;
    };

    var setLoadError = function (value) {
        loadError = value;
    };

    var getLoadError = function () {
        return loadError;
    };

    var setLoadedNoStates = function (value) {
        loadedNoStates = value;
    };

    var getLoadedNoStates = function () {
        return loadedNoStates;
    };

    var setLoadedNoComboData = function (value) {
        loadedNoComboData = value;
    };

    var getLoadedNoComboData = function () {
        return loadedNoComboData;
    };

    var setMaximalPacketId = function (value) {
        maximalPacketId = value;
    };

    var getMaximalPacketId = function () {
        return maximalPacketId;
    };

    var setMaximalPacketIndex = function (value) {
        maximalPacketIndex = value;
    };

    var getMaximalPacketIndex = function () {
        return maximalPacketIndex;
    };

    var setLoadEmpty = function (value) {
        loadEmpty = value;
    };

    var getLoadEmpty = function () {
        return loadEmpty;
    };

    var setSelectedPacketId = function (value) {
        selectedPacketId = value;
    };

    var getSelectedPacketId = function () {
        return selectedPacketId;
    };

    var setSelectedPage = function (value) {
        selectedPage = value;
        $rootScope.$broadcast('selectedPage:update', selectedPage);
    };

    var getSelectedPage = function () {
        return selectedPage;
    };

    var setLoadedNoPacket = function (value) {
        loadedNoPacket = value;
    };

    var setLoadedNoSelectedPacket = function (value) {
        if (!helperService.isUndefinedOrNull(selectedPacketId)) {
            loadedNoPacket[selectedPacketId] = value;
        }
    };

    var getLoadedNoSelectedPacket = function () {
        if (!helperService.isUndefinedOrNull(selectedPacketId)) {
            return loadedNoPacket[selectedPacketId];
        }
        return false;
    };

    var setLoadedNoUnSelectedPacket = function (value, pktId) {
        if (!helperService.isUndefinedOrNull(pktId)) {
            loadedNoPacket[pktId] = value;
        }
    };

        var setLoadedNoPackets = function (value) {
            loadedNoPackets = value;
    };

        var getLoadedNoPackets = function () {
            return loadedNoPackets;
    };

    var setMaximalComptId = function (value) {
        maximalComptId = value;
    };

    var getMaximalComptId = function () {
        return maximalComptId;
    };

    var setPacketIsAlreadySelectedAtLeastOnce = function (value) {
        packetIsAlreadySelectedAtLeastOnce = value;
    };

    var getPacketIsAlreadySelectedAtLeastOnce = function () {
        return packetIsAlreadySelectedAtLeastOnce;
    };

    var setComptLabels = function (value, pktId, label) {
        if (angular.isDefined(label)) {
            comptLabels[pktId][label] = value;
        } else {
            if (angular.isDefined(pktId)) {
                comptLabels[pktId] = value;
            } else {
                comptLabels = value;
            }    
        }
    };

    var getComptLabels = function (pktId, label) {
        if (angular.isDefined(label)) {
            return comptLabels[pktId][label];
        }
        return comptLabels[pktId];
    };

    var getPacketIdToInd = function (packetId) {
        return packetIdToInd[packetId];
    };

    var setPacketIdToInd = function (value, packetId) {
        if (angular.isDefined(packetId)) {
            packetIdToInd[packetId] = value;
        } else {
            packetIdToInd = value;
        }
    };

    var getComptIdToInd = function (id) {
        return comptIdToInd[id];
    };

    var setComptIdToInd = function (value, id) {
        if (angular.isDefined(id)) {
            comptIdToInd[id] = value;
        } else {
            comptIdToInd = value;
        }
    };

    var setCompts = function (value, packetInd) {
        if (angular.isDefined(packetInd)) {
            compts[packetInd] = value;
        } else {
            compts = value;
        }
    };

    var pushToCompts = function (value, packetInd) {
        if (angular.isDefined(packetInd)) {
            compts[packetInd].push(value);
        } else {
            compts.push(value);
        }
    };

    var getCompts = function (packetInd) {
        return compts[packetInd];
    };

    var getComptsLength = function (packetInd) {
        return compts[packetInd].length;
    };

    var setAllPackets = function (value, pktId) {
        if (angular.isDefined(pktId)) {
            allPackets[pktId] = value;
        } else {
            allPackets = value;
        }
        loadedNoPackets = angular.equals({}, allPackets);
        $rootScope.$broadcast('allPackets:update', allPackets);
    };

    var getAllPackets = function (pktId) {
        if (pktId) {
            return allPackets[pktId];
        }
        return allPackets;
    };

    var deleteAllPackets = function (pktId) {
        delete allPackets[pktId];
        loadedNoPackets = angular.equals({}, allPackets);
        $rootScope.$broadcast('allPackets:update', allPackets);
    };

    var setPacketInitialStateIds = function (value, pktId) {
        if (angular.isDefined(pktId)) {
            packetInitialStateIds[pktId] = value;
        } else {
            packetInitialStateIds = value;
        }
    };

    var getPacketInitialStateIds = function (pktId) {
        return packetInitialStateIds[pktId];
    };

    var setAllStates = function (value) {
        allStates = value;
        $rootScope.$broadcast('allStates:update', allStates);
    };

    var getAllStates = function () {
        return allStates;
    };

    var getAllStatesLength = function () {
        return allStates.length;
    };

    var getAllStateLabels = function () {
        return allStateLabels;
    };

    var setAllStateLabels = function (states, labelLabel) {
        allStateLabels = [labelLabel];
        angular.forEach(states, function (state) {
            allStateLabels.push(state.label);
        });
        $rootScope.$broadcast('allStateLabels:update', allStateLabels);
    };

    var getComboDataDefaultSet = function (index) {
        if (!angular.isUndefined(index)) {
            return comboDataDefaultSet[index];
        }
        return comboDataDefaultSet;

    };

    var setComboDataDefaultSet = function (comboData) {
        comboDataDefaultSet = [];
        angular.forEach(comboData, function (cd) {
            comboDataDefaultSet.push(cd.label);
        });
        $rootScope.$broadcast('comboDataDefaultSet:update', comboDataDefaultSet);
    };

    var getNewComptCheckedVals = function (index) {
        if (angular.isDefined(index)) {
            return newComptCheckedVals[index];
        }
        return newComptCheckedVals;
    };

    var setNewComptCheckedVals = function (value) {
        newComptCheckedVals = value;
    };

    var initializeNewComptCheckedVals = function () {
        var defaultCheckedVal = comboDataDefaultSet[0];
        for (var i = 0; i < allStates.length; i++) {
            newComptCheckedVals.push(defaultCheckedVal);
        }
        $rootScope.$broadcast('newComptCheckedVals:update', newComptCheckedVals);
    };

        var setAllComboData = function (broadcast, value, comptId, stateId) {
        if (angular.isUndefined(stateId)) {
            if (angular.isDefined(comptId)) {
                allComboData[comptId] = value;
            } else {
                allComboData = value;
            }
        } else {
            allComboData[comptId][stateId] = value;
        }
            if (broadcast) {
                $rootScope.$broadcast('allComboData:update', allComboData);
            }
    };

        var pushToAllComboData = function (broadcast, value, comptId, stateId) {
        allComboData[comptId][stateId].push(value);
            if (broadcast) {
            $rootScope.$broadcast('allComboData:update', allComboData);
        }
    };

    var getAllComboData = function (comptId, stateId) {
        if (angular.isUndefined(stateId)) {
            if (!angular.isUndefined(comptId)) {
                return allComboData[comptId];
            } else {
                return allComboData;
            }   
        }
        return allComboData[comptId][stateId];
    };

        var setAllCheckedComboData = function (broadcast, updInitialVals, value, comptId, stateId) {
        if (angular.isUndefined(stateId)) {
            if (angular.isDefined(comptId)) {
                allCheckedComboData[comptId] = value;
                if (updInitialVals) {
                    initialAllCheckedComboData[comptId] = value;
                }
            } else {
                allCheckedComboData = value;
                if (updInitialVals) {
                    initialAllCheckedComboData = value;
                }
            }
        } else {
            allCheckedComboData[comptId][stateId] = value;
            if (updInitialVals) {
                initialAllCheckedComboData[comptId][stateId] = value;
            }
        }
            if (broadcast) {
                $rootScope.$broadcast('allCheckedComboData:update', allCheckedComboData);
                if (updInitialVals) {
                    $rootScope.$broadcast('initialAllCheckedComboData:update', initialAllCheckedComboData);
                }
            }
    };

    var getAllCheckedComboData = function (comptId, stateId) {
        if (angular.isUndefined(stateId)) {
            if (!angular.isUndefined(comptId)) {
                return allCheckedComboData[comptId];
            } else {
                return allCheckedComboData;
            }
        }
        return allCheckedComboData[comptId][stateId];
    };

        var setInitialAllCheckedComboData = function () {
            initialAllCheckedComboData = allCheckedComboData;
            $rootScope.$broadcast('initialAllCheckedComboData:update', initialAllCheckedComboData);
        };

    var setNewPackets = function (value, pktId) {
        if (angular.isDefined(pktId)) {
            newPackets[pktId] = value;
        } else {
            newPackets = value;
        }
        $rootScope.$broadcast('newPackets:update', newPackets);
    };

    var getNewPackets = function (pktId) {
        if (angular.isDefined(pktId)) {
            return newPackets[pktId];
        }
        return newPackets;
    };

    var deleteNewPackets = function (pktId) {
        if (angular.isDefined(pktId)) {
            delete newPackets[pktId];
        } else {
            newPackets = {};
        }
    };

    var deleteNewComptLabels = function (pktId, comptId) {
        if (angular.isDefined(pktId)) {
            var isPktNew = pktId in newPackets;
            if (angular.isUndefined(comptId)) {
                delete newComptLabels[isPktNew][pktId];
            } else {
                delete newComptLabels[isPktNew][pktId][comptId];
            }
        } else {
            newComptLabels[true] = {};
            newComptLabels[false] = {};
        }
    };

    var getNewComptLabels = function (pktId, comptId) {
        if (angular.isUndefined(pktId)) {
            return newComptLabels;
        } else {
            var isPktNew = pktId in newPackets;
            if (angular.isUndefined(comptId)) {
                return newComptLabels[isPktNew][pktId];
            } else {
                return newComptLabels[isPktNew][pktId][comptId];
            }
        }
    };

    var setNewComptLabels = function (value, pktId, comptId) {
        if (angular.isUndefined(pktId)) {
            newComptLabels = value;
        } else {
            var isPktNew = pktId in newPackets;
            if (angular.isUndefined(comptId)) {
                newComptLabels[isPktNew][pktId] = value;
            } else {
                newComptLabels[isPktNew][pktId][comptId] = value;
            }
        }
    };

    var deleteComptIdsToDelete = function (pktId) {
        if (angular.isDefined(pktId)) {
            delete comptIdsToDelete[pktId];
        } else {
            comptIdsToDelete = {};
        }
    };

    var getComptIdsToDelete = function (pktId) {
        return comptIdsToDelete[pktId];
    };

    var setComptIdsToDelete = function (value, pktId) {
        if (angular.isDefined(pktId)) {
            comptIdsToDelete[pktId] = value;
        } else {
            comptIdsToDelete = value;
        }
    };

    var pushToComptIdsToDelete = function (value, pktId) {
        comptIdsToDelete[pktId].push(value);
    };

    var setComptIdsToUpdate = function (value, pktId, comptId) {
        if (angular.isDefined(pktId)) {
            if (angular.isDefined(comptId)) {
                comptIdsToUpdate[pktId][comptId] = value;
            } else {
                comptIdsToUpdate[pktId] = value;
            }
        } else {
            comptIdsToUpdate = value;
        }
    };
    
    var getComptIdsToUpdate = function (pktId) {
        if (angular.isDefined(pktId)) {
            return comptIdsToUpdate[pktId];
        }
        return comptIdsToUpdate;
    };

    var deleteSelectedComptLabels = function (label) {
        delete selectedComptLabels[label];
        selectedPacketIsEmpty = angular.equals({}, selectedComptLabels);
    };

    var setSelectedComptLabels = function (value, label) {
        if (angular.isDefined(label)) {
            selectedComptLabels[label] = value;
        } else {
            selectedComptLabels = value;
        }
        selectedPacketIsEmpty = angular.equals({}, selectedComptLabels);
    };

    var getSelectedComptLabels = function (label) {
        if (angular.isDefined(label)) {
            return selectedComptLabels[label];
        }
        return selectedComptLabels;
    };

        var getSelectedPacketIsEmpty = function () {
            return selectedPacketIsEmpty;
    };

        var setSelectedCompts = function (value) {
            selectedCompts = value;
        $rootScope.$broadcast('selectedCompts:update', selectedCompts);
        };

        var setSelectedCompt = function (index, value, pageSize) {
            selectedCompts[index] = value;

            if (value == null && getNumberOfNotNullSelectedCompts() % pageSize === 0) {
                if (selectedPage === 1) {
                    selectedPacketIsEmpty = true;
                } else {
                    setSelectedPage(selectedPage - 1);
                }
            }
            $rootScope.$broadcast('selectedCompts:update', selectedCompts);
        };


        var getNumberOfNotNullSelectedCompts = function () {
            var notNullCompts = 0;
            angular.forEach(selectedCompts, function (compt) {
                if (compt !== null) {
                    notNullCompts++;
                }
            });
            return notNullCompts;
    };

    var pushToSelectedCompts = function (value) {
        selectedCompts.push(value);
        $rootScope.$broadcast('selectedCompts:update', selectedCompts);
    };

    var getSelectedCompts = function () {
        return selectedCompts;
    };

    var getSelectedComptsLength = function () {
        return selectedCompts.length;
    };

    var deleteComptIdsToUpdate = function (pktId, comptId) {
        if (angular.isDefined(comptId)) {
            delete comptIdsToUpdate[pktId][comptId];
        } else if (angular.isDefined(pktId)) {
            delete comptIdsToUpdate[pktId];
        } else {
            comptIdsToUpdate = {};
        }
    };

    return {
        getSelectedPacket: getSelectedPacket,
        setSelectedPacket: setSelectedPacket,
        setLoadError: setLoadError,
        getLoadError: getLoadError,
        setLoadedNoStates: setLoadedNoStates,
        getLoadedNoStates: getLoadedNoStates,
        setLoadedNoComboData: setLoadedNoComboData,
        getLoadedNoComboData: getLoadedNoComboData,
        setMaximalPacketId: setMaximalPacketId,
        getMaximalPacketId: getMaximalPacketId,
        setMaximalPacketIndex: setMaximalPacketIndex,
        getMaximalPacketIndex: getMaximalPacketIndex,
        setLoadEmpty: setLoadEmpty,
        getLoadEmpty: getLoadEmpty,
        setSelectedPacketId: setSelectedPacketId,
        getSelectedPacketId: getSelectedPacketId,
        setSelectedPage: setSelectedPage,
        getSelectedPage: getSelectedPage,
        setLoadedNoSelectedPacket: setLoadedNoSelectedPacket,
        getLoadedNoSelectedPacket: getLoadedNoSelectedPacket,
        setLoadedNoUnSelectedPacket: setLoadedNoUnSelectedPacket,
        setLoadedNoPackets: setLoadedNoPackets,
        getLoadedNoPackets: getLoadedNoPackets,
        setMaximalComptId: setMaximalComptId,
        getMaximalComptId: getMaximalComptId,
        setPacketIdToInd: setPacketIdToInd,
        getPacketIdToInd: getPacketIdToInd,
        setPacketIsAlreadySelectedAtLeastOnce: setPacketIsAlreadySelectedAtLeastOnce,
        getPacketIsAlreadySelectedAtLeastOnce: getPacketIsAlreadySelectedAtLeastOnce,
        setComptLabels: setComptLabels,
        getComptLabels: getComptLabels,
        setComptIdToInd: setComptIdToInd,
        getComptIdToInd: getComptIdToInd,
        setCompts: setCompts,
        getCompts: getCompts,
        pushToCompts: pushToCompts,
        getComptsLength: getComptsLength,
        setAllPackets: setAllPackets,
        setPacketInitialStateIds: setPacketInitialStateIds,
        setAllStates: setAllStates,
        getAllStates: getAllStates,
        getAllStateLabels: getAllStateLabels,
        setAllStateLabels: setAllStateLabels,
        getComboDataDefaultSet: getComboDataDefaultSet,
        setComboDataDefaultSet: setComboDataDefaultSet,
        initializeNewComptCheckedVals: initializeNewComptCheckedVals,
        setAllComboData: setAllComboData,
        getAllComboData: getAllComboData,
        pushToAllComboData: pushToAllComboData,
        setAllCheckedComboData: setAllCheckedComboData,
        getAllCheckedComboData: getAllCheckedComboData,
        deleteNewPackets: deleteNewPackets,
        setNewPackets: setNewPackets,
        getNewPackets: getNewPackets,
        deleteNewComptLabels: deleteNewComptLabels,
        deleteAllPackets: deleteAllPackets,
        deleteComptIdsToDelete: deleteComptIdsToDelete,
        getNewComptLabels: getNewComptLabels,
        getComptIdsToUpdate: getComptIdsToUpdate,
        getAllPackets: getAllPackets,
        getPacketInitialStateIds: getPacketInitialStateIds,
        getComptIdsToDelete: getComptIdsToDelete,
        getSelectedComptLabels: getSelectedComptLabels,
        setSelectedComptLabels: setSelectedComptLabels,
        getSelectedPacketIsEmpty: getSelectedPacketIsEmpty,
        pushToSelectedCompts: pushToSelectedCompts,
        getSelectedCompts: getSelectedCompts,
        setNewComptLabels: setNewComptLabels,
        getAllStatesLength: getAllStatesLength,
        getNewComptCheckedVals: getNewComptCheckedVals,
        deleteSelectedComptLabels: deleteSelectedComptLabels,
        setSelectedCompts: setSelectedCompts,
        setSelectedCompt: setSelectedCompt,
        deleteComptIdsToUpdate: deleteComptIdsToUpdate,
        setComptIdsToDelete: setComptIdsToDelete,
        pushToComptIdsToDelete: pushToComptIdsToDelete,
        setComptIdsToUpdate: setComptIdsToUpdate,
        getSelectedComptsLength: getSelectedComptsLength,
        setInitialAllCheckedComboData: setInitialAllCheckedComboData,
        init: init
    };
});

angular.module("packetAdminApp")
    .service('helperService', function () {

        var isUndefinedOrNull = function (value) {
            return angular.isUndefined(value) || value === null
        };

        var isEmpty = function (value) {
            return (!(value && angular.isArray(value) && value.length !== 0));
        };

        return {
            isUndefinedOrNull: isUndefinedOrNull,
            isEmpty: isEmpty
        }
    });
