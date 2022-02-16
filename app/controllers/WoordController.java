package controllers;

import authentication.Administrator;
import dao.Categorie.CategorieDao;
import dao.Categorie.ICategorieDao;
import dao.SpellingRegel.ISpellingRegelDao;
import dao.SpellingRegel.SpellingRegelDao;
import dao.Woord.IWoordDao;
import dao.Woord.WoordDao;
import formpackage.AbstractForm;
import formpackage.WoordForm;
import model.Categorie;
import model.Moeilijkheidsgraad;
import model.SpellingRegel;
import model.Woord;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import validators.WoordValidator;
import views.html.dynamicmodaldelete;
import views.html.dynamicmodalform;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Security.Authenticated(Administrator.class)
public class WoordController extends Controller {
    private static List<AbstractForm> woordForms = new ArrayList<>();

    @Transactional
    public static Result createWoord(){
        ISpellingRegelDao spellingRegelDao = new SpellingRegelDao();
        IWoordDao woordDao = new WoordDao();
        ICategorieDao categorieDao = new CategorieDao();
        WoordForm woordForm;

        try {
            Http.MultipartFormData body = request().body().asMultipartFormData();
            woordForm = getWoordForm(body);
            woordForm.validate();
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        if(woordForm != null){

            SpellingRegel spellingRegel = spellingRegelDao.getByName(woordForm.getSpellingregelnaam());
            Categorie categorie = categorieDao.getByName(woordForm.getNieuweCategorie());
            Woord woord = new Woord(
                woordForm.getNieuwWoord(),
                woordForm.getNieuweUitspraak(),
                Moeilijkheidsgraad.fromString(woordForm.getNieuweMoeilijkheidsgraad()),
                categorie
            );

            try {
                WoordValidator.create(categorie, spellingRegel, woord);
            } catch (Exception e) {
                return badRequest(e.getMessage());
            }

            woordDao.create(woord);

            spellingRegel.addWoord(woord);
            spellingRegelDao.update(spellingRegel);

            woordForms.remove(woordForm);

            flash("success-message", "Woord " + woordForm.getNieuwWoord() + " is succesvol toegevoegd.");
            flash("spellingregelnaam", woordForm.getSpellingregelnaam());

            return SpellingregelController.redirectToBeheer();
        }
        return badRequest("Er is iets mis gegaan, probeer het opnieuw!");
    }

    @Transactional
    public static Result updateWoord(){
        IWoordDao woordDao = new WoordDao();
        ICategorieDao categorieDao = new CategorieDao();
        WoordForm woordForm = null;

        try {
            Http.MultipartFormData body = request().body().asMultipartFormData();
            woordForm = getWoordForm(body);
            woordForm.validate();
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        if(woordForm != null) {
            Woord woord = woordDao.getByName(woordForm.getOudWoord());

            woord.setWoord(woordForm.getNieuwWoord());
            woord.setUitspraak(woordForm.getNieuweUitspraak());
            woord.setMoeilijkheidsgraad(Moeilijkheidsgraad.fromString(woordForm.getNieuweMoeilijkheidsgraad()));
            woord.setCategorie(categorieDao.getByName(woordForm.getNieuweCategorie()));

            woordDao.update(woord);

            flash("success-message", "Woord is succesvol gewijzigd.");
            flash("spellingregelnaam", woordForm.getSpellingregelnaam());
            woordForms.remove(woordForm);
            return SpellingregelController.redirectToBeheer();
        }
        return badRequest();
    }

    @Transactional
    public static Result deleteWoord(){
        IWoordDao woordDao = new WoordDao();
        WoordForm woordForm = getWoordForm(request().body().asMultipartFormData());

        woordDao.deleteByString(woordForm.getOudWoord());

        woordForms.remove(woordForm);

        flash("success-message", "Woord " + woordForm.getOudWoord() + " is succesvol verwijderd");
        flash("spellingregelnaam", woordForm.getSpellingregelnaam());

        return SpellingregelController.redirectToBeheer();
    }

    private static WoordForm getWoordForm(Http.MultipartFormData body) {
        WoordForm woordForm;
        woordForm = (WoordForm) FormController.handleFormRequest(body.asFormUrlEncoded(), woordForms);
        saveFiles(body.getFiles(), woordForm);
        return woordForm;
    }

    private static void saveFiles(List<Http.MultipartFormData.FilePart> fileParts, WoordForm woordForm){
        for(Http.MultipartFormData.FilePart tempfile : fileParts){
            String dir = "public/audio/" + woordForm.getSpellingregelnaam() + "/woorden/";

            File file = tempfile.getFile();

            File directory = new File(dir);
            if(!directory.exists())
                directory.mkdirs();

            file.renameTo(new File(dir + tempfile.getFilename()));

            woordForm.setNieuweUitspraak("audio/" + woordForm.getSpellingregelnaam() + "/woorden/" + tempfile.getFilename());
        }
        if (woordForm.getNieuweUitspraak().split("/").length > 1 && woordForm.getNieuweUitspraak().split("/")[1].equals("assets")) {
            String newpath = woordForm.getNieuweUitspraak().substring(woordForm.getNieuweUitspraak().indexOf("/", 1));
            woordForm.setNieuweUitspraak(newpath);
        }
    }

    //region Modal handlers
    @Transactional
    public static Result newWoordModal(){
        ISpellingRegelDao spellingRegelDao = new SpellingRegelDao();
        WoordForm woordForm = new WoordForm();
        Map<String, String[]> params = request().body().asFormUrlEncoded();
        woordForm.setNieuweMoeilijkheidsgraaden(Moeilijkheidsgraad.getMoeilijkheidsgraden());
        int spellingRegelId = Integer.parseInt(params.get("spellingregelid")[0]);

        List<Categorie> categories = spellingRegelDao.getCategorieenBySpellingregelId(spellingRegelId);
        List<String> categorieNamen = new ArrayList<>();
        for(Categorie categorie : categories){
            categorieNamen.add(categorie.getNaam());
        }

        woordForm.setSpellingregelnaam(params.get("spellingregelnaam")[0]);
        woordForm.setNieuweCategorieen(categorieNamen);
        woordForms.add(woordForm);
        return ok(dynamicmodalform.render("Woord toevoegen", "/woord/create", woordForm.getInputFields()));
    }

    @Transactional
    public static Result updateWoordModal(){
        ISpellingRegelDao spellingRegelDao = new SpellingRegelDao();
        WoordForm woordForm = new WoordForm();
        Map<String, String[]> params = request().body().asFormUrlEncoded();

        woordForm.setOudWoord(params.get("woord")[0]);
        woordForm.setNieuwWoord(params.get("woord")[0]);
        woordForm.setOudeUitspraak(params.get("uitspraak")[0]);
        woordForm.setNieuweUitspraak(params.get("uitspraak")[0]);
        woordForm.setOudeMoeilijkheidsgraad(params.get("moeilijkheidsgraad")[0]);
        woordForm.setNieuweMoeilijkheidsgraaden(Moeilijkheidsgraad.getMoeilijkheidsgraden());
        woordForm.setOudeCategorie(params.get("categorie")[0]);
        woordForm.setSpellingregelnaam(params.get("spellingregelnaam")[0]);

        int spellingRegelId = Integer.parseInt(params.get("spellingregelid")[0]);
        List<Categorie> categories = spellingRegelDao.getCategorieenBySpellingregelId(spellingRegelId);

        List<String> categorieNamen = new ArrayList<>();
        for(Categorie categorie : categories){
            if(categorie.getNaam().equals(woordForm.getOudeCategorie())) {
                categorieNamen.add(0, categorie.getNaam());
            } else {
                categorieNamen.add(categorie.getNaam());
            }
        }

        woordForm.setNieuweCategorieen(categorieNamen);
        woordForms.add(woordForm);

        return ok(dynamicmodalform.render("Woord wijzigen", "/woord/update", woordForm.getInputFields()));
    }

    public static Result deleteWoordModal(){
        WoordForm woordForm = new WoordForm();
        Map<String, String[]> params = request().body().asFormUrlEncoded();

        woordForm.setOudWoord(params.get("woord")[0]);
        woordForm.setOudeUitspraak(params.get("uitspraak")[0]);
        woordForm.setOudeMoeilijkheidsgraad(params.get("moeilijkheidsgraad")[0]);
        woordForm.setOudeCategorie(params.get("categorie")[0]);
        woordForm.setSpellingregelnaam(params.get("spellingregelnaam")[0]);

        woordForms.add(woordForm);

        return ok(dynamicmodaldelete.render(" het woord " + woordForm.getOudWoord(), "/woord/delete", woordForm.getInputFields()));
    }
    //endregion
}
