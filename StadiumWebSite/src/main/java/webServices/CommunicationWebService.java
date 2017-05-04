package webServices;

import beans.Refreshment;
import beans.custom.RefreshmentsCustom;
import dao.impl.RefreshmentDaoImpl;
import exceptions.NotFoundException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

@Produces(MediaType.APPLICATION_JSON)
@Path("communication")
public class CommunicationWebService {

    private final float BASE_BUVETTE = 1023;
    private RefreshmentDaoImpl refreshmentDao = new RefreshmentDaoImpl();

    @POST
    @Path("/refreshments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postCustomRefresh(RefreshmentsCustom refreshmentsCustom) {
        try {
            float value1 = toAttendance(refreshmentsCustom.getAttendance1());
            float value2 = toAttendance(refreshmentsCustom.getAttendance2());

            ArrayList<Refreshment> refreshments = refreshmentDao.getAllRefreshment();
            refreshmentDao.setAttendance(refreshments.get(0).getId(), value1);
            refreshmentDao.setAttendance(refreshments.get(1).getId(), value2);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.CREATED).build();
    }

    private float toAttendance(int base) {
        float value = base/BASE_BUVETTE;

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);

        return bd.floatValue();
    }
}
