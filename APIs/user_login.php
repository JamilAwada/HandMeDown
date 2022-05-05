<?php 
include('db_info.php');

$Username = $_POST['Username'];
$Password = $_POST['Password'];
$ID = "";
$Name = "";
$Number = "";
$Address = "";
$Email = "";
$Picture = "";



// Query to find pre-existing user with entered username
$UsernameQuery = $mysqli->query("SELECT username from user where username='$Username'");

// Check if there is an account with the entered username
if($UsernameQuery->num_rows == 0){
    $Result = "User with entered username does not exist. Try again."; 
}
else{
// Get the hashed password of the account from the database
$PasswordQuery = $mysqli->query("SELECT password from user where username='$Username'");
$FetchHashedPassword = mysqli_fetch_assoc($PasswordQuery);
$HashedPassword = $FetchHashedPassword["password"];

// Check if the password matches
$CheckPassword = password_verify($Password,$HashedPassword);

if($CheckPassword){

    // Get user details to display on the "Profile" fragment
    $DetailsQuery = $mysqli->query("SELECT id, name, number, address, username, email, picture from user where username = '$Username'");
    
    $FetchDetails = mysqli_fetch_assoc($DetailsQuery);
    $ID = $FetchDetails['id'];
    $Name = $FetchDetails['name'];
    $Number = $FetchDetails['number'];
    $Address = $FetchDetails['address'];
    $Email = $FetchDetails['email'];
    $Picture = $FetchDetails['picture'];

    $Result = "Welcome";
}
else{
    $Result  = "Unable to log in. Check password or verify internet connectivity.";
}

}

$Response = array("status" =>$Result, "id" => $ID, "name"=> $Name,"number"=>$Number,"address"=>$Address,"username"=>$Username, "email"=>$Email, "password"=>$Password, "picture"=>$Picture);

echo json_encode($Response);  