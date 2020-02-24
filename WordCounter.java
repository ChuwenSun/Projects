
import components.map.Map;
import components.map.Map1L;
import components.sequence.Sequence;
import components.sequence.Sequence1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

public final class WordCounter {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private WordCounter() {
        // no code needed here
    }
    
    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        String out = "";
        /*
         * Get the boolean value for temp that if the character on the first position is a separator
         */
        boolean temp = separators.contains(text.charAt(position));
        /*
         * get into the while loop, let the position increment starts, quit the loop if the position reach the last position of the string or the position reach a separator(or non-separator)
         */
        while (position < text.length()) {
            if(temp == separators.contains(text.charAt(position))) {
            	out = out + text.charAt(position);
            }else {
            	position = text.length();
            }
            position++;
        }
        return out;
    }
    
    
    private static void sortSequence(Sequence<String> s) {
        /*
         * Create a temporary sequence
         */
        Sequence<String> temp = s.newInstance();
        temp.transferFrom(s);
        String comp = "";
        int count = 0;
        while (temp.length() != 0) {
            /*
             * Add every element back to the sequence by alphabet sequence
             */
            int pos = 0;
            for (int i = 0; i < temp.length() - 1; i++) {
                comp = temp.entry(pos);
                if (comp.compareToIgnoreCase(temp.entry(i + 1)) > 0) {
                    pos = i + 1;
                }
            }
            s.add(count, temp.entry(pos));
            temp.remove(pos);
            count++;
        }
    }
    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
    	SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        /*
         * Ask the user for input file and output file
         */
        out.print("Please enter the name of the input file: ");
        String title = in.nextLine();
        SimpleReader input = new SimpleReader1L(title);
        out.print("Please enter the name of the output file: ");
        String outfile = in.nextLine();
        SimpleWriter output = new SimpleWriter1L(outfile);
        /*
         * Create a sequence to store all the words(no duplicates)
         */
        Sequence<String> words = new Sequence1L<String>();
        /*
         * Create a map to match words and the times they showed in the file
         */
        Map<String, Integer> amount = new Map1L<String, Integer>();
        /*
         * Create a Set with all the separators
         */
        Set<Character> separators = new Set1L<Character>();
        separators.add('\t');
        separators.add(' ');
        separators.add(',');
        separators.add('.');
        separators.add('-');
        separators.add('~');
        separators.add('?');
        separators.add('!');
        separators.add(';');
        separators.add(':');
        separators.add('/');
        separators.add('*');
        separators.add('"');
        separators.add('\'');
        separators.add('(');
        separators.add(')');
        /*
         * Get into the while loop to start adding elements to Sequence and Map
         */
        while(!input.atEOS()) {
            /*
             * Read a line
             */
        	 String line = input.nextLine();
            	 int pos = 0;
                 /*
                  * Get into the while loop to read every single words including separators
                  */
            	 while(pos < line.length()) {  
            	        /*
            	         * Get a word using this method
            	         */
            		 String part = nextWordOrSeparator(line, pos, separators);
            	        /*
            	         * add that word into Map and sequence if it is not a separator
            	         */
                		 if(!separators.contains(part.charAt(0)) ) { 
                		        /*
                		         * Change value in map if the word already has been detected
                		         */
                    		 if(amount.hasKey(part)) {
                    			 amount.replaceValue(part, amount.value(part) + 1);
                    		 }else {
                    		        /*
                    		         * Add a pair to the map and add the word to the sequence if the word is detected the first time
                    		         */
                    			 words.add(0, part); 
                    			 amount.add(part, 1);
                    		 }
                		 }
                	        /*
                	         * Try to read the next word
                	         */
            		 pos = pos + part.length();
            	 }
         }
        /*
         * Sort the sequence in alphabetical order
         */
         sortSequence(words);
         /*
          * Print html file header
          */
         output.println("<html>");
         output.println("<head>");
         output.println("<title>Words Counted in data/" + title + "</title>");
         output.println("</head>");
         output.println("<body>");
         output.println("<h2>Words Counted in data/" + title + "</h2>");
         output.println("<hr />");
         /*
          * print a table
          */
         output.println("<table border=\"1\">");
         output.println("<tr>");
         output.println("<th>Words</th>");
         output.println("<th>Counts</th>");
         output.println("</tr>");
         /*
          * use for loop to print every single word and how many times they showed in the input file
          */
         for(int i = 0; i < words.length(); i++) {
        	 output.println("<tr>");
        	 output.println("<td>" + words.entry(i) + "</td>");
        	 output.println("<td>" + amount.value(words.entry(i)) + "</td>");
        	 output.println("</tr>");       	 
         }
         /*
          * finish printing and close streams
          */
         output.println("</table>");
         output.println("</body>");
         output.println("</html>");
         input.close();
         output.close();
    }    
}
