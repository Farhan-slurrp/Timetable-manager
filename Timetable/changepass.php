<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['email']) && isset($_POST['password'])) {
    if ($db->dbConnect()) {
        if ($db->changePassword("users", $_POST['email'], $_POST['password'])) {
            echo "Password Changed Successfully";
        } else echo "Email is wrong";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>