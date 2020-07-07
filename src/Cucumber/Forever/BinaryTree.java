package Cucumber.Forever;

public class BinaryTree {
    protected BinaryTree.TreeNode root = null;

    class TreeNode {
        public Integer value; // значение (ключ) узла
        public BinaryTree.TreeNode left; // левый потомок
        public BinaryTree.TreeNode right; // правый потомок

        public TreeNode(Integer value, BinaryTree.TreeNode left, BinaryTree.TreeNode right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        public TreeNode(Integer value) {
            this(value, null, null);
        }

        public Integer getValue() {
            return value;
        }

        public BinaryTree.TreeNode getLeft() {
            return left;
        }

        public BinaryTree.TreeNode getRight() {
            return right;
        }
    }

    private Integer fromStr(String s) {
        s = s.trim();
        if (s.length() > 0 && s.charAt(0) == '"') {
            s = s.substring(1);
        }
        if (s.length() > 0 && s.charAt(s.length() - 1) == '"') {
            s = s.substring(0, s.length() - 1);
        }

        return Integer.parseInt(s);
    }

    private static class IndexWrapper {
        int index = 0;
    }

    private void skipSpaces(String bracketStr, BinaryTree.IndexWrapper iw) {
        while (iw.index < bracketStr.length() && Character.isWhitespace(bracketStr.charAt(iw.index))) {
            iw.index++;
        }
    }

    private Integer readValue(String bracketStr, BinaryTree.IndexWrapper iw) {
        skipSpaces(bracketStr, iw);
        if (iw.index >= bracketStr.length()) {
            return null;
        }
        int from = iw.index;
        boolean quote = bracketStr.charAt(iw.index) == '"';
        if (quote) {
            iw.index++;
        }
        while (iw.index < bracketStr.length() && (
                quote && bracketStr.charAt(iw.index) != '"' ||
                        !quote && !Character.isWhitespace(bracketStr.charAt(iw.index)) && "(),".indexOf(bracketStr.charAt(iw.index)) < 0
        )) {
            iw.index++;
        }
        if (quote && bracketStr.charAt(iw.index) == '"') {
            iw.index++;
        }
        String valueStr = bracketStr.substring(from, iw.index);
        Integer value = fromStr(valueStr);
        skipSpaces(bracketStr, iw);
        return value;
    }


    private BinaryTree.TreeNode fromBracketStr(String bracketStr, BinaryTree.IndexWrapper iw) throws Exception {
        Integer parentValue = readValue(bracketStr, iw);
        BinaryTree.TreeNode parentNode = new BinaryTree.TreeNode(parentValue);
        if (bracketStr.charAt(iw.index) == '(') {
            iw.index++;
            skipSpaces(bracketStr, iw);
            if (bracketStr.charAt(iw.index) != ',') {
                BinaryTree.TreeNode leftNode = fromBracketStr(bracketStr, iw);
                parentNode.left = leftNode;
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) == ',') {
                iw.index++;
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) != ')') {
                BinaryTree.TreeNode rightNode = fromBracketStr(bracketStr, iw);
                parentNode.right = rightNode;
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) != ')') {
                throw new Exception(String.format("Ожидалось ')' [%d]", iw.index));
            }
            iw.index++;
        }

        return parentNode;
    }

    public int minLeftNode() {
        return searchMinLeftNode(root);
    }

    private int searchMinLeftNode(TreeNode node) {
        if (node.left != null) {
            return searchMinLeftNode(node.left);
        } else {
            return node.value;
        }
    }

    public void fromBracketNotation(String bracketStr) throws Exception {
        BinaryTree.IndexWrapper iw = new BinaryTree.IndexWrapper();
        BinaryTree.TreeNode root = fromBracketStr(bracketStr, iw);
        if (iw.index < bracketStr.length()) {
            throw new Exception(String.format("Ожидался конец строки [%d]", iw.index));
        }
        this.root = root;
    }

    public String toString () {
        String treeInString = "";
        String otherTreeInString = toString(root, treeInString);
        return treeInString + otherTreeInString;
    }

    private String toString (TreeNode node, String treeInString) {
        String peaceOfTreeInString = new String();
        peaceOfTreeInString += node.value;

        if (node.left != null)  {
            peaceOfTreeInString += " (";
            peaceOfTreeInString += toString(node.left, treeInString);
            if (node.right == null) {
                peaceOfTreeInString += ")";
            }
        }
        if (node.right != null) {
            if (node.left == null) {
                peaceOfTreeInString += " (";
            }
            peaceOfTreeInString += ", ";
            peaceOfTreeInString += toString(node.right, treeInString);
            peaceOfTreeInString += ")";
        }
        return peaceOfTreeInString;
    }

}