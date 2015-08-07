/*$6*/


package creatorjames.Basyk.StackObjects;

abstract public class   StackObject
{
    /***
	 * 
	 * @param command
	 * @return if the command was ran correctly or not
	 */
    public abstract boolean     Execute(Stacker stack, String command);

    /***
	 * Makes it easy for variable duplication and such. 
	 * @return a copy.
	 */
    public abstract StackObject Clone();
    public abstract String      Serialize();
}
