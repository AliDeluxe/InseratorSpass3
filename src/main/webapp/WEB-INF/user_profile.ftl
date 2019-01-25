<html>
<head>
<title>User Profile</title>
<style>
span.a {
  display: inline-block;
  vertical-align: top;
  padding: 10px 10px;
}
</style>
</head>
 
<body>
    <div id="header">
    		<h1>${username}</h1>
    </div>

    <p> <h7><b>Name: </b>${name} </h7> <br>
        <h7><b>Erstellt am: </b>${datum} </h7> <br>
        <h7><b>${verkauft}</b> verkaufte(r) Artikel</h7></p>

    <span class="a">
    <h3> Meine Anzeigen: </h3>
    <#list anzeigeListeAngeboten as anzeige>

        <fieldset style="width:270px">
        <legend> <h4> ${anzeige.titel} </h4> </legend>
        <label>Preis: ${anzeige.preis} € <br /> </label>
        <label>Erstellt am: ${anzeige.erstellungsDatum} <br />
        <label> ${anzeige.status} <br />

        </fieldset>
        <br>

       </#list>
       </span>


    <span class="a">
    <h3> Gekaufte Anzeigen: </h3>
    <#list anzeigeListeGekauft as anzeige>

            <fieldset style="width:270px">
            <legend> <h4> ${anzeige.titel} </h4> </legend>
            <label>Preis: ${anzeige.preis} € <br /> </label>
            <label>Erstellt am: ${anzeige.erstellungsDatum} <br />
            <label> ${anzeige.status} <br />

            </fieldset>
            <br>

           </#list>
        </span>

</body>
</html>
