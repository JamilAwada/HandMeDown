<?php 
include("db_info.php");

// User data that will be collected in order to make a new account
$Title = $_POST["Title"];
$Description = $_POST["Description"];
$Price = $_POST["Price"];
$Category = $_POST["Category"];
$Seller = $_POST["Seller"];
$Date = date("Y/m/d");

$Id = rand(1, 999999);


// Query to register the new user with the inputted details
$AddListing = $mysqli->query("INSERT INTO listing (id, title, description, price, category, seller, posted_on) VALUES ('$Id','$Title','$Description','$Price','$Category','$Seller','$Date')"); 

if($AddListing){
    echo "Listing added";
}

?>