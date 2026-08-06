
USE portfolio_db;


-- ============================
-- MARKET DATA
-- ============================

INSERT INTO market
(symbol, company_name, exchange, sector, current_price, change_percent)
VALUES

-- Existing Records

('AAPL',
 'Apple Inc',
 'NASDAQ',
 'Technology',
 220.50,
 1.25),

('TSLA',
 'Tesla Inc',
 'NASDAQ',
 'Automotive',
 350.20,
 -2.35),

('MSFT',
 'Microsoft Corporation',
 'NASDAQ',
 'Technology',
 420.75,
 0.80),

('AMZN',
 'Amazon Inc',
 'NASDAQ',
 'E-Commerce',
 185.30,
 -0.55),


-- Additional Records

('GOOGL',
 'Alphabet Inc',
 'NASDAQ',
 'Technology',
 175.80,
 1.10),

('BTC',
 'Bitcoin',
 'CRYPTO',
 'Cryptocurrency',
 68000.00,
 2.50),

('ETH',
 'Ethereum',
 'CRYPTO',
 'Cryptocurrency',
 3600.00,
 1.80),

('VOO',
 'Vanguard S&P 500 ETF',
 'NYSE',
 'ETF',
 520.25,
 0.65),

('SIP500',
 'S&P 500 Index Mutual Fund',
 'MUTUAL_FUND',
 'Mutual Fund',
 245.60,
 0.45),

('JPM',
 'JPMorgan Chase & Co',
 'NYSE',
 'Finance',
 215.40,
 0.90),

('GOLDETF',
 'Tata Gold ETF',
 'NSE',
 'Commodity',
 72.80,
 0.30),

('BND',
 'Vanguard Total Bond Market ETF',
 'NASDAQ',
 'Bond',
 71.50,
 0.15),

('NVDA',
 'NVIDIA Corporation',
 'NASDAQ',
 'Technology',
 135.70,
 2.20),

('HDFCBANK',
 'HDFC Bank Limited',
 'NSE',
 'Banking',
 1850.00,
 0.75);



-- ============================
-- HOLDING DATA
-- ============================

INSERT INTO holding
(
 market_id,
 quantity,
 purchase_price,
 purchase_date
)

VALUES

-- Existing Holdings

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
 '2026-03-10'),


-- Additional Holdings

-- Alphabet
(5,
 12,
 150.00,
 '2026-01-25'),


-- Bitcoin
(6,
 1,
 60000.00,
 '2026-02-05'),


-- Ethereum
(7,
 3,
 3000.00,
 '2026-02-18'),


-- Vanguard ETF
(8,
 15,
 490.00,
 '2026-03-01'),


-- Mutual Fund
(9,
 50,
 220.00,
 '2026-03-15'),


-- JPMorgan
(10,
 20,
 190.00,
 '2026-04-05'),


-- Gold ETF
(11,
 100,
 65.00,
 '2026-04-20'),


-- Bond ETF
(12,
 40,
 70.00,
 '2026-05-10'),


-- Nvidia
(13,
 25,
 110.00,
 '2026-05-25'),


-- HDFC Bank
(14,
 15,
 1700.00,
 '2026-06-01');