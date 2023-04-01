import java.util.*;

public class Solitaire
{
    public static void main(String[] args)
    {
        new Solitaire();
    }

    private Stack<Card> stock;
    private Stack<Card> waste;
    private Stack<Card>[] foundations;
    private Stack<Card>[] piles;
    private SolitaireDisplay display;

    public Solitaire()
    {
        foundations = new Stack[4];
        for (int i = 0; i < 4; i++) foundations[i] = new Stack<Card>();
        piles = new Stack[7];
        for (int i = 0; i < 7; i++) piles[i] = new Stack<Card>();

        stock = new Stack<Card>();
        waste = new Stack<Card>();

        display = new SolitaireDisplay(this);

        createStock();
        deal();
    }

    //creates a standard deck and then distributes cards randomly
    //to the stock.
    public void createStock(){
        String[] suit = {"h","s","c","d"};
        ArrayList<Card> deck = new ArrayList<Card>();
        for (int s = 0; s < 4; s++)
            for (int r = 1; r <= 13; r++)   deck.add(new Card(r, suit[s]));
        while (deck.size() > 0)
            stock.push(deck.remove((int)(Math.random()*deck.size())));
    }

    //deals the cards in the stock to the 7 piles
    public void deal(){
        for (int p = 0; p < 7; p++){
            for (int i = 0; i <= p; i++)
                piles[p].push(stock.pop());
            piles[p].peek().turnUp();
        }
    }

    //deals three cards from the stock to the waste
    public void dealThreeCards(){
        int count = 0;
        while (stock.size() > 0 && count < 3){
            waste.push(stock.pop());
            waste.peek().turnUp();
            count++;
        }
    }

    //resets the stock and waste piles
    public void resetStock(){
        while (waste.size() > 0){
            stock.push(waste.pop());
            stock.peek().turnDown();
        }
    }

    //returns the card on top of the stock,
    //or null if the stock is empty
    public Card getStockCard()
    {
        return (stock.isEmpty()) ? null : stock.peek();
    }

    //returns the card on top of the waste,
    //or null if the waste is empty
    public Card getWasteCard()
    {
        return (waste.isEmpty()) ? null : waste.peek();
    }

    //precondition:  0 <= index < 4
    //postcondition: returns the card on top of the given
    //               foundation, or null if the foundation
    //               is empty
    public Card getFoundationCard(int index)
    {
        return (foundations[index].isEmpty()) ? null : foundations[index].peek();
    }

    //precondition:  0 <= index < 7
    //postcondition: returns a reference to the given pile
    public Stack<Card> getPile(int index)
    {
        return (piles[index].isEmpty()) ? null : piles[index];
    }

    //called when the stock is clicked
    public void stockClicked()
    {
        if (display.isWasteSelected() || display.isPileSelected()) return;
        if(stock.isEmpty()) resetStock();
        else dealThreeCards();
        System.out.println("stock clicked");
    }

    //called when the waste is clicked
    public void wasteClicked()
    {
        //IMPLEMENT ME
        if(display.isWasteSelected())
            display.unselect();
        else if (!waste.isEmpty() && !display.isWasteSelected() && !display.isPileSelected())
            display.selectWaste();
        System.out.println("waste clicked");
    }

    //precondition:  0 <= index < 4
    //called when given foundation is clicked
    public void foundationClicked(int index)
    {
        if(display.isWasteSelected())
            if(canAddToFoundation(waste.peek(), index)){
                foundations[index].push(waste.pop());
                display.unselect();
            }
        if(display.isPileSelected())
        {
            Card bottom = piles[display.selectedPile()].peek();
            if (canAddToFoundation(bottom, index)){
                foundations[index].push(piles[display.selectedPile()].pop());
                if(!piles[display.selectedPile()].isEmpty())
                    piles[display.selectedPile()].peek().turnUp();
            }
            display.unselect();
        }
        System.out.println("foundation #" + index + " clicked");
    }
    
    //precondition:  0 <= index < 7
    //called when given pile is clicked
    public void pileClicked(int index)
    {
        if (!display.isPileSelected()){
            if(display.isWasteSelected() && canAddToPile(waste.peek(), index)){
                piles[index].push(waste.pop());
                display.unselect();
            }
            else if(!piles[index].isEmpty() && !piles[index].peek().isFaceUp()) piles[index].peek().turnUp();
            else  display.selectPile(index);
        }
        else if (display.isPileSelected()){
            if(display.selectedPile() != index){
                Stack<Card> removed = removeFaceUpCards(display.selectedPile());
                if (canAddToPile(removed.peek(), index)){
                    addToPile(removed, index);
                    if(!piles[display.selectedPile()].isEmpty())
                        piles[display.selectedPile()].peek().turnUp();
                }
                else addToPile(removed, display.selectedPile());
            }
            display.unselect();
        }
        System.out.println("pile #" + index + " clicked");
    }

    //precondition:  0 <= index < 7
    //postcondition: Returns true if the given card can be
    //               legally moved to the top of the given
    //               pile
    private boolean canAddToPile(Card card, int index){
        if(piles[index].isEmpty()) return (card.getRank()== 13) ? true : false;
        return (piles[index].peek().isFaceUp()) ? 
            piles[index].peek().getRank() == 1 + card.getRank() && 
            piles[index].peek().isRed() != card.isRed() 
        : false;
    }

    //precondition:  0 <= index < 7
    //postcondition: Removes all face-up cards on the top of
    //               the given pile; returns a stack
    //               containing these cards
    private Stack<Card> removeFaceUpCards(int index){
        Stack<Card> up = new Stack<Card>();
        while (!piles[index].isEmpty() && piles[index].peek().isFaceUp())
            up.push(piles[index].pop());
        return up;
    }

    //precondition:  0 <= index < 7
    //postcondition: Removes elements from cards, and adds
    //               them to the given pile.
    private void addToPile(Stack<Card> cards, int index){
        while (!cards.isEmpty()) piles[index].push(cards.pop());
    }

    //precondition:  0 <= index < 4
    //postcondition: Returns true if the given card can be
    //               legally moved to the top of the given
    //               foundation
    private boolean canAddToFoundation(Card card, int index){
        if(foundations[index].isEmpty()) return card.getRank() == 1;
        return  foundations[index].peek().getRank() == card.getRank() - 1 && 
        foundations[index].peek().getSuit().equals(card.getSuit());
    }
}