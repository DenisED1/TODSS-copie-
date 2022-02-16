package formpackage;

import controllers.FormController;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

public class PersoonsGegevensForm extends AbstractForm {
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = false)
    protected String uuid;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = true)
    protected String functie;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = true)
    protected String begeleider;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.text, required = true, labelname = "Voornaam")
    protected String voornaam;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.text, required = true, labelname = "Achternaam")
    protected String achternaam;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.date, required = true, labelname = "Geboortedatum")
    protected String geboortedatum;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.text, required = false, labelname = "Klas")
    protected String klas;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = true)
    protected String formIdentifier;

    public PersoonsGegevensForm() {
        getInputFieldMap(this.getClass());
    }

    public String getVoornaam() {
        return getInputField(voornaam).getValue();
    }

    public String getAchternaam() {
        return getInputField(achternaam).getValue();
    }

    public String getGeboortedatum() {
        return getInputField(geboortedatum).getValue();
    }

    public String getKlas() {
        return getInputField(klas).getValue();
    }

    public void setVoornaam(String voornaam) {
        this.getInputField(this.voornaam).setValue(voornaam);
    }

    public void setAchternaam(String achternaam) {
        this.getInputField(this.achternaam).setValue(achternaam);
    }

    public void setGeboortedatum(String geboortedatum) {
        this.getInputField(this.geboortedatum).setValue(geboortedatum);
    }

    public void setKlas(String klas) {
        this.getInputField(this.klas).setValue(klas);
    }

    public String getUuid() {
        return getInputField(uuid).getValue();
    }

    public void setUuid(String uuid) {
        this.getInputField(this.uuid).setValue(uuid);
    }

    public String getFunctie() {
        return getInputField(functie).getValue();
    }

    public void setFunctie(String functie) {
        this.getInputField(this.functie).setValue(functie);
    }

    public String getBegeleider() {
        return getInputField(begeleider).getValue();
    }

    public void setBegeleider(String begeleider) {
        this.getInputField(this.begeleider).setValue(begeleider);
    }

    @Override
    public void validate() throws Exception {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        if(getVoornaam().length() == 0 )
        {
            jab.add("Geef a.u.b. een voornaam.");
        }

        if(getAchternaam().length() == 0 )
        {
            jab.add("Geef a.u.b. een achternaam");
        }

        if(getGeboortedatum().length() == 0)
        {
            jab.add("Geef a.u.b. een geboortedatum");
        }
        else if (!FormController.isValidDate(getGeboortedatum()))
        {
            jab.add("Ongeldige geboortedatum. Datum moet volgens format " + FormController.getDateFormat().toLowerCase());
        }

        //Als de jab niet leeg is
        ThrowExceptionIfErrors(jab);
    }

    @Override
    public String getFormIdentifier() {
        return formIdentifier;
    }
}
