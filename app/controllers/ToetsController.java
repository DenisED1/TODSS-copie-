package controllers;

import analyze.AnalyzeToetsService;
import authentication.Leerling;
import dao.SpellingRegel.ISpellingRegelDao;
import dao.SpellingRegel.SpellingRegelDao;
import dao.Toets.IToetsDao;
import dao.Toets.ToetsDao;
import dao.Woord.IWoordDao;
import dao.Woord.WoordDao;
import model.*;
import model.analyze.Toets.Toets;
import model.analyze.Toets.ToetsAntwoord;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.main;
import views.html.toets;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.time.LocalDateTime;
import java.util.*;

@Security.Authenticated(Leerling.class)
public class ToetsController extends Controller {
    private static Map<String, Opdracht> opdrachten = new HashMap<>();
    private static AnalyzeToetsService analyzeToetsService = new AnalyzeToetsService();

    //Initial call when the toets is first made
    @Transactional
    public static Result toetsMaken(){
        IWoordDao woordDao = new WoordDao();
        ISpellingRegelDao spellingRegelDao = new SpellingRegelDao();
        int spellingregelid = Integer.parseInt(request().body().asMultipartFormData().asFormUrlEncoded().get("spellingregelid")[0]);
        Opdracht opdracht = new Opdracht();

        Moeilijkheidsgraad moeilijkheidsgraad;
        try{
            moeilijkheidsgraad = Moeilijkheidsgraad.fromString(request().body().asMultipartFormData().asFormUrlEncoded().get("moeilijkheidsgraad")[0]);
        } catch (NullPointerException e){
            moeilijkheidsgraad = Moeilijkheidsgraad.Makkelijk;
        }

        analyzeToetsService.nieuweOpdracht(spellingregelid);

        //Get the words
        List<Woord> woordLijst = woordDao.getBySpellingIdWhereMoeilijkheidsgraad(spellingregelid, moeilijkheidsgraad);

        //Shuffle the list in order to get random words
        Collections.shuffle(woordLijst, new Random());

        if(woordLijst.size() > 20){
            woordLijst = woordLijst.subList(0,20);
        }

        opdracht.setWoorden(woordLijst);
        String opdrachtUuid = Application.generateUUID();
        session("opdrachtuuid", opdrachtUuid);
        opdrachten.put(opdrachtUuid, opdracht);

        return ok(main.render(
                "Toets",
                toets.render(
                        opdracht.getWoorden(),
                        spellingRegelDao.getByIdEmpty(spellingregelid).getOpdrachtUitleg()
                )
        ));
    }

    @Transactional
    public static Result checkAntwoord(String correctAntwoord, String antwoordDefinitief, String antwoordVersie1) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("error", false);
        Opdracht opdracht = opdrachten.get(session("opdrachtuuid"));

        //Save to database for analyze
        try {
            analyzeToetsService.VoegAntwoordToe(new ToetsAntwoord(correctAntwoord, antwoordDefinitief, antwoordVersie1));
        } catch (Exception e) {
            e.printStackTrace();
            job.add("error", true);
            return ok(job.build().toString());
        }

        //Check antwoord.
        job.add("goed", opdracht.geefAntwoord(antwoordDefinitief, correctAntwoord));

        if(!opdracht.hasNextWoord()){
            opdrachten.remove(session("opdrachtuuid"));
            session().remove("opdrachtuuid");
            job.add("finished", true);
            job.add("aantalgoed", opdracht.getAantalGoed());
            job.add("aantalfout", opdracht.getAantalFout());

            System.out.println("Test");

            //Reset the controller, so it gets the new regel when needed.
            analyzeToetsService.resetController();
        }

        return ok(job.build().toString());
    }
}
