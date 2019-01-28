<html>
<head>
<title>Hauptseite</title>
    <h1> Hauptseite </h1>
</head>
 
<body>

<form name="aktualisieren" action="" method"get">
  <p> Sortieren nach:
  <select name="sortieren">
      <option value="nicht">-Bitte auswählen-</option>
      <option value="titel asc">Titel Aufsteigend</option>
      <option value="titel desc">Titel Absteigend</option>
      <option value="erstellungsdatum asc">Datum Neu zuerst</option>
      <option value="erstellungsdatum desc">Datum Alt zuerst</option>
    </select>

  Filtern nach:
  <select name="filtern">
      <option value="nicht">-Bitte auswählen-</option>
      <option value="Digitale Waren">Digitale Waren</option>
      <option value="Haus & Garten">Haus & Garten</option>
      <option value="Mode & Kosmetik">Mode & Kosmetik</option>
      <option value="Multimedia & Elektronik">Elektronik & Multimedia</option>
    </select>
    </p>


    <input type="submit" value="Aktualisieren">
    </form>



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
