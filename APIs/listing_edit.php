<?php 
include("db_info.php");

// User data that will be collected in order to modify listing data
$ID = $_POST["ID"];
$Title = $_POST["Title"];
$Description = $_POST["Description"];
$Price = $_POST["Price"];
$Category = $_POST["Category"];
$Picture = $_POST["Picture"];

// Query to update user info with the inputted details
$UpdateListingQuery = $mysqli->prepare("UPDATE listing SET title = '$Title', description = '$Description', price = '$Price', category = '$Category', picture = '$Picture' WHERE id = '$ID'"); 
$UpdateListingQuery->execute();

if($UpdateListingQuery){
    echo "Listing updated.";
}

?>