<?php 
include("db_info.php");

// User data that will be collected in order to modify user data
$ID = $_POST["ID"];
$Name = $_POST["Name"];
$Number = $_POST["Number"];
$Address = $_POST["Address"];
$Email = $_POST["Email"];
$Username = $_POST["Username"];
$Picture = $_POST["Picture"];

// Check if the email is a valid email
if (!filter_var($Email, FILTER_VALIDATE_EMAIL)) {
    exit("$Email is not a valid email address");
  }

// Check for duplicate username or email
$UsernameQuery = $mysqli->prepare("SELECT username from user where username='$Username'");
$UsernameQuery->execute();


// If returned rows is greater than 0 then duplicate exists
if($UsernameQuery->get_result()->num_rows > 1){
    exit('Username taken. Try another one.');
}

$EmailQuery = $mysqli->prepare("SELECT username from user where email='$Email'");
$EmailQuery->execute();

if($EmailQuery->get_result()->num_rows > 1){
    exit('Email taken. Try another one.');
}


// Query to update user info with the inputted details
$UpdateUser = $mysqli->query("UPDATE user SET name = '$Name', number = '$Number', address = '$Address', username = '$Username', email = '$Email', picture ='$Picture' WHERE id = '$ID'"); 

if($UpdateUser){
    echo "User info updated.";
}

?>