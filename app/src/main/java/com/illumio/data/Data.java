package com.illumio.data;

import java.io.Serializable;

/**
 * Marker Interface for Data that analyzer returns
 */
public interface Data extends Serializable {
    String identifier();
}
