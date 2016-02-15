
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
            <div class="progress-bar" role="progressbar" aria-valuenow="10" aria-valuemin="0" aria-valuemax="30" style="width: {{ ($current_step / 30) * 100 }}%;">
            </div>
        </div>
        <h1>Bericht aus Woche {{ $current_step - 1 }}</h1>
        <br>
        <h4 class="heading">Vor Woche {{ $current_step - 1 }} bestellt:</h4>

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
                <td>{{ $previous_supplier->lieferzeit }} Wochen</td>
                <td class="text-right">{{ $bestellte_menge }}</td>
                <td class="danger text-right">{{ $bestellte_menge }} x {{ $previous_supplier->preis }} € = {{ $bestellte_menge * $previous_supplier->preis }} €</td>
            </tr>
        </table>
        @else
            Letzte Woche wurde nicht bestellt.
        @endif

        <hr>
        <h4 class="heading">Eingetroffene Lieferungen:</h4>
        @if ($gesamte_gelieferte_menge > 0)
            <table class="table table-striped table-bordered">
                <tr>
                    <th>Bestellt vor Woche:</th>
                    <th>Preis</th>
                    <th>Lieferzeit laut Lieferant</th>
                    <th>Tatsächliche Lieferzeit</th>
                    <th>Menge</th>
                </tr>
                @foreach($lieferungen as $lieferung)
                <tr>
                    <td>{{ $lieferung->bestellt_in_runde }}</td>
                    <td>{{ $lieferung->preis }} €</td>
                    <td>{{ $lieferung->lieferzeit_laut_supplier }} Woche(n)</td>
                    <td>{{ $lieferung->lieferzeit }} Woche(n)</td>
                    <td class="text-right">{{ $lieferung->gelieferte_menge }}</td>
                </tr>
                @endforeach
                <tr>
                    <th colspan="4">Gesamtmenge:</th>
                    <th class="text-right">{{ $gesamte_gelieferte_menge }}</th>
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
                    <th class="text-right">{{ $gesamte_back_order_menge }}</th>
                </tr>
            </table>
        @else
            Aktuell keine offenen Nachfragen
        @endif


        <hr>
        <h4 class="heading">Lagerbilanz:</h4>
        <table class="table table-striped table-bordered">
            <tr>
                <th>Lagerbestand vor Woche {{ $current_step - 1 }}</th>
                <td class="text-right">{{ $bisheriger_lagerbestand }}</td>
            </tr>
            <tr>
                <th>In Woche {{ $current_step - 1 }} erhalten</th>
                <td class="text-right">{{ $gesamte_gelieferte_menge }}</td>
            </tr>
            <tr>
                <th>In Woche {{ $current_step - 1 }} verkauft</th>
                <td class="text-right">{{ $verkaufte_menge }}</td>
            </tr>
            <tr>
                <th>Lagerbestand am Ende von Woche {{ $current_step - 1 }}</th>
                <td class="text-right">{{ $neuer_lagerbestand }}</td>
            </tr>
        </table>

        <hr>
        <h4 class="heading">Kontoübersicht:</h4>
        <table class="table table-striped table-bordered">
            <tr>
                <th colspan="2">Kontostand vor Woche {{ $current_step - 1 }}</th>
                <td class="text-right">{{ $bisheriger_kontostand }} €</td>
            </tr>
            <tr>
                <th>Bestellkosten der letzten Woche</th>
                <td class="text-right">{{ $bestellte_menge }} x {{ $previous_supplier->preis }} €</td>
                <td class="text-right danger">{{ $bestellte_menge * $previous_supplier->preis }} €</td>
            </tr>
            <tr>
                <th>Lagerkosten in Woche {{ $current_step - 1 }}</th>
                <td class="text-right">{{ $neuer_lagerbestand }} x 10 €</td>
                <td class="text-right danger">{{ $lagerkosten }} €</td>
            </tr>
            <tr>
                <th>Kosten für nicht erfüllte Nachfragen</th>
                <td class="text-right">{{ $gesamte_back_order_menge }} x 6 €</td>
                <td class="text-right danger">{{ $back_order_kosten }} €</td>
            </tr>
            <tr>
                <th colspan="2">Umsätze durch Verkäufe in Woche {{ $current_step - 1 }}</th>
                <td class="text-right success">{{ $gesamtumsatz }} €</td>
            </tr>
            <tr>
                <th colspan="2"><h4>Kontostand am Ende von Woche {{ $current_step - 1 }}</h4></th>

                <td class="text-right"><h4>{{ $neuer_kontostand }} €</h4></td>
            </tr>
        </table>


    </div>

    <div class="col-lg-4">
        <br>
        <table class="table table-striped table-bordered">
            <tr>
                <th>Ihr Kontostand</th>
                <td class="text-right">{{ $neuer_kontostand }} €</td>
            </tr>
            <tr>
                <th>Ihr Lagerbestand</th>
                <td class="text-right">{{ $neuer_lagerbestand }}</td>
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
    <div class="col-lg-8">
        <h1>Planung für Woche {{ $current_step }}</h1>

        {!! Form::model($survey) !!}
        <div class="form-group">

            <p>Ein Kunde hat bei Ihnen für die nächste Woche 5 Produkteinheiten bestellt.</p>
            <p>Bitte wählen Sie einen Lieferanten aus und geben Sie die Menge an, die Sie bestellen möchten.</p>
            <p>Wenn Sie nichts bestellen wollen, wählen Sie trotzdem den gewünschten Lieferanten aus.</p>
            <br>
            {!! Form::label('supplier_step'.$current_step, 'Bitte einen Lieferanten auswählen:') !!}

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
                                {!! Form::radio('supplier_step'.$current_step, $supplier->id) !!}
                            </td>
                            <td>
                                {{ $supplier->number }}
                            </td>
                            <td>
                                {{ $supplier->preis }} €
                            </td>
                            <td>
                                {{ $supplier->angegebene_lieferzeit }} Wochen
                            </td>
                            <td>
                                {{ $supplier->vertrauen }}
                            </td>
                        </tr>
                    </label>
                @endforeach
            </table>

            <br>

            {!! Form::label('bestellmenge_step'.$current_step, 'Bestellmenge:') !!}
            {!! Form::text('bestellmenge_step'.$current_step, null, ['class' => 'form-control']) !!}

        </div>
        <button type="submit" class="btn btn-primary">Weiter</button>
        {!! Form::close() !!}
        <br/><br/>
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
