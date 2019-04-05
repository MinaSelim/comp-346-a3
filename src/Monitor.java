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

	private int sleepingPhilosophers;
	private int numberOfPhilosophers;
	private int[] philosophersDining;
	private boolean chopstickAvailable[];
	private boolean talking;
	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		numberOfPhilosophers = piNumberOfPhilosophers;
		talking = false;
		philosophersDining = new int[4];
		chopstickAvailable = new boolean[piNumberOfPhilosophers];
		for(int i = 0; i<chopstickAvailable.length ; i++)
		{
			chopstickAvailable[i] = true;
		}
		
		sleepingPhilosophers = 0;
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
		while(!canEat(piTID))
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
		notifyAll();
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		chopstickAvailable[getLeftChopStick(piTID)] = true;
		chopstickAvailable[getRightChopStick(piTID)] = true;
		philosophersDining[piTID-1]++;
		this.notifyAll();
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		while(!canTalk())
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
	
	private synchronized boolean canTalk() {
		return !talking && sleepingPhilosophers == 0;
	}

	public synchronized void requestSleep()
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
		sleepingPhilosophers++;
	}
	
	public synchronized void wakeUp()
	{
		sleepingPhilosophers--;
		notifyAll();
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
	
	private synchronized boolean canEat(final int piTID)
	{
		boolean pickup = canPickup(piTID);
		if(!pickup)
		{
			return false;
		}
		
		int leftPhilosopherId = getLeftPhilosopher(piTID);
		if(leftPhilosopherId > piTID && philosophersDining[leftPhilosopherId -1] < DiningPhilosophers.DINING_STEPS)
		{
			if(canPickup(leftPhilosopherId))
			{
				System.out.println("Philosoper " + piTID + " will wait for philosopher " + leftPhilosopherId +
						" due to his priority");
				return false;
			}
		}
		
		int rightPhilosopherId = getRightPhilosopher(piTID);
		if(rightPhilosopherId > piTID)
		{
			if(canPickup(rightPhilosopherId) && philosophersDining[rightPhilosopherId-1] < DiningPhilosophers.DINING_STEPS)
			{
				System.out.println("Philosoper " + piTID + " will wait for philosopher " + rightPhilosopherId +
						" due to his priority");
				return false;
			}
		}
		
		return true;
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
	
	private synchronized int getLeftPhilosopher(final int piTID)
	{
		return piTID != 1 ? numberOfPhilosophers : numberOfPhilosophers-1;		
	}
	
	private synchronized int getRightPhilosopher(final int piTID)
	{
		return piTID != numberOfPhilosophers ? piTID+1 : 1;
	}
}

// EOF
