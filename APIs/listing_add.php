<?php 
include("db_info.php");

// User data that will be collected in order to make a new account
$Title = $_POST["Title"];
$Description = $_POST["Description"];
$Price = $_POST["Price"];
$Category = $_POST["Category"];
$Seller = $_POST["Seller"];
$Picture = $_POST["Picture"];
$Date = date("Y/m/d");

$Id = rand(1, 999999);

$FindUserNameQuery = $mysqli->query("SELECT name FROM user WHERE id = '$Seller'");
$FetchUserName = mysqli_fetch_assoc($FindUserNameQuery);
$FetchedName = $FetchUserName['name'];


// Query to register the new user with the inputted details
$AddListing = $mysqli->query("INSERT INTO listing (id, title, description, price, category, seller, posted_on, seller_name, picture) VALUES ('$Id','$Title','$Description','$Price','$Category','$Seller','$Date', '$FetchedName', '$Picture')"); 

if($AddListing){
    echo "Listing added";
}

?>