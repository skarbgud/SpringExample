<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
	.uploadResult{
		width: 100%;
		background-color: gray;
	}
	
	.uploadResult ul{
		display: flex;
		flex-flow: row;
		justify-content: center;
		align-items: center;
	}
	
	.uploadResult ul li{
		list-style: none;
		padding: 10px;
		align-content: center;
		text-align: center;
	}
	
	.uploadResult ul li img{
		width: 100px;
	}
	
	.uploadResult ul li span{
		color: white;
	}
</style>
<style>
	.bigPictureWrapper{ 
		position: absolute;
		display: none;
		justify-content: center;
		align-items: center;
		top: 0%;
		width: 100%;
		height: 100%;
		background-color: gray;
		z-index: 100;
		background: rgba(255,255,255,0.5);
	}
	.bigPicture{
		position: relative;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.bigPicture img{
		width: 600px;
	}
</style>
</head>
<body>
	<h1>Upload with Ajax</h1>
	
	<div class="bigPictureWrapper">
		<div class="bigPicture">
		</div>
	</div>
	
	<div class="uploadDiv">
		<input type="file" name="uploadFile" multiple>
	</div>
	
	<div class='uploadResult'>
		<ul>
		
		</ul>
	</div>
	
	
	
	<button id="uploadBtn">Upload</button>
	
	<script src="https://code.jquery.com/jquery-3.5.1.js" integrity="sha256-QWo7LDvxbWT2tbbQ97B53yJnYU3WhH/C8ycbRAkjPDc=" crossorigin="anonymous"></script>
	
	<script>
	//첨부파일 삭제는 <span> 태그를 이용해서 처리하지만, 첨부파일의 업로드 후에 생성되기 때문에 '이벤트 위임'방식으로 처리해야
	//한다. 이벤트 처리에서는 Ajax를 이용해서 첨부파일의 경로와 이름, 파일의 종류를 전송한다.
	$(".uploadResult").on("click","span",function(e){
		
		var targetFile= $(this).data("file");
		var type = $(this).data("type");
		console.log(targetFile);
		
		$.ajax({
			url: '/deleteFile',
			data: {fileName:targetFile, type:type},
			dataType:'text',
			type: 'POST',
			success: function(result){
				alert(result);
			}
		});//$.ajax
		

	});
	//showImage() 함수는 jQuery의 $(document).ready()의 바깥쪽에 작성한다.이렇게 하는 이유는 나중에 <a>태그에서 직접 showImage()를 호출할 수 있는 방식으로 작성하기 위해서 이다.
	function showImage(fileCallPath) {
		//alert(fileCallPath);
		
		$(".bigPictureWrapper").css("display","flex").show();
		
		$(".bigPicture")
		.html("<img src='/display?fileName=" +encodeURI(fileCallPath)+"'>")
		.animate({width:'100%',height:'100%'},1000);
		//showImage()는 내부적으로 화면 가운데 배치하는 작업 후 <img> 태그를 추가하고,
		//jQuery의 animate()를 이용해서 지정된 시간 동안 화면에서 열리는 효과를 처리한다.
	
		
		
		//원본 이미지가 보여지는 <div>는 전체 화면을 차지하기 때문에 다시 클릭하면 사라지도록 이벤트를 처리한다.
		$(".bigPictureWrapper").on("click",function(e){
			$(".bigPicture").animate({width:'0%',height:'0%'},1000);
			setTimeout(function() {
				$('.bigPictureWrapper').hide();
			},1000);
		});
		
		
	}
	
	$(document).ready(function(){
		
		var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
		var maxSize = 5242880;	//5MB
		
		function checkExtension(fileName, fileSize) {
			
			if(fileSize >= maxSize){
				alert("파일 사이즈 초과");
				return false;
			}
			
			if(regex.test(fileName)){
				alert("해당 종류의 파일은 업로드할 수 없습니다.");
				return false;
			}
			return true;
		}
		
		var cloneObj = $(".uploadDiv").clone();
		
		$("#uploadBtn").on("click",function(e){
			
			var formData = new FormData();
			
			var inputFile = $("input[name='uploadFile']");
			//jQuery를 이용하는 경우에는 파일 업로드는 FormData라는 객체를 이용하게 된다. FormData는 쉽게 말해서 가상의 form태그와 같다고 생각하면 된다.
			//Ajax를 이용하는 파일 업로드는 FormData를 이용해서 필요한 파라미터를 담아서 전송하는 방식이다.	
			var files = inputFile[0].files;
			
			console.log(files);
			
			//add filedate to formdata	첨부파일 데이터는 formData에 추가한 뒤에 Ajax를 통해서 formData 자체를 전송
			for (var i = 0; i < files.length; i++) {
				
				if(!checkExtension(files[i].name, files[i].size)){
					return false;
				}
				
				formData.append("uploadFile",files[i]);
			}
			

			$.ajax({
				url : '/uploadAjaxAction',
				processData : false,
				contentType : false,
				data : formData,
				type : 'POST',
				dataType : 'json',
				success : function(result) {

					console.log(result);
					
					showUploadedFile(result);

					$(".uploadDiv").html(cloneObj.html());
					
				}
			}); //$.ajax
			
		});
		
		
		var uploadResult = $(".uploadResult ul");
		
		function showUploadedFile(uploadResultArr) {
			
			var str = "";
			
			$(uploadResultArr).each(function(i, obj) {
				
				if(!obj.image){
					
					var fileCallPath = encodeURIComponent(obj.uploadPath+"/"+obj.uuid+"_"+obj.fileName);
					
					var fileLink = fileCallPath.replace(new RegExp(/\\/g),"/");
					
					str +="<li><div><a href='/download?fileName="+fileCallPath+"'>"
						+"<img src='/resources/img/attach.png'>"+obj.fileName+"</a>"
						+"<span data-file=\'"+fileCallPath+"\' data-type='file'>x</span>"
						+"<div></li>";
						
				}else{
				
				       var fileCallPath =  encodeURIComponent( obj.uploadPath+ "/s_"+obj.uuid +"_"+obj.fileName);
				       
				       var originPath = obj.uploadPath+ "\\"+obj.uuid +"_"+obj.fileName;
				       
				       originPath = originPath.replace(new RegExp(/\\/g),"/");
				       
				       str += "<li><a href=\"javascript:showImage(\'"+originPath+"\')\">"+
				       		  "<img src='/display?fileName="+fileCallPath+"'></a>"+
				       		  "<span data-file=\'"+fileCallPath+"\' data-type='image'>x</span>"+
				              "<li>";
				       
				     }
			});
			
			uploadResult.append(str);
		}
	});
	</script>

	
</body>
</html>