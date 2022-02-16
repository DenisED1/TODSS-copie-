package validators;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

public abstract class Validator {
    static JsonArrayBuilder jab;

    protected static void ThrowExceptionIfErrors(JsonArrayBuilder jab) throws Exception {
        if(jab == null) return;

        JsonArray jabArray = jab.build();
        if(!jabArray.isEmpty())
        {
            throw new Exception(jabArray.toString());
        }
    }

    protected  static void emptyJab(){
        jab = null;
    }

    protected static String jab(){
        return jab.build().toString();
    }

    protected static JsonArrayBuilder jab(String input){
        if(jab == null){
             jab = Json.createArrayBuilder();
        }

        jab.add(input);

        return jab;
    }
}
