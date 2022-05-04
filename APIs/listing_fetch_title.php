<?php

include("db_info.php");

$Title = $_GET["title"];
$TitleListingsQuery = $mysqli->query("SELECT * FROM listing WHERE title LIKE '%$Title%'");

while($row = mysqli_fetch_assoc($TitleListingsQuery)){
    $arr[] = $row;
}

echo json_encode($arr);


?>