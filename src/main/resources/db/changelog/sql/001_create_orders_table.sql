-- Create ORDERS table
CREATE TABLE IF NOT EXISTS orders
(
	code TEXT NOT NULL,
	user_id TEXT,
	creation_time TIMESTAMP,
	PRIMARY KEY (code)
);
