package controllers;

import authentication.Administrator;
import dao.Categorie.CategorieDao;
import dao.Categorie.ICategorieDao;
import dao.SpellingRegel.ISpellingRegelDao;
import dao.SpellingRegel.SpellingRegelDao;
import formpackage.AbstractForm;
import formpackage.CategorieForm;
import model.Categorie;
import model.SpellingRegel;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import validators.CategorieValidator;
import views.html.dynamicmodaldelete;
import views.html.dynamicmodalform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Security.Authenticated(Administrator.class)
public class CategorieController extends Controller {
    private static List<AbstractForm> categorieForms = new ArrayList<>();

    @Transactional
    public static Result createCategorie(){
        ICategorieDao categorieDao = new CategorieDao();
        ISpellingRegelDao spellingRegelDao = new SpellingRegelDao();

        Map<String, String[]> formParams = request().body().asMultipartFormData().asFormUrlEncoded();
        CategorieForm categorieForm;

        try {
            categorieForm = (CategorieForm) FormController.handleFormRequest(formParams, categorieForms);
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        Categorie nieuweCategorie = new Categorie(categorieForm.getNieuweCategorienaam());
        SpellingRegel spellingRegel = spellingRegelDao.getByName(categorieForm.getSpellingregelnaam());
        spellingRegel.addCategorie(nieuweCategorie);

        categorieDao.insert(nieuweCategorie);
        spellingRegelDao.update(spellingRegel);

        flash("success-message", "Categorie " + categorieForm.getNieuweCategorienaam() + " is succesvol toegevoegd.");
        flash("spellingregelnaam", categorieForm.getSpellingregelnaam());

        categorieForms.remove(categorieForm);

        return SpellingregelController.redirectToBeheer();
    }

    @Transactional
    public static Result updateCategorie(){
        ICategorieDao categorieDao = new CategorieDao();
        Map<String, String[]> formparams = request().body().asMultipartFormData().asFormUrlEncoded();
        CategorieForm categorieForm;

        try {
            categorieForm = (CategorieForm) FormController.handleFormRequest(formparams, categorieForms);
            if(categorieForm != null)
                categorieForm.validate();
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        try {
            CategorieValidator.update(categorieForm.getNieuweCategorienaam());
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        categorieDao.updateNaam(categorieForm.getOudeCategorienaam(), categorieForm.getNieuweCategorienaam());

        flash("success-message", "Categorie " + categorieForm.getOudeCategorienaam() + " is succesvol geupdate naar " + categorieForm.getNieuweCategorienaam());
        flash("spellingregelnaam", categorieForm.getSpellingregelnaam());

        categorieForms.remove(categorieForm);

        return SpellingregelController.redirectToBeheer();
    }

    @Transactional
    public static Result deleteCategorie(){
        ICategorieDao categorieDao = new CategorieDao();
        Map<String, String[]> formparams = request().body().asMultipartFormData().asFormUrlEncoded();
        CategorieForm categorieForm;

        try {
            categorieForm = (CategorieForm) FormController.handleFormRequest(formparams, categorieForms);
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        String categorieNaam = categorieForm.getOudeCategorienaam();

        Categorie cat = categorieDao.getByName(categorieNaam);

        //try {
        //    CategorieValidator.delete(cat);
        //} catch (Exception e) {
        //    return badRequest(e.getMessage());
        //}

        categorieDao.remove(cat);

        flash("success-message", "Categorie " + categorieNaam + " is succesvol verwijderd.");
        flash("spellingregelnaam", categorieForm.getSpellingregelnaam());

        categorieForms.remove(categorieForm);

        return SpellingregelController.redirectToBeheer();
    }

    //region Modal handlers
    public static Result newCategorieModal(){
        Map<String, String[]> params = request().body().asFormUrlEncoded();

        CategorieForm categorieForm = new CategorieForm();
        categorieForm.setSpellingregelnaam(params.get("spellingregelnaam")[0]);

        categorieForms.add(categorieForm);

        return ok(dynamicmodalform.render("Categorie toevoegen", "/categorie/create", categorieForm.getInputFields()));
    }

    public static Result updateCategorieModal(){
        Map<String, String[]> params = request().body().asFormUrlEncoded();

        CategorieForm categorieForm = new CategorieForm();
        categorieForm.setSpellingregelnaam(params.get("spellingregelnaam")[0]);
        categorieForm.setOudeCategorienaam(params.get("naam")[0]);
        categorieForm.setNieuweCategorienaam(params.get("naam")[0]);

        categorieForms.add(categorieForm);

        return ok(dynamicmodalform.render("Categorie wijzigen", "/categorie/update", categorieForm.getInputFields()));
    }

    public static Result deleteCategorieModal(){
        Map<String, String[]> params = request().body().asFormUrlEncoded();

        CategorieForm categorieForm = new CategorieForm();
        categorieForm.setSpellingregelnaam(params.get("spellingregelnaam")[0]);
        categorieForm.setOudeCategorienaam(params.get("naam")[0]);

        categorieForms.add(categorieForm);

        return ok(dynamicmodaldelete.render("de categorie " + categorieForm.getOudeCategorienaam(), "/categorie/delete", categorieForm.getInputFields()));
    }
    //endregion
}
