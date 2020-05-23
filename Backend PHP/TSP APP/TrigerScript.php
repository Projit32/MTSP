<?php
$st=$_POST['name'];
#!C:\Python27\python.exe
ini_set('max_execution_time', 1000);
include "connection.php";
$start = microtime(true);
$command=exec("d: && cd D:\\Program Files\\XAMPP\\htdocs\\TSP APP\\APP MTSP && python connection.py ".$st);
echo $command;

$msg="Done Calculating : Time Taken is :";
$end = microtime(true);
$time=array("Msg"=>$msg,"code"=>($end-$start));
$time=json_encode($time,JSON_PRETTY_PRINT);
echo $time;
?>