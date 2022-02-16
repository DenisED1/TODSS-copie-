package formpackage;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

public class SpellingregelForm extends AbstractForm {
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = true)
    protected String formIdentifier;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.text, required = true, labelname = "SpellingRegel naam")
    protected String spellingregelnaam;

    public SpellingregelForm() {
        getInputFieldMap(this.getClass());
    }

    public String getSpellingregelnaam() {
        return getInputField(spellingregelnaam).getValue();
    }

    public void setSpellingregelnaam(String spellingregelnaam) {
        getInputField(this.spellingregelnaam).setValue(spellingregelnaam);
    }

    @Override
    public void validate() throws Exception {
        if(getSpellingregelnaam() == null || getSpellingregelnaam().isEmpty()) {
            JsonArrayBuilder jab = Json.createArrayBuilder();
            jab.add("Je moet een naam opgeven");

            throw new Exception(jab.build().toString());
        }
    }

    @Override
    public String getFormIdentifier() {
        return formIdentifier;
    }
}
