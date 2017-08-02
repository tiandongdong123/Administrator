

<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=1170" />
    <meta name="save" content="history">
    <link href="http://static.wanfangdata.com.cn/wfks/css/animate.min.css?v=201607051606" rel="stylesheet" />
    <link href="http://static.wanfangdata.com.cn/wfks/css/global.min.css?v=201607051606" rel="stylesheet" />
    <link href="/Content/css/c.min.css" rel="stylesheet" />
    <link href="/favicon.ico" rel="shortcut icon" />
    <link href="/favicon.ico" rel="apple-touch-icon" />
    <!--[if lt IE 9 ]><link href="http://static.wanfangdata.com.cn/wfks/css/ltie9.min.css?v=201607051606" rel="stylesheet" /><![endif]-->
    
    <title>地方志-万方数据知识服务平台</title>
    <style>
        .door-content{width:1170px;margin:0 auto;margin-bottom:20px;}
        .containExbg{ margin: 0 auto; position: relative; width: 1000px;height:556px; background: url(../Images/mainbg.png)}
        .door-content-main { background-color: #f5f5f5; height: 556px; overflow: hidden; }
        .door-content-top, .door-content-bottom { background: url(../Images/sprocket.png) repeat-x left top; height: 6px; overflow: hidden; }
        .door-l, .door-r { position: absolute; top: 0; }
        .door-l { background: url(../Images/door-l.jpg) no-repeat; left: -100px; }
        .door-r { background: url(../Images/door-r.jpg) no-repeat; right: -100px; }
        .door-l a, .door-r a { background-color: #000; display: block; height: 556px; opacity: 0; width: 600px; filter: alpha(opacity=0); }
        .door-l a:hover, .door-r a:hover { opacity: 0.2; filter: alpha(opacity=20); }
    </style>

</head>
<body>
    <div class="header">
        <div class="nav clear">
            <a href="http://www.wanfangdata.com.cn/">
                <img class="logo" src="http://static.wanfangdata.com.cn/wfks/img/logo_new.png" alt="" /></a>
            <ul>
                <li class="active fixed" data-target="search-wrap">检索</li>
                <li id="navResource" data-target="resource-wrap">资源</li>
                <li id="navService" data-target="service-wrap">服务</li>
                <li class="profile"></li>
            </ul>
        </div>
        <div class="width-wrap search-wrap active fixed">
            <div class="panel search-panel clear">
                <div class="drp res-drp clear">
                    <div class="res-lb">
                        <span id="res" class="lb">学术论文</span>
                        <i class="icon-more"></i>
                    </div>
                    <div class="res-list bd pop" data-open=".res-lb">
                        <span class="clear-shadow"></span>
                        <ul class="resource clear"></ul>
                        <ul class="library clear"></ul>
                    </div>
                </div>
                <input class="search-input" type="search" name="q" autocomplete="off" />
                <div id="adSearchBtn" class="icon-search-ad" type="button">
		<div class="search-ad pop">
                    <a target="_blank" id="adSearchLink" href="http://librarian.wanfangdata.com.cn/">跨库检索</a>
                    <a target="_blank" href="http://www.wanfangdata.com.cn/NewGuide?helpitem=searchepaper">检索帮助</a>
                </div>
		</div>
                <button class="btn icon-search search-btn" type="submit"></button>
                <div class="search-tip-field pop"></div>
            </div>
        </div>
        <div class="width-wrap resource-wrap">
            <div class="panel resource-panel"></div>
        </div>
        <div class="width-wrap service-wrap">
            <div class="panel service-panel clear">
                <div class="md">
                    <div class="hd clear">
                        <i class="icon icon-zzfu"></i>
                        <h6 class="tt">增值服务</h6>
                    </div>
                    <div class="bd">
                        <a target="_blank" href="http://social.wanfangdata.com.cn/">万方学术圈</a>
                        <a target="_blank" href="http://trend.wanfangdata.com.cn/">知识脉络分析</a>
                        <a target="_blank" href="http://librarian.wanfangdata.com.cn/">查新/跨库检索</a>
                        <a target="_blank" href="http://librarian.wanfangdata.com.cn/ScientificLiterature">科技文献分析</a>                        
                    </div>
                </div>
                <div class="md">
                    <div class="hd clear">
                        <i class="icon icon-gjlfu"></i>
                        <h6 class="tt">工具类服务</h6>
                    </div>
                    <div class="bd">
                        <a target="_blank" href="http://check.wanfangdata.com.cn/">论文相似性检测</a>
                        <a target="_blank" href="http://stats.wanfangdata.com.cn/">国家经济统计数据库</a>
                        <a target="_blank" href="http://patentool.wanfangdata.com.cn/">专利工具</a>
                    </div>
                </div>
                <div class="md">
                    <div class="hd clear">
                        <i class="icon icon-bjbzyfu"></i>
                        <h6 class="tt">编辑部专用服务</h6>
                    </div>
                    <div class="bd">
                        <a target="_blank" href="http://stats.editors.wanfangdata.com.cn/">期刊统计分析与评价</a>
                        <a target="_blank" href="http://www.wanfangdata.com.cn/ResourceDescription/DOI">中文DOI</a>
                        <a target="_blank" href="http://publish.wanfangdata.com.cn/">优先出版</a>
                        <a target="_blank" href="http://publish.wanfangdata.com.cn/Regular">同步出版</a>
                        <a target="_blank" href="http://editors.wanfangdata.com.cn/">编审平台</a>
                    </div>
                </div>
                <div class="md">
                    <div class="hd clear">
                        <i class="icon icon-zzzyff"></i>
                        <h6 class="tt">作者专用服务</h6>
                    </div>
                    <div class="bd">
                        <a target="_blank" href="http://www.wanfangdata.com.cn/UserService?tab=citenotice">引用通知</a>
                        <a target="_blank" href="http://contribute.wanfangdata.com.cn/">投稿</a>
                        <a target="_blank" href="http://oa.wanfangdata.com.cn/">OA论文托管</a>
                    </div>
                </div>
                <div class="md last-md">
                    <div class="hd clear">
                        <i class="icon icon-yqzl"></i>
                        <h6 class="tt">舆情专栏</h6>
                    </div>
                    <div class="bd">
                        <a target="_blank" href="http://subject.wanfangdata.com.cn/">专题</a>
                        <a target="_blank" href="http://message.wanfangdata.com.cn/">科技动态</a>
                    </div>
                </div>
            </div>
        </div>
        <div class="width-wrap account-wrap">
            <div class="panel account-panel clear">
                <div class="top-link">
                    <a class="login-link">登录</a>
                    <a href="http://login.wanfangdata.com.cn/Register/Index">注册</a>
                    <a href="http://login.wanfangdata.com.cn/Logout.aspx">退出</a>
                </div>
                <div class="account-container">
                    <div class="account person-account">
                        <i class="icon icon-point"></i>
                        <span class="display-name"></span>
                        <span class="my-link">
                            <a target="_blank" href="http://www.wanfangdata.com.cn/UserService?tab=myorder">查看订单</a>
                            <a target="_blank" href="http://www.wanfangdata.com.cn/UserService?tab=citenotice">引用通知</a>
                            <a target="_blank" href="http://www.wanfangdata.com.cn/UserService?tab=mywallet">我的钱包</a>
                            <a target="_blank" href="http://tran.wanfangdata.com.cn/Charge.aspx">充值</a>
                            <a target="_blank" class="last-item" href="http://www.wanfangdata.com.cn/UserService?tab=edituser">个人信息管理</a>
                        </span>
                    </div>
                    <div class="account group-account">
                        <i class="icon icon-point"></i>
                        <span class="display-name"></span>
                        <span class="my-link">
                            <a target="_blank" class="order-link" href="http://istic.wanfangdata.com.cn/Order">查看订单</a>
                            <a target="_blank" class="last-item group-center" href="http://www.wanfangdata.com.cn/Organization">服务站</a>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </div>
                            
    
        <div class="title-wraper">
            <h1 class="title top-title">地方志</h1>
        </div>
        <div class="desc desc-resource">中国地方志数据库（China Local Gazetteers Database，CLGD），新方志收录始于1949年，40000余册，旧方志收录年代为0000-1949年，预计近50000册。</div>
    
    <div class="door-content">
        <div class="door-content-top"></div>
        <div class="door-content-main">
            <div class="container containExbg" id="sliderCont">
                <div class="door-l"><a href="#"></a></div>
                <div class="door-r"><a href="#"></a></div>
            </div>
        </div>
        <div class="door-content-bottom"></div>
    </div>

    <div class="footer-wrap">
        <div class="lt-bg"></div>
        <div class="rt-bg"></div>
        <div class="footer clear">
            <div class="lt">
                <div class="top">
                    <a target="_blank" href="http://www.wanfangdata.com.cn/NewGuide">帮助</a>
                    <a target="_blank" href="http://www.wanfangdata.com.cn/CustomerService/Services">客户服务</a>
                    <a target="_blank" href="http://www.sojump.com/jq/4550749.aspx">问卷调查</a>
                    <a target="_blank" href="http://www.wanfangdata.com.cn/AboutUs/index">关于我们</a>
                    <a target="_blank" href="http://www.wanfang.com.cn/">公司首页</a>
                    <a target="_blank" href="http://weibo.com/wanfangdata">平台微博</a>
                    <a target="_blank" href="http://wanfangdata.zhiye.com/">加入我们</a>
                    <a target="_blank" href="http://www.wanfangdata.com.cn/Sitemap">网站地图</a>
                    <a id="langCN" data-lang="zn-CN" class="lang">简</a>
                    <a id="langTW" data-lang="zn-TW" class="lang">繁</a>
                    <a id="langEN" data-lang="EN" href="http://www.wanfangdata.com/" target="_blank">ENG</a>
                </div>
                <div class="bottom">
                    <div>
                        <a target="_blank" href="http://ad.wanfangdata.com.cn/images/hlwcb.jpg">互联网出版许可证：新出网证(京)字042号</a>
                        <a target="_blank" href="http://ad.wanfangdata.com.cn/images/zhengshu_2.jpg">互联网药品信息服务资格证书号：(京)-经营性-2011-0017</a>
                        <a target="_blank" href="http://ad.wanfangdata.com.cn/images/stjmxkz2.jpg">信息网络传播视听节目许可证 许可证号：0108284 </a>
                    </div>
                    <div>
                        <a target="_blank" href="http://ad.wanfangdata.com.cn/images/icp.jpg">京ICP证：010071</a>
                        <a target="_blank" href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=11010802020237" ><img src="http://ad.wanfangdata.com.cn/images/gabeian.png" style="margin-right:2px;"/>京公网安备11010802020237号</a>
<a target="_blank" href="http://ad.wanfangdata.com.cn/images/yunicp.jpg">万方数据知识资源云服务系统[简称：知识云服务平台]V1.0 证书号：软著登字第1016589号</a>
                    </div>
                    <p>万方数据知识服务平台--国家科技支撑计划资助项目（编号：2006BAH03B01）©北京万方数据股份有限公司  万方数据电子出版社</p>
                </div>
            </div>
            <div class="rt">
                <div class="top">
                    <i class="icon icon-service"></i>
                    <span>联系客服</span>
                </div>
                <div class="mid">
                <span>4000115888</span>
                </div>
                <div class="service-email">service@wanfangdata.com.cn</div>
            </div>
        </div>
    </div>
                            
                            
    <script type="text/javascript" src="http://static.wanfangdata.com.cn/wfks/lib/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="http://static.wanfangdata.com.cn/wfks/lib/jquery-ui-1.11.4.custom.min.js"></script>
    <script type="text/javascript" src="http://www.wanfangdata.com.cn/Config/index"></script>
    <script type="text/javascript" src="http://static.wanfangdata.com.cn/wfks/js/global.min.js?v=201607051606"></script>
        <script type="text/javascript">
            (function () {
                wf.header.resource.init({ id: 'localchronicleitem' });
                wf.fixbox.init();
                $('.content-c .data-link').click(function () {
                    var $this = $(this);
                    var $list = $this.parent().parent();
                    var ajaxModel = {
                        filePath: $list.data('ajax-xmlpath'),
                        templ: $list.data('ajax-templ'),
                        pt: $list.data('ajax-pt'),
                        st: $list.data('ajax-st'),
                        wrap: $list.data('ajax-wrap')
                    };
                    $.getJSON('XmlFetch.ashx', { filePath: ajaxModel.filePath, nodeValue: $this.data('value') }, function (result) {
                        var html = [];
                        $this.addClass('active');
                        $list.find('.data-link').not($this).removeClass('active');
                        var pushItems = function (arr, t, fun, wrap) {
                            $.each(this, function () {
                                if ($.isFunction(fun)) { fun(this, arr, t); }
                                else { arr.push(t.format(this.Name, encodeURIComponent(this.Value), this.Buy === 'False' ? 'notbuy' : '')); }
                            });
                        }
                        if (ajaxModel.templ) {
                            pushItems.call(result.Children, html, ajaxModel.templ);
                        } else {
                            pushItems.call(result.Children, html, ajaxModel.pt, function (item, arr, pt) {
                                var sls = [];
                                pushItems.call(item.Children, sls, ajaxModel.st);
                                html.push('<div class="tb-list-item clear">{0}</div>'.format('<span class="p-link">{0}</span>'.format(pt.format(item.Name, encodeURIComponent(item.Value), item.Buy === 'False' ? 'notbuy' : '')) + '<span class="s-link">{0}</span>'.format(sls.join('<span class="spliter"> | </span>'))));
                            });
                        }
                        $list.next().html(html.join(''));
                    });
                });
            })();
        </script>
    
    <script>
        $(function () {
            var $sliderCont = $('#sliderCont');
            var $item = $sliderCont.children('div');
            var DELAY = 200;
            var leftLink = 'OldLocalChronicle.aspx';
            var rightLink = 'NewLocalChronicle.aspx';
            $item.click(function (e) {
                var $this = $(this);
                var idx = $this.index();
                e.preventDefault();
                if (idx == 0) {
                    $item.eq(0).animate({
                        'left': -500
                    }, DELAY, function () {
                        window.location.href = leftLink;
                    });
                } else {
                    $item.eq(1).animate({
                        'right': -500
                    }, DELAY, function () {
                        window.location.href = rightLink;
                    });
                };
            });
        });
    </script>

    
</body>
</html>

