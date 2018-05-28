package pl.edu.wat.simulation;

import hla.rti.*;
import hla.rti.jlc.NullFederateAmbassador;
import org.portico.impl.hla13.types.DoubleTime;

public abstract class Ambassador extends NullFederateAmbassador {

    protected double federateTime = 0.0;
    protected double federateLookahead = 1.0;

    protected boolean isRegulating = false;
    protected boolean isConstrained = false;
    protected boolean isAdvancing = false;

    protected boolean isAnnounced = false;
    protected boolean isReadyToRun = false;

    protected boolean running = true;

    @Override
    public void synchronizationPointRegistrationFailed(String synchronizationPointLabel) throws FederateInternalError {
        super.synchronizationPointRegistrationFailed(synchronizationPointLabel);
        System.out.println("Failed to register sync point: " + synchronizationPointLabel);
    }

    @Override
    public void synchronizationPointRegistrationSucceeded(String synchronizationPointLabel) throws FederateInternalError {
        super.synchronizationPointRegistrationSucceeded(synchronizationPointLabel);
        System.out.println("Successfully registered sync point: " + synchronizationPointLabel);
    }

    @Override
    public void announceSynchronizationPoint(String synchronizationPointLabel, byte[] userSuppliedTag) throws FederateInternalError {
        super.announceSynchronizationPoint(synchronizationPointLabel, userSuppliedTag);
        System.out.println("Synchronization point announced: " + synchronizationPointLabel);
        if (synchronizationPointLabel.equals(Federation.SYNCHRONIZATION_POINT)) {
            setAnnounced(true);
        }
    }

    @Override
    public void federationSynchronized(String synchronizationPointLabel) throws FederateInternalError {
        super.federationSynchronized(synchronizationPointLabel);
        System.out.println("Federation synchronized for: " + synchronizationPointLabel);
        if (synchronizationPointLabel.equals(Federation.SYNCHRONIZATION_POINT)) {
            setReadyToRun(true);
        }
    }

    @Override
    public void timeRegulationEnabled(LogicalTime theFederateTime) throws InvalidFederationTime, EnableTimeRegulationWasNotPending, FederateInternalError {
        super.timeRegulationEnabled(theFederateTime);
        setFederateTime(convertTime(theFederateTime));
        setRegulating(true);
    }

    @Override
    public void timeConstrainedEnabled(LogicalTime theFederateTime) throws InvalidFederationTime, EnableTimeConstrainedWasNotPending, FederateInternalError {
        super.timeConstrainedEnabled(theFederateTime);
        setFederateTime(convertTime(theFederateTime));
        setConstrained(true);
    }

    @Override
    public void timeAdvanceGrant(LogicalTime theTime) throws InvalidFederationTime, TimeAdvanceWasNotInProgress, FederateInternalError {
        super.timeAdvanceGrant(theTime);
        setFederateTime(convertTime(theTime));
        setAdvancing(false);
    }

    protected double convertTime(LogicalTime logicalTime) {
        return ((DoubleTime) logicalTime).getTime();
    }

    public double getFederateTime() {
        return federateTime;
    }

    public void setFederateTime(double federateTime) {
        this.federateTime = federateTime;
    }

    public double getFederateLookahead() {
        return federateLookahead;
    }

    public void setFederateLookahead(double federateLookahead) {
        this.federateLookahead = federateLookahead;
    }

    public boolean isRegulating() {
        return isRegulating;
    }

    public void setRegulating(boolean regulating) {
        isRegulating = regulating;
    }

    public boolean isConstrained() {
        return isConstrained;
    }

    public void setConstrained(boolean constrained) {
        isConstrained = constrained;
    }

    public boolean isAdvancing() {
        return isAdvancing;
    }

    public void setAdvancing(boolean advancing) {
        isAdvancing = advancing;
    }

    public boolean isAnnounced() {
        return isAnnounced;
    }

    public void setAnnounced(boolean announced) {
        isAnnounced = announced;
    }

    public boolean isReadyToRun() {
        return isReadyToRun;
    }

    public void setReadyToRun(boolean readyToRun) {
        isReadyToRun = readyToRun;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}