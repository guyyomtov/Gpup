package DataManager;

import Graph.Target;

import java.util.*;

public abstract class Task {


    private String myStatus = new String();


    public List<String> setMyParentsNames(Target t){

        List<String> parentsNames = new ArrayList<>();

        for(Target curT : t.getRequiredFor())
            parentsNames.add(curT.getName());

        return parentsNames;
    }

    public List<String> setMyKidsNames(Target t){

        List<String> kidsNames = new ArrayList<>();

        for(Target curT : t.getDependsOn())
            kidsNames.add(curT.getName());

        return kidsNames;
    }

  //  public List<String> getMyKidsNames(){ return this.myKidsNames;}

    public abstract List<String> tryToRunMe(List<Simulation> myKids, Map<String,Simulation> allTasks);

    public abstract List<String> runMe(Map<String,Simulation> allTasks);

    public String getMyStatus(){return this.myStatus;}



}
