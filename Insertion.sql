-- Use the database
USE StockTradingDB;

-----------------------------------------------------------
-- Insertion code for each table with sample data
-----------------------------------------------------------

-- 1. Inserting into User table
INSERT INTO User (User_ID, Name, Email, Password, Created_Date, Contact_Info, User_Type, Annual_Income)
VALUES 
  (1, 'Raj Kumar', 'raj.kumar@example.com', 'raj123', '2025-01-15', '9123456780', 'Retail', 550000.00),
  (2, 'Amit Sharma', 'amit.sharma@example.com', 'amit123', '2025-02-20', '9876543210', 'Retail', 720000.00),
  (3, 'Sita Patel', 'sita.patel@example.com', 'sita123', '2025-03-10', '9988776655', 'Institutional', 850000.00),
  (4, 'Neha Singh', 'neha.singh@example.com', 'neha123', '2025-01-05', '9123987654', 'Retail', 640000.00),
  (5, 'Vijay Reddy', 'vijay.reddy@example.com', 'vijay123', '2025-04-01', '9876512340', 'Retail', 910000.00),
  (6, 'Priya Joshi', 'priya.joshi@example.com', 'priya123', '2025-02-28', '9988123456', 'Institutional', 1200000.00),
  (7, 'Manish Gupta', 'manish.gupta@example.com', 'manish123', '2025-03-15', '9123678901', 'Retail', 480000.00),
  (8, 'Anjali Verma', 'anjali.verma@example.com', 'anjali123', '2025-01-25', '9876098765', 'Retail', 730000.00),
  (9, 'Sanjay Mehta', 'sanjay.mehta@example.com', 'sanjay123', '2025-04-10', '9988001122', 'Retail', 670000.00),
  (10, 'Rohit Desai', 'rohit.desai@example.com', 'rohit123', '2025-02-15', '9123001122', 'Retail', 800000.00);

-----------------------------------------------------------
-- 2. Inserting into Institution table (for users with institutional affiliations)
INSERT INTO Institution (Institution_ID, Institution_Type, Reg_Num, User_ID)
VALUES
  (1, 'Mutual Fund', 'MF-1001', 3),
  (2, 'Insurance', 'INS-2001', 6),
  (3, 'Investment Bank', 'IB-3001', 3);

-----------------------------------------------------------
-- 3. Inserting into Portfolio table
INSERT INTO Portfolio (Portfolio_ID, Portfolio_Name, Creation_Date, Total_Value, User_ID)
VALUES
  (1, 'Retirement Savings', '2025-02-01', 100000.00, 1),
  (2, 'Growth Portfolio', '2025-03-05', 150000.00, 2),
  (3, 'Dividend Portfolio', '2025-02-20', 200000.00, 4),
  (4, 'Bluechip Holdings', '2025-03-15', 250000.00, 5),
  (5, 'Aggressive Investments', '2025-04-05', 120000.00, 8);

-----------------------------------------------------------
-- 4. Inserting into Stock table (using popular Indian companies)
INSERT INTO Stock (Stock_ID, Market_Price, Market_Cap, Name, Volatility)
VALUES
  (101, 1200.50, 7500000000, 'Tata Motors', 2.5),
  (102, 2500.75, 4500000000, 'Reliance Industries', 1.8),
  (103, 800.20, 3000000000, 'Infosys', 1.2),
  (104, 1800.00, 5500000000, 'HDFC Bank', 1.5),
  (105, 1500.30, 5000000000, 'ICICI Bank', 1.3),
  (106, 2200.40, 6800000000, 'Larsen & Toubro', 2.0),
  (107, 350.80, 2100000000, 'Sun Pharma', 2.8),
  (108, 900.10, 2600000000, 'Bharti Airtel', 1.9);

-----------------------------------------------------------
-- 5. Inserting into Portfolio_Stock table 
-- Assuming some portfolios contain multiple stocks; purchase price and quantity are sample values.
INSERT INTO Portfolio_Stock (Portfolio_ID, Stock_ID, Current_Value, Purchase_Price, Quantity)
VALUES
  (1, 101, 1200.50 * 10, 1180.00, 10),
  (1, 103, 800.20 * 15, 790.00, 15),
  (2, 102, 2500.75 * 8, 2450.00, 8),
  (2, 104, 1800.00 * 5, 1750.00, 5),
  (3, 105, 1500.30 * 12, 1480.00, 12),
  (3, 107, 350.80 * 20, 340.00, 20),
  (4, 106, 2200.40 * 7, 2150.00, 7),
  (4, 108, 900.10 * 10, 880.00, 10),
  (5, 102, 2500.75 * 4, 2480.00, 4),
  (5, 104, 1800.00 * 6, 1780.00, 6);

-----------------------------------------------------------
-- 6. Inserting into Trade table
INSERT INTO Trade (Trade_ID, Price, Trade_Date, Trade_Qty, User_ID, Portfolio_ID, Stock_ID)
VALUES
  (1001, 1200.50, '2025-04-10', 10, 1, 1, 101),
  (1002, 800.20, '2025-04-11', 15, 1, 1, 103),
  (1003, 2500.75, '2025-04-12', 8, 2, 2, 102),
  (1004, 1800.00, '2025-04-13', 5, 2, 2, 104),
  (1005, 1500.30, '2025-04-14', 12, 4, 3, 105),
  (1006, 350.80, '2025-04-15', 20, 4, 3, 107),
  (1007, 2200.40, '2025-04-16', 7, 5, 4, 106),
  (1008, 900.10, '2025-04-17', 10, 5, 4, 108),
  (1009, 2500.75, '2025-04-18', 4, 8, 5, 102),
  (1010, 1800.00, '2025-04-19', 6, 8, 5, 104);

-----------------------------------------------------------
-- 7. Inserting into Buy_Trade table (for buy trades)
-- We assume Trade_IDs 1001, 1003, 1005, 1007, 1009 are buy trades.
INSERT INTO Buy_Trade (Trade_ID, Broker_Fee, Payment_Method, Exchange)
VALUES
  (1001, 50.00, 'NEFT', 'NSE'),
  (1003, 45.00, 'IMPS', 'BSE'),
  (1005, 40.00, 'RTGS', 'NSE'),
  (1007, 55.00, 'NEFT', 'BSE'),
  (1009, 35.00, 'IMPS', 'NSE');

-----------------------------------------------------------
-- 8. Inserting into Sell_Trade table (for sell trades)
-- We assume Trade_IDs 1002, 1004, 1006, 1008, 1010 are sell trades.
INSERT INTO Sell_Trade (Trade_ID, Sell_Charges, Capital_gain_loss)
VALUES
  (1002, 60.00, 100.00),
  (1004, 55.00, 80.00),
  (1006, 50.00, 120.00),
  (1008, 65.00, 90.00),
  (1010, 70.00, 110.00);

-----------------------------------------------------------
-- 9. Inserting into Dividend_History table
INSERT INTO Dividend_History (Dividend_ID, Div_Amount, Payment_Date, Stock_ID)
VALUES
  (2001, 15.50, '2025-03-31', 101),
  (2002, 20.00, '2025-04-05', 103),
  (2003, 18.75, '2025-04-08', 104),
  (2004, 12.50, '2025-04-10', 105),
  (2005, 10.00, '2025-04-12', 107);

-----------------------------------------------------------
-- 10. Inserting into Market_Data table
INSERT INTO Market_Data (Data_ID, Cur_Date, High, Low, Volume, Open_Price, Closed_Price, Stock_ID)
VALUES
  (3001, '2025-04-10', 1210.00, 1195.00, 150000, 1200.00, 1205.50, 101),
  (3002, '2025-04-10', 2510.00, 2490.00, 200000, 2500.00, 2505.75, 102),
  (3003, '2025-04-10', 810.00, 790.00, 100000, 800.00, 805.20, 103),
  (3004, '2025-04-10', 1820.00, 1790.00, 130000, 1800.00, 1805.00, 104),
  (3005, '2025-04-10', 1510.00, 1485.00, 120000, 1500.00, 1505.30, 105),
  (3006, '2025-04-10', 2220.00, 2180.00, 90000, 2200.00, 2205.40, 106),
  (3007, '2025-04-10', 360.00, 345.00, 85000, 350.00, 355.80, 107),
  (3008, '2025-04-10', 910.00, 880.00, 110000, 900.00, 905.10, 108);

-----------------------------------------------------------
-- 11. Inserting into Watchlist table
INSERT INTO Watchlist (Watchlist_ID, Watchlist_Name, Notes, Added_Date, Stock_ID, User_ID)
VALUES
  (4001, 'Tech Stocks', 'Monitor Infosys for earnings', '2025-04-01', 103, 2),
  (4002, 'Banking', 'Watch HDFC and ICICI for stability', '2025-04-05', 104, 4),
  (4003, 'Automobiles', 'Tata Motors for long term hold', '2025-04-08', 101, 1),
  (4004, 'Diverse', 'Mix of sectors for diversification', '2025-04-12', 108, 5);

-----------------------------------------------------------
-- 12. Inserting into Account table
INSERT INTO Account (Account_ID, Balance, Opening_Date, User_ID)
VALUES
  (5001, 100000.00, '2025-01-10', 1),
  (5002, 150000.00, '2025-02-15', 2),
  (5003, 200000.00, '2025-03-01', 3),
  (5004, 250000.00, '2025-03-20', 4),
  (5005, 300000.00, '2025-04-05', 5),
  (5006, 120000.00, '2025-02-28', 6),
  (5007, 90000.00,  '2025-03-10', 7),
  (5008, 110000.00, '2025-03-25', 8),
  (5009, 130000.00, '2025-04-01', 9),
  (5010, 140000.00, '2025-04-10', 10);

-----------------------------------------------------------
-- 13. Inserting into Bank_Details table
INSERT INTO Bank_Details (Bank_ID, Bank_Name, Currency, Account_ID)
VALUES
  (6001, 'State Bank of India', 'INR', 5001),
  (6002, 'HDFC Bank', 'INR', 5002),
  (6003, 'ICICI Bank', 'INR', 5003),
  (6004, 'Axis Bank', 'INR', 5004),
  (6005, 'Punjab National Bank', 'INR', 5005),
  (6006, 'Kotak Mahindra Bank', 'INR', 5006),
  (6007, 'IDBI Bank', 'INR', 5007),
  (6008, 'Yes Bank', 'INR', 5008),
  (6009, 'Bank of Baroda', 'INR', 5009),
  (6010, 'IndusInd Bank', 'INR', 5010);

-----------------------------------------------------------
-- 14. Inserting into Transaction_History table
INSERT INTO Transaction_History (Transaction_ID, Date, Amount, Transaction_Type, Status, Account_ID)
VALUES
  (7001, '2025-04-05', 5000.00, 'Deposit', 'Completed', 5001),
  (7002, '2025-04-06', 1000.00, 'Withdrawal', 'Completed', 5002),
  (7003, '2025-04-07', 1500.00, 'Deposit', 'Pending', 5003),
  (7004, '2025-04-08', 2000.00, 'Withdrawal', 'Completed', 5004),
  (7005, '2025-04-09', 2500.00, 'Deposit', 'Completed', 5005),
  (7006, '2025-04-10', 3000.00, 'Deposit', 'Completed', 5006),
  (7007, '2025-04-11', 3500.00, 'Withdrawal', 'Completed', 5007),
  (7008, '2025-04-12', 4000.00, 'Deposit', 'Completed', 5008),
  (7009, '2025-04-13', 4500.00, 'Withdrawal', 'Completed', 5009),
  (7010, '2025-04-14', 5000.00, 'Deposit', 'Completed', 5010);

-----------------------------------------------------------
-- 15. Inserting into Alerts table
INSERT INTO Alerts (Alert_ID, Alert_Type, Threshold_Value, Alert_Time, Read_Status, User_ID, Stock_ID)
VALUES
  (8001, 'Price Drop', 1100.00, '09:30:00', FALSE, 1, 101),
  (8002, 'Price Surge', 2600.00, '10:00:00', FALSE, 2, 102),
  (8003, 'Dividend Alert', 20.00, '11:00:00', FALSE, 3, 103),
  (8004, 'Volume Spike', 50000, '12:00:00', FALSE, 4, 104),
  (8005, 'Price Drop', 1400.00, '13:30:00', FALSE, 5, 105),
  (8006, 'Price Surge', 2300.00, '14:00:00', FALSE, 6, 106),
  (8007, 'Dividend Alert', 15.00, '14:30:00', FALSE, 7, 107),
  (8008, 'Volume Spike', 60000, '15:00:00', FALSE, 8, 108);
