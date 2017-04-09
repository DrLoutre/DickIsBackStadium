package Modes;

public enum ModeType {

    NORMAL_MODE("Production mode"),
    DEMO1_MODE("Mode for demo 1");

    private String mode;

    ModeType(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return this.mode;
    }
}
