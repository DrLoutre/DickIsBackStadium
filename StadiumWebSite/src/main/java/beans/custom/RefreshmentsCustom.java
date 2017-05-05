package beans.custom;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RefreshmentsCustom {

    private int attendance1;
    private int attendance2;

    public int getAttendance1() {
        return attendance1;
    }

    public void setAttendance1(int attendance1) {
        this.attendance1 = attendance1;
    }

    public int getAttendance2() {
        return attendance2;
    }

    public void setAttendance2(int attendance2) {
        this.attendance2 = attendance2;
    }
}
