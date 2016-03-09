
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
        <br>
        <div class="progress">
            <div class="progress-bar" role="progressbar" aria-valuenow="10" aria-valuemin="0" aria-valuemax="30" style="width: {{ (1 / 30) * 100 }}%;">
            </div>
        </div>
        <h1>Planung für Woche 1</h1>

        {!! Form::model($survey) !!}
        <div class="form-group">
            <p>Ein Kunde hat bei Ihnen für die nächste Woche 5 Produkteinheiten bestellt.</p>
            <p>Bitte wählen Sie einen Lieferanten aus und geben Sie die Menge an, die Sie bestellen möchten.</p>
            <p>Wenn Sie nichts bestellen wollen, wählen Sie trotzdem den gewünschten Lieferanten aus.</p>
            <br>
            {!! Form::label('gewaehlter_lieferant_week1', 'Bitte einen Lieferanten auswählen:') !!}

            <table class="table table-striped table-bordered table-hover">
                <tr>
                    <th></th>
                    <th>Lieferant</th>
                    <th>Preis</th>
                    <th>Lieferzeit</th>
                    <th>Allg. Vertrauenswürdigkeit</th>
                </tr>
                @foreach($suppliers as $supplier)
                    <label>
                        <tr>
                            <td>
                                {!! Form::radio('gewaehlter_lieferant_week1', $supplier->id) !!}
                            </td>
                            <td>
                                {{ $supplier->number }}
                            </td>
                            <td>
                                {{ $supplier->preis }} €
                            </td>
                            <td>
                                {{ $supplier->lieferzeit }} Wochen
                            </td>
                            <td>
                                {{ $supplier->vertrauen }}
                            </td>
                        </tr>
                    </label>
                @endforeach
            </table>

            <br>

            {!! Form::label('bestellmenge_week1', 'Bestellmenge:') !!}
            {!! Form::text('bestellmenge_week1', null, ['class' => 'form-control']) !!}



        </div>
        <button type="submit" class="btn btn-primary">Weiter</button>
        {!! Form::close() !!}
    </div>
    <div class="col-lg-4">
        <br>
        <table class="table table-striped table-bordered">
            <tr>
                <th>Ihr Kontostand</th>
                <td class="text-right">1000 €</td>
            </tr>
            <tr>
                <th>Ihr Lagerbestand</th>
                <td class="text-right">5</td>
            </tr>
        </table>
        <div class="info">
            <h5>Verkaufspreis eines Produkts</h5>
            <ul>
                <li>100 € abzüglich 4 € pro verspäteter Woche</li>
            </ul>
            <h5>Lagerkosten pro Produkt</h5>
            <ul>
                <li>10 € pro Woche</li>
            </ul>
            <h5>Kosten für nicht erfüllte Nachfrage pro Produkt</h5>
            <ul>
                <li>6 € pro Woche</li>
            </ul>
            <h5>Allg. Vertrauenswürdigkeit</h5>
            <ul>
                <li><u>niedrig:</u> Relativ hohe Wahrscheinlichkeit, dass Lieferung verspätet eintrifft und Anzahl weiterverkaufbarer Produkte geringer als bestellt.</li>
                <li><u>mittel:</u> Geringere Wahrscheinlichkeit, dass Lieferung verspätet eintrifft und Anzahl weiterverkaufbarer Produkte geringer als bestellt.</li>
                <li><u>hoch:</u> Sehr geringe Wahrscheinlichkeit, dass Lieferung verspätet eintrifft und Anzahl weiterverkaufbarer Produkte geringer als bestellt.</li>
            </ul>
        </div>


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
