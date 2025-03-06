CREATE TABLE cart (
    cartID BIGSERIAL NOT NULL PRIMARY KEY
);

CREATE TABLE cart_products (
    cartID BIGSERIAL NOT NULL,
    productID INT NOT NULL,
    quantity INT 
);

CREATE TABLE product (
    productID INT NOT NULL PRIMARY KEY,
    name VARCHAR(32) NOT NULL,
    description VARCHAR(255),
    price DOUBLE NOT NULL
);


INSERT INTO product (productID, name, price)
VALUES(1, "JBL speaker", 39.99)
VALUES(2, "PTZ Camera", 299.99)
VALUES(3, "Wireless Microphone", 159.99);