USE StockTradingDB;

/* Query 1: Detailed Account Information with Bank and User Data */
SELECT 
    u.User_ID,
    u.Name,
    u.Email,
    a.Account_ID,
    a.Balance,
    bd.Bank_Name,
    bd.Currency
FROM User u
JOIN Account a ON u.User_ID = a.User_ID
LEFT JOIN Bank_Details bd ON a.Account_ID = bd.Account_ID
ORDER BY u.User_ID;

/* Query 2: Portfolio Summary with Purchase and Current Values */
SELECT 
    p.Portfolio_ID,
    p.Portfolio_Name,
    p.Creation_Date,
    p.Total_Value AS Stored_Total,
    SUM(ps.Purchase_Price * ps.Quantity) AS Total_Purchase_Value,
    SUM(ps.Current_Value) AS Total_Current_Value,
    (SUM(ps.Current_Value) - SUM(ps.Purchase_Price * ps.Quantity)) AS Profit_Loss
FROM Portfolio p
JOIN Portfolio_Stock ps ON p.Portfolio_ID = ps.Portfolio_ID
GROUP BY p.Portfolio_ID, p.Portfolio_Name, p.Creation_Date, p.Total_Value
ORDER BY Profit_Loss DESC;

/* Query 3: Stock Trade Summary: Total Trades and Average Trade Price per Stock */
SELECT 
    s.Stock_ID,
    s.Name AS Stock_Name,
    COUNT(t.Trade_ID) AS Total_Trades,
    AVG(t.Price) AS Avg_Trade_Price
FROM Stock s
LEFT JOIN Trade t ON s.Stock_ID = t.Stock_ID
GROUP BY s.Stock_ID, s.Name
ORDER BY Total_Trades DESC;

/* Query 4: Dividend Summary: Aggregated Dividend Data per Stock */
SELECT 
    s.Stock_ID,
    s.Name AS Stock_Name,
    COUNT(d.Dividend_ID) AS Dividend_Count,
    AVG(d.Div_Amount) AS Avg_Dividend
FROM Stock s
LEFT JOIN Dividend_History d ON s.Stock_ID = d.Stock_ID
GROUP BY s.Stock_ID, s.Name
ORDER BY Dividend_Count DESC;

/* Query 5: Market Data Overview: Latest Daily Stock Metrics */
SELECT 
    s.Stock_ID,
    s.Name AS Stock_Name,
    md.Cur_Date,
    md.Open_Price,
    md.Closed_Price,
    md.High,
    md.Low,
    md.Volume
FROM Stock s
JOIN Market_Data md ON s.Stock_ID = md.Stock_ID
ORDER BY md.Cur_Date DESC, s.Stock_ID;

/* Query 6: Alert Details: Count of Alerts per User and Alert Type */
SELECT 
    u.User_ID,
    u.Name,
    a.Alert_Type,
    COUNT(a.Alert_ID) AS Alert_Count
FROM User u
JOIN Alerts a ON u.User_ID = a.User_ID
GROUP BY u.User_ID, u.Name, a.Alert_Type
ORDER BY u.User_ID;

/* Query 7: Detailed Trade Information Including Trade Type (Buy/Sell) */
SELECT 
    t.Trade_ID,
    t.Trade_Date,
    t.Price,
    t.Trade_Qty,
    CASE 
        WHEN bt.Trade_ID IS NOT NULL THEN 'Buy'
        WHEN st.Trade_ID IS NOT NULL THEN 'Sell'
        ELSE 'Unknown'
    END AS Trade_Type,
    t.User_ID,
    t.Portfolio_ID,
    t.Stock_ID
FROM Trade t
LEFT JOIN Buy_Trade bt ON t.Trade_ID = bt.Trade_ID
LEFT JOIN Sell_Trade st ON t.Trade_ID = st.Trade_ID
ORDER BY t.Trade_Date DESC;

/* Query 8: User Transaction History: Deposits/Withdrawals with User and Account Details */
SELECT 
    u.User_ID,
    u.Name,
    a.Account_ID,
    a.Balance,
    th.Transaction_ID,
    th.Date,
    th.Amount,
    th.Transaction_Type,
    th.Status
FROM User u
JOIN Account a ON u.User_ID = a.User_ID
JOIN Transaction_History th ON a.Account_ID = th.Account_ID
ORDER BY th.Date DESC;

/* Query 9: Watchlist Overview: Number of Watchlist Entries per User */
SELECT 
    u.User_ID,
    u.Name,
    COUNT(w.Watchlist_ID) AS Num_Watchlist_Entries
FROM User u
LEFT JOIN Watchlist w ON u.User_ID = w.User_ID
GROUP BY u.User_ID, u.Name
ORDER BY Num_Watchlist_Entries DESC;

/* Query 10: Combined Portfolio Performance: Trades and Stocks Performance Analysis */
SELECT 
    p.Portfolio_ID,
    p.Portfolio_Name,
    COUNT(t.Trade_ID) AS Total_Trades,
    COALESCE(SUM(ps.Current_Value), 0) AS Portfolio_Current_Value,
    COALESCE(SUM(ps.Purchase_Price * ps.Quantity), 0) AS Portfolio_Purchase_Value,
    (COALESCE(SUM(ps.Current_Value), 0) - COALESCE(SUM(ps.Purchase_Price * ps.Quantity), 0)) AS Total_Profit_Loss
FROM Portfolio p
LEFT JOIN Trade t ON p.Portfolio_ID = t.Portfolio_ID
LEFT JOIN Portfolio_Stock ps ON p.Portfolio_ID = ps.Portfolio_ID
GROUP BY p.Portfolio_ID, p.Portfolio_Name
ORDER BY Total_Profit_Loss DESC;
