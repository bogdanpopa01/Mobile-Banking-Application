<?php

require_once '../includes/DbOperations.php';
$response = array();

if($_SERVER['REQUEST_METHOD'] == 'GET'){
    if(isset($_GET['transactionId'])){
        // intentionally left blank
    } else {
        $db = new DbOperations();
        $transactions = $db->getAllTransactions();
        echo json_encode($transactions);
    }
}