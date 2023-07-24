<?php

require_once '../includes/DbOperations.php';
$response = array();

if($_SERVER['REQUEST_METHOD'] == 'DELETE'){
    if(isset($_GET['depositId'])){
        $db = new DbOperations();
        $result = $db->deleteDeposit($_GET['depositId']);
        if($result){
            $response['error'] = false;
            $response['message'] = "Deposit deleted successfully";
        } else {
            $response['error'] = true;
            $response['message'] = "Error deleting deposit";
        }
    } else {
        $response['error'] = true;
        $response['message'] = "Deposit ID not provided";
    }
} else {
    $response['error'] = true;
    $response['message'] = "Invalid request method";
}

echo json_encode($response);
