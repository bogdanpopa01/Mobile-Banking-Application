<?php
    
    class DbOperations{
        private $con;
        function __construct(){
            require_once dirname(__FILE__).'/DbConnect.php';

            $db = new DbConnect();

            $this->con = $db->connect();
        }

        public function createTransfer($transferId,$transferAmount,$transferPayee,$transferIBAN,$transferDescription,$userId,$transferDate){
            $stmt = $this->con->prepare("INSERT INTO `transfers` (`transferId`, `transferAmount`, `transferPayee`, `transferIBAN`, `transferDescription`, `userId`,`transferDate`) VALUES (?, ?, ?, ?, ?, ?, ?);");
            $stmt->bind_param("sdsssss", $transferId, $transferAmount, $transferPayee, $transferIBAN, $transferDescription, $userId,$transferDate);

            if($stmt->execute()){
                return true;
            } else {
                return false;
            }
        }

        public function createDeposit($depositId, $depositName, $depositAmount, $depositPeriod, $depositInterestRate, $depositTimeLeft, $userId){
            $stmt = $this->con->prepare("INSERT INTO `deposits` (`depositId`,`depositName`,`depositAmount`,`depositPeriod`,`depositInterestRate`,`depositTimeLeft`,`userId`) VALUES (?, ?, ?, ?, ?, ?, ?);");
            $stmt->bind_param("ssdisss", $depositId, $depositName, $depositAmount, $depositPeriod, $depositInterestRate, $depositTimeLeft, $userId);
            if($stmt->execute()){
                return true;
            } else {
                return false;
            }
        }

        public function createTransaction($transactionId, $transactionName, $transactionAmount, $transactionDate, $transactionType, $userId){
            $stmt = $this->con->prepare("INSERT INTO `transactions` (`transactionId`,`transactionName`,`transactionAmount`,`transactionDate`,`transactionType`,`userId`) VALUES (?, ?, ?, ?, ?, ?);");
            $stmt->bind_param("ssdsss", $transactionId, $transactionName, $transactionAmount, $transactionDate, $transactionType, $userId);
            if($stmt->execute()){
                return true;
            } else {
                return false;
            }
        }

        public function getTransferById($transferId){
            $stmt = $this->con->prepare("SELECT * FROM transfers WHERE transferId = ?");
            $stmt->bind_param("s",$transferId);
            $stmt->execute();
            $result = $stmt->get_result();
            if($result->num_rows == 0){
                return null;
            } else {
                return $result->fetch_assoc();
            }
        }

        public function getUserByCredentials($userName, $password){
            $stmt = $this->con->prepare("SELECT * FROM users WHERE userName = ? and password = ?");
            $stmt->bind_param("ss",$userName,$password);
            $stmt->execute();
            $result = $stmt->get_result();
            if($result->num_rows == 0){
                return null;
            } else {
                return $result->fetch_assoc();
            }
        }

        public function getDepositsByUser($userId){
            $stmt = $this->con->prepare("SELECT * FROM deposits WHERE userId = ?");
            $stmt->bind_param("s",$userId);
            $stmt->execute();
            $result = $stmt->get_result();
            if($result->num_rows == 0){
                return null;
            } else {
                return $result->fetch_all(MYSQLI_ASSOC);
            }
        }

        public function getTransfersByUser($userId){
            $stmt = $this->con->prepare("SELECT * FROM transfers WHERE userId = ?");
            $stmt->bind_param("s",$userId);
            $stmt->execute();
            $result = $stmt->get_result();
            if($result->num_rows == 0){
                return null;
            } else {
                return $result->fetch_all(MYSQLI_ASSOC);
            }
        }

        public function getTransactionsByUser($userId){
            $stmt = $this->con->prepare("SELECT * FROM transactions WHERE userId = ?");
            $stmt -> bind_param("s",$userId);
            $stmt->execute();
            $result = $stmt->get_result();
            if($result->num_rows == 0){
                return null;
            } else {
                return $result->fetch_all(MYSQLI_ASSOC);
            }
        }
        
        public function getAllTransfers() {
            $stmt = $this->con->prepare("SELECT * FROM transfers");
            $stmt->execute();
            $transfers = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
            $stmt->close();
            return $transfers;
        }

        public function getAllDeposits(){
            $stmt = $this->con->prepare("SELECT * FROM deposits");
            $stmt->execute();
            $deposits = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
            $stmt->close();
            return $deposits;
        }

        public function getAllTransactions(){
            $stmt = $this->con->prepare("SELECT * FROM transactions");
            $stmt->execute();
            $transactions = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
            $stmt->close();
            return $transactions;
        }

        public function deleteDeposit($depositId){
            $stmt = $this->con->prepare("DELETE FROM deposits WHERE depositId = ?");
            $stmt->bind_param("s", $depositId);
            $result = $stmt->execute();
            $stmt->close();
            return $result;
        }

        public function updateDeposit($depositId, $depositName, $depositAmount){
            $stmt = $this->con->prepare("UPDATE deposits SET depositName = ?, depositAmount = ? WHERE depositId = ?");
            $stmt->bind_param("sds", $depositName, $depositAmount, $depositId);
            $result = $stmt->execute();
            $stmt->close();
            return $result;
        }

        public function updateUser($userId, $balance) {
            $stmt = $this->con->prepare("UPDATE users SET balance = ? WHERE userId = ?");
            $stmt->bind_param("ds", $balance, $userId);
            $result = $stmt->execute();
            $stmt->close();
            return $result;
        }
        
    }