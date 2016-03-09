<?php

use App\User;

Route::get('/', 'GetSurveyController@getSurvey');
Route::post('/', 'PostSurveyController@postSurvey');
Route::get('/week/1', 'GetSurveyController@getWeek1');
Route::post('/week/1', 'PostSurveyController@processWeek1');
Route::get('/week/X', 'GetSurveyController@getWeekX');
Route::post('/week/X', 'PostSurveyController@processWeekX');

Route::get('/week/18', 'GetSurveyController@getWeek18');
Route::get('/week/31', 'GetSurveyController@getEndBericht');

Route::get('/week/X2', 'GetSurveyController@getWeekX2');
Route::post('/week/X2', 'PostSurveyController@processWeekX2');

Route::get('/frage', 'GetSurveyController@get99Punkte');
Route::post('/frage', 'PostSurveyController@postFrage');

Route::get('Teil2', 'GetSurveyController@getTeil2');

Route::get('/frage2', 'GetSurveyController@get100Punkte');
Route::post('/frage2', 'PostSurveyController@postFrage2');

Route::get('/bewertung', 'GetSurveyController@getBewertung');
Route::post('/bewertung', 'PostSurveyController@postBewertung');

Route::get('highscore', 'GetSurveyController@getHighscore');
Route::get('ende', 'GetSurveyController@getEndFragen');
Route::post('ende', 'PostSurveyController@postEndFragen');


Route::get('/done', 'GetSurveyController@getSurveyDone');

Route::get('login', function() {
    return view('login');
});

// create a new user
Route::get('newUser', function()
{
    User::create([
        'email' => 'social',
        'password' => Hash::make('trust')
    ]);

    return 'User created';
});

Route::controllers([
    '/' => 'Auth\AuthController',
]);