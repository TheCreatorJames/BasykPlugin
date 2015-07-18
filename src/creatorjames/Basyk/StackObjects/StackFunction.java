package creatorjames.Basyk.StackObjects;


public class StackFunction extends StackObject
{
	private String function;
	
	//Constructors//
	public StackFunction()
	{
		this("");
	}
	
	public StackFunction(StackFunction sf)
	{
		this(sf.GetFunction());
	}
	
	public StackFunction(String str)
	{
		this.function = str;
	}
	
	/**
	 * Sets the string function.
	 * @param x
	 */
	public void SetFunction(String x)
	{
		this.function = x;
	}
	
	/**
	 * Gets the string function
	 * @return
	 */
	public String GetFunction()
	{
		return function;
	}
	
	@Override
	public boolean Execute(Stacker stack, String command) {
		// TODO Auto-generated method stub
		
		if(command.equals("string"))
		{
			stack.Pop();
			stack.Push(new StackString(GetFunction().trim()));
			
			return true;
		}
		
		
		return false;
	}

	@Override
	public StackObject Clone() {
		// TODO Auto-generated method stub
		return this;
	}

	
	
	public String toString()
	{
		return GetFunction() + " [Function]";
	}

	@Override
	public String Serialize() {
		// TODO Auto-generated method stub
		return "{ " + GetFunction() + " }";
	}
	
	
	
}
