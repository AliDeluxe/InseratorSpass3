<html>
<head>

<title>Anmelden</title>

</head>
 
<body>

	
	

	<h2> Wie ist ihr Username? </h2>

  <form name="ersteller" action="hello" method="post">
	<table>
		<tr>
			<td align = "left" > <b> Username: </b> </td>
			<td align = "right"> <input type = "text" name = "username"/></td>
		</tr>
		<tr>
			<td align = "left" >Vorname:</td>
			<td align = "right"> <input type = "text" name = "firstname"/> </td>
		</tr>
		<tr>
			<td align = "left" >Nachname:</td>
			<td align = "right"> <input type = "text" name = "lastname"/> </td>
		</tr>
		<tr>
			<td align = "left" >		</td>
			<td align = "right"> <input type = "submit" value = "Erstellen"/></td>
		</tr>
	</table>
		
  </form>


  <table class="datatable">
    <tr>
        <th>Users</th>
    </tr>
    <#list users as ersteller>
    <tr>
        <td> <a href="hauptseite?currentUser=${ersteller.username}"> ${ersteller.username} </a> </td>
    </tr>
    </#list>
  </table>

</body>
</html>