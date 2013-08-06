package de.diemex.inventorygui.inventory.service;


import de.diemex.inventorygui.inventory.views.Button;
import org.bukkit.Material;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * @author Diemex
 */
public class TestLayoutParams
{
    private LayoutParams params;

    private Map<Integer, Button> contents = new HashMap<Integer, Button>();

    private final int SMALL = 2, BIG = 4;


    @Test
    public void testGravityBottom()
    {
        clear();
        params.addGravity(LayoutGravity.BOTTOM);
        contents.put(8, btnWithParams());
        validate(LayoutParams.applyLayoutParams(contents, SMALL), SMALL, 17);
    }


    @Test
    public void testGravityTop()
    {
        clear();
        params.addGravity(LayoutGravity.TOP);
        contents.put(9 + 8, btnWithParams());
        validate(LayoutParams.applyLayoutParams(contents, SMALL), SMALL, 8);
    }


    @Test
    public void testGravityRight()
    {
        clear();
        params.addGravity(LayoutGravity.RIGHT);
        contents.put(2, btnWithParams());
        validate(LayoutParams.applyLayoutParams(contents, SMALL), SMALL, 8);
    }


    @Test
    public void testGravityLeft()
    {
        clear();
        params.addGravity(LayoutGravity.LEFT);
        contents.put(7, btnWithParams());
        validate(LayoutParams.applyLayoutParams(contents, SMALL), SMALL, 0);
    }


    @Test
    public void testGravityBottomLayed()
    {
        clear();
        params.addGravity(LayoutGravity.BOTTOM);
        contents.put(9, btnWithParams());
        validate(LayoutParams.applyLayoutParams(contents, SMALL), SMALL, 9);
    }


    @Test
    public void testGravityTopLayed()
    {
        clear();
        params.addGravity(LayoutGravity.TOP);
        contents.put(1, btnWithParams());
        validate(LayoutParams.applyLayoutParams(contents, SMALL), SMALL, 1);
    }


    @Test
    public void testGravityRightLayed()
    {
        clear();
        params.addGravity(LayoutGravity.RIGHT);
        contents.put(8, btnWithParams());
        validate(LayoutParams.applyLayoutParams(contents, SMALL), SMALL, 8);
    }


    @Test
    public void testGravityLeftLayed()
    {
        clear();
        params.addGravity(LayoutGravity.LEFT);
        contents.put(9, btnWithParams());
        validate(LayoutParams.applyLayoutParams(contents, SMALL), SMALL, 9);
    }


    @Test
    public void testGravityLeftBig()
    {
        clear();
        params.addGravity(LayoutGravity.LEFT);
        contents.put(7, btnWithParams());
        validate(LayoutParams.applyLayoutParams(contents, BIG), BIG, 0);
    }


    @Test
    public void testGravityBottomBig()
    {
        clear();
        params.addGravity(LayoutGravity.BOTTOM);
        contents.put(16, btnWithParams());
        validate(LayoutParams.applyLayoutParams(contents, BIG), BIG, 9 * (BIG - 1) + 7);
    }


    @Test
    public void testGravityTopBig()
    {
        clear();
        params.addGravity(LayoutGravity.TOP);
        contents.put(33, btnWithParams());
        validate(LayoutParams.applyLayoutParams(contents, BIG), BIG, 33 % 9);
    }


    @Test
    public void testFixedColumn()
    {
        clear();
        params.setFixedColumn(8);
        params.addGravity(LayoutGravity.BOTTOM);
        contents.put(5, btnWithParams());
        validate(LayoutParams.applyLayoutParams(contents, BIG), BIG, (BIG - 1) * 9 + 8);
    }


    @Test
    public void testFixedRow()
    {
        clear();
        params.setFixedRow(3);
        params.addGravity(LayoutGravity.RIGHT);
        contents.put(5, btnWithParams());
        validate(LayoutParams.applyLayoutParams(contents, BIG), BIG, 4 * 9 - 1);
    }


    @Test
    public void testFixedColumnFullRow()
    {
        clear();
        for (int i = 0; i < 9; i++)
            contents.put(i, btn());
        params.setFixedColumn(8);
        params.addGravity(LayoutGravity.BOTTOM);
        contents.put(10, btnWithParams());

        validate(LayoutParams.applyLayoutParams(contents, 2), 0, 1, 2, 3, 4, 5, 6, 7, 8, 17);
    }


    @Test
    public void complexLayout()
    {
        clear();
        params.addGravity(LayoutGravity.BOTTOM);
        contents.put(3, btnWithParams());

        params = new LayoutParams();
        params.addGravity(LayoutGravity.RIGHT);
        contents.put(5, btnWithParams());

        validate(LayoutParams.applyLayoutParams(contents, BIG), BIG, 8, 9 * (BIG - 1) + 3);
    }


    @Test
    public void fullView()
    {
        clear();
        for (int i = 0; i < 12; i++)
            contents.put(i, btnWithParams());

        params = new LayoutParams();
        params.setFixedColumn(8);
        contents.put(13, btnWithParams());

        validate(LayoutParams.applyLayoutParams(contents, BIG), BIG, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 17);
    }


    private void validate(Map<Integer, Button> result, int lines, Integer... positions)
    {
        Set<Integer> pos = new HashSet<Integer>(Arrays.asList(positions));
        for (int i = 0; i < 9 * lines; i++)
            if (!pos.contains(i))
                assertTrue("There was a Button at index " + i + " " + result + " Expected: " + Arrays.toString(positions), !result.containsKey(i));

        for (int i : positions)
            assertTrue("Expected a button at position " + i + " but there was no button. " + result, result.containsKey(i));
    }


    private void clear()
    {
        params = new LayoutParams();
        contents.clear();
    }


    private Button btn()
    {
        return new Button("Hey", Material.APPLE);
    }


    private Button btnWithParams()
    {
        Button btn = btn();
        btn.setLayoutParams(params);
        return btn;
    }
}
