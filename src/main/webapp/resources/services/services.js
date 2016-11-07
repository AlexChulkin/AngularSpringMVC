/*
 * Copyright (c) 2016.  Alex Chulkin
 */

'use strict';

/**
 * The main service allowing to exchange data among the controllers.
 *
 * One remark: the newPackets, the allPackets, comptIdsToDelete, comptIdsToUpdate  and many other collections
 * were set as objects(maps) - not as arrays. This could like kinda premature optimization from one side and some kind
 * of experiment given this project was not commercial one from another side. The idea was just to use the map's
 * search O(1) friendliness instead of the array's unpleasant O(n/2).
 */

angular.module("packetAdminApp")
    .service('exchangeService', function ($rootScope, utilsService) {
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
        initializeCompts();
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
        if (!checkIfTheSameExistingPacketIsReselected(oldPacket, newPacket)) {
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

        var checkIfTheSameExistingPacketIsReselected = function (pkt1, pkt2) {
            return !utilsService.isUndefinedOrNull(pkt1) && !utilsService.isUndefinedOrNull(pkt2) && pkt1.id === pkt2.id;
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

        var getLoadedNoPacket = function (pktId) {
            if (angular.isUndefined(pktId)) {
                return loadedNoPacket;
            }
            return loadedNoPacket[pktId];
        };

    var setLoadedNoSelectedPacket = function (value) {
        if (!utilsService.isUndefinedOrNull(selectedPacketId)) {
            loadedNoPacket[selectedPacketId] = value;
        }
    };

    var getLoadedNoSelectedPacket = function () {
        if (!utilsService.isUndefinedOrNull(selectedPacketId)) {
            return loadedNoPacket[selectedPacketId];
        }
        return false;
    };

    var setLoadedNoUnSelectedPacket = function (value, pktId) {
        if (!utilsService.isUndefinedOrNull(pktId)) {
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
            if (angular.isUndefined(comptLabels[pktId])) {
                comptLabels[pktId] = {};
            }
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
        if (angular.isDefined(pktId)) {
            return comptLabels[pktId];
        }
        return comptLabels;
    };

    var getPacketIdToInd = function (packetId) {
        if (angular.isDefined(packetId)) {
            return packetIdToInd[packetId];
        }
        return packetIdToInd;
    };

    var setPacketIdToInd = function (value, packetId) {
        if (angular.isDefined(packetId)) {
            packetIdToInd[packetId] = value;
        } else {
            packetIdToInd = value;
        }
    };

    var getComptIdToInd = function (id) {
        if (angular.isDefined(id)) {
            return comptIdToInd[id];
        }
        return comptIdToInd;
    };

    var setComptIdToInd = function (value, id) {
        if (angular.isDefined(id)) {
            comptIdToInd[id] = value;
        } else {
            comptIdToInd = value;
        }
    };

        var initializeCompts = function (packetInd) {
        if (angular.isDefined(packetInd)) {
            compts[packetInd] = [];
        } else {
            compts = [];
        }
    };

    var pushToCompts = function (value, packetInd) {
        if (angular.isDefined(packetInd)) {
            if (angular.isUndefined(compts[packetInd])) {
                compts[packetInd] = [];
            }
            compts[packetInd].push(value);
        } else {
            compts.push(value);
        }
    };

    var getCompts = function (packetInd) {
        if (angular.isDefined(packetInd)) {
            return compts[packetInd];
        }
        return compts;
    };

    var getComptsLength = function (packetInd) {
        if (angular.isDefined(packetInd) && angular.isArray(compts[packetInd])) {
            return compts[packetInd].length;
        } 
        return 0;
    };

    var setAllPackets = function (value, pktId) {
        if (angular.isDefined(pktId)) {
            allPackets[pktId] = value;
        } else {
            allPackets = value;
        }
        setLoadedNoPackets(angular.equals({}, allPackets));
        $rootScope.$broadcast('allPackets:update', allPackets);
    };

    var getAllPackets = function (pktId) {
        if (pktId) {
            return allPackets[pktId];
        }
        return allPackets;
    };

    var deleteAllPackets = function (pktId) {
        if (pktId in allPackets) {
            delete allPackets[pktId];
            setLoadedNoPackets(angular.equals({}, allPackets));
            $rootScope.$broadcast('allPackets:update', allPackets);
        }
    };

    var setPacketInitialStateIds = function (value, pktId) {
        if (angular.isDefined(pktId)) {
            packetInitialStateIds[pktId] = value;
        } else {
            packetInitialStateIds = value;
        }
    };

    var getPacketInitialStateIds = function (pktId) {
        if (angular.isDefined(pktId)) {
            return packetInitialStateIds[pktId];
        }
        return packetInitialStateIds;
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
        allStateLabels = [];
        if (angular.isDefined(labelLabel)) {
            allStateLabels.push(labelLabel);
        }
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
        if (angular.isArray(comboData)) {
            comboDataDefaultSet = [];
            angular.forEach(comboData, function (cd) {
                comboDataDefaultSet.push(cd.label);
            });
            $rootScope.$broadcast('comboDataDefaultSet:update', comboDataDefaultSet);
        }
    };

    var getNewComptCheckedVals = function (index) {
        if (angular.isDefined(index)) {
            return newComptCheckedVals[index];
        }
        return newComptCheckedVals;
    };

        var setNewComptCheckedVals = function (value, index) {
            if (angular.isUndefined(index)) {
                newComptCheckedVals = value;
            } else {
                newComptCheckedVals[index] = value;
            }
    };

    var initializeNewComptCheckedVals = function () {
        if (comboDataDefaultSet.length < 1 || allStates.length < 1) {
            return;
        }
        var defaultCheckedVal = comboDataDefaultSet[0];
        for (var i = 0; i < allStates.length; i++) {
            newComptCheckedVals.push(defaultCheckedVal);
        }
        if (allStates.length > 0) {
            $rootScope.$broadcast('newComptCheckedVals:update', newComptCheckedVals);
        }
    };

        var setAllComboData = function (broadcast, value, comptId, stateId) {
        if (angular.isUndefined(stateId)) {
            if (angular.isDefined(comptId)) {
                allComboData[comptId] = value;
            } else {
                allComboData = value;
            }
        } else {
            if (angular.isUndefined(allComboData[comptId])) {
                allComboData[comptId] = {};
            }
            allComboData[comptId][stateId] = value;
        }
            if (broadcast) {
                $rootScope.$broadcast('allComboData:update', allComboData);
            }
    };

        var pushToAllComboData = function (broadcast, value, comptId, stateId) {
            if (angular.isUndefined(allComboData[comptId])) {
                allComboData[comptId] = {};
            }
            if (angular.isUndefined(allComboData[comptId][stateId])) {
                allComboData[comptId][stateId] = [];
            }
        allComboData[comptId][stateId].push(value);
            if (broadcast) {
            $rootScope.$broadcast('allComboData:update', allComboData);
        }
    };

    var getAllComboData = function (comptId, stateId) {
        if (angular.isUndefined(stateId)) {
            if (angular.isDefined(comptId)) {
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
                    initialAllCheckedComboData[comptId] = angular.copy(value);
                }
            } else {
                allCheckedComboData = value;
                if (updInitialVals) {
                    initialAllCheckedComboData = angular.copy(value);
                }
            }
        } else {
            if (angular.isUndefined(allCheckedComboData[comptId])) {
                allCheckedComboData[comptId] = {};
            }
            allCheckedComboData[comptId][stateId] = value;
            if (updInitialVals) {
                if (angular.isUndefined(initialAllCheckedComboData[comptId])) {
                    initialAllCheckedComboData[comptId] = {};
                }
                initialAllCheckedComboData[comptId][stateId] = angular.copy(value);
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
            if (angular.isDefined(comptId)) {
                return allCheckedComboData[comptId];
            } else {
                return allCheckedComboData;
            }
        }
        return allCheckedComboData[comptId][stateId];
    };

        var setInitialAllCheckedComboData = function () {
            initialAllCheckedComboData = angular.copy(allCheckedComboData);
            $rootScope.$broadcast('initialAllCheckedComboData:update', initialAllCheckedComboData);
        };

        var getInitialAllCheckedComboData = function () {
            return initialAllCheckedComboData;
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
                if (angular.isUndefined(newComptLabels[isPktNew][pktId])) {
                    newComptLabels[isPktNew][pktId] = {};
                }
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
        if (angular.isDefined(pktId)) {
            if (pktId in comptIdsToDelete) {
                return comptIdsToDelete[pktId];
            }
            return [];
        }
        return comptIdsToDelete;
    };

    var setComptIdsToDelete = function (value, pktId) {
        if (angular.isDefined(pktId)) {
            comptIdsToDelete[pktId] = value;
        } else {
            comptIdsToDelete = value;
        }
    };

    var pushToComptIdsToDelete = function (value, pktId) {
        if (angular.isUndefined(comptIdsToDelete[pktId])) {
            comptIdsToDelete[pktId] = [];
        }
        comptIdsToDelete[pktId].push(value);
    };

    var setComptIdsToUpdate = function (value, pktId, comptId) {
        if (angular.isDefined(pktId)) {
            if (angular.isDefined(comptId)) {
                if (angular.isUndefined(comptIdsToUpdate[pktId])) {
                    comptIdsToUpdate[pktId] = {};
                }
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

        var deleteComptIdsToUpdate = function (pktId, comptId) {
            if (angular.isDefined(comptId)) {
                delete comptIdsToUpdate[pktId][comptId];
            } else if (angular.isDefined(pktId)) {
                delete comptIdsToUpdate[pktId];
            } else {
                comptIdsToUpdate = {};
            }
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

        var setSelectedPacketIsEmpty = function (value) {
            selectedPacketIsEmpty = value;
        };

        var setSelectedCompts = function (value) {
            selectedCompts = value;
            $rootScope.$broadcast('selectedCompts:update', selectedCompts);
        };

        var getSelectedCompts = function () {
            return selectedCompts;
        };

        var getSelectedComptsLength = function () {
            return selectedCompts.length;
        };

        var pushToSelectedCompts = function (value) {
            selectedCompts.push(value);
            $rootScope.$broadcast('selectedCompts:update', selectedCompts);
        };

        var setSelectedCompt = function (index, pageSize) {
            selectedCompts[index] = null;
            if (getNumberOfNotNullSelectedCompts() % pageSize === 0) {
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
        setLoadedNoPacket: setLoadedNoPacket,
        getLoadedNoPacket: getLoadedNoPacket,
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
        initializeCompts: initializeCompts,
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
        setSelectedPacketIsEmpty: setSelectedPacketIsEmpty,
        getSelectedPacketIsEmpty: getSelectedPacketIsEmpty,
        pushToSelectedCompts: pushToSelectedCompts,
        getSelectedCompts: getSelectedCompts,
        setNewComptLabels: setNewComptLabels,
        getAllStatesLength: getAllStatesLength,
        getNewComptCheckedVals: getNewComptCheckedVals,
        setNewComptCheckedVals: setNewComptCheckedVals,
        deleteSelectedComptLabels: deleteSelectedComptLabels,
        setSelectedCompts: setSelectedCompts,
        setSelectedCompt: setSelectedCompt,
        deleteComptIdsToUpdate: deleteComptIdsToUpdate,
        setComptIdsToDelete: setComptIdsToDelete,
        pushToComptIdsToDelete: pushToComptIdsToDelete,
        setComptIdsToUpdate: setComptIdsToUpdate,
        getSelectedComptsLength: getSelectedComptsLength,
        setInitialAllCheckedComboData: setInitialAllCheckedComboData,
        getInitialAllCheckedComboData: getInitialAllCheckedComboData,
        init: init
    };
});

/**
 * The small utils service.
 */

angular.module("packetAdminApp")
    .service('utilsService', function () {

        var isUndefinedOrNull = function (value) {
            return angular.isUndefined(value) || value === null
        };

        var isEmpty = function (value) {
            return (value.length === 0);
        };

        return {
            isUndefinedOrNull: isUndefinedOrNull,
            isEmpty: isEmpty
        }
    });
