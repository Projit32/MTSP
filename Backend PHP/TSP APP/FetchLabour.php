<?php
include 'connection.php';

$id=$_POST['id'];

$sql="SELECT * FROM labour WHERE Labour_ID LIKE 'LB__".$id."%'";

$json=[];

if($qur=$conn->query($sql))
{
	while($row=mysqli_fetch_assoc($qur))
	{
		$json[]=array("Name"=>$row['name'],"ID"=>$row['Labour_ID']);
	}
}
else
{
	$json[]="Not Found";
}
@mysqli_close($conn);
$jsonObj=json_encode($json,JSON_PRETTY_PRINT);
echo $jsonObj;

?>