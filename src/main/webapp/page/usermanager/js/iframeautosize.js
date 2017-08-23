function doReSize(framename) {
    var iframeWin = window.frames[framename];
    var iframeEl = window.document.getElementById ? window.document.getElementById(framename) : document.all ?
        document.all[framename] : null;
    if (iframeEl && iframeWin) {
        var docHei = getDocHeight(iframeWin.contentDocument ? iframeWin.contentDocument : iframeWin.document);
        if (docHei != iframeEl.style.height) iframeEl.style.height = docHei + 'px';
    }
    else if (iframeEl) {
        var docHei = getDocHeight(iframeEl.contentDocument);
        if (docHei != iframeEl.style.height) iframeEl.style.height = docHei + 'px';
    }
}
function getDocHeight(doc) {
    var scrollHei = 0; //scrollHeight
    var offsetHei = 0; //offsetHeight，包含了边框的高度
    if (doc.body) {
        if (doc.body.offsetHeight) offsetHei = doc.body.offsetHeight;
        if (doc.body.scrollHeight) scrollHei = doc.body.scrollHeight;
    }
    else if (doc.documentElement) {
        if (doc.documentElement.offsetHeight) offsetHei = doc.documentElement.offsetHeight;
        if (doc.documentElement.scrollHeight) scrollHei = doc.documentElement.scrollHeight;
    }
    else if (doc.height) {
        //Firefox支持此属性，IE不支持
        offsetHei = scrollHei = doc.height;
    }
    if (offsetHei == scrollHei)
        return offsetHei;
    else return Math.max(scrollHei, offsetHei);
}
function setheight() {
    setInterval(function () {
        $.each($('.frameinfo'), function (n) {
            doReSize(this.id);
        });
    }, 200);
}
$(function(){
    $.each($('.frameinfo'), function (n) {
        $(this).load(function () {
            var innerDoc = $(this.contentWindow.document);
            $(this).height(innerDoc.height());
            setTimeout(setheight, 1000);
        });
    });
})