DROP TABLE IF EXISTS t_order_item;
DROP TABLE IF EXISTS t_order;
DROP TABLE IF EXISTS t_inventory;
DROP TABLE IF EXISTS t_sku;
DROP TABLE IF EXISTS t_product;
DROP TABLE IF EXISTS t_user;

CREATE TABLE t_user (
  user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_name VARCHAR(64),
  mobile VARCHAR(32),
  status INT,
  points INT
);

CREATE TABLE t_product (
  product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_name VARCHAR(128),
  shelf_status INT,
  sales_count INT
);

CREATE TABLE t_sku (
  sku_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_id BIGINT,
  sku_code VARCHAR(64),
  sku_name VARCHAR(128),
  sale_price DECIMAL(16, 2),
  shelf_status INT
);

CREATE TABLE t_inventory (
  inventory_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  store_id BIGINT,
  sku_id BIGINT,
  total_stock INT,
  locked_stock INT,
  available_stock INT
);

CREATE TABLE t_order (
  order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_no VARCHAR(64) NOT NULL,
  user_id BIGINT,
  store_id BIGINT,
  status INT,
  total_amount DECIMAL(16, 2),
  created_at TIMESTAMP NULL,
  paid_at TIMESTAMP NULL,
  cancelled_at TIMESTAMP NULL
);

CREATE TABLE t_order_item (
  order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_no VARCHAR(64) NOT NULL,
  sku_id BIGINT,
  sku_name VARCHAR(128),
  quantity INT,
  unit_price DECIMAL(16, 2),
  line_amount DECIMAL(16, 2)
);

INSERT INTO t_user(user_id, user_name, mobile, status, points)
VALUES (1, 'test-user', '13800000000', 1, 100);

INSERT INTO t_product(product_id, product_name, shelf_status, sales_count)
VALUES (200, 'Coffee', 1, 0);

INSERT INTO t_sku(sku_id, product_id, sku_code, sku_name, sale_price, shelf_status)
VALUES (100, 200, 'SKU100', 'Coffee-Latte', 22.00, 1);

INSERT INTO t_inventory(inventory_id, store_id, sku_id, total_stock, locked_stock, available_stock)
VALUES (1, 10, 100, 100, 0, 100);
