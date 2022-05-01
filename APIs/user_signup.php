<?php 
include("db_info.php");

// User data that will be collected in order to make a new account
$FullName = $_POST["FullName"];
$PhoneNumber = $_POST["PhoneNumber"];
$Address = $_POST["Address"];
$Email = $_POST["Email"];
$Username = $_POST["Username"];
$Password = $_POST["Password"];

$Id = rand(1, 999999);

// Check if the email is a valid email
if (!filter_var($Email, FILTER_VALIDATE_EMAIL)) {
    exit("$Email is not a valid email address");
  }

// Check for duplicate username or email
$UsernameQuery = $mysqli->query("SELECT username from user where username='$Username'");
$EmailQuery = $mysqli->query("SELECT username from user where email='$Email'");

// If returned rows is greater than 0 then duplicate exists
if($UsernameQuery->num_rows > 0){
    exit('Username taken. Try another one.');
}
if($EmailQuery->num_rows > 0){
    exit('Email taken. Try logging in with it if it belongs to you.');
}

// Hash the password 
$HashedPassword = password_hash($Password, PASSWORD_BCRYPT);

// Register the client to the Database
$FullName = ucwords($FullName);

// Query to register the new user with the inputted details
$RegisterUser = $mysqli->query("INSERT INTO user (id, name, phone_number, address, email, username, password) VALUES ('$Id','$FullName','$PhoneNumber','$Address','$Email','$Username','$HashedPassword')"); 

if($RegisterUser){
    echo "User registered";
}


?>