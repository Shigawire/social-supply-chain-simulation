
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
        <h1>Bewertung der Lieferanten</h1>
        <p>
            Diese Woche sind {{ count($lieferungen) }} Lieferungen angekommen. Bitte bewerten Sie
            die entsprechenden Lieferanten. Bewerten Sie die folgenden Dimensionen (siehe rechter
            Rand) anhand der tatsächlichen Lieferzeit und der Qualität.
        </p>
        <br>
        {!! Form::model($survey) !!}

        @foreach($lieferungen as $lieferung)

            <h4 class="heading">Bestellung aus Woche {{ $lieferung->bestellt_in_runde }}</h4>

                <table class="table table-striped table-bordered">
                    <tr>
                        <th>Bestellt in Woche</th>
                        <th>Lieferzeit laut Lieferant</th>
                        <th>Tatsächliche Lieferzeit</th>
                        <th>Bestellte Menge</th>
                        <th>Gelieferte Menge</th>
                        <th>Fehlerrate</th>
                    </tr>
                    <tr>
                        <td>{{ $lieferung->bestellt_in_runde }}</td>
                        <td>{{ $lieferung->lieferzeit_laut_supplier }} Woche(n)</td>
                        <td>{{ $lieferung->lieferzeit }} Woche(n)</td>
                        <td>{{ $lieferung->bestellmenge }}</td>
                        <td>{{ $lieferung->gelieferte_menge }}</td>
                        <td>{{ $lieferung->quality }} %</td>
                    </tr>
                </table>
                <p>Bewerten Sie den Lieferanten:</p>
                <table class="table table-striped table-bordered">
                    <tr>
                        <th></th>
                        <th>Reliability</th>
                        <th>Quality</th>
                        <th>Competence</th>
                    </tr>
                    <tr>
                        <td>Bisheriger Wert</td>
                        <td>{{ $lieferung->reliability_bewertung }} %</td>
                        <td>{{ $lieferung->quality_bewertung }} %</td>
                        <td>{{ $lieferung->competence_bewertung }} %</td>
                    </tr>
                    <tr>
                        <td>Neuer Wert</td>
                        <td>{!! Form::text('reliability_bewertung'.$lieferung->id, null, ['class' => 'form-control']) !!}</td>
                        <td>{!! Form::text('quality_bewertung'.$lieferung->id, null, ['class' => 'form-control']) !!}</td>
                        <td>{!! Form::text('competence_bewertung'.$lieferung->id, null, ['class' => 'form-control']) !!}</td>
                    </tr>
                </table>
            <hr>
        @endforeach

        <button type="submit" class="btn btn-primary">Weiter</button>
        {!! Form::close() !!}

    </div>
    <div class="col-lg-4">
        <br>
        <div class="info">

            <h5>Trust Dimensionen</h5>
            <ul>
                <li><u>Reliability:</u>
                    Ihre Zufriedenheit bezüglich der Pünktlichkeit der Lieferung.
                    Je höher, desto zufriedener sind Sie mit dem Zeitpunkt der Lieferung.
                </li>
                <li><u>Quality:</u>
                    Ihre Zufriedenheit bezüglich der Vollständigkeit der
                    erhaltenen Lieferung. Je höher, desto zufriedener sind Sie mit der
                    Anzahl an weiterverkaufbaren Produkten.
                </li>
                <li><u>Competence:</u>
                    Ihre Zufriedenheit bezüglich der Bestellung allgemein. Je höher,
                    desto zufriedener sind Sie mit dem Lieferanten und seiner letzten
                    Lieferung.
                </li>
            </ul>
        </div>

    </div>


</div><!-- /.container -->
<br><br>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
{!! HTML::script('jquery/jquery.js') !!}
<script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>')</script>
{!! HTML::script('bootstrap/js/bootstrap.min.js') !!}
</body>
</html>
