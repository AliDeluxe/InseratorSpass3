<html>
<head>
<title>Anzeige Details</title>

</head>
 
<body>
    <div id="header">
    		<h1> Anzeige Details </h1>
    </div>

    <fieldset style="width:270px">

        <legend> <h4> ${titel} </h4> </legend>
        <label>Preis: ${preis} €<br /></label>
        <label>Erstellt am: ${erstellungsdatum}<br />
        <label>Ersteller: <a href="user_profile?Ersteller=${ersteller}">${ersteller}</a> <br />

        </fieldset>


    <p> ${beschreibung} </p>


  <a href="anzeige_editieren?ID=${id}">Editieren</a> <br> <br>


  <form name="kaufen" action="" method="post">
  <input type="submit" value="Kaufen" />
  </form>

  <form name="löschen" action="" method="post">
    <input type="submit" name="loeschen" value="Löschen" />
    </form>


  <br> <br>

  <h2> Kommentare: <h2>

  <#list kommentarListe as kommentar>

      <h4> ${kommentar.kommentator}: </h4>
      <p> ${kommentar.text} </p>

      <br>

     </#list>

     <br> <br>

    <form name="anzeige" action="" method="post">

        <h3>Schreibe einen Kommentar: </h3>
        <input type="textarea" name="kommentar" style="width:400px;height:100px;"/> <br> <br>

        <input type="submit" value="Kommentar abschicken" />

        </form>



</body>
</html>
