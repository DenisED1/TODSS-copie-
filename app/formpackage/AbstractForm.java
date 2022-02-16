package formpackage;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractForm {
    private Map<String, InputField> inputFields;

    Map<String, InputField> getInputFieldMap(Class<?> clazz){
        if(inputFields == null) {
            Map<String, InputField> inputfields = new HashMap<>();
            SecureRandom random = new SecureRandom();
            int i = 1;
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(HTMLInputTypeAnnotation.class)) {
                    HTMLInputTypeAnnotation annotation = field.getAnnotation(HTMLInputTypeAnnotation.class);
                    //generate random hard to guess token
                    String token = new BigInteger(130, random).toString(32);
                    InputField inputField = new InputField(token,annotation.HTML_INPUT_TYPE(), "", annotation.required(), annotation.labelname(), i++);
                    inputfields.put(token, inputField);
                    try {
                        field.set(this, token);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            inputFields = inputfields;
        }
        return inputFields;
    }

    InputField getInputField(String token){
        if(inputFields == null)
            getInputFieldMap(this.getClass());
        return inputFields.get(token);
    }

    protected String getValue(String token){
        return getInputField(token).getValue();
    }

    public List<InputField> getInputFields(){
        if(inputFields == null)
            getInputFieldMap(this.getClass());
        InputField[] inputFieldCollection = inputFields.values().toArray(new InputField[inputFields.values().size()]);
        Arrays.sort(inputFieldCollection);
        return Arrays.asList(inputFieldCollection);
    }

    public void setValue(String token, String value){
        getInputField(token).setValue(value);
    }

    public abstract void validate() throws Exception;

    public abstract String getFormIdentifier();

    /**
     * It returns an empty string so that in the forms you can check if the string is empty or not.
     * @param jab The array builder you want to check.
     * @return It either returns a empty string or the array converted into a string
     */
    void ThrowExceptionIfErrors(JsonArrayBuilder jab) throws Exception {
        //Als de jab niet leeg is
        JsonArray jabArray = jab.build();
        if(!jabArray.isEmpty())
        {
            throw new Exception(jabArray.toString());
        }
    }
}
