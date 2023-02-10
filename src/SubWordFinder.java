
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SubWordFinder implements WordFinder{
    private ArrayList<ArrayList<String>> dictionary;
    private String alpha = "abcdefghijklmnopqrstuvwxyz";

    public SubWordFinder(){
        dictionary = new ArrayList<>();
        for(int i = 0; i < 26; i++){
            dictionary.add(new ArrayList<String>());
        }
        populateDictionary();
    }

    /**
     * Populates the dictionary from the text file contents
     * The dictionary object should contain 26 buckets, each
     * bucket filled with an ArrayList<String>
     * The String objects in the buckets are sorted A-Z because
     * of the nature of the text file words.txt
     */

    public void populateDictionary(){
        try {
            Scanner in = new Scanner(new File("new_scrabble.txt"));
            while (in.hasNext()) {
                String word = in.nextLine();
                int index = alpha.indexOf(word.substring(0,1));
                dictionary.get(index).add(word);

            }
            in.close();
            for (int i = 0; i < dictionary.size(); i++) {
                Collections.sort(dictionary.get(i));
            }
        } catch (Exception e) {
           System.out.println(e);}
    }

    /**
     * Retrieve all SubWord objects from the dictionary.
     * A SubWord is defined as a word that can be split into two
     * words that are also found in the dictionary.  The words
     * MUST be split evenly, e.g. no unused characters.
     * For example, "baseball" is a SubWord because it contains
     * "base" and "ball" (no unused characters)
     * To do this, you must look through every word in the dictionary
     * to see if it is a SubWord object
     *
     * @return An ArrayList containing the SubWord objects
     * pulled from the file words.txt
     */
    @Override
    //TODO: THIS
    public ArrayList<SubWord> getSubWords() {
        ArrayList<SubWord> subwords = new ArrayList<>();
        for(ArrayList<String> bucket : dictionary){
            for(String word :  bucket){
                String front = "", back = "";
                for(int i = 2; i < word.length() -1; i++){
                    front =  word.substring(0, i);
                    back = word.substring(i);
                    if (inDictionary(front) && inDictionary(back)) {
                        subwords.add(new SubWord(word, front, back));
                    }
                }
            }
        }
        Collections.sort(subwords);
        return subwords;
    }
    /**
     * Look through the entire dictionary object to see if
     * word exists in dictionary
     * @param word The item to be searched for in dictionary
     * @return true if word is in dictionary, false otherwise
     * NOTE: EFFICIENCY O(log N) vs O(N) IS A BIG DEAL HERE!!!
     * You MAY NOT use Collections.binarySearch() here; you must use
     * YOUR OWN DEFINITION of a binary search in order to receive
     * the credit as specified on the grading rubric.
     */

    @Override
    public boolean inDictionary(String word) {
        ArrayList<String> bucket = dictionary.get(alpha.indexOf(word.substring(0,1)));
        return binarySearch(bucket, 0, bucket.size()-1, word) >= 0;
    }

    int binarySearch(ArrayList<String> list, int left, int right, String word){
        if (right>= left){
            int mid = left + ((right-left)/2);
            if (list.get(mid).equals(word)){
                return mid;
            }
            return word.compareTo(list.get(mid))> 0 ? binarySearch(list, mid +1, right, word) :  binarySearch(list, left, mid-1, word);
        }
        return -1;
    }
    //TODO: THIS
    public static void main(String[] args) {
        SubWordFinder app = new SubWordFinder();
        ArrayList<SubWord> subwords = app.getSubWords();
        System.out.println("* List of SubWord objects in dictionary *");
        for(SubWord temp : subwords){
            System.out.println(temp);
        }
        System.out.println(subwords.size() + " total Subwords");
    }
}
