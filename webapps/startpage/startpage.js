var startpage = {};

window.onload = function() {
	var subPath=document.location.hash.split('#')[1];
	document.getElementById(subPath).click();
}

startpage.resizeIframe = function(obj) {	
	try {
		var win = obj.contentWindow || obj.contentDocument;
		obj.style.height = win.document.body.offsetHeight + 50 + 'px';
	} catch(err) {
		console.log('An error occurred during optimising size of iFrame - using default settings instead.')
	}
};

startpage.setContent = function(path) {
	$("#mainContent").attr("src", path);
};

startpage.openRestDemos = function() {
	startpage.setContent('./clientExample/client.html');
    $("#submenuRestUL").attr("hidden", false);
};

startpage.openSolutionDemos = function() {
    $("#submenuSolutionDemosUL").attr("hidden", false);
};