package de.diemex.inventorygui.inventory.service;


import de.diemex.inventorygui.inventory.views.Button;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Diemex
 */
public class LayoutParams
{
    private final Set<LayoutGravity> gravities = new HashSet<LayoutGravity>();

    private int mFixedColumn = -1, mFixedRow = -1;


    public boolean isEmpty()
    {
        return gravities.isEmpty() && mFixedRow < 0 && mFixedColumn < 0;
    }


    public void addGravity(LayoutGravity gravity)
    {
        gravities.add(gravity);
    }


    public LayoutGravity[] getGravities()
    {
        return gravities.toArray(new LayoutGravity[gravities.size()]);
    }


    /**
     * When you want to have a button in a specific column
     *
     * @param column
     *         column or -1 to clear the column
     */
    public void setFixedColumn(int column)
    {
        mFixedColumn = column;
    }


    /**
     * Is a fixed column set?
     *
     * @return if a column has been set
     */
    public boolean hasFixedColumn()
    {
        return mFixedColumn > 0;
    }


    /**
     * Get the row that has been set
     *
     * @return fixed row
     */
    public int getFixedColumn()
    {
        return mFixedColumn;
    }


    /**
     * When you want to have a button in a specific row
     *
     * @param row
     *         row or -1 to clear the row
     */
    public void setFixedRow(int row)
    {
        mFixedRow = row;
    }


    /**
     * Is a fixed column set?
     *
     * @return if a column has been set
     */
    public boolean hasFixedRow()
    {
        return mFixedRow > 0;
    }


    /**
     * Get the row that has been set
     *
     * @return fixed row
     */
    public int getFixedRow()
    {
        return mFixedRow;
    }


    /**
     * Apply the LayoutParams to the View
     *
     * @param contents contents (current layout) of the View
     * @param lines how many lines the inventory has
     * @return
     */
    public static Map<Integer, Button> applyLayoutParams(Map<Integer, Button> contents, int lines)
    {
        Map<Integer, Button> paramButtons = new HashMap<Integer, Button>();

        //Only Buttons which have parameters
        for (Map.Entry<Integer, Button> entry : contents.entrySet())
        {
            int position = entry.getKey();
            Button btn = entry.getValue();
            if (btn.hasLayoutParams())
                paramButtons.put(position, btn);
        }

        //Apply the parameters
        for (Map.Entry<Integer, Button> entry : paramButtons.entrySet())
        {
            int position = entry.getKey();
            LayoutParams params = entry.getValue().getLayoutParams();

            int column = params.hasFixedColumn() ? params.getFixedColumn() : position % 9;
            int row = params.hasFixedRow() ? params.getFixedRow() : (int) Math.floor(position/9.0);


            //Force Fixed Columns
            if (params.hasFixedColumn())
                if (!contents.containsKey(row * 9 + column)) //same line but forced column
                {
                    moveButton(contents, position, row * 9 + column);
                    position = row * 9 + column;
                }
                else //Let gravity take over
                    params.addGravity(LayoutGravity.BOTTOM);

            //Force Fixed Rows
            if (params.hasFixedRow())
                if (!contents.containsKey(row * 9 + column)) //same column but forced row
                {
                    moveButton(contents, position, row * 9 + column);
                    position = row * 9 + column;
                }
                else //Let gravity take over
                    params.addGravity(LayoutGravity.RIGHT);


            //Apply gravity
            for (LayoutGravity layoutGravity : params.getGravities())
            {
                //Look for a "free" spot starting from the edge
                switch (layoutGravity)
                {
                    case BOTTOM:
                        //if (row < lines) //Only if not at the bottom already
                        for (int i = (lines - 1) * 9 + column; i > column; i -= 9)
                        {
                            if (!contents.containsKey(i))
                            {
                                contents = moveButton(contents, position, i);
                                break;
                            }
                        }
                        break;
                    case TOP:
                        //if (row > 1)
                        for (int i = column; i < position; i += 9)
                        {
                            if (!contents.containsKey(i))
                            {
                                contents = moveButton(contents, position, i);
                                break;
                            }
                        }
                        break;
                    case RIGHT:
                        //if (column < 9)
                        for (int i = 9 * (row + 1) - 1; i > position; i--)
                        {
                            if (!contents.containsKey(i))
                            {
                                contents = moveButton(contents, position, i);
                                break;
                            }
                        }
                        break;
                    case LEFT:
                        for (int i = 0; i < column; i++)
                        {
                            if (!contents.containsKey(i))
                            {
                                contents = moveButton(contents, position, i);
                                break;
                            }
                            break;
                        }
                        break;
                    default:
                        throw new UnsupportedOperationException("Unknown gravity " + layoutGravity.name());
                }
            }
        }
        return contents;
    }


    /**
     * Move a button to a new position while keeping the Button
     *
     * @param oldPos
     *         old position of the button
     * @param newPos
     *         new position to move to
     */
    private static Map<Integer, Button> moveButton(Map<Integer, Button> contents, int oldPos, int newPos)
    {
        //TODO Validation
        //Not sure how efficient pass by value is with Maps. Might want to put the Map into a member variable.
        if (contents.containsKey(oldPos))
        {
            //Move to new position
            Button btn = contents.get(oldPos);
            contents.remove(oldPos);
            contents.put(newPos, btn);
        }
        return contents;
    }
}
