package formpackage;

import java.util.List;

public class InputField implements Comparable<InputField> {
    private String name;
    private HTMLInputType htmlInputType;
    private String value;
    private List<String> values;
    private boolean required;
    private String labelname;
    private int volgorde;

    public InputField(String name, HTMLInputType htmlInputType) {
        this(name, htmlInputType, "");
    }

    public InputField(String name, HTMLInputType htmlInputType, String value) {
        this(name, htmlInputType, value, false);
    }

    public InputField(String name, HTMLInputType htmlInputType, String value, boolean required) {
        this(name,htmlInputType,value,required,"");
    }

    public InputField(String name, HTMLInputType htmlInputType, String value, boolean required, String labelname, int volgorde) {
        this.name = name;
        this.htmlInputType = htmlInputType;
        this.value = value;
        this.required = required;
        this.labelname = labelname;
        this.volgorde = volgorde;
    }

    public InputField(String name, HTMLInputType htmlInputType, String value, boolean required, String labelname) {
        this(name, htmlInputType, value, required, labelname, 100);
    }

    public String getName() {
        return name;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabelname() {
        return labelname;
    }

    public HTMLInputType getHtmlInputType() {
        return htmlInputType;
    }

    public String getValue() {
        return value;
    }

    public String getRequired(){
        if(required)
            return "required";
        return "";
    }

    public int getVolgorde() {
        return volgorde;
    }

    public boolean isRequired() {
        return required;
    }

    @Override
    public int compareTo(InputField o) {
        return Integer.compare(getVolgorde(), o.getVolgorde());
    }
}
