# HashGraph Wallet to Wallet Payment

Fun with HashGraph: how to implement a simple asset swap using HashGraph

The current implementation is not meant to be high-performant (for example I am using defaults Java serialization - could use Google Protobuf instead). I just wanted to focus on understanding Hashgraph by implementing a real-time asset swap.

This implementation demonstrates:

* The issuing of a balance in a specific currency: selected nodes have a random amount allocated to EUR and SGD
* A member published FX quotes for two currency pairs EUR/SGD and SGD/EUR
* Real-time cash transfer between Alice and Bob from EUR to SGD, SGD to EUR and same currency transfer

Developed in ~ 5 hours using the outstanding https://www.hederahashgraph.com/

This demo can be extended to any asset transfer

TJ

