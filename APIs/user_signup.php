<?php 
include("db_info.php");

// User data that will be collected in order to make a new account
$Name = $_POST["Name"];
$Number = $_POST["Number"];
$Address = $_POST["Address"];
$Email = $_POST["Email"];
$Username = $_POST["Username"];
$Password = $_POST["Password"];

$ID = rand(1, 999999);

// Check if the email is a valid email
if (!filter_var($Email, FILTER_VALIDATE_EMAIL)) {
    exit("$Email is not a valid email address");
  }

// Check for duplicate username or email
$UsernameQuery = $mysqli->query("SELECT username from user where username='$Username'");
$EmailQuery = $mysqli->query("SELECT username from user where email='$Email'");
$NumberQuery = $mysqli->query("SELECT username from user where number='$Number'");

// If returned rows is greater than 0 then duplicate exists
if($UsernameQuery->num_rows > 0){
    exit('Username taken. Try another one.');
}
if($EmailQuery->num_rows > 0){
    exit('Email taken. Try logging in with it if it belongs to you.');
}
if($NumberQuery->num_rows > 0){
    exit('Phone number already in use.');
}

// Hash the password 
$HashedPassword = password_hash($Password, PASSWORD_BCRYPT);

// Register the client to the Database
$Name = ucwords($Name);

// Query to register the new user with the inputted details
$RegisterUser = $mysqli->query("INSERT INTO user (id, name, number, address, email, username, password) VALUES ('$ID','$Name','$Number','$Address','$Email','$Username','$HashedPassword')"); 

if($RegisterUser){
    echo "User registered";
}


?>