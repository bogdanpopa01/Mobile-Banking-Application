<?php

require_once '../includes/DbOperations.php';
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    if(isset($_POST['transactionId']) && isset($_POST['transactionName']) && isset($_POST['transactionAmount']) && isset($_POST['transactionDate']) && isset($_POST['transactionType']) && isset($_POST['userId'])){
        $db = new DbOperations();
        if($db->createTransaction($_POST['transactionId'], $_POST['transactionName'], $_POST['transactionAmount'], $_POST['transactionDate'], $_POST['transactionType'], $_POST['userId'])){
            $response['error'] = false;
            $response['message'] = "Transaction created successfully";
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