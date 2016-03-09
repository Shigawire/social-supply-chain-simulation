<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateLieferungenTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('lieferungen', function (Blueprint $table) {
            $table->increments('id');
            $table->integer('verbl_lieferzeit');
            $table->integer('bestellmenge');
            $table->integer('gelieferte_menge')->nullable();
            $table->integer('quality')->nullable();
            $table->integer('bestellt_in_runde');
            $table->integer('preis');
            $table->integer('lieferzeit');
            $table->integer('lieferzeit_laut_supplier');
            $table->integer('supplier')->unsigned();
            $table->foreign('supplier')
                ->references('id')
                ->on('suppliers')
                ->onDelete('cascade');
            $table->integer('competence_bewertung')->nullable();
            $table->integer('quality_bewertung')->nullable();
            $table->integer('reliability_bewertung')->nullable();
            $table->integer('competence_differenz')->nullable();
            $table->integer('quality_differenz')->nullable();
            $table->integer('reliability_differenz')->nullable();
            $table->integer('besteller')->unsigned();
            $table->foreign('besteller')
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
        Schema::drop('lieferungen');
    }
}
