package controllers;

import formpackage.AbstractForm;
import play.mvc.Controller;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FormController extends Controller{
    private static String dateFormat = "dd/MM/yyyy";
    private static DateFormat format = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
    public static AbstractForm handleFormRequest(Map<String, String[]> formparams, List<AbstractForm> formList) {
        AbstractForm form = null;
        for(AbstractForm uitlegForm2 : formList){
            if(formparams.get(uitlegForm2.getFormIdentifier()) != null){
                form =  uitlegForm2;
                break;
            }
        }
        if(form != null) {
            for (Map.Entry<String, String[]> entry : formparams.entrySet()) {
                form.setValue(entry.getKey(), entry.getValue()[0]);
            }
        }
        return form;
    }

    public static DateFormat getDateFormatter(){
        return format;
    }

    public static boolean isValidDate(String date){
        boolean valid = date.length() == 10;
        try {
            valid = valid && Integer.parseInt(date.substring(0, 2)) >= 1 && Integer.parseInt(date.substring(0, 2)) <= 31;
            valid = valid && Integer.parseInt(date.substring(3, 5)) >= 1 && Integer.parseInt(date.substring(3, 5)) <= 12;
            valid = valid && Integer.parseInt(date.substring(6, 10)) >= 1900 && Integer.parseInt(date.substring(6, 10)) <= Calendar.getInstance().get(Calendar.YEAR);
            valid = valid && date.substring(2,3).equals("/") && date.substring(5,6).equals("/");
        } catch(NumberFormatException e) {
           return false;
        }
        return valid;
    }

    public static Date parseDate(String dateText){
        Date date = null;
        try {
             date = format.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String DateToString(Date date){
        return format.format(date);
    }

    public static void deleteFile(String filepath){
        File file = new File("public/" + filepath);
        file.delete();
    }

    public static String getDateFormat() {
        return dateFormat;
    }
}
