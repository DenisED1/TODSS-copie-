package controllers;

import authentication.Leerling;
import dao.SpellingRegel.ISpellingRegelDao;
import dao.SpellingRegel.SpellingRegelDao;
import dao.Uitleg.IUitlegDao;
import dao.Uitleg.UitlegDao;
import model.SpellingRegel;
import model.Uitleg;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import java.util.List;

@Security.Authenticated(Leerling.class)
public class LeerlingController extends Controller {

    @Transactional
    public static Result spellingregelKiezen(){
        ISpellingRegelDao spellingRegelDao = new SpellingRegelDao();
        List<SpellingRegel> allSpellingRegels = spellingRegelDao.getAllSpellingRegelsEmpty();
        return ok(
            main.render(
            "SpellingRegel kiezen", spellingregelkiezen.render(allSpellingRegels)
            )
        );
    }

    @Transactional
    public static Result beluisterUitleg(){
        IUitlegDao uitlegDao = new UitlegDao();
        String gekozenspellingregel = request().body().asMultipartFormData().asFormUrlEncoded().get("spellingregelnaam")[0];
        session("spellingregel", gekozenspellingregel);

        List<Uitleg> uitlegList = uitlegDao.getBySpellingRegelNaam(gekozenspellingregel);

        return ok(main.render("Uitleg beluisteren", uitlegbeluisteren.render(uitlegList, gekozenspellingregel)));
    }
}
