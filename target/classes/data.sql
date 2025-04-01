-- 插入用戶數據
INSERT INTO users (username, password, email, full_name, phone, address, role, is_active, created_at, updated_at) VALUES
('admin', '$2a$10$eDsPDEVmNRaBDDcnS/v84u0QM3xSUvL3VZLtbEqmcLxLN5l3AqilS', 'admin@example.com', '系統管理員', '1234567890', '管理員地址', 'ADMIN', true, NOW(), NOW()),
('user1', '$2a$10$eDsPDEVmNRaBDDcnS/v84u0QM3xSUvL3VZLtbEqmcLxLN5l3AqilS', 'user1@example.com', '普通用戶1', '0987654321', '用戶1地址', 'USER', true, NOW(), NOW()),
('user2', '$2a$10$eDsPDEVmNRaBDDcnS/v84u0QM3xSUvL3VZLtbEqmcLxLN5l3AqilS', 'user2@example.com', '普通用戶2', '1357924680', '用戶2地址', 'USER', true, NOW(), NOW());

-- 插入分類數據
INSERT INTO categories (name, description, image_url, is_active, created_at, updated_at) VALUES
('電子產品', '各類電子設備和配件', 'https://example.com/images/electronics.jpg', true, NOW(), NOW()),
('家居用品', '家具、裝飾和各類家用物品', 'https://example.com/images/home.jpg', true, NOW(), NOW()),
('服裝鞋包', '男女服裝、鞋子和包包', 'https://example.com/images/fashion.jpg', true, NOW(), NOW()),
('美妝個護', '化妝品和個人護理產品', 'https://example.com/images/beauty.jpg', true, NOW(), NOW()),
('食品飲料', '各類食品、飲料和零食', 'https://example.com/images/food.jpg', true, NOW(), NOW());

-- 插入產品數據 - 電子產品
INSERT INTO products (name, description, price, image_url, stock, category_id, is_active, created_at, updated_at) VALUES
('智能手機', '高清屏幕，強大處理器，專業攝像頭', 5999.00, 'https://example.com/images/smartphone.jpg', 50, 1, true, NOW(), NOW()),
('筆記本電腦', '輕薄便攜，長效電池，高性能', 7599.00, 'https://example.com/images/laptop.jpg', 30, 1, true, NOW(), NOW()),
('無線藍牙耳機', '高音質，降噪，長時間舒適佩戴', 899.00, 'https://example.com/images/headphones.jpg', 100, 1, true, NOW(), NOW()),
('智能手錶', '健康監測，防水設計，多運動模式', 1299.00, 'https://example.com/images/smartwatch.jpg', 45, 1, true, NOW(), NOW()),
('平板電腦', '高清大屏，輕薄設計，適合娛樂和工作', 3699.00, 'https://example.com/images/tablet.jpg', 25, 1, true, NOW(), NOW());

-- 插入產品數據 - 家居用品
INSERT INTO products (name, description, price, image_url, stock, category_id, is_active, created_at, updated_at) VALUES
('北歐風沙發', '舒適布藝，簡約設計，多色可選', 3999.00, 'https://example.com/images/sofa.jpg', 10, 2, true, NOW(), NOW()),
('智能燈具', '可調亮度和色溫，支持語音控制', 399.00, 'https://example.com/images/light.jpg', 60, 2, true, NOW(), NOW()),
('多功能廚具套裝', '不粘鍋，耐用材質，適合各種烹飪方式', 899.00, 'https://example.com/images/cookware.jpg', 40, 2, true, NOW(), NOW()),
('記憶棉床墊', '舒適支撐，透氣設計，讓您睡得更香', 1799.00, 'https://example.com/images/mattress.jpg', 15, 2, true, NOW(), NOW()),
('智能掃地機器人', '自動清潔，多種模式，APP遠程控制', 1999.00, 'https://example.com/images/robot.jpg', 20, 2, true, NOW(), NOW());

-- 插入產品數據 - 服裝鞋包
INSERT INTO products (name, description, price, image_url, stock, category_id, is_active, created_at, updated_at) VALUES
('男士休閒西裝', '修身剪裁，舒適面料，適合多種場合', 1299.00, 'https://example.com/images/suit.jpg', 30, 3, true, NOW(), NOW()),
('女士連衣裙', '優雅設計，舒適面料，適合春夏穿著', 699.00, 'https://example.com/images/dress.jpg', 50, 3, true, NOW(), NOW()),
('真皮手提包', '優質真皮，時尚設計，大容量', 1599.00, 'https://example.com/images/bag.jpg', 25, 3, true, NOW(), NOW()),
('運動鞋', '輕便舒適，抓地力強，適合日常運動', 899.00, 'https://example.com/images/shoes.jpg', 70, 3, true, NOW(), NOW()),
('羽絨服', '保暖輕盈，防風防水，適合冬季穿著', 1799.00, 'https://example.com/images/coat.jpg', 35, 3, true, NOW(), NOW());

-- 插入訂單數據
INSERT INTO orders (order_number, user_id, status, total_amount, shipping_address, payment_method, created_at, updated_at) VALUES
('ORD-1001', 2, 'DELIVERED', 7598.00, '用戶1地址', '信用卡', NOW() - INTERVAL 30 DAY, NOW() - INTERVAL 25 DAY),
('ORD-1002', 3, 'SHIPPED', 2198.00, '用戶2地址', '支付寶', NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 5 DAY),
('ORD-1003', 2, 'PENDING', 1799.00, '用戶1地址', '微信支付', NOW(), NOW());

-- 插入訂單項目數據
INSERT INTO order_items (order_id, product_id, quantity, price, created_at, updated_at) VALUES
(1, 2, 1, 7599.00, NOW() - INTERVAL 30 DAY, NOW() - INTERVAL 30 DAY),
(2, 3, 1, 899.00, NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 7 DAY),
(2, 7, 1, 399.00, NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 7 DAY),
(2, 13, 1, 899.00, NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 7 DAY),
(3, 14, 1, 1799.00, NOW(), NOW());

-- 插入購物車項目數據
INSERT INTO cart_items (user_id, product_id, quantity, created_at, updated_at) VALUES
(2, 5, 1, NOW(), NOW()),
(2, 8, 2, NOW(), NOW()),
(3, 10, 1, NOW(), NOW()),
(3, 15, 1, NOW(), NOW());

-- 插入評論數據
INSERT INTO reviews (user_id, product_id, rating, comment, is_active, created_at, updated_at) VALUES
(2, 1, 5, '非常好用的手機，續航和拍照都很滿意！', true, NOW() - INTERVAL 60 DAY, NOW() - INTERVAL 60 DAY),
(3, 1, 4, '整體不錯，就是價格有點高。', true, NOW() - INTERVAL 45 DAY, NOW() - INTERVAL 45 DAY),
(2, 7, 5, '燈光非常好，APP控制也很方便。', true, NOW() - INTERVAL 30 DAY, NOW() - INTERVAL 30 DAY),
(3, 13, 4, '鞋子很舒適，穿著跑步很輕鬆。', true, NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 15 DAY),
(2, 2, 5, '電腦性能很好，辦公娛樂都很流暢。', true, NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 7 DAY);

-- 添加測試用戶數據 (密碼為加密前的 "password")
INSERT INTO users (username, password, email, full_name, phone, address, role, is_active) VALUES
('admin', '$2a$10$X4/vF4Vqt0eJoP87MUBWYuyNjI3XPXJ.0c5ZsJzw/hOTnxOhR3xQK', 'admin@example.com', '管理員', '1234567890', '北京市海淀區', 'ADMIN', true),
('user1', '$2a$10$X4/vF4Vqt0eJoP87MUBWYuyNjI3XPXJ.0c5ZsJzw/hOTnxOhR3xQK', 'user1@example.com', '張三', '1380000001', '上海市浦東新區', 'USER', true),
('user2', '$2a$10$X4/vF4Vqt0eJoP87MUBWYuyNjI3XPXJ.0c5ZsJzw/hOTnxOhR3xQK', 'user2@example.com', '李四', '1390000002', '廣州市天河區', 'USER', true);

-- 添加測試分類數據
INSERT INTO categories (name, description, image_url, is_active) VALUES
('電子產品', '各種電子設備和配件', 'https://example.com/images/electronics.jpg', true),
('服裝', '時尚服裝和配飾', 'https://example.com/images/clothing.jpg', true),
('家居用品', '家居裝飾和生活用品', 'https://example.com/images/home.jpg', true),
('書籍', '各種書籍和雜誌', 'https://example.com/images/books.jpg', true),
('食品', '食品和飲料', 'https://example.com/images/food.jpg', true);

-- 添加測試產品數據
INSERT INTO products (name, description, price, image_url, stock, category_id, is_active) VALUES
('iPhone 13', '蘋果最新款智能手機', 6999.00, 'https://example.com/images/iphone13.jpg', 100, 1, true),
('MacBook Pro', '專業級筆記本電腦', 12999.00, 'https://example.com/images/macbook.jpg', 50, 1, true),
('Nike運動鞋', '舒適耐用的運動鞋', 699.00, 'https://example.com/images/nike.jpg', 200, 2, true),
('棉質T恤', '舒適透氣的休閒T恤', 99.00, 'https://example.com/images/tshirt.jpg', 300, 2, true),
('智能燈具', '可語音控制的智能燈具', 299.00, 'https://example.com/images/light.jpg', 150, 3, true),
('廚房置物架', '多功能廚房收納架', 199.00, 'https://example.com/images/rack.jpg', 100, 3, true),
('哈利波特全集', '魔法世界經典小說', 399.00, 'https://example.com/images/harrypotter.jpg', 80, 4, true),
('JavaScript高級程序設計', '前端開發必讀書籍', 89.00, 'https://example.com/images/javascript.jpg', 60, 4, true),
('有機蔬菜包', '新鮮有機蔬菜組合', 59.00, 'https://example.com/images/vegetables.jpg', 30, 5, true),
('進口零食禮盒', '各國特色零食組合', 129.00, 'https://example.com/images/snacks.jpg', 40, 5, true);

-- 添加測試訂單數據
INSERT INTO orders (order_number, user_id, status, total_amount, shipping_address, payment_method) VALUES
('ORD20230001', 2, 'COMPLETED', 7698.00, '上海市浦東新區張江高科技園區', 'ALIPAY'),
('ORD20230002', 2, 'PROCESSING', 299.00, '上海市浦東新區張江高科技園區', 'WECHAT'),
('ORD20230003', 3, 'SHIPPED', 798.00, '廣州市天河區體育西路', 'CREDIT_CARD');

-- 添加測試訂單項目數據
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(1, 1, 1, 6999.00),
(1, 3, 1, 699.00),
(2, 5, 1, 299.00),
(3, 3, 1, 699.00),
(3, 4, 1, 99.00);

-- 添加測試購物車數據
INSERT INTO cart_items (user_id, product_id, quantity) VALUES
(2, 2, 1),
(2, 4, 2),
(3, 1, 1),
(3, 6, 1);

-- 添加測試評論數據
INSERT INTO reviews (user_id, product_id, rating, comment, is_active) VALUES
(2, 1, 5, 'iPhone 13的性能和拍照功能都非常出色，電池續航也有明顯提升。', true),
(2, 3, 4, '鞋子很舒適，但尺碼偏小，建議購買大一號的。', true),
(3, 5, 5, '智能燈具很好用，可以通過手機APP控制，也支持語音控制。', true),
(3, 7, 4, '印刷和裝訂質量不錯，是收藏的好選擇。', true); 