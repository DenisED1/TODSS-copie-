package controllers;

import authentication.Leerling;
import dao.SpellingRegel.ISpellingRegelDao;
import dao.SpellingRegel.SpellingRegelDao;
import dao.Woord.IWoordDao;
import dao.Woord.WoordDao;
import model.Categorie;
import model.Moeilijkheidsgraad;
import model.SpellingRegel;
import model.Woord;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.main;
import views.html.voorbereidendoefenen;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Security.Authenticated(Leerling.class)
public class OpdrachtPrikkeldraadController extends Controller {

    @Transactional
    public static Result voorbereindendOefenen(){
        IWoordDao woordDao = new WoordDao();
        ISpellingRegelDao spellingRegelDao = new SpellingRegelDao();

        String gekozenspellingregel = request().body().asMultipartFormData().asFormUrlEncoded().get("spellingRegel")[0];
        Moeilijkheidsgraad moeilijkheidsgraad;
        try{
            moeilijkheidsgraad = Moeilijkheidsgraad.fromString(request().body().asMultipartFormData().asFormUrlEncoded().get("moeilijkheidsgraad")[0]);
        } catch (NullPointerException e){
            moeilijkheidsgraad = Moeilijkheidsgraad.Makkelijk;
        }

        SpellingRegel spellingRegel = spellingRegelDao.getByName(gekozenspellingregel);
        List<Categorie> categorieen = spellingRegelDao.getCategorieenBySpellingregelId(spellingRegel.getId());
        List<Woord> woordLijst = woordDao.getBySpellingIdWhereMoeilijkheidsgraad(spellingRegel.getId(), moeilijkheidsgraad);

        categorieen.add(new Categorie("Anders"));

        Collections.shuffle(woordLijst, new Random());

        if(woordLijst.size() > 20){
            woordLijst = woordLijst.subList(0,20);
        }

        session("spellingregel", gekozenspellingregel);
        return ok(main.render("Voorbereindend oefenen", voorbereidendoefenen.render(woordLijst,categorieen)));
    }
}
