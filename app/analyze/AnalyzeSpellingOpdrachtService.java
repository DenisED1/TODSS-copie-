package analyze;

import model.analyze.SpellingOpdracht.AnalyzeSpellingAntwoord;
import play.db.jpa.Transactional;

public class AnalyzeSpellingOpdrachtService {
    AnalyzeSpellingOpdrachtController analyzeSpellingOpdrachtController;

    @Transactional
    public void VoegAntwoordToe(AnalyzeSpellingAntwoord woord) throws Exception {
        if(analyzeSpellingOpdrachtController == null){
            throw new Exception("Geen opdracht geselecteerd");
        }

        analyzeSpellingOpdrachtController.voegWoordToe(woord);
    }

    public void StopOpdracht(){
        analyzeSpellingOpdrachtController.stopOpdracht();
    }

    public void resetController(){
        analyzeSpellingOpdrachtController = null;
    }

    public void nieuweOpdracht(int spellingregelid) {
        analyzeSpellingOpdrachtController = new AnalyzeSpellingOpdrachtController(spellingregelid);
    }
}
