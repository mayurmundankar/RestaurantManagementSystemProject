create database DBMS_Project;
use DBMS_Project;
CREATE TABLE menu_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100),
    description VARCHAR(255),
    price DECIMAL(10, 2),
    category VARCHAR(50),
    availability BOOLEAN
);
select *from menu_items;
delete from menu_items;