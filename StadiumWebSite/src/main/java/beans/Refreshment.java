package beans;

public class Refreshment {

    private int Id;
    private float Attendance;
    private String Localisation;

    public Refreshment() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public float getAttendance() {
        return Attendance;
    }

    public void setAttendance(float attendance) {
        Attendance = attendance;
    }

    public String getLocalisation() {
        return Localisation;
    }

    public void setLocalisation(String localisation) {
        Localisation = localisation;
    }
}
