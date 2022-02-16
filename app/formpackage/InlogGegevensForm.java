package formpackage;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

public class InlogGegevensForm extends AbstractForm {
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = false)
    protected String uuid;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = true)
    protected String oudeGebruikersnaam;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.text, required = true, labelname = "Gebruikersnaam")
    protected String gebruikersnaam;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.password, required = true, labelname = "Wachtwoord")
    protected String wachtwoord1;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.password, required = true, labelname = "Herhaal wachtwoord")
    protected String wachtwoord2;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = true)
    protected String formIdentifier;

    public InlogGegevensForm() {
        getInputFieldMap(this.getClass());
    }

    public String getGebruikersnaam() {
        return getInputField(gebruikersnaam).getValue();
    }

    public void setGebruikersnaam(String gebruikersnaam) {
        this.getInputField(this.gebruikersnaam).setValue(gebruikersnaam);
    }

    public String getWachtwoord1() {
        return getInputField(wachtwoord1).getValue();
    }

    public String getWachtwoord2() {
        return getInputField(wachtwoord2).getValue();
    }

    public void setWachtwoord1(String wachtwoord1) {
        this.getInputField(this.wachtwoord1).setValue(wachtwoord1);
    }

    public void setWachtwoord2(String wachtwoord2) {
        this.getInputField(this.wachtwoord2).setValue(wachtwoord2);
    }

    public String getUuid() {
        return getInputField(uuid).getValue();
    }

    public void setUuid(String uuid) {
        this.getInputField(this.uuid).setValue(uuid);
    }

    public String getOudeGebruikersnaam() {
        return getInputField(oudeGebruikersnaam).getValue();
    }

    public void setOudeGebruikersnaam(String oudeGebruikersnaam) {
        this.getInputField(this.oudeGebruikersnaam).setValue(oudeGebruikersnaam);
    }

    @Override
    public void validate() throws Exception {
        JsonArrayBuilder jab = Json.createArrayBuilder();

        //Check gebruikersnaam
        if(getGebruikersnaam().length() == 0)
        {
            jab.add("Vul a.u.b. een gebruikersnaam in.");
        }
        else if(getGebruikersnaam().length() < 5)
        {
            jab.add("Gebruikersnaam moet minimaal vijf karakters bevatten.");
        }

        //Check wachtwoord
        if(getWachtwoord1().length() == 0 || getWachtwoord2().length() == 0)
        {
            jab.add("Vul a.u.b. een wachtwoord in");
        }
        else if(!getWachtwoord1().equals(getWachtwoord2()))
        {
            jab.add("De wachtwoorden komen niet overeen.");
        }
        else if(!getWachtwoord1().matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z].*[a-z].*[a-z]).{6,}$"))
        {
            jab.add("Wachtwoord moet minimaal zes tekens lang zijn, één hoofdletter, drie kleineletters en één cijfer bevatten.");
        }

        //Als de jab niet leeg is
        ThrowExceptionIfErrors(jab);
    }

    @Override
    public String getFormIdentifier() {
        return formIdentifier;
    }

}
