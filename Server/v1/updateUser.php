<?php

require_once '../includes/DbOperations.php';
$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'PUT') {
    // Retrieve parameters from the URL
    $userId = $_GET['userId'] ?? '';
    $balance = $_GET['balance'] ?? '';

    // Check if both parameters are present
    if (!empty($userId) && !empty($balance)) {
        $db = new DbOperations();
        if ($db->updateUser($userId, $balance)) {
            $response['error'] = false;
            $response['message'] = "User updated successfully";
        } else {
            $response['error'] = true;
            $response['message'] = "Some error occurred. Please try again.";
        }
    } else {
        $response['error'] = true;
        $response['message'] = "Required fields are missing";
    }
} else {
    $response['error'] = true;
    $response['message'] = "Invalid Request";
}

echo json_encode($response);
