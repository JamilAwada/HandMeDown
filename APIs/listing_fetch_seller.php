<?php

include("db_info.php");

$UserID = $_GET["id"];
$Order = $_GET["order"];

if ($Order == "Newest"){
    $UserListingsQuery = $mysqli->query("SELECT * FROM listing WHERE seller ='$UserID' ORDER BY posted_on DESC");
}
else if ($Order == "Oldest"){
    $UserListingsQuery = $mysqli->query("SELECT * FROM listing WHERE seller ='$UserID' ORDER BY posted_on ASC");
}

$arr = [];

while($row = mysqli_fetch_assoc($UserListingsQuery)){
    $arr[] = $row;
}

if (empty($arr)){
    exit("0");
}

echo json_encode($arr);


?>