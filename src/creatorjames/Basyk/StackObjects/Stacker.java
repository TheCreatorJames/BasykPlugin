package creatorjames.Basyk.StackObjects;
import java.util.Stack;



/***
 * 
 * @author Jesse
 *	This is used to store the objects used
 *  by the parser.
 */
public class Stacker {

	private Stack<StackObject> stack;
	
	public Stacker()
	{
		this.stack = new Stack<StackObject>();
	}
	
	public StackObject Peek()
	{
		return this.stack.peek();
	}
	
	public StackObject Pop()
	{
		return this.stack.pop();
	}
	
	public void Clear()
	{
		stack.clear();
	}
	
	public void Push(StackObject so)
	{
		if(stack.size() > 1024)
		return;
		this.stack.push(so);
	}
	
	public StackObject FromTop(int num)
	{
		return this.stack.elementAt(stack.size() - 1 - num);
	}
	
	public int GetSize()
	{
		return stack.size();
	}
}
