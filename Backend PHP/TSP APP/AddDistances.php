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

$from=$decoded['from'];
$to=$decoded['to'];
$dist=$decoded['dist'];
$stname=$decoded['station'];

$msg="";
include 'connection.php';

$sql="INSERT INTO ".$stname."_distances (origin,`to`,distance) VALUES ('$from','$to','$dist')";
if($conn->query($sql))
	{
		$msg="Data Inserted";
	}
	else
		$msg=mysqli_error($conn);

	@mysqli_close($conn);

$json=array("Msg"=>$msg,"code"=>0);
$json=json_encode($json,JSON_PRETTY_PRINT);
echo $json;


?>