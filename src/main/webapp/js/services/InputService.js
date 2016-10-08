services.factory("InputPopupService", [function () {
    var popup = {};

    popup.show = false;

    popup.text = "";

    popup.success = function (data) {

    };

    popup.message = function (data) {
        popup.popupMessage = data;
    };

    popup.title = function (data) {
        popup.popupTitle = data;
    };

    popup.showPopup = function (id) {
        popup.show = true;
        $('.popup').removeClass("hidden");
        if (id) {
            popup.id = id;
        } else {
            popup.id = '#input-popup';
        }
        $(popup.id).removeClass("hidden");
        $(".popup-text").focus();
    };

    popup.closePopup = function (cancel) {
        popup.show = false;
        $('.popup').addClass("hidden");
        $(popup.id).addClass("hidden");
        if (!cancel) {
            popup.success(popup.text);
        }
    };
    return popup;
}]);