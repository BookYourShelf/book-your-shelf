/* JS Document */

/******************************

 [Table of Contents]

 1. Vars and Inits
 2. Set Header
 3. Init Menu
 4. Init SVG
 5. Init Isotope


 ******************************/

$(document).ready(function () {
    "use strict";

    /*

    1. Vars and Inits

    */

    var header = $('.header');

    initMenu();
    initSvg();
    initIsotope();

    setHeader();

    $(window).on('resize', function () {
        setHeader();
    });

    $(document).on('scroll', function () {
        setHeader();
    });

    /*

    2. Set Header

    */

    function setHeader() {
        if ($(window).scrollTop() > 91) {
            header.addClass('scrolled');
        } else {
            header.removeClass('scrolled');
        }
    }

    /*

    3. Init Menu

    */

    function initMenu() {
        if ($('.menu').length) {
            var hamburger = $('.hamburger');
            var header = $('.header');
            var superContainerInner = $('.super_container_inner');
            var superOverlay = $('.super_overlay');
            var headerOverlay = $('.header_overlay');
            var menu = $('.menu');
            var isActive = false;

            hamburger.on('click', function () {
                superContainerInner.toggleClass('active');
                menu.toggleClass('active');
                header.toggleClass('active');
                isActive = true;
            });

            superOverlay.on('click', function () {
                if (isActive) {
                    superContainerInner.toggleClass('active');
                    menu.toggleClass('active');
                    header.toggleClass('active');
                    isActive = false;
                }
            });

            headerOverlay.on('click', function () {
                if (isActive) {
                    superContainerInner.toggleClass('active');
                    menu.toggleClass('active');
                    header.toggleClass('active');
                    isActive = false;
                }
            });
        }
    }

    /*

    4. Init SVG

    */

    function initSvg() {
        if ($('img.svg').length) {
            jQuery('img.svg').each(function () {
                var $img = jQuery(this);
                var imgID = $img.attr('id');
                var imgClass = $img.attr('class');
                var imgURL = $img.attr('src');

                jQuery.get(imgURL, function (data) {
                    // Get the SVG tag, ignore the rest
                    var $svg = jQuery(data).find('svg');

                    // Add replaced image's ID to the new SVG
                    if (typeof imgID !== 'undefined') {
                        $svg = $svg.attr('id', imgID);
                    }
                    // Add replaced image's classes to the new SVG
                    if (typeof imgClass !== 'undefined') {
                        $svg = $svg.attr('class', imgClass + ' replaced-svg');
                    }

                    // Remove any invalid XML tags as per http://validator.w3.org
                    $svg = $svg.removeAttr('xmlns:a');

                    // Replace image with new SVG
                    $img.replaceWith($svg);
                }, 'xml');
            });
        }
    }

    /*

    5. Init Isotope

    */

    function initIsotope() {
    }

});

(function ($) {

    // :: ScrollUp Active Code
    if ($.fn.scrollUp) {
        $.scrollUp({
            scrollSpeed: 1000,
            easingType: 'easeInOutQuart',
            scrollText: '<i class="fa fa-angle-up" aria-hidden="true"></i>'
        });
    }

})(jQuery);