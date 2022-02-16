package formpackage;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

public class LoginForm extends AbstractForm {
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.text, labelname = "Gebruikersnaam", required = true)
    protected String gebruikersnaam;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.password, labelname = "Wachtwoord", required = true)
    protected String wachtwoord;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = true)
    protected String formIdentifier;

    public LoginForm(){
        getInputFieldMap(this.getClass());
    }

    public String getGebruikersnaam() {
        return getInputField(gebruikersnaam).getValue();
    }

    public void setGebruikersnaam(String gebruikersnaam) {
        getInputField(this.gebruikersnaam).setValue(gebruikersnaam);
    }

    public String getWachtwoord() {
        return getInputField(wachtwoord).getValue();
    }

    public void setWachtwoord(String wachtwoord) {
        getInputField(this.wachtwoord).setValue(wachtwoord);
    }

    @Override
    public void validate() throws Exception {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        boolean fail = false;

        //validate InputFields
        if(getWachtwoord() == null || getWachtwoord().isEmpty()){
            jab.add("Je moet een wachtwoord invullen.");
            fail = true;
        }

        if(getGebruikersnaam() == null || getGebruikersnaam().isEmpty()){
            jab.add("Je moet een gebruikersnaam invullen.");
            fail = true;
        }

        if(fail){
            ThrowExceptionIfErrors(jab);
        }
    }

    @Override
    public String getFormIdentifier() {
        return formIdentifier;
    }
}
