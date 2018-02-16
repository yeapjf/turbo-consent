package com.turboconsulting.Entity;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Visitor {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private String uname, name, password;
    private ConsentLevel consentLevel;
    private GregorianCalendar dob;
    private ArrayList<Integer> experimentIDs;
    private ArrayList<Visitor> dependents;

    public Visitor(int id, String uname, String password, String name, GregorianCalendar dob) {
        this.id = id;
        this.uname = uname;
        this.password = password;
        this.name = name;
        this.consentLevel = ConsentLevel.NONE;
        this.dob = dob;
        this.experimentIDs = new ArrayList<Integer>();
        this.dependents = new ArrayList<Visitor>();

    }

    public ConsentLevel getConsentLevel() {
        return consentLevel;
    }

    public void setConsentLevel(ConsentLevel consentLevel) {
        this.consentLevel = consentLevel;
    }

    public int getId() {
        return id;
    }

    public String getUname() {
        return uname;
    }

    public String getName() {
        return name;
    }

    public GregorianCalendar getDob() {
        return dob;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Integer> getExperimentIDs() {
        return experimentIDs;
    }

    public void addExperimentID(int experimentID) {
        this.experimentIDs.add(experimentID);
    }

    public ArrayList<Visitor> getDependents() {
        return dependents;
    }

    public void setDependents(ArrayList<Visitor> dependents) {
        this.dependents = dependents;
    }
}
