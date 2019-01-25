<html>
<head>
<title>Anzeige Erstellen</title>

</head>
 
<body>
    <div id="header">
    		<h1> Anzeige erstellen </h1>
    </div>

  <form name="anzeige" action="/anzeige_erstellen" method="post">

    Titel: <br>
    <input type="text" name="titel" style="300px;" max="100"/> <br> <br>

    Preis(€): <br>
    <input type="number" name="preis" step="0.01" min="0" style="width:100px;"/> <br> <br>

    Beschreibung: <br>
    <input type="textarea" name="beschreibung" style="width:400px;height:100px;"/> <br> <br>

    <h4> Kategorie: </h4>

     <input type="checkbox" name="kategorie" value="Digitale Waren">
     Digitale Waren

     <input type="checkbox" name="kategorie" value="Haus & Garten">
     Haus & Garten

     <input type="checkbox" name="kategorie" value="Mode & Kosmetik">
     Mode & Kosmetik

     <input type="checkbox" name="kategorie" value="Multimedia & Elektronik">
     Elektronik & Multimedia
    <br> <br>


  <input type="submit" value="Erstellen" />
  </form>

  <p> <font color = red> ${fehlermeldung} </font> </p>
  <p ${erfolgreich}> <font color = green> <a href="anzeige_details?ID=${anzeigeid}">Anzeige wurde erfolgreich erstellt.</a> </font> </p>
 

</body>
</html>
