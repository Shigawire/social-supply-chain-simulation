<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Back_Order extends Model
{
    protected $table = 'back_orders';

    protected $guarded = ['id', 'created_at', 'updated_at'];
}
