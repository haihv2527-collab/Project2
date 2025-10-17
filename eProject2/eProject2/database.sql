-- Tạo database
CREATE DATABASE IF NOT EXISTS restaurant_db;
USE restaurant_db;

-- Users (Tài khoản)
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    role ENUM('admin', 'staff', 'customer') DEFAULT 'customer',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bàn
CREATE TABLE tables (
    table_id INT PRIMARY KEY AUTO_INCREMENT,
    table_name VARCHAR(50) NOT NULL,
    capacity INT,
    status ENUM('available', 'occupied', 'reserved') DEFAULT 'available',
    position_x INT,
    position_y INT
);

-- Menu (Thêm cột image_url và category)
CREATE TABLE menu (
    dish_id INT PRIMARY KEY AUTO_INCREMENT,
    dish_name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2),
    category ENUM('chicken', 'pizza', 'burger', 'rice', 'salad', 'drink') NOT NULL,
    description TEXT,
    image_url VARCHAR(255),
    available BOOLEAN DEFAULT TRUE
);

-- Đặt bàn (đã cập nhật)
CREATE TABLE reservations (
    reservation_id INT PRIMARY KEY AUTO_INCREMENT,
    table_id INT,
    customer_name VARCHAR(100),
    customer_phone VARCHAR(20),
    customer_email VARCHAR(100),
    reservation_time DATETIME,
    num_guests INT,
    status ENUM('confirmed', 'cancelled', 'completed') DEFAULT 'confirmed',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (table_id) REFERENCES tables(table_id)
);

-- Orders
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    table_id INT,
    reservation_id INT,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2),
    status ENUM('pending', 'confirmed', 'completed', 'paid') DEFAULT 'pending',
    FOREIGN KEY (table_id) REFERENCES tables(table_id),
    FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id)
);

-- Chi tiết order
CREATE TABLE order_details (
    detail_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    dish_id INT,
    quantity INT,
    price DECIMAL(10, 2),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (dish_id) REFERENCES menu(dish_id)
);

-- Thanh toán
CREATE TABLE transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    amount DECIMAL(10, 2),
    payment_method ENUM('cash', 'qrcode', 'card') DEFAULT 'qrcode',
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    qr_code_data TEXT,
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

-- ==================== DỮ LIỆU MẪU ====================

-- Thêm admin
INSERT INTO users VALUES
(1, 'admin', SHA2('admin123', 256), 'admin@restaurant.com', 'admin', NOW());

-- Thêm bàn (10 bàn)
INSERT INTO tables VALUES 
(1, 'Bàn 1', 4, 'available', 50, 50),
(2, 'Bàn 2', 4, 'available', 200, 50),
(3, 'Bàn 3', 6, 'available', 350, 50),
(4, 'Bàn 4', 2, 'available', 50, 200),
(5, 'Bàn 5', 8, 'available', 200, 200),
(6, 'Bàn 6', 4, 'available', 350, 200),
(7, 'Bàn 7', 2, 'available', 50, 350),
(8, 'Bàn 8', 6, 'available', 200, 350),
(9, 'Bàn 9', 4, 'available', 350, 350),
(10, 'Bàn 10', 10, 'available', 200, 500);

-- Thêm menu (Chicken, Pizza, Burger, Rice, Salad, Drink)
INSERT INTO menu VALUES 
-- Chicken
(1, 'Chicken 2 pieces', 69000, 'chicken', 'Gà rán giòn 2 miếng', 'chicken_2.jpg', TRUE),
(2, 'Chicken 4 pieces', 129000, 'chicken', 'Gà rán giòn 4 miếng', 'chicken_4.jpg', TRUE),
(3, 'Chicken 6 pieces', 159000, 'chicken', 'Gà rán giòn 6 miếng', 'chicken_6.jpg', TRUE),
(4, 'Chicken 8 pieces', 239000, 'chicken', 'Gà rán giòn 8 miếng', 'chicken_8.jpg', TRUE),

-- Pizza
(5, 'Pizza Hải Sản', 199000, 'pizza', 'Pizza hải sản tươi ngon', 'pizza_seafood.jpg', TRUE),
(6, 'Pizza Xúc Xích', 149000, 'pizza', 'Pizza xúc xích Đức', 'pizza_sausage.jpg', TRUE),
(7, 'Pizza Phô Mai', 169000, 'pizza', 'Pizza phô mai 4 vị', 'pizza_cheese.jpg', TRUE),

-- Burger
(8, 'Burger Bò', 79000, 'burger', 'Burger bò Úc 100%', 'burger_beef.jpg', TRUE),
(9, 'Burger Gà', 69000, 'burger', 'Burger gà giòn', 'burger_chicken.jpg', TRUE),
(10, 'Burger Phô Mai', 89000, 'burger', 'Burger phô mai đặc biệt', 'burger_cheese.jpg', TRUE),

-- Rice
(11, 'Cơm Gà Teriyaki', 59000, 'rice', 'Cơm gà sốt Teriyaki', 'rice_chicken.jpg', TRUE),
(12, 'Cơm Bò Lúc Lắc', 79000, 'rice', 'Cơm bò lúc lắc', 'rice_beef.jpg', TRUE),
(13, 'Cơm Chiên Dương Châu', 49000, 'rice', 'Cơm chiên Dương Châu', 'rice_fried.jpg', TRUE),

-- Salad
(14, 'Salad Caesar', 45000, 'salad', 'Salad Caesar truyền thống', 'salad_caesar.jpg', TRUE),
(15, 'Salad Rau Trộn', 39000, 'salad', 'Salad rau củ tươi', 'salad_mix.jpg', TRUE),

-- Drink
(16, 'Coca Cola', 15000, 'drink', 'Coca Cola 330ml', 'coca.jpg', TRUE),
(17, 'Pepsi', 15000, 'drink', 'Pepsi 330ml', 'pepsi.jpg', TRUE),
(18, 'Nước Cam', 25000, 'drink', 'Nước cam tươi', 'orange_juice.jpg', TRUE),
(19, 'Trà Đào', 30000, 'drink', 'Trà đào cam sả', 'peach_tea.jpg', TRUE),
(20, 'Cà Phê Đen', 25000, 'drink', 'Cà phê đen đá', 'coffee.jpg', TRUE);

-- Thêm combo
INSERT INTO menu VALUES 
(21, 'Combo C (1 người)', 159000, 'chicken', 'Gà + Pizza + Coca', 'combo_c.jpg', TRUE);