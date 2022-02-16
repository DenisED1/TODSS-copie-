package formpackage;

public class CategorieForm extends AbstractForm{
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.text, required = true, labelname = "Categorienaam")
    protected String nieuweCategorienaam;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = true)
    protected String oudeCategorienaam;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = true)
    protected String spellingregelnaam;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = true)
    protected String formIdentifier;

    public CategorieForm(){
        getInputFieldMap(this.getClass());
    }

    public void setNieuweCategorienaam(String nieuweCategorienaam) {
        getInputField(this.nieuweCategorienaam).setValue(nieuweCategorienaam);
    }

    public void setOudeCategorienaam(String oudeCategorienaam) {
        getInputField(this.oudeCategorienaam).setValue(oudeCategorienaam);
    }

    public void setSpellingregelnaam(String spellingregelnaam) {
        getInputField(this.spellingregelnaam).setValue(spellingregelnaam);
    }

    public String getNieuweCategorienaam() {
        return getInputField(nieuweCategorienaam).getValue();
    }

    public String getOudeCategorienaam() {
        return getInputField(oudeCategorienaam).getValue();
    }

    public String getSpellingregelnaam() {
        return getInputField(this.spellingregelnaam).getValue();
    }

    public void setValue(String token, String value){
        getInputField(token).setValue(value);
    }

    @Override
    public void validate() throws Exception {
    }

    @Override
    public String getFormIdentifier() {
        return formIdentifier;
    }
}
