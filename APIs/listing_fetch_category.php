<?php

include("db_info.php");

$Category = $_GET["category"];
$CategoryListingsQuery = $mysqli->query("SELECT * FROM listing WHERE category ='$Category'");

while($row = mysqli_fetch_assoc($CategoryListingsQuery)){
    $arr[] = $row;
}

echo json_encode($arr);


?>