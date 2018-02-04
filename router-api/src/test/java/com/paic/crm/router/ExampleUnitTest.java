package com.paic.crm.router;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String a = "a";
        String b = "b";
        String c = "a" + b;//new String ();
        String d = "ab";
        String e  = "a"+"b";
        String f = new String("a");
        final String g = "b";
        String h = "a"+g;
        final String i = getStr();
        String  j = "a"+i;
        System.out.println("c==d?"+(c == d));
        System.out.println("c.intern() == d?"+(c.intern() == d));
        System.out.println("d == e?"+(d == e));
        System.out.println("a == f?"+(a == f));
        System.out.println("a == f.intern()?"+(a == f.intern()));
        System.out.println("a.equals(f)?"+(a.equals(f)));
        System.out.println("d == h?"+(d == h));
        System.out.println("d == j?"+(d == j));

    }
    String getStr(){
        return "b";
    }
}