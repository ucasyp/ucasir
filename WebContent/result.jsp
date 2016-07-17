<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="ucas.ir.pojo.*,java.util.*"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
	List<News> arrlist = (List<News>) request.getAttribute("newslist");
	String queryback = (String) request.getAttribute("queryback");
	int totalnews = (Integer) request.getAttribute("totaln");
	double time = Double.parseDouble(request.getAttribute("time").toString());
	Page pageInfo=(Page)request.getAttribute("page");
	//out.print(arrlist.size());
	int p = 1, i;
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>UCAS IR</title>
<link rel="stylesheet" style="text/css" href="css/main.css">
<!--  -->
<link rel="stylesheet" style="text/css" href="css/bootstrap.min.css">
<script type="text/javascript">
	window.onload = function() {
		document.getElementById("query").value ="<%=queryback%>";
	}
	
</script>
</head>
<body>
	<div class="nav">
		<div class="nav_left">
			<a href="index.jsp"><img alt="logo" src="images/LOGO.png"></a>
		</div>
		<div class="nav_right">
			<div class="nav_form">
				<form action="search" method="get">
					    <input id="query" type="text" name="query" value=" "> 
					    <input type="submit" value="搜索"><br /> 
					 <div class="sortPick">
					     按相关度排序 <input type="radio" value="byreRelevancy" name="sortnews"  checked="checked" ">
						 按时间排序 <input type="radio"  value="byTime" name="sortnews"  "> 
						 
				     </div>
				</form>

			</div>
		</div>
	</div>

	<div class="newmain">
		<h4>

			共搜到<span class="newsnum"><%=totalnews%></span>条结果|用时<span
				class="newsnum"><%=time%></span>秒
		</h4>
		<%
			if (arrlist.size() > 0) {
				Iterator<News> iter = arrlist.iterator();
				News news;
				while (iter.hasNext()) {
					news = iter.next();
		%>

		<div class="item">
			<h4>
				<a href="<%=news.getURL()%> " target="_blank"><%=news.getTitle()%></a>
			</h4>
			<p><%=news.getArtical().length() > 200 ? news.getArtical().substring(0, 200) : news.getArtical()%></br>
				<a href=""><%=news.getURL()%></a>
			</p>
		</div>
		<%
			}
			}
		%>
	</div>
	<div class="paging">
		<ul>
			<li><a href="search?query=<%=queryback%>&&p=1">首页</a></li>
			<li><a href="search?query=<%=queryback%>&&p=<%=pageInfo.getPage()-1==0?1:pageInfo.getPage()-1%>">上一页</a></li>
			<%
				for (i = 1; i <= 10; i++) {
			%>
			<li><a href="search?query=<%=queryback%>&&p=<%=i%>"><%=i%></a></li>
			<%
				}
			%>
			<li><a href="search?query=<%=queryback%>&&p=<%=pageInfo.getPage()+1%>">下一页</a></li>
		</ul>
		<hr>
	</div>
	<div class="footerinfo">
		<p>现代信息检索 工程类搜索型&copy;2016 All Rights Reserved</p>
	</div>
</body>
</html>