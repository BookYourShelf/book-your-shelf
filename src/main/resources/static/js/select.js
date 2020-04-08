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