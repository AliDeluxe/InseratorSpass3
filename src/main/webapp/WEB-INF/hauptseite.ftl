<html>
<head>
<title>Hauptseite</title>
    <h1> Hauptseite </h1>
</head>
 
<body>


  <#list anzeigeListe as anzeige>

    <fieldset style="width:270px">
    <legend> <h4> <a href="anzeige_details?ID=${anzeige.id}"> ${anzeige.titel} </a> </h4> </legend>
    <label>Preis: ${anzeige.preis} €<br /></label>
    <label>Erstellt am: ${anzeige.erstellungsDatum}<br />
    <label>Ersteller: <a href="user_profile?Ersteller=${anzeige.ersteller}">${anzeige.ersteller}</a> <br />

    </fieldset>
    <br>

   </#list>

	
	


   <a href="anzeige_erstellen?currentUser=${currentUser}"> Neue Anzeige erstellen </a>
   

</body>
</html>
