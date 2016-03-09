<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Umsatz extends Model
{
    protected $table = 'umsaetze';

    protected $guarded = ['id', 'created_at', 'updated_at'];
}
