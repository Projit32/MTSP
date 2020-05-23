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

$n=$decoded['Name'];
$un=$decoded['Username'];
$pass=$decoded['Password'];
include 'connection.php';

$sql="SELECT * FROM labour WHERE 1";
$genID="LB";
if($conn->query($sql))
{
	$count=$conn->affected_rows;
	for($i=0;$i<5-strlen($count);$i++)
	{
		$genID=$genID."0";
	}

	$genID=$genID.($count+1);
}



$sql="INSERT INTO labour(name,username,pass,Labour_ID) VALUES ('$n','$un','$pass','$genID')";

if($conn->query($sql))
{
	$json=array("Msg"=>"Data Inserted","Name"=>"$n","ID"=>"$genID");
	$json = json_encode($json,JSON_PRETTY_PRINT);
	echo $json;
}
else
{
	echo mysqli_error($conn);
}
@mysqli_close($conn);







?>