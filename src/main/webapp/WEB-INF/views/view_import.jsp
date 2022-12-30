<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layouts/taglib.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript"
	src="<c:url value="/assets/import/js/bootstrap.bundle.min.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/assets/import/js/jquery.slim.min.js" />"></script>
<link rel="stylesheet"
	href="<c:url value="/assets/import/css/bootstrap.min.css" />" />
<link rel="stylesheet"
	href="<c:url value="/assets/import/css/style.css" />" />
<title>Import File</title>

</head>
<body>
	<div class="wrapper fadeInDown">
		<div id="formContent">
			<h2 style="color: gray;">Import file excel</h2>
			<form id="myUploadForm" class="mt-3" action="" method="POST"
				enctype="multipart/form-data">
				<input id="file" name="file" type="file"
					class="form-control-file fadeIn second" value="" /><br /> <input
					type="button" class="fadeIn third mt-2" id="btn_import"
					value="Upload">
			</form>
		</div>
	</div>
</body>

<!-- JavaScript -->
<script
	src="https://cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/alertify.min.js"></script>

<!-- CSS -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/alertify.min.css" />
<!-- Default theme -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/themes/default.min.css" />
<!-- Semantic UI theme -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/themes/semantic.min.css" />
<!-- Bootstrap theme -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/themes/bootstrap.min.css" />

<script
	src="<c:url value='/assets/user/plugins/jquery/jquery.min.js' />"></script>
<script type="text/javascript">
	$( "#btn_import" ).on( "click", function() {
		var form = new FormData();
		form.append("file", $('#file')[0].files[0]);

		var settings = {
			"url": "http://10.1.3.10:3001/upload",
			"method": "POST",
			"timeout": 0,
			"headers": {
			},
			"processData": false,
			"mimeType": "multipart/form-data",
			"contentType": false,
			"data": form
		};

		$.ajax(settings).done(function (response) {
		  	console.log(response);
		  	try {
			  response = JSON.parse(response);
		  	} catch (e) {
			  
		  	}
		  	console.log(response.message);
		  	alertify.success(response.message);
		  	setTimeout(function(){ 
		  		history.back();
			}, 1500);
		}).catch(function(e) {
			console.log(e.responseText);
			var err = {};
			try {
			 	err = JSON.parse(e.responseText);
			} catch (e) {
			 
			}
			alertify.error(err.detail);
		});
		
	  	return false;
	});
</script>

</html>