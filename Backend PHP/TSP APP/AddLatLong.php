<?php

if(strcasecmp($_SERVER['REQUEST_METHOD'], 'POST') != 0){
    throw new Exception('Request method must be POST!');
}

$contentType = isset($_SERVER["CONTENT_TYPE"]) ? trim($_SERVER["CONTENT_TYPE"]) : '';
if(strcasecmp($contentType, 'application/json') != 0){
    throw new Exception('Content type must be: application/json');
}
 
//Receive the RAW post data.
$content = file_get_contents("php://input");
 
//Attempt to decode the incoming RAW post data from JSON.
$decoded = json_decode($content, true);

//If json_decode failed, the JSON is invalid
if(!is_array($decoded)){
    throw new Exception('Received content contained invalid JSON!');
}

$crt=0;
$msg="Data Inserted";
$json="";

include 'connection.php';
foreach ($decoded as $value) {
	$name=$value['location'];
	$name = addslashes($name);
	$lat=$value['lat'];
	$lng=$value['lng'];
	$stname=$value['station'];
	$sql="INSERT INTO ".$stname."_locations (name,lat,lng) VALUES ('$name','$lat','$lng')";
	if($conn->query($sql))
	{
		$crt++;
	}
	else
		$msg=mysqli_error($conn);

}
@mysqli_close($conn);
$json=array("Msg"=>$msg,"code"=>$crt);
$json=json_encode($json,JSON_PRETTY_PRINT);
echo $json;

?>