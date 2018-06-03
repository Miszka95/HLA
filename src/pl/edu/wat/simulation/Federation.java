package pl.edu.wat.simulation;

import hla.rti.*;
import hla.rti.jlc.RtiFactoryFactory;
import org.portico.impl.hla13.types.DoubleTime;
import org.portico.impl.hla13.types.DoubleTimeInterval;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

public class Federation {

    public static final String SYNCHRONIZATION_POINT = "Restaurant Opening";

    private static final String FED_FILE = "restaurant.fed";
    private static final String FEDERATION_NAME = "Restaurant";

    private static RTIambassador rti;

    static {
        try {
            rti = RtiFactoryFactory.getRtiFactory().createRtiAmbassador();
            File fom = new File(FED_FILE);
            rti.createFederationExecution(FEDERATION_NAME, fom.toURI().toURL());
        } catch (RTIexception | MalformedURLException exception) {
            System.out.println("Federation already exists. Joining federation " + FEDERATION_NAME);
        }
    }

    public static void tick() {
        try {
            rti.tick();
        } catch (RTIexception exception) {
            exception.printStackTrace();
        }
    }

    public static void advanceTime(double timeStep, Ambassador ambassador) {
        ambassador.setAdvancing(true);
        LogicalTime newTime = convertTime(ambassador.getFederateTime() + timeStep);
        try {
            rti.timeAdvanceRequest(newTime);
            while (ambassador.isAdvancing()) {
                tick();
            }
        } catch (RTIexception exception) {
            exception.printStackTrace();
        }
    }

    public static void join(String name, Ambassador ambassador) {
        try {
            rti.joinFederationExecution(name, FEDERATION_NAME, ambassador);
        } catch (RTIexception exception) {
            exception.printStackTrace();
        }
        System.out.println(String.format("%s joined %s", name, FEDERATION_NAME));
    }

    public static void synchronize(Ambassador ambassador) {
        try {
            rti.registerFederationSynchronizationPoint(SYNCHRONIZATION_POINT, null);
            while (!ambassador.isAnnounced()) {
                tick();
            }
            waitForUser();
            rti.synchronizationPointAchieved(SYNCHRONIZATION_POINT);
            System.out.println("Synchronization point achieved");
            while (!ambassador.isReadyToRun()) {
                tick();
            }

        } catch (RTIexception exception) {
            exception.printStackTrace();
        }
    }

    public static void enableTimePolicy(Ambassador ambassador) {
        LogicalTime currentTime = convertTime(ambassador.federateTime);
        LogicalTimeInterval lookahead = convertInterval(ambassador.federateLookahead);

        try {
            rti.enableTimeRegulation(currentTime, lookahead);
            while (!ambassador.isRegulating()) {
                tick();
            }
            rti.enableTimeConstrained();
            while (!ambassador.isConstrained()) {
                tick();
            }
        } catch (RTIexception exception) {
            exception.printStackTrace();
        }
    }

    public static int publishInteraction(InteractionType interactionType) {
        int handle = 0;
        try {
            handle = getInteractionClassHandle(interactionType);
            rti.publishInteractionClass(handle);
        } catch (RTIexception exception) {
            exception.printStackTrace();
        }
        return handle;
    }

    public static int subscribeInteraction(InteractionType interactionType) {
        int handle = 0;
        try {
            handle = getInteractionClassHandle(interactionType);
            rti.subscribeInteractionClass(handle);
        } catch (RTIexception exception) {
            exception.printStackTrace();
        }
        return handle;
    }

    public static int getInteractionClassHandle(InteractionType interactionType) {
        try {
            return rti.getInteractionClassHandle(interactionType.getLabel());
        } catch (RTIexception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static SuppliedParameters getSuppliedParameters() {
        try {
            return RtiFactoryFactory.getRtiFactory().createSuppliedParameters();
        } catch (RTIexception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static int getParameterHandle(String parameterName, int interactionHandle) {
        try {
            return rti.getParameterHandle(parameterName, interactionHandle);
        } catch (RTIexception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static void sendInteraction(int interactionHandle, SuppliedParameters parameters, Ambassador ambassador) {
        try {
            rti.sendInteraction(interactionHandle, parameters, "tag".getBytes(),
                    convertTime(ambassador.getFederateTime() + ambassador.getFederateLookahead()));
        } catch (RTIexception exception) {
            exception.printStackTrace();
        }
    }

    private static void waitForUser() {
        System.out.println("Press ENTER to start simulation");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LogicalTime convertTime(double time) {
        return new DoubleTime(time);
    }

    private static LogicalTimeInterval convertInterval(double time) {
        return new DoubleTimeInterval(time);
    }

    private Federation() {
    }
}
