CREATE TABLE IF NOT EXISTS Discount_Types(
    id SERIAL PRIMARY KEY,
    type TEXT
);
CREATE TABLE IF NOT EXISTS Discounts(
    id SERIAL PRIMARY KEY,
    type_id INT,
    FOREIGN KEY (type_id) REFERENCES Discount_Types(id)
);

CREATE TABLE IF NOT EXISTS Discount_bulk(
    id INT PRIMARY KEY,
    product_id INT,
    bulk INT,
    amount DOUBLE PRECISION,
    FOREIGN KEY (product_id) REFERENCES Products(id),
    FOREIGN KEY (id) REFERENCES Discounts(id)
);

CREATE TABLE IF NOT EXISTS Discount_combos(
    id INT PRIMARY KEY,
    new_price DOUBLE PRECISION,
    FOREIGN KEY (id) REFERENCES Discounts(id)
);

CREATE TABLE IF NOT EXISTS Combo_items(
    id SERIAL PRIMARY KEY,
    discount_combo_id INT,
    product_type_id INT,
    FOREIGN KEY (discount_combo_id) REFERENCES Discount_combos(id),
    FOREIGN KEY (product_type_id) REFERENCES Product_types(id)
);
