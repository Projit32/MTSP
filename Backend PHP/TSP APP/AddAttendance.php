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
include 'connection.php';
$msg="Inserted";
$crt=0;
foreach($decoded as $values)
{
	$id=$values['ID'];
    $name=$values['Name'];
    $staion=$values['stname'];
    $sql="UPDATE labour SET stassign ='$staion' WHERE Labour_ID='$id'";
    if($conn->query($sql))
    {
    	$sql="INSERT INTO ".$staion."_attendance(id, name) VALUES ('$id','$name')";
    	if($conn->query($sql))
    	{
    		$crt++;
    	}
    	else
    	{
    		$msg=mysqli_error($conn);
    	}
    	
    }
    else
    {
    	$msg=mysqli_error($conn);
    }

}

@mysqli_close($conn);
$json=array("Msg"=>$msg,"code"=>$crt);
$json=json_encode($json,JSON_PRETTY_PRINT);
echo $json;



?>