/**
 * 页面跳转
 * @param {Object} url
 */
function jump(url) {
	if (!url || url == 'null' || url == null) {
		window.location.href = './index.html';
	}
	// 路径访问设置
	localStorage.setItem('iframeUrl', url.replace('../', './pages/'));
	window.location.href = url;
}

/**
 * 返回
 */
function back(num = -1) {
	window.history.go(num)
}

/**
 * 生成订单
 */
function genTradeNo() {
	var date = new Date();
	var tradeNo = date.getFullYear().toString() + (date.getMonth() + 1).toString() +
		date.getDate().toString() + date.getHours().toString() + date.getMinutes().toString() +
		date.getSeconds().toString() + date.getMilliseconds().toString();
	for (var i = 0; i < 5; i++) //5位随机数，用以加在时间戳后面。
	{
		tradeNo += Math.floor(Math.random() * 10);
	}
	return tradeNo;
}

/**
* 获取当前时间（yyyy-MM-dd hh:mm:ss）
*/
function getCurDateTime() {
	var currentTime = new Date(),
	year = currentTime.getFullYear(),
	month = currentTime.getMonth() + 1 < 10 ? '0' + (currentTime.getMonth() + 1) : currentTime.getMonth() + 1,
	day = currentTime.getDate() < 10 ? '0' + currentTime.getDate() : currentTime.getDate(),
	hour = currentTime.getHours(),
	minute = currentTime.getMinutes(),
	second = currentTime.getSeconds();
    return year + "-" + month + "-" + day + " " +hour +":" +minute+":"+second;
}

/**
* 获取当前日期（yyyy-MM-dd）
*/
function getCurDate() {
	var currentTime = new Date(),
	year = currentTime.getFullYear(),
	month = currentTime.getMonth() + 1 < 10 ? '0' + (currentTime.getMonth() + 1) : currentTime.getMonth() + 1,
	day = currentTime.getDate() < 10 ? '0' + currentTime.getDate() : currentTime.getDate();
    return year + "-" + month + "-" + day;
}

/**
 * 防抖函数
 * @param {Function} fn - 需要防抖的函数
 * @param {Number} delay - 延迟时间（毫秒）
 */
function debounce(fn, delay) {
    var timer = null;
    return function() {
        var context = this;
        var args = arguments;
        clearTimeout(timer);
        timer = setTimeout(function() {
            fn.apply(context, args);
        }, delay);
    };
}

/**
 * 处理图片路径
 * @param {String} imgPath - 图片路径
 * @param {String} baseUrl - 基础URL
 * @returns {String} - 完整的图片URL
 */
function getImageUrl(imgPath, baseUrl) {
    if (!imgPath || imgPath === 'null' || imgPath === 'undefined' || imgPath === '') {
        return '';
    }
    // 如果已经是完整URL，直接返回
    if (imgPath.startsWith('http://') || imgPath.startsWith('https://')) {
        return imgPath;
    }
    // 如果路径以upload/开头，直接拼接baseUrl
    if (imgPath.startsWith('upload/')) {
        return baseUrl + imgPath;
    }
    // 处理逗号分隔的多张图片，取第一张
    var firstImage = imgPath.split(',')[0];
    if (firstImage.startsWith('upload/')) {
        return baseUrl + firstImage;
    }
    return baseUrl + 'upload/' + firstImage;
}

/**
 * 获取默认图片（SVG格式）
 * @param {Number} width - 宽度
 * @param {Number} height - 高度
 * @param {String} text - 显示文字
 * @returns {String} - Base64编码的SVG图片
 */
function getDefaultImage(width, height, text) {
    text = text || '暂无图片';
    return 'data:image/svg+xml,%3Csvg xmlns=%27http://www.w3.org/2000/svg%27 width=%27' + width + '%27 height=%27' + height + '%27%3E%3Crect fill=%27%23eeeeee%27 width=%27' + width + '%27 height=%27' + height + '%27/%3E%3Ctext fill=%27%23999999%27 font-family=%27Arial%27 font-size=%2714%27 x=%2750%25%27 y=%2750%25%27 text-anchor=%27middle%27 dominant-baseline=%27middle%27%3E' + encodeURIComponent(text) + '%3C/text%3E%3C/svg%3E';
}
