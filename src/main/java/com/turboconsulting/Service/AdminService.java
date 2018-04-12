package com.turboconsulting.Service;

import com.turboconsulting.DAO.*;
import com.turboconsulting.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

@Service
public class AdminService implements AdminServiceInterface {


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    @Qualifier("sqlVisitorData")
    private VisitorDao visitorDao;
    @Autowired
    @Qualifier("sqlVisitorExperimentData")
    private VisitorExperimentDao visitorExperimentDao;
    @Autowired
    @Qualifier("sqlAccountData")
    private AccountDao accountDao;
    @Autowired
    @Qualifier("sqlExperimentData")
    private ExperimentDao experimentDao;
    @Autowired
    @Qualifier("sqlConsentData")
    private ConsentOptionDao consentOptionDao;
    @Autowired
    @Qualifier("sqlConsentExperimentData")
    private ConsentExperimentDao consentExperimentDao;



    @Override
    @PostConstruct
    public void AdminService() {

        accountDao.deleteAll();
        experimentDao.deleteAll();
        consentOptionDao.deleteAll();
        consentExperimentDao.deleteAll();

        consentOptionDao.save(new ConsentOption("FULL CONSENT",
                "This option means you give consent for We the Curious to use all of your data"));
        consentOptionDao.save(new ConsentOption("NO CONSENT",
                "This option means you do not give consent for We the Curious to use any of your data"));


        Account account1 = new Account("Harry", "hw16471@bristol.ac.uk", bCryptPasswordEncoder.encode("password"));
        addNewAccount(account1);
        Visitor visitor1 = new Visitor("Harry", new GregorianCalendar(0, 0, 0 ));
        addNewVisitor(visitor1, account1.getAccountId());
        Experiment experiment1 = new Experiment("Physics Experiment", "A lovely desciption.");
        addNewExperiment(experiment1, new HashSet<>());
        addVisitorExperiment(visitor1.getVisitorId(), experiment1.getId());

        Account account2 = new Account("Finn", "user@turboconsent.com", bCryptPasswordEncoder.encode("password"));
        addNewAccount(account2);
        Visitor visitor2 = new Visitor("Finn", new GregorianCalendar(0, 0, 0 ));
        addNewVisitor(visitor2, account2.getAccountId());
        Experiment experiment2 = new Experiment("Chemistry Experiment", "A lovely desciption.");
        addNewExperiment(experiment2, new HashSet<>());
        addVisitorExperiment(visitor2.getVisitorId(), experiment1.getId());
        addVisitorExperiment(visitor2.getVisitorId(), experiment2.getId());

    }



    @Override
    public boolean addNewAccount(Account a)  {
        return (accountDao.findByEmail(a.getEmail()) == null) && (accountDao.save(a) != null);
    }
    @Override
    public Iterable<Account> getAllAccounts(){
        return accountDao.findAll();
    }

    @Override
    public boolean addNewVisitor(Visitor v, int accountID)  {
        v.setAccount(accountDao.findByAccountId(accountID));
        v.setDefaultConsent(consentOptionDao.findByName("NO CONSENT"));
        consentOptionDao.findByName("NO CONSENT").addVisitor(v);
        return visitorDao.save(v) != null;
    }
    @Override
    public Iterable<Visitor> getAllVisitors(){
        return visitorDao.findAll();
    }

    @Override
    public boolean addNewExperiment(Experiment e, HashSet<ConsentOption> newConsentOptions){
        if( experimentDao.findByName(e.getName()) != null  )  return false;

        Set<ConsentExperiment> consentExperiments = new HashSet<>();
        consentExperiments.add(new ConsentExperiment(consentOptionDao.findByName("FULL CONSENT"), e));
        consentExperiments.add(new ConsentExperiment(consentOptionDao.findByName("NO CONSENT"), e));

        for (ConsentOption c : newConsentOptions)  {
            if (consentOptionDao.findByName(c.getName()) == null)  {
                consentOptionDao.save(c);
            }
            consentExperiments.add(new ConsentExperiment(consentOptionDao.findByName(c.getName()), e));
        }
        e.setConsentExperiments(consentExperiments);
        experimentDao.save(e);
        for(ConsentExperiment consentExperiment : consentExperiments)  {
            consentExperiment.getConsentOption().addConsentExperiment(consentExperiment);
            consentOptionDao.save(consentExperiment.getConsentOption());
        }
        return true;
    }
    @Override
    public Iterable<Experiment> getAllExperiments(){
        return experimentDao.findAll();
    }

    @Override
    public ArrayList<VisitorExperiment> getVisitorExperiments(int id)  {
        ArrayList<VisitorExperiment> visitorExperimentsList = new ArrayList<>();
        Iterable<VisitorExperiment> visitorExperimentsIterable = visitorExperimentDao.findAllByVisitor(visitorDao.findByVisitorId(id));

        visitorExperimentsIterable.forEach(visitorExperimentsList::add);
        visitorExperimentsList.sort((ve1, ve2) -> ve2.getDate().compareTo(ve1.getDate()));

        return visitorExperimentsList;
    }
    @Override
    public boolean addVisitorExperiment(int visitorId, int experimentId)  {
        VisitorExperiment visitorExperiment = new VisitorExperiment( visitorDao.findByVisitorId(visitorId),
                experimentDao.findById(experimentId));

        experimentDao.findById(experimentId).addVisitorExperiment(visitorExperiment);
        Visitor v = visitorDao.findByVisitorId(visitorId);
        v.doExperiment(visitorExperiment);
        visitorExperiment.getConsentOption().addExperiment(visitorExperiment);
        //consentOptionDao.save(visitorExperiment.getConsentOption());
        return visitorDao.save(v) != null;
    }
    @Override
    public Iterable<VisitorExperiment> getAllVisitorExperiments()  {
        return visitorExperimentDao.findAll();
    }



}
