<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateBackOrdersTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('back_orders', function (Blueprint $table) {
            $table->increments('id');
            $table->integer('nachgefragt_in_runde');
            $table->integer('verkaufspreis');
            $table->integer('verbl_menge');
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
        Schema::drop('back_orders');
    }
}
