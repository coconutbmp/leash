package com.coconutbmp.leash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import com.coconutbmp.leash.BudgetComponentInstrumentedTest;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class BudgetBriefIntrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class) ;

    @Test
    public void test_1_initiate(){
        //BudgetComponentInstrumentedTest preceeding = new BudgetComponentInstrumentedTest();
        //preceeding.test_6_ensure_populated();
        //ActivityScenario scenario = rule.getScenario();
        System.out.println("setup complete");
    }

    @Test
    public void test_2_create_liability_brief(){

    }
}
