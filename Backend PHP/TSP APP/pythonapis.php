<?php

$stname=$_POST['station'];
$code=$_POST['code'];

include 'connection.php';
$msg=[];

$query=array("SELECT * FROM ".$stname."_distances","SELECT * FROM ".$stname."_locations","SELECT * FROM ".$stname."_attendance","SELECT * FROM ".$stname."_route");

if($code=='attendance')
{
	$res=$conn->query($query[2]);
	if ($res)
	{
		while($row=mysqli_fetch_assoc($res))
		{
			$msg[]=array("id"=>$row['id'],"name"=>$row['name'],"routeassign"=>$row['routeassign'],"lat"=>$row['lat'],"lng"=>$row['lng'],"status"=>$row['status']);
		}
	}
}
elseif ($code=='distances') {
	$res=$conn->query($query[0]);
	if ($res)
	{
		while($row=mysqli_fetch_assoc($res))
		{
			$msg[]=array("origin"=>$row['origin'],"to"=>$row['to'],"distance"=>$row['distance']);
		}
	}
}
elseif ($code=='location') {
	$res=$conn->query($query[1]);
	if ($res)
	{
		while($row=mysqli_fetch_assoc($res))
		{
			$msg[]=array("name"=>$row['name'],"lat"=>$row['lat'],"lng"=>$row['lng']);
		}
	}
}
elseif ($code=='route') {
	$res=$conn->query($query[3]);
	if ($res)
	{
		while($row=mysqli_fetch_assoc($res))
		{
			$msg[]=array("Name"=>$row['name'], "Route"=>$row['routes'],"Current"=>$row['current']);
		}
	}
}

echo(json_encode($msg,JSON_PRETTY_PRINT));

?>