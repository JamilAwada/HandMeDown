<?php
include("db_info.php");

$ListingID = $_GET["ID"];

$DeleteListingQuery = $mysqli->query("DELETE FROM listing WHERE ID = '$ListingID'"); 

if ($DeleteListingQuery){
    echo "Listing deleted";
}


?>