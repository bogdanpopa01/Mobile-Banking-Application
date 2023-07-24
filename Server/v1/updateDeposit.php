<?php

require_once '../includes/DbOperations.php';
$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'PUT') {
    // Retrieve parameters from request body
    parse_str(file_get_contents("php://input"), $params);

    // Check if all required parameters are set
    if (isset($params['depositId']) && isset($params['depositName']) && isset($params['depositAmount']) && isset($params['depositPeriod']) && isset($params['depositInterestRate']) && isset($params['depositTimeLeft']) && isset($params['userId'])) {
        $db = new DbOperations();
        if ($db->updateDeposit($params['depositId'], $params['depositName'], $params['depositAmount'])) {
            $response['error'] = false;
            $response['message'] = "Deposit updated successfully";
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
