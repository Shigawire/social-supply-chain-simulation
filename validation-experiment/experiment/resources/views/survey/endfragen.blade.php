
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
        <h1>Fast geschafft!</h1>
        <p>Beantworten Sie bitte noch folgende Fragen.</p>
        {!! Form::model($survey) !!}
            <h2>Frage 1</h2>
            <p>
                Ab welchem Vertrauenswert würden Sie folgende Informationen mit anderen Lieferanten
                teilen? (0% = keine Vertrauensbasis, 100% = maximales Vertrauen)
                (Bsp.: Ab einem mittleren Vertrauenverhältnis (50%) würde ich dem Lieferanten mitteilen,
                ob ich bei ihm bestelle. An einem hohen (80%) nenne ich ihm die Bestellmenge zusätzlich
                eine Runde vorher)
            </p>
            <table class="table table-striped table-bordered">
                <tr>
                    <th>(a) Bestellung eine Woche bevor eigentlichem Zeitpunkt</th>
                    <td>{!! Form::text('1a', null, ['class' => 'form-control']) !!}</td>
                </tr>
                <tr>
                    <th>(b) Information, ob generell bei diesem Lieferanten in der nächsten Woche bestellt wird.</th>
                    <td>{!! Form::text('1b', null, ['class' => 'form-control']) !!}</td>
                </tr>
            </table>
            <h2>Frage 2</h2>
            <p>
                Angenommen, Sie erhalten die durchschnittliche Vertrauensbewertung ihrer "Mitspieler" bezüglich
                der Lieferanten (vgl. Bewertungen anderer Personen bei Amazon). Wie würden Sie diese Bewertungen
                im Vergleich zu Ihren eigenen gemachten Erfahrungen zur Auswahl dieser Lieferanten nutzen? Geben
                Sie dazu bitte prozentual ihre Gewichtung an. (Bsp.: Ich vertraue zu 90% auf meine eigenen
                Erfahrungen und nur zu 10% den Bewertungen anderer)
            </p>
            <table class="table table-striped table-bordered">
                <tr>
                    <th>(a) Prozentzahl eigene Erfahrung</th>
                    <td>{!! Form::text('2a', null, ['class' => 'form-control']) !!}</td>
                </tr>
                <tr>
                    <th>(b) Prozentzahl Erfahrung Anderer</th>
                    <td>{!! Form::text('2b', null, ['class' => 'form-control']) !!}</td>
                </tr>
            </table>
        <h2>Frage 3</h2>
        <p>
            Wenn Sie das Experiment Revue passieren lassen, welche Eigenschaft der Lieferanten war
            Ihnen am wichtigsten?
        </p>
        <table class="table table-striped table-bordered">
            <tr>
                <td>{!! Form::radio('3', 1) !!}</td>
                <td>Preis</td>
            </tr>
            <tr>
                <td>{!! Form::radio('3', 2) !!}</td>
                <td>Lieferzeit</td>
            </tr>
            <tr>
                <td>{!! Form::radio('3', 3) !!}</td>
                <td>Vertrauenswürdigkeit</td>
            </tr>
            <tr>
                <td>{!! Form::radio('3', 4) !!}</td>
                <td>Ausgeglichene Relevanz</td>
            </tr>
        </table>
        <h2>Frage 4</h2>
        <p>
            Wenn Sie das Experiment Revue passieren lassen, welche Dimensionen des
            Vertrauenswertes waren Ihnen am wichtigsten?
        </p>
        <table class="table table-striped table-bordered">
            <tr>
                <td>{!! Form::radio('4', 1) !!}</td>
                <td>Reliability</td>
            </tr>
            <tr>
                <td>{!! Form::radio('4', 2) !!}</td>
                <td>Quality</td>
            </tr>
            <tr>
                <td>{!! Form::radio('4', 3) !!}</td>
                <td>Shared Values</td>
            </tr>
            <tr>
                <td>{!! Form::radio('4', 4) !!}</td>
                <td>Competence</td>
            </tr>
            <tr>
                <td>{!! Form::radio('4', 5) !!}</td>
                <td>Ausgeglichene Relevanz</td>
            </tr>
        </table>

            <button type="submit" class="btn btn-primary">Weiter</button>
        {!! Form::close() !!}
    </div>

    <br><br>

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
