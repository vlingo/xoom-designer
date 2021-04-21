export default {
	download(base64) {
		var link = document.createElement('a');
		link.href = "data:application/zip;base64," + base64;
		link.download = 'project.zip';
		document.body.appendChild(link);
		link.click();
		link.remove();
	}
}