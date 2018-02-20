package com.turboconsulting.Service;

import com.turboconsulting.DAO.FakeExperimentDao;
import com.turboconsulting.DAO.MySqlExperimentDao;
import com.turboconsulting.DAO.MySqlVisitorDao;
import com.turboconsulting.Entity.DidExperiment;
import com.turboconsulting.Entity.Experiment;
import com.turboconsulting.Entity.LoginDetails;
import com.turboconsulting.Entity.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ConsentService {

    @Autowired
    @Qualifier("sqlVisitorData")
    private MySqlVisitorDao visitorDao;

    @Autowired
    @Qualifier("sqlExperimentData")
    private MySqlExperimentDao experimentDao;

    public Iterable<Visitor> getAllVisitors(){
        return visitorDao.findAll();
    }

    public Iterable<Experiment> getAllExperiments(){
        return experimentDao.findAll();
    }

    public boolean checkLoginDetails(LoginDetails loginDetails)  {
        Iterable<Visitor> visitors = visitorDao.findAll();
        for (Visitor v : visitors) {
            if(v.getUname().equals(loginDetails.getUname()))  {
                if(v.getPassword().equals(loginDetails.getPword()))  return true;
            }
        }
        return false;
    }

    public void updatePassword(LoginDetails login) {
        //visitorDao.updateVisitor(login);


    }

    public void addNewUser(Visitor v)  {
        DidExperiment e = new DidExperiment(v, experimentDao.findOne(1));
        v.doExperiment(e);
        experimentDao.findOne(1).doExperiment(e);
        visitorDao.save(v);


    }

    public void addNewExperiment(Experiment e){
        experimentDao.save(e);
    }
}
