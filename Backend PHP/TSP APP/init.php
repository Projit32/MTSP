<?php

include 'connection.php';

$queries =array("CREATE TABLE labour( name text, username text, pass text, Labour_ID text PRIMARY KEY, stassign text)","CREATE TABLE stations (name VARCHAR(30) PRIMARY KEY)");

$crt=0;
foreach($queries as $item)
{
	if($conn->query($item))
	{
		$crt++;
	}
	else
	{
		$msg=mysqli_error($conn);
	}
}

print($crt." queries executed... init complete")

?>