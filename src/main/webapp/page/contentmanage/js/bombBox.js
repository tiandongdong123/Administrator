var h1 = $(window).height();
	var h2 = $('.bombBox').height();
	var h = (h1-h2)/2+h2/2;
	$(window).scroll(function(){
		var iScroll = $(document).scrollTop();
		$('.bombBox').css('position','fixed');
	});
	