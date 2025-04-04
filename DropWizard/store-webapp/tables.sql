CREATE TABLE cart (
	cartid BIGSERIAL PRIMARY KEY );

CREATE TABLE product (
	productid BIGSERIAL NOT NULL PRIMARY KEY,
	name VARCHAR(32) NOT NULL,
    description VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL ) ;

CREATE TABLE cart-products (
	cartid BIGSERIAL NOT NULL,
    productid  BIGSERIAL NOT NULL, 
    quantity INT );
