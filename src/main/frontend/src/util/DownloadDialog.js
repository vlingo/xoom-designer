export default {
	forZipFile(filename, base64) {
		open(filename, base64, "application/zip");
	},

	forJsonFile(filename, base64) {
		open(filename, base64, "text/json");
	},
}

function open(filename, base64, mimeType) {
	var link = document.createElement('a');
	link.href = "data:" + mimeType + ";base64," + base64;
	link.download = filename;
	document.body.appendChild(link);
	link.click();
	link.remove();
}