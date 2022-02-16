package controllers;

import analyze.AnalyzeSpellingOpdrachtService;
import authentication.Leerling;
import dao.SpellingRegel.ISpellingRegelDao;
import dao.SpellingRegel.SpellingRegelDao;
import dao.Woord.IWoordDao;
import dao.Woord.WoordDao;
import model.*;
import model.analyze.SpellingOpdracht.AnalyzeSpellingAntwoord;
import play.api.Play;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.main;
import views.html.spellingregelmaken;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.io.File;
import java.util.*;

@Security.Authenticated(Leerling.class)
public class OpdrachtSpellingController extends Controller{
    private static Map<String, Opdracht> opdrachten = new HashMap<>();
    private static AnalyzeSpellingOpdrachtService analyzeSpellingOpdrachtService = new AnalyzeSpellingOpdrachtService();

    //Initial call when the opdacht is first made
    @Transactional
    public static Result opdrachtMaken(){
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

        analyzeSpellingOpdrachtService.nieuweOpdracht(spellingregelid);

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

        File[] plaatjes = Play.getFile("public/images/opdracht/", Play.current()).listFiles();
        String filename = plaatjes[new Random().nextInt(plaatjes.length)].getName();

        return ok(main.render(
                "opdracht maken",
                spellingregelmaken.render(
                        opdracht.getAantalWoordenOver(),
                        opdracht.hasNextWoord() ? opdracht.getNextWoord() : new Woord(),
                        filename,
                        spellingRegelDao.getByIdEmpty(spellingregelid).getOpdrachtUitleg()
                )
        ));
    }

    @Transactional
    public static Result checkAntwoord(String antwoordDefinitief, String antwoordVersie1) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("error", false);
        Opdracht opdracht = opdrachten.get(session("opdrachtuuid"));

        //Save to database for analyze
        try {
            analyzeSpellingOpdrachtService.VoegAntwoordToe(new AnalyzeSpellingAntwoord(opdracht.getCurrentWoord().getWoord(), antwoordDefinitief, antwoordVersie1));
        } catch (Exception e) {
            e.printStackTrace();
            job.add("error", true);
            return ok(job.build().toString());
        }

        //Check antwoord.
        job.add("goed", opdracht.geefAntwoord(antwoordDefinitief));

        if(opdracht.hasNextWoord()){
            Woord woord = opdracht.getNextWoord();
            job.add("uitspraak", routes.Assets.at(woord.getUitspraak()).url());
            job.add("vraag", woord.getWoord());
        } else {
            opdrachten.remove(session("opdrachtuuid"));
            session().remove("opdrachtuuid");
            job.add("finished", true);
            job.add("aantalgoed", opdracht.getAantalGoed());
            job.add("aantalfout", opdracht.getAantalFout());

            //Reset the controller, so it gets the new regel when needed.
            analyzeSpellingOpdrachtService.StopOpdracht();
            analyzeSpellingOpdrachtService.resetController();
        }

        return ok(job.build().toString());
    }
}
