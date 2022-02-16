package controllers;

import authentication.Leraar;
import dao.AnalyzeSpellingRegel.AnalyzeSpellingOpdrachtDao;
import dao.AnalyzeSpellingRegel.IAnalyzeSpellingOpdrachtDao;
import dao.Gebruiker.GebruikerDao;
import dao.Gebruiker.IGebruikerDao;
import dao.Toets.IToetsDao;
import dao.Toets.ToetsDao;
import model.Gebruiker;
import model.analyze.AnalyzeGeneriek;
import model.analyze.SpellingOpdracht.AnalyzeSpellingOpdracht;
import model.analyze.SpellingOpdracht.AnalyzeSpellingAntwoord;
import model.analyze.Toets.Toets;
import model.analyze.Toets.ToetsAntwoord;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.analyzeoverzicht;
import views.html.main;

import javax.json.*;
import java.util.*;

@Security.Authenticated(Leraar.class)
public class AnalyzeOverzichtController extends Controller {

    //TODO Convert this to 1 query.
    @Transactional
    public static Result analyzeOverzicht(String uuid){
        IGebruikerDao gebruikerDao = new GebruikerDao();
        IAnalyzeSpellingOpdrachtDao analyzeSpellingRegelDao = new AnalyzeSpellingOpdrachtDao();
        IToetsDao toetsRegelDao = new ToetsDao();

        Gebruiker gebruiker = gebruikerDao.getByUUID(uuid);
        List<AnalyzeSpellingOpdracht> opdrachten = analyzeSpellingRegelDao.getByGebruiker(gebruiker);
        List<Toets> toetsen = toetsRegelDao.getByGebruiker(gebruiker);

        //copied the list above to extract duplicate dates
        List<AnalyzeGeneriek> datums = new ArrayList<>(opdrachten);
        List<AnalyzeGeneriek> toetsDatums = new ArrayList<>(toetsen);
        List<AnalyzeGeneriek> dates = new ArrayList<>();
        dates.clear();

        Map<Date, AnalyzeGeneriek> map = new LinkedHashMap<>();
        for (AnalyzeGeneriek asr : datums){
            map.put(asr.getDatum(), asr);
        }

        for (AnalyzeGeneriek tsm : toetsDatums){
            map.put(tsm.getDatum(), tsm);
        }
        dates.addAll(map.values());

        List<AnalyzeSpellingAntwoord> woorden = analyzeSpellingRegelDao.getWoordenByUser(gebruiker);
        List<ToetsAntwoord> toetsAntwoorden = toetsRegelDao.getWoordenByUser(gebruiker);

        return ok(main.render("Statistieken", analyzeoverzicht.render(dates, gebruiker, opdrachten, woorden, toetsAntwoorden, toetsen)));
    }

    @Transactional
    public static Result deletePerDatum(int aId){
        IAnalyzeSpellingOpdrachtDao analyzeSpellingRegelDao = new AnalyzeSpellingOpdrachtDao();
        AnalyzeSpellingOpdracht regel = analyzeSpellingRegelDao.getById(aId);

        analyzeSpellingRegelDao.deleteByDateAndLeerling(regel);

        String msg = "Analyze opdracht per " + regel.getDatum() + " is verwijderd.";
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("msg", msg);
        flash("succes-message", msg);

        return ok(job.build().toString());
    }

    @Transactional
    public static Result deletePerOpdracht(int id){
        JsonObjectBuilder job = Json.createObjectBuilder();
        IAnalyzeSpellingOpdrachtDao analyzeSpellingRegelDao = new AnalyzeSpellingOpdrachtDao();

        AnalyzeSpellingOpdracht regel = analyzeSpellingRegelDao.getById(id);
        analyzeSpellingRegelDao.delete(regel);

        String msg = "Analyze opdracht is verwijderd.";
        job.add("msg", msg);
        flash("success-message", msg);

        return ok(job.build().toString());
    }

    @Transactional
    public static Result deletePerToets(int id){
        JsonObjectBuilder job = Json.createObjectBuilder();
        IToetsDao toetsRegelDao = new ToetsDao();

        Toets regel = toetsRegelDao.getById(id);
        toetsRegelDao.delete(regel);

        String msg = "Analyze toets is verwijderd.";
        job.add("msg", msg);
        flash("success-message", msg);

        return ok(job.build().toString());
    }
}