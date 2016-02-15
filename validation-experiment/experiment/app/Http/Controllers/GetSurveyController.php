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

class GetSurveyController extends Controller
{
    public function __construct()
    {
        $this->middleware('auth');
    }

    public function getSurvey()
    {
        return view('survey.willkommen');
    }

    public function getWeek1(Request $request)
    {
        $suppliers = Supplier::where('step', '=', '1')->get();

        $user_session = $request->session()->get('survey');

        if ($user_session->current_step != 1)
        {
            return Redirect::back();
        }

        return view('survey.week_1', ['survey' => $request->session()->get('survey')])->with('suppliers', $suppliers);
    }

    public function getWeekX(Request $request)
    {
        // Get the current user
        $user_session = $request->session()->get('survey');
        $current_step = $user_session->current_step;
        $previous_step = $current_step - 1;
        $pre_previous_step = $current_step - 2;

        // Get all suppliers for the current step
        $current_suppliers = Supplier::where('step', '=', $current_step)->get();

        // Get last supplier
        $supplier_id = 'gewaehlter_lieferant_week'.$previous_step;
        $previous_supplier = Supplier::where('id', '=', $user_session->$supplier_id)->first();

        // Alle Lieferungen, die angekommen sind
        $lieferungen = Lieferung::where('besteller', '=', $user_session->id)->where('verbl_lieferzeit', '=', 0)->where('gelieferte_menge', '>', 0)->get();

        $umsaetze = Umsatz::where('teilnehmer', '=', $user_session->id)->where('umsatz_erzielt_in_runde', '=', $user_session->current_step - 1)->where('umsatz', '>', 0)->get();
        $gesamtumsatz = 0;
        foreach ($umsaetze as $umsatz)
        {
            $gesamtumsatz += $umsatz->umsatz;
        }

        $back_orders = Back_Order::where('teilnehmer', '=', $user_session->id)->get();
        $gesamte_back_order_menge = 0;
        foreach ($back_orders as $back_order)
        {
            $gesamte_back_order_menge += $back_order->verbl_menge;
        }

        // Sume der eingetroffenen Bestellungen
        $gesamte_gelieferte_menge = 0;
        foreach ($lieferungen as $lieferung)
        {
            $gesamte_gelieferte_menge += $lieferung->gelieferte_menge;
        }

        $bisheriger_lagerbestand_stepX = 'lagerbestand_week'.$pre_previous_step;
        $bisheriger_lagerbestand = $user_session->$bisheriger_lagerbestand_stepX;
        $neuer_lagerbestand_stepX = 'lagerbestand_week'.$previous_step;
        $neuer_lagerbestand = $user_session->$neuer_lagerbestand_stepX;

        $bisheriger_kontostand_stepX = 'kontostand_week'.$pre_previous_step;
        $bisheriger_kontostand = $user_session->$bisheriger_kontostand_stepX;
        $neuer_kontostand_stepX = 'kontostand_week'.$previous_step;
        $neuer_kontostand = $user_session->$neuer_kontostand_stepX;

        $verkaufte_menge_weekX = 'verkaufte_menge_week'.$previous_step;
        $verkaufte_menge = $user_session->$verkaufte_menge_weekX;

        $bestellmenge_stepX = 'bestellmenge_week'.$previous_step;
        $bestellte_menge = $user_session->$bestellmenge_stepX;

        $back_order_kosten_weekX = 'back_order_kosten_week'.$previous_step;
        $back_order_kosten = $user_session->$back_order_kosten_weekX;

        $lagerkosten_stepX = 'lagerkosten_week'.$previous_step;
        $lagerkosten = $user_session->$lagerkosten_stepX;

        return view('survey.week_X', ['survey' => $request->session()->get('survey')])
                ->with('current_step', $current_step)
                ->with('suppliers', $current_suppliers)
                ->with('neuer_kontostand', $neuer_kontostand)
                ->with('neuer_lagerbestand', $neuer_lagerbestand)
                ->with('bestellte_menge', $bestellte_menge)
                ->with('previous_supplier', $previous_supplier)
                ->with('gesamte_gelieferte_menge', $gesamte_gelieferte_menge)
                ->with('lieferungen', $lieferungen)
                ->with('gesamtumsatz', $gesamtumsatz)
                ->with('umsaetze', $umsaetze)
                ->with('back_orders', $back_orders)
                ->with('gesamte_back_order_menge', $gesamte_back_order_menge)
                ->with('back_order_kosten', $back_order_kosten)
                ->with('bisheriger_lagerbestand', $bisheriger_lagerbestand)
                ->with('verkaufte_menge', $verkaufte_menge)
                ->with('bisheriger_kontostand', $bisheriger_kontostand)
                ->with('lagerkosten', $lagerkosten);
    }

    public function getWeek18(Request $request)
    {
        // Get the current user
        $user_session = $request->session()->get('survey');
        $current_step = $user_session->current_step;
        $previous_step = $current_step - 1;
        $pre_previous_step = $current_step - 2;

        // Get last supplier
        $supplier_id = 'gewaehlter_lieferant_week'.$previous_step;
        $previous_supplier = Supplier::where('id', '=', $user_session->$supplier_id)->first();

        // Alle Lieferungen, die angekommen sind
        $lieferungen = Lieferung::where('besteller', '=', $user_session->id)->where('verbl_lieferzeit', '=', 0)->where('gelieferte_menge', '>', 0)->get();

        $umsaetze = Umsatz::where('teilnehmer', '=', $user_session->id)->where('umsatz_erzielt_in_runde', '=', $user_session->current_step - 1)->where('umsatz', '>', 0)->get();
        $gesamtumsatz = 0;
        foreach ($umsaetze as $umsatz)
        {
            $gesamtumsatz += $umsatz->umsatz;
        }

        $back_orders = Back_Order::where('teilnehmer', '=', $user_session->id)->get();
        $gesamte_back_order_menge = 0;
        foreach ($back_orders as $back_order)
        {
            $gesamte_back_order_menge += $back_order->verbl_menge;
        }

        // Sume der eingetroffenen Bestellungen
        $gesamte_gelieferte_menge = 0;
        foreach ($lieferungen as $lieferung)
        {
            $gesamte_gelieferte_menge += $lieferung->gelieferte_menge;
        }

        $bisheriger_lagerbestand_stepX = 'lagerbestand_week'.$pre_previous_step;
        $bisheriger_lagerbestand = $user_session->$bisheriger_lagerbestand_stepX;
        $neuer_lagerbestand_stepX = 'lagerbestand_week'.$previous_step;
        $neuer_lagerbestand = $user_session->$neuer_lagerbestand_stepX;

        $bisheriger_kontostand_stepX = 'kontostand_week'.$pre_previous_step;
        $bisheriger_kontostand = $user_session->$bisheriger_kontostand_stepX;
        $neuer_kontostand_stepX = 'kontostand_week'.$previous_step;
        $neuer_kontostand = $user_session->$neuer_kontostand_stepX;

        $verkaufte_menge_weekX = 'verkaufte_menge_week'.$previous_step;
        $verkaufte_menge = $user_session->$verkaufte_menge_weekX;

        $bestellmenge_stepX = 'bestellmenge_week'.$previous_step;
        $bestellte_menge = $user_session->$bestellmenge_stepX;

        $back_order_kosten_weekX = 'back_order_kosten_week'.$previous_step;
        $back_order_kosten = $user_session->$back_order_kosten_weekX;

        $lagerkosten_stepX = 'lagerkosten_week'.$previous_step;
        $lagerkosten = $user_session->$lagerkosten_stepX;

        return view('survey.bericht_week_18', ['survey' => $request->session()->get('survey')])
            ->with('current_step', $current_step)
            ->with('neuer_kontostand', $neuer_kontostand)
            ->with('neuer_lagerbestand', $neuer_lagerbestand)
            ->with('bestellte_menge', $bestellte_menge)
            ->with('previous_supplier', $previous_supplier)
            ->with('gesamte_gelieferte_menge', $gesamte_gelieferte_menge)
            ->with('lieferungen', $lieferungen)
            ->with('gesamtumsatz', $gesamtumsatz)
            ->with('umsaetze', $umsaetze)
            ->with('back_orders', $back_orders)
            ->with('gesamte_back_order_menge', $gesamte_back_order_menge)
            ->with('back_order_kosten', $back_order_kosten)
            ->with('bisheriger_lagerbestand', $bisheriger_lagerbestand)
            ->with('verkaufte_menge', $verkaufte_menge)
            ->with('bisheriger_kontostand', $bisheriger_kontostand)
            ->with('lagerkosten', $lagerkosten);
    }

    public function getEndBericht(Request $request)
    {
        // Get the current user
        $user_session = $request->session()->get('survey');
        $current_step = $user_session->current_step;
        $previous_step = $current_step - 1;
        $pre_previous_step = $current_step - 2;

        // Get last supplier
        $supplier_id = 'gewaehlter_lieferant_week'.$previous_step;
        $previous_supplier = Supplier::where('id', '=', $user_session->$supplier_id)->first();

        // Alle Lieferungen, die angekommen sind
        $lieferungen = Lieferung::where('besteller', '=', $user_session->id)->where('verbl_lieferzeit', '=', 0)->where('gelieferte_menge', '>', 0)->get();

        $umsaetze = Umsatz::where('teilnehmer', '=', $user_session->id)->where('umsatz_erzielt_in_runde', '=', $user_session->current_step - 1)->where('umsatz', '>', 0)->get();
        $gesamtumsatz = 0;
        foreach ($umsaetze as $umsatz)
        {
            $gesamtumsatz += $umsatz->umsatz;
        }

        $back_orders = Back_Order::where('teilnehmer', '=', $user_session->id)->get();
        $gesamte_back_order_menge = 0;
        foreach ($back_orders as $back_order)
        {
            $gesamte_back_order_menge += $back_order->verbl_menge;
        }

        // Sume der eingetroffenen Bestellungen
        $gesamte_gelieferte_menge = 0;
        foreach ($lieferungen as $lieferung)
        {
            $gesamte_gelieferte_menge += $lieferung->gelieferte_menge;
        }

        $bisheriger_lagerbestand_stepX = 'lagerbestand_week'.$pre_previous_step;
        $bisheriger_lagerbestand = $user_session->$bisheriger_lagerbestand_stepX;
        $neuer_lagerbestand_stepX = 'lagerbestand_week'.$previous_step;
        $neuer_lagerbestand = $user_session->$neuer_lagerbestand_stepX;

        $bisheriger_kontostand_stepX = 'kontostand_week'.$pre_previous_step;
        $bisheriger_kontostand = $user_session->$bisheriger_kontostand_stepX;
        $neuer_kontostand_stepX = 'kontostand_week'.$previous_step;
        $neuer_kontostand = $user_session->$neuer_kontostand_stepX;

        $verkaufte_menge_weekX = 'verkaufte_menge_week'.$previous_step;
        $verkaufte_menge = $user_session->$verkaufte_menge_weekX;

        $bestellmenge_stepX = 'bestellmenge_week'.$previous_step;
        $bestellte_menge = $user_session->$bestellmenge_stepX;

        $back_order_kosten_weekX = 'back_order_kosten_week'.$previous_step;
        $back_order_kosten = $user_session->$back_order_kosten_weekX;

        $lagerkosten_stepX = 'lagerkosten_week'.$previous_step;
        $lagerkosten = $user_session->$lagerkosten_stepX;

        return view('survey.end_bericht', ['survey' => $request->session()->get('survey')])
            ->with('current_step', $current_step)
            ->with('neuer_kontostand', $neuer_kontostand)
            ->with('neuer_lagerbestand', $neuer_lagerbestand)
            ->with('bestellte_menge', $bestellte_menge)
            ->with('previous_supplier', $previous_supplier)
            ->with('gesamte_gelieferte_menge', $gesamte_gelieferte_menge)
            ->with('lieferungen', $lieferungen)
            ->with('gesamtumsatz', $gesamtumsatz)
            ->with('umsaetze', $umsaetze)
            ->with('back_orders', $back_orders)
            ->with('gesamte_back_order_menge', $gesamte_back_order_menge)
            ->with('back_order_kosten', $back_order_kosten)
            ->with('bisheriger_lagerbestand', $bisheriger_lagerbestand)
            ->with('verkaufte_menge', $verkaufte_menge)
            ->with('bisheriger_kontostand', $bisheriger_kontostand)
            ->with('lagerkosten', $lagerkosten);
    }

    public function getWeekX2(Request $request)
    {
        // Get the current user
        $user_session = $request->session()->get('survey');
        $current_step = $user_session->current_step;
        $previous_step = $current_step - 1;
        $pre_previous_step = $current_step - 2;

        // Get all suppliers for the current step
        $current_suppliers = Supplier::where('step', '=', $current_step)->get();

        // Get last supplier
        $supplier_id = 'gewaehlter_lieferant_week'.$previous_step;
        $previous_supplier = Supplier::where('id', '=', $user_session->$supplier_id)->first();

        // Alle Lieferungen, die angekommen sind
        $lieferungen = Lieferung::where('besteller', '=', $user_session->id)->where('verbl_lieferzeit', '=', 0)->where('gelieferte_menge', '>', 0)->get();

        $umsaetze = Umsatz::where('teilnehmer', '=', $user_session->id)->where('umsatz_erzielt_in_runde', '=', $user_session->current_step - 1)->where('umsatz', '>', 0)->get();
        $gesamtumsatz = 0;
        foreach ($umsaetze as $umsatz)
        {
            $gesamtumsatz += $umsatz->umsatz;
        }

        $back_orders = Back_Order::where('teilnehmer', '=', $user_session->id)->get();
        $gesamte_back_order_menge = 0;
        foreach ($back_orders as $back_order)
        {
            $gesamte_back_order_menge += $back_order->verbl_menge;
        }

        // Sume der eingetroffenen Bestellungen
        $gesamte_gelieferte_menge = 0;
        foreach ($lieferungen as $lieferung)
        {
            $gesamte_gelieferte_menge += $lieferung->gelieferte_menge;
        }

        $bisheriger_lagerbestand_stepX = 'lagerbestand_week'.$pre_previous_step;
        $bisheriger_lagerbestand = $user_session->$bisheriger_lagerbestand_stepX;
        $neuer_lagerbestand_stepX = 'lagerbestand_week'.$previous_step;
        $neuer_lagerbestand = $user_session->$neuer_lagerbestand_stepX;

        $bisheriger_kontostand_stepX = 'kontostand_week'.$pre_previous_step;
        $bisheriger_kontostand = $user_session->$bisheriger_kontostand_stepX;
        $neuer_kontostand_stepX = 'kontostand_week'.$previous_step;
        $neuer_kontostand = $user_session->$neuer_kontostand_stepX;

        $verkaufte_menge_weekX = 'verkaufte_menge_week'.$previous_step;
        $verkaufte_menge = $user_session->$verkaufte_menge_weekX;

        $bestellmenge_stepX = 'bestellmenge_week'.$previous_step;
        $bestellte_menge = $user_session->$bestellmenge_stepX;

        $back_order_kosten_weekX = 'back_order_kosten_week'.$previous_step;
        $back_order_kosten = $user_session->$back_order_kosten_weekX;

        $lagerkosten_stepX = 'lagerkosten_week'.$previous_step;
        $lagerkosten = $user_session->$lagerkosten_stepX;

        return view('survey.week_X2', ['survey' => $request->session()->get('survey')])
            ->with('current_step', $current_step)
            ->with('suppliers', $current_suppliers)
            ->with('neuer_kontostand', $neuer_kontostand)
            ->with('neuer_lagerbestand', $neuer_lagerbestand)
            ->with('bestellte_menge', $bestellte_menge)
            ->with('previous_supplier', $previous_supplier)
            ->with('gesamte_gelieferte_menge', $gesamte_gelieferte_menge)
            ->with('lieferungen', $lieferungen)
            ->with('gesamtumsatz', $gesamtumsatz)
            ->with('umsaetze', $umsaetze)
            ->with('back_orders', $back_orders)
            ->with('gesamte_back_order_menge', $gesamte_back_order_menge)
            ->with('back_order_kosten', $back_order_kosten)
            ->with('bisheriger_lagerbestand', $bisheriger_lagerbestand)
            ->with('verkaufte_menge', $verkaufte_menge)
            ->with('bisheriger_kontostand', $bisheriger_kontostand)
            ->with('lagerkosten', $lagerkosten);
    }

    public function getBewertung(Request $request)
    {
        // Get the current user
        $user_session = $request->session()->get('survey');

        $lieferungen = Lieferung::where('besteller', '=', $user_session->id)->where('verbl_lieferzeit', '=', 0)->where('gelieferte_menge', '>', 0)->get();


        return view('survey.bewertung_X', ['survey' => $request->session()->get('survey')])->with('lieferungen', $lieferungen)
                                                                                           ->with('user_session', $user_session);
    }

    public function get99Punkte(Request $request)
    {
        return view('survey.99_Punkte', ['survey' => $request->session()->get('survey')]);
    }

    public function get100Punkte(Request $request)
    {
        return view('survey.100_Punkte', ['survey' => $request->session()->get('survey')]);
    }

    public function getHighscore(Request $request)
    {
        $surveys = Survey::orderBy('kontostand_week30', 'desc')->where('current_step', '=', 31)->get();


        return view('highscore', ['survey' => $request->session()->get('survey')])->with('surveys', $surveys);
    }

    public function getEndFragen(Request $request)
    {
        return view('survey.endfragen', ['survey' => $request->session()->get('survey')]);
    }

    public function getTeil2(Request $request)
    {
        return view('survey.teil2', ['survey' => $request->session()->get('survey')]);
    }
}