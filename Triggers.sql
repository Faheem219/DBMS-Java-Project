USE StockTradingDB2;

CREATE TABLE IF NOT EXISTS Trigger_Log (
    Log_ID INT AUTO_INCREMENT PRIMARY KEY,
    Trigger_Name VARCHAR(100) NOT NULL,
    Event_Time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Message TEXT
);

DELIMITER //

-- Trigger 1: Update Portfolio Total Value After a Trade Insert
-- When a new trade is inserted, its value (Price * Trade_Qty) is added to the associated portfolio’s Total_Value.
CREATE TRIGGER trg_after_trade_insert
AFTER INSERT ON Trade
FOR EACH ROW
BEGIN
    -- Update the Portfolio total value
    UPDATE Portfolio 
    SET Total_Value = Total_Value + (NEW.Price * NEW.Trade_Qty)
    WHERE Portfolio_ID = NEW.Portfolio_ID;
    
    -- Insert logging entry
    INSERT INTO Trigger_Log (Trigger_Name, Message)
    VALUES ('trg_after_trade_insert', CONCAT('Inserted trade for Portfolio_ID ', NEW.Portfolio_ID, 
          ': added ', (NEW.Price * NEW.Trade_Qty), ' to Total_Value.'));
END;
//

-- Trigger 2: Update Stock Market Price After Inserting New Market Data
-- Sets the Stock’s Market_Price to the Closed_Price from the new Market_Data record.
CREATE TRIGGER trg_after_marketdata_insert
AFTER INSERT ON Market_Data
FOR EACH ROW
BEGIN
    -- Update the Stock market price
    UPDATE Stock
    SET Market_Price = NEW.Closed_Price
    WHERE Stock_ID = NEW.Stock_ID;
    
    -- Insert logging entry
    INSERT INTO Trigger_Log (Trigger_Name, Message)
    VALUES ('trg_after_marketdata_insert', CONCAT('Market_Data inserted for Stock_ID ', NEW.Stock_ID, 
          ': set Market_Price to ', NEW.Closed_Price, '.'));
END;
//

-- Trigger 3: Update Portfolio Total Value After a Trade is Deleted
-- When a trade is deleted, its previously added value is deducted from the portfolio’s Total_Value.
CREATE TRIGGER trg_after_trade_delete
AFTER DELETE ON Trade
FOR EACH ROW
BEGIN
    -- Update the Portfolio total value
    UPDATE Portfolio 
    SET Total_Value = Total_Value - (OLD.Price * OLD.Trade_Qty)
    WHERE Portfolio_ID = OLD.Portfolio_ID;
    
    -- Insert logging entry
    INSERT INTO Trigger_Log (Trigger_Name, Message)
    VALUES ('trg_after_trade_delete', CONCAT('Deleted trade for Portfolio_ID ', OLD.Portfolio_ID, 
          ': subtracted ', (OLD.Price * OLD.Trade_Qty), ' from Total_Value.'));
END;
//

-- Trigger 4: Adjust Portfolio Total Value on Trade Update
-- On update, remove the effect of the old trade values and add the new ones.
CREATE TRIGGER trg_after_trade_update
AFTER UPDATE ON Trade
FOR EACH ROW
BEGIN
    -- Adjust the Portfolio total value
    UPDATE Portfolio 
    SET Total_Value = Total_Value - (OLD.Price * OLD.Trade_Qty) + (NEW.Price * NEW.Trade_Qty)
    WHERE Portfolio_ID = NEW.Portfolio_ID;
    
    -- Insert logging entry
    INSERT INTO Trigger_Log (Trigger_Name, Message)
    VALUES ('trg_after_trade_update', CONCAT('Updated trade for Portfolio_ID ', NEW.Portfolio_ID, 
          ': updated Total_Value by removing ', (OLD.Price * OLD.Trade_Qty), ' and adding ', (NEW.Price * NEW.Trade_Qty), '.'));
END;
//

-- Trigger 5: Update Account Balance When a Dividend is Inserted
-- A dividend received for a stock held by a portfolio is credited to the user's account.
CREATE TRIGGER trg_after_dividend_insert
AFTER INSERT ON Dividend_History
FOR EACH ROW
BEGIN
    DECLARE userAccount INT;
    DECLARE shareQty INT;
    DECLARE targetAccount INT;
    
    -- Identify one portfolio holding the stock and get its User_ID and share quantity
    SELECT p.User_ID, ps.Quantity 
    INTO userAccount, shareQty
    FROM Portfolio_Stock ps
    JOIN Portfolio p ON ps.Portfolio_ID = p.Portfolio_ID
    WHERE ps.Stock_ID = NEW.Stock_ID
    LIMIT 1;
    
    -- Retrieve the Account_ID for this user into a local variable
    SELECT Account_ID INTO targetAccount
    FROM Account 
    WHERE User_ID = userAccount 
    LIMIT 1;
    
    -- Update the account balance for the selected account
    UPDATE Account
    SET Balance = Balance + (NEW.Div_Amount * shareQty)
    WHERE Account_ID = targetAccount;
    
    -- Insert logging entry
    INSERT INTO Trigger_Log (Trigger_Name, Message)
    VALUES ('trg_after_dividend_insert', CONCAT('Dividend inserted for Stock_ID ', NEW.Stock_ID, 
          ': credited account of user ', userAccount, ' with ', (NEW.Div_Amount * shareQty), '.'));
END;
//

DELIMITER ;
