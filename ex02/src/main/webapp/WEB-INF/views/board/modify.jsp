<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@include file="../include/header.jsp"%>
<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">Board Modify Page</h1>
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
		
			<div class="panel-heading">Board Modify Page</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
					<form role="form" action="/board/modify" method="post">
						<input type="hidden" name="pageNum" value='<c:out value="${cri.pageNum}"/>'>
						<input type="hidden" name="amount" value='<c:out value="${cri.amount}"/>'>
						<input type="hidden" name="type" value='<c:out value="${cri.type }"/>'>
						<input type="hidden" name="keyword" value='<c:out value="${cri.keyword}"/>'>
						
						<div class="form-group">
							<label>Bno</label>
							<input class="form-control" name="bno" 
							value='<c:out value="${board.bno}"/>' readonly="readonly" >
						</div>
						
						<div class="form-group">
							<label>Title</label> 
							<input class="form-control" name="title" 
							value='<c:out value="${board.title}"/>'>
						</div>
						
						<div class="form-group">
							<label>Text area</label>
							<textarea class="form-control" rows="3" name="content"
							><c:out value="${board.content}" /></textarea>
						</div>
						
						<div class="form-group">
							<label>Writer</label>
							<input class="form-control" name="writer" 
							value='<c:out value="${board.writer}"/>' 
							readonly="readonly">
						</div>
						
						<div class="form-group" style="display: none;">
							<label>RegDate</label>
							<input class="form-control" name="regDate" 
							value='<fmt:formatDate pattern="yyyy/MM/dd" value="${board.
							regdate}" />' readonly="readonly"/> 
						</div>
						
						<div class="form-group" style="display: none;">
							<label>updateDate</label>
							<input class="form-control" name="regDate" 
							value='<fmt:formatDate pattern="yyyy/MM/dd" value="${board.
							updateDate}" />' readonly="readonly"/> 
						</div>
						
						<button type="submit" data-oper="modify" class="btn btn-default">
							Modify
						</button>
						
						<button type="submit" data-oper="remove" class="btn btn-danger">
							Remove
						</button>
						
						<button type="submit" data-oper="list" class="btn btn-info">
							List
						</button>
					</form>
			</div>
			<!-- /.table-responsive -->
		</div>
		<!-- /.panel-body -->
	</div>
	<!-- /.panel -->
</div>

<%@include file="../include/footer.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		
		var formObj = $("form");
		
		$('button').on("click",function(e){
			
			e.preventDefault();
			
			var operation = $(this).data("oper");
			
			console.log(operation);
			
			if(operation === 'remove'){
				formObj.attr("action","/board/remove");
			}else if(operation === 'list'){
				//move to list
				
				formObj.attr("action","/board/list").attr("method","get");
				var pageNumTag = $("input[name='pageNum']").clone();	//List버튼을 클릭한다면 <form> 태그에서 필요한 부분만 잠시 복사(clone)해 보관해두고
				var amountTag = $("input[name='amount']").clone();		//<form>태그 내의 모든 내용은 지워버린다(empty) 
				var keywordTag = $("input[name='keyword']").clone();
				var typeTag = $("input[name='type']").clone();
				
				formObj.empty();
				formObj.append(pageNumTag);		//다시필요한 태그만 추가해서 name값으로 list에 get방식으로 /board/list를 호출한다
				formObj.append(amountTag);
				formObj.append(keywordTag);
				formObj.append(typeTag);
				
				/* self.location = "/board/list";
				return; */
			}
			formObj.submit();
			
		});
	});
</script>