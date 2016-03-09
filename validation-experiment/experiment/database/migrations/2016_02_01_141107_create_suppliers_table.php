<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateSuppliersTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('suppliers', function (Blueprint $table) {
            $table->increments('id');
            $table->string('number');
            $table->string('preis');
            $table->string('lieferzeit');
            $table->string('angegebene_lieferzeit');
            $table->string('vertrauen');
            $table->string('competence');
            $table->string('quality');
            $table->string('reliability');
            $table->string('shared_values');
            $table->string('step');

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
        Schema::drop('suppliers');
    }
}
