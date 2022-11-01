package Drones;

import CommonUtils.BetterStack;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Manages everything regarding the requesting of items in our game.
 * Will be integrated with the other drone classes.
 *
 * You may only use java.util.List, java.util.ArrayList, java.io.* and java.util.Scanner
 * from the standard library.  Any other containers used must be ones you created.
 */
public class ItemRequestManager implements ItemRequestManagerInterface {
    /**
     * Get the retrieval times as per the specifications
     *
     * @param filename file to read input from
     * @return the list of times requests were filled and index of the original request, per the specifications
     */
    @Override
    public ArrayList<ItemRetrievalTimes> getRetrievalTimes(String filename) {

        ArrayList<ItemRetrievalTimes> retrievalTimes = new ArrayList<>();
        try {
            // as all of the inputs are on the same line, it is actually more efficient to use scanner's nextInt since
            // with BufferedReader you would have to read in the entire line (possibly 10m integers long) at once
            Scanner scan = new Scanner(new FileReader(filename));

            BetterStack<PendingRequest> pendingRequests = new BetterStack<>();

            int numRequests = scan.nextInt();
            int t = scan.nextInt();

            //Drone variables
            long currentTime = 0;
            long nextRequestTime;
            int currentIndex = 0;
            int timeLeft;
            int distanceFromPlayer = 0;

            currentTime = scan.nextInt(); //Set current time to time of first request:

            while(scan.hasNextInt()) {
                nextRequestTime = scan.nextInt(); //Find time of next request:
                timeLeft = (int) (nextRequestTime - currentTime); //Calculate time before next request:

                //Check where drone is when next request will happen
                if (timeLeft <= t - distanceFromPlayer) { //Hasn't grabbed item

                    //Put the request on the stack, distance from player = t, increase index
                    pendingRequests.push(new PendingRequest(currentIndex++, t));

                    //Update drone:
                    currentTime = nextRequestTime; //We are now at the next request
                    distanceFromPlayer += timeLeft; //Update position (will always be less than t)

                }
                else if (timeLeft < ((2 * t) - distanceFromPlayer)) { //Grabbed item (must drop)

                    //Put request on stack
                    pendingRequests.push(new PendingRequest(currentIndex++, (2 * t) - timeLeft - distanceFromPlayer));

                    //Update drone:
                    distanceFromPlayer = (2 * t) - timeLeft - distanceFromPlayer; //Update position
                    currentTime = nextRequestTime; //At next request
                }
                else { //Request can be completed

                    //Complete request:
                    timeLeft -= 2 * t - distanceFromPlayer;
                    currentTime += 2 * t - distanceFromPlayer; //Update time it took to complete request:
                    distanceFromPlayer = 0; //Drone is now at the player

                    //Save retrieval to list
                    retrievalTimes.add(new ItemRetrievalTimes(currentIndex++, currentTime));

                    //If able, complete pending requests on stack:
                    while (!pendingRequests.isEmpty() && timeLeft > 0) {
                        //Pop a request off the stack
                        PendingRequest temp = pendingRequests.pop();

                        //Check to see if given request can be completed
                        if (timeLeft < temp.distanceFromPlayer) { //Item can't even be grabbed
                            //Update drone position and time
                            distanceFromPlayer = timeLeft;
                            timeLeft = 0;
                            currentTime = nextRequestTime;

                            //Put the pending request back on top of the stack and proceed
                            pendingRequests.push(temp);
                        }
                        else if (timeLeft < 2 * temp.distanceFromPlayer) { //Item was grabbed and dropped again
                            //Update drone position and time
                            distanceFromPlayer = 2 * temp.distanceFromPlayer - timeLeft;
                            timeLeft = 0;
                            currentTime = nextRequestTime; //Go to next request

                            //Update the position of the pending request and put it back on the stack
                            temp.distanceFromPlayer = distanceFromPlayer;
                            pendingRequests.push(temp);

                        }
                        else if (timeLeft >= 2 * temp.distanceFromPlayer) { //Can be completed

                            //Complete request and add to list:
                            currentTime += 2 * temp.distanceFromPlayer; //Update current time
                            timeLeft -= 2 * temp.distanceFromPlayer; //Update time left
                            retrievalTimes.add(new ItemRetrievalTimes(temp.reqNum, currentTime));
                        }
                    }
                    if (pendingRequests.isEmpty()) {
                        currentTime = nextRequestTime; //If all requests are complete, skip to next request time
                    }
                }
            }
            //Complete current request:
            currentTime += 2 * t - distanceFromPlayer; //Update time it took to complete request:
            distanceFromPlayer = 0; //Drone is now at the player

            retrievalTimes.add(new ItemRetrievalTimes(currentIndex++, currentTime));

            //Complete the rest of the requests:
            while (!pendingRequests.isEmpty()) {
                PendingRequest temp = pendingRequests.pop();

                currentTime += temp.distanceFromPlayer * 2;
                retrievalTimes.add(new ItemRetrievalTimes(temp.reqNum, currentTime));
            }

        } catch (IOException e) {
            //This should never happen... uh oh o.o
            System.err.println("ATTENTION TAs: Couldn't find test file: \"" + filename + "\":: " + e.getMessage());
            System.exit(1);
        }

        return retrievalTimes;
    }

    class PendingRequest {
        int reqNum; //Index of request (0 based)
        int distanceFromPlayer; //Distance of item from player

        public PendingRequest(int reqNum, int distanceFromPlayer) {
            this.reqNum = reqNum;
            this.distanceFromPlayer = distanceFromPlayer;
        }
    }
}
