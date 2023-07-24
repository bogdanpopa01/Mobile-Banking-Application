<?php

require_once '../includes/DbOperations.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    if (isset($_GET['userId'])) {
        $db = new DbOperations();
        $userId = $_GET['userId'];
        $transfers = $db->getTransfersByUser($userId);
        if ($transfers) {
            $response['error'] = false;
            $response['transfers'] = array();
            foreach ($transfers as $transfer) {
                $response['transfers'][] = array(
                    'transferId' => $transfer['transferId'],
                    'transferAmount' => $transfer['transferAmount'],
                    'transferPayee' => $transfer['transferPayee'],
                    'transferIBAN' => $transfer['transferIBAN'],
                    'transferDescription' => $transfer['transferDescription'],
                    'userId' => $transfer['userId'],
                    'transferDate' => $transfer['transferDate']
                );
            }
        } else {
            $response['error'] = true;
            $response['message'] = 'No transfers found';
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
