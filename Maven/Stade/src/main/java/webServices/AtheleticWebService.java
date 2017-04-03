package webServices;

import beans.Credentials;
import beans.Athletic;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("atheletics")
public class AtheleticWebService {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postLogin(Credentials credentials) {
        String username = credentials.getUsername();

        //Get athlectics from DB
        Athletic athletic = new Athletic();

        return Response.status(Response.Status.CREATED).entity(athletic).build();
    }
}
