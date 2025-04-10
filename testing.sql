USE StockTradingDB;

-- Disable foreign key checks to avoid constraint errors during truncation
SET FOREIGN_KEY_CHECKS = 0;

-- Truncate tables in the recommended order (child tables first, then parent tables)
TRUNCATE TABLE Alerts;
TRUNCATE TABLE Transaction_History;
TRUNCATE TABLE Bank_Details;
TRUNCATE TABLE Watchlist;
TRUNCATE TABLE Market_Data;
TRUNCATE TABLE Dividend_History;
TRUNCATE TABLE Sell_Trade;
TRUNCATE TABLE Buy_Trade;
TRUNCATE TABLE Trade;
TRUNCATE TABLE Portfolio_Stock;
TRUNCATE TABLE Portfolio;
TRUNCATE TABLE Stock;
TRUNCATE TABLE Institution;
TRUNCATE TABLE Account;
TRUNCATE TABLE User;
TRUNCATE TABLE Trigger_Log;

-- Re-enable foreign key checks after truncation is complete
SET FOREIGN_KEY_CHECKS = 1;
