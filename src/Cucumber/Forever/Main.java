package Cucumber.Forever;

public class Main {

    public static void main(String[] args) throws Exception {
        BinaryTree tree = new BinaryTree();
        String treeInString = "53 (15 (3 (, 4 (, 5)), 24 (, 34 (29 (25), 47 (36 (, 38), 51)))), 77 (60 (58 (57), 64 (, 75 (67))), 99 (87 (78 (, 86), 94))))";
        System.out.println("\nБинарное дерево в скобочной структуре: \n" + treeInString);
        tree.fromBracketNotation(treeInString);
        int minNode = tree.minLeftNode();
        System.out.println("Наименьший левый потомок: " + minNode);
    }
}
