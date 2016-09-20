/* Poker Tools by Jeff Rohlman
 * Version: 1.0.0
 * github.com/jeffrohlman
 */


import java.util.*;
import java.lang.*;

/*Card Class to handle all cards within the program
*   Holds a rank (2-10 + J, Q, K, A)
*   And also holds a suit (Clubs, Diamonds, Hearts, Spades)
        2-10 = 2-10
        J = 11
        Q = 12
        K = 13
        A = 14

        Clubs = 1
        Diamonds = 2
        Hearts = 3
        Spades = 4
    The Card Class also implements the Comparable interface to utilize the compareTo method
    The compareTo method is based on the rank of the Card object */
class card implements Comparable<card>{
    int rank;
    int suit;

    public int compareTo(card card2) {
        if (this.rank < card2.rank)
            return 1;
        else if (this.rank > card2.rank)
            return -1;
        else
            return 0;
    }
}

/*The main class*/
public class Main {

    /*Global variables to hold 2 hand cards and 5 common cards*/
    private static card[] handC = new card[2];
    private static card[] commonC = new card[5];

    /*Global variable to hold the best hand possible out of the 7 available cards*/
    private static card[] bestArr = new card[5];

    /*Global scanner for command line input*/
    private static Scanner s = new Scanner(System.in);


    /*main method to handle the other methods*/
    public static void main(String[] args) {
        System.out.println("Poker Tools v1.0.0");
        /*calls the controller method to start the program*/
        controller();
    }

    /*Main controller for the entire program
    * Calls function and does minimal math within the actual function
    * Handles all print statements to keep the I/O mostly concise*/
    private static void controller(){
        initCards();
        System.out.println("Your cards in hand: " + cardValue(handC[0]) + ", " + cardValue(handC[1]));
        System.out.println("The community cards: " + cardValue(commonC[0]) + ", " + cardValue(commonC[1]) + ", " + cardValue(commonC[2]) + ", " + cardValue(commonC[3]) + ", " + cardValue(commonC[4]) + "\n");
        boolean hasStraight = checkStraight();
        boolean hasFlush = checkFlush();
        sortHigh();
        int p = pairs();
        if(p == 5){
            System.out.println("Your best hand is a Four of a Kind of " + cardValue(bestArr[0]).substring(0, cardValue(bestArr[0]).indexOf('o') - 1) + "'s with a high card of " + cardValue(bestArr[4]).substring(0, cardValue(bestArr[4]).indexOf('o') - 1) + ":");
        }
        else if(p == 4) {
            if (hasFlush && hasStraight) {
                if (checkSF()) {
                    if (bestArr[0].rank == 14)
                        System.out.println("Your best hand is a Royal Flush of " + cardValue(bestArr[0]).substring(cardValue(bestArr[0]).indexOf('o') + 3) + "'s:");
                    else
                        System.out.println("Your best hand is a Straight Flush of " + cardValue(bestArr[0]).substring(cardValue(bestArr[0]).indexOf('o') + 3) + "'s:");
                }
                else
                    System.out.println("Your best hand is a Full House with three of a kind of " + cardValue(bestArr[0]).substring(0, cardValue(bestArr[0]).indexOf('o') - 1) + "'s and a pair of " + cardValue(bestArr[4]).substring(0, cardValue(bestArr[4]).indexOf('o') - 1) + "'s:");
            }
            else
                System.out.println("Your best hand is a Full House with three of a kind of " + cardValue(bestArr[0]).substring(0, cardValue(bestArr[0]).indexOf('o') - 1) + "'s and a pair of " + cardValue(bestArr[4]).substring(0, cardValue(bestArr[4]).indexOf('o') - 1) + "'s:");
        }

        else if(p < 4 && (hasFlush || hasStraight)) {
            if (checkSF()) {
                if (bestArr[0].rank == 14)
                    System.out.println("Your best hand is a Royal Flush of " + cardValue(bestArr[0]).substring(cardValue(bestArr[0]).indexOf('o') + 3) + "'s:");
                else
                    System.out.println("Your best hand is a Straight Flush of " + cardValue(bestArr[0]).substring(cardValue(bestArr[0]).indexOf('o') + 3) + "'s:");
            } else if (hasFlush) {
                checkFlush();
                System.out.println("Your best hand is a Flush of " + cardValue(bestArr[0]).substring(cardValue(bestArr[0]).indexOf('o') + 3) + "'s:");
            } else {
                checkStraight();
                System.out.println("Your best hand is a Straight of " + cardValue(bestArr[0]).substring(0, cardValue(bestArr[0]).indexOf('o') - 1) + "'s:");
            }
        }
        else {
            if(p == 3)
                System.out.println("Your best hand is a Three of a Kind of " + cardValue(bestArr[0]).substring(0, cardValue(bestArr[0]).indexOf('o') - 1) + "'s with high cards " + cardValue(bestArr[3]).substring(0, cardValue(bestArr[3]).indexOf('o') - 1) + " and " + cardValue(bestArr[4]).substring(0, cardValue(bestArr[4]).indexOf('o') - 1) + ":");
            else if(p == 2)
                System.out.println("Your best hand is a Two Pair of " + cardValue(bestArr[0]).substring(0, cardValue(bestArr[0]).indexOf('o') - 1) + "'s and " + cardValue(bestArr[2]).substring(0, cardValue(bestArr[2]).indexOf('o') - 1) + "'s with high card of " + cardValue(bestArr[4]).substring(0, cardValue(bestArr[4]).indexOf('o') - 1) + ":");
            else if(p == 1)
                System.out.println("Your best hand is a Pair of " + cardValue(bestArr[0]).substring(0, cardValue(bestArr[0]).indexOf('o') - 1) + "'s with high cards " + cardValue(bestArr[2]).substring(0, cardValue(bestArr[2]).indexOf('o') - 1) + ", " + cardValue(bestArr[3]).substring(0, cardValue(bestArr[3]).indexOf('o') - 1) + " and " +cardValue(bestArr[4]).substring(0, cardValue(bestArr[4]).indexOf('o') - 1) + ":");
            else
                System.out.println("You best hand is a high card of " + cardValue(bestArr[0]).substring(0, cardValue(bestArr[0]).indexOf('o') - 1) + ":");
        }

        System.out.println(cardValue(bestArr[0]) + ", " + cardValue(bestArr[1]) + ", " + cardValue(bestArr[2]) + ", " + cardValue(bestArr[3]) + ", " + cardValue(bestArr[4]));
    }

    /*Initializing and declaring all cards from user input
    * Input firsts asks for a rank (2-14) with 14 being ace
    * Then input asks for suit (1-4)
    * If user inputs any value outside of range allowed the question is asked again until proper input*/
    private static void initCards(){
        /*Declaring the 2 hand cards and 5 common cards*/
        handC[0] = new card(); handC[0].rank = 0; handC[0].suit = 0;
        handC[1] = new card(); handC[1].rank = 0; handC[1].suit = 0;
        for(int i = 0; i < 5; i++){
            commonC[i] = new card();
            commonC[i].rank = 0;
            commonC[i].suit = 0;
        }

        /*Initializing all 7 cards*/
        while(handC[0].rank < 2 || handC[0].rank > 14) {
            System.out.print("Enter the first card rank in your hand (2-14): ");
            handC[0].rank = s.nextInt();
        }
        while(handC[0].suit < 1 || handC[0].suit > 4) {
            System.out.print("Enter the first card suit in your hand (1-4): ");
            handC[0].suit = s.nextInt();
        }
        while(handC[1].rank < 2 || handC[1].rank > 14) {
            System.out.print("Enter the second card rank in your hand (2-14): ");
            handC[1].rank = s.nextInt();
        }
        while(handC[1].suit < 1 || handC[1].suit > 4) {
            System.out.print("Enter the second card suit in your hand (1-4): ");
            handC[1].suit = s.nextInt();
        }

        System.out.println();
        while(commonC[0].rank < 2 || commonC[0].rank > 14) {
            System.out.print("Enter the first card rank in the dealer's hand (2-14): ");
            commonC[0].rank = s.nextInt();
        }
        while(commonC[0].suit < 1 || commonC[0].suit > 4) {
            System.out.print("Enter the first card suit in the dealer's hand (1-4): ");
            commonC[0].suit = s.nextInt();
        }
        while(commonC[1].rank < 2 || commonC[1].rank > 14) {
            System.out.print("Enter the second card rank in the dealer's hand (2-14): ");
            commonC[1].rank = s.nextInt();
        }
        while(commonC[1].suit < 1 || commonC[1].suit > 4) {
            System.out.print("Enter the second card suit in the dealer's hand (1-4): ");
            commonC[1].suit = s.nextInt();
        }
        while(commonC[2].rank < 2 || commonC[2].rank > 14) {
            System.out.print("Enter the third card rank in the dealer's hand (2-14): ");
            commonC[2].rank = s.nextInt();
        }
        while(commonC[2].suit < 1 || commonC[2].suit > 4) {
            System.out.print("Enter the third card suit in the dealer's hand (1-4): ");
            commonC[2].suit = s.nextInt();
        }
        while(commonC[3].rank < 2 || commonC[3].rank> 14) {
            System.out.print("Enter the fourth card rank in the dealer's hand (2-14): ");
            commonC[3].rank = s.nextInt();
        }
        while(commonC[3].suit < 1 || commonC[3].suit > 4) {
            System.out.print("Enter the fourth card suit in the dealer's hand (1-4): ");
            commonC[3].suit = s.nextInt();
        }
        while(commonC[4].rank < 2 || commonC[4].rank > 14) {
            System.out.print("Enter the fifth card rank in the dealer's hand (2-14): ");
            commonC[4].rank = s.nextInt();
        }
        while(commonC[4].suit < 1 || commonC[4].suit > 4) {
            System.out.print("Enter the fifth card suit in the dealer's hand (1-4): ");
            commonC[4].suit = s.nextInt();
        }
    }

    /*Method that takes a card object in and returns a string of the cards literal value*/
    private static String cardValue(card c)
    {
        switch(c.suit){
            case 1:
                if(c.rank < 11)
                    return (c.rank + " of Clubs");
                else if(c.rank == 11)
                    return ("Jack of Clubs");
                else if(c.rank == 12)
                    return ("Queen of Clubs");
                else if(c.rank == 13)
                    return ("King of Clubs");
                else
                    return ("Ace of Clubs");
            case 2:
                if(c.rank < 11)
                    return (c.rank + " of Diamonds");
                else if(c.rank == 11)
                    return ("Jack of Diamonds");
                else if(c.rank == 12)
                    return ("Queen of Diamonds");
                else if(c.rank == 13)
                    return ("King of Diamonds");
                else
                    return ("Ace of Diamonds");
            case 3:
                if(c.rank < 11)
                    return (c.rank + " of Hearts");
                else if(c.rank == 11)
                    return ("Jack of Hearts");
                else if(c.rank == 12)
                    return ("Queen of Hearts");
                else if(c.rank == 13)
                    return ("King of Hearts");
                else
                    return ("Ace of Hearts");
            case 4:
                if(c.rank < 11)
                    return (c.rank + " of Spades");
                else if(c.rank == 11)
                    return ("Jack of Spades");
                else if(c.rank == 12)
                    return ("Queen of Spades");
                else if(c.rank == 13)
                    return ("King of Spades");
                else
                    return ("Ace of Spades");
             default:
                 return "error";
        }
    }

    /*Method to determine if the player has any pairs
    * Returns zero if the player has no pairs
    * Returns:
    * 1 if the player has one pair
    * 2 if the player has two pairs
    * 3 if the player has a three of a kind
    * 4 if the player has a full house (three of a kind and a pair)
    * 5 if the player has a four of a kind
    *
    * Along with returning an integer value the method sets bestArr to reflect return type if greater than zero */
    private static int pairs(){
        card[] cArray = {handC[0], handC[1], commonC[0], commonC[1], commonC[2], commonC[3], commonC[4]};
        Arrays.sort(cArray);
        int pos1 = -1;
        int l1 = 0;
        int pos2 = -1;
        int l2 = 0;
        int pos3 = -1;
        int l3 = 0;
        for(int i = 0; i < 6; i++){
            if(cArray[i].rank == cArray[i+1].rank){
                if(i < 5 && cArray[i].rank == cArray[i+2].rank){
                    if(i < 4 && cArray[i].rank == cArray[i+3].rank){
                        pos1 = i;
                        l1 = 4;
                        i = 6;
                    }
                    else{
                        if(pos1 >= 0 && pos2 >= 0){
                            pos3 = i;
                            l3 = 3;
                        }
                        else if(pos1 >= 0) {
                            pos2 = i;
                            l2 = 3;
                        }
                        else{
                            pos1 = i;
                            l1 = 3;
                        }
                        i+=2;
                    }
                }
                else{
                    if(pos1 >= 0 && pos2 >= 0) {
                        pos3 = i;
                        l3 = 2;
                    }
                    else if(pos1 >= 0){
                        pos2 = i;
                        l2 = 2;
                    }
                    else{
                        pos1 = i;
                        l1 = 2;
                    }
                    i++;
                }
            }
        }
        if(l1 == 0)
            return 0;
        else if(l1 == 4){
            for(int i = 0; i < 4; i++)
                bestArr[i] = cArray[pos1 + i];
            if(pos1 == 0)
                bestArr[4] = cArray[4];
            else
                bestArr[4] = cArray[0];
            return 5;
        }

        else if(l1 == 3 || l2 == 3 || l3 == 3){
            if(l1 == 3){
                bestArr[0] = cArray[pos1];
                bestArr[1] = cArray[pos1 + 1];
                bestArr[2] = cArray[pos1 + 2];
                if(l2 > 0){
                    bestArr[3] = cArray[pos2];
                    bestArr[4] = cArray[pos2 + 1];
                    return 4;
                }
                else{
                    if(pos1 == 0){
                        bestArr[3] = cArray[3];
                        bestArr[4] = cArray[4];
                    }
                    else if(pos1 == 1){
                        bestArr[3] = cArray[0];
                        bestArr[4] = cArray[4];
                    }
                    else{
                        bestArr[3] = cArray[0];
                        bestArr[4] = cArray[1];
                    }
                    return 3;
                }
            }
            else if(l2 == 3){
                bestArr[0] = cArray[pos2];
                bestArr[1] = cArray[pos2 + 1];
                bestArr[2] = cArray[pos2 + 2];

                }
            else{
                bestArr[0] = cArray[pos3];
                bestArr[1] = cArray[pos3 + 1];
                bestArr[2] = cArray[pos3 + 2];
            }
            bestArr[3] = cArray[pos1];
            bestArr[4] = cArray[pos1 + 1];
            return 4;
        }
        else if(l2 == 2){
            bestArr[0] = cArray[pos1];
            bestArr[1] = cArray[pos1 + 1];
            bestArr[2] = cArray[pos2];
            bestArr[3] = cArray[pos2 + 1];
            if(pos1 == 0){
                if(pos2 == 2)
                    bestArr[4] = cArray[4];
                else
                    bestArr[4] = cArray[2];
            }
            else
                bestArr[4] = cArray[0];
            return 2;
        }
        else{
            bestArr[0] = cArray[pos1];
            bestArr[1] = cArray[pos1 + 1];
            if(pos1 == 0){
                bestArr[2] = cArray[2];
                bestArr[3] = cArray[3];
                bestArr[4] = cArray[4];
            }
            else if(pos1 == 1){
                bestArr[2] = cArray[0];
                bestArr[3] = cArray[3];
                bestArr[4] = cArray[4];
            }
            else if(pos1 == 2){
                bestArr[2] = cArray[0];
                bestArr[3] = cArray[1];
                bestArr[4] = cArray[4];
            }
            else{
                bestArr[2] = cArray[0];
                bestArr[3] = cArray[1];
                bestArr[4] = cArray[2];
            }
            return 1;
        }
    }

    /*Method that sorts 7 available cards by rank and pushes high cards to bestArr[]*/
    private static void sortHigh(){
        card[] cArray = {handC[0], handC[1], commonC[0], commonC[1], commonC[2], commonC[3], commonC[4]};
        Arrays.sort(cArray);
        for(int i = 0; i < 5; i++)
            bestArr[i] = cArray[i];
    }

    /*Method that checks for a straight in the seven available cards
    * Also pushes best cards to bestArr[] if there is a straight*/
    private static boolean checkStraight(){
        card[] cArray = {handC[0], handC[1], commonC[0], commonC[1], commonC[2], commonC[3], commonC[4]};
        Arrays.sort(cArray);
        int current = cArray[0].rank;
        int pos = 1;
        for (int i = 0; i < cArray.length; i++) {
            if (current != cArray[i].rank) {
                current = cArray[i].rank;
                cArray[pos++] = cArray[i];
            }
        }

        for(int i = 0; i < pos - 4; i++) {
            if (cArray[i].rank == cArray[i + 4].rank + 4 && cArray[i].rank == cArray[i + 3].rank + 3 && cArray[i].rank == cArray[i + 2].rank + 2 && cArray[i].rank == cArray[i + 1].rank + 1){
                for(int j = i; j < i + 5; j++)
                    bestArr[j] = cArray[j];
                return true;
            }
        }
        return false;
    }

    /*Method that returns true if there is a flush based on the global handC and commonC array of cards
    *   If there is a flush then bestArr[] is updated to match the player's best possible hand*/
    private static boolean checkFlush(){
        card[] cArray = {handC[0], handC[1], commonC[0], commonC[1], commonC[2], commonC[3], commonC[4]};
        int clubs = 0;
        int diamonds = 0;
        int hearts = 0;
        int spades = 0;
        for(int i = 0; i < 7; i++) {
            if(cArray[i].suit == 1)
                clubs++;
            else if(cArray[i].suit == 2)
                diamonds++;
            else if(cArray[i].suit == 3)
                hearts++;
            else
                spades++;
        }
        if(clubs > 4){
            card[] rArray = new card[clubs];
            int pos = 0;
            for(int i = 0; i < 7; i++)
                if(cArray[i].suit == 1)
                    rArray[pos++] = cArray[i];
            Arrays.sort(rArray);
            for(int i = 0; i < 5; i++)
                bestArr[i]  = rArray[i];
            return true;
        }

        else if(diamonds > 4){
            card[] rArray = new card[diamonds];
            int pos = 0;
            for(int i = 0; i < 7; i++)
                if(cArray[i].suit == 2)
                    rArray[pos++] = cArray[i];
            Arrays.sort(rArray);
            for(int i = 0; i < 5; i++)
                bestArr[i]  = rArray[i];
            return true;
        }

        else if(hearts > 4){
            card[] rArray = new card[hearts];
            int pos = 0;
            for(int i = 0; i < 7; i++)
                if(cArray[i].suit == 3)
                    rArray[pos++] = cArray[i];
            Arrays.sort(rArray);
            for(int i = 0; i < 5; i++)
                bestArr[i]  = rArray[i];
            return true;
        }

        else if(spades > 4){
            card[] rArray = new card[spades];
            int pos = 0;
            for(int i = 0; i < 7; i++)
                if(cArray[i].suit == 4)
                    rArray[pos++] = cArray[i];
            Arrays.sort(rArray);
            for(int i = 0; i < 5; i++)
                bestArr[i]  = rArray[i];
            return true;
        }

        else
            return false;
    }

    /*Method to check for a straight flush
    * This method is only called if checkStraight() and checkFlush() both return true
    * Some logic is based on the fact listed above
    * return true if there is a straight flush*/
    private static boolean checkSF(){
        card[] cArray = {handC[0], handC[1], commonC[0], commonC[1], commonC[2], commonC[3], commonC[4]};
        Arrays.sort(cArray);
        ArrayList<card> a = new ArrayList<>();
        int current = 0;
        int pos = 0;
        int pos2 = -1;
        int pos3 = -1;
        for (int i = 0; i < cArray.length; i++) {
            if (current != cArray[i].rank) {
                current = cArray[i].rank;
                cArray[pos++] = cArray[i];
            }
            else if(pos2 == -1){
                a.add(cArray[i]);
                pos2 = pos - 1;
            }
            else{
                a.add(cArray[i]);
                if(a.get(0).rank == a.get(1).rank)
                    pos3 = pos - 2;
                else
                    pos3 = pos - 1;
            }
        }
        if(a.size() == 0){
            return checkSFs(cArray, pos);
        }
        else if(a.size() == 1){
            if(checkSFs(cArray, pos))
                return true;
            cArray[pos2] = a.get(0);
            return checkSFs(cArray, pos);
        }
        else{
            if(checkSFs(cArray, pos))
                return true;
            a.add(cArray[pos2]);
            cArray[pos2] = a.get(0);
            if(checkSFs(cArray,pos))
                return true;
            cArray[pos2] = a.get(2);
            cArray[pos3] = a.get(1);
            if(checkSFs(cArray, pos))
                return true;
            cArray[pos2] = a.get(0);
            return checkSFs(cArray, pos);
        }

    }

    /*Supplemental method called by checkSF() to cut down on repeated code
    * return true if there is a straight flush and also pushes to bestArr[] based on return*/
    private static boolean checkSFs(card[] cArray, int pos){
        for(int i = 0; i < pos - 4; i++) {
            if (cArray[i].rank == cArray[i + 4].rank + 4 && cArray[i].rank == cArray[i + 3].rank + 3 && cArray[i].rank == cArray[i + 2].rank + 2 && cArray[i].rank == cArray[i + 1].rank + 1) {
                if (cArray[i].suit == cArray[i + 4].suit && cArray[i].suit == cArray[i + 3].suit && cArray[i].suit == cArray[i + 2].suit && cArray[i].suit == cArray[i + 1].suit) {
                    for (int j = i; j < i + 5; j++)
                        bestArr[j] = cArray[j];
                    return true;
                }
            }
        }
        return false;
    }
}