package controllers;

import authentication.Ingelogd;
import play.Routes;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.home;
import views.html.main;

import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;

public class Application extends Controller {
    @Security.Authenticated(Ingelogd.class)
    @Transactional
    public static Result home(){
//        MergeFromOld.merge();


        return ok(main.render("Home" , home.render()));
    }

    @Security.Authenticated(Ingelogd.class)
    public static Result loguit(){
        session().clear();
        return redirect(routes.LoginController.login());
    }

    public static String generateUUID(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    private static String saveFileAndGetPath(Http.MultipartFormData formData, String key){
        Http.MultipartFormData.FilePart filePart = formData.getFile(key);
        File file = filePart.getFile();
        String dir = "public/audio/" + formData.asFormUrlEncoded().get("spellingregelnaam")[0] + "/uitleg/";
        File directory = new File(dir);
        if(!directory.exists())
            directory.mkdirs();
        file.renameTo(new File(dir + filePart.getFilename()));
        return dir.substring(dir.indexOf("audio/"), dir.length()) + filePart.getFilename();
    }

    public static Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(
            Routes.javascriptRouter("jsRoutes",
                    routes.javascript.OpdrachtSpellingController.checkAntwoord(),
                    routes.javascript.OpdrachtSpellingController.opdrachtMaken(),
                    routes.javascript.ToetsController.checkAntwoord(),
                    routes.javascript.AnalyzeOverzichtController.analyzeOverzicht(),
                    routes.javascript.AnalyzeOverzichtController.deletePerDatum(),
                    routes.javascript.AnalyzeOverzichtController.deletePerOpdracht(),
                    routes.javascript.AnalyzeOverzichtController.deletePerToets()
            )
        );
    }
}
