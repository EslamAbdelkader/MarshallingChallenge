package com.aurasoftwareinc.java.challenge1;

import org.json.JSONObject;

public class SubclassTypes implements JsonMarshalInterface {
    public PrimitiveTypes primitiveTypes;
    public JSONTypes jsonTypes;
    private ObjectTypes objectTypes;
    private ObjectTypes[][][] objectTypesArray;

    public void populateTestData() {
        primitiveTypes = new PrimitiveTypes();
        primitiveTypes.populateTestData();

        objectTypes = new ObjectTypes();
        objectTypes.populateTestData();

        jsonTypes = new JSONTypes();
        jsonTypes.populateTestData();

        objectTypesArray = new ObjectTypes[1][2][1];
        objectTypesArray[0][0][0] = objectTypes;
        objectTypesArray[0][1][0] = objectTypes;
    }

    @Override
    public JSONObject marshalJSON() {
        return JsonMarshal.marshalJSON(this);
    }

    @Override
    public boolean unmarshalJSON(JSONObject json) {
        return JsonMarshal.unmarshalJSON(this, json);
    }
}
