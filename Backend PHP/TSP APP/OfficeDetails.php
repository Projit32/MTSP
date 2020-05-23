<?php

include 'connection.php';

$sql="SELECT * FROM stations";

$query=$conn->query($sql);
while($row=mysqli_fetch_assoc($query))
{
	$msg[]=array("name"=>$row['name']);
}

 
header('content-type: application/json');
$jsonobj=json_encode($msg,JSON_PRETTY_PRINT);
echo $jsonobj;
$conn->close();

?>