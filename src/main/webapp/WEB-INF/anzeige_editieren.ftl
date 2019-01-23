<html>
<head>
<title>Anzeige Editieren</title>

</head>
 
<body>
    <div id="header">
    		<h1> Anzeige editieren </h1>
    </div>

  <form name="anzeige" action="/anzeige_editieren" method="post">

    Titel: <br>
    <input type="text" name="titel"  style="300px;" max="100"/> <br> <br>

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


  <input type="submit" value="Aktualisieren" />
  </form>
 

</body>
</html>
