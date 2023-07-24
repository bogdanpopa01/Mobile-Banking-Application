<?php

require_once '../includes/DbOperations.php';
$response = array();

if($_SERVER['REQUEST_METHOD'] == 'GET'){
    if(isset($_GET['transferId'])){
        // intentionally left blank
    } else {
        $db = new DbOperations();
        $transfers = $db->getAllTransfers();
        echo json_encode($transfers);
    }
}