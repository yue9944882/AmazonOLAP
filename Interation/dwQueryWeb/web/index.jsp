<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: guguli
  Date: 2015/12/29
  Time: 13:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
  </head>
  <body>
    <h1>AmazonOLAP</h1>
    <hr>

    <h3>STYLE</h3>
    <form method="post" action="/parse">
      <input type="checkbox" name="style" value="Comedy"/>Comedy<br>
      <input type="checkbox" name="style" value="Kids & Family"/>Kids & Family<br>
      <input type="checkbox" name="style" value="Science Fiction"/>Science Fiction<br>
      <input type="checkbox" name="style" value="Drama"/>Drama<br>
      <input type="checkbox" name="style" value="Thriller"/>Thriller<br>
      <input type="checkbox" name="style" value="Mystery"/>Mystery<br>
      <input type="checkbox" name="style" value="Horror"/>Horror<br>
      <input type="checkbox" name="style" value="Adventure"/>Adventure<br>
      <input type="checkbox" name="style" value="Action"/>Action<br>
      <input type="checkbox" name="style" value="Documentary"/>Documentary<br>
      <input type="checkbox" name="style" value="Fantasy"/>Fantasy<br>
      <input type="checkbox" name="style" value="Military & War"/>Military & War<br>
      <input type="checkbox" name="style" value="Western"/>Western<br>
      <input type="checkbox" name="style" value="Romance"/>Romance<br>
      <input type="checkbox" name="style" value="International"/>International<br>
      <input type="checkbox" name="style" value="Music"/>Music<br>
      <input type="checkbox" name="style" value="Reality TV"/>Reality TV<br>
      <input type="checkbox" name="style" value="Musical"/>Musical<br>
      <input type="checkbox" name="style" value="Sports"/>Sports<br>
      <input type="checkbox" name="style" value="Gay & Lesbian"/>Gay & Lesbian<br>
      <input type="checkbox" name="style" value="TV Game Shows"/>TV Game Shows<br>
      <input type="checkbox" name="style" value="TV Talk Shows"/>TV Talk Shows<br>
      <input type="checkbox" name="style" value="Other"/>Other<br>

      <hr>

      <% response.sendRedirect("report.jsp");%>

      <hr>
      <input type="submit" value="查询">
    </form>
  </body>
</html>
