<?php

include 'connection.php'

$lat=$_POST['lat'];
$lng=$_POST['lng'];
$stname=$_POST['stname'];

$sql2="SELECT `name` FROM ".$stname."_locations WHERE `lat`=$lat AND `lng`=$lng";

$quer=$conn->query($sql2);

$row=mysqli_fetch_assoc($quer);
$name=$row['name'];

$sql=array("DELETE FROM ".$stname."_locations WHERE `lat`='$lat' and `lng`='$lng';","DELETE FROM ".$stname."_distances WHERE `origin`='$name' OR `to`='$name';")
$msg=""
$crt=0;
foreach ($sql as $item) 
{
	if($conn->query($item))
	{
		$msg="DELETED FROM TABLE";
		$crt++;
	}
	else
		$msg=mysqli_error($conn);

	$json=array("Msg"=>$msg,"CODE"=>$crt);
	$json=json_encode($json,JSON_PRETTY_PRINT);
	echo $json;
}
@mysqli_close($conn);
?>