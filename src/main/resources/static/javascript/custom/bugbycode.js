$(function() {
　　if (window.history && window.history.pushState) {
	　　$(window).on('popstate', function () {
	　　	window.history.pushState('forward', null, '#');
	　　	window.history.forward(1);
	　　});
　　}
　　window.history.pushState('forward', null, '#'); //在IE中必须得有这两行
　　window.history.forward(1);

   $("a").on('hover',function(e) {
	   var title = $(this).prop('title');
		if (title != undefined) {
			$(this).tooltip({
				title: title,
				placement: 'top',
				trigger: 'hover',
				container: 'body'
			});
			$(this).tooltip('show');
			$(this).attr('data-init', true);
		}
	});
});