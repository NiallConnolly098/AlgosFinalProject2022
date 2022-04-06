import java.util.ArrayList;
public class Tree {
	private TreeNode root;
	private ArrayList<String> store;
	
	public Tree() {
		root = null;
	}
	
	public Boolean isEmpty() {
		return root == null;
	}
	
	public void makeEmpty() {
		root = null;
	}
	
	public void addWord(String word) {
		root = addWord(root, word.toCharArray(), 0);
	}
	
	public TreeNode addWord(TreeNode i, char[] word, int j) {
		if(i == null) i = new TreeNode(word[j]);
		if (word[j] < i.Data) {
			i.left = addWord(i.left, word, j);
		}
		else if(word[j] > i.Data) {
			i.right = addWord(i.right, word, j);
		}
		else {
			if(j+1 < word.length) {
			i.middle = addWord(i.middle, word, j+1);
			}
			else i.End = true;
		}
		return i;
	}
	
	public void deleteWord(String word) {
		deleteWord(root, word.toCharArray(), 0);
	}
	
	private void deleteWord(TreeNode i, char[] word, int j) {
		if(i == null) return;
		if(word[j]<i.Data) {
			deleteWord(i.left, word, j);
		}
		else if(word[j]>i.Data) {
			deleteWord(i.right, word, j);
		}
		else {
			if(i.End && j==word.length-1) {
				i.End = false;
			}
			else if(j+1 < word.length) {
				deleteWord(i.right, word, j);
			}
		}
	}
	
	
	public String[] searchWord(String word) {
        String[] empty = new String[0];
        try {
            if (word == null) {
                return empty;
            }
        } 
        catch (Exception e) {
            System.out.println(e);
        }
        try {
            if (word.charAt(0) == ' ') {
                return empty;
            }
        } 
        catch (Exception e) {
            System.out.println(e);
        }
        StringBuilder sb = new StringBuilder();
        TreeNode Root = toLastNode(root, word.toCharArray(), 0);
        suggestions(Root, "", word, sb);
        if (sb.length() < 1) {
        	System.out.println("no result");
            return empty;
        }
        return sb.toString().split("\n");
    }
	
	public String toString() {
		store = new ArrayList<String>();
		moveThrough(root, "");
		return "\nTernary Search Tree: " + store;
	}
	
	public void moveThrough(TreeNode i, String j) {
		if(i!=null) {
			moveThrough(i.left, j);
			j=j+i.Data;
			if(i.End) store.add(j);
			moveThrough(i.middle, j);
			j = j.substring(0, j.length()-1);
			moveThrough(i.right, j);
		}
	}
	
	private TreeNode toLastNode(TreeNode node, char[] word, int i) {
		if(node==null) return null;
		if(word[i]<node.Data) return toLastNode(node.left, word, i);
		else if(word[i]>node.Data) return toLastNode(node.right, word, i);
		else {
			if(i==word.length-1) return node;
			else return toLastNode(node.middle, word, i);
		}
	}
	
	private void suggestions(TreeNode node, String i, String j, StringBuilder sb) {
		if(node!=null) {
			suggestions(node.left, i, j, sb);
			i = i + node.Data;
			if(node.End) {
				if(j.length()==1) {
					if(j.equals(i.substring(0,1))) {
						sb.append(j + i.substring(1) + "\n");
					}
				}
				else sb.append(j + i.substring(1) + "\n");
			}
			suggestions(node.middle, i, j, sb);
			i = i.substring(0, i.length()-1);
			suggestions(node.right, i, j, sb);
		}
	}
}
