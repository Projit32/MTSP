<?php

include "connection.php";
$stname=$_POST['stname'];

$sql="SELECT * FROM ".$stname."_attendance";

$query=$conn->query($sql);

if($query)
{
	while($row=mysqli_fetch_assoc($query))
	{
		$rt=$row['routeassign'];
		$SQL ="SELECT * FROM ".$stname."_route WHERE name='$rt'";
		$quer=$conn->query($SQL);
		$RouteRow=mysqli_fetch_assoc($quer);
		$msg[]=array("id"=>$row['id'],"name"=>$row['name'],"routeassign"=>$row['routeassign'],"lat"=>$row['lat'],"lng"=>$row['lng'],"status"=>$row['status'],"Route"=>$RouteRow['routes'],"at"=>$RouteRow['current']);
	}
}
else
{
	$msg=mysqli_error($conn);
}



$json=json_encode($msg,JSON_PRETTY_PRINT);
	echo $json;

?>