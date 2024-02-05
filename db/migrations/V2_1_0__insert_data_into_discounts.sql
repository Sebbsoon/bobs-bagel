INSERT INTO Discounts(type_id) VALUES
(1),
(1),
(1),
(2);

INSERT INTO Discount_bulk(id,product_id, bulk, amount) VALUES
(1,1,6,0.415),
(2,2,12,0.3325),
(3,3,6,0.415);

INSERT INTO Discount_combos(id,new_price) VALUES
(4,1.25);

INSERT INTO Combo_items(discount_combo_id,product_type_id) VALUES
(4,1),
(4,2);