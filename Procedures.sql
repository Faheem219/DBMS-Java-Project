USE StockTradingDB;

DELIMITER //

-- Procedure 1: Show Account Details by Username (search by either Name or Email)
CREATE PROCEDURE showAccountDetails (
    IN in_username VARCHAR(50)
)
BEGIN
    SELECT 
        u.User_ID,
        u.Name,
        u.Email,
        a.Account_ID,
        a.Balance,
        a.Opening_Date
    FROM Account AS a
    JOIN User AS u ON a.User_ID = u.User_ID
    WHERE u.Email = in_username OR u.Name = in_username;
END;
//

-- Procedure 2: Change Password for a User
CREATE PROCEDURE changePassword (
    IN in_username VARCHAR(50),
    IN in_old_password VARCHAR(100),
    IN in_new_password VARCHAR(100)
)
BEGIN
    DECLARE current_pass VARCHAR(100);
    
    -- Retrieve the current password (from User table)
    SELECT Password INTO current_pass 
    FROM User 
    WHERE Name = in_username OR Email = in_username;
    
    IF current_pass IS NOT NULL AND current_pass = in_old_password THEN
        -- Update password if the old password matches
        UPDATE User 
        SET Password = in_new_password 
        WHERE Name = in_username OR Email = in_username;
    END IF;
END;
//

-- Procedure 3: Get Portfolio Summary
-- Returns portfolio details along with the aggregated current value from Portfolio_Stock.
CREATE PROCEDURE getPortfolioSummary (
    IN in_portfolioID INT
)
BEGIN
    SELECT 
        p.Portfolio_ID,
        p.Portfolio_Name,
        p.Creation_Date,
        p.Total_Value,
        COALESCE(SUM(ps.Current_Value), 0) AS Calculated_Value
    FROM Portfolio AS p
    LEFT JOIN Portfolio_Stock AS ps ON p.Portfolio_ID = ps.Portfolio_ID
    WHERE p.Portfolio_ID = in_portfolioID
    GROUP BY p.Portfolio_ID, p.Portfolio_Name, p.Creation_Date, p.Total_Value;
END;
//

-- Procedure 4: List Trades for a Specific User
CREATE PROCEDURE listUserTrades (
    IN in_userID INT
)
BEGIN
    SELECT 
        t.Trade_ID,
        t.Price,
        t.Trade_Date,
        t.Trade_Qty,
        t.Portfolio_ID,
        t.Stock_ID
    FROM Trade t
    WHERE t.User_ID = in_userID
    ORDER BY t.Trade_Date DESC;
END;
//

-- Procedure 5: List Stocks in a Specific Portfolio
CREATE PROCEDURE listPortfolioStocks (
    IN in_portfolioID INT
)
BEGIN
    SELECT 
        ps.Stock_ID,
        s.Name AS Stock_Name,
        ps.Quantity,
        ps.Purchase_Price,
        ps.Current_Value
    FROM Portfolio_Stock ps
    JOIN Stock s ON ps.Stock_ID = s.Stock_ID
    WHERE ps.Portfolio_ID = in_portfolioID;
END;
//

-- Procedure 6: Get Dividend History for a Given Stock
CREATE PROCEDURE getDividendHistory (
    IN in_stockID INT
)
BEGIN
    SELECT 
        Dividend_ID,
        Div_Amount,
        Payment_Date
    FROM Dividend_History
    WHERE Stock_ID = in_stockID
    ORDER BY Payment_Date DESC;
END;
//

DELIMITER ;

-- USAGE EXAMPLES:
-- Example: Show account details (using name or email)
-- CALL showAccountDetails('johndoe@example.com');

-- Example: Change password
-- CALL changePassword('johndoe@example.com', 'oldpassword', 'newpassword');

-- Example: Get portfolio summary
-- CALL getPortfolioSummary(1);

-- Example: List all trades for user with ID 1
-- CALL listUserTrades(1);

-- Example: List stocks within portfolio ID 1
-- CALL listPortfolioStocks(1);

-- Example: Get dividend history for a stock (e.g., Stock_ID = 100)
-- CALL getDividendHistory(100);

