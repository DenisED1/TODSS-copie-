package formpackage;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

public class UitlegForm extends AbstractForm{
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.number, required = true, labelname = "id")
    protected String id;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.number, required = true, labelname = "Volgorde")
    protected String nieuweVolgorde;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = true)
    protected String oudeVolgorde;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.text, required = true, labelname = "Zin")
    protected String nieuweTitel;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = true)
    protected String oudeTitel;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = false)
    protected String uuid;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = true)
    protected String formIdentifier;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = true)
    protected String spellingregelnaam;

    public UitlegForm(){
        getInputFieldMap(this.getClass());
    }

    public void setId(String id) {
        getInputField(this.id).setValue(id);
    }

    public String getid() {
        return getInputField(id).getValue();
    }

    public void setOudeVolgorde(String oudeVolgorde) {
        getInputField(this.oudeVolgorde).setValue(oudeVolgorde);
    }

    public void setNieuweVolgorde(String nieuweVolgorde) {
        getInputField(this.nieuweVolgorde).setValue(nieuweVolgorde);
    }

    public String getNieuweVolgorde() {
        return getInputField(nieuweVolgorde).getValue();
    }

    public String getOudeVolgorde() {
        return getInputField(oudeVolgorde).getValue();
    }

    public String getSpellingregelnaam() {
        return getInputField(spellingregelnaam).getValue();
    }

    public String getNieuweTitel() {
        return getInputField(nieuweTitel).getValue();
    }

    public void setNieuweTitel(String nieuweTitel) {
        this.getInputField(this.nieuweTitel).setValue(nieuweTitel);
    }

    public String getOudeTitel() {
        return getInputField(oudeTitel).getValue();
    }

    public void setOudeTitel(String oudeTitel) {
        this.getInputField(this.oudeTitel).setValue(oudeTitel);
    }

    public String getUuid() {
        return getInputField(uuid).getValue();
    }

    public void setUuid(String uuid) {
        this.getInputField(this.uuid).setValue(uuid);
    }

    public void setSpellingregelnaam(String spellingregelnaam) {
        getInputField(this.spellingregelnaam).setValue(spellingregelnaam);
    }

    public void setValue(String token, String value){
        getInputField(token).setValue(value);
    }

    @Override
    public void validate() throws Exception {
        JsonArrayBuilder jab = Json.createArrayBuilder();

        if(getNieuweVolgorde() == null) {
            jab.add("Kies a.u.b. een volgorde.");
        }
        else
        {
            try {
                if(Integer.parseInt(getNieuweVolgorde()) <= 0){
                    jab.add("Volgorde mag niet kleiner of gelijk aan 0 zijn.");
                }
            } catch (NumberFormatException e){
                jab.add("Volgorde moet een nummer zijn.");
            }
        }

        if(getNieuweTitel().length() == 0){
            jab.add("Vul a.u.b. een zin in.");
        }

        //Als de jab niet leeg is
        ThrowExceptionIfErrors(jab);
    }

    @Override
    public String getFormIdentifier() {
        return formIdentifier;
    }
}
