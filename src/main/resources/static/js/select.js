$('select').each(
    function () {
        $(this).on('change', function () {
            var text = $(this).find("option:selected").text();
            $(this).removeClass('clblackimportant');
            $(this).removeClass('clgrayimportant');
            if (!text.toLowerCase().includes("select")) {
                $(this).addClass('clblackimportant');
            } else {
                $(this).addClass('clgrayimportant');
            }
        });
    }
);
$('#siteTab a').click(function (e) {
    e.preventDefault();

    var url = $(this).attr("data-url");
    var href = this.hash;
    var pane = $(this);

    console.log(url);

    switch (url.split("?")[0]) {

        case "/admin-panel/user":
            url = "/admin-panel/user?size=10&page=1&sort=ID-desc";
            break;

        case "/admin-panel/product":
            url = "/admin-panel/product?size=10&page=1&sort=date-desc&filter=all";
            break;

        case "/admin-panel/category":
            url = "/admin-panel/category?size=10&page=1&sort=ID-desc&filter=all";
            break;

        case "/admin-panel/hotList":
            url = "/admin-panel/hotList?size=10&page=1&sort=ID-desc&filter=all";
            break;

        case "/admin-panel/campaign":
            url = "/admin-panel/campaign?size=10&page=1&sort=ID-desc&filter=all";
            break;

        case "/admin-panel/pending-order":
            url = "/admin-panel/pending-order?size=10&page=1&sort=time-desc&optionFilter=all&statusFilter=all&couponFilter=all";
            break;

        case "/admin-panel/coupon":
            url = "/admin-panel/coupon?size=10&page=1&sort=ID-desc";
            break;

        case "/user-details/order":
            var url_ = new URL("https://localhost:8443" + url);
            var id = url_.searchParams.get("id");
            url = "/user-details/order?id=" + id + "&size=10&page=1&sort=time-desc&payOptFilter=all&payStatFilter=all&orderStatFilter=all&delStatFilter=all&couponFilter=all";
            break;

        case "/user-details/review":
            var url_ = new URL("https://localhost:8443" + url);
            var id = url_.searchParams.get("id");
            url = "/user-details/review?id=" + id + "&size=10&page=1&sort=date-desc&ratingFilter=all&titleFilter=all";
            break;

        case "/user-details/cart":
            var url_ = new URL("https://localhost:8443" + url);
            var id = url_.searchParams.get("id");
            url = "/user-details/cart?id=" + id + "&size=10&page=1";
            break;

        case "/user-details/wish-list":
            var url_ = new URL("https://localhost:8443" + url);
            var id = url_.searchParams.get("id");
            url = "/user-details/wish-list?id=" + id + "&size=10&page=1";
            break;

        case "/user-details/search":
            var url_ = new URL("https://localhost:8443" + url);
            var id = url_.searchParams.get("id");
            url = "/user-details/search?id=" + id + "&size=10&page=1&sort=Total-Search-desc";
            break;

        case "/profile/order":
            url = "/profile/order?size=10&page=1&sort=time-desc&payOptFilter=all&payStatFilter=all&orderStatFilter=all&delStatFilter=all&couponFilter=all";
            break;

        case "/profile/review":
            url = "/profile/review?size=10&page=1&sort=date-desc&ratingFilter=all&titleFilter=all";
            break;
    }

    $(href).load(url, function (result) {
        pane.tab('show');
    });
});

var tabs$ = $(".nav-tabs a");

$(window).on("hashchange", function () {
    var hash = window.location.hash,
        menu_item$ = tabs$.filter('[href="' + hash + '"]');

    var url = menu_item$.attr("data-url");
    console.log(url);
    if (hash) {
        $(hash).load(url, function (result) {
            menu_item$.tab("show");
        });
    }
}).trigger("hashchange");