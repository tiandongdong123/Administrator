/**
 * Created by 崔微娜 on 2018/12/13.
 */
//翻页跳转
(function () {
    var getPager = function (url, $container) {
        // $.get(url, function (html) {
        //     $container.replaceWith(html);
        // });
        $container.find('.loadingPic').show();
        $.ajax({
            url:url,
            success:function (html) {
                $container.replaceWith(html);
                $container.find('.loadingPic').hide();
            },
            error:function () {
                $container.find('.loadingPic').hide();
            }
        });
    };
    //page-a异步跳转
    $(document).on('click', '.sync-html .mypage a', function () {
        var $this = $(this);
        // $this.closest('.sync-html').empty('');
        var href = $this.attr('href');
        $this.removeAttr('href');
        $this.closest('.sync-html').find('.loadingPic').show();
        // $.get(href, function (html) {
        //     $this.closest('.sync-html').html(html);
        //     $this.closest('.sync-html').find('.loadingPic').hide();
        // })
        $.ajax({
            url:href,
            success:function (html) {
                $this.closest('.sync-html').replaceWith(html);
                $this.closest('.sync-html').find('.loadingPic').hide();
            },
            error:function () {
                $this.closest('.sync-html').find('.loadingPic').hide();
            }
        });
    });
    //page-form异步跳转
    $(document).on('submit', '.sync-html .mypage form', function () {
        var $this = $(this);
        var action = $this.attr('action');
        var inputPage = parseInt($this.find('.laypage_skip').last().val());
        var allPage = $this.attr('data-all');
        if (inputPage > 0 && inputPage <= allPage) {
            var href = action + inputPage;
            getPager(href, $this.closest('.sync-html'));
        } else {
            alert('请输入正确页码');
        }
        return false;
    });
    //page-form同步跳转
    $(document).on('submit', '.no-sync .mypage form', function () {
        var $this = $(this);
        var action = $this.attr('action');
        var inputPage = parseInt($this.find('.laypage_skip').last().val());
        var allPage = $this.attr('data-all');
        if (inputPage > 0 && inputPage <= allPage) {
            window.location.href = action + encodeURIComponent(inputPage);
        } else {
            alert('请输入正确页码');
        }
        return false;
    });
})();
