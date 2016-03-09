<?php

namespace App\Http\Controllers;

use App\Back_Order;
use Illuminate\Http\Request;

use App\Http\Requests;
use App\Http\Controllers\Controller;
use App\Survey;
use App\Question;
use Auth;
use App\Supplier;
use App\Lieferung;
use App\Umsatz;
use Illuminate\Support\Facades\Redirect;
use App\Helpers\General;


/*
 * This controller contains all
 * post request of the survey
 */
class PostSurveyController extends Controller
{
    /*
     * Protect all functionality of this
     * controller against users that are
     * not logged in
     */
    public function __construct()
    {
        $this->middleware('auth');
    }

    /*
     * Process the beginning of the survey and
     * save the user's entered name
     */
    public function postSurvey(Request $request)
    {
        // Validation for user input
        $this->validate($request, [
            'name' => 'required|min:2|max:16|unique:surveys,name'
        ], [
            'name.required' => 'Bitte einen Namen angeben',
            'name.min' => 'Der Name muss mindestens 2 Zeichen lang sein',
            'name.max' => 'Der Name darf nicht länger als 16 Zeichen sein',
            'name.unique' => 'Der Name ist bereits vergeben'
        ]);

        // Store initial data to database using the Survey Model
        $survey = Survey::Create([
            'name' => $request->input('name'),
            'current_step' => 1,
            'kontostand_week0' => 1000,
            'lagerbestand_week0' => 5,
        ]);

        $request->session()->put('survey', $survey);

        // Redirect to the first Week 1 of the experiment
        return redirect()->action('GetSurveyController@getWeek1');
    }

    /*
     * The first week has (still) its own controller
     * because it is different to upcoming weeks
     * - no back orders
     * - no shipments coming in
     */
    public function processWeek1(Request $request)
    {
        // 1. Get all relevant data
        $user_session = $request->session()->get('survey');
        $bestellmenge_week1 = $request->input('bestellmenge_week1');
        // Get all Suppliers for the current week from the database
        $selected_supplier = Supplier::where('id', '=', $request->input('gewaehlter_lieferant_week1'))->first();
        $bisheriger_lagerbestand = $user_session->lagerbestand_week0;

        // 2. Validation
        $this->validate($request, [
            'gewaehlter_lieferant_week1' => 'required',
            'bestellmenge_week1' => 'required|integer|min:0',
        ], [
            'gewaehlter_lieferant_week1.required' => 'Bitte Lieferant auswählen',
            'bestellmenge_week1.required' => 'Bitte Bestellmenge angeben',
        ]);

        // 3. Create shipment if something has been ordered
        if ($bestellmenge_week1 > 0)
        {
            // Create shipment with helper class
            General::create_lieferung($selected_supplier, $user_session, $bestellmenge_week1, $request);
        }

        // 4. Calculate order cost
        $bestellkosten = $bestellmenge_week1 * $selected_supplier->preis;

        // 5. Check, whether shipments of previous orders have arrived
        $erhaltene_menge = General::check_lieferungen($user_session->id);

        // 6. Refresh inventory level
        $neuer_lagerbestand = $bisheriger_lagerbestand + $erhaltene_menge;

        // 7. Try to fulfill demand
        $back_order_menge = 0;
        $verkaufte_menge = 0;
        $umsatz = 0;
        // fulfill demand using helper class method
        $results = General::fulfill_demand($neuer_lagerbestand, $user_session);
        $verkaufte_menge += $results[0];
        $neuer_lagerbestand = $results[1];
        $umsatz += $results[2];
        $back_order_menge = $results[3];

        // 8. Calculate holding costs
        $lagerkosten = $neuer_lagerbestand * 10;

        // 9. Calculate earnings
        $ergebnis = $umsatz - $bestellkosten;
        $bisheriger_kontostand = $user_session->kontostand_week0;
        $neuer_kontostand = $bisheriger_kontostand + $ergebnis;

        // 10. Refresh data of the user for the database
        $request->session()->get('survey')->update([
            'current_step' => 2,
            'kontostand_week1' => $neuer_kontostand,
            'bestellkosten_week1' => $bestellkosten,
            'umsatz_week1' => $umsatz,
            'lagerkosten_week1' => $lagerkosten,
            'lagerbestand_week1' => $neuer_lagerbestand,
            'bestellmenge_week1' => $bestellmenge_week1,
            'gewaehlter_lieferant_week1' => $selected_supplier->id,
            'erhaltene_menge_week1' => $erhaltene_menge,
            'verkaufte_menge_week1' => $verkaufte_menge,
            'back_order_menge_week1' => $back_order_menge,
            'back_order_kosten_week1' => 0,
        ]);

        // 11. Refresh remaining delivery time of shipments
        General::lieferzeit_verringern($user_session);

        // 12. Refresh remaining back orders (they lose value=)
        General::back_orders($user_session);

        // 13. Redirect to the next week
        return redirect()->action('GetSurveyController@getWeekX');
    }

    /*
     * This method processes week 2 to week 17
     */
    public function processWeekX(Request $request)
    {
        // 1. Get all relevant data
        $user_session = $request->session()->get('survey');
        $current_step = $user_session->current_step;
        $previous_step = $current_step - 1;
        $pre_previous_step = $current_step - 2;

        $bestellmenge = $request->input('bestellmenge_step'.$current_step);
        $selected_supplier = Supplier::where('id', '=', $request->input('supplier_step'.$current_step))->first();
        $lagerbestand_weekX = 'lagerbestand_week'.$previous_step;
        $lagerbestand = $user_session->$lagerbestand_weekX;
        $back_order_menge_weekX = 'back_order_menge_week'.$previous_step;
        $back_order_menge = $user_session->$back_order_menge_weekX;

        // 2. Validation
        $this->validate($request, [
            'supplier_step'.$current_step => 'required',
            'bestellmenge_step'.$current_step => 'required|integer|min:0',
        ], [
            'supplier_step'.$current_step.'.required' => 'Bitte Lieferant auswählen',
            'bestellmenge_step'.$current_step.'.required'=> 'Bitte Bestellmenge angeben',
            'bestellmenge_step'.$current_step.'.integer'=> 'Bestellmenge muss ganzzahlig sein',
            'bestellmenge_step'.$current_step.'.min'=> 'Bestellmenge darf nicht negativ sein',
        ]);

        // 3. Lieferung erstellen
        if ($bestellmenge > 0)
        {
            General::create_lieferung($selected_supplier, $user_session, $bestellmenge, $request);
        }

        // 4. Bestellkosten berechnen
        $bestellkosten = $bestellmenge * $selected_supplier->preis;

        // 5. Prüfen, ob Lieferungen angekommen sind
        $erhaltene_menge = General::check_lieferungen($user_session->id);

        // 6. Lagerstand aktualieren
        $lagerbestand += $erhaltene_menge;

        // 7. Versuchen, back order zu erfüllen
        $verkaufte_menge = 0;
        $results = General::fulfill_back_orders($user_session, $lagerbestand);
        $verkaufte_menge += $results[0];
        $lagerbestand = $results[1];
        $umsatz = $results[2];
        if ($back_order_menge - $verkaufte_menge >= 0)
        {
            $back_order_menge -= $verkaufte_menge;
        }
        else
        {
            $back_order_menge = 0;
        }


        // 8. Versuchen neue Nachfrage zu erfüllen
        $results2 = General::fulfill_demand($lagerbestand, $user_session);
        $verkaufte_menge += $results2[0];
        $lagerbestand = $results2[1];
        $umsatz += $results2[2];
        $back_order_menge += $results2[3];

        // 9. Lagerkosten berechnen
        $lagerkosten = $lagerbestand * 10;

        $back_order_kosten = $back_order_menge * 6;

        // 10. Ergebnis berechnen
        $ergebnis = $umsatz - $bestellkosten - $lagerkosten - $back_order_kosten;
        $kontostand_weekX = 'kontostand_week'.$previous_step;
        $neuer_kontostand = $user_session->$kontostand_weekX;
        $neuer_kontostand += $ergebnis;

        // 11. Daten des Teilnehmers aktualisieren
        General::update_survey($request, $user_session,
                               $neuer_kontostand, $bestellkosten,
                               $umsatz, $lagerkosten,
                               $lagerbestand, $bestellmenge,
                               $selected_supplier, $erhaltene_menge,
                               $verkaufte_menge, $back_order_menge, $back_order_kosten);

        // 12. Verbleibende Zeiten aller Lieferungen aktualisieren
        General::lieferzeit_verringern($user_session);

        // 13. Back_orders aktualisieren (verlieren an Wert)
        General::back_orders($user_session);

        // 14. Wenn Lieferung ankommen wird, dann Bewertungsseite anzeigen,
        //     ansonsten zur nächsten Woche weiterleiten
        $lieferungen = Lieferung::where('besteller', '=', $user_session->id)->get();
        foreach ($lieferungen as $lieferung)
        {
            if ($lieferung->verbl_lieferzeit == 0 && $lieferung->bestellmenge > 0)
            {
                return redirect()->action('GetSurveyController@getBewertung');
            }
        }

        if ($current_step == 18)
        {
            return redirect()->action('GetSurveyController@getWeek18');
        }

        return redirect()->action('GetSurveyController@getWeekX');
    }

    /*
     * This method processes week 19 to week 30
     */
    public function processWeekX2(Request $request)
    {
        // 1. Get all relevant data
        $user_session = $request->session()->get('survey');
        $current_step = $user_session->current_step;
        $previous_step = $current_step - 1;
        $pre_previous_step = $current_step - 2;

        $bestellmenge = $request->input('bestellmenge_step'.$current_step);
        $selected_supplier = Supplier::where('id', '=', $request->input('supplier_step'.$current_step))->first();
        $lagerbestand_weekX = 'lagerbestand_week'.$previous_step;
        $lagerbestand = $user_session->$lagerbestand_weekX;
        $back_order_menge_weekX = 'back_order_menge_week'.$previous_step;
        $back_order_menge = $user_session->$back_order_menge_weekX;

        // 2. Validation
        $this->validate($request, [
            'supplier_step'.$current_step => 'required',
            'bestellmenge_step'.$current_step => 'required|integer|min:0',
        ], [
            'supplier_step'.$current_step.'.required' => 'Bitte Lieferant auswählen',
            'bestellmenge_step'.$current_step.'.required'=> 'Bitte Bestellmenge angeben',
            'bestellmenge_step'.$current_step.'.integer'=> 'Bestellmenge muss ganzzahlig sein',
            'bestellmenge_step'.$current_step.'.min'=> 'Bestellmenge darf nicht negativ sein',
        ]);

        // 3. Lieferung erstellen
        if ($bestellmenge > 0)
        {
            General::create_lieferung($selected_supplier, $user_session, $bestellmenge, $request);
        }

        // 4. Bestellkosten berechnen
        $bestellkosten = $bestellmenge * $selected_supplier->preis;

        // 5. Prüfen, ob Lieferungen angekommen sind
        $erhaltene_menge = General::check_lieferungen($user_session->id);

        // 6. Lagerstand aktualieren
        $lagerbestand += $erhaltene_menge;

        // 7. Versuchen, back order zu erfüllen
        $verkaufte_menge = 0;
        $results = General::fulfill_back_orders($user_session, $lagerbestand);
        $verkaufte_menge += $results[0];
        $lagerbestand = $results[1];
        $umsatz = $results[2];
        if ($back_order_menge - $verkaufte_menge >= 0)
        {
            $back_order_menge -= $verkaufte_menge;
        }
        else
        {
            $back_order_menge = 0;
        }

        // 8. Versuchen neue Nachfrage zu erfüllen
        $results2 = General::fulfill_demand($lagerbestand, $user_session);
        $verkaufte_menge += $results2[0];
        $lagerbestand = $results2[1];
        $umsatz += $results2[2];
        $back_order_menge += $results2[3];

        // 9. Lagerkosten berechnen
        $lagerkosten = $lagerbestand * 10;

        $back_order_kosten = $back_order_menge * 6;

        // 10. Ergebnis berechnen
        $ergebnis = $umsatz - $bestellkosten - $lagerkosten - $back_order_kosten;
        $kontostand_weekX = 'kontostand_week'.$previous_step;
        $neuer_kontostand = $user_session->$kontostand_weekX;
        $neuer_kontostand += $ergebnis;

        // 11. Daten des Teilnehmers aktualisieren
        General::update_survey($request, $user_session,
            $neuer_kontostand, $bestellkosten,
            $umsatz, $lagerkosten,
            $lagerbestand, $bestellmenge,
            $selected_supplier, $erhaltene_menge,
            $verkaufte_menge, $back_order_menge, $back_order_kosten);

        // 12. Verbleibende Zeiten aller Lieferungen aktualisieren
        General::lieferzeit_verringern($user_session);

        // 13. Back_orders aktualisieren (verlieren an Wert)
        General::back_orders($user_session);

        // 14. Wenn Lieferung ankommen wird, dann Bewertungsseite anzeigen,
        //     ansonsten zur nächsten Woche weiterleiten
        $lieferungen = Lieferung::where('besteller', '=', $user_session->id)->get();
        foreach ($lieferungen as $lieferung)
        {
            if ($lieferung->verbl_lieferzeit == 0 && $lieferung->bestellmenge > 0)
            {
                return redirect()->action('GetSurveyController@getBewertung');
            }
        }

        if ($current_step == 30)
        {
            return redirect()->action('GetSurveyController@getEndBericht');
        }

        return redirect()->action('GetSurveyController@getWeekX2');
    }

    /*
     * Process evaluations of shipments
     */
    public function postBewertung(Request $request)
    {
        $user_session = $request->session()->get('survey');
        $current_step = $user_session->current_step;
        $lieferungen = Lieferung::where('besteller', '=', $user_session->id)->where('verbl_lieferzeit', '=', 0)->where('gelieferte_menge', '>', 0)->get();

        // validate all shipments that have arrived
        foreach($lieferungen as $lieferung)
        {
            $this->validate($request, [
                'quality_bewertung'.$lieferung->id => 'required|min:0|max:100|integer',
                'reliability_bewertung'.$lieferung->id => 'required|min:0|max:100|integer',
                'competence_bewertung'.$lieferung->id => 'required|min:0|max:100|integer',
            ], [
                'quality_bewertung'.$lieferung->id.'.required' => 'Bitte die Felder zur Quality ausfüllen',
                'reliability_bewertung'.$lieferung->id.'.required' => 'Bitte die Felder zur Reliability ausfüllen',
                'competence_bewertung'.$lieferung->id.'.required' => 'Bitte die Felder zur Competence ausfüllen',
                'quality_bewertung'.$lieferung->id.'.min' => 'Der Wert zu Quality muss positiv sein',
                'reliability_bewertung'.$lieferung->id.'.min' => 'Der Wert zu Reliability muss positiv sein',
                'competence_bewertung'.$lieferung->id.'.min' => 'Der Wert zu Competence muss positiv sein',
                'quality_bewertung'.$lieferung->id.'.max' => 'Der Wert zu Quality darf max. 100 sein',
                'reliability_bewertung'.$lieferung->id.'.max' => 'Der Wert zu Reliability darf max. 100 sein',
                'competence_bewertung'.$lieferung->id.'.max' => 'Der Wert zu Competence darf max. 100 sein',
                'quality_bewertung'.$lieferung->id.'.integer' => 'Der Wert zu Quality muss ganzzahlig sein',
                'reliability_bewertung'.$lieferung->id.'.integer' => 'Der Wert zu Reliability muss ganzzahlig sein',
                'competence_bewertung'.$lieferung->id.'.integer' => 'Der Wert zu Competence muss ganzzahlig sein',
            ]);
        }

        // update all shipments that have arrived
        foreach($lieferungen as $lieferung)
        {
            $quality_differenz = $lieferung->quality_bewertung - $request->input('quality_bewertung'.$lieferung->id);
            $reliability_differenz = $lieferung->reliability_bewertung - $request->input('reliability_bewertung'.$lieferung->id);
            $competence_differenz = $lieferung->competence_bewertung - $request->input('competence_bewertung'.$lieferung->id);

            $lieferung->quality_bewertung = $request->input('quality_bewertung'.$lieferung->id);
            $lieferung->reliability_bewertung = $request->input('quality_bewertung'.$lieferung->id);
            $lieferung->competence_bewertung = $request->input('quality_bewertung'.$lieferung->id);
            $lieferung->quality_differenz = $quality_differenz;
            $lieferung->reliability_differenz = $reliability_differenz;
            $lieferung->competence_differenz = $competence_differenz;
            $lieferung->save();
        }

        if ($current_step == 19)
        {
            return redirect()->action('GetSurveyController@getWeek18');
        }

        if ($current_step > 19)
        {
            return redirect()->action('GetSurveyController@getWeekX2');
        }

        return redirect()->action('GetSurveyController@getWeekX');
    }

    public function postFrage(Request $request)
    {
        $preis = $request->input('preis');
        $lieferzeit = $request->input('lieferzeit');
        $vertrauen = $request->input('vertrauen');

        $this->validate($request, [
            'preis' => 'required|integer|min:'.(99-$vertrauen-$lieferzeit).'|max:'.(99-$vertrauen-$lieferzeit),
            'lieferzeit' => 'required|integer|min:'.(99-$vertrauen-$preis).'|max:'.(99-$vertrauen-$preis),
            'vertrauen' => 'required|integer|min:'.(99-$preis-$lieferzeit).'|max:'.(99-$lieferzeit-$preis),
        ], [
            'preis.required' => 'Bitte geben Sie eine Zahl für Preis ein',
            'lieferzeit.required' => 'Bitte geben Sie eine Zahl für Lieferzeit ein',
            'vertrauen.required' => 'Bitte geben Sie eine Zahl für Vertrauen ein',
            'preis.integer' => 'Die Zahl für Preis muss ganzzahlig sein',
            'lieferzeit.integer' => 'Die Zahl für Lieferzeit muss ganzzahlig sein',
            'vertrauen.integer' => 'Die Zahl für Vertrauen muss ganzzahlig sein',
            'preis.min' => 'Die Summe der 3 Werte muss genau 99 sein',
            'lieferzeit.min' => 'Die Summe der 3 Werte muss genau 99 sein',
            'vertrauen.min' => 'Die Summe der 3 Werte muss genau 99 sein',
            'preis.max' => 'Die Summe der 3 Werte muss genau 99 sein',
            'vertrauen.max' => 'Die Summe der 3 Werte muss genau 99 sein',
            'lieferzeit.max' => 'Die Summe der 3 Werte muss genau 99 sein',
        ]);

        $request->session()->get('survey')->update([
            'frage_preis' => $preis,
            'frage_lieferzeit' => $lieferzeit,
            'frage_vertrauen' => $vertrauen,
        ]);

        return redirect()->action('GetSurveyController@getTeil2');
//        return redirect()->action('GetSurveyController@getWeekX2');
    }

    public function postFrage2(Request $request)
    {
        $competence = $request->input('competence');
        $reliability = $request->input('reliability');
        $quality = $request->input('quality');
        $shared_values = $request->input('shared_values');

        $this->validate($request, [
            'competence' => 'required|integer|min:'.(100-$reliability-$quality-$shared_values).'|max:'.(100-$reliability-$quality-$shared_values),
            'reliability' => 'required|integer|min:'.(100-$competence-$quality-$shared_values).'|max:'.(100-$competence-$quality-$shared_values),
            'quality' => 'required|integer|min:'.(100-$competence-$reliability-$shared_values).'|max:'.(100-$competence-$reliability-$shared_values),
            'shared_values' => 'required|integer|min:'.(100-$competence-$reliability-$quality).'|max:'.(100-$competence-$reliability-$quality),
        ], [
            'competence.required' => 'Bitte geben Sie eine Zahl für Competence ein',
            'reliability.required' => 'Bitte geben Sie eine Zahl für Reliability ein',
            'quality.required' => 'Bitte geben Sie eine Zahl für Quality ein',
            'shared_values.required' => 'Bitte geben Sie eine Zahl für Shared Values ein',

            'competence.integer' => 'Die Zahl für Competence muss ganzzahlig sein',
            'reliability.integer' => 'Die Zahl für Reliability muss ganzzahlig sein',
            'quality.integer' => 'Die Zahl für Quality muss ganzzahlig sein',
            'shared_values.integer' => 'Die Zahl für Shared Values muss ganzzahlig sein',

            'competence.min' => 'Die Summe der 4 Werte muss genau 100 sein',
            'reliability.min' => 'Die Summe der 4 Werte muss genau 100 sein',
            'quality.min' => 'Die Summe der 4 Werte muss genau 100 sein',
            'shared_values.min' => 'Die Summe der 4 Werte muss genau 100 sein',

            'competence.max' => 'Die Summe der 4 Werte muss genau 100 sein',
            'reliability.max' => 'Die Summe der 4 Werte muss genau 100 sein',
            'quality.max' => 'Die Summe der 4 Werte muss genau 100 sein',
            'shared_values.max' => 'Die Summe der 4 Werte muss genau 100 sein',
        ]);

        $request->session()->get('survey')->update([
            'frage_competence' => $competence,
            'frage_reliability' => $reliability,
            'frage_quality' => $quality,
            'frage_shared_values' => $shared_values,
        ]);

        return redirect()->action('GetSurveyController@getEndFragen');
    }

    public function postEndFragen(Request $request)
    {
        $a1 = $request->input('1a');
        $b1 = $request->input('1b');
        $a2 = $request->input('2a');
        $b2 = $request->input('2b');
        $drei = $request->input('3');
        $vier = $request->input('4');

        $this->validate($request, [
            '1a' => 'required|integer|min:0|max:100',
            '1b' => 'required|integer|min:0|max:100',
            '2a' => 'required|integer|min:'.(100-$b2).'|max:'.(100-$b2),
            '2b' => 'required|integer|min:'.(100-$a2).'|max:'.(100-$a2),
            '3' => 'required',
            '4' => 'required',
        ], [
            '1a.required' => 'Geben Sie eine Zahl für Frage 1a ein',
            '1b.required' => 'Geben Sie eine Zahl für Frage 1b ein',
            '2a.required' => 'Geben Sie eine Zahl für Frage 2a ein',
            '2b.required' => 'Geben Sie eine Zahl für Frage 2b ein',
            '1a.integer' => 'Die Zahl für Frage 1a muss ganzzahlig sein',
            '1b.integer' => 'Die Zahl für Frage 1b muss ganzzahlig sein',
            '2a.integer' => 'Die Zahl für Frage 2a muss ganzzahlig sein',
            'b.integer' => 'Die Zahl für Frage 2b muss ganzzahlig sein',
            '1a.min' => 'Die Zahl für Frage 1a darf nicht negativ sein',
            '1b.min' => 'Die Zahl für Frage 1a darf nicht negativ sein',
            '2a.min' => 'Die Bewertung für Frage muss insgesamt 100% ausmachen',
            '2b.min' => 'Die Bewertung für Frage muss insgesamt 100% ausmachen',
            '1a.max' => 'Die Zahl für Frage 1a darf nicht größer als 100 sein',
            '1b.max' => 'Die Zahl für Frage 1b darf nicht größer als 100 sein',
            '2a.max' => 'Die Bewertung für Frage muss insgesamt 100% ausmachen',
            '2b.max' => 'Die Bewertung für Frage muss insgesamt 100% ausmachen',
            '3.required' => 'Beantworten Sie Frage 3',
            '4.required' => 'Beantworten Sie Frage 4',
        ]);


        $request->session()->get('survey')->update([
            'Frage_1a' => $a1,
            'Frage_1b' => $b1,
            'Frage_2a' => $a2,
            'Frage_2b' => $b2,
            'Frage_3' => $drei,
            'Frage_4' => $vier,
        ]);

        return redirect()->action('GetSurveyController@getHighscore');
    }
}