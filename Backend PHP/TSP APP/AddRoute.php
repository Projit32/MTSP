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


$station=$decoded['station'];
$name=$decoded['name'];
$route=$decoded['route'];
$l_id=$decoded['l_id'];

include 'connection.php';

$sql="INSERT INTO ".$station."_route (name,routes,current) VALUES ('$name','$route',-1)";

if($conn->query($sql))
{
	$sql="UPDATE ".$station."_attendance SET `routeassign` = '$name' WHERE `id` = '$l_id'";
	
	if($conn->query($sql))
	{
		$msg="Success";
	}
	else
	{
		$msg=$sql;
	}
}
else
{
	$msg=$sql;
}

$conn->close();
$json=array("Msg"=>$msg);
$json=json_encode($json,JSON_PRETTY_PRINT);
echo $json;

?>