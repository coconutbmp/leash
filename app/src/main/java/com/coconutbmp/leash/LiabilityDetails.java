package com.coconutbmp.leash;

import org.json.JSONObject;

/**
 * Interface to provide the {@link LiabilityDetails#getJSONRepresentation()} function for implementation
 * this allows multiple classes to be able implement concrete classes for this function.
 * Reduces coupling with specific implementation in individual classes
 */
public interface LiabilityDetails {
    public JSONObject getJSONRepresentation() throws Exception;
}
