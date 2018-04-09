package com.turboconsulting.Entity;

import com.turboconsulting.Service.ConsentService;

import java.util.*;
import javax.persistence.*;

@Entity
public class Visitor {



    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int visitorId;

    @ManyToOne
    @JoinColumn(name="accountId", nullable = false)
    private Account account;

    private String name;
    private GregorianCalendar dob;

    @Transient
    private ConsentOption defaultConsent;

    @OneToMany(mappedBy = "visitor", cascade = CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER)
    private Set<VisitorExperiment> experiments;

    public Visitor(){}

    public Visitor(String name, GregorianCalendar dob) {
        this.name = name;
        this.dob = dob;
        this.defaultConsent = new ConsentOption("NO CONSENT", "This option means you do not give consent for We the Curious to use any of your data");
        experiments = new HashSet<VisitorExperiment>();

    }


    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public ConsentOption getDefaultConsent() {
        return defaultConsent;
    }
    public void setDefaultConsent(ConsentOption defaultConsent) {
        this.defaultConsent = defaultConsent;
    }

    public void setDob(GregorianCalendar dob) {
        this.dob = dob;
    }
    public GregorianCalendar getDob() {
        return dob;
    }

    public void setVisitorId(int visitorId) {
        this.visitorId = visitorId;
    }
    public int getVisitorId() {
        return visitorId;
    }

    public boolean addExperiment(VisitorExperiment visitorExperiment)  {
        return experiments.add(visitorExperiment);
    }

    public void setExperiments(Set<VisitorExperiment> experiments) {
        this.experiments = experiments;
    }
    public Collection<VisitorExperiment> getExperiments() {
        return experiments;
    }

    public void doExperiment(VisitorExperiment e) {
        experiments.add(e);
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    public Account getAccount() { return account; }

    public int getPendingExperiments()  {
        int count = 0;
        for (VisitorExperiment ve : experiments)
            count += ve.getConsentChanged()? 0 : 1;
        return count;
    }

}
