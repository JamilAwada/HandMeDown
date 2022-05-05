<?php
include("db_info.php");

$ListingID = $_GET["ID"];

$DeleteListingQuery = $mysqli->prepare("DELETE FROM listing WHERE ID = '$ListingID'"); 
$DeleteListingQuery->execute();

if ($DeleteListingQuery){
    echo "Listing deleted";
}


?>