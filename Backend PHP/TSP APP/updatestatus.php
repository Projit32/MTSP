<?php


$content=file_get_contents("php://input");

$decoded=json_decode($content,true);

if(!is_array($decoded)){
    throw new Exception('Received content contained invalid JSON!');
}

$name=$decoded['name'];
$lat=$decoded['lat'];
$lng=$decoded['lng'];
$status=$decoded['status'];
$stname="default";
$msg="";
$json="";
include 'connection.php';
$sql="SELECT `stassign` FROM labour WHERE `Labour_ID`='$name'";
$quer=$conn->query($sql);
if($quer)
{
	$row=mysqli_fetch_assoc($quer);
	$stname=$row['stassign'];
	$sql="UPDATE ".$stname."_attendance SET `lat`='$lat',`lng`='$lng',`status`='$status' WHERE `id`='$name'";

if($conn->query($sql))
{
	$msg="Status Updated Successfully";
	$json=array("Msg"=>$msg,"CODE"=>0);
}
else
{
	$msg=mysqli_error($conn);
	$json=array("Msg"=>$msg,"CODE"=>1);
}

}
else
{
	$msg=mysqli_error($conn);
	$json=array("Msg"=>$msg,"CODE"=>2);
}


@mysqli_close($conn);

$json=json_encode($json);
echo $json;


?>