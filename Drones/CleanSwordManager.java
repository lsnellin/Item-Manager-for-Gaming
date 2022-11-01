package Drones;

import CommonUtils.BetterQueue;

import java.io.*;
import java.util.ArrayList;

/**
 * Manages everything regarding the cleaning of swords in our game.
 * Will be integrated with the other drone classes.
 *
 * You may only use java.util.List, java.util.ArrayList, and java.io.* from
 * the standard library.  Any other containers used must be ones you created.
 */
public class CleanSwordManager implements CleanSwordManagerInterface {
    /**
     * Gets the cleaning times per the specifications.
     *
     * @param filename file to read input from
     * @return the list of times requests were filled and times it took to fill them, as per the specifications
     */
    @Override
    public ArrayList<CleanSwordTimes> getCleaningTimes(String filename) {

        ArrayList<CleanSwordTimes> cleaningTimes = new ArrayList<CleanSwordTimes>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(filename));

            String[] nmt = bf.readLine().split(" ");

            long n = Integer.parseInt(nmt[0]);
            long m = Integer.parseInt(nmt[1]);
            long t = Integer.parseInt(nmt[2]);

            BetterQueue<Long> timeReq = new BetterQueue<Long>();
            BetterQueue<Long> timeNeed = new BetterQueue<Long>();

            for (int i = 0; i < m; i++) {
                if (i < n) {
                    timeNeed.add(Long.parseLong(bf.readLine()));
                }
                else {
                    timeNeed.add(t);
                }
            }

            for (int i = 0; i < m; i++) {
                timeReq.add(Long.parseLong(bf.readLine()));
            }

            long totalTime = 0;

            for (int i = 0; i < m; i++) {
                //Variables to store data from queue
                long requested;
                long cleanliness;

                //Variables to store output
                long timeFilled;
                long timeTaken;

                requested = timeReq.remove();
                cleanliness = timeNeed.remove();

                totalTime += cleanliness;

                //If request happens when there are no clean swords
                if (totalTime > requested) {
                    timeFilled = totalTime;
                    timeTaken = totalTime - requested;

                    cleaningTimes.add(new CleanSwordTimes(timeFilled, timeTaken));
                }
                else { //Request happens after sword(s) are clean

                    //Fill request on same time step
                    timeFilled = requested;
                    timeTaken = 0;
                    cleaningTimes.add(new CleanSwordTimes(timeFilled, timeTaken));

                    long swordNum = 1;
                    //Continue cleaning swords in queue when there is time before the next request
                    //1.) Check time of next request
                    //2.) Check time needed for next sword in queue
                    //3.) If there is enough time, fill request immediately and repeat checking process
                    while ((i < m - 1) && swordNum < n && timeReq.peek() >= (totalTime + timeNeed.peek())) {
                        i++;
                        swordNum++;
                        totalTime += timeNeed.remove();
                        timeFilled = timeReq.remove();
                        timeTaken = 0;

                        cleaningTimes.add(new CleanSwordTimes(timeFilled, timeTaken));
                    }

                    //If all swords in queue are clean, then the drone waits (set current time to next request)
                    //Also ensures that there is another request
                    if (swordNum >= n && (i < (m - 1) )) {
                        totalTime = timeReq.peek();
                    }

                }
            }

        } catch (IOException e) {
            //This should never happen... uh oh o.o
            System.err.println("ATTENTION TAs: Couldn't find test file: \"" + filename + "\":: " + e.getMessage());
            System.exit(1);
        }
        return cleaningTimes;
    }
}
