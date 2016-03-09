
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>Experiment "Social Simulation in Supply Chains"</title>

    <!-- Bootstrap core CSS -->
    {!! HTML::style('bootstrap/css/bootstrap.min.css') !!}

            <!-- Custom styles for this template -->
    <link href="startseite.css" rel="stylesheet">
    {!! HTML::style('css/startseite.css') !!}

</head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Experiment "Social Simulation in Supply Chains"</a>
        </div>

        <ul class="nav navbar-nav navbar-right">
            <li><a href="{{ url('logout') }}">Logout</a></li>
        </ul>
    </div>
</nav>

<div class="container">
    <div class="col-lg-8">
        @include('errors.list')
        <h1>Frage zu Trust Dimensionen</h1>
        <p>Bitte geben Sie an, wie wichtig Ihnen die 4 Trust Dimensionen Competence, Reliability, Quality und Shared Values in den letzten 12 Wochen war.</p>
        <p>Verteilen Sie dazu insgesamt 100 Punkte auf die 4 Werte.</p>
        {!! Form::model($survey) !!}
            <table class="table table-striped table-bordered">
                <tr>
                    <th>Competence</th>
                    <th>Reliability</th>
                    <th>Quality</th>
                    <th>Shared Values</th>
                </tr>
                <tr>
                    <td>{!! Form::text('competence', null, ['class' => 'form-control']) !!}</td>
                    <td>{!! Form::text('reliability', null, ['class' => 'form-control']) !!}</td>
                    <td>{!! Form::text('quality', null, ['class' => 'form-control']) !!}</td>
                    <td>{!! Form::text('shared_values', null, ['class' => 'form-control']) !!}</td>
                </tr>
            </table>
            <button type="submit" class="btn btn-primary">Weiter</button>
        {!! Form::close() !!}
    </div>



</div><!-- /.container -->


<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
{!! HTML::script('jquery/jquery.js') !!}
<script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>')</script>
{!! HTML::script('bootstrap/js/bootstrap.min.js') !!}
</body>
</html>
