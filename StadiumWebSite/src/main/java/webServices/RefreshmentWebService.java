package webServices;

import beans.Refreshment;
import dao.impl.RefreshmentDaoImpl;
import exceptions.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

@Produces(MediaType.APPLICATION_JSON)
@Path("refreshments")
public class RefreshmentWebService {

    private final float BASE_BUVETTE = 1024;
    private RefreshmentDaoImpl refreshmentDao = new RefreshmentDaoImpl();

    @GET
    @Path("/{id}")
    public Response getRefreshment(@PathParam("id") int id) {

        if (id < 1 || id > 2) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        String pourcentage;
        Refreshment refreshment;
        int i = 0;
        try {

            pourcentage = "193;167;";
            String[] tokens = pourcentage.split("[;]+");
            ArrayList<Refreshment> refreshments = refreshmentDao.getAllRefreshment();
            for (Refreshment refresh : refreshments) {
                float value = Integer.parseInt(tokens[i])/BASE_BUVETTE;

                BigDecimal bd = new BigDecimal(value);
                bd = bd.setScale(2, RoundingMode.HALF_UP);

                refreshmentDao.setAttendance(refresh.getId(), bd.floatValue());
                i +=1;
            }
            refreshment = refreshmentDao.getRefreshment(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(refreshment).build();
    }

    @GET
    public Response getRefreshments() {
        ArrayList<Refreshment> refreshments;
        try {
            refreshments = refreshmentDao.getAllRefreshment();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(refreshments).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postRefreshment(Refreshment refreshment) {
        int id = refreshmentDao.addRefreshment(refreshment.getAttendance(), refreshment.getLocalisation());
        refreshment.setId(id);
        return Response.status(Response.Status.CREATED).entity(refreshment).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putRefreshment(@PathParam("id") int id, Refreshment refreshment1) {
        try {
            Refreshment refreshment = refreshmentDao.getRefreshment(id);
            if (refreshment.getAttendance() != refreshment1.getAttendance()) refreshmentDao.setAttendance(id, refreshment1.getAttendance());
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(refreshment1).build();
    }

    private String getUrl() throws NotFoundException {
        HttpURLConnection connection = null;
        StringBuilder output = new StringBuilder();

        try {
            //Create connection
            URL url = new URL("http://192.168.2.2/G");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            //Get Response
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                output.append(inputLine);
            }

            br.close();

            return output.toString();
        } catch (Exception e) {
            throw new NotFoundException();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
