package com.coconutbmp.leash;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UXFunctionsTest{
    String today;

    @Mock
    TextView month;

    @Mock
    TextView day;

    @Mock
    Button sender;

    @Mock
    TextView date;

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

    @Test
    public void select_date_test_in_save(){
        DatePicker datePicker = mock(DatePicker.class);
        Mockito.when(sender.getText()).thenReturn("Save");
        Mockito.when(datePicker.getYear()).thenReturn(2022);
        Mockito.when(datePicker.getMonth()).thenReturn(6);
        Mockito.when(datePicker.getDayOfMonth()).thenReturn(12);
        String s = Integer.toString(datePicker.getYear()) + "-"
                + Integer.toString(datePicker.getMonth()) + "-" + Integer.toString(datePicker.getDayOfMonth());
        Mockito.doCallRealMethod().when(date).setText(s);
        Mockito.when(date.getText()).thenReturn(s);
        UXFunctions.select_date(sender, datePicker, date);
        assertEquals(s, date.getText());
    }

    @Test
    public void select_date_test_in_set(){
        DatePicker datePicker = mock(DatePicker.class);
        Mockito.when(sender.getText()).thenReturn("Set Date");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);
        UXFunctions.select_date(sender, datePicker, date);
        String  s = baos.toString();
        assertEquals("<- olo ->", s.trim());
        System.out.flush();
        System.setOut(old);
    }
}
