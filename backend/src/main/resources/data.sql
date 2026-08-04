INSERT IGNORE INTO market
(symbol, company_name, exchange, sector, current_price)
VALUES

('AAPL',
 'Apple Inc',
 'NASDAQ',
 'Technology',
 220.50),

('TSLA',
 'Tesla Inc',
 'NASDAQ',
 'Automotive',
 350.20),

('MSFT',
 'Microsoft Corporation',
 'NASDAQ',
 'Technology',
 420.75),

('AMZN',
 'Amazon Inc',
 'NASDAQ',
 'E-Commerce',
 185.30);

 INSERT IGNORE INTO holding
 (
 market_id,
 quantity,
 purchase_price,
 purchase_date
 )

 VALUES

 (1,
 10,
 180.00,
 '2026-01-15'),


 (2,
 5,
 300.00,
 '2026-02-20'),


 (3,
 8,
 390.50,
 '2026-03-10');