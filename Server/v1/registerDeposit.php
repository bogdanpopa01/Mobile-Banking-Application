<?php

require_once '../includes/DbOperations.php';
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    if(isset($_POST['depositId']) && isset($_POST['depositName']) && isset($_POST['depositAmount']) && isset($_POST['depositPeriod']) && isset($_POST['depositInterestRate']) && isset($_POST['depositTimeLeft']) && isset($_POST['userId'])){
        $db = new DbOperations();
        if($db->createDeposit($_POST['depositId'], $_POST['depositName'], $_POST['depositAmount'], $_POST['depositPeriod'], $_POST['depositInterestRate'], $_POST['depositTimeLeft'], $_POST['userId'])){
            $response['error'] = false;
            $response['message'] = "Deposit created successfully";
        } else {
            $response['error'] = true;
            $response['message'] = "Some error occurred. Please try again.";
        }
    } else {
        $response['error'] = true;
        $response['message'] = "Required fields are missing";
    }
} else {
    $response['error'] = true;
    $response['message'] = "Invalid Request";
}

echo json_encode($response);