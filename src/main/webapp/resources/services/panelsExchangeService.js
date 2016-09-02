/**
 * Created by alexc_000 on 2016-09-01.
 */
var app = angular.module("packetApp");

app.service('panelsExchangeService', function () {
    var selectedPacketId = null;
    var selectedPacket = null;
    var selectedCompts = [];

    var setSelectedPacket = function (item) {
        selectedPacket = item;
    };

    var getSelectedPacket = function () {
        return selectedPacket;
    };

    return {
        getSelectedPacket: getSelectedPacket,
        setSelectedPacket: setSelectedPacket
    };

});
