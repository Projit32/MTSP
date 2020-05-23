<?php
include 'connection.php';
$stname=strtolower($_POST['station']);
$sql=array("CREATE TABLE IF NOT EXISTS ".$stname."_locations(    `name` text,    `lat` REAL,    `lng` REAL);","CREATE TABLE IF NOT EXISTS ".$stname."_distances(    `origin` text,    `to` text,    `distance` REAL);","CREATE TABLE IF NOT EXISTS ".$stname."_route(    `name` text,    `routes` text, `current` INT(5));","CREATE TABLE IF NOT EXISTS ".$stname."_attendance(id VARCHAR(20) NOT NULL UNIQUE, name text,lat REAL,lng REAL,status text,routeassign text);","INSERT INTO stations (name) VALUES ('$stname')");
$crt=0;
if(!(is_null($stname)))
{
$msg="Tables Created";
foreach($sql as $item)
{
	if($conn->query($item))
	{
		$crt++;
	}
	else
	{
		$msg=mysqli_error($conn);
	}
} 
}
else
{
	$msg="EMPTY STATION FIELD";
}
$json = array('Msg' =>$msg,'code'=>$crt );
$json=json_encode($json,JSON_PRETTY_PRINT);
echo $json;


?>