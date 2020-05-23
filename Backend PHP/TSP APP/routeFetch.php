<?php

$username=$_POST['username'];
$password=$_POST['password'];

include "connection.php";

$sql="SELECT `Labour_ID`,`stassign` FROM labour WHERE `username`='$username' AND `pass`='$password'";
$msg="";

$stname="";
$routename="";
$json="";
$name="default";
$quer=$conn->query($sql);
if($quer)
{
	$row=mysqli_fetch_assoc($quer);

	$name=$row['Labour_ID'];
	$stname=$row['stassign'];
	$sql2="SELECT `routeassign` FROM ".$stname."_attendance WHERE `id`='$name'";
	$quer2=$conn->query($sql2);
	if(empty($name))
	{
		$va=mysqli_error($conn);
		$msg=json_encode(array("Name"=>$va, "Route"=>"ERROR"));
     		echo $msg;
	}
	else
	{
	if($quer2)
	{
		$row=mysqli_fetch_assoc($quer2);
		$routename=$row['routeassign'];
		$sql3="SELECT * FROM ".$stname."_route WHERE `name`='$routename'";
		$quer3=$conn->query($sql3);
		if($quer3)
		{
			$row=mysqli_fetch_assoc($quer3);
			$json=array("Name"=>$row['name'], "Route"=>$row['routes'],"Lname"=>$name,"Current"=>$row['current']);
			$json=json_encode($json,JSON_PRETTY_PRINT);
			echo $json;
		}
		else
		{
			$msg=mysqli_error($conn);
			$msg=json_encode(array("Name"=>$msg, "Route"=>"ERROR"));
     		echo $msg;
		}
	}
	else
	{
		$msg=mysqli_error($conn);
		$msg=json_encode(array("Name"=>"No Routes Assigned", "Route"=>"ERROR"));
		echo $msg;
	}
}
}
else
{
	$msg=mysqli_error($conn);
	$msg=json_encode(array("Name"=>$msg, "Route"=>"ERROR"));
	echo $msg;
}
@mysqli_close($conn);

?>