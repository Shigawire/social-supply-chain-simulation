<?php

namespace App\Helpers;

use App\Lieferung;
use App\Back_Order;
use App\Umsatz;

class General
{
    public static function create_lieferung($selected_supplier, $user_session, $bestellmenge_stepX, $request)
    {
        $min = 40;
        $max = 60;

        if ($user_session->current_step == 2 || $user_session->current_step == 8 || $user_session->current_step == 14)
        {
            $quality = 60;
            $gelieferte_menge = round($bestellmenge_stepX * 0.6);
            $quality = 100 - (($gelieferte_menge / $bestellmenge_stepX) * 100);
        }
        else if ($user_session->current_step == 5 ||
                 $user_session->current_step == 6 ||
                 $user_session->current_step == 11 ||
                 $user_session->current_step == 17 ||
                 $user_session->current_step == 18 ||
                 $user_session->current_step == 20 ||
                 $user_session->current_step == 24 ||
                 $user_session->current_step == 28)
        {
            $quality = 80;
            $gelieferte_menge = round($bestellmenge_stepX * 0.8);
            $quality = 100 - (($gelieferte_menge / $bestellmenge_stepX) * 100);
        }
        else if ($user_session->current_step == 12)
        {
            $quality = 20;
            $gelieferte_menge = round($bestellmenge_stepX * 0.2);
            $quality = 100 - (($gelieferte_menge / $bestellmenge_stepX) * 100);
        }
        else
        {
            $quality = 0;
            $gelieferte_menge = $bestellmenge_stepX;
        }


        $lieferung = Lieferung::Create([
            'verbl_lieferzeit' => $selected_supplier->lieferzeit,
            'besteller' => $user_session->id,
            'bestellmenge' => $bestellmenge_stepX,
            'bestellt_in_runde' => $user_session->current_step,
            'lieferzeit' => $selected_supplier->lieferzeit,
            'lieferzeit_laut_supplier' => $selected_supplier->angegebene_lieferzeit,
            'preis' => $selected_supplier->preis,
            'supplier' => $selected_supplier->id,
            'competence_bewertung' => rand($min, $max),
            'quality_bewertung' => rand($min, $max),
            'reliability_bewertung' => rand($min, $max),
            'quality' => $quality,
            'gelieferte_menge' => $gelieferte_menge,
        ]);

        $request->session()->put('lieferungen', $lieferung);
    }

    public static function check_lieferungen($besteller)
    {
        $lieferungen = Lieferung::where('besteller', '=', $besteller)->where('verbl_lieferzeit', '=', 1)->get();
        $angekommen = 0;
        foreach ($lieferungen as $lieferung)
        {
            $angekommen += $lieferung->gelieferte_menge;
        }

        return $angekommen;
    }

    public static function fulfill_back_orders($teilnehmer, $lagerbestand)
    {
        $gesamtgewinn = 0;
        $verkaufte_menge = 0;
        $umsatz = 0;
        $back_orders = Back_Order::where('teilnehmer', '=', $teilnehmer->id)->where('verbl_menge', '>', 0)->OrderBy('nachgefragt_in_runde', 'asc')->get();
        foreach ($back_orders as $back_order)
        {
            if ($lagerbestand >= $back_order->verbl_menge)
            {
                $lagerbestand -= $back_order->verbl_menge;
                $verkaufte_menge += $back_order->verbl_menge;
                $umsatz = $back_order->verbl_menge * $back_order->verkaufspreis;
                $gesamtgewinn += $umsatz;
                General::create_umsatz($back_order->verkaufspreis, $back_order->verbl_menge, $umsatz, $back_order->nachgefragt_in_runde, $teilnehmer->current_step, $back_order->verbl_menge, $teilnehmer->id);
                $back_order->delete();
            }
            else if ($lagerbestand < $back_order->verbl_menge && $lagerbestand > 0)
            {
                $verkaufte_menge += $lagerbestand;
                $umsatz = $lagerbestand * $back_order->verkaufspreis;
                $gesamtgewinn += $umsatz;
                General::create_umsatz($back_order->verkaufspreis, $lagerbestand, $umsatz, $back_order->nachgefragt_in_runde, $teilnehmer->current_step, $back_order->verbl_menge, $teilnehmer->id);
                $back_order->verbl_menge -= $lagerbestand;
                $back_order->save();
                $lagerbestand = 0;
            }
            else
            {
                $verkaufte_menge += 0;
                $umsatz = 0;
                $gesamtgewinn += $umsatz;
                General::create_umsatz($back_order->verkaufspreis, 0, $umsatz, $back_order->nachgefragt_in_runde, $teilnehmer->current_step, $back_order->verbl_menge, $teilnehmer->id);
            }
        }

        return array($verkaufte_menge, $lagerbestand, $gesamtgewinn);
    }

    public static function fulfill_demand($lagerbestand, $user_session)
    {
        $verkaufte_menge = 0;
        $back_order_menge = 0;
        $gesamtgewinn = 0;
        if ($lagerbestand >= 5)
        {
            $lagerbestand -= 5;
            $verkaufte_menge += 5;
            $umsatz = 5 * 100;
            $gesamtgewinn += $umsatz;
            General::create_umsatz(100, 5, $umsatz, $user_session->current_step, $user_session->current_step, 5, $user_session->id);
        }
        else if ($lagerbestand < 5 && $lagerbestand > 0)
        {
            $verkaufte_menge += $lagerbestand;
            $back_order_menge = 5 - $lagerbestand;
            $back_order = Back_Order::Create([
                'nachgefragt_in_runde' => $user_session->current_step,
                'verkaufspreis' => 100,
                'verbl_menge' => $back_order_menge,
                'teilnehmer' => $user_session->id,
            ]);
            $umsatz = $lagerbestand * 100;
            $gesamtgewinn += $umsatz;
            General::create_umsatz(100, $lagerbestand, $umsatz, $user_session->current_step, $user_session->current_step, 5, $user_session->id);
            $lagerbestand = 0;
        }
        else
        {
            $verkaufte_menge += 0;
            $back_order_menge = 5;
            $back_order = Back_Order::Create([
                'nachgefragt_in_runde' => $user_session->current_step,
                'verkaufspreis' => 100,
                'verbl_menge' => $back_order_menge,
                'teilnehmer' => $user_session->id,
            ]);
            $umsatz = 0;
            $gesamtgewinn += $umsatz;
            General::create_umsatz(100, 0, $umsatz, $user_session->current_step, $user_session->current_step, 5, $user_session->id);
        }

        return array($verkaufte_menge, $lagerbestand, $gesamtgewinn, $back_order_menge);
    }

    public static function lieferzeit_verringern($user_session)
    {
        $lieferungen_ende = Lieferung::where('besteller', '=', $user_session->id)->get();

        foreach ($lieferungen_ende as $lieferung)
        {
            $lieferung->verbl_lieferzeit--;
            $lieferung->save();
        }
    }

    public static function back_orders($user_session)
    {
        $back_orders_ende = Back_Order::where('teilnehmer', '=', $user_session->id)->get();

        foreach ($back_orders_ende as $back_order)
        {
            if ($back_order->verkaufspreis >= 12)
            {
                $back_order->verkaufspreis -= 4;
            }
            $back_order->save();
        }
    }

    public static function update_survey($request, $user_session,
                                         $neuer_kontostand, $bestellkosten,
                                         $umsatz, $lagerkosten,
                                         $lagerbestand, $bestellmenge,
                                         $selected_supplier, $erhaltene_menge,
                                         $verkaufte_menge, $back_order_menge, $back_order_kosten)
    {
        $current_step = $user_session->current_step;
        $next_step = $current_step + 1;

        $request->session()->get('survey')->update([
            'current_step' => $next_step,
            'kontostand_week'.$current_step => $neuer_kontostand,
            'bestellkosten_week'.$current_step => $bestellkosten,
            'umsatz_week'.$current_step => $umsatz,
            'lagerkosten_week'.$current_step => $lagerkosten,
            'lagerbestand_week'.$current_step => $lagerbestand,
            'bestellmenge_week'.$current_step => $bestellmenge,
            'gewaehlter_lieferant_week'.$current_step => $selected_supplier->id,
            'erhaltene_menge_week'.$current_step => $erhaltene_menge,
            'verkaufte_menge_week'.$current_step => $verkaufte_menge,
            'back_order_menge_week'.$current_step => $back_order_menge,
            'back_order_kosten_week'.$current_step => $back_order_kosten,
        ]);
    }



    public static function create_umsatz($verkaufspreis, $verkaufte_menge,
                                         $umsatz, $nachfrage_aus_runde,
                                         $umsatz_erzielt_in_runde, $verbl_nachfrage,
                                         $teilnehmer)
    {
        Umsatz::create([
            'verkaufspreis' => $verkaufspreis,
            'verkaufte_menge' => $verkaufte_menge,
            'umsatz' => $umsatz,
            'nachfrage_aus_runde' => $nachfrage_aus_runde,
            'umsatz_erzielt_in_runde' => $umsatz_erzielt_in_runde,
            'verbl_nachfrage' => $verbl_nachfrage,
            'teilnehmer' => $teilnehmer,
        ]);
    }
}