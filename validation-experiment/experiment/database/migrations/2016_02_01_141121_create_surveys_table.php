<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateSurveysTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('surveys', function (Blueprint $table) {
            $table->increments('id');
            $table->string('name')->unique();
            $table->string('current_step');

            for ($i = 0; $i <= 30; $i++)
            {
                $table->integer('kontostand_week'.$i);
            }

            // Für jede Woche
            for ($i = 1; $i <= 30; $i++)
            {
                $table->integer('bestellkosten_week'.$i);
            }

            // Für jede Woche
            for ($i = 1; $i <= 30; $i++)
            {
                $table->integer('umsatz_week'.$i);
            }

            // Für jede Woche
            for ($i = 1; $i <= 30; $i++)
            {
                $table->integer('lagerkosten_week'.$i);
            }

            // Für den Anfang jeder Woche und 31 für Endstand
            for ($i = 0; $i <= 31; $i++)
            {
                $table->integer('lagerbestand_week'.$i);
            }

            // Für jede Woche
            for ($i = 1; $i <= 30; $i++)
            {
                $table->integer('bestellmenge_week'.$i);
            }

            // Für jede Woche
            for ($i = 1; $i <= 30; $i++)
            {
                $table->integer('gewaehlter_lieferant_week'.$i)->unsigned()->nullable();
                $table->foreign('gewaehlter_lieferant_week'.$i)
                    ->references('id')
                    ->on('suppliers')
                    ->onDelete('cascade');
            }

            // Für jede Woche, außer Woche 1
            for ($i = 1; $i <= 30; $i++)
            {
                $table->integer('erhaltene_menge_week'.$i);
            }

            for ($i = 1; $i <= 30; $i++)
            {
                $table->integer('verkaufte_menge_week'.$i);
            }

            for ($i = 1; $i <= 30; $i++)
            {
                $table->integer('back_order_menge_week'.$i);
            }

            for ($i = 1; $i <= 30; $i++)
            {
                $table->integer('back_order_kosten_week'.$i);
            }

            $table->integer('frage_preis');
            $table->integer('frage_lieferzeit');
            $table->integer('frage_vertrauen');
            $table->integer('frage_competence');
            $table->integer('frage_reliability');
            $table->integer('frage_quality');
            $table->integer('frage_shared_values');
            $table->string('Frage_1a');
            $table->string('Frage_1b');
            $table->string('Frage_2a');
            $table->string('Frage_2b');
            $table->string('Frage_3');
            $table->string('Frage_4');

            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::drop('surveys');
    }
}
