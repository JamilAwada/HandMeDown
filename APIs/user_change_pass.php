<?php 
include("db_info.php");

// Old inputted password to authorize change
$ID = $_POST["ID"];
$OldPassword = $_POST["OldPassword"];
$NewPassword = $_POST["NewPassword"];

// Fetch old password
$PasswordQuery = $mysqli->query("SELECT password from user where id = '$ID'");
$FetchHashedPassword = mysqli_fetch_assoc($PasswordQuery);
$OldHashedPassword = $FetchHashedPassword["password"];

if (password_verify($OldPassword, $OldHashedPassword)){
    // Hash the password 
    $NewHashedPassword = password_hash($NewPassword, PASSWORD_BCRYPT);
    // Query to register the new user with the inputted details
    $ChangePassword = $mysqli->query("UPDATE user SET password = '$NewHashedPassword' WHERE id = '$ID'"); 

    if($ChangePassword){
    echo "Password changed.";
    }

}

else {
    echo "Wrong password.";
}



?>