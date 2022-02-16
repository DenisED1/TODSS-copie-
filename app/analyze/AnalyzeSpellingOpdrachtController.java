package analyze;

import dao.AnalyzeSpellingRegel.AnalyzeSpellingOpdrachtDao;
import dao.AnalyzeSpellingRegel.IAnalyzeSpellingOpdrachtDao;
import dao.Gebruiker.GebruikerDao;
import dao.Gebruiker.IGebruikerDao;
import dao.SpellingRegel.ISpellingRegelDao;
import dao.SpellingRegel.SpellingRegelDao;
import model.Gebruiker;
import model.analyze.SpellingOpdracht.AnalyzeSpellingOpdracht;
import model.analyze.SpellingOpdracht.AnalyzeSpellingAntwoord;
import play.db.jpa.Transactional;

import static play.mvc.Controller.session;

public class AnalyzeSpellingOpdrachtController implements IAnalyze, IAnalyzeSpellingOpdracht {
    private AnalyzeSpellingOpdracht analyzeSpellingOpdracht;

    public AnalyzeSpellingOpdrachtController(int spellingregelid) {
        analyzeSpellingOpdracht = new AnalyzeSpellingOpdracht();
        IGebruikerDao gebruikerDao = new GebruikerDao();
        ISpellingRegelDao spellingRegelDao = new SpellingRegelDao();
        IAnalyzeSpellingOpdrachtDao analyzeSpellingRegelDao = new AnalyzeSpellingOpdrachtDao();

        //Set some required fields
        String gebruikerUuid = session("gebruikerUuid");
        Gebruiker gebruiker = gebruikerDao.getByUUID(gebruikerUuid);
        analyzeSpellingOpdracht.setLeerling(gebruiker);
        analyzeSpellingOpdracht.setSpellingRegel(spellingRegelDao.getByIdEmpty(spellingregelid));
        startOpdracht();

        //Update to database to get ID.
        analyzeSpellingRegelDao.insert(analyzeSpellingOpdracht);
        analyzeSpellingOpdracht = analyzeSpellingRegelDao.getByTijd(analyzeSpellingOpdracht.getLeerling(), analyzeSpellingOpdracht.getBeginTijd());
    }

    @Override
    public void startOpdracht() {
        //Set time to now.
        analyzeSpellingOpdracht.setBeginTijd();
    }

    @Override
    @Transactional
    public void voegWoordToe(AnalyzeSpellingAntwoord woord) {
        IAnalyzeSpellingOpdrachtDao analyzeSpellingOpdrachtDao = new AnalyzeSpellingOpdrachtDao();

        woord.setRegel(analyzeSpellingOpdracht);
        analyzeSpellingOpdracht.voegAntwoordToe(woord);

        analyzeSpellingOpdrachtDao.insertWoord(woord);
    }

    @Override
    public void stopOpdracht() {
        IAnalyzeSpellingOpdrachtDao analyzeSpellingOpdrachtDao = new AnalyzeSpellingOpdrachtDao();

        analyzeSpellingOpdracht.setEindTijd();

        analyzeSpellingOpdrachtDao.updateEindTijd(analyzeSpellingOpdracht);
    }
}
