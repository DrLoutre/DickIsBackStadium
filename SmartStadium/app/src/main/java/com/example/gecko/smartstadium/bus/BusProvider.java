package com.example.gecko.smartstadium.bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public final class BusProvider {

    private static final Bus BUS = new Bus(ThreadEnforcer.ANY);

    /**
     * Get the only instance of the bus
     * @return
     */
    public static Bus getInstance() {
        return BUS;
    }

    /**
     * Private due to singleton.
     */
    private BusProvider() {
    }
}
