/*$6*/


package creatorjames.Basyk.StackObjects;

public class StackBoolean extends   StackObject
{
    private boolean value;

    //your constructors//
    public StackBoolean()
    {
        this(false);
    }

    public StackBoolean(StackBoolean sb)
    {
        this(sb.GetValue());
    }

    public StackBoolean(boolean value)
    {
        this.value = value;
    }

    /**
	 * gets the boolean value
	 * @return
	 */
    public boolean GetValue()
    {
        return value;
    }

    /**
	 * sets the boolean value
	 * @param value
	 */
    public void SetValue(boolean value)
    {
        this.value = value;
    }

    /**
	 * checks if the booleans are equal
	 * @param stack
	 * @param invert
	 * @return
	 */
    private boolean CheckEquals(Stacker stack, boolean invert)
    {
        if (stack.FromTop(1) instanceof StackBoolean)
        {
            stack.Pop();

            StackBoolean    x = (StackBoolean) stack.Pop();

            if (x.GetValue() == GetValue())
            {
                stack.Push(new StackBoolean(!invert));
            }
            else
            {
                stack.Push(new StackBoolean(invert));
            }

            return true;
        }

        return false;
    }

    /**
	 * checks if one of the booleans are true.
	 * @param stack
	 * @return
	 */
    private boolean Or(Stacker stack)
    {
        if (stack.FromTop(1) instanceof StackBoolean)
        {
            stack.Pop();

            StackBoolean    x = (StackBoolean) stack.Pop();

            if (x.GetValue() || GetValue())
            {
                stack.Push(new StackBoolean(true));
            }
            else
            {
                stack.Push(new StackBoolean(false));
            }

            return true;
        }

        return false;
    }

    /**
	 * Checks if both booleans are true.
	 * @param stack
	 * @return
	 */
    private boolean And(Stacker stack)
    {
        if (stack.FromTop(1) instanceof StackBoolean)
        {
            stack.Pop();

            StackBoolean    x = (StackBoolean) stack.Pop();

            if (x.GetValue() && GetValue())
            {
                stack.Push(new StackBoolean(true));
            }
            else
            {
                stack.Push(new StackBoolean(false));
            }

            return true;
        }

        return false;
    }

    /**
	 * This turns your boolean into a string.
	 * @param stack
	 * @return
	 */
    private boolean Stringify(Stacker stack)
    {
        stack.Pop();
        stack.Push(new StackString("" + this.GetValue()));
        return true;
    }

    /**
	 * For some odd reason, I made a boolean to number method.
	 * @param stack
	 * @return
	 */
    private boolean Numbify(Stacker stack)
    {
        stack.Pop();

        int num = 0;
        if (GetValue()) num += 1;
        stack.Push(new StackNumber(num));
        return true;
    }

    @Override public boolean Execute (Stacker stack, String command)
    {
        // TODO Auto-generated method stub

        /*
		switch(command)
		{
			case "==":
				return CheckEquals(stack, false);
			case "!=":
				return CheckEquals(stack, true);
			case "&&":
				return And(stack);
			case "||":
				return Or(stack);
			case "string":
				return Stringify(stack);
			case "number":
				return Numbify(stack);
		}*/
        if (command.equals("=="))
        {
            return CheckEquals(stack, false);
        }
        else if (command.equals("!="))
        {
            return CheckEquals(stack, true);
        }
        else if (command.equals("&&"))
        {
            return And(stack);
        }
        else if (command.equals("||"))
        {
            return Or(stack);
        }
        else if (command.equals("string"))
        {
            return Stringify(stack);
        }
        else if (command.equals("number"))
        {
            return Numbify(stack);
        }
        return false;
    }
    @Override public StackObject Clone ()
    {
        // TODO Auto-generated method stub
        return new StackBoolean(this);
    }
    public String toString()
    {
        return "" + this.GetValue() + " [Boolean]";
    }

    @Override public String Serialize ()
    {
        // TODO Auto-generated method stub
        return "" + GetValue();
    }
}
