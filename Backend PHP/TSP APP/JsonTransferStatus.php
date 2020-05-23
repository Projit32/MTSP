<?php 

if($_SERVER["REQUEST_METHOD"]=="POST")
{
$name=$_POST['name'];
$lat=$_POST['lat'];
$lng=$_POST['lng'];
$status=$_POST['status'];

	$json=array("name"=>$name,"lat"=> $lat,"lng"=>$lng,"status"=>$status);
	$json=json_encode($json);

	//echo $json;

	$ch = curl_init('http://127.0.0.1/TSP%20APP/updatestatus.php');


	curl_setopt($ch, CURLOPT_POST, 1);
 
//Attach our encoded JSON string to the POST fields.
curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
 

curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json')); 
 
//Execute the request
$result = curl_exec($ch);
}




?>