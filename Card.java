
/**
 * A class that simulates a solitaire card.
 *
 * @author Angelina
 * @version 10.29.2021
 */
public class Card
{
    private int rank;
    private boolean faceUp;
    private String suit;

    /**
     * Constructor for objects of class Card
     * 
     * @param r rank
     * @param s suit
     */
    public Card(int r, String s){
        faceUp = false;
        rank = r;
        suit = s;
    }

    /**
     * Returns a character representing the card's rank.
     * 
     * @return rank as character
     */
    public String rankName(){
        switch(rank){
            case 1: return "a";
            case 10: return "t";
            case 11: return "j";
            case 12: return "q";
            case 13: return "k";
            default: return Integer.toString(rank);
        }
    }
    
    /**
     * Returns the file name of the card jpg
     * 
     * @return file name
     */
    public String getFileName(){
        return (faceUp) ? "cards/" + this.rankName() + suit + ".gif" : "cards/back.gif";
    }
    
    /**
     * Returns whether the card is face up or not.
     * 
     * @return  true if the card is face up; otherwise,
     *          false
     */
    public boolean isFaceUp(){
        return faceUp;
    }
    
    /**
     * Returns whether the card is a red (heart or diamond) card.
     * 
     * @return  true if the card is red; otherwise,
     *          false
     */
    public boolean isRed(){
        return suit.equals("d") || suit.equals("h");
    }
    
    /**
     * Returns the rank of the card as a number
     * 
     * @return rank number
     */
    public int getRank(){
        return rank;
    }
    
    /**
     * Returns the suit of the card as a string
     * 
     * @return suit
     */
    public String getSuit(){
        return suit;
    }
    
    /**
     * Flips the card up.
     */
    public void turnUp()
    {
        faceUp = true;
    }
    
    /**
     * Flips the card down.
     */
    public void turnDown()
    {
        faceUp = false;
    }
}
