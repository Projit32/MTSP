<?php
include 'connection.php';

$stname=$_POST['stname'];

$sql = array("TRUNCATE TABLE ".$stname."_locations;","TRUNCATE TABLE ".$stname."_route;","TRUNCATE TABLE ".$stname."_distances;","TRUNCATE TABLE ".$stname."_attendance"); 

/*

*/
foreach($sql as $item)
{
	if($conn->query($item))
{
	echo "Deletion Successfull <br>";
}
else
{
	echo mysqli_error($conn);
}
}
?>