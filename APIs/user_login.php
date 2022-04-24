<?php 
include('db_info.php');

$Username = $_POST['Username'];
$Password = $_POST['Password'];
$FullName = "";
$PhoneNumber = "";
$Address = "";
$Email = "";

// Query to find pre-existing user with entered username
$UsernameQuery = $mysqli->query("SELECT username from user where username='$Username'");

// Check if there is an account with the entered username
if($UsernameQuery->num_rows == 0){
    $result = "User with entered username does not exist. Try again."; 
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
    $DetailsQuery = $mysqli->query("SELECT name, phone_number, address, email from user where username='$Username'");
    
    $FetchDetails = mysqli_fetch_assoc($DetailsQuery);
    $FullName = $FetchDetails['name'];
    $PhoneNumber = $FetchDetails['phone_number'];
    $Address = $FetchDetails['address'];
    $Email = $FetchDetails['email'];

    $SplitName = explode(" ", $FullName);
    $FullName = $SplitName[0];
    
    $Result = "Accepted";
}
else{
    $Result  = "Wrong password!";
}

}

$Response= array("status" =>$Result,"Name"=> $FullName,"PhoneNumber"=>$PhoneNumber,"Address"=>$Address,"Username"=>$Username, "Email"=>$Email, "Password"=>$Password);

echo json_encode($Response);  