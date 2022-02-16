package controllers;

import authentication.Administrator;
import dao.SpellingRegel.ISpellingRegelDao;
import dao.SpellingRegel.SpellingRegelDao;
import dao.Uitleg.IUitlegDao;
import dao.Uitleg.UitlegDao;
import formpackage.AbstractForm;
import formpackage.UitlegForm;
import model.Uitleg;
import model.UitlegRegel;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.dynamicmodaldelete;
import views.html.dynamicmodalform;
import views.html.main;
import views.html.uitlegaanpassen;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonParsingException;
import java.io.File;
import java.io.StringReader;
import java.util.*;

@Security.Authenticated(Administrator.class)
public class UitlegController extends Controller {
    private static List<AbstractForm> uitlegForms = new ArrayList<>();

    public static Result createUitlegPage(){
        List<UitlegRegel> uitlegRegels = new ArrayList<>();
        String pathToImages = "images/icons/";
        uitlegRegels.add(new UitlegRegel(Application.generateUUID(), "", "", pathToImages + "horen.png", "Spreek uit"));
        uitlegRegels.add(new UitlegRegel(Application.generateUUID(), "", "", pathToImages + "nazeggen.png", "Nazeggen"));
        uitlegRegels.add(new UitlegRegel(Application.generateUUID(), "", "", pathToImages + "klankgroepzeggen.png", "Spreek uit"));
        uitlegRegels.add(new UitlegRegel(Application.generateUUID(), "", "", pathToImages + "Question_Mark.png", "Hoe zit het"));
        uitlegRegels.add(new UitlegRegel(Application.generateUUID(), "", "", pathToImages + "intikken.png", "Intikken"));
        uitlegRegels.add(new UitlegRegel(Application.generateUUID(), "", "", pathToImages + "controleren.png", "Controleren"));
        UitlegForm uitlegForm = new UitlegForm();
        uitlegForm.setNieuweTitel("");
        return ok(main.render("Uitleg aanpassen", uitlegaanpassen.render(request().body().asFormUrlEncoded().get("spellingregelnaam")[0], uitlegRegels, uitlegForm)));
    }

    @Transactional
    public static Result createUitleg(){
        IUitlegDao uitlegDao = new UitlegDao();
        ISpellingRegelDao spellingRegelDao = new SpellingRegelDao();
        Map<String, String[]> formParams = new HashMap<>(request().body().asMultipartFormData().asFormUrlEncoded());
        String spellingregelnaam = formParams.get("spellingregelnaam")[0];
        formParams.remove("spellingregelnaam");

        Uitleg uitleg = getData(formParams);
        uitleg.setSpellingRegel(spellingRegelDao.getByName(spellingregelnaam));

        Uitleg oudeUitleg = uitlegDao.getByUuId(uitleg.getUuid());
        if(oudeUitleg != null){
            uitlegDao.removeIncludesRegels(oudeUitleg);
        }

        uitlegDao.insert(uitleg);

        flash("success-message", "Uitleg is succesvol aangemaakt.");
        flash("spellingregelnaam", spellingregelnaam);

        return SpellingregelController.redirectToBeheer();
    }

    private static Uitleg getData(Map<String, String[]> formParams){
        //TODO please, convert this to JSON.
        String titel = formParams.get("title")[0];
        String uuid = formParams.get("uuid")[0];

        if(uuid.charAt(0) == '"') uuid = uuid.substring(1, uuid.length() -1);
        if(uuid.length() < 1) uuid = Application.generateUUID();

        Uitleg uitleg = new Uitleg(uuid, titel.substring(1, titel.length() - 1));

        try {
            uitleg.setVolgorde(Integer.parseInt(formParams.get("volgorde")[0].replaceAll("[^0-9]", "")));
        } catch (NumberFormatException e){
            e.printStackTrace();
            throw e;
        }

        formParams.remove("title");
        formParams.remove("uuid");
        SortedSet<Integer> keys = new TreeSet<>();

        for (String key : formParams.keySet()) {
            try {
                keys.add(Integer.parseInt(key));
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }

        for(Integer formkey : keys){
            UitlegRegel uitlegRegel = new UitlegRegel(Application.generateUUID());
            JsonObject object;

            try {
                object = Json.createReader(new StringReader(formParams.get(formkey.toString())[0])).readObject();
            } catch (JsonParsingException e) {
                continue;
            }

            for(String key : object.keySet()){
                String value = object.get(key).toString();
                if(!key.equals("text")) {
                    value = value.replace("\"", "");
                }
                else{
                    value = value.replace("\\\"", "\"");
                    if(value.charAt(0) == '\"'){
                        value = value.substring(1);
                    }
                    if(value.charAt(value.length() - 1) == '\"'){
                        value = value.substring(0, value.length() - 1);
                    }
                    if(value.equals("<p>Typ hier</p>")){
                        value = "";
                    }
                }

                switch (key){
                    case "uitspraakpath":   uitlegRegel.setUitspraak(value); break;
                    case "uitspraak":       uitlegRegel.setUitspraak(saveFileAndGetPath(request().body().asMultipartFormData(), value, "audio/")); break;
                    case "text":            uitlegRegel.setText(value); break;
                    case "icon":            uitlegRegel.setIcon(saveFileAndGetPath(request().body().asMultipartFormData(), value, "icon/")); break;
                    case "iconpath":        uitlegRegel.setIcon(value); break;
                    case "iconText":        uitlegRegel.setIconText(value); break;
                    case "overschrijven":   uitlegRegel.setOverschrijven(Boolean.valueOf(value)); break;
                    case "intikken":        uitlegRegel.setIntikken(Boolean.valueOf(value)); break;
                    case "controleren":     uitlegRegel.setControleren(Boolean.valueOf(value)); break;
                    case "wachttijd" :      uitlegRegel.setWachttijd(Integer.parseInt(value)); break;
                }
            }

            uitleg.addUitlegRegel(uitlegRegel);
        }
        return uitleg;
    }

    @Transactional
    public static Result uitlegAanpassen(){
        IUitlegDao uitlegDao = new UitlegDao();
        Map<String, String[]> params = request().body().asFormUrlEncoded();
        String uitlegUuid = params.get("uuid")[0];

        List<UitlegRegel> uitlegRegels = uitlegDao.getUitlegRegels(uitlegUuid);
        Uitleg uitleg = uitlegDao.getByUuId(uitlegUuid);

        UitlegForm uitlegForm = new UitlegForm();
        uitlegForm.setId(uitleg.getId() + "");
        uitlegForm.setNieuweTitel(uitleg.getTitle());
        uitlegForm.setOudeTitel(uitleg.getTitle());
        uitlegForm.setNieuweVolgorde(uitleg.getVolgorde() + "");
        uitlegForm.setUuid(uitleg.getUuid());

        return ok(main.render("Uitleg aanpassen", uitlegaanpassen.render(params.get("spellingregelnaam")[0], uitlegRegels, uitlegForm)));
    }

    @Transactional
    public static Result deleteUitleg(){
        UitlegForm uitlegForm = (UitlegForm) FormController.handleFormRequest(request().body().asMultipartFormData().asFormUrlEncoded(), uitlegForms);
        IUitlegDao uitlegDao = new UitlegDao();

        Uitleg uitleg = uitlegDao.getByUuId(uitlegForm.getUuid());
        uitlegDao.removeIncludesRegels(uitleg);

        flash("success-message", "Uitleg is succesvol verwijderd.");
        flash("spellingregelnaam", uitlegForm.getSpellingregelnaam());

        uitlegForms.remove(uitlegForm);

        return SpellingregelController.redirectToBeheer();
    }

    private static String saveFileAndGetPath(Http.MultipartFormData formData, String key, String map){
        Http.MultipartFormData.FilePart filePart = formData.getFile(key);

        if(filePart == null) return "";

        File file = filePart.getFile();
        String dir = "public/" + map + formData.asFormUrlEncoded().get("spellingregelnaam")[0] + "/uitleg/";
        File directory = new File(dir);
        if(!directory.exists())
            directory.mkdirs();
        file.renameTo(new File(dir + filePart.getFilename()));
        return dir.substring(dir.indexOf(map), dir.length()) + filePart.getFilename();
    }

    //region Modal handlers
    public static Result newUitlegModal(){
        UitlegForm uitlegForm = new UitlegForm();
        Map<String, String[]> params = request().body().asFormUrlEncoded();
        uitlegForm.setSpellingregelnaam(params.get("spellingregelnaam")[0]);

        uitlegForms.add(uitlegForm);

        return ok(dynamicmodalform.render("Uitleg toevoegen", "/uitleg/create", uitlegForm.getInputFields()));
    }

    public static Result deleteUitlegModal(){
        UitlegForm uitlegForm = new UitlegForm();
        Map<String, String[]> params = request().body().asFormUrlEncoded();
        uitlegForm.setSpellingregelnaam(params.get("spellingregelnaam")[0]);
        uitlegForm.setOudeVolgorde(params.get("volgorde")[0]);
        uitlegForm.setUuid(params.get("uuid")[0]);
        uitlegForm.setOudeTitel(params.get("titel")[0]);
        uitlegForms.add(uitlegForm);
        return ok(dynamicmodaldelete.render("de uitleg " + uitlegForm.getOudeTitel(),"/uitleg/delete", uitlegForm.getInputFields()));
    }
    //endregion
}
