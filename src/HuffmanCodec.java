import java.io.*;
import java.util.*;

// Gregory Halverson
// Pierce College
// CS 532
// Spring 2014

// Huffman Encoder/Decoder
// Uses Huffman tree to build code table and compress and decompress sets of values or files
@SuppressWarnings("unchecked")
public class HuffmanCodec<T extends Comparable<? super T>>
{
    // Member data
    private FrequencyAnalyzer analyzer = null;
    private FrequencyAnalyzer.FrequencyDistribution<T> frequencyDistribution = null;
    private Tree<T> tree = null;
    private CodeTable<T> codeTable = null;
    private FileSize lastFile = null;

    @SuppressWarnings("unchecked")
    public static class Tree<T extends Comparable<? super T>> implements Comparable<Tree>, Serializable
    {
        // Used to report if the tree is empty
        private boolean empty;

        // Probability of symbol or accumulated probability of children
        protected Double probability;

        // Holds symbol and associated probability
        public static class Leaf<T extends Comparable<? super T>> extends Tree<T>
        {
            // Symbol encapsulated in the leaf
            private T data;

            // Initialize member data
            public Leaf(T data, Double weight)
            {
                super(weight);
                this.data = data;
            }

            // Used to determine if a node is a leaf
            @Override
            public boolean isLeaf()
            {
                return true;
            }

            // Returns symbol represented by the leaf
            public T getData()
            {
                return data;
            }

            // Output
            public String toString()
            {
                return data + " (" + StringFormat.percent(probability) + ")";
            }
        }

        // Holds accumulated probability and child nodes
        public static class Node<T extends Comparable<? super T>> extends Tree<T>
        {
            // Child nodes
            private Tree<T> left, right;

            // Assign child nodes and accumulate probability
            public Node(Tree left, Tree right)
            {
                super(left.probability + right.probability);
                this.left = left;
                this.right = right;
            }

            // Used to determine if a node is a leaf
    /*        @Override
            public boolean isLeaf()
            {
                return false;
            }
    */

            // Traverse to left child
            public Tree getLeft()
            {
                return left;
            }

            // Traverse to right child
            public Tree getRight()
            {
                return right;
            }

            // Output
            public String toString()
            {
                CompleteBinaryTree<String> output_tree = new CompleteBinaryTree<String>();

                // Level order traversal

                Queue this_level = new LinkedList<Tree<T>>();
                ArrayList<Tree<T>> next_level = new ArrayList<Tree<T>>();
                int level_size = 1;

                // Start queue with root node
                this_level.add(this);

                // Level order traversal by queue
                while (!this_level.isEmpty())
                {
                    // Pop current node from queue
                    Tree<T> current_node = (Tree<T>)this_level.remove();

                    if (current_node.isEmpty())
                    {
                        // Fill in dummy nodes in the output tree
                        next_level.add(new Tree<T>());
                        next_level.add(new Tree<T>());
                        output_tree.insert("");
                    }
                    else if (current_node.isLeaf())
                    {
                        // Insert symbol and probability to output tree

                        Tree.Leaf<T> leaf = (Tree.Leaf<T>)current_node;

                        next_level.add(new Tree<T>());
                        next_level.add(new Tree<T>());

                        output_tree.insert(leaf.toString());
                    }
                    else
                    {
                        // Insert accumulated probability into output tree and traverse Huffman tree

                        Tree.Node<T> node = (Tree.Node<T>)current_node;
                        output_tree.insert(StringFormat.percent(node.getProbability()));

                        if (node.getLeft() == null)
                            next_level.add(new Tree<T>());
                        else
                            next_level.add(node.getLeft());

                        if (node.getRight() == null)
                            next_level.add(new Tree<T>());
                        else
                            next_level.add(node.getRight());
                    }

                    if (this_level.isEmpty())
                    {
                        // Check if next level is empty
                        boolean empty_level = true;
                        for (int i = 0; i < next_level.size(); i++)
                            if (!next_level.get(i).isEmpty())
                                empty_level = false;

                        // Queue level
                        if (!empty_level)
                        {
                            for (int i = 0; i < next_level.size(); i++)
                            {
                                this_level.add(next_level.get(i));
                            }

                            next_level.clear();

                            level_size *= 2;
                        }
                    }
                }

                // Render output
                return output_tree.printTree();
            }
        }

        // Used to build tree from set of symbols and probabilities
        public static class Set<T extends Comparable<? super T>>
        {
            //ArrayList<HuffmanCodec.Tree<T>> trees;
            private PriorityQueue<Tree<T>> trees;

            // Initialize priority queue
            Set()
            {
                trees = new PriorityQueue<Tree<T>>();
            }

            // Initialize from extant frequency distribution of symbols and probabilities
            Set(FrequencyAnalyzer.FrequencyDistribution<T> frequency_distribution)
            {
                // Allocate new priority queue
                trees = new PriorityQueue<Tree<T>>();

                // Iterate caseSensitiveSet and push values to singular trees in queue
                for (Map.Entry<T, Double> entry : frequency_distribution.entrySet())
                    add(entry.getKey(), entry.getValue());
            }

            // Return the number of trees held in the set
            public int getSize()
            {
                return trees.size();
            }

            // Insert a symbol into the set
            public void add(T value, Double weight)
            {
                trees.enqueue(new Leaf<T>(value, weight), weight);
            }

            // Join two smallest probabilities in Huffman tree
            public void joinSmallest()
            {
                if (trees.size() >= 2)
                {
                    // Pop two smallest nodes
                    Tree<T> node1 = trees.dequeue();
                    Tree<T> node2 = trees.dequeue();
                    Node<T> combined_node;

                    // Accumulate probabilities into new node
                    // Extra comparison used to generate keys in numeric order
                    if (node1.probability < node2.probability)
                        combined_node = new Node<T>(node2, node1);
                    else
                        combined_node = new Node<T>(node1, node2);

                    // Push accumulated node to the queue
                    trees.enqueue(combined_node, combined_node.getProbability());
                }
            }

            // Build Huffman tree from set of probabilities
            public Tree<T> generateTree()
            {
                // Build tree by joining the two smallest probabilities until a single tree remains
                while (trees.size() > 1)
                    joinSmallest();

                // Return blank tree on error
                if (trees.size() == 1)
                    return trees.dequeue();
                else
                    return new Tree<T>();
            }

            // Output trees juxtaposed horizontally
            public String toString()
            {
                if (trees.size() > 1)
                {
                    String [] tree_strings = new String[trees.size()];

                    for (int i = 0; i < trees.size(); i++)
                        tree_strings[i] = trees.get(i).toString();

                    return StringFormat.joinBlocks(tree_strings, " ");
                }
                else
                {
                    return trees.peek().toString();
                }
            }
        }

        // Default constructor: empty tree
        Tree()
        {
            empty = true;
        }

        // Assign probability
        Tree(Double probability)
        {
            empty = false;
            this.probability = probability;
        }

        // Check if subtree is empty
        public boolean isEmpty()
        {
            return empty;
        }

        // Used to determine if a node is a leaf
        public boolean isLeaf()
        {
            return false;
        }

        // Return probability of symbol or accumulated probability of subtree
        public Double getProbability()
        {
            return probability;
        }

        // Implements Comparable for sorting nodes
        @Override
        public int compareTo(Tree o) {
            return (int)(probability - o.probability);
        }

        // Implemented in Node and Leaf
        @Override
        public String toString()
        {
            return "";
        }
    }

    // Represents a Huffman code as a wrapper of BitSet
    public static class Code implements Comparable<Code>, Serializable
    {
        // Number of bits encoded
        private int size;

        // Buffer of bits
        private BitSet data;

        // Default constructor: zero-length code
        Code()
        {
            data = new BitSet();
            size = 0;
        }

        // Return number of bits in code
        public int getSize()
        {
            return size;
        }

        // Return bit at zero-based index
        public boolean getBit(int index)
        {
            return data.get(index);
        }

        // Append given bit as boolean to end of code
        public void setNext(boolean bit)
        {
            // Set bit in buffer
            data.set(size, bit);

            // Increment size
            size++;
        }

        // Agglutinate another Huffman code to the end of this one
        public void append(Code other)
        {
            // Iterate bits of the other code and append them to this one
            for (int i = 0; i < other.getSize(); i++)
                setNext(other.getBit(i));
        }

        // Return a copy of this code
        public Code clone()
        {
            Code clone = new Code();
            clone.data.or(data);
            clone.size = size;
            return clone;
        }

        // Return buffer as byte array
        // Beware of trailing bits at end of array
        public byte [] toByteArray()
        {
            return data.toByteArray();
        }

        // Compare by length, then by numeric value
        @Override
        public int compareTo(Code other)
        {
            // Compare by length of code
            int result = size - other.size;

            // Compare as string
            if (result == 0)
                return this.toString().compareTo(other.toString());

            return result;
        }

        // Output
        @Override
        public String toString()
        {
            StringBuilder output = new StringBuilder();

            // Iterate bits and output 1 for true and 0 for false
            for (int i = 0; i < size; i++)
                output.append((data.get(i) ? "1" : "0"));

            return output.toString();
        }
    }

    // Used to assign Huffman codes to symbols
    public static class CodeTable<T extends Comparable<? super T>> extends TreeMap<T, Code>
    {
        // Initialize path at root of given tree and begin recursive traversal to build code table
        public void generate(Tree<T> tree)
        {
            Code path = new Code();

            generate(tree, path);
        }

        // Recursively traverse tree to build code table
        private void generate(Tree<T> tree, Code path)
        {
            //System.out.println("path: " + path);
            if (!tree.isEmpty())
            {
                // Check if the path reached a leaf yet
                if (tree.isLeaf())
                {
                    // Interface as leaf
                    Tree.Leaf<T> leaf = (Tree.Leaf<T>)tree;

                    // Push symbol and code to code table
                    this.put(leaf.data, path.clone());
                }
                else
                {
                    // Interface as node
                    Tree.Node<T> node = (Tree.Node<T>)tree;

                    // Bifurcate path

                    // Traverse to left child
                    Code path_left = path.clone();
                    path_left.setNext(false);
                    generate(((Tree.Node<T>) tree).getLeft(), path_left);

                    // Traverse to right child
                    Code path_right = path.clone();
                    path_right.setNext(true);
                    generate(((Tree.Node<T>) tree).getRight(), path_right);
                }
            }
        }

        // Print symbols and keys in vertical table
        public String printVertical()
        {
            StringBuilder output = new StringBuilder();

            for (Map.Entry<T, Code> entry : entrySet())
                output.append(entry.getKey() + ": " + entry.getValue().toString() + System.getProperty("line.separator"));

            return output.toString();
        }

        // Output
        @Override
        public String toString()
        {
            return printVertical();
        }
    }

    // Used to differentiate amount of space used by encoding tree and data in file operation
    public static class FileSize
    {
        // Member data
        public int tree = 0; // Bytes written to store tree
        public int data = 0; // Bytes written to store data
        public int total = 0; // Total bytes written
        public int code = 0; // Minimum number of bytes required to hold Huffman code;

        // Output
        public String toString()
        {
            return "(" + StringFormat.fileSize(tree) + " tree, " + StringFormat.fileSize(data) + " data, " + StringFormat.fileSize(total) + " total)";
        }
    }

    // Default constructor: empty codec
    // Tree and code table will be built by frequency analysis of input at time of encoding
    HuffmanCodec()
    {
        tree = null;
        codeTable = null;
    }

    // Build tree and code table from set of symbols and probabilities
    HuffmanCodec(FrequencyAnalyzer.FrequencyDistribution<T> frequencyDistribution)
    {
        this.frequencyDistribution = frequencyDistribution;
        codeTable = new CodeTable<T>();
        setFrequencyDistribution(frequencyDistribution);
    }

    // Encode a set of symbols as an agglutinated Huffman code
    public Code encode(T[] symbols)
    {
        // Allocate Huffman code
        Code code = new Code();

        // If the code table hasn't already been built,
        // build a new tree and code table by applying
        // the frequency analyzer to input
        if (codeTable == null)
            analyze(symbols);

        // Iterate set of symbols and agglutinate Huffman code
        for (int s = 0; s < symbols.length; s++)
            code.append(codeTable.get(symbols[s]));

        // Return Huffman code
        return code;
    }

    // Decodes a Huffman code into a list of symbols by traversing a Huffman tree
    // Codec MUST be initialized by the same tree that was used to encode
    public ArrayList<T> decode(Code code)
    {
        // Allocate resulting list of symbols
        ArrayList<T> symbols = new ArrayList<T>();

        // Iterate code
        int i = 0;

        while (i < code.getSize())
        {
            // Start at root of tree
            Tree<T> current_node = tree;

            // Traverse to leaf, stop if code stream is cut off
            while (!current_node.isLeaf() && i < code.getSize())
            {
                Tree.Node<T> node = (Tree.Node<T>)current_node;
                boolean bit = code.getBit(i++);

                // Traverse to the left if bit in Huffman code is 0, right if it's 1
                if (bit)
                    current_node = node.getRight();
                else
                    current_node = node.getLeft();
            }

            // Push symbol once leaf is found
            if (current_node.isLeaf())
            {
                Tree.Leaf<T> leaf = (Tree.Leaf<T>)current_node;
                symbols.add(leaf.getData());
            }
        }

        // Return decoded symbols
        return symbols;
    }

    // Encode set of symbols to file
    // File will include the Huffman tree used to encode the data
    public FileSize encodeFile(T[] output_symbols, String filename) throws IOException
    {
        try
        {
            // Open file
            FileSize file_size = new FileSize();
            FileOutputStream output_file = new FileOutputStream(filename);
            ByteArrayOutputStream output_byte_stream = new ByteArrayOutputStream();
            ObjectOutputStream output_stream = new ObjectOutputStream(output_byte_stream);

            // Encode symbols
            Code code = encode(output_symbols);
            file_size.code = (int)Math.ceil(code.getSize() / 8.0);

            // Write tree to buffer
            output_stream.writeObject(tree);
            file_size.tree = output_byte_stream.size();

            // Write encoded data to buffer
            output_stream.writeObject(code);
            file_size.data = output_byte_stream.size() - file_size.tree;

            // Write buffer to file
            byte[] byte_array = output_byte_stream.toByteArray();
            output_file.write(byte_array);

            // Close file
            output_byte_stream.close();
            output_stream.close();
            output_file.close();

            // Return file size
            file_size.total = byte_array.length;
            lastFile = file_size;
            return file_size;
        }
        catch (IOException e)
        {
            throw e; // Tell calling function that file creation failed
        }
    }

    // Decode Huffman tree and code from file into list of symbols
    public ArrayList<T> decodeFile(String filename) throws IOException, ClassNotFoundException
    {
        try
        {
            // Open file
            FileInputStream input_file = new FileInputStream(filename);
            ObjectInputStream input_stream = new ObjectInputStream(input_file);

            // Retrieve tree from file
            tree = (Tree<T>)input_stream.readObject();

            // Build code table
            codeTable = new CodeTable<T>();
            codeTable.generate(tree);

            // Retrieve encoded data from file
            Code code = (Code)input_stream.readObject();

            // Close file
            input_stream.close();
            input_file.close();

            // Decode data and return list of symbols
            return decode(code);
        }
        catch (IOException e)
        {
            throw e; // Unable to open file
        }
        catch (ClassNotFoundException e)
        {
            throw e; // Unable to read properly written tree or data
        }
    }

    public FrequencyAnalyzer<T> getAnalyzer()
    {
        return analyzer;
    }

    public FrequencyAnalyzer.FrequencyDistribution<T> getFrequencyDistribution()
    {
        return frequencyDistribution;
    }

    // Return Huffman tree
    public Tree<T> getTree()
    {
        return tree;
    }

    // Return code table
    public CodeTable<T> getCodeTable()
    {
        return codeTable;
    }

    // Return size details of last file written
    public FileSize lastFileSize()
    {
        return lastFile;
    }

    // Use frequency analyzer to build Huffman tree and code table
    public void analyze(T[] input)
    {
        analyzer = new FrequencyAnalyzer<T>(input);
        FrequencyAnalyzer.FrequencyDistribution<T> frequency_distribution = analyzer.getFrequencyDistribution();
        setFrequencyDistribution(frequency_distribution);
    }

    // Build Huffman tree and code table from set of symbols and probabilities
    public void setFrequencyDistribution(FrequencyAnalyzer.FrequencyDistribution<T> frequency_distribution)
    {
        this.frequencyDistribution = frequency_distribution;
        Tree.Set<T> set = new Tree.Set<T>(frequency_distribution);
        tree = set.generateTree();
        codeTable = new CodeTable<T>();
        codeTable.generate(tree);
    }
}