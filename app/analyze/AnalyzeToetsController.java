package analyze;

import dao.Gebruiker.GebruikerDao;
import dao.Gebruiker.IGebruikerDao;
import dao.SpellingRegel.ISpellingRegelDao;
import dao.SpellingRegel.SpellingRegelDao;
import dao.Toets.IToetsDao;
import dao.Toets.ToetsDao;
import model.Gebruiker;
import model.analyze.Toets.Toets;
import model.analyze.Toets.ToetsAntwoord;

import static play.mvc.Controller.session;

public class AnalyzeToetsController implements IAnalyze, IAnalyzeToets{
    private Toets toets = new Toets();
    private IToetsDao toetsDao = new ToetsDao();

    public AnalyzeToetsController(int spellingRegelId){
        IGebruikerDao gebruikerDao = new GebruikerDao();
        ISpellingRegelDao spellingRegelDao = new SpellingRegelDao();

        //Set some required fields
        String gebruikerUuid = session("gebruikerUuid");
        Gebruiker gebruiker = gebruikerDao.getByUUID(gebruikerUuid);
        toets.setLeerling(gebruiker);
        toets.setSpellingRegel(spellingRegelDao.getByIdEmpty(spellingRegelId));
        startOpdracht();

        //Update to database to get ID.
        toetsDao.insert(toets);
        toets = toetsDao.getByTijd(toets.getLeerling(), toets.getBeginTijd());
    }

    @Override
    public void startOpdracht() {
        toets.setBeginTijd();
    }

    @Override
    public void stopOpdracht() {
        toets.setEindTijd();
        toetsDao.updateEindTijd(toets);
    }

    @Override
    public void voegWoordToe(ToetsAntwoord toetsAntwoord) {
        toetsAntwoord.setRegel(toets);
        toets.voegAntwoordToe(toetsAntwoord);
        toetsDao.insertWoord(toetsAntwoord);
    }
}
