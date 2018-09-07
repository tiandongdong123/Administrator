//翻页跳转
(function () {
    var getPager = function (url, $container) {
        var evey = $(".evey-page").val();
        $.get(url, function (html) {
            $container.html(html);
            $(".evey-page").val(evey);
            redq();
        });
    };
    //page-a异步跳转
    $(document).on('click', '.sync-html .page_bind a', function () {
        var evey = $(".evey-page").val();
        $('.sync-html').empty('');
        var href = $(this).attr('href');
        $(this).removeAttr('href');
        $.get(href, function (html) {
            $('.sync-html').html(html);
            $(".evey-page").val(evey);
            redq();
        });

    });
    //page-form异步跳转
    $(document).on('submit', '.sync-html .page_bind form', function () {
        var evey = $(".evey-page").val();
        var action = $(this).attr('action');
        var inputPage = parseInt($(this).find('.laypage_skip').val());
        var allPage = $(this).attr('data-all');
        if (inputPage > 0 && inputPage <= allPage) {
            var href = action + inputPage;
            getPager(href, $(this).closest('.sync-html'));
            $(".evey-page").val(evey);
            redq();
        } else {
            alert('请输入正确页码');
        }
        return false;
    });
    //page-form同步跳转
    $(document).on('submit', '.no-sync .page_bind form', function () {
        var evey = $(".evey-page").val();
        var action = $(this).attr('action');
        var inputPage = parseInt($(this).find('.laypage_skip').val());
        var allPage = $(this).attr('data-all');
        if (inputPage > 0 && inputPage <= allPage) {
            window.location.href = action + encodeURIComponent(inputPage);
            $(".evey-page").val(evey);
            redq();
        } else {
            alert('请输入正确页码');
        }
        return false;
    });
})();