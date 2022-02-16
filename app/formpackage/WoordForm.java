package formpackage;

import controllers.FormController;
import model.Moeilijkheidsgraad;
import org.apache.commons.lang3.EnumUtils;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import java.util.List;

public class WoordForm extends AbstractForm {
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = false)
    protected String oudWoord;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.text, required = true, labelname = "Woord")
    protected String nieuwWoord;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = false)
    protected String oudeMoeilijkheidsgraad;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.options, required = true, labelname = "Moeilijkheid")
    protected String nieuweMoeilijkheidsgraad;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = false)
    protected String oudeCategorie;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.options, required = true, labelname = "Categorie")
    protected String nieuweCategorie;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required =  false)
    protected String oudeUitspraak;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.file, required =  true, labelname = "Uitspraak")
    protected String nieuweUitspraak;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = true)
    protected String spellingregelnaam;
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = true)
    protected String formIdentifier;

    public WoordForm(){
        getInputFieldMap(this.getClass());
    }

    public String getOudWoord() {
        return getInputField(oudWoord).getValue();
    }

    public String getNieuwWoord() {
        return getInputField(nieuwWoord).getValue();
    }

    public String getOudeUitspraak() {
        return getInputField(oudeUitspraak).getValue();
    }

    public String getNieuweUitspraak() {
        return getInputField(nieuweUitspraak).getValue();
    }

    public String getOudeMoeilijkheidsgraad() {
        return getInputField(oudeMoeilijkheidsgraad).getValue();
    }

    public List<String> getNieuweMoeilijkheidsgraaden() {
        return getInputField(nieuweMoeilijkheidsgraad).getValues();
    }

    public String getOudeCategorie() {
        return getInputField(oudeCategorie).getValue();
    }

    public List<String> getNieuweCategorieen() {
        return getInputField(nieuweCategorie).getValues();
    }

    public String getSpellingregelnaam() {
        return getInputField(spellingregelnaam).getValue();
    }

    public void setSpellingregelnaam(String spellingregelnaam) {
        getInputField(this.spellingregelnaam).setValue(spellingregelnaam);
    }

    public void setOudWoord(String oudWoord) {
        getInputField(this.oudWoord).setValue(oudWoord);
    }

    public void setNieuwWoord(String nieuwWoord) {
        getInputField(this.nieuwWoord).setValue(nieuwWoord);
    }

    public void setOudeMoeilijkheidsgraad(String oudeMoeilijkheidsgraad) {
        getInputField(this.oudeMoeilijkheidsgraad).setValue(oudeMoeilijkheidsgraad);
    }

    public void setNieuweMoeilijkheidsgraaden(List<String> nieuweMoeilijkheidsgraad) {
        getInputField(this.nieuweMoeilijkheidsgraad).setValues(nieuweMoeilijkheidsgraad);
    }

    public void setOudeCategorie(String oudeCategorie) {
        getInputField(this.oudeCategorie).setValue(oudeCategorie);
    }

    public void setNieuweCategorieen(List<String> nieuweCategorie) {
        getInputField(this.nieuweCategorie).setValues(nieuweCategorie);
    }

    public void setOudeUitspraak(String oudeUitspraak) {
        getInputField(this.oudeUitspraak).setValue(oudeUitspraak);
    }

    public void setNieuweUitspraak(String nieuweUitspraak) {
        getInputField(this.nieuweUitspraak).setValue(nieuweUitspraak);
    }

    public String getNieuweMoeilijkheidsgraad() {
        return getInputField(this.nieuweMoeilijkheidsgraad).getValue();
    }

    public void setNieuweMoeilijkheidsgraad(String nieuweMoeilijkheidsgraad) {
        getInputField(this.nieuweMoeilijkheidsgraad).setValue(nieuweMoeilijkheidsgraad);
    }

    public String getNieuweCategorie() {
        return getInputField(nieuweCategorie).getValue();
    }

    public void setNieuweCategorie(String nieuweCategorie) {
        getInputField(this.nieuweCategorie).setValue(nieuweCategorie);
    }

    @Override
    public void validate() throws Exception {
        JsonArrayBuilder jab = Json.createArrayBuilder();

        if(getNieuweMoeilijkheidsgraad().length() == 0){
            jab.add("Kies a.u.b. een moeilijkheidsgraad.");
        }

        if(!EnumUtils.isValidEnum(Moeilijkheidsgraad.class, getNieuweMoeilijkheidsgraad())){
            jab.add("De gekozen moeilijkheidsgraad " + getNieuweMoeilijkheidsgraad() + " bestaat niet");
        }

        if(getNieuweCategorie().length() == 0){
            jab.add("Kies a.u.b. een categorie.");
        }

        if(getNieuwWoord().length() == 0){
            jab.add("Vul a.u.b. een woord in.");
        }

        if(getNieuweUitspraak().length() == 0){
            jab.add("Kies a.u.b. een uitspraak bestand");
        } else if(!getNieuweUitspraak().substring(getNieuweUitspraak().lastIndexOf("."), getNieuweUitspraak().length()).contains("mp3")) {
            jab.add("Alleen mp3 bestanden worden ondersteund.");
            FormController.deleteFile(getNieuweUitspraak());
        }

        //Als de jab niet leeg is
        ThrowExceptionIfErrors(jab);
    }

    @Override
    public String getFormIdentifier() {
        return formIdentifier;
    }
}
