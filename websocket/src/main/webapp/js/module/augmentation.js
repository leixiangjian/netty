/**
 * 如果一个模块很大，必须分成几个部分，或者一个模块需要继承另一个模块，这时就有必要采用"放大模式"（augmentation）。
 */
var module1 = (function(mod){
	mod.m3 = function () {
		console.log('============>add m3');
		return 'add m3';
    };
	return mod;
})(module1);