# challenge.java
Android Studio / Java Challenge new Candidates

This is an Android Studio project. 

It contains a test case for the implementation of a so called Marshaller-Interface.

Its purpose is to marshal any Java Class which supports the given interface into a JSON-Object and back.

### Step 1 - Fork this repository into Your github account

* Create a personal account if You have none, its free.

### Step 2 - Clone, compile and test Your forked project

* Use Android Studio
* Do not use Eclipse

### Step 3 - Implement two methods in Class JsonMarshal

public static JSONObject marshalJSON(Object object)

public static boolean unmarshalJSON(Object object, JSONObject json)

* Use Reflection
* Do not use any third party code
* Document both methods

### Step 4 - Check in and commit Your solution onto github

### Step 5 - Mail link to Your repository to recruiter

### -------------------------------------------------------------------------------------------------------

### Solution

* Task is implemented using only reflection as stated.
* The implementation includes a recursive solution to marshal any java class the implements Marshal interface, as long as all its fields are either primitives, wrappers, strings, JSONObjects, JSONArrays, one dimension and multidimensional arrays. And of course unmarshaling them back.
* The solution does not support marshaling or unmarshaling Generic classes.

### Note
I have added a multidimensional array in the SubclassTypes class and an array in PrimitiveTypes class, to be able to test them, since the test case doesn't cover that.
