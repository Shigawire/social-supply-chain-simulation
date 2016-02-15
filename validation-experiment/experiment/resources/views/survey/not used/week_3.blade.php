
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
        <h1>Woche {{ $current_step }}</h1>

        {!! Form::model($survey) !!}
        <div class="form-group">
            <p>Bitte wählen Sie einen Lieferanten aus und wieviel Sie bestellen möchten</p>
            <br>
            {!! Form::label('supplier_step3', 'Bitte einen Händler auswählen:') !!}

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
                                {!! Form::radio('supplier_step3', $supplier->id) !!}
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

            {!! Form::label('bestellmenge_step3', 'Bestellmenge:') !!}
            {!! Form::text('bestellmenge_step3', null, ['class' => 'form-control']) !!}

        </div>
        <button type="submit" class="btn btn-primary">Weiter</button>
        {!! Form::close() !!}
    </div>
    <div class="col-lg-4 info">
        <br>
        <table class="table table-striped table-bordered">
            <tr>
                <th>Kontostand</th>
                <td class="text-right">{{ $kontostand }} €</td>
            </tr>
            <tr>
                <th>Lagerstand</th>
                <td class="text-right"‚‚>{{ $lagerbestand }}</td>
            </tr>
        </table>

    </div>

    <div class="col-lg-8">
        <hr>
        <h4 class="heading">Letzte Woche bestellt:</h4>

        @if ($bestellte_menge > 0)
            <table class="table table-striped table-bordered">
                <tr>
                    <th>Lieferant</th>
                    <th>Preis</th>
                    <th>Lieferzeit</th>
                    <th>Bestellte Menge</th>
                    <th>Kosten</th>
                </tr>
                <tr>
                    <td>Lieferant {{ $previous_supplier->number }}</td>
                    <td>{{ $previous_supplier->preis }} €</td>
                    <td>{{ $previous_supplier->lieferzeit }} Woche</td>
                    <td class="text-right">{{ $bestellte_menge }}</td>
                    <td class="danger text-right">{{ $bestellte_menge }} x {{ $previous_supplier->preis }} € = {{ $bestellte_menge * $previous_supplier->preis }} €</td>
                </tr>
            </table>
        @else
            Letzte Woche wurde nicht bestellt.
        @endif

        <hr>
        <h4 class="heading">Eingetroffene Lieferungen:</h4>
        @if ($gesamte_bestellmenge > 0)
            <table class="table table-striped table-bordered">
                <tr>
                    <th>Aus Woche:</th>
                    <th>Preis</th>
                    <th>Lieferzeit</th>
                    <th>Menge</th>
                </tr>
                @foreach($lieferungen as $lieferung)
                    <tr>
                        <td>{{ $lieferung->bestellt_in_runde }}</td>
                        <td>{{ $lieferung->preis }} €</td>
                        <td>{{ $lieferung->lieferzeit }} Woche(n)</td>
                        <td class="text-right">{{ $lieferung->gelieferte_menge }}</td>
                    </tr>
                @endforeach
                <tr>
                    <th colspan="3">Gesamtmenge:</th>
                    <th class="text-right">{{ $gesamte_bestellmenge }}</th>
                </tr>
            </table>
        @else
            Diese Woche sind keine Lieferungen angekommen.
        @endif

        <hr>
        <h4 class="heading">Umsätze</h4>
        @if ($gesamtumsatz > 0)
            <table class="table table-striped table-bordered">
                <tr>
                    <th>Aus Woche</th>
                    <th>Nachgefragte Menge</th>
                    <th>Verkaufte Menge</th>
                    <th>Verkaufspreis</th>
                    <th>Umsatz</th>
                </tr>
                @foreach ($umsaetze as $umsatz)
                    <tr>
                        <td>{{ $umsatz->nachfrage_aus_runde }}</td>
                        <td class="text-right">{{ $umsatz->verbl_nachfrage }}</td>
                        <td class="text-right">{{ $umsatz->verkaufte_menge }}</td>
                        <td class="text-right">{{ $umsatz->verkaufspreis }} €</td>
                        <td class="text-right">{{ $umsatz->umsatz }} €</td>
                    </tr>
                @endforeach

                <tr>
                    <th colspan="4">Gesamtumsatz:</th>
                    <th class="success text-right">{{ $gesamtumsatz }} €</th>
                </tr>
            </table>
        @else
            Es sind keine Umsätze erzielt worden.
        @endif

        <hr>
        <h4 class="heading">Noch nicht erfüllte Nachfragen</h4>
        @if (count($back_orders) > 0)
            <table class="table table-striped table-bordered">
                <tr>
                    <th>Nachfrage aus Woche</th>
                    <th>Verbleibender Verkaufspreis</th>
                    <th>Verbleibende nachgefragte Menge</th>
                </tr>
                @foreach ($back_orders as $back_order)
                    <tr>
                        <td>{{ $back_order->nachgefragt_in_runde }}</td>
                        <td class="text-right">{{ $back_order->verkaufspreis }} €</td>
                        <td class="text-right">{{ $back_order->verbl_menge }}</td>
                    </tr>
                @endforeach

                <tr>
                    <th colspan="2">Gesamte nicht erfüllte Nachfrage:</th>
                    <th class="text-right">{{ $gesamt_back_order_menge }}</th>
                </tr>
            </table>
        @else
            Aktuell keine offenen Nachfragen
        @endif


        <hr>
        <h4 class="heading">Lagerbilanz:</h4>
        <table class="table table-striped table-bordered">
            <tr>
                <th>Bisheriger Lagerstand</th>
                <td class="text-right">{{ $bisheriger_lagerbestand }}</td>
            </tr>
            <tr>
                <th>Diese Woche erhalten</th>
                <td class="text-right">{{ $gesamte_bestellmenge }}</td>
            </tr>
            <tr>
                <th>Diese Woche verkauft</th>
                <td class="text-right">{{ $verkaufte_menge }}</td>
            </tr>
            <tr>
                <th>Neuer Lagerstand</th>
                <td class="text-right">{{ $neuer_lagerbestand }}</td>
            </tr>
        </table>

        <hr>
        <h4 class="heading">Kontoübersicht:</h4>
        <table class="table table-striped table-bordered">
            <tr>
                <th colspan="2">Bisheriger Kontostand</th>
                <td class="text-right">{{ $alter_kontostand }} €</td>
            </tr>
            <tr>
                <th>Ausgaben für Bestellungen</th>
                <td class="text-right">{{ $bestellte_menge }} x {{ $previous_supplier->preis }} €</td>
                <td class="text-right danger">{{ $bestellte_menge * $previous_supplier->preis }} €</td>
            </tr>
            <tr>
                <th>Lagerkosten</th>
                <td class="text-right">{{ $neuer_lagerbestand }} x 10 €</td>
                <td class="text-right danger">{{ $lagerkosten }} €</td>
            </tr>
            <tr>
                <th colspan="2">Umsätze durch Verkäufe</th>
                <td class="text-right success">{{ $gesamtumsatz }} €</td>
            </tr>
            <tr>
                <th colspan="2"><h4>Ergebnis</h4></th>

                <td class="text-right"><h4>{{ $kontostand }} €</h4></td>
            </tr>
        </table>

        <br/><br/><br/>
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
