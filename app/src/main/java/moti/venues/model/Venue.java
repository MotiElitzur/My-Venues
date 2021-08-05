package moti.venues.model;

import java.io.Serializable;

/**
 * This class contains the venue details and properties.
 */
public class Venue implements Serializable {

    // region Data Members

    // The venue id.
    private String _id;

    // The venue name.
    private String _name;

    // The venue location address.
    private String _address;

    // endregion

    // region Constructor

    public Venue() {
    }

    public Venue(String name, String address) {
        this._name = name;
        this._address = address;
    }

    public Venue(String id, String name, String address) {

        this._id = id;
        this._name = name;
        this._address = address;
    }

    // endregion

    // region Properties

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getAddress() {
        return _address;
    }

    public void setAddress(String address) {
        this._address = address;
    }

    // endregion
}