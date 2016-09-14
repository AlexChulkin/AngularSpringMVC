/**
 * Created by alexc_000 on 2016-09-01.
 */
angular.module("packetAdminApp").service('exchangeService', function ($rootScope) {
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
    var maximalComptId;
    var packetIsAlreadySelectedAtLeastOnce;
    var newComptCheckedVals;
    var comptLabels;
    var comboDataDefaultSet;
    var selectedComptLabels;
    var isSelectedPacketNew;
    var comptsIsSelected;

    var init = function () {
        comptIdToInd = {};
        loadError = null;
        loadedNoStates = null;
        loadedNoComboData = null;
        maximalPacketId = null;
        packetIdToInd = {};
        maximalPacketIndex = null;
        allPackets = {};
        newPackets = {};
        loadEmpty = null;
        newComptLabels = {true: {}, false: {}};
        comptIdsToDelete = {};
        comptIdsToUpdate = {};
        allStates = [];
        allStateLabels = [];
        allComboData = {};
        allCheckedComboData = {};
        packetInitialStateIds = {};
        selectedCompts = [];
        compts = [];
        selectedPacketId = null;
        selectedPacket = null;
        selectedPage = null;
        loadedNoPackets = null;
        maximalComptId = null;
        packetIsAlreadySelectedAtLeastOnce = null;
        newComptCheckedVals = [];
        comptLabels = {};
        comboDataDefaultSet = [];
        selectedComptLabels = {};
        isSelectedPacketNew = null;
        comptsIsSelected = null;
    };

    var setSelectedPacket = function (newValue) {
        var oldValue = selectedPacket;
        selectedPacket = newValue;
        $rootScope.$broadcast('selectedPacket:update', selectedPacket);
        if (!angular.equals(newValue, oldValue)) {
            if (newValue) {
                if (oldValue) {
                    var oldSelectedPacketId = oldValue.id;
                    compts[packetIdToInd[oldSelectedPacketId]] = selectedCompts;
                    comptLabels[oldSelectedPacketId] = selectedComptLabels;
                }
                selectedPacketId = newValue.id;
                packetIsAlreadySelectedAtLeastOnce = true;
                isSelectedPacketNew = selectedPacketId in newPackets;
                setSelectedCompts(compts[packetIdToInd[selectedPacketId]] || []);
                setSelectedComptLabels(comptLabels[selectedPacketId] || {});
            } else {
                setSelectedCompts([]);
                setSelectedComptLabels({});
                isSelectedPacketNew = null;
                selectedPacketId = null;
            }
        } else {
            setSelectedCompts(compts[packetIdToInd[selectedPacketId]] || []);
            setSelectedComptLabels(comptLabels[selectedPacketId] || {});
        }
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
        if (label != undefined) {
            comptLabels[pktId][label] = value;
        } else {
            comptLabels[pktId] = value;
        }
    };

    var getComptLabels = function (pktId, label) {
        if (label != undefined) {
            return comptLabels[pktId][label];
        }
        return comptLabels[pktId];
    };

    var getPacketIdToInd = function (packetId) {
        return packetIdToInd[packetId];
    };

    var setPacketIdToInd = function (value, packetId) {
        packetIdToInd[packetId] = value;
    };

    var getComptIdToInd = function (id) {
        return comptIdToInd[id];
    };

    var setComptIdToInd = function (id, index) {
        comptIdToInd[id] = index;
    };

    var setCompts = function (value, packetInd) {
        compts[packetInd] = value;
    };

    var pushToCompts = function (value, packetInd) {
        if (packetInd != undefined) {
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
        allPackets[pktId] = value;
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
        $rootScope.$broadcast('allPackets:update', allPackets);
    };

    var setPacketInitialStateIds = function (pktId, value) {
        packetInitialStateIds[pktId] = value;
    };

    var getPacketInitialStateIds = function (pktId) {
        return packetInitialStateIds[pktId];
    };

    var setAllStates = function (states) {
        allStates = states;
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
        if (!isUndefinedOrNull(index)) {
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
        if (index != undefined) {
            return newComptCheckedVals[index];
        }
        return newComptCheckedVals;
    };

    var setNewComptCheckedVals = function () {
        var defaultCheckedVal = comboDataDefaultSet[0];
        for (i = 0; i < allStates.length; i++) {
            newComptCheckedVals.push(defaultCheckedVal);
        }
        $rootScope.$broadcast('newComptCheckedVals:update', newComptCheckedVals);
    };

    var setAllComboData = function (value, comptId, stateId) {
        if (stateId == undefined) {
            allComboData[comptId] = value;
        } else {
            allComboData[comptId][stateId] = value;
            if (stateId == allStates.length) {
                $rootScope.$broadcast('allComboData:update', allComboData);
            }
        }
    };

    var pushToAllComboData = function (value, comptId, stateId, comptSupplInfoIndex, comptSupplInfoLength) {
        allComboData[comptId][stateId].push(value);
        if (comptSupplInfoIndex === comptSupplInfoLength - 1) {
            $rootScope.$broadcast('allComboData:update', allComboData);
        }
    };

    var getAllComboData = function (comptId, stateId) {
        if (isUndefinedOrNull(stateId)) {
            if (!isUndefinedOrNull(comptId)) {
                return allComboData[comptId];
            } else {
                return allComboData;
            }   
        }
        return allComboData[comptId][stateId];
    };

    var setAllCheckedComboData = function (value, comptId, stateId) {
        if (stateId == undefined) {
            allCheckedComboData[comptId] = value;
        } else {
            allCheckedComboData[comptId][stateId] = value;
        }
        $rootScope.$broadcast('allCheckedComboData:update', allCheckedComboData);
    };

    var getAllCheckedComboData = function (comptId, stateId) {
        if (isUndefinedOrNull(stateId)) {
            if (!isUndefinedOrNull(comptId)) {
                return allCheckedComboData[comptId];
            } else {
                return allCheckedComboData;
            }
        }
        return allCheckedComboData[comptId][stateId];
    };

    var setNewPackets = function (value, pktId) {
        newPackets[pktId] = value;
    };

    var getNewPackets = function (pktId) {
        if (pktId != undefined) {
            return newPackets[pktId];
        }
        return newPackets;
    };

    var deleteNewComptLabels = function (isPktNew, pktId, comptId) {
        if (comptId == undefined) {
            delete newComptLabels[isPktNew][pktId];
        } else {
            delete newComptLabels[isPktNew][pktId][comptId];
        }
    };

    var getNewComptLabels = function (isPktNew, pktId, comptId) {
        if (pktId == undefined) {
            if (comptId == undefined) {
                return newComptLabels[isPktNew];
            }
        } else {
            if (comptId == undefined) {
                return newComptLabels[isPktNew][pktId];
            } else {
                return newComptLabels[isPktNew][pktId][comptId];
            }
        }
    };

    var setNewComptLabels = function (value, isPktNew, pktId, comptId) {
        if (pktId == undefined) {
            if (comptId == undefined) {
                newComptLabels[isPktNew] = value;
            }
        } else {
            if (comptId == undefined) {
                newComptLabels[isPktNew][pktId] = value;
            } else {
                newComptLabels[isPktNew][pktId][comptId] = value;
            }
        }
    };

    var deleteComptIdsToDelete = function (pktId) {
        delete comptIdsToDelete[pktId];
    };

    var getComptIdsToDelete = function (pktId) {
        return comptIdsToDelete[pktId];
    };

    var setComptIdsToDelete = function (value, pktId) {
        comptIdsToDelete[pktId] = value;
    };

    var pushToComptIdsToDelete = function (value, pktId) {
        comptIdsToDelete[pktId].push(value);
    };

    var setComptIdsToUpdate = function (value, pktId, comptId) {
        if (comptId != undefined) {
            comptIdsToUpdate[pktId][comptId] = value;
        } else {
            comptIdsToUpdate[pktId] = value;
        }
    };
    
    var getComptIdsToUpdate = function (pktId) {
        if (pktId != undefined) {
            return comptIdsToUpdate[pktId];
        }
        return comptIdsToUpdate;
    };

    var deleteSelectedComptLabels = function (label) {
        delete selectedComptLabels[label];
    };

    var setSelectedComptLabels = function (value, label) {
        if (label != undefined) {
            selectedComptLabels[label] = value;
        } else {
            selectedComptLabels = value;
        }
        comptsIsSelected = !angular.equals({}, selectedComptLabels);
    };

    var getSelectedComptLabels = function (label) {
        if (label != undefined) {
            return selectedComptLabels[label];
        }
        return selectedComptLabels;
    };

    var getComptsIsSelected = function () {
        return comptsIsSelected;
    };

    var setSelectedCompts = function (value, index) {
        if (index != undefined) {
            selectedCompts[index] = value;
        } else {
            selectedCompts = value;
        }
        $rootScope.$broadcast('selectedCompts:update', selectedCompts);
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

    var getisSelectedPacketNew = function () {
        return isSelectedPacketNew;
    };

    var deleteComptIdsToUpdate = function (pktId, comptId) {
        if (comptId != undefined) {
            delete comptIdsToUpdate[pktId][comptId];
        } else {
            delete comptIdsToUpdate[pktId];
        }
    };

    var isUndefinedOrNull = function (value) {
        return angular.isUndefined(value) || value === null
    };

    var isEmpty = function (value) {
        return (!(value && angular.isArray(value) && value.length !== 0));
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
        setNewComptCheckedVals: setNewComptCheckedVals,
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
        getNewComptCheckedVals: getNewComptCheckedVals,
        deleteSelectedComptLabels: deleteSelectedComptLabels,
        setSelectedCompts: setSelectedCompts,
        deleteComptIdsToUpdate: deleteComptIdsToUpdate,
        setComptIdsToDelete: setComptIdsToDelete,
        pushToComptIdsToDelete: pushToComptIdsToDelete,
        setComptIdsToUpdate: setComptIdsToUpdate,
        getSelectedComptsLength: getSelectedComptsLength,
        isUndefinedOrNull: isUndefinedOrNull,
        isEmpty: isEmpty,
        init: init
    };
});
