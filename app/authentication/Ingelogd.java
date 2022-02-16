package authentication;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class Ingelogd extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context ctx) {
        return ctx.session().get("functie");
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return redirect(controllers.routes.LoginController.login());
    }
}
