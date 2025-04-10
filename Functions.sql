USE StockTradingDB;

DELIMITER //

-- Function 1: Calculate the Current Portfolio Value (sum of Current_Value for all stocks in the portfolio)
CREATE FUNCTION currentPortfolioValue (in_portfolioID INT)
RETURNS DECIMAL(15,2)
READS SQL DATA
BEGIN
    DECLARE totalVal DECIMAL(15,2);
    SELECT COALESCE(SUM(Current_Value), 0) INTO totalVal
    FROM Portfolio_Stock
    WHERE Portfolio_ID = in_portfolioID;
    RETURN totalVal;
END;
//

-- Function 2: Total Number of Trades for a Specific Stock
CREATE FUNCTION totalTradesForStock (in_stockID INT)
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE tradeCount INT;
    SELECT COUNT(*) INTO tradeCount
    FROM Trade
    WHERE Stock_ID = in_stockID;
    RETURN tradeCount;
END;
//

-- Function 3: Current Account Balance
-- (Reads the balance from the Account table for the provided Account_ID.)
CREATE FUNCTION currentAccountBalance (in_accountID INT)
RETURNS DECIMAL(15,2)
READS SQL DATA
BEGIN
    DECLARE balanceVal DECIMAL(15,2);
    SELECT Balance INTO balanceVal
    FROM Account
    WHERE Account_ID = in_accountID;
    RETURN balanceVal;
END;
//

-- Function 4: Calculate Portfolio ROI (%)
-- ROI is computed as: ((Total Current Value - Total Purchase Value) / Total Purchase Value) * 100.
CREATE FUNCTION calculatePortfolioROI (in_portfolioID INT)
RETURNS DECIMAL(10,2)
READS SQL DATA
BEGIN
    DECLARE totalPurchase DECIMAL(15,2);
    DECLARE totalCurrent DECIMAL(15,2);
    DECLARE roi DECIMAL(10,2);
    
    SELECT COALESCE(SUM(Purchase_Price * Quantity), 0) INTO totalPurchase
    FROM Portfolio_Stock
    WHERE Portfolio_ID = in_portfolioID;
    
    SELECT COALESCE(SUM(Current_Value), 0) INTO totalCurrent
    FROM Portfolio_Stock
    WHERE Portfolio_ID = in_portfolioID;
    
    IF totalPurchase = 0 THEN
        SET roi = 0;
    ELSE
        SET roi = ((totalCurrent - totalPurchase) / totalPurchase) * 100;
    END IF;
    
    RETURN roi;
END;
//

DELIMITER ;

-- USAGE EXAMPLES:
-- Get current value of portfolio 1
-- SELECT currentPortfolioValue(1) AS PortfolioValue;

-- Get total number of trades for stock with ID 101
-- SELECT totalTradesForStock(101) AS TradeCount;

-- Get current account balance for account ID 1
-- SELECT currentAccountBalance(1) AS AccountBalance;

-- Calculate ROI (%) for portfolio 1
-- SELECT calculatePortfolioROI(1) AS PortfolioROI;

