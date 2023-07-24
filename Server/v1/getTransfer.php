<?php

require_once '../includes/DbOperations.php';
$response = array();

if($_SERVER['REQUEST_METHOD'] == 'GET'){
    if(isset($_GET['transferId'])){
        $db = new DbOperations();

        if($transfer = $db->getTransferById($_GET['transferId'])){
            $response['error'] = false;
            $response['transferId'] = $transfer['transferId'];
            $response['transferAmount'] = $transfer['transferAmount'];
            $response['transferPayee'] = $transfer['transferPayee'];
            $response['transferIBAN'] = $transfer['transferIBAN'];
            $response['transferDescription'] = $transfer['transferDescription'];
            $response['userId'] = $transfer['userId'];
            $response['transferDate'] = $transfer['transferDate'];
        } else {
            $response['error'] = true;
            $response['message'] = 'Invalid transferId';
        }
    } else {
        $response['error'] = true;
        $response['message'] = 'Required fields are missing';
    }
}


echo json_encode($response);