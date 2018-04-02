package de.marcus_deuss.inventorytracker.db.entity;

public class Owner {
    // private variables
    private long id;                       // database internal number, must be long!
    private String ownerName;

    // Empty constructor
    public Owner(){

    }

    // constructor
    public Owner(long id, String ownerName){
        this.id = id;                           // id autogenerated in database
        this.ownerName = ownerName;
    }

    // getter methods
    public long getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    // setter methods

    public void setId(long id) {
        this.id = id;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public String toString() {
        return "id:" + id + ", name:" + ownerName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Owner other = (Owner) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
