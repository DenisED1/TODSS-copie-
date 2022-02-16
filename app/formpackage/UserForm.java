package formpackage;

import java.util.ArrayList;
import java.util.List;

public class UserForm extends AbstractForm {
    @HTMLInputTypeAnnotation(HTML_INPUT_TYPE = HTMLInputType.hidden, required = true)
    protected String formIdentifier;
    private PersoonsGegevensForm persoonsGegevensForm = new PersoonsGegevensForm();
    private InlogGegevensForm inlogGegevensForm = new InlogGegevensForm();

    @Override
    public List<InputField> getInputFields() {
        List<InputField> inputFields = new ArrayList<>(super.getInputFields());
        inputFields.addAll(new ArrayList<>(persoonsGegevensForm.getInputFields()));
        inputFields.addAll(new ArrayList<>(inlogGegevensForm.getInputFields()));
        return inputFields;
    }

    @Override
    public void validate() throws Exception {
        persoonsGegevensForm.validate();
        inlogGegevensForm.validate();
    }

    @Override
    public String getFormIdentifier() {
        return formIdentifier;
    }

    public PersoonsGegevensForm getPersoonsGegevensForm(){
        return persoonsGegevensForm;
    }

    public InlogGegevensForm getInlogGegevensForm() {
        return inlogGegevensForm;
    }

    @Override
    public void setValue(String token, String value) {
        if(getPersoonsGegevensForm().getInputField(token) != null)
            getPersoonsGegevensForm().setValue(token, value);
        else if (getInlogGegevensForm().getInputField(token) != null)
            getInlogGegevensForm().setValue(token, value);
    }
}
