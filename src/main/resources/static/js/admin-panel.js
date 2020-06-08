$('#product_type').change(function () {
    var index = $(this).prop('selectedIndex');

    if (index === 1) {
        clearOthers();
        $('#book').removeClass('d-none');
    } else if (index === 2) {
        clearOthers();
        $('#e-book').removeClass('d-none');
    } else if (index === 3) {
        clearOthers();
        $('#audiobook').removeClass('d-none');
    } else if (index === 4) {
        clearOthers();
        $('#e-book_reader').removeClass('d-none');
    } else if (index === 5) {
        clearOthers();
        $('#e-book_reader_case').removeClass('d-none');
    } else if (index === 6) {
        clearOthers();
        $('#book_case').removeClass('d-none');
    }
});

function updatePhoto(inputId) {
    var input = document.getElementById(inputId);
    var output = document.getElementById(inputId.concat("_list"));
    var children = "";
    for (var i = 0; i < input.files.length; ++i) {
        children += '<li style="padding-bottom: 6px">' + input.files.item(i).name + '</li>';
    }
    output.innerHTML = '<ul>' + children + '</ul>';
}

function FileSelected(e) {
    file = document.getElementById('fu').files[document.getElementById('fu').files.length - 1];
    document.getElementById('fileName').innerHtml = file.name;
}

function clearOthers() {
    $('#book').addClass('d-none');
    $('#e-book').addClass('d-none');
    $('#audiobook').addClass('d-none');
    $('#e-book_reader').addClass('d-none');
    $('#e-book_reader_case').addClass('d-none');
    $('#book_case').addClass('d-none');
}