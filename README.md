Add a Product
=============

echo '{"name":"some name", "price": 0.5, "description":"some description"}' | curl --verbose -XPOST -d @- --header "Content-Type:application/json" http://127.0.0.1:8080/products

Display all products
====================

curl -XGET http://127.0.0.1:8080/products
