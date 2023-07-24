<?php

require_once '../includes/DbOperations.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    if (isset($_GET['userId'])) {
        $db = new DbOperations();
        $userId = $_GET['userId'];
        $transactions = $db->getTransactionsByUser($userId);
        if ($transactions) {
            $response['error'] = false;
            $response['transactions'] = array();
            foreach ($transactions as $transaction) {
                $response['transactions'][] = array(
                    'transactionId' => $transaction['transactionId'],
                    'transactionName' => $transaction['transactionName'],
                    'transactionAmount' => $transaction['transactionAmount'],
                    'transactionDate' => $transaction['transactionDate'],
                    'transactionType' => $transaction['transactionType'],
                    'userId' => $transaction['userId'],
                );
            }
        } else {
            $response['error'] = true;
            $response['message'] = 'No transaction found';
        }
    } else {
        $response['error'] = true;
        $response['message'] = 'Missing user ID parameter';
    }
} else {
    $response['error'] = true;
    $response['message'] = 'Invalid request method';
}

echo json_encode($response);
