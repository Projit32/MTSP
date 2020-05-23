<?php

$name=$_POST['name'];
$count=$_POST['count'];


include "connection.php";
$sql="SELECT `stassign` FROM labour WHERE `Labour_ID`='$name'";
$msg="";
$stname="";
$routename="";
$json="";
$quer=$conn->query($sql);
if($quer)
{
	$row=mysqli_fetch_assoc($quer);
	$stname=$row['stassign'];
	$sql2="SELECT `routeassign` FROM ".$stname."_attendance WHERE `id`='$name'";
	$quer2=$conn->query($sql2);
	if($quer2)
	{
		$row=mysqli_fetch_assoc($quer2);
		$routename=$row['routeassign'];
		
		$sql3="UPDATE ".$stname."_route SET `current`=$count WHERE name='$routename'";
		if($conn->query($sql3))
		{
			$msg="Inserted";
			

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
else
{
	$msg=mysqli_error($conn);

}
@mysqli_close($conn);

$json = array("Msg" =>$msg);
$json=json_encode($json,JSON_PRETTY_PRINT);
echo $json;

?>