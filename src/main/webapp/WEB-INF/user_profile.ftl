<html>
<head>
<title>User Profile</title>

</head>
 
<body>
    <div id="header">
    		<h2>${username}</h2>
    </div>

    <p> <h7><b>Name: </b>${name} </h7> <br>
        <h7><b>Erstellt am: </b>${datum} </h7> <br>
        <h7><b>${verkauft}</b> verkaufte(r) Artikel</h7></p>


    <#list anzeigeListe as anzeige>

        <fieldset style="width:270px">
        <legend> <h4> ${anzeige.titel} </h4> </legend>
        <label>Preis: ${anzeige.preis} € <br /> </label>
        <label>Erstellt am: ${anzeige.erstellungsDatum} <br />
        <label> ${anzeige.status} <br />

        </fieldset>
        <br>

       </#list>


</body>
</html>
