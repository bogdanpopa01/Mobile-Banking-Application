<?php

require_once '../includes/DbOperations.php';
$response = array();

if($_SERVER['REQUEST_METHOD'] == 'GET'){
    if(isset($_GET['userName']) && isset($_GET['password'])){
        $db = new DbOperations();

        if($user = $db->getUserByCredentials($_GET['userName'],$_GET['password'])){
            $response['error'] = false;
            $response['userId'] = $user['userId'];
            $response['userName'] = $user['userName'];
            $response['email'] = $user['email'];
            $response['password'] = $user['password'];
            $response['telephone'] = $user['telephone'];
            $response['firstName'] = $user['firstName'];
            $response['lastName'] = $user['lastName'];
            $response['cardNumber'] = $user['cardNumber'];
            $response['cardCvv'] = $user['cardCvv'];
            $response['cardExpirationDate'] = $user['cardExpirationDate'];
            $response['balance'] = $user['balance'];
            $response['IBAN'] = $user['IBAN'];
        } else {
            $response['error'] = true;
            $response['message'] = 'Invalid username or password';
        }
    } else {
        $response['error'] = true;
        $response['message'] = 'Required fields are missing';
    }
}

echo json_encode($response);