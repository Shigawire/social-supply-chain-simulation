<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Lieferung extends Model
{
    protected $table = 'lieferungen';

    protected $guarded = ['id', 'created_at', 'updated_at'];


}
