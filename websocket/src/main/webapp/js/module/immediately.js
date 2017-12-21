/**
 * 立即执行函数的写法
 */
var module1 = (function(){
	var _count=0;
	var m1 = function(){
		return '11111';
	};
	var m2 = function(){
		return '2222';
	};
	return {
		m1 : m1,
	    m2 : m2
	};
})();