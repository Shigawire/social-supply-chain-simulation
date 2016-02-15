
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>Experiment "Social Simulation in Supply Chains"</title>

    <!-- Bootstrap core CSS -->
    {!! HTML::style('bootstrap/css/bootstrap.min.css') !!}

            <!-- Custom styles for this template -->
    <link href="startseite.css" rel="stylesheet">
    {!! HTML::style('css/startseite.css') !!}

</head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Experiment "Social Simulation in Supply Chains"</a>
        </div>

        <ul class="nav navbar-nav navbar-right">
            <li><a href="{{ url('logout') }}">Logout</a></li>
        </ul>
    </div>
</nav>

<div class="container">
    <div class="col-lg-8">

            @include('errors.list')

        <h1>Willkommen zum Experiment!</h1>
        <br>
        <p>
            Allgemein wird bei der ökonomischen Entscheidungsfindung <i>Vertrauen</i> teils bewusst aber ebenso
            unbewusst genutzt. Um dieses Verhalten analysieren zu können, wurde folgendes interaktives
            Experiment entwickelt, bei dem es sich um ein fiktives Szenario einer Supply Chain handelt.
        </p>
        <p>
            Sie sind in der Rolle eines Händlers und erhalten pro Runde jeweils einen Auftrag durch einen Kunden.
            Um diesen zu erfüllen, haben Sie jeweils verschiedene Zulieferer zur Auswahl und können dabei
            entscheiden, bei wem und wie viele Produkte Sie bestellen. Im nun folgenden ersten Teil des Experiments
            hat jeder dieser Zulieferer zur Entscheidungsfindung ein eigenes Profil bestehend aus einem Preis pro
            Produkt, einer Lieferzeit sowie einem fiktiven Wert, der die Vertrauenswürdigkeit beschreibt (vergleichbar
            mit Kundenzufriedenheit bezüglich Lieferanten bei Amazon).
        </p>
        <p>
            Der Kunde zahlt Ihnen dabei pro Produkt 100 Euro, wenn Sie direkt liefern können. Für jede spätere
            Woche fällt dieser Preis um 4 Euro und Sie zahlen eine Strafe von 6 Euro für jedes nicht gelieferte
            Produkt. Ebenfalls haben Sie Lagerkosten von 10 Euro pro Produkt und Woche. <br>
            Beispiel: Erhalten Sie in Runde 1 eine Bestellung von 1 Produkt zu 100 Euro, zahlt der Kunde in Runde
            3 nur noch 92 Euro. Gleichzeitig müssen Sie 12 Euro Strafkosten zahlen (6 je Runde).
        </p>
        <p>
            Ihr Ziel ist es, einen möglichst hohen Gewinn zu erwirtschaften.
        </p>
        <p>
            Nach jeder ankommenden Bestellung sollen sie ebenfalls den Händler nach verschiedenen Dimensionen
            der Vertrauenswürdigkeit bewerten, die dort noch genauer erläutert werden.
        </p>
        <p>
            Sie starten mit einem anfänglichen Kontostand von 1000 Euro und einem Lagerbestand von 10.
        </p>
        <p>
            Das Experiment besteht aus 30 - 40 Runden.
        </p>
        <p>
            Bitte benutzen Sie nicht den "Zurück"-Button Ihres Browsers. Ansonsten müssten Sie von vorne anfangen.
        </p>

        <hr>
        <p>Bitte geben Sie einen beliebigen Namen ein. Dieser muss nicht mit Ihrem realen Name übereinstimmen.</p>
        {!! Form::open() !!}
        <div class="form-group">
            {!! Form::label('name', 'Name:') !!}
            {!! Form::text('name', null, ['class' => 'form-control']) !!}
        </div>
        <button type="submit" class="btn btn-primary">Beginnen</button>
        {!! Form::close() !!}
    </div>
</div><!-- /.container -->
<br>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
{!! HTML::script('jquery/jquery.js') !!}
<script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>')</script>
{!! HTML::script('bootstrap/js/bootstrap.min.js') !!}
</body>
</html>
