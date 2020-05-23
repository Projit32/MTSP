<?php
$conn = new mysqli("localhost","root","","tspapp");

if($conn->connect_error)
{
	die($conn->connect_error);
}
?>