/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	private int numberOfPhilosophers;
	private boolean chopstickAvailable[];
	private boolean talking;
	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		numberOfPhilosophers = piNumberOfPhilosophers;
		talking = false;
		chopstickAvailable = new boolean[piNumberOfPhilosophers];
		for(int i = 0; i<chopstickAvailable.length ; i++)
		{
			chopstickAvailable[i] = true;
		}
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{
		while(!canPickup(piTID))
		{
			try {
				System.out.println("Philosopher " + piTID + " is waiting to eat");
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		chopstickAvailable[getLeftChopStick(piTID)] = false;
		chopstickAvailable[getRightChopStick(piTID)] = false;
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		chopstickAvailable[getLeftChopStick(piTID)] = true;
		chopstickAvailable[getRightChopStick(piTID)] = true;
		this.notifyAll();
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		while(talking)
		{
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		talking = true;
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		talking = false;
		notifyAll();
	}
	
	private synchronized boolean canPickup(final int piTID)
	{
		return leftChopStickAvailable(piTID) && rightChopStickAvailable(piTID);
	}
	
	private synchronized boolean leftChopStickAvailable(final int piTID)
	{
		int leftChopstick = piTID != 0 ? piTID - 1 : numberOfPhilosophers-1;
		return chopstickAvailable[leftChopstick];
		
	}
	
	private synchronized boolean rightChopStickAvailable(final int piTID)
	{
		int rightChopstick = piTID != numberOfPhilosophers ? piTID : 0;
		return chopstickAvailable[rightChopstick];
	}
	
	private synchronized int getLeftChopStick(final int piTID)
	{
		return piTID != 0 ? piTID - 1 : numberOfPhilosophers-1;		
	}
	
	private synchronized int getRightChopStick(final int piTID)
	{
		return piTID != numberOfPhilosophers ? piTID : 0;
	}
}

// EOF
