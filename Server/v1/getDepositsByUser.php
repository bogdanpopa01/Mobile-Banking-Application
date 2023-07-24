<?php

require_once '../includes/DbOperations.php';
$response = array();

if($_SERVER['REQUEST_METHOD'] == 'GET'){
    if(isset($_GET['userId'])){
        $db = new DbOperations();
        $deposits = $db->getDepositsByUser($_GET['userId']);
        if($deposits){
            $response['error'] = false;
            $response['deposits'] = array();
            foreach($deposits as $deposit){
                $response['deposits'][] = array(
                    'depositId' => $deposit['depositId'],
                    'depositName' => $deposit['depositName'],
                    'depositAmount' => $deposit['depositAmount'],
                    'depositPeriod' => $deposit['depositPeriod'],
                    'depositInterestRate' => $deposit['depositInterestRate'],
                    'depositTimeLeft' => $deposit['depositTimeLeft'],
                    'userId' => $deposit['userId']
                );
            }
        } else {
            $response['error'] = true;
            $response['message'] = 'No deposit found';
        }
    } else {
        $response['error'] = true;
        $response['message'] = 'Required fields are missing';
    }
}

echo json_encode($response);
