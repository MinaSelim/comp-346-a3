import common.BaseThread;

/**
 * Class Philosopher.
 * Outlines main subrutines of our virtual philosopher.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;

	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done eating.
	 */
	public void eat()
	{
		try
		{
			System.out.println("Philosopher " + this.iTID + ": is eating");
			DiningPhilosophers.soMonitor.pickUpPepper();
			System.out.println("Philosopher " + this.iTID + ": picked up pepper");
			sleep((long)(Math.random() * TIME_TO_WASTE));
			System.out.println("Philosopher " + this.iTID + ": is putting down pepper");
			DiningPhilosophers.soMonitor.putDownPepper();
			System.out.println("Philosopher " + this.iTID + ": is done eating");
			// ...
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done thinking.
	 */
	public void think()
	{
		try
		{
			System.out.println("Philosopher " + this.iTID + ": is thinking");
			sleep((long)(Math.random() * TIME_TO_WASTE));
			System.out.println("Philosopher " + this.iTID + ": is done thinking");
			// ...
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.think():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - yield
	 * - Say something brilliant at random
	 * - yield
	 * - The print that they are done talking.
	 */
	public void talk()
	{
		
			
			try {
				System.out.println("Philosopher " + this.iTID + ": is talking");
				saySomething();
				sleep((long)(Math.random() * TIME_TO_WASTE));
				System.out.println("Philosopher " + this.iTID + ": is done talking");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	
	}

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	public void run()
	{
		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++)
		{
			DiningPhilosophers.soMonitor.pickUp(getTID());

			eat();

			DiningPhilosophers.soMonitor.putDown(getTID());

			think();

			/*
			 * TODO:
			 * A decision is made at random whether this particular
			 * philosopher is about to say something terribly useful.
			 */
			if((int)(Math.random() * 10) > 7)
			{
			    DiningPhilosophers.soMonitor.requestTalk();
				talk();
				DiningPhilosophers.soMonitor.endTalk();
				// ...
			}
			
			if((int)(Math.random() * 10) > 7)
			{
			    DiningPhilosophers.soMonitor.requestSleep();
				PhilSleep();
				DiningPhilosophers.soMonitor.wakeUp();
				// ...
			}

			yield();
		}
	} // run()
	
	public void PhilSleep()
    {
            //...
            System.out.println("Philosopher " + this.getTID() + " has started  sleeping.");
            yield();
            try {
				sleep((long)(Math.random() * TIME_TO_WASTE));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            yield();
            System.out.println("Philosopher " + this.getTID() + " has finished  sleeping.");
            // ...
    }


	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	public void saySomething()
	{
		String[] astrPhrases =
		{
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID() + ""
		};

		System.out.println
		(
			"Philosopher " + getTID() + " says: " +
			astrPhrases[(int)(Math.random() * astrPhrases.length)]
		);
	}
}

// EOF
