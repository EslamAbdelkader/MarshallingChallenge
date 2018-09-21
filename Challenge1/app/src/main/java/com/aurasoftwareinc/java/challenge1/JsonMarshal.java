package com.aurasoftwareinc.java.challenge1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JsonMarshal {
    private static final Set<Class> WRAPPER_TYPES =
            new HashSet(Arrays.asList(Boolean.class, Character.class, Byte.class,
                    Short.class, Integer.class, Long.class, Float.class, Double.class));

    /**
     * Marshals the object recursively by marshaling all its fields that are either primitives,
     * wrappers, strings, JSONObjects, JSONArrays, arrays, or objects that implements JavaMarshalInterface.
     *
     * @param object The object to be marshaled
     * @return The Marshaled JSON
     */
    public static JSONObject marshalJSON(Object object) {
        JSONObject json = new JSONObject();
        try {
            //Loop over the fields of the object
            for (Field field : object.getClass().getDeclaredFields()) {
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                Object fieldObject = field.get(object);
                Class<?> type = field.getType();
                String fieldName = field.getName();

                // Do not marshal null fields
                if (fieldObject == null) {
                    continue;
                }

                if (isJsonType(type)) {
                    //
                    // If can be directly marshaled
                    //
                    json.put(fieldName, fieldObject);
                } else if (type.isArray()) {
                    //
                    // If Array
                    //
                    List list = marshalArray(fieldObject);
                    json.put(field.getName(), new JSONArray(list));
                } else if (JsonMarshalInterface.class.isAssignableFrom(type)) {
                    //
                    // If implements JavaMarshalInterface
                    //
                    json.put(fieldName, ((JsonMarshalInterface) fieldObject).marshalJSON());
                }
                field.setAccessible(isAccessible);
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        return json;
    }

    /**
     * Recursively unmarshals a JSONObject and fills the corresponding object's fields.
     *
     * @param object the object to be filled as a result of the unmarshaling operation.
     * @param json   the json to be marshaled
     * @return true if marshaling was successful, false otherwise.
     */
    public static boolean unmarshalJSON(Object object, JSONObject json) {
        //
        // Todo: replace contents of this method with Your code.
        //

        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                Class<?> type = field.getType();
                String fieldName = field.getName();

                if (!json.has(fieldName))
                    continue;

                if (isJsonType(type)) {
                    //
                    // If json type
                    //
                    field.set(object, json.get(fieldName));
                } else if (type.isArray()) {
                    //
                    // If Array
                    //
//                    List list = unmarshalArray(fieldObject);
                    JSONArray jsonArray = json.getJSONArray(fieldName);
                    Object array = unmarshalArray(type, jsonArray);
                    field.set(object, array);
                } else if (JsonMarshalInterface.class.isAssignableFrom(type)) {
                    //
                    // If Object
                    //
                    Object o = type.newInstance();
                    ((JsonMarshalInterface) o).unmarshalJSON((JSONObject) json.get(fieldName));
                    field.set(object, o);
                }
                field.setAccessible(isAccessible);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    //region Arrays

    /**
     * Marshals an array (recursively if multidimensional)
     *
     * @param array The array to be marshaled
     * @return A List of Objects that can be directly marshaled to JSON
     */
    private static List<Object> marshalArray(Object array) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < Array.getLength(array); i++) {
            Object arrayObject = Array.get(array, i);
            if (arrayObject != null) {
                if (isJsonType(arrayObject.getClass())) {
                    list.add(arrayObject);
                } else if (arrayObject.getClass().isArray()) {
                    list.add(marshalArray(arrayObject));
                } else if (arrayObject instanceof JsonMarshalInterface) {
                    list.add(((JsonMarshalInterface) arrayObject).marshalJSON());
                }
            }
        }
        return list;
    }

    /**
     * Unmarshals a JSONArray, given the expected type of the unmarshal operation.
     *
     * @param type      the expected array type of the unmarshal operation
     * @param jsonArray the array to be unmarshaled
     * @return an object (that's actually an array) as a result of the unmarshal operation.
     */
    private static Object unmarshalArray(Class<?> type, JSONArray jsonArray) {
        type = type.getComponentType();
        Object array = Array.newInstance(type, jsonArray.length());
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Object jsonElement = jsonArray.get(i);
                if (jsonElement instanceof JSONArray) {
                    Array.set(array, i, unmarshalArray(type, (JSONArray) jsonElement));
                } else if (isJsonType(type)) {
                    Array.set(array, i, jsonElement);
                } else if (JsonMarshalInterface.class.isAssignableFrom(type)) {
                    JsonMarshalInterface marshalledObject = (JsonMarshalInterface) type.newInstance();
                    marshalledObject.unmarshalJSON((JSONObject) jsonElement);
                    Array.set(array, i, marshalledObject);
                }
            }
            return array;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //endregion

    //region Helper

    /**
     * Checks if a type is one of the Wrapper types
     *
     * @param type The type to be checked
     * @return true if type is one of the Wrapper types, false otherwise.
     */
    public static boolean isWrapperType(Class type) {
        return WRAPPER_TYPES.contains(type);
    }

    /**
     * Checks if an object of a specific type can be directly marshaled to JSON,
     * that's the type is either primitive, wrapper, string, JSONObject or JSONArray.
     *
     * @param type the type to be checked
     * @return true if can be directly marshaled to JSON, false otherwise.
     */
    private static boolean isJsonType(Class<?> type) {
        return type.isPrimitive()
                || isWrapperType(type)
                || type.getName().equals(String.class.getName())
                || type.getName().equals(JSONObject.class.getName())
                || type.getName().equals(JSONArray.class.getName());
    }
    //endregion
}