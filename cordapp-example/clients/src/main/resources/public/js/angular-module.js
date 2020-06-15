"use strict";

const app = angular.module('demoAppModule', ['ui.bootstrap']);

// Fix for unhandled rejections bug.
app.config(['$qProvider', function ($qProvider) {
    $qProvider.errorOnUnhandledRejections(false);
}]);

app.controller('DemoAppController', function($http, $location, $uibModal, $scope) {
    const demoApp = this;

    const apiBaseURL = "/api/example/";
    let peers = [];
    var ious = [];
    var myious = [];
    var screenpeers = {"长庆钻井总公司":"O=ChangQing, L=XiAn, C=CN","陕西银邦":"O=CompanyA, L=ChengDu, C=CN","宁夏恒顺":"O=CompanyB, L=ShangHai, C=CN"};
    var screenpeers_reverse = {"O=ChangQing, L=XiAn, C=CN":"长庆钻井总公司","O=CompanyA, L=ChengDu, C=CN":"陕西银邦","O=CompanyB, L=ShangHai, C=CN":"宁夏恒顺"};

    $http.get(apiBaseURL + "me").then((response) => demoApp.thisNode = screenpeers_reverse[response.data.me]);

    $http.get(apiBaseURL + "peers").then((response) => peers = response.data.peers);

    $http.get(apiBaseURL + "peers").then((response) => demoApp.peers = response.data.peers);


    demoApp.getIOUs = () => $http.get(apiBaseURL + "ious")
        .then((response) => demoApp.ious = response.data);

    demoApp.getMyIOUs = () => $http.get(apiBaseURL + "my-ious")
        .then((response) => demoApp.myious = response.data);

    demoApp.getStates = () => $http.get(apiBaseURL + "states")
        .then((response) => demoApp.states = Object.keys(response.data)
            .map((key) => response.data[key])
            .reverse());

    demoApp.getStates();
    demoApp.getIOUs();
    demoApp.getMyIOUs();


    demoApp.openModal = () => {
        const modalInstance = $uibModal.open({
            templateUrl: 'demoAppModal.html',
            controller: 'ModalInstanceCtrl',
            controllerAs: 'modalInstance',
            resolve: {
                demoApp: () => demoApp,
                apiBaseURL: () => apiBaseURL,
                peers: () => peers,
                screenpeers: () => screenpeers,
                screenpeers_reverse: () => screenpeers_reverse
            }
        });

        modalInstance.result.then(() => {}, () => {});
    };

    $scope.openqrModal = function(itemID) {
        const modalInstanceQR = $uibModal.open({
            templateUrl: 'demoAppQR.html',
            controller: 'ModalInstanceCtrlQR',
            controllerAs: 'modalInstanceQR',
            resolve: {
                demoApp: () => demoApp,
                apiBaseURL: () => apiBaseURL,
                peers: () => peers,
                itemID: () => itemID
            }
        });
        modalInstanceQR.result.then(() => {}, () => {});
    };
});

app.controller('ModalInstanceCtrlQR', function ($http, $location, $uibModalInstance, $uibModal, demoApp, apiBaseURL, peers, itemID, $scope) {
    const modalInstanceQR = this;
    function toUtf8(str) {
        var out, i, len, c;
        out = "";
        len = str.length;
        for(i = 0; i < len; i++) {
            c = str.charCodeAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                out += str.charAt(i);
            } else if (c > 0x07FF) {
                out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
                out += String.fromCharCode(0x80 | ((c >>  6) & 0x3F));
                out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
            } else {
                out += String.fromCharCode(0xC0 | ((c >>  6) & 0x1F));
                out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
            }
        }
        return out;
    }
    var iou = demoApp.ious[itemID];
    var state = demoApp.states[itemID];
    var text = "ID：qc20200605676ads1" + "\n买家:" + iou.buyer + "\n卖家:" + iou.seller + "\n结算:" + String(iou.cash)  + "\n代扣:" + String(iou.oil) + "\nHASH value:\n" + String(state.ref.txhash);
    var qrcode = null;
    jQuery(document).ready(function(){
         qrcode = $('#QRcode').qrcode({
            render:"canvas",
            width: 200,
            height: 200,
            correctLevel: 0,
            text: toUtf8(String(text))
        });
//        var canvas = qrcode.find('canvas').get(0);
//        var a = document.createElement("a");
//        a.href=canvas.toDataURL("image/jpg");
//        a.download = "QRcode.jpg";
//        a.click();
    });

//    var qrcode = $("#QRcode").qrcode(toUtf8(String(text))).hide();
//    var canvas = qrcode.find('canvas').get(0);
//    //把canvas的二维码转换为图片
//    $("#imgOne").attr("src",canvas.toDataURL('image/jpg'));
//    function down(){
//        var canvas = qrcode.find('canvas').get(0);
//        var a = document.createElement("a");
//        a.href=canvas.toDataURL("image/jpg");
//        a.download = "QRcode.jpg";
//        a.click();
//    }
    function sleep(ms) {
      return new Promise(resolve =>
          setTimeout(resolve, ms)
      )
    }
    $scope.exportExcel = function() {
        var canvas = qrcode.find('canvas').get(0);
        var a = document.createElement("a");
        a.href=canvas.toDataURL("image/jpg");
        a.download = "QRcode.jpg";
        a.click();
        sleep(2000).then(()=>{
            var iou = demoApp.ious[itemID];
            var state = demoApp.states[itemID];
            let EXPORT_EXCEL_PATH = apiBaseURL + "export-excel"

            let exportExcelData = $.param({
                buyer : iou.buyer,
                seller : iou.seller,
                cash : iou.cash,
                oil : iou.oil,
                hash : state.ref.txhash
                });

            let exportExcelHeaders = {
                headers : {
                    "Content-Type": "application/x-www-form-urlencoded"
                }
            };

            // Create IOU  and handles success / fail responses.
            $http.post(EXPORT_EXCEL_PATH, exportExcelData, exportExcelHeaders).then(
                modalInstanceQR.displayMessage
            );
        })
    }
    modalInstanceQR.displayMessage = (message) => {
        const modalInstanceTwo = $uibModal.open({
            templateUrl: 'messageContent.html',
            controller: 'messageCtrl',
            controllerAs: 'modalInstanceTwo',
            resolve: { message: () => message }
        });
        // No behaviour on close / dismiss.
        modalInstanceTwo.result.then(() => {}, () => {});
    };
});
app.controller('ModalInstanceCtrl', function ($http, $location, $uibModalInstance, $uibModal, demoApp, apiBaseURL, peers, screenpeers, screenpeers_reverse, $scope) {
    const modalInstance = this;
    modalInstance.peers = peers
    modalInstance.form = {};
    modalInstance.formError = false;


    // Validates and sends IOU.
    modalInstance.create = function validateAndSendIOU() {
        if (modalInstance.form.value <= 0) {
            modalInstance.formError = true;
        } else {
            modalInstance.formError = false;
            $uibModalInstance.close();

            let CREATE_IOUS_PATH = apiBaseURL + "create-iou"

            let createIOUData = $.param({
                partyName: screenpeers[modalInstance.form.counterparty],
                cashValue : modalInstance.form.cash,
                oilValue : modalInstance.form.oil
            });

            let createIOUHeaders = {
                headers : {
                    "Content-Type": "application/x-www-form-urlencoded"
                }
            };

            // Create IOU  and handles success / fail responses.
            $http.post(CREATE_IOUS_PATH, createIOUData, createIOUHeaders).then(
                modalInstance.displayMessage,
                modalInstance.displayMessage
            );
        }
    };

    modalInstance.displayMessage = (message) => {
        const modalInstanceTwo = $uibModal.open({
            templateUrl: 'messageContent.html',
            controller: 'messageCtrl',
            controllerAs: 'modalInstanceTwo',
            resolve: { message: () => message }
        });

        // No behaviour on close / dismiss.
        modalInstanceTwo.result.then(() => {}, () => {});
    };

    // Close create IOU modal dialogue.
    modalInstance.cancel = () => $uibModalInstance.dismiss();

    // Validate the IOU.
    function invalidFormInput() {
        return isNaN(modalInstance.form.value) || (modalInstance.form.counterparty === undefined);
    }
});

// Controller for success/fail modal dialogue.
app.controller('messageCtrl', function ($uibModalInstance, message) {
    const modalInstanceTwo = this;
    modalInstanceTwo.message = message.data;
});