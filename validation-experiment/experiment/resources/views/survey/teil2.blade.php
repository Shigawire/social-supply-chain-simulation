
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

        <h1>Teil 2</h1>
        <br>
        <p>
            Im nun folgenden zweiten Teil des Experiments verändern sich die Profile der Zulieferer dahingehend, dass
            der Preis und die Lieferzeit für jeden Händler gleich hoch sind und der Vertrauenswert in vier Dimensionen
            aufgeteilt wird. Diese vier Dimensionen sind <i>Quality</i>, <i>Reliability</i>, <i>Competence</i> und <i>Shared Values</i>
        </p>
        <p>
            Ihre bisherigen Bestellungen, Einnahmen, Produkte auf Lager etc. aus der ersten Runde bleiben dabei bestehen.
        </p>
        <p>
            <i>Quality</i> gibt die Wahrscheinlichkeit an, dass 100 % der gesendeten Produkte weiterverkauft werden können.
        </p>
        <p>
            <i>Reliability</i> gibt die Wahrscheinlichkeit an, dass die bestellte menge pünktlich geliefert wird.
        </p>
        <p>
            <i>Competence</i> gibt eine Prozentzahl an, die beschreibt, wie viele der letzten Bestellungen allgemein positiv
            bewertet worden sind.
        </p>
        <p>
            <i>Shared Values</i> beschreibt die Ähnlichkeit des Wertesystems des Händlers (Als Beispiel wäre Einstellung
            zu Ökoglogischer Nachhaltigkeit oder zur Bewertung von Pünktlichkeit zu nennen).
        </p>
        <p>
            Aus Gründen der Einfachheit wird dabei auf prozentuale Werte verzichtet und die die Ausprägung stattdessen
            durch "niedrig", "mittel" oder "hoch" beschrieben.
        </p>

        <a href="{{ url('/week/X2') }}"><button type="submit" class="btn btn-primary">Weiter</button></a>
    </div>
</div><!-- /.container -->


<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
{!! HTML::script('jquery/jquery.js') !!}
<script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>')</script>
{!! HTML::script('bootstrap/js/bootstrap.min.js') !!}
</body>
</html>
