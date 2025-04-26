To run Springboot Application localhost:8080

1. ./run.bat

Products
@GET http://localhost:8080/store/products
Retrieves a list of all products available

@GET http://localhost:8080/store/products/find?id=1
Retrieve a product from it's id
Param {id = int}

@POST http://localhost:8080/store/products/create/
Create a new Product from Body request, example
{
"name": "Microphone",
"description": "High-quality Microphone",
"price": "90.0"
}

@PUT http://localhost:8080/store/products/?id=1
Update a product's details
Param {id = int},
Body request
{
"name": "JBL Speaker",
"description": "A really fun speaker",
"price": "32.0"
}

@DEL http://localhost:8080/store/products/1
Delete a product
Path products/{id}

Cart
@POST http://localhost:8080/store/cart/create
Create a new Cart, each new cart generates a unique integer ID, incrementing from 1

@POST http://localhost:8080/store/cart/add?id={integer}&p={integer}&q={integer}
Add a product to a cart, where p is the productid, and q is the quantity

@PUT http://localhost:8080/store/cart/subtract?id={integer}&p={integer}&q={integer}
Subtract an item from a cart, where p is the productid, and q is the quantity

@GET http://localhost:8080/store/cart?id={integer}
Retrieve a cart's item information

@GET http://localhost:8080/store/cart/cost?id={integer}
Retrieve the total cost of items in a cart

@DELETE http://localhost:8080/store/cart/clear?id={integer}
Clear a cart's items

@DELETE http://localhost:8080/store/cart/delete?id={integer}
Delete a cart
