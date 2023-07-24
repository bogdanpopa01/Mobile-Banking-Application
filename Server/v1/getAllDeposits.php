<?php

require_once '../includes/DbOperations.php';
$response = array();

if($_SERVER['REQUEST_METHOD'] == 'GET'){
    if(isset($_GET['depositId'])){
        // intentionally left blank
    } else {
        $db = new DbOperations();
        $deposits = $db->getAllDeposits();
        echo json_encode($deposits);
    }
}