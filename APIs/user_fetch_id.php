<?php

include("db_info.php");

$UserID = $_GET["id"];
$UserListingsQuery = $mysqli->query("SELECT * FROM user WHERE id ='$UserID'");

while($row = mysqli_fetch_assoc($UserListingsQuery)){
    $arr[] = $row;
}

echo json_encode($arr);


?>