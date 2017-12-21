/**
 * 第一个执行的部分有可能加载一个不存在空对象
 * 避免空对象导致前端js异常
 */
 
	var module1 = (function(mod){
		mod.m3 = function () {
			console.log('============>add m3');
			return 'add m3';
	    };
		return mod;
	})(window.module1 || {});
 