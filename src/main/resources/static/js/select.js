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

$('#user').load($('.nav-link.active').attr("data-url"), function (result) {
});