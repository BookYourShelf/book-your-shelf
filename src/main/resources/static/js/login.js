$(".reveal").on('click', function () {
    var $pwd = $(".pwd");
    var $reveal = $("#eye");
    if ($pwd.attr('type') === 'password') {
        $pwd.attr('type', 'text');
        $reveal.removeClass("fa-eye");
        $reveal.addClass("fa-eye-slash");
    } else {
        $pwd.attr('type', 'password');
        $reveal.removeClass("fa-eye-slash");
        $reveal.addClass("fa-eye");
    }
});

$(".reveal1").on('click', function () {
    var $pwd = $(".pwd1");
    var $reveal = $("#eye1");
    if ($pwd.attr('type') === 'password') {
        $pwd.attr('type', 'text');
        $reveal.removeClass("fa-eye");
        $reveal.addClass("fa-eye-slash");
    } else {
        $pwd.attr('type', 'password');
        $reveal.removeClass("fa-eye-slash");
        $reveal.addClass("fa-eye");
    }
});