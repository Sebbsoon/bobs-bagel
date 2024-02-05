-- Create the Products table
CREATE TABLE IF NOT EXISTS Product_types(
    id SERIAL PRIMARY KEY,
    type TEXT
);
CREATE TABLE Products (
    id SERIAL PRIMARY KEY,
    sku VARCHAR(4),
    price DOUBLE PRECISION,
    type_id INT,
    variant TEXT,
    FOREIGN KEY (type_id) REFERENCES Product_types(id)
);

