<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Error</title>
<link rel="stylesheet" style="text/css" href="css/main.css">
<link rel="stylesheet" style="text/css" href="css/bootstrap.min.css">
</head>
<body>
	<div class="nav">
		<div class="nav_left">
			<a href="index.jsp"><img alt="logo" src="images/LOGO.png"></a>
		</div>
		<div class="nav_right">
			<div class="nav_form">
				<form action="search" method="get">
					<input id="query" type="text" name="query" value=" "> <input
						type="submit" value="搜索"><br />
					<div class="sortPick">
						按相关度排序 <input type="radio" value="byreRelevancy" name="sortnews"
							checked="checked""> 按时间排序 <input type="radio"
							value="byTime" name="sortnews"">

					</div>
				</form>

			</div>
		</div>
	</div>

	<div class="newmain">
	  <h4>共搜到0条结果</h4>
	</div>

</body>
</html>