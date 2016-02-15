<?php

use Illuminate\Database\Seeder;
use Illuminate\Database\Eloquent\Model;

use App\Question;

class DatabaseSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        Model::unguard();

        Question::create(['step' => 1,
                          'name' => 'haendler',
                          'description' => 'Ihr Kunde bestellt bei Ihnen 5 Produkte. Sie haben folgende Händler zu Auswahl.',
                          'type' => 'radio',
                          'text' => 'Für welchen Händler entscheiden Sie sich?',
                          'values' => ['1: Preis: 40 Euro, Lieferzeit: 1 Runden, allg. Vertrauenswürdigkeit: (niedrig)',
                                       '2: Preis: 20 Euro, Lieferzeit: 2 Runden, allg. Vertrauensw.: (niedrig)',
                                       '3: Preis: 30 Euro, Lieferzeit: 2 Runden, allg. Vertrauensw.: (hoch)',
                                       '4: Preis: 20 Euro, Lieferzeit: 2 Runden, allg. Vertrauensw. (mittel)'],
                          'rule' => 'required']);
        Question::create(['step' => 1,
            'name' => 'bestellmenge',
            'description' => 'Bitte geben Sie an, wieviel Sie bestellen möchten',
            'type' => 'text',
            'text' => 'Bestellmenge',
            'rule' => 'integer']);


        Question::create(['step' => 2, 'name' => 'bestellmenge2', 'description' => 'Sie haben 10 Produkte von Ihren Zulieferern erhalten. Wieiviel möchten Sie jetzt bestellen?', 'type' => 'text', 'text' => 'Bestellmenge', 'rule' => 'integer']);

        Model::reguard();
    }
}
