package com.coconutbmp.leash;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.content.Context;
import android.text.Editable;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UXFunctionsTest{
    String today;

    @Mock
    TextView month;
    @Mock
    TextView day;

    @Before
    public void setUp(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
        today = sdf.format(new Date());
        MockitoAnnotations.openMocks(this);
        Mockito.doNothing().when(day).setText(today.substring(0, 2));
        Mockito.when(day.getText()).thenReturn(today.substring(0, 2));
        Mockito.doNothing().when(day).setText(today.substring(3, 6));
        Mockito.when(month.getText()).thenReturn(today.substring(3, 6));
    }

    @Test
    public void testDay(){
        UXFunctions.setDate(day, month);
        assertEquals(today.substring(0, 2), day.getText().toString());
    }

    @Test
    public void testMonth(){
        UXFunctions.setDate(day, month);
        assertEquals(today.substring(3, 6), month.getText().toString());
    }
}
