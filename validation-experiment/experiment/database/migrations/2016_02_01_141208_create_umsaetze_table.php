<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateUmsaetzeTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('umsaetze', function (Blueprint $table) {
            $table->increments('id');
            $table->integer('verkaufspreis');
            $table->integer('verkaufte_menge');
            $table->integer('umsatz');
            $table->string('nachfrage_aus_runde');
            $table->string('umsatz_erzielt_in_runde');
            $table->integer('verbl_nachfrage');
            $table->integer('teilnehmer')->unsigned();
            $table->foreign('teilnehmer')
                ->references('id')
                ->on('surveys')
                ->onDelete('cascade');
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
        Schema::drop('umsaetze');
    }
}
