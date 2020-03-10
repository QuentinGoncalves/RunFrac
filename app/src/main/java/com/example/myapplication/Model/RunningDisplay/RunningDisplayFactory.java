package com.example.myapplication.Model.RunningDisplay;

import android.content.Context;

import com.example.myapplication.Model.RunningDisplay.Strategies.AltitudeGainStrategy;
import com.example.myapplication.Model.RunningDisplay.Strategies.AltitudeLossStrategy;
import com.example.myapplication.Model.RunningDisplay.Strategies.AltitudeStrategy;
import com.example.myapplication.Model.RunningDisplay.Strategies.AvgPaceStrategy;
import com.example.myapplication.Model.RunningDisplay.Strategies.AvgSpeedStrategy;
import com.example.myapplication.Model.RunningDisplay.Strategies.DistanceStrategy;
import com.example.myapplication.Model.RunningDisplay.Strategies.PaceStrategy;
import com.example.myapplication.Model.RunningDisplay.Strategies.SpeedStrategy;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RunningDisplayFactory {
    private List<RunningDisplayStrategy> displayedStrategy = new ArrayList<RunningDisplayStrategy>();
    private List<RunningDisplayStrategy> undisplayedStrategy = new ArrayList<RunningDisplayStrategy>();

    public RunningDisplayFactory() {
        displayedStrategy.add(new SpeedStrategy());
        displayedStrategy.add(new DistanceStrategy());
        displayedStrategy.add(new PaceStrategy());
        displayedStrategy.add(new AvgSpeedStrategy());

        displayedStrategy.get(0).setLayout(R.id.running_layout1);
        displayedStrategy.get(1).setLayout(R.id.running_layout2);
        displayedStrategy.get(2).setLayout(R.id.running_layout3);
        displayedStrategy.get(3).setLayout(R.id.running_layout4);

        undisplayedStrategy.add(new AvgPaceStrategy());
        undisplayedStrategy.add(new AltitudeStrategy());
        undisplayedStrategy.add(new AltitudeGainStrategy());
        undisplayedStrategy.add(new AltitudeLossStrategy());
    }

    public List<RunningDisplayStrategy> getDisplayedStrategy() {
        return displayedStrategy;
    }

    public RunningDisplayStrategy getStrategyByLayoutId(int id) {
        Iterator iter = displayedStrategy.iterator();
        while (iter.hasNext()) {
            RunningDisplayStrategy strategy = (RunningDisplayStrategy) iter.next();
            if (strategy.getLayout() == id) {
                return strategy;
            }
        }
        return  null;
    }

    public List<String> getUndisplayedStrategyTitle(Context c){
        List<String> tmp = new ArrayList<String>();
        Iterator iter = undisplayedStrategy.iterator();
        while (iter.hasNext()){
            RunningDisplayStrategy undisplayed = (RunningDisplayStrategy) iter.next();
            tmp.add(undisplayed.getTitle(c));
        }
        return tmp;
    }

    public void setDisplayedStrategy(String s, int id, Context c){
        Iterator iter =  undisplayedStrategy.iterator();
        RunningDisplayStrategy strategyDisplaying = null, strategyClicked = null;
        while(iter.hasNext()){
            strategyClicked = (RunningDisplayStrategy) iter.next();
            if(strategyClicked.getTitle(c).equals(s)){
                break;
            }
        }
        strategyClicked.setLayout(id);
        undisplayedStrategy.remove(strategyClicked);
        displayedStrategy.add(strategyClicked);

        iter = displayedStrategy.iterator();
        while(iter.hasNext()){
            strategyDisplaying = (RunningDisplayStrategy) iter.next();
            if (strategyDisplaying.getLayout() == id){
                break;
            }
        }
        strategyDisplaying.setLayout(0);
        displayedStrategy.remove(strategyDisplaying);
        undisplayedStrategy.add(strategyDisplaying);
    }
}