/**
 * Created by alexc_000 on 2016-09-01.
 */
var app = angular.module("packetApp");

app.service('exchangeService', function ($rootScope) {
    var comptIdToInd = {};
    var loadError = null;
    var loadedNoStates = null;
    var loadedNoComboData = null;
    var maximalPacketId = null;
    var packetIdToInd = {};
    var maximalPacketIndex = null;
    var allPackets = {};
    var newPackets = {};
    var loadEmpty = null;
    var newComptLabels = {true: {}, false: {}};
    var comptIdsToDelete = {};
    var comptIdsToUpdate = {};
    var allStates = [];
    var allStateLabels = [];
    var allComboData = {};
    var allCheckedComboData = {};
    var packetInitialStateIds = {};
    var selectedCompts = [];
    var packetIsSelectedOrSelectedPacketIsReloaded = {oldVal: null, newVal: null};
    var compts = [];
    var selectedPacketId = null;
    var selectedPacket = null;
    var selectedPage = null;
    var loadedNoPackets = null;
    var maximalComptId = null;
    var packetIsAlreadySelectedAtLeastOnce = null;
    var newComptCheckedVals = [];
    var comptLabels = {};
    var comboDataDefaultSet = [];
    var selectedComptLabels = {};
    var isSelectedPacketNew = null;
    var comptsIsSelected = null;

    var activatePacketIsSelectedOrSelectedPacketIsReloadedListener = function (item) {
        var oldValue = item.oldVal;
        var newValue = item.newVal;
        if (newValue !== oldValue) {
            if (newValue) {
                if (oldValue) {
                    var oldSelectedPacketId = oldValue.id;
                    compts[packetIdToInd[oldSelectedPacketId]] = selectedCompts;
                    comptLabels[oldSelectedPacketId] = selectedComptLabels;
                }
                selectedPacketId = newValue.id;
                packetIsAlreadySelectedAtLeastOnce = true;
                isSelectedPacketNew = selectedPacketId in newPackets;
                selectedCompts = compts[packetIdToInd[selectedPacketId]] || [];
                setSelectedComptLabels(comptLabels[selectedPacketId] || {});
            } else {
                selectedCompts = [];
                setSelectedComptLabels({});
                isSelectedPacketNew = null;
                selectedPacketId = null;
            }
        } else {
            selectedCompts = compts[packetIdToInd[selectedPacketId]] || [];
            setSelectedComptLabels(comptLabels[selectedPacketId] || {});
        }
    };

    var activateSelectedComptLabelsListener = function (item) {
        comptsIsSelected = !angular.equals({}, item);
    };

    var setSelectedPacket = function (item) {
        selectedPacket = item;
    };

    var getSelectedPacket = function () {
        return selectedPacket;
    };

    var setLoadError = function (item) {
        loadError = item;
    };

    var getLoadError = function () {
        return loadError;
    };

    var setLoadedNoStates = function (item) {
        loadedNoStates = item;
    };

    var getLoadedNoStates = function () {
        return loadedNoStates;
    };

    var setLoadedNoComboData = function (item) {
        loadedNoComboData = item;
    };

    var getLoadedNoComboData = function () {
        return loadedNoComboData;
    };

    var setMaximalPacketId = function (item) {
        maximalPacketId = item;
    };

    var getMaximalPacketId = function () {
        return maximalPacketId;
    };

    var setMaximalPacketIndex = function (item) {
        maximalPacketIndex = item;
    };

    var getMaximalPacketIndex = function () {
        return maximalPacketIndex;
    };

    var setLoadEmpty = function (item) {
        loadEmpty = item;
    };

    var getLoadEmpty = function () {
        return loadEmpty;
    };

    var setSelectedPacketId = function (item) {
        selectedPacketId = item;
    };

    var getSelectedPacketId = function () {
        return selectedPacketId;
    };

    var setSelectedPage = function (item) {
        selectedPage = item;
    };

    var getSelectedPage = function () {
        return selectedPage;
    };

    var setLoadedNoPackets = function (item) {
        loadedNoPackets = item;
    };

    var getLoadedNoPackets = function () {
        return loadedNoPackets;
    };

    var setMaximalComptId = function (item) {
        maximalComptId = item;
    };

    var getMaximalComptId = function () {
        return maximalComptId;
    };

    var setPacketIsSelectedOrSelectedPacketIsReloaded = function (item) {
        packetIsSelectedOrSelectedPacketIsReloaded = item;
        activatePacketIsSelectedOrSelectedPacketIsReloadedListener(packetIsSelectedOrSelectedPacketIsReloaded);
    };

    var getPacketIsSelectedOrSelectedPacketIsReloaded = function () {
        return packetIsSelectedOrSelectedPacketIsReloaded;
    };

    var setPacketIsAlreadySelectedAtLeastOnce = function (item) {
        packetIsAlreadySelectedAtLeastOnce = item;
    };

    var getPacketIsAlreadySelectedAtLeastOnce = function () {
        return packetIsAlreadySelectedAtLeastOnce;
    };

    var setComptLabels = function (pktId, item, label) {
        if (label) {
            comptLabels[pktId][label] = item;
        } else {
            comptLabels[pktId] = item;
        }
    };

    var getComptLabels = function (pktId, label) {
        if (label) {
            return comptLabels[pktId][label];
        } else {
            return comptLabels[pktId];
        }
    };

    var getPacketIdToInd = function (packetId) {
        return packetIdToInd[packetId];
    };

    var setPacketIdToInd = function (packetId, item) {
        packetIdToInd[packetId] = item;
    };

    var getComptIdToInd = function (id) {
        return comptIdToInd[id];
    };

    var setComptIdToInd = function (id, index) {
        comptIdToInd[id] = index;
    };

    var setCompts = function (item, packetInd) {
        compts[packetInd] = item;
    };

    var pushToCompts = function (item, packetInd) {
        if (packetInd) {
            compts[packetInd].push(item);
        } else {
            compts.push(item);
        }
    };

    var getCompts = function (packetInd) {
        return compts[packetInd];
    };

    var getComptsLength = function (packetInd) {
        return compts[packetInd].length;
    };

    var setAllPackets = function (pktId, item) {
        allPackets[pktId] = item;
    };

    var getAllPackets = function (pktId) {
        if (pktId) {
            return allPackets[pktId];
        }
        return allPackets;

    };

    var deleteAllPackets = function (pktId) {
        delete allPackets[pktId];
    };

    var setPacketInitialStateIds = function (pktId, item) {
        packetInitialStateIds[pktId] = item;
    };

    var getPacketInitialStateIds = function (pktId) {
        return packetInitialStateIds[pktId];
    };

    var setAllStates = function (states) {
        allStates = states;
    };

    var getAllStates = function () {
        return allStates;
    };

    var getAllStatesLength = function () {
        return allStates.length;
    };


    var pushToAllStateLabels = function (item) {
        allStateLabels.push(item);
    };

    var pushToComboDataDefaultSet = function (item) {
        comboDataDefaultSet.push(item);
    };

    var getComboDataDefaultSet = function (index) {
        return comboDataDefaultSet[index];
    };

    var pushToNewComptCheckedVals = function (item) {
        newComptCheckedVals.push(item);
    };

    var getNewComptCheckedVals = function (index) {
        return newComptCheckedVals[index];
    };

    var setAllComboData = function (item, comptId, stateId) {
        if (!stateId) {
            allComboData[comptId] = item;
        } else {
            allComboData[comptId][stateId] = item;
        }
    };

    var pushToAllComboData = function (comptId, stateId, item) {
        allComboData[comptId][stateId].push(item);
    };

    var getAllComboData = function (comptId, stateId) {
        if (!stateId) {
            return allComboData[comptId];
        }

        return allComboData[comptId][stateId];
    };

    var setAllCheckedComboData = function (item, comptId, stateId) {
        if (!stateId) {
            allCheckedComboData[comptId] = item;
        }

        allCheckedComboData[comptId][stateId] = item;
    };

    var getAllCheckedComboData = function (comptId, stateId) {
        if (!stateId) {
            return allCheckedComboData[comptId];
        }

        return allCheckedComboData[comptId][stateId];
    };

    var setNewPackets = function (pktId, item) {
        newPackets[pktId] = item;
    };

    var getNewPackets = function (pktId) {
        if (pktId) {
            return newPackets[pktId];
        }
        return newPackets;
    };

    var deleteNewComptLabels = function (isPktNew, pktId) {
        delete newComptLabels[isPktNew][pktId];
    };

    var getNewComptLabels = function (isPktNew, pktId) {
        if (pktId) {
            return newComptLabels[isPktNew][pktId];
        }
        return newComptLabels[isPktNew];
    };

    var setNewComptLabels = function (item, isPktNew, pktId, comptId) {
        if (!pktId && !comptId) {
            newComptLabels[isPktNew] = item;
        } else if (pktId && !comptId) {
            newComptLabels[isPktNew][pktId] = item;
        } else if (pktId && comptId) {
            newComptLabels[isPktNew][pktId][comptId] = item;
        }
    };

    var deleteComptIdsToDelete = function (pktId) {
        delete comptIdsToDelete[pktId];
    };

    var getComptIdsToDelete = function (pktId) {
        return comptIdsToDelete[pktId];
    };

    var getComptIdsToUpdate = function (pktId) {
        if (pktId) {
            return comptIdsToUpdate[pktId];
        }
        return comptIdsToUpdate;
    };

    var setSelectedComptLabels = function (item, label) {
        if (label) {
            selectedComptLabels[label] = item;
        } else {
            selectedComptLabels = item;
        }
        activateSelectedComptLabelsListener(selectedComptLabels);
    };

    var getSelectedComptLabels = function () {
        return selectedComptLabels;
    };

    var getComptsIsSelected = function () {
        return comptsIsSelected;
    };

    var pushToSelectedCompts = function (item) {
        selectedCompts.push(item);
    };

    var getSelectedCompts = function () {
        return selectedCompts;
    };

    var getisSelectedPacketNew = function () {
        return isSelectedPacketNew;
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
        setLoadedNoPackets: setLoadedNoPackets,
        getLoadedNoPackets: getLoadedNoPackets,
        setMaximalComptId: setMaximalComptId,
        getMaximalComptId: getMaximalComptId,
        setPacketIsSelectedOrSelectedPacketIsReloaded: setPacketIsSelectedOrSelectedPacketIsReloaded,
        getPacketIsSelectedOrSelectedPacketIsReloaded: getPacketIsSelectedOrSelectedPacketIsReloaded,
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
        pushToAllStateLabels: pushToAllStateLabels,
        pushToComboDataDefaultSet: pushToComboDataDefaultSet,
        getComboDataDefaultSet: getComboDataDefaultSet,
        pushToNewComptCheckedVals: pushToNewComptCheckedVals,
        setAllComboData: setAllComboData,
        getAllComboData: getAllComboData,
        pushToAllComboData: pushToAllComboData,
        setAllCheckedComboData: setAllCheckedComboData,
        getAllCheckedComboData: getAllCheckedComboData,
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
        getComptsIsSelected: getComptsIsSelected,
        pushToSelectedCompts: pushToSelectedCompts,
        getSelectedCompts: getSelectedCompts,
        getIsSelectedPacketNew: getisSelectedPacketNew,
        setNewComptLabels: setNewComptLabels,
        getAllStatesLength: getAllStatesLength,
        getNewComptCheckedVals: getNewComptCheckedVals
    };
});
