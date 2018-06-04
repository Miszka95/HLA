package pl.edu.wat.simulation.client;

import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import pl.edu.wat.simulation.Ambassador;
import pl.edu.wat.simulation.restaurant.Restaurant;

public class ClientAmbassador extends Ambassador {

    @Override
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] userSuppliedTag, LogicalTime theTime, EventRetractionHandle retractionHandle) throws ObjectNotKnown, AttributeNotKnown, FederateOwnsAttributes, InvalidFederationTime, FederateInternalError {
        super.reflectAttributeValues(theObject, theAttributes, userSuppliedTag, theTime, retractionHandle);

        Restaurant restaurant = ((ClientFederate) getFederate()).getRestaurant();
        try {
            restaurant.setFreePlaces(EncodingHelpers.decodeInt(theAttributes.getValue(0)));
        } catch (ArrayIndexOutOfBounds arrayIndexOutOfBounds) {
            arrayIndexOutOfBounds.printStackTrace();
        }
    }
}
