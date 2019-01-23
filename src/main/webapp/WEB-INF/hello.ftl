<html>
<head>
<title>Hello World</title>

</head>
 
<body>
  <form name="ersteller" action="hello" method="post">
    Firstname: <input type="text" name="firstname" /> <br/>
    Lastname: <input type="text" name="lastname" /> <br/>
    <input type="submit" value="Save" />
  </form>
 
  <table class="datatable">
    <tr>
        <th>Firstname</th>  <th>Lastname</th>
    </tr>
    <#list users as ersteller>
    <tr>
        <td>${ersteller.firstname}</td> <td>${ersteller.lastname}</td>
    </tr>
    </#list>
  </table>
</body>
</html>