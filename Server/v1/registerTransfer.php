<?php

require_once '../includes/DbOperations.php';
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){

    if(isset($_POST['transferId']) and isset($_POST['transferAmount']) 
        and isset($_POST['transferPayee']) and isset($_POST['transferIBAN']) and isset($_POST['transferDescription'])
             and isset($_POST['userId']) and isset($_POST['transferDate'])){
                $db = new DbOperations();
                if($db->createTransfer($_POST['transferId'],$_POST['transferAmount'],$_POST['transferPayee']
            ,$_POST['transferIBAN'],$_POST['transferDescription'],$_POST['userId'],$_POST['transferDate'])){
                $response['error'] = false;
                $response['message'] = "Transfer registered successfully";
            } else{
                $response['error'] = true;
                $response['message'] = "Some error occurred. Please try again.";
            }
    } else {
        $response['error'] = true;
        $response['message'] = "Required fields are missing";
    }

} else {
    $response['error'] = true;
    $response['message'] = "Invalid Response";
}

echo json_encode($response);