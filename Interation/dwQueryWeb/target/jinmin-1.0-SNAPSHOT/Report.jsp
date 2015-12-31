<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

  <title>Data Warehouse Project</title>
  <style type="text/css">
    .font {
      font-family:Arial,Helvetica,sans-serif;
      font-size:2em;
      vertical-align:middle;
      font-weight:normal

    }
    body{
      background: #f5faff;
    }
    .demo_con{
      width: 960px;
      margin:40px auto 0;
    }
    .button{
      width: 140px;
      line-height: 38px;
      text-align: center;
      font-weight: bold;
      color: #fff;
      text-shadow:1px 1px 1px #333;
      border-radius: 5px;
      margin:0 20px 20px 0;
      position: relative;
      overflow: hidden;
    }
    .button:nth-child(6n){
      margin-right: 0;
    }
    .button.black{
      border:1px solid #333;
      box-shadow: 0 1px 2px #8b8b8b inset,0 -1px 0 #3d3d3d inset,0 -2px 3px #8b8b8b inset;
      background: -webkit-linear-gradient(top,#656565,#4c4c4c);
      background: -moz-linear-gradient(top,#656565,#4a4a4a);
      background: linear-gradient(top,#656565,#4a4a4a);
    }
    .black.rarrow:before{
      background: #333;
    }
    .black.rarrow:after{
      box-shadow: 0 1px 0 #8B8B8B inset,-1px 0 0 #3d3d3d inset,-2px 0 0 #8B8B8B inset;
      background:-webkit-linear-gradient(top left,#656565,#4C4C4C);
      background:-moz-linear-gradient(top left,#656565,#4C4C4C);
      background:linear-gradient(top left,#656565,#4C4C4C);
    }
    .larrow:before{
      top: 6px;
      left: -13px;
      width: 27px;
      height: 27px;
      clip: rect(2px 26px auto auto);
    }
    .larrow:after{
      top: 6px;
      left: -12px;
      clip: rect(2px 26px auto auto);
    }
    .black.larrow:before{
      background: #333;
    }
    .black.larrow:after{
      box-shadow: 0 -1px 0 #3d3d3d inset,0 -2px 0 #8B8B8B inset,1px 0 0 #8B8B8B inset;
      background:-webkit-linear-gradient(top left,#656565,#4C4C4C);
      background:-moz-linear-gradient(top left,#656565,#4C4C4C);
      background:linear-gradient(top left,#656565,#4C4C4C);
    }
    .black:active{
      top:1px;
      box-shadow: 0 1px 3px #111 inset,0 3px 0 #fff;
      background: -webkit-linear-gradient(top,#424242,#575757);
      background: -moz-linear-gradient(top,#424242,#575757);
      background: linear-gradient(top,#424242,#575757);
    }
    .black.side:hover:after{
      background:-webkit-linear-gradient(right,#555,#6f6f6f 60%);
      background:-moz-linear-gradient(right,#555,#6f6f6f 60%);
      background:linear-gradient(right,#555,#6f6f6f 60%);
    }
    .black.side:active:after{
      box-shadow:-1px 0 1px #111 inset;
      background:-webkit-linear-gradient(right,#414040,#4d4c4c 60%);
      background:-moz-linear-gradient(right,#414040,#4d4c4c 60%);
      background:linear-gradient(right,#414040,#4d4c4c 60%);
    }
    .black.rarrow:hover:after,
    .black.larrow:hover:after{
      background:-webkit-linear-gradient(top left,#818181,#575757);
      background:-moz-linear-gradient(top left,#818181,#575757);
      background:linear-gradient(top left,#818181,#575757);
    }
    .black.rarrow:active:after,
    .black.larrow:active:after{
      background:-webkit-linear-gradient(top left,#424242,#575757);
      background:-moz-linear-gradient(top left,#424242,#575757);
      background:linear-gradient(top left,#424242,#575757);
    }
    .black.rarrow:active:after{
      box-shadow: 0 1px 0 #333 inset,-1px 0 0 #333 inset;
    }
    .black.larrow:active:after{
      box-shadow: 0 -1px 0 #333 inset,1px 0 0 #333 inset;
    }

    /* ---- Some Resets ---- */

    p,
    table, caption, td, tr, th {
      margin:3;
      padding:0;
      font-weight:normal;
    }

    /* ---- Paragraphs ---- */

    p {
      margin-bottom:15px;
    }

    /* ---- Table ---- */

    table {
      border-collapse:collapse;
      margin-bottom:0px;
      width:90%;
    }
    caption {
      text-align:center;
      font-size:15px;
      padding-bottom:10px;
    }

    table td,
    table th {
      padding:5px;
      border:1px solid #fff;
      border-width:1px 1px 1px 1px;
    }

    thead th {
      background:#91c5d4;
    }

    thead th[colspan],
    thead th[rowspan] {
      background:#66a9bd;
    }
    tbody th,
    tfoot th {
      text-align:left;
      background:#91c5d4;
    }
    tbody td,
    tfoot td {
      text-align:center;
      background:#d5eaf0;
    }
    tfoot th {
      background:#b0cc7f;
    }
    tfoot td {
      background:#d7e1c5;
      font-weight:bold;
    }
    tbody tr.odd td {
      background:#bcd9e1;
    }
  </style>

  <style>
    body {font-size:12px}
    td {text-align:center}
    h1 {font-size:26px;}
    h4 {font-size:16px;}
    em {color:#999; margin:0 10px; font-size:11px; display:block}
  </style>
</head>


<body style="background-color:gray">

<h1 style="text-align:center" class="font" >Data Warehouse</h1>
<form method="post" action="/parse">
<table bgcolor="gray" align="center" border="1" width="800" height="200">

  <tr>
    <td>SEARCH BY</td>
    <td>CONDITION</td>
    <td>AND</td>
    <td>OR</td>
  </tr>
  <tr>
    <td>Day</td>
    <td>
      <table align="center" >
        <tr>
          <td><input type="text" style="width:100px" name="DayL" ></td>
          <td>~</td>
          <td><input type="text" style="width:100px" name="DayR" ></td>
        </tr>
      </table>
    </td>
    <td><input type=checkbox name="DayAnd" value="true"></td>
    <td><input type=checkbox name="DayOr" value="true"></td>
  </tr>
  <tr>
    <td>Month</td>
    <td>
      <table align="center" >
        <tr>
          <td><input type="text" style="width:100px" name="MonthL" ></td>
          <td>~</td>
          <td><input type="text" style="width:100px" name="MonthR" ></td>
        </tr>
      </table>
    </td>
    <td><input type=checkbox name="MonthAnd" value="true"></td>
    <td><input type=checkbox name="MonthOr" value="true"></td>
  </tr>
  <tr>
    <td>Year</td>
    <td>
      <table align="center" >
        <tr>
          <td><input type="text" style="width:100px" name="YearL" ></td>
          <td>~</td>
          <td><input type="text" style="width:100px" name="YearR" ></td>
        </tr>
      </table>
    </td>
    <td><input type=checkbox name="YearAnd" value="true"></td>
    <td><input type=checkbox name="YearOr" value="true"></td>
  </tr>
  <tr>
    <td>Director</td>
    <td>
      <input type="text"  name="Director" style="width:230px"/>
    </td>
    <td><input type=checkbox name="DirAnd" value="true"></td>
    <td><input type=checkbox name="DirOr" value="true"></td>
  </tr>
  <tr>
    <td>Actor</td>
    <td>
        <input type="text"  name="Actor"/>
        <select name="selectActorType">
          <option value="star">star</option>
          <option value="support">support</option>
        </select>
    </td>
    <td><input type=checkbox name="ActAnd" value="true"></td>
    <td><input type=checkbox name="ActOr" value="true"></td>
  </tr>
  <tr>
    <td>MPAA-Level</td>
    <td>
        <select name="MPAA">
          <option value="NR (Not Rated)">NR (Not Rated)</option>
          <option value="Unrated" >Unrated</option>
          <option value="TV-G" >TV-G </option>
          <option value="R (Restricted)" >R (Restricted) </option>
          <option value="PG (Parental Guidance Suggested)">PG (Parental Guidance Suggested) </option>
          <option value="PG-13 (Parental Guidance Suggested)">PG-13 (Parental Guidance Suggested) </option>
          <option value="G (General Audience)">G (General Audience) </option>
          <option value="TV-Y" >TV-Y</option>
          <option value="TV-PG" >TV-PG</option>
          <option value="TV-MA" >TV-MA</option>
          <option value="X (Mature Audiences Only)" >X (Mature Audiences Only)</option>
          <option value="NC-17" >NC-17</option>
          <option value="TV-14" >TV-14</option>
          <option value="TV-NR" >TV-NR</option>
          <option value="TV-Y7" >TV-Y7</option>
        </select>
    </td>
    <td><input type=checkbox name="MpaaAnd" value="true"></td>
    <td><input type=checkbox name="MpaaOr" value="true"></td>
  </tr>
  <tr>
    <td>Type</td>
    <td>
        <table align="center">
          <tr>
            <td><input  type="checkbox" value="Comedy" name="Style"/>Comedy</td>
            <td><input  type="checkbox" value="Kids & Family" name="Style"/>Kids & Family </td>
            <td><input  type="checkbox" value="Science Fiction" name="Style"/>Science Fiction </td>
            <td><input  type="checkbox" value="Drama" name="Style"/>Drama </td>
            <td><input  type="checkbox" value="Thriller" name="Style"/>Thriller </td>
            <td><input  type="checkbox" value="Mystery" name="Style"/>Mystery </td>
            <td><input  type="checkbox" value="Horror" name="Style"/>Horror </td>
            <td><input  type="checkbox" value="Adventure" name="Style"/>Adventure </td>
          </tr>
          <tr>
            <td><input  type="checkbox" value="Action" name="Style"/>Action</td>
            <td><input  type="checkbox" value="Documentary" name="Style"/>Documentary </td>
            <td><input  type="checkbox" value="Fantasy" name="Style"/>Fantasy</td>
            <td><input  type="checkbox" value="Military & War" name="Style"/>Military & War </td>
            <td><input  type="checkbox" value="Western" name="Style"/>Western </td>
            <td><input  type="checkbox" value="Romance" name="Style"/>Romance </td>
            <td><input  type="checkbox" value="International" name="Style"/>International </td>
            <td><input  type="checkbox" value="Music" name="Style"/>Music </td>
          </tr>
          <tr>
            <td><input  type="checkbox" value="Reality TV" name="Style"/>Reality TV </td>
            <td><input  type="checkbox" value="Musical" name="Style"/>Musical </td>
            <td><input  type="checkbox" value="Sports" name="Style"/>Sports </td>
            <td><input  type="checkbox" value="Gay & Lesbian" name="Style"/>Gay & Lesbian </td>
            <td><input  type="checkbox" value="TV Game Shows" name="Style"/>TV Game Shows </td>
            <td><input  type="checkbox" value="TV Talk Shows" name="Style"/>TV Talk Shows </td>
            <td><input  type="checkbox" value="Other" name="Style"/>Other </td>
          </tr>
        </table>
    </td>
    <td><input type=checkbox name="StyleAnd" value="true"></td>
    <td><input type=checkbox name="StyleOr" value="true"></td>
  </tr>
</table>



<div align="center">
  <p></p>
  <input type="submit" class="button black"/>
</div>

</form>

<table bgcolor="gray" align="center" border="1" width="800" >
  <tr>
    <td>ID</td>
    <td>Name</td>
    <td>Style</td>
    <td>Duration</td>
    <td>Studio</td>
    <td>Time</td>
    <td>MPAA</td>
  </tr>

  <c:forEach var="movie" items="${requestScope.list}">
    <tr>
      <td>${movie.movieId}</td>
      <td>${movie.movieName}</td>
      <td>${movie.movieStyle}</td>
      <td>${movie.movieDuration}</td>
      <td>${movie.movieStudio}</td>
      <td>${movie.movieTime}</td>
      <td>${movie.movieMPAA}</td>
    </tr>
  </c:forEach>
</table>

</body>
</html>