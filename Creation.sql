-- Create the database and use it
CREATE DATABASE IF NOT EXISTS StockTradingDB2;
USE StockTradingDB;

-- 1. User Table
CREATE TABLE User (
    User_ID INT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Password VARCHAR(100) NOT NULL,
    Created_Date DATE NOT NULL,
    Contact_Info VARCHAR(100),
    User_Type VARCHAR(50),
    Annual_Income DECIMAL(12,2)
);

-- 2. Institution Table
CREATE TABLE Institution (
    Institution_ID INT PRIMARY KEY,
    Institution_Type VARCHAR(100) NOT NULL,
    Reg_Num VARCHAR(50) NOT NULL,
    User_ID INT,
    CONSTRAINT fk_institution_user FOREIGN KEY (User_ID) REFERENCES User(User_ID)
        ON DELETE SET NULL ON UPDATE CASCADE
);

-- 3. Portfolio Table
CREATE TABLE Portfolio (
    Portfolio_ID INT PRIMARY KEY,
    Portfolio_Name VARCHAR(100) NOT NULL,
    Creation_Date DATE NOT NULL,
    Total_Value DECIMAL(15,2) NOT NULL DEFAULT 0,
    User_ID INT,
    CONSTRAINT fk_portfolio_user FOREIGN KEY (User_ID) REFERENCES User(User_ID)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 4. Stock Table
CREATE TABLE Stock (
    Stock_ID INT PRIMARY KEY,
    Market_Price DECIMAL(10,2) NOT NULL,
    Market_Cap DECIMAL(15,2),
    Name VARCHAR(100) NOT NULL,
    Volatility DECIMAL(5,2)
);

-- 5. Portfolio_Stock Table (Associative table between Portfolio and Stock)
CREATE TABLE Portfolio_Stock (
    Portfolio_ID INT,
    Stock_ID INT,
    Current_Value DECIMAL(15,2) NOT NULL,
    Purchase_Price DECIMAL(10,2) NOT NULL,
    Quantity INT NOT NULL,
    PRIMARY KEY (Portfolio_ID, Stock_ID),
    CONSTRAINT fk_portfolio_stock_portfolio FOREIGN KEY (Portfolio_ID) REFERENCES Portfolio(Portfolio_ID)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_portfolio_stock_stock FOREIGN KEY (Stock_ID) REFERENCES Stock(Stock_ID)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 6. Trade Table
CREATE TABLE Trade (
    Trade_ID INT PRIMARY KEY,
    Price DECIMAL(10,2) NOT NULL,
    Trade_Date DATE NOT NULL,
    Trade_Qty INT NOT NULL,
    User_ID INT,
    Portfolio_ID INT,
    Stock_ID INT,
    CONSTRAINT fk_trade_user FOREIGN KEY (User_ID) REFERENCES User(User_ID)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_trade_portfolio FOREIGN KEY (Portfolio_ID) REFERENCES Portfolio(Portfolio_ID)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_trade_stock FOREIGN KEY (Stock_ID) REFERENCES Stock(Stock_ID)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 7. Buy_Trade Table (Details for buy trades; Trade_ID acts as primary key and FK)
CREATE TABLE Buy_Trade (
    Trade_ID INT PRIMARY KEY,
    Broker_Fee DECIMAL(10,2) NOT NULL,
    Payment_Method VARCHAR(50),
    Exchange VARCHAR(50),
    CONSTRAINT fk_buytrade_trade FOREIGN KEY (Trade_ID) REFERENCES Trade(Trade_ID)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 8. Sell_Trade Table (Details for sell trades; Trade_ID acts as primary key and FK)
CREATE TABLE Sell_Trade (
    Trade_ID INT PRIMARY KEY,
    Sell_Charges DECIMAL(10,2) NOT NULL,
    Capital_gain_loss DECIMAL(10,2),
    CONSTRAINT fk_selltrade_trade FOREIGN KEY (Trade_ID) REFERENCES Trade(Trade_ID)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 9. Dividend_History Table
CREATE TABLE Dividend_History (
    Dividend_ID INT PRIMARY KEY,
    Div_Amount DECIMAL(10,2) NOT NULL,
    Payment_Date DATE NOT NULL,
    Stock_ID INT,
    CONSTRAINT fk_dividend_stock FOREIGN KEY (Stock_ID) REFERENCES Stock(Stock_ID)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 10. Market_Data Table
CREATE TABLE Market_Data (
    Data_ID INT PRIMARY KEY,
    Cur_Date DATE NOT NULL,
    High DECIMAL(10,2) NOT NULL,
    Low DECIMAL(10,2) NOT NULL,
    Volume INT NOT NULL,
    Open_Price DECIMAL(10,2) NOT NULL,
    Closed_Price DECIMAL(10,2) NOT NULL,
    Stock_ID INT,
    CONSTRAINT fk_marketdata_stock FOREIGN KEY (Stock_ID) REFERENCES Stock(Stock_ID)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 11. Watchlist Table
CREATE TABLE Watchlist (
    Watchlist_ID INT PRIMARY KEY,
    Watchlist_Name VARCHAR(100) NOT NULL,
    Notes VARCHAR(255),
    Added_Date DATE NOT NULL,
    Stock_ID INT,
    User_ID INT,
    CONSTRAINT fk_watchlist_stock FOREIGN KEY (Stock_ID) REFERENCES Stock(Stock_ID)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_watchlist_user FOREIGN KEY (User_ID) REFERENCES User(User_ID)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 12. Account Table
CREATE TABLE Account (
    Account_ID INT PRIMARY KEY,
    Balance DECIMAL(15,2) NOT NULL DEFAULT 0,
    Opening_Date DATE NOT NULL,
    User_ID INT,
    CONSTRAINT fk_account_user FOREIGN KEY (User_ID) REFERENCES User(User_ID)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 13. Bank_Details Table
CREATE TABLE Bank_Details (
    Bank_ID INT PRIMARY KEY,
    Bank_Name VARCHAR(100) NOT NULL,
    Currency VARCHAR(10) NOT NULL,
    Account_ID INT,
    CONSTRAINT fk_bankdetails_account FOREIGN KEY (Account_ID) REFERENCES Account(Account_ID)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 14. Transaction_History Table
CREATE TABLE Transaction_History (
    Transaction_ID INT PRIMARY KEY,
    Date DATE NOT NULL,
    Amount DECIMAL(15,2) NOT NULL,
    Transaction_Type VARCHAR(50) NOT NULL,
    Status VARCHAR(50),
    Account_ID INT,
    CONSTRAINT fk_transaction_account FOREIGN KEY (Account_ID) REFERENCES Account(Account_ID)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 15. Alerts Table
CREATE TABLE Alerts (
    Alert_ID INT PRIMARY KEY,
    Alert_Type VARCHAR(50) NOT NULL,
    Threshold_Value DECIMAL(10,2) NOT NULL,
    Alert_Time TIME NOT NULL,
    Read_Status BOOLEAN DEFAULT FALSE,
    User_ID INT,
    Stock_ID INT,
    CONSTRAINT fk_alerts_user FOREIGN KEY (User_ID) REFERENCES User(User_ID)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_alerts_stock FOREIGN KEY (Stock_ID) REFERENCES Stock(Stock_ID)
        ON DELETE CASCADE ON UPDATE CASCADE
);
