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

    $(href).load(url, function (result) {
        pane.tab('show');
    });
});

var tabs$ = $(".nav-tabs a");

$(window).on("hashchange", function () {
    var hash = window.location.hash, // get current hash
        menu_item$ = tabs$.filter('[href="' + hash + '"]'); // get the menu element

    var url = menu_item$.attr("data-url");
    $(hash).load(url, function (result) {
        menu_item$.tab("show"); // call bootstrap to show the tab
    });
}).trigger("hashchange");