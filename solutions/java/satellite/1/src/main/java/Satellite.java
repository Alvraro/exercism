import java.util.List;

public class Satellite {
    public Tree treeFromTraversals(List<Character> preorderInput, List<Character> inorderInput) {
        return treeFromTraversals(preorderInput, inorderInput, true);
    }
    
    private Tree treeFromTraversals(List<Character> preorderInput, List<Character> inorderInput, boolean checkInput) {
        if(checkInput) {
            // Validate that the input lists have the same length
            if (preorderInput.size() != inorderInput.size()) {
                throw new IllegalArgumentException("traversals must have the same length");
            }
       
            // Validate that the input lists contain the same elements
            preorderInput.stream().forEach(c -> {
                int first = inorderInput.indexOf(c);
                if (first == -1) {
                    throw new IllegalArgumentException("traversals must have the same elements");
                }
                if (inorderInput.lastIndexOf(c) != first) {
                    throw new IllegalArgumentException("traversals must contain unique items");
                }
                if(preorderInput.lastIndexOf(c) != preorderInput.indexOf(c)) {
                    throw new IllegalArgumentException("traversals must contain unique items");
                }
            });
        }

        // Base case: if the input lists are empty, return an empty tree
        if (preorderInput.isEmpty()) {
            return new Tree(null);
        }

        // Root splits the inorder list into left and right subtrees
        Node root = new Node(preorderInput.get(0));
        int index = inorderInput.indexOf(root.value);
        
        List<Character> leftInorder = inorderInput.subList(0, index);
        List<Character> rightInorder = inorderInput.subList(index+1, inorderInput.size());
        
        List<Character> leftPreorder = preorderInput.subList(1, leftInorder.size() + 1);
        List<Character> rightPreorder = preorderInput.subList(leftInorder.size() + 1, preorderInput.size());

        // Recursively build the left and right subtrees
        Tree left = treeFromTraversals(leftPreorder, leftInorder, false);
        Tree right = treeFromTraversals(rightPreorder, rightInorder, false);
        root.left = left.getRoot();
        root.right = right.getRoot();

        return new Tree(root);
    }
}
