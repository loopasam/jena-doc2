$(document).ready(function() {
	var url = $(location).attr('href');

	//Regex must be modified by export website script!
	var matches = url.split('/').slice(3);
	console.log(matches);
	var breadcrumbs = '<ol class="breadcrumb">';
	matches.forEach(function(entry) {
		console.log("entry: " + entry);
		breadcrumbs += '<li><a href="#">' + entry + '</a></li>'; 
	});
	breadcrumbs += '</ol>';
	$('#breadcrumbs').append(breadcrumbs);
});

//TODO
//Handle urls to link parts
// get rid of index.html and the html trailing if not index
// Get rid of the underscore in labels
//Get read of breadcrumbs on the home page
