<?php 
include("db_info.php");

// User data that will be collected in order to modify user data
$ID = $_POST["ID"];
$Title = $_POST["Title"];
$Description = $_POST["Description"];
$Price = $_POST["Price"];
$Category = $_POST["Category"];

// Query to update user info with the inputted details
$UpdateListing = $mysqli->query("UPDATE listing SET title = '$Title', description = '$Description', price = '$Price', category = '$Category' WHERE id = '$ID'"); 

if($UpdateListing){
    echo "Listing info updated.";
}

?>