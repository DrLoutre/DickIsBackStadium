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

/**
 * This class regroups all WebService associated to Refreshments
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("refreshments")
public class RefreshmentWebService {

    private final float BASE_BUVETTE = 1023;
    private final String IP_ARDUINO = "http://192.168.2.2";
    private RefreshmentDaoImpl refreshmentDao = new RefreshmentDaoImpl();

    /**
     * Get a refreshment with a specific id
     * @param id The refreshment's id
     * @return Refreshment
     */
    @GET
    @Path("/{id}")
    public Response getRefreshment(@PathParam("id") int id) {

        if (id < 1 || id > 2) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Refreshment refreshment;
        try {
            /*
            int i = 0;
            float [] pourcentage = getFromArduino();
            ArrayList<Refreshment> refreshments = refreshmentDao.getAllRefreshment();
            for (Refreshment refresh : refreshments) {
                refreshmentDao.setAttendance(refresh.getId(), pourcentage[i]);
                i +=1;
            }*/
            refreshment = refreshmentDao.getRefreshment(id);
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(refreshment).build();
    }

    /**
     * Get all refreshments
     * @return ArrayList<Refreshment>
     */
    @GET
    public Response getRefreshments() {
        ArrayList<Refreshment> refreshments;
        try {
            /*int i = 0;
            float [] pourcentage = getFromArduino();
            refreshments = refreshmentDao.getAllRefreshment();
            for (Refreshment refresh : refreshments) {
                refreshmentDao.setAttendance(refresh.getId(), pourcentage[i]);
                i +=1;
            }*/
            refreshments = refreshmentDao.getAllRefreshment(); //Get again due to update
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(refreshments).build();
    }

    /**
     * Create a new refreshment
     * @param refreshment The refreshment object
     * @return Refreshment
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postRefreshment(Refreshment refreshment) {
        int id = refreshmentDao.addRefreshment(refreshment.getAttendance(), refreshment.getLocalisation());
        refreshment.setId(id);
        return Response.status(Response.Status.CREATED).entity(refreshment).build();
    }

    /**
     * Update a refreshment and all its attributes
     * @param id The refreshment's id
     * @param refreshment1 The refreshment object
     * @return Refreshment
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putRefreshment(@PathParam("id") int id, Refreshment refreshment1) {
        try {
            Refreshment refreshment = refreshmentDao.getRefreshment(id);
            if (refreshment.getAttendance() != refreshment1.getAttendance()) {
                refreshmentDao.setAttendance(id, refreshment1.getAttendance());
            }
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(refreshment1).build();
    }

    /**
     * Not used anymore.
     * @return
     * @throws NotFoundException
     */
    @Deprecated
    private float[] getFromArduino() throws NotFoundException {
        HttpURLConnection connection = null;
        StringBuilder output = new StringBuilder();

        try {
            //Create connection
            URL url = new URL(IP_ARDUINO);
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

            //Use to remove all Html character
            char[] tab = output.toString().toCharArray();
            output = new StringBuilder();
            for (char c : tab) {
                if (Character.isDigit(c) || c == ';') {
                    output.append(c);
                }
            }

            return splitMessage(output.toString());
        } catch (Exception e) {
            throw new NotFoundException();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Not used anymore.
     * @param message
     * @return
     */
    @Deprecated
    private float[] splitMessage(String message) {
        String[] tokens = message.split("[;]+");
        float[] percentage = new float[tokens.length];

        for (int i = 0; i < tokens.length ; i++) {

            float value = Integer.parseInt(tokens[i])/BASE_BUVETTE;

            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(2, RoundingMode.HALF_UP);

            percentage[i] = bd.floatValue();
        }

        return percentage;
    }
}
